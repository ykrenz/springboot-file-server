package com.github.ren.file.client.ali;

import com.aliyun.oss.OSS;
import com.aliyun.oss.model.*;
import com.github.ren.file.client.FileClient;
import com.github.ren.file.client.ex.FileIOException;
import com.github.ren.file.client.model.UploadGenericResult;
import com.github.ren.file.client.part.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * @Description ali oss文件客戶端
 * @Author ren
 * @Since 1.0
 */
public class AliOssClient implements FileClient {

    private static final Logger logger = LoggerFactory.getLogger(AliOssClient.class);

    private OSS oss;

    private String bucketName;

    private int partExpirationDays = -1;

    public AliOssClient(OSS oss, String bucketName) {
        this.oss = oss;
        this.bucketName = bucketName;
    }

    public OSS getOss() {
        return oss;
    }

    public void setOss(OSS oss) {
        this.oss = oss;
    }

    public String getBucketName() {
        return bucketName;
    }

    public void setBucketName(String bucketName) {
        this.bucketName = bucketName;
    }

    /**
     * 设置分片自动过期策略
     *
     * @param expirationDays
     */
    public void setPartExpirationDays(int expirationDays) {
        SetBucketLifecycleRequest request = new SetBucketLifecycleRequest(bucketName);
        LifecycleRule rule = new LifecycleRule(null, null, LifecycleRule.RuleStatus.Enabled);
        LifecycleRule.AbortMultipartUpload abortMultipartUpload = new LifecycleRule.AbortMultipartUpload();
        abortMultipartUpload.setExpirationDays(expirationDays);
        rule.setAbortMultipartUpload(abortMultipartUpload);
        request.AddLifecycleRule(rule);
        this.oss.setBucketLifecycle(request);
        partExpirationDays = expirationDays;
    }

    @Override
    public UploadGenericResult upload(File file, String objectName) {
        PutObjectRequest putObjectRequest = new PutObjectRequest(bucketName, objectName, file);
        PutObjectResult putObjectResult = oss.putObject(putObjectRequest);
        return new UploadGenericResult(objectName, putObjectResult.getETag());
    }

    @Override
    public UploadGenericResult upload(InputStream is, String objectName) {
        PutObjectRequest putObjectRequest = new PutObjectRequest(bucketName, objectName, is);
        PutObjectResult putObjectResult = oss.putObject(putObjectRequest);
        return new UploadGenericResult(objectName, putObjectResult.getETag());
    }

    @Override
    public UploadGenericResult upload(byte[] content, String objectName) {
        try (InputStream is = new ByteArrayInputStream(content)) {
            PutObjectRequest putObjectRequest = new PutObjectRequest(bucketName, objectName, is);
            PutObjectResult putObjectResult = oss.putObject(putObjectRequest);
            return new UploadGenericResult(objectName, putObjectResult.getETag());
        } catch (IOException e) {
            throw new FileIOException("ali oss upload byte[] error", e);
        }
    }

    @Override
    public UploadGenericResult upload(String url, String objectName) {
        try (InputStream is = new URL(url).openStream()) {
            return this.upload(is, objectName);
        } catch (IOException e) {
            throw new FileIOException("ali oss upload url file error", e);
        }
    }

    @Override
    public InitMultipartResponse initMultipartUpload(InitMultipartUploadArgs args) {
        // 创建InitiateMultipartUploadRequest对象。
        String objectName = args.getObjectName();
        InitiateMultipartUploadRequest request = new InitiateMultipartUploadRequest(bucketName, objectName);
        InitiateMultipartUploadResult upresult = oss.initiateMultipartUpload(request);
        // 返回uploadId，它是分片上传事件的唯一标识，您可以根据这个uploadId发起相关的操作，如取消分片上传、查询分片上传等。
        return new InitMultipartResponse(upresult.getUploadId(), upresult.getKey());
    }

