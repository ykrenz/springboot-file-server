package com.ren.file.service;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.github.ren.file.clients.AliOssFileClient;
import com.github.ren.file.clients.FastDfsFileClient;
import com.github.ren.file.clients.LocalFileClient;
import com.github.ren.file.properties.LocalFileProperties;
import com.github.tobato.fastdfs.domain.fdfs.StorePath;
import com.google.common.collect.Lists;
import com.ren.file.config.FileSreverProperties;
import com.ren.file.dto.request.Chunk;
import com.ren.file.dto.request.MergeChunkDto;
import com.ren.file.dto.response.ChunkRes;
import com.ren.file.dto.response.MergeRes;
import com.ren.file.enmus.RErrorEnum;
import com.ren.file.entity.Fileinfo;
import com.ren.file.mapper.FileinfoMapper;
import com.ren.file.util.FileLock;
import com.ren.file.util.R;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.locks.Lock;
import java.util.stream.Collectors;

/**
 * @author RenYinKui
 * @Description:
 * @date 2020/6/2 16:16
 */
@Service
@Slf4j
public class FileServiceImpl implements IFileService {

    @Autowired
    private LocalFileClient localFileClient;

    @Autowired
    private FastDfsFileClient fastDfsFileClient;

    @Autowired
    private LocalFileProperties localFileProperties;

    @Autowired
    private FileSreverProperties fileSreverProperties;

    @Autowired
    private AliOssFileClient aliOssFileClient;

    @Resource
    private FileinfoMapper fileinfoMapper;

    @Override
    public R<MergeRes> checkChunk(Chunk chunk) {
        MergeRes mergeRes = new MergeRes();
        //查询数据库MD5校验文件是否已经上传,实现秒传,此处写死false
        Fileinfo fileinfo = fileinfoMapper.selectOne(Wrappers.<Fileinfo>lambdaQuery().eq(Fileinfo::getMd5, chunk.getIdentifier()).last("limit 1"));
        mergeRes.setUploaded(fileinfo != null);
        String identifier = chunk.getIdentifier();
        List<File> mergeFileList = this.getMergeFileList(identifier);
        if (fileinfo != null) {
            mergeRes.setFileinfo(fileinfo);
            //删除可能存在的临时文件
            mergeRes.setMerge(false);
            mergeFileList.forEach(FileUtils::deleteQuietly);
            mergeRes.setChunkNumbers(Lists.newArrayListWithCapacity(0));
        } else {
            List<Integer> chunkNumbers = this.getMergeChunkNumbers(identifier);
            mergeRes.setChunkNumbers(chunkNumbers);
            mergeRes.setMerge(chunk.getTotalChunks() == chunkNumbers.size());
        }
        return R.success(mergeRes);
    }

    @Override
    public R<ChunkRes> uploadChunk(Chunk chunk, MultipartFile file) {
        //直接上传到文件服务
        if (chunk.getTotalChunks() <= 1) {
            try (InputStream inputStream = file.getInputStream()) {
                StorePath storePath = fastDfsFileClient.uploadFileStorePath(inputStream,
                        chunk.getIdentifier().concat(".") + FilenameUtils.getExtension(chunk.getFilename()));
                Fileinfo fileinfo = Fileinfo.builder()
                        .md5(chunk.getIdentifier())
                        .filename(chunk.getFilename())
                        .filesize(chunk.getTotalSize())
                        .extension(chunk.getExtension())
                        .filetype(chunk.getFileType())
                        .createTime(LocalDateTime.now())
                        .updateTime(LocalDateTime.now())
                        .path(storePath.getFullPath())
                        .build();
                fileinfoMapper.insert(fileinfo);
                fileinfo.setPath(fastDfsFileClient.getWebServerPath(storePath));
                ChunkRes chunkRes = ChunkRes.builder().
                        merge(false)
                        .fileinfo(fileinfo)
                        .build();
                return R.success(chunkRes);
            } catch (IOException e) {
                return R.fail(RErrorEnum.UPLOAD_CHUNK_ERROR);
            }
        } else {
            //上传到分块目录
            Integer chunkNumber = chunk.getChunkNumber();
            String identifier = chunk.getIdentifier();
            String chunkTempPath = fileSreverProperties.getChunkTempPath();
            String chunkName = identifier.concat("-") + chunkNumber;
            File chunkFile = new File(chunkTempPath, chunkName);
            Lock lock = FileLock.getLock(chunkName);
            try {
                lock.lock();
                if (chunkFile.exists() && chunkFile.length() == chunk.getCurrentChunkSize()) {
                    log.info("该分块已经上传{}", chunkFile.getName());
                } else {
                    FileUtils.copyInputStreamToFile(file.getInputStream(), chunkFile);
                }
                ChunkRes chunkRes = ChunkRes.builder().
                        merge(chunk.getChunkNumber().equals(chunk.getTotalChunks()))
                        .build();
                return R.success(chunkRes);
            } catch (Exception e) {
                log.error("上传异常", e);
                return R.fail(RErrorEnum.UPLOAD_CHUNK_ERROR);
            } finally {
                //解锁
                lock.unlock();
                FileLock.removeLock(chunkName);
            }
        }
    }

