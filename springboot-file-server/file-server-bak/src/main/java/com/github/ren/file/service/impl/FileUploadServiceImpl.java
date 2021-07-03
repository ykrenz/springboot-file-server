package com.github.ren.file.service.impl;

import cn.hutool.core.util.StrUtil;
import cn.hutool.crypto.digest.MD5;
import com.github.ren.file.chunk.Chunk;
import com.github.ren.file.chunk.ChunkHandler;
import com.github.ren.file.clients.FileClient;
import com.github.ren.file.clients.UploadPart;
import com.github.ren.file.entity.TbFile;
import com.github.ren.file.entity.TbFileInfo;
import com.github.ren.file.ex.ChunkException;
import com.github.ren.file.ex.UploadException;
import com.github.ren.file.lock.FileLock;
import com.github.ren.file.model.ErrorCode;
import com.github.ren.file.model.request.CheckChunkRequest;
import com.github.ren.file.model.request.ChunkMergeRequest;
import com.github.ren.file.model.request.ChunkRequest;
import com.github.ren.file.model.result.CheckFileResult;
import com.github.ren.file.model.result.ChunkUploadResult;
import com.github.ren.file.model.result.FileUploadResult;
import com.github.ren.file.service.FileServerHelper;
import com.github.ren.file.service.FileUploadService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author Mr Ren
 * @Description:
 * @date 2021/4/10 23:26
 */
@Service("SimpleUploadServiceImpl")
@Slf4j
public class FileUploadServiceImpl implements FileUploadService {

    private final ChunkHandler handler;

    private final FileLock lock;

    @Autowired
    private FileDbServiceImpl fileDbService;

    @Autowired
    private FileClient fileClient;

    public FileUploadServiceImpl(FileServerHelper fileServerHelper) {
        handler = fileServerHelper.getChunkHandler();
        lock = fileServerHelper.getFileLock();
    }

    @Override
    public FileUploadResult upload(MultipartFile file) {
        int large = 5242880;
        long size = file.getSize();
        String filename = file.getOriginalFilename();
        if (size > large) {
            throw new UploadException(ErrorCode.FILE_TO_LARGE);
        }
        String md5 = null;
        try (InputStream inputStream = file.getInputStream()) {
            md5 = MD5.create().digestHex(file.getInputStream());
            log.info("小文件上传开始md5={}", md5);
            //加入锁 此方法保证简单上传文件唯一性
            lock.lock(md5);
            log.info("小文件上传获取到锁md5={}", md5);

            TbFile tbFile = fileDbService.getTbFile(md5);

            if (tbFile != null) {
                boolean exist = fileClient.isExist(tbFile.getPath());
                if (exist) {
                    //保存文件信息表并返回
                    TbFileInfo tbFileInfo = fileDbService.saveInfo(md5, filename);
                    return FileUploadResult.builder()
                            .id(tbFileInfo.getId())
                            .filename(tbFileInfo.getFilename())
                            .filesize(tbFile.getFilesize())
                            .md5(tbFile.getMd5())
                            .filepath(tbFile.getPath())
                            .accessPath(fileClient.getAccessPath(tbFile.getPath())).build();
                }
                //删除脏数据
                fileDbService.deleteTbFile(md5);
            }

            String objectName = createObjectName(filename, md5);
            String path = fileClient.uploadFile(inputStream, objectName);

            fileDbService.saveFile(md5, path, size);
            TbFileInfo tbFileInfo = fileDbService.saveInfo(md5, filename);

            String fileInfoId = tbFileInfo.getId();
            return FileUploadResult
                    .builder()
                    .id(fileInfoId)
                    .md5(md5)
                    .filename(filename)
                    .filesize(size)
                    .filepath(path)
                    .accessPath(fileClient.getAccessPath(path))
                    .build();

        } catch (IOException e) {
            log.error("上传出现异常", e);
            throw new UploadException(ErrorCode.UPLOAD_ERROR);
        } finally {
            if (md5 != null) {
                lock.unlock(md5);
                log.info("小文件上传释放锁md5={}", md5);
            }
        }
    }

    @Override
    public CheckFileResult check(CheckChunkRequest request) {
        String md5 = request.getMd5();
        String filename = request.getFilename();
        //秒传功能
        //判断数据库文件是否存在
        TbFile tbFile = fileDbService.getTbFile(md5);
        if (tbFile != null) {
            //判断服务器文件是否存在
            boolean exist = fileClient.isExist(tbFile.getPath());
            if (exist) {
                //保存文件信息表并返回
                TbFileInfo tbFileInfo = fileDbService.saveInfo(md5, filename);
                FileUploadResult uploadResult = FileUploadResult.builder()
                        .id(tbFileInfo.getId())
                        .filename(tbFileInfo.getFilename())
                        .filesize(tbFile.getFilesize())
                        .md5(tbFile.getMd5())
                        .filepath(tbFile.getPath())
                        .accessPath(fileClient.getAccessPath(tbFile.getPath())).build();
                return new CheckFileResult(true, uploadResult);
            }
            //删除脏数据
            fileDbService.deleteTbFile(md5);
        }

        //断点续传 查询所有已经上传的分块给到客户端
        List<Chunk> chunks = handler.getChunks(md5);
        List<Integer> chunkNumbers = chunks.stream()
                .map(Chunk::getNumber)
                .collect(Collectors.toList());
        return new CheckFileResult(false, chunkNumbers);
    }

