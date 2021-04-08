package com.github.ren.file.service.impl;

import cn.hutool.crypto.digest.MD5;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.github.ren.file.clients.FileClient;
import com.github.ren.file.entity.TbFile;
import com.github.ren.file.entity.TbFileInfo;
import com.github.ren.file.ex.ApiException;
import com.github.ren.file.model.ChunkRequest;
import com.github.ren.file.model.ChunkResult;
import com.github.ren.file.model.ErrorCode;
import com.github.ren.file.model.FileUploadResult;
import com.github.ren.file.service.AbstractFileService;
import com.github.ren.file.service.ChunkOperator;
import com.github.ren.file.service.FileOperatorHelper;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

/**
 * @author RenYinKui
 * @Description: 文件实现逻辑类
 * @date 2021/4/7 9:58
 */
@Service("FileUploadServiceImpl")
@Slf4j
public class FileUploadServiceImpl extends AbstractFileService {

    @Autowired
    @Qualifier("TbFileServiceImpl")
    private TbFileServiceImpl tbFileService;
    @Autowired
    @Qualifier("TbFileInfoServiceImpl")
    private TbFileInfoServiceImpl tbFileInfoService;
    @Autowired
    private FileOperatorHelper fileOperatorHelper;
    @Autowired
    private FileClient fileClient;

    @Override
    protected ChunkResult checkChunk(ChunkRequest chunkRequest) {
        String identifier = chunkRequest.getIdentifier();
        Long totalSize = chunkRequest.getTotalSize();
        ChunkOperator chunkOperator = fileOperatorHelper.getChunkOperator(chunkRequest);
        //秒传文件
        TbFile tbFile = isExist(identifier);
        if (tbFile != null) {
            String fileId = tbFile.getId();
            TbFileInfo tbFileInfo = new TbFileInfo();
            tbFileInfo.setFilename(chunkRequest.getFilename());
            tbFileInfo.setFid(fileId);
            tbFileInfoService.save(tbFileInfo);

            String fileInfoId = tbFileInfo.getId();
            String filename = tbFileInfo.getFilename();
            Long filesize = tbFile.getFilesize();
            String md5 = tbFile.getMd5();
            String path = tbFile.getPath();

            //删除所有分片 容错
            if (!chunkOperator.delAllChunk()) {
                log.error("删除所有分片失败 请检查服务器文件path={}", path);
            }
            FileUploadResult uploadResult = FileUploadResult
                    .builder()
                    .id(fileInfoId)
                    .filename(filename)
                    .filesize(filesize)
                    .md5(md5)
                    .path(fileClient.getAccessPath(path))
                    .build();

            return new ChunkResult(uploadResult);
        }
        //检测是否需要合并文件 合并文件后返回

        if (chunkOperator.needMerge(totalSize)) {
            FileUploadResult fileUploadResult = mergeChunk(chunkRequest);
            return new ChunkResult(fileUploadResult);
        }
        //没有上传过且不需要合并 获取已经上传的分块给到客户端 用于断点续传
        List<Long> chunks = chunkOperator.getChunkNumbers();
        return new ChunkResult(chunks);
    }

