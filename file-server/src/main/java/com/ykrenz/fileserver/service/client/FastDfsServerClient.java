package com.ykrenz.fileserver.service.client;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.ykrenz.fastdfs.common.Crc32;
import com.ykrenz.fileserver.config.StorageProperties;
import com.ykrenz.fileserver.entity.FileInfo;
import com.ykrenz.fileserver.entity.FilePartInfo;
import com.ykrenz.fileserver.ex.ApiException;
import com.ykrenz.fileserver.mapper.FileInfoMapper;
import com.ykrenz.fileserver.mapper.FilePartInfoMapper;
import com.ykrenz.fileserver.model.ErrorCode;
import com.ykrenz.fileserver.model.request.CancelPartRequest;
import com.ykrenz.fileserver.model.request.CompletePartRequest;
import com.ykrenz.fileserver.model.request.FileInfoRequest;
import com.ykrenz.fileserver.model.request.InitUploadMultipartRequest;
import com.ykrenz.fileserver.model.request.SimpleUploadRequest;
import com.ykrenz.fileserver.model.request.UploadMultipartRequest;
import com.ykrenz.fileserver.model.result.FileInfoResult;
import com.ykrenz.fileserver.model.result.InitMultipartResult;
import com.ykrenz.fastdfs.FastDfs;
import com.ykrenz.fastdfs.model.CompleteMultipartRequest;
import com.ykrenz.fastdfs.model.UploadMultipartPartRequest;
import com.ykrenz.fastdfs.model.fdfs.StorePath;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.ContextClosedEvent;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * @author ykren
 * @date 2022/3/1
 */
@Slf4j
public class FastDfsServerClient implements FileServerClient, ApplicationListener<ContextRefreshedEvent> {

    private final FastDfs fastDfs;

    private final StorageProperties storageProperties;

    private final int partExpireDays;

    @Resource
    private FileInfoMapper fileInfoMapper;

    @Resource
    private FilePartInfoMapper filePartInfoMapper;

    @Resource
    private LockClient lockClient;

    public FastDfsServerClient(FastDfs fastDfs, StorageProperties storageProperties) {
        this.fastDfs = fastDfs;
        this.storageProperties = storageProperties;
        this.partExpireDays = storageProperties.getFastdfs().getPartExpireDays();
    }

    @Override
    public FileInfo upload(SimpleUploadRequest request) throws IOException {
        MultipartFile file = request.getFile();
        String originalFilename = file.getOriginalFilename();
        String extension = FilenameUtils.getExtension(originalFilename);
        StorePath storePath = fastDfs.uploadFile(file.getInputStream(), file.getSize(), extension);
        checkCrc32(request.getCrc32(), storePath);
        FileInfo fileInfo = new FileInfo();
        fileInfo.setBucketName(storePath.getGroup());
        fileInfo.setObjectName(storePath.getPath());
        // 注意: 这里保存的是有符号crc32 和fastdfs返回的不一致
        fileInfo.setCrc32(request.getCrc32());
        fileInfo.setMd5(request.getMd5());

        fileInfoMapper.insert(fileInfo);
        return fileInfo;
    }