    @Override
    public ChunkUploadResult uploadChunk(ChunkRequest request, MultipartFile file) {
        String md5 = request.getMd5();
        log.info("文件分块上传开始request={}", request);
        Integer chunkNumber = request.getChunkNumber();
        //上传分片操作 加锁 lock为md5值和分块索引 保证多线程同时传一个文件分片是完整的
        //注意这里 上传完分片 merge操作需要加文件锁 这里是分片锁 不然会文件表会有多条 发生异常
        String lockKey = md5 + StrUtil.UNDERLINE + chunkNumber;
        Chunk chunk = handler.create(md5, chunkNumber, file.getSize());
        try {
            lock.lock(lockKey);
            log.info("文件分片获取锁md5={}", md5);
            //判断是否已经传过该分片 逻辑:文件存在且当前分片大小和本地大小一致
            //如果其它线程已经合并上传完 那么之前的分片就被删掉了 这里会上传部分脏文件分片到服务器 需要在合并的时候判断并删除
            if (!handler.isUpload(chunk)) {
                handler.upload(chunk, file.getInputStream());
                log.info("文件分片上传完毕chunk={}", chunk);
            }
            log.info("文件分片已经存在chunk={}", chunk);
            return new ChunkUploadResult(md5, chunk);
        } catch (IOException e) {
            log.error("上传分片异常", e);
            handler.deleteChunk(chunk);
            throw new ChunkException(ErrorCode.UPLOAD_CHUNK_ERROR);
        } finally {
            lock.unlock(lockKey);
            log.info("文件分片释放锁md5={}", md5);
        }
    }

    @Override
    public FileUploadResult merge(ChunkMergeRequest request) {
        String md5 = request.getMd5();
        String filename = request.getFilename();
        log.info("文件合并开始md5={}", md5);
        try {
            //这里注意 不能在最上面加事务 不然事务没有提交 其它线程已经在另一个事务中了 就获取不到插入的数据
            lock.lock(md5);
            log.info("文件合并获取到锁md5={}", md5);
            //多线程情况下 检测是否已经上传过该文件
            TbFile tbFile = fileDbService.getTbFile(md5);
            if (tbFile != null) {
                //保存文件信息表并返回
                //这里没有判断文件是否存在 因为其它线程刚merge完不可能不存在 可能性太小 避免影响性能
                //fileClient.isExist(tbFile.getPath());
                TbFileInfo tbFileInfo = fileDbService.saveInfo(md5, filename);
                return FileUploadResult.builder()
                        .id(tbFileInfo.getId())
                        .filename(tbFileInfo.getFilename())
                        .filesize(tbFile.getFilesize())
                        .md5(tbFile.getMd5())
                        .filepath(tbFile.getPath())
                        .accessPath(fileClient.getAccessPath(tbFile.getPath())).build();
            }

            long localTotalSize = handler.getTotalSize(md5);
            List<Chunk> chunks2 = handler.getChunks(md5);
            Long size = request.getSize();
            if (localTotalSize != size) {
                log.error("合并分块错误 发现分片文件不满足合并条件" +
                                "md5={} 实际总大小={} 计算总大小={} 计算分片数量={}",
                        md5, size, localTotalSize, chunks2.size());
                throw new ChunkException(ErrorCode.UPLOAD_CHUNK_CHECK_ERROR);
            }

            List<Chunk> chunks = handler.getChunks(md5);
            List<UploadPart> parts = new ArrayList<>(chunks.size());
            for (Chunk chunk : chunks) {
                UploadPart uploadPart = new UploadPart(chunk.getNumber(),
                        chunk.getSize(), handler.getInputStream(chunk));
                parts.add(uploadPart);
            }
            String path = fileClient.uploadPart(parts, createObjectName(filename, md5));

            fileDbService.saveFile(md5, path, size);
            TbFileInfo tbFileInfo = fileDbService.saveInfo(md5, filename);
            log.info("合并完成MD5={}", md5);
            return FileUploadResult
                    .builder()
                    .id(tbFileInfo.getId())
                    .md5(md5)
                    .filename(filename)
                    .filesize(size)
                    .filepath(path)
                    .accessPath(fileClient.getAccessPath(path))
                    .build();
        } catch (IOException e) {
            log.info("合并分块异常", e);
            throw new ChunkException(ErrorCode.UPLOAD_ERROR);
        } finally {
            //删除所有分片 容错uploadChunk方法可能存在多余的分片
            handler.deleteChunk(md5);
            log.info("删除所有分片完毕md5={}", md5);
            log.info("文件合并释放锁md5={}", md5);
            lock.unlock(md5);
        }
    }

    public String createObjectName(String fileName, String md5) {
        String extension = FilenameUtils.getExtension(fileName);
        String prefix = "";
        if (extension != null) {
            switch (extension.toLowerCase()) {
                case "png":
                case ".png":
                case "jpg":
                case ".jpg":
                case "jpeg":
                case ".jpeg":
                case "gif":
                case ".gif":
                case "bmp":
                case ".bmp":
                    prefix = "pics/";
                    break;
                default:
                    break;
            }
        }
        return DateFormatUtils.format(new Date(), "yyyyMMdd")
                + StrUtil.SLASH + prefix + md5 + StrUtil.DOT + extension;
    }

}
