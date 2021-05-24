package com.github.ren.file.sdk.ali;

import com.aliyun.oss.OSS;
import com.aliyun.oss.model.*;
import com.github.ren.file.sdk.FileClient;
import com.github.ren.file.sdk.ex.FileIOException;
import com.github.ren.file.sdk.model.UploadGenericResult;
import com.github.ren.file.sdk.part.CompleteMultipart;
import com.github.ren.file.sdk.part.InitMultipartResult;
import com.github.ren.file.sdk.part.PartInfo;
import com.github.ren.file.sdk.part.UploadPart;
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
public class AliClient implements FileClient {

    private static final Logger logger = LoggerFactory.getLogger(AliClient.class);

    private OSS oss;

    private String bucketName;

    private static class SingletonHolder {
        private static final AliClient INSTANCE = new AliClient();
    }

    public static AliClient getInstance() {
        return AliClient.SingletonHolder.INSTANCE;
    }

    private AliClient() {
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
    public InitMultipartResult initiateMultipartUpload(String objectName) {
        // 创建InitiateMultipartUploadRequest对象。
        InitiateMultipartUploadRequest request = new InitiateMultipartUploadRequest(bucketName, objectName);
        InitiateMultipartUploadResult upresult = oss.initiateMultipartUpload(request);
        // 返回uploadId，它是分片上传事件的唯一标识，您可以根据这个uploadId发起相关的操作，如取消分片上传、查询分片上传等。
        return new InitMultipartResult(upresult.getUploadId(), upresult.getKey());
    }

    @Override
    public PartInfo uploadPart(UploadPart part) {
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
            PartInfo partInfo = new PartInfo();
            partInfo.setPartSize(uploadPartResult.getPartSize());
            partInfo.setUploadId(part.getUploadId());
            partInfo.setPartNumber(uploadPartResult.getPartNumber());
            partInfo.setETag(uploadPartResult.getETag());
            return partInfo;
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
    public List<PartInfo> listParts(String uploadId, String objectName) {
        PartListing partListing;
        ListPartsRequest listPartsRequest = new ListPartsRequest(bucketName, objectName, uploadId);
        List<PartInfo> partInfos = new ArrayList<>();
        do {
            partListing = oss.listParts(listPartsRequest);
            for (PartSummary part : partListing.getParts()) {
                PartInfo partInfo = new PartInfo();
                partInfo.setUploadId(uploadId);
                partInfo.setPartNumber(part.getPartNumber());
                partInfo.setPartSize(part.getSize());
                partInfo.setETag(part.getETag());
                partInfos.add(partInfo);
            }
            // 指定List的起始位置，只有分片号大于此参数值的分片会被列出。
            listPartsRequest.setPartNumberMarker(partListing.getNextPartNumberMarker());
        } while (partListing.isTruncated());
        return partInfos;
    }

    @Override
    public CompleteMultipart completeMultipartUpload(String uploadId, String objectName, List<PartInfo> parts) {
        List<PartETag> eTags = new ArrayList<>(parts.size());
        for (PartInfo partInfo : parts) {
            PartETag eTag = new PartETag(partInfo.getPartNumber(), partInfo.getETag());
            eTags.add(eTag);
        }
        CompleteMultipartUploadRequest completeMultipartUploadRequest =
                new CompleteMultipartUploadRequest(bucketName, objectName, uploadId, eTags);
        CompleteMultipartUploadResult uploadResult = oss.completeMultipartUpload(completeMultipartUploadRequest);
        return new CompleteMultipart(uploadResult.getETag(), objectName);
    }

    @Override
    public void abortMultipartUpload(String uploadId, String objectName) {
        // 取消分片上传，其中uploadId源自InitiateMultipartUpload。
        AbortMultipartUploadRequest abortMultipartUploadRequest =
                new AbortMultipartUploadRequest(bucketName, objectName, uploadId);
        oss.abortMultipartUpload(abortMultipartUploadRequest);
    }
}