    @Override
    @Transactional
    protected FileUploadResult uploadChunk(ChunkRequest chunkRequest, MultipartFile multipartFile) {
        Long totalSize = chunkRequest.getTotalSize();
        ChunkOperator chunkOperator = fileOperatorHelper.getChunkOperator(chunkRequest);
        //判断是否已经传过该分片 逻辑:文件存在且当前分片大小和本地大小一致
        if (chunkOperator.isUpload(chunkRequest.getCurrentChunkSize())) {
            //已经上传过该文件 判断是否需要合并文件
            if (chunkOperator.needMerge(totalSize)) {
                return mergeChunk(chunkRequest);
            }
            //已经上传过了不需要合并 直接返回
            return null;
        }

        try {
            //没有上传过该分片 上传到分块目录
            chunkOperator.upload(multipartFile.getInputStream());
            //判断是否需要合并文件
            if (chunkOperator.needMerge(totalSize)) {
                return mergeChunk(chunkRequest);
            }
            //不需要合并 直接返回
            return null;
        } catch (Exception e) {
            log.error("分片上传失败", e);
            if (!chunkOperator.delCurrentChunk()) {
                log.error("分片上传失败 删除分片失败 请检查服务器是否有问题", e);
            }
            throw new ApiException(ErrorCode.UPLOAD_CHUNK_ERROR);
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    protected FileUploadResult mergeChunk(ChunkRequest chunkRequest) {
        ChunkOperator chunkOperator = fileOperatorHelper.getChunkOperator(chunkRequest);
        List<File> chunks = chunkOperator.getChunks();
        String filename = chunkRequest.getFilename();
        String identifier = chunkRequest.getIdentifier();
        String objectName = fileOperatorHelper.createObjectName(filename, identifier);
        String path = fileClient.uploadPart(chunks, objectName);

        Long totalSize = chunkRequest.getTotalSize();
        TbFile tbFile = new TbFile();
        tbFile.setFilesize(totalSize);
        tbFile.setMd5(identifier);
        tbFile.setPath(path);
        tbFileService.save(tbFile);

        String fileId = tbFile.getId();

        TbFileInfo tbFileInfo = new TbFileInfo();
        tbFileInfo.setFilename(filename);
        tbFileInfo.setFid(fileId);
        tbFileInfoService.save(tbFileInfo);
        //删除所有分片
        if (!chunkOperator.delAllChunk()) {
            log.error("删除所有分片失败 请检查服务器文件path={}", path);
        }
        return FileUploadResult
                .builder()
                .id(tbFileInfo.getId())
                .md5(identifier)
                .filename(filename)
                .filesize(totalSize)
                .path(fileClient.getAccessPath(path))
                .build();
    }

    @Override
    @Transactional
    public FileUploadResult upload(MultipartFile multipartFile) {

        try (InputStream inputStream = multipartFile.getInputStream()) {
            String md5 = MD5.create().digestHex(multipartFile.getInputStream());
            String filename = multipartFile.getOriginalFilename();
            String path = fileClient.uploadFile(inputStream, md5.concat(".") + FilenameUtils.getExtension(filename));
            long size = multipartFile.getSize();
            TbFile tbFile = tbFileService.getBaseMapper()
                    .selectOne(Wrappers.<TbFile>lambdaQuery().eq(TbFile::getMd5, md5));
            if (tbFile == null) {
                tbFile = new TbFile();
                tbFile.setFilesize(size);
                tbFile.setMd5(md5);
                tbFile.setPath(path);
                tbFileService.save(tbFile);
            }

            String fileId = tbFile.getId();

            TbFileInfo tbFileInfo = new TbFileInfo();
            tbFileInfo.setFilename(filename);
            tbFileInfo.setFid(fileId);
            tbFileInfoService.save(tbFileInfo);

            String fileInfoId = tbFileInfo.getId();
            return FileUploadResult
                    .builder()
                    .id(fileInfoId)
                    .md5(md5)
                    .filename(filename)
                    .filesize(size)
                    .path(fileClient.getAccessPath(path))
                    .build();
        } catch (IOException e) {
            log.error("上传失败", e);
            throw new ApiException(ErrorCode.UPLOAD_ERROR);
        }
    }

    private TbFile isExist(String md5) {
        TbFile tbFile = tbFileService.getBaseMapper()
                .selectOne(Wrappers.<TbFile>lambdaQuery().eq(TbFile::getMd5, md5));
        if (tbFile == null) {
            return null;
        }
        boolean exist = fileClient.isExist(tbFile.getPath());
        if (!exist) {
            tbFileService.getBaseMapper().delete(Wrappers.<TbFile>lambdaQuery().eq(TbFile::getMd5, md5));
        }
        return exist ? tbFile : null;
    }
}
