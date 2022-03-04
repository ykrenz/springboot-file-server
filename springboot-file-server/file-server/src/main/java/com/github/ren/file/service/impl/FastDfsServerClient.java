package com.github.ren.file.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.github.ren.file.entity.FileInfo;
import com.github.ren.file.entity.FilePartInfo;
import com.github.ren.file.ex.ApiException;
import com.github.ren.file.mapper.FastFileMapper;
import com.github.ren.file.mapper.FastFilePartMapper;
import com.github.ren.file.model.ErrorCode;
import com.github.ren.file.model.request.AbortPartRequest;
import com.github.ren.file.model.request.CheckRequest;
import com.github.ren.file.model.request.CompletePartRequest;
import com.github.ren.file.model.request.InitPartRequest;
import com.github.ren.file.model.request.SimpleUploadRequest;
import com.github.ren.file.model.request.UploadPartRequest;
import com.github.ren.file.model.result.InitPartResult;
import com.ykrenz.fastdfs.FastDfs;
import com.ykrenz.fastdfs.model.CompleteMultipartRequest;
import com.ykrenz.fastdfs.model.FileInfoRequest;
import com.ykrenz.fastdfs.model.InitMultipartUploadRequest;
import com.ykrenz.fastdfs.model.UploadFileRequest;
import com.ykrenz.fastdfs.model.UploadMultipartPartRequest;
import com.ykrenz.fastdfs.model.fdfs.StorePath;
import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author ykren
 * @date 2022/3/1
 */
public class FastDfsServerClient implements FileServerClient {

    private FastDfs fastDfs;

    @Autowired
    private FastFileMapper fastFileMapper;

    @Autowired
    private FastFilePartMapper fastFilePartMapper;

    public FastDfsServerClient(FastDfs fastDfs) {
        this.fastDfs = fastDfs;
    }

