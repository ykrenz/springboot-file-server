package com.github.ren.file.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.github.ren.file.entity.FastFile;
import com.github.ren.file.entity.FastFilePart;
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
import com.github.ren.file.model.result.CheckResult;
import com.ykrenz.fastdfs.FastDfs;
import com.ykrenz.fastdfs.model.CompleteMultipartRequest;
import com.ykrenz.fastdfs.model.FileInfoRequest;
import com.ykrenz.fastdfs.model.InitMultipartUploadRequest;
import com.ykrenz.fastdfs.model.UploadFileRequest;
import com.ykrenz.fastdfs.model.UploadMultipartPartRequest;
import com.ykrenz.fastdfs.model.fdfs.StorePath;
import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author ykren
 * @date 2022/3/1
 */
public class FastDfsServiceImpl extends AbstractFileService<FastFile, FastFilePart> {

    private FastDfs fastDfs;

    @Autowired
    private FastFileMapper fastFileMapper;

    @Autowired
    private FastFilePartMapper fastFilePartMapper;

    public FastDfsServiceImpl(FastDfs fastDfs) {
        this.fastDfs = fastDfs;
    }

    @Override
    public FastFile uploadFile(SimpleUploadRequest request) throws IOException {
        MultipartFile file = request.getFile();
        String originalFilename = file.getOriginalFilename();
        UploadFileRequest uploadFileRequest = UploadFileRequest.builder()
                .stream(file.getInputStream(), file.getSize(),
                        FilenameUtils.getExtension(originalFilename))
                .crc32(request.getCrc32() == null ? 0 : request.getCrc32())
                .build();
        StorePath storePath = fastDfs.uploadFile(uploadFileRequest);
        FastFile fastFile = new FastFile();
        fastFile.setGroupName(storePath.getGroup());
        fastFile.setPath(storePath.getPath());
        fastFile.setCrc32(request.getCrc32());
        fastFile.setMd5(request.getMd5());

        fastFileMapper.insert(fastFile);
        return fastFile;
    }

    @Override
    public FastFilePart initUploadPart(InitPartRequest request) {
        InitMultipartUploadRequest initMultipartUploadRequest = InitMultipartUploadRequest.builder()
                .fileSize(request.getFileSize())
                .fileExtName(FilenameUtils.getExtension(request.getFileName()))
                .build();
        StorePath storePath = fastDfs.initMultipartUpload(initMultipartUploadRequest);
        FastFilePart fastFilePart = new FastFilePart();
        fastFilePart.setGroupName(storePath.getGroup());
        fastFilePart.setPath(storePath.getPath());
        fastFilePart.setPartNumber(0);
        fastFilePart.setPartSize(request.getPartSize());
        fastFilePart.setFileSize(request.getFileSize());

        fastFilePart.setUploadId(request.getUploadId());
        fastFilePartMapper.insert(fastFilePart);
        return fastFilePart;
    }

    @Override
    public FastFilePart uploadPart(UploadPartRequest request) throws IOException {
        MultipartFile file = request.getFile();
        Integer partNumber = request.getPartNumber();
        FastFilePart initPart = getInitPart(request.getUploadId());
        UploadMultipartPartRequest multipartPartRequest = UploadMultipartPartRequest.builder()
                .groupName(initPart.getGroupName())
                .path(initPart.getPath())
                .streamPart(file.getInputStream(), file.getSize(), partNumber, initPart.getPartSize())
                .build();
        fastDfs.uploadMultipart(multipartPartRequest);

        FastFilePart fastFilePart = new FastFilePart();
        fastFilePart.setUploadId(request.getUploadId());
        fastFilePart.setGroupName(initPart.getGroupName());
        fastFilePart.setPath(initPart.getPath());
        fastFilePart.setPartNumber(partNumber);
        fastFilePart.setPartSize(initPart.getPartSize());
        fastFilePart.setFileSize(file.getSize());

        fastFilePartMapper.insert(fastFilePart);
        return fastFilePart;
    }