    /**
     * 获取临时目录
     *
     * @return
     */
    private String getChunkTempPath() {
        return fileSreverProperties.getChunkTempPath();
    }

    /**
     * 获取分片文件 并排序
     *
     * @param identifier
     * @return
     */
    private List<File> getMergeFileList(String identifier) {
        return fastDfsFileClient.getMergeFileList(this.getChunkTempPath(), chunk1 -> chunk1.getName().startsWith(identifier),
                Comparator.comparing(f -> Integer.parseInt(f.getName().substring(f.getName().lastIndexOf("-") + 1))));
    }

    /**
     * 获取已经上传分片集合 断点续传
     *
     * @param identifier
     * @return
     */
    private List<Integer> getMergeChunkNumbers(String identifier) {
        return this.getMergeFileList(identifier).stream()
                .map(f -> Integer.parseInt(f.getName().substring(f.getName().lastIndexOf("-") + 1)))
                .sorted(Comparator.comparing(Integer::new))
                .collect(Collectors.toList());
    }

    @Override
    public R<Fileinfo> mergeChunk(MergeChunkDto mergeChunkDto) {
        String identifier = mergeChunkDto.getIdentifier();
        String filename = mergeChunkDto.getFilename();
        Lock lock = FileLock.getLock(identifier);
        try {
            lock.lock();
            List<File> mergeFileList = this.getMergeFileList(identifier);
            StorePath storePath = fastDfsFileClient.uploadPartStorePath(mergeFileList, filename);
            mergeFileList.forEach(FileUtils::deleteQuietly);
            Fileinfo fileinfo = null;
            if (storePath == null) {
                //线程并发 优先获取到锁的线程已经上传完该文件，查询数据库，进行返回
                fileinfo = fileinfoMapper.selectOne(Wrappers.<Fileinfo>lambdaQuery().eq(Fileinfo::getMd5, identifier));
                if (fileinfo == null) {
                    return R.fail(RErrorEnum.UPLOAD_MERGE_ERROR);
                }
                fileinfo.setPath(fastDfsFileClient.getWebServerUrl() + fileinfo.getPath());
            } else {
                fileinfo = Fileinfo.builder()
                        .md5(identifier)
                        .filename(filename)
                        .filesize(mergeChunkDto.getFilesize())
                        .extension(mergeChunkDto.getExtension())
                        .filetype(mergeChunkDto.getFileType())
                        .createTime(LocalDateTime.now())
                        .updateTime(LocalDateTime.now())
                        .path(storePath.getFullPath())
                        .build();
                fileinfoMapper.insert(fileinfo);
                fileinfo.setPath(fastDfsFileClient.getWebServerPath(storePath));
            }
            return R.success(fileinfo);
        } catch (Exception e) {
            log.error("合并分片失败", e);
            return R.fail(RErrorEnum.UPLOAD_MERGE_ERROR);
        } finally {
            //解锁
            lock.unlock();
            FileLock.removeLock(identifier);
        }
    }
}