    @Override
    public FileInfo upload(SimpleUploadRequest request) throws IOException {
        MultipartFile file = request.getFile();
        String originalFilename = file.getOriginalFilename();
        UploadFileRequest uploadFileRequest = UploadFileRequest.builder()
                .stream(file.getInputStream(), file.getSize(),
                        FilenameUtils.getExtension(originalFilename))
                .crc32(request.getCrc32() == null ? 0 : request.getCrc32())
                .build();
        StorePath storePath = fastDfs.uploadFile(uploadFileRequest);
        FileInfo fileInfo = new FileInfo();
        fileInfo.setBucketName(storePath.getGroup());
        fileInfo.setObjectName(storePath.getPath());
        fileInfo.setCrc32(request.getCrc32());
        fileInfo.setMd5(request.getMd5());

        fastFileMapper.insert(fileInfo);
        return fileInfo;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public InitPartResult initMultipart(InitPartRequest request) {
        String uploadId = request.getUploadId();
        FilePartInfo initPart = getInitPart(uploadId);
        if (initPart != null) {
            CheckRequest checkRequest = new CheckRequest();
            checkRequest.setFileMd5(request.getFileMd5());
            checkRequest.setUploadId(request.getUploadId());
            checkRequest.setFileCrc32(request.getFileCrc32());
            return check(checkRequest);
        }
        InitMultipartUploadRequest initMultipartUploadRequest = InitMultipartUploadRequest.builder()
                .fileSize(request.getFileSize())
                .fileExtName(FilenameUtils.getExtension(request.getFileName()))
                .build();
        StorePath storePath = fastDfs.initMultipartUpload(initMultipartUploadRequest);
        FilePartInfo filePartInfo = new FilePartInfo();
        filePartInfo.setFileName(request.getFileName());
        filePartInfo.setBucketName(storePath.getGroup());
        filePartInfo.setObjectName(storePath.getPath());
        filePartInfo.setPartNumber(0);
        filePartInfo.setPartSize(request.getPartSize());
        filePartInfo.setFileSize(request.getFileSize());

        filePartInfo.setUploadId(request.getUploadId());
        fastFilePartMapper.insert(filePartInfo);
        return new InitPartResult(false);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public FilePartInfo uploadMultipart(UploadPartRequest request) throws IOException {
        MultipartFile file = request.getFile();
        Integer partNumber = request.getPartNumber();
        FilePartInfo initPart = getInitPart(request.getUploadId());
        if (initPart == null) {
            throw new ApiException(ErrorCode.UPLOAD_ID_NOT_FOUND);
        }
        UploadMultipartPartRequest multipartPartRequest = UploadMultipartPartRequest.builder()
                .groupName(initPart.getBucketName())
                .path(initPart.getObjectName())
                .streamPart(file.getInputStream(), file.getSize(), partNumber, initPart.getPartSize())
                .build();
        fastDfs.uploadMultipart(multipartPartRequest);

        FilePartInfo filePartInfo = new FilePartInfo();
        filePartInfo.setUploadId(request.getUploadId());
        filePartInfo.setBucketName(initPart.getBucketName());
        filePartInfo.setObjectName(initPart.getObjectName());
        filePartInfo.setFileName(initPart.getFileName());
        filePartInfo.setPartNumber(partNumber);
        filePartInfo.setPartSize(initPart.getPartSize());
        filePartInfo.setFileSize(file.getSize());

        fastFilePartMapper.insert(filePartInfo);
        return filePartInfo;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public FileInfo completeMultipart(CompletePartRequest request) {
        FilePartInfo initPart = getInitPart(request.getUploadId());
        if (initPart == null) {
            throw new ApiException(ErrorCode.UPLOAD_ID_NOT_FOUND);
        }
        CompleteMultipartRequest multipartRequest = CompleteMultipartRequest.builder()
                .groupName(initPart.getBucketName())
                .path(initPart.getObjectName())
                // 6.0.2以下版本设置为false
                .regenerate(true)
                .crc32(request.getFileCrc32() == null ? 0 : request.getFileCrc32())
                .build();
        StorePath storePath = fastDfs.completeMultipartUpload(multipartRequest);

        FileInfo fileInfo = new FileInfo();
        fileInfo.setBucketName(storePath.getGroup());
        fileInfo.setObjectName(storePath.getPath());
        fileInfo.setCrc32(request.getFileCrc32());
        fileInfo.setMd5(request.getFileMd5());
        fileInfo.setFileSize(initPart.getFileSize());
        fileInfo.setFileName(initPart.getFileName());

        fastFileMapper.insert(fileInfo);

        //清空所有分片记录
        List<String> parts = fastFilePartMapper.selectList(Wrappers.<FilePartInfo>lambdaQuery()
                .eq(FilePartInfo::getUploadId, request.getUploadId())
                .select(FilePartInfo::getId)).stream().map(FilePartInfo::getId).collect(Collectors.toList());
        fastFilePartMapper.deleteBatchIds(parts);
        return fileInfo;
    }

    private FilePartInfo getInitPart(String uploadId) {
        //TODO 加入缓存 upload重复校验
        FilePartInfo initPart = fastFilePartMapper.selectOne(Wrappers.<FilePartInfo>lambdaQuery()
                .eq(FilePartInfo::getUploadId, uploadId)
                .eq(FilePartInfo::getPartNumber, 0)
                .eq(FilePartInfo::getStatus, 1)
        );
        return initPart;
    }

    private InitPartResult check(CheckRequest request) {
        Long crc32 = request.getFileCrc32();
        String md5 = request.getFileMd5();
        if (StringUtils.isNotBlank(md5)) {
            LambdaQueryWrapper<FileInfo> wrapper = Wrappers.<FileInfo>lambdaQuery();
            if (crc32 != null) {
                wrapper.eq(FileInfo::getCrc32, crc32);
            }
            FileInfo fileInfo = fastFileMapper.selectOne(wrapper
                    .eq(FileInfo::getMd5, md5).last(" limit 1"));
            if (fileInfo != null) {
                return new InitPartResult(true);
            }
        }

        if (StringUtils.isNotBlank(request.getUploadId())) {
            List<FilePartInfo> partList = fastFilePartMapper.selectList(Wrappers.<FilePartInfo>lambdaQuery()
                    .eq(FilePartInfo::getUploadId, request.getUploadId())
                    .ne(FilePartInfo::getPartNumber, 0)
                    .select(FilePartInfo::getId, FilePartInfo::getPartNumber)
            );

            List<Integer> list = partList.stream()
                    .map(FilePartInfo::getPartNumber)
                    .distinct()
                    .collect(Collectors.toList());
            return new InitPartResult(false, list);
        }
        return new InitPartResult();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void abortMultipart(AbortPartRequest request) {
        FilePartInfo initPart = getInitPart(request.getUploadId());
        if (initPart == null) {
            throw new ApiException(ErrorCode.UPLOAD_ID_NOT_FOUND);
        }
        initPart.setStatus(0);
        fastFilePartMapper.updateById(initPart);
    }

    @Override
    public void deleteAllFiles() {
        List<FilePartInfo> filePartInfos = fastFilePartMapper.selectList(Wrappers.emptyWrapper());
        List<FileInfo> fileInfos = fastFileMapper.selectList(Wrappers.emptyWrapper());
        for (FilePartInfo file : filePartInfos) {
            FileInfoRequest request = FileInfoRequest.builder()
                    .groupName(file.getBucketName())
                    .path(file.getObjectName())
                    .build();
            fastDfs.deleteFile(request);
            if (fastDfs.queryFileInfo(request) != null) {
                throw new ApiException(ErrorCode.SERVER_ERROR, "删除失败");
            }
            fastFilePartMapper.deleteById(file.getId());
        }

        for (FileInfo file : fileInfos) {
            FileInfoRequest request = FileInfoRequest.builder()
                    .groupName(file.getBucketName())
                    .path(file.getObjectName())
                    .build();
            fastDfs.deleteFile(request);
            if (fastDfs.queryFileInfo(request) != null) {
                throw new ApiException(ErrorCode.SERVER_ERROR, "删除失败");
            }
            fastFileMapper.deleteById(file.getId());
        }
    }

}