    @Override
    public FastFile completePart(CompletePartRequest request) {
        FastFilePart initPart = getInitPart(request.getUploadId());
        CompleteMultipartRequest multipartRequest = CompleteMultipartRequest.builder()
                .groupName(initPart.getGroupName())
                .path(initPart.getPath())
                // 6.0.2以下版本设置为false
                .regenerate(true)
                .crc32(request.getFileCrc32() == null ? 0 : request.getFileCrc32())
                .build();
        StorePath storePath = fastDfs.completeMultipartUpload(multipartRequest);

        FastFile fastFile = new FastFile();
        fastFile.setGroupName(storePath.getGroup());
        fastFile.setPath(storePath.getPath());
        fastFile.setCrc32(request.getFileCrc32());
        fastFile.setMd5(request.getFileMd5());
        fastFile.setFileSize(initPart.getFileSize());

        fastFileMapper.insert(fastFile);

        //清空所有分片记录
        List<String> parts = fastFilePartMapper.selectList(Wrappers.<FastFilePart>lambdaQuery()
                .eq(FastFilePart::getUploadId, request.getUploadId())
                .select(FastFilePart::getId)).stream().map(FastFilePart::getId).collect(Collectors.toList());
        fastFilePartMapper.deleteBatchIds(parts);
        return fastFile;
    }

    private FastFilePart getInitPart(String uploadId) {
        //TODO 加入缓存 upload重复校验
        FastFilePart initPart = fastFilePartMapper.selectOne(Wrappers.<FastFilePart>lambdaQuery()
                .eq(FastFilePart::getUploadId, uploadId)
                .eq(FastFilePart::getPartNumber, 0)
                .eq(FastFilePart::getStatus, 1)
        );
        if (initPart == null) {
            throw new ApiException(ErrorCode.UPLOAD_ID_NOT_FOUND);
        }
        return initPart;
    }

    @Override
    public CheckResult check(CheckRequest request) {
        Long crc32 = request.getFileCrc32();
        String md5 = request.getFileMd5();
        if (StringUtils.isNotBlank(md5)) {
            LambdaQueryWrapper<FastFile> wrapper = Wrappers.<FastFile>lambdaQuery();
            if (crc32 != null) {
                wrapper.eq(FastFile::getCrc32, crc32);
            }
            FastFile fastFile = fastFileMapper.selectOne(wrapper
                    .eq(FastFile::getMd5, md5));
            if (fastFile != null) {
                return new CheckResult(true);
            }
        }

        if (StringUtils.isNotBlank(request.getUploadId())) {
            List<FastFilePart> partList = fastFilePartMapper.selectList(Wrappers.<FastFilePart>lambdaQuery()
                    .eq(FastFilePart::getUploadId, request.getUploadId())
                    .ne(FastFilePart::getPartNumber, 0)
                    .select(FastFilePart::getId, FastFilePart::getPartNumber)
            );

            List<Integer> list = partList.stream()
                    .map(FastFilePart::getPartNumber)
                    .distinct()
                    .collect(Collectors.toList());
            return new CheckResult(false, list);
        }
        return new CheckResult();
    }

    @Override
    public void abortMultipart(AbortPartRequest request) {
        FastFilePart initPart = getInitPart(request.getUploadId());
        initPart.setStatus(0);
        fastFilePartMapper.updateById(initPart);
    }

    @Override
    public void deleteAllFiles() {
        List<FastFilePart> fastFileParts = fastFilePartMapper.selectList(Wrappers.emptyWrapper());
        List<FastFile> fastFiles = fastFileMapper.selectList(Wrappers.emptyWrapper());
        for (FastFilePart file : fastFileParts) {
            FileInfoRequest request = FileInfoRequest.builder()
                    .groupName(file.getGroupName())
                    .path(file.getPath())
                    .build();
            fastDfs.deleteFile(request);
            if (fastDfs.queryFileInfo(request) != null) {
                throw new ApiException(ErrorCode.SERVER_ERROR, "删除失败");
            }
            fastFilePartMapper.deleteById(file.getId());
        }

        for (FastFile file : fastFiles) {
            FileInfoRequest request = FileInfoRequest.builder()
                    .groupName(file.getGroupName())
                    .path(file.getPath())
                    .build();
            fastDfs.deleteFile(request);
            if (fastDfs.queryFileInfo(request) != null) {
                throw new ApiException(ErrorCode.SERVER_ERROR, "删除失败");
            }
            fastFileMapper.deleteById(file.getId());
        }
    }

}