    private void checkCrc32(Long crc32, StorePath storePath) {
        if (crc32 != null && crc32 > 0) {
            com.ykrenz.fastdfs.model.fdfs.FileInfo fileInfo = fastDfs.queryFileInfo(storePath.getGroup(), storePath.getPath());
            log.debug("check crc32 client crc32={} fastdfs crc32 ={} convert crc32={}",
                    crc32, fileInfo.getCrc32(), Crc32.convertUnsigned(fileInfo.getCrc32()));
            if (crc32 != Crc32.convertUnsigned(fileInfo.getCrc32())) {
                throw new ApiException(ErrorCode.FILE_CRC32_ERROR);
            }
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public InitMultipartResult initMultipart(InitUploadMultipartRequest request) {
        String uploadId = request.getUploadId();
        if (StringUtils.isNotBlank(uploadId)) {
            return check(request);
        }
        String extension = FilenameUtils.getExtension(request.getFileName());
        StorePath storePath = fastDfs.initMultipartUpload(request.getFileSize(), extension);
        FilePartInfo filePartInfo = new FilePartInfo();
        filePartInfo.setFileName(request.getFileName());
        filePartInfo.setBucketName(storePath.getGroup());
        filePartInfo.setObjectName(storePath.getPath());
        filePartInfo.setPartNumber(0);
        filePartInfo.setPartSize(request.getPartSize());
        filePartInfo.setFileSize(request.getFileSize());

        filePartInfoMapper.insert(filePartInfo);
        filePartInfo.setUploadId(uploadId);
        LambdaUpdateWrapper<FilePartInfo> updateWrapper = Wrappers.<FilePartInfo>lambdaUpdate()
                .eq(FilePartInfo::getId, filePartInfo.getId())
                .set(FilePartInfo::getUploadId, filePartInfo.getId());
        filePartInfoMapper.update(null, updateWrapper);
        return new InitMultipartResult(filePartInfo.getId(), false);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public FilePartInfo uploadMultipart(UploadMultipartRequest request) throws IOException {
        FilePartInfo initPart = checkUpload(request.getUploadId());
        MultipartFile file = request.getFile();
        Integer partNumber = request.getPartNumber();
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

        filePartInfoMapper.insert(filePartInfo);
        return filePartInfo;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public FileInfo completeMultipart(CompletePartRequest request) {
        FilePartInfo initPart = checkUpload(request.getUploadId());
        StorePath storePath = new StorePath(initPart.getBucketName(), initPart.getObjectName());
        try {
            CompleteMultipartRequest multipartRequest = CompleteMultipartRequest.builder()
                    .groupName(storePath.getGroup())
                    .path(storePath.getPath())
                    // 6.0.2以下版本设置为false
                    .regenerate(true)
                    .build();
            storePath = fastDfs.completeMultipartUpload(multipartRequest);

            checkCrc32(request.getFileCrc32(), storePath);

            FileInfo fileInfo = new FileInfo();
            fileInfo.setBucketName(storePath.getGroup());
            fileInfo.setObjectName(storePath.getPath());
            fileInfo.setCrc32(request.getFileCrc32());
            fileInfo.setMd5(request.getFileMd5());
            fileInfo.setFileSize(initPart.getFileSize());
            fileInfo.setFileName(initPart.getFileName());
            fileInfoMapper.insert(fileInfo);

            //清空所有分片记录
            List<String> parts = filePartInfoMapper.selectList(Wrappers.<FilePartInfo>lambdaQuery()
                    .eq(FilePartInfo::getUploadId, request.getUploadId())
                    .select(FilePartInfo::getId)).stream().map(FilePartInfo::getId).collect(Collectors.toList());
            filePartInfoMapper.deleteBatchIds(parts);
            filePartInfoMapper.deleteById(request.getUploadId());
            return fileInfo;
        } catch (Exception e) {
            CancelPartRequest cancelPartRequest = new CancelPartRequest();
            cancelPartRequest.setUploadId(request.getUploadId());
            cancelMultipart(cancelPartRequest);
            if (e instanceof ApiException) {
                throw e;
            }
            throw new ApiException(ErrorCode.UPLOAD_ERROR);
        }
    }

    private FilePartInfo getInitPart(String uploadId) {
        //TODO 加入缓存
        return filePartInfoMapper.selectOne(Wrappers.<FilePartInfo>lambdaQuery()
                .eq(FilePartInfo::getId, uploadId)
                .eq(FilePartInfo::getPartNumber, 0)
                .eq(FilePartInfo::getStatus, 1)
        );
    }

    private InitMultipartResult check(InitUploadMultipartRequest request) {
        String uploadId = request.getUploadId();
        checkUpload(uploadId);

        Long crc32 = request.getFileCrc32();
        String md5 = request.getFileMd5();
        if (StringUtils.isNotBlank(md5)) {
            LambdaQueryWrapper<FileInfo> wrapper = Wrappers.lambdaQuery();
            if (crc32 != null) {
                wrapper.eq(FileInfo::getCrc32, crc32);
            }
            FileInfo fileInfo = fileInfoMapper.selectOne(wrapper
                    .eq(FileInfo::getMd5, md5).last(" limit 1"));
            if (fileInfo != null) {
                return new InitMultipartResult(request.getUploadId(), true);
            }
        }

        if (StringUtils.isNotBlank(request.getUploadId())) {
            List<FilePartInfo> partList = filePartInfoMapper.selectList(Wrappers.<FilePartInfo>lambdaQuery()
                    .eq(FilePartInfo::getUploadId, request.getUploadId())
                    .ne(FilePartInfo::getPartNumber, 0)
                    .select(FilePartInfo::getId, FilePartInfo::getPartNumber)
            );

            List<Integer> list = partList.stream()
                    .map(FilePartInfo::getPartNumber)
                    .distinct()
                    .collect(Collectors.toList());
            return new InitMultipartResult(request.getUploadId(), list);
        }
        return new InitMultipartResult();
    }

    private FilePartInfo checkUpload(String uploadId) {
        FilePartInfo initPart = getInitPart(uploadId);
        if (initPart == null) {
            throw new ApiException(ErrorCode.UPLOAD_ID_NOT_FOUND);
        }
        LocalDateTime createTime = initPart.getCreateTime();
        if (partExpireDays > 0 && createTime != null && isExpire(createTime)) {
            throw new ApiException(ErrorCode.UPLOAD_ID_NOT_FOUND);
        }
        return initPart;
    }

    private boolean isExpire(LocalDateTime createTime) {
        return createTime.plusDays(partExpireDays).isBefore(LocalDateTime.now());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void cancelMultipart(CancelPartRequest request) {
        FilePartInfo initPart = getInitPart(request.getUploadId());
        if (initPart == null) {
            return;
        }
        deleteUpload(request.getUploadId(), initPart.getBucketName(), initPart.getObjectName());
    }

    private void deleteUpload(String uploadId, String bucketName, String objectName) {
        fastDfs.deleteFile(bucketName, objectName);
        //清空所有分片记录
        List<String> parts = filePartInfoMapper.selectList(Wrappers.<FilePartInfo>lambdaQuery()
                .eq(FilePartInfo::getUploadId, uploadId)
                .select(FilePartInfo::getId)).stream().map(FilePartInfo::getId).collect(Collectors.toList());
        if (!parts.isEmpty()) {
            filePartInfoMapper.deleteBatchIds(parts);
        }
        filePartInfoMapper.deleteById(uploadId);
    }

    @Override
    public FileInfoResult info(FileInfoRequest request) {
        LambdaQueryWrapper<FileInfo> wrapper = Wrappers.<FileInfo>lambdaQuery()
                .eq(FileInfo::getBucketName, request.getBucketName())
                .eq(FileInfo::getObjectName, request.getObjectName());
        FileInfo fileInfo = fileInfoMapper.selectOne(wrapper);
        if (fileInfo == null) {
            throw new ApiException(ErrorCode.FILE_NOT_FOUND);
        }

        FileInfoResult fileInfoResult = new FileInfoResult();
        BeanUtils.copyProperties(fileInfo, fileInfoResult);

        fileInfoResult.setWebPath(fastDfs.accessUrl(fileInfo.getBucketName(), fileInfo.getObjectName()));
        fileInfoResult.setDownloadPath(
                fastDfs.downLoadUrl(fileInfo.getBucketName(), fileInfo.getObjectName(), fileInfo.getFileName()));
        return fileInfoResult;
    }

    @Override
    public void deleteAllFiles() {
        List<FilePartInfo> filePartInfos = filePartInfoMapper.selectList(Wrappers.emptyWrapper());
        List<FileInfo> fileInfos = fileInfoMapper.selectList(Wrappers.emptyWrapper());
        for (FilePartInfo file : filePartInfos) {
            fastDfs.deleteFile(file.getBucketName(), file.getObjectName());
            if (fastDfs.queryFileInfo(file.getBucketName(), file.getObjectName()) != null) {
                throw new ApiException(ErrorCode.SERVER_ERROR, "删除失败");
            }
            filePartInfoMapper.deleteById(file.getId());
        }

        for (FileInfo file : fileInfos) {
            fastDfs.deleteFile(file.getBucketName(), file.getObjectName());
            if (fastDfs.queryFileInfo(file.getBucketName(), file.getObjectName()) != null) {
                throw new ApiException(ErrorCode.SERVER_ERROR, "删除失败");
            }
            fileInfoMapper.deleteById(file.getId());
        }
    }

    public void clearExpireUpload() {
        List<FilePartInfo> uploads = getPartUploads(partExpireDays);
        while (!uploads.isEmpty()) {
            for (FilePartInfo upload : uploads) {
                clearUpload(upload);
            }
            uploads = getPartUploads(partExpireDays);
        }
    }

    private List<FilePartInfo> getPartUploads(int expireDays) {
        return filePartInfoMapper.selectExpireUploads(expireDays);
    }

    private void clearUpload(FilePartInfo filePartInfo) {
        this.deleteUpload(filePartInfo.getUploadId(), filePartInfo.getBucketName(), filePartInfo.getObjectName());
    }

    private static final ScheduledExecutorService service = Executors.newScheduledThreadPool(1);

    private static final String CLEAR_LOCK_KEY = "FastDfsClearPartTask";

    @Override
    public void onApplicationEvent(ContextRefreshedEvent contextRefreshedEvent) {
        log.info("start clear part service...");
        long evictableSeconds = storageProperties.getFastdfs().getPartEvictableSeconds();
        service.scheduleAtFixedRate(() -> {
            try {
                if (partExpireDays <= 0) {
                    return;
                }
                if (lockClient.tryLock(CLEAR_LOCK_KEY)) {
                    clearExpireUpload();
                }
            } catch (Exception e) {
                log.error("clear part error", e);
            } finally {
                lockClient.unlock(CLEAR_LOCK_KEY);
            }
        }, 0, evictableSeconds, TimeUnit.SECONDS);
    }

    @Configuration
    static class FastDfsClearShutdown implements ApplicationListener<ContextClosedEvent> {

        int awaitTime = 5;

        @Override
        public void onApplicationEvent(ContextClosedEvent contextClosedEvent) {
            log.info("shutdown clear part service soon.");
            service.shutdown();
            try {
                if (!service.awaitTermination(awaitTime, TimeUnit.SECONDS)) {
                    log.warn("Executor did not terminate in the specified time.");
                    service.shutdownNow();
                }
            } catch (Exception e) {
                log.error("stop service awaitTermination failed.", e);
                service.shutdownNow();
            }
        }
    }
}