    @Override
    public UploadMultipartResponse uploadMultipart(UploadPartArgs part) {
        InputStream inputStream = null;
        try {
            inputStream = part.getInputStream();
            UploadPartRequest uploadPartRequest = new UploadPartRequest();
            uploadPartRequest.setBucketName(bucketName);
            uploadPartRequest.setKey(part.getObjectName());
            uploadPartRequest.setUploadId(part.getUploadId());
            uploadPartRequest.setInputStream(inputStream);
            // 设置分片大小。除了最后一个分片没有大小限制，其他的分片最小为100 KB。
            uploadPartRequest.setPartSize(part.getPartSize());
            // 设置分片号。每一个上传的分片都有一个分片号，取值范围是1~10000，如果超出这个范围，OSS将返回InvalidArgument的错误码。
            uploadPartRequest.setPartNumber(part.getPartNumber());
            // 每个分片不需要按顺序上传，甚至可以在不同客户端上传，OSS会按照分片号排序组成完整的文件。
            UploadPartResult uploadPartResult = oss.uploadPart(uploadPartRequest);
            UploadMultipartResponse uploadMultipartResponse = new UploadMultipartResponse();
            uploadMultipartResponse.setPartSize(uploadPartResult.getPartSize());
            uploadMultipartResponse.setUploadId(part.getUploadId());
            uploadMultipartResponse.setPartNumber(uploadPartResult.getPartNumber());
            uploadMultipartResponse.setETag(uploadPartResult.getETag());
            return uploadMultipartResponse;
        } finally {
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    logger.error("ali oss upload part InputStream close error", e);
                }
            }
        }
    }

    @Override
    public List<UploadMultipartResponse> listMultipartUpload(ListMultipartUploadArgs args) {
        PartListing partListing;
        String uploadId = args.getUploadId();
        String objectName = args.getObjectName();
        ListPartsRequest listPartsRequest = new ListPartsRequest(bucketName, objectName, uploadId);
        List<UploadMultipartResponse> uploadMultipartResponses = new ArrayList<>();
        do {
            partListing = oss.listParts(listPartsRequest);
            for (PartSummary part : partListing.getParts()) {
                UploadMultipartResponse uploadMultipartResponse = new UploadMultipartResponse();
                uploadMultipartResponse.setUploadId(uploadId);
                uploadMultipartResponse.setPartNumber(part.getPartNumber());
                uploadMultipartResponse.setPartSize(part.getSize());
                uploadMultipartResponse.setETag(part.getETag());
                uploadMultipartResponses.add(uploadMultipartResponse);
            }
            // 指定List的起始位置，只有分片号大于此参数值的分片会被列出。
            listPartsRequest.setPartNumberMarker(partListing.getNextPartNumberMarker());
        } while (partListing.isTruncated());
        return uploadMultipartResponses;
    }

    @Override
    public CompleteMultipartResponse completeMultipartUpload(String uploadId, String objectName, List<UploadMultipartResponse> parts) {
        List<PartETag> eTags = new ArrayList<>(parts.size());
        for (UploadMultipartResponse uploadMultipartResponse : parts) {
            PartETag eTag = new PartETag(uploadMultipartResponse.getPartNumber(), uploadMultipartResponse.getETag());
            eTags.add(eTag);
        }
        CompleteMultipartUploadRequest completeMultipartUploadRequest =
                new CompleteMultipartUploadRequest(bucketName, objectName, uploadId, eTags);
        CompleteMultipartUploadResult uploadResult = oss.completeMultipartUpload(completeMultipartUploadRequest);
        return new CompleteMultipartResponse(uploadResult.getETag(), objectName);
    }

    @Override
    public void abortMultipartUpload(String uploadId, String objectName) {
        // 取消分片上传，其中uploadId源自InitiateMultipartUpload。
        AbortMultipartUploadRequest abortMultipartUploadRequest =
                new AbortMultipartUploadRequest(bucketName, objectName, uploadId);
        oss.abortMultipartUpload(abortMultipartUploadRequest);
    }

    @Override
    public int getPartExpirationDays() {
        return partExpirationDays;
    }
}
