//package com.github.ren.file.sdk.ali;
//
//import com.aliyun.oss.OSS;
//import com.aliyun.oss.model.*;
//import com.github.ren.file.sdk.FileClient;
//import com.github.ren.file.sdk.FileIOException;
//import com.github.ren.file.sdk.part.CompleteMultipart;
//import com.github.ren.file.sdk.part.PartInfo;
//import com.github.ren.file.sdk.part.UploadPart;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//
//import java.io.ByteArrayInputStream;
//import java.io.File;
//import java.io.IOException;
//import java.io.InputStream;
//import java.net.URL;
//import java.util.ArrayList;
//import java.util.Comparator;
//import java.util.List;
//
///**
// * @Description ali oss文件客戶端
// * @Author ren
// * @Since 1.0
// */
//public class AliClient implements FileClient {
//
//    private static final Logger logger = LoggerFactory.getLogger(AliClient.class);
//
//    private OSS oss;
//
//    private String bucketName;
//
//    public AliClient(OSS oss, String bucketName) {
//        this.oss = oss;
//        this.bucketName = bucketName;
//    }
//
//    public OSS getOss() {
//        return oss;
//    }
//
//    public void setOss(OSS oss) {
//        this.oss = oss;
//    }
//
//    public String getBucketName() {
//        return bucketName;
//    }
//
//    public void setBucketName(String bucketName) {
//        this.bucketName = bucketName;
//    }
//
//    @Override
//    public String upload(File file, String yourObjectName) {
//        PutObjectRequest putObjectRequest = new PutObjectRequest(bucketName, yourObjectName, file);
//        oss.putObject(putObjectRequest);
//        return yourObjectName;
//    }
//
//    @Override
//    public String upload(InputStream is, String yourObjectName) {
//        PutObjectRequest putObjectRequest = new PutObjectRequest(bucketName, yourObjectName, is);
//        oss.putObject(putObjectRequest);
//        return yourObjectName;
//    }
//
//    @Override
//    public String upload(byte[] content, String yourObjectName) {
//        try (InputStream is = new ByteArrayInputStream(content)) {
//            PutObjectRequest putObjectRequest = new PutObjectRequest(bucketName, yourObjectName, is);
//            oss.putObject(putObjectRequest);
//            return yourObjectName;
//        } catch (IOException e) {
//            throw new FileIOException("ali oss upload byte[] error", e);
//        }
//    }
//
//    @Override
//    public String upload(String url, String yourObjectName) {
//        try (InputStream is = new URL(url).openStream()) {
//            return this.upload(is, yourObjectName);
//        } catch (IOException e) {
//            throw new FileIOException("ali oss upload url file error", e);
//        }
//    }
//
//    @Override
//    public String initiateMultipartUpload(String yourObjectName) {
//        // 创建InitiateMultipartUploadRequest对象。
//        InitiateMultipartUploadRequest request = new InitiateMultipartUploadRequest(bucketName, yourObjectName);
//        InitiateMultipartUploadResult upresult = oss.initiateMultipartUpload(request);
//        // 返回uploadId，它是分片上传事件的唯一标识，您可以根据这个uploadId发起相关的操作，如取消分片上传、查询分片上传等。
//        return upresult.getUploadId();
//    }
//
//    @Override
//    public void uploadPart(UploadPart part) {
//        InputStream inputStream = null;
//        try {
//            inputStream = part.getInputStream();
//            UploadPartRequest uploadPartRequest = new UploadPartRequest();
//            uploadPartRequest.setBucketName(bucketName);
//            uploadPartRequest.setKey(part.getKey());
//            uploadPartRequest.setUploadId(part.getUploadId());
//            uploadPartRequest.setInputStream(inputStream);
//            // 设置分片大小。除了最后一个分片没有大小限制，其他的分片最小为100 KB。
//            uploadPartRequest.setPartSize(part.getPartSize());
//            // 设置分片号。每一个上传的分片都有一个分片号，取值范围是1~10000，如果超出这个范围，OSS将返回InvalidArgument的错误码。
//            uploadPartRequest.setPartNumber(part.getPartNumber());
//            // 每个分片不需要按顺序上传，甚至可以在不同客户端上传，OSS会按照分片号排序组成完整的文件。
//            oss.uploadPart(uploadPartRequest);
//        } finally {
//            if (inputStream != null) {
//                try {
//                    inputStream.close();
//                } catch (IOException e) {
//                    logger.error("ali oss upload part InputStream close error", e);
//                }
//            }
//        }
//    }
//
//    @Override
//    public List<PartInfo> listParts(String uploadId, String yourObjectName) {
//        PartListing partListing;
//        ListPartsRequest listPartsRequest = new ListPartsRequest(bucketName, yourObjectName, uploadId);
//        List<PartInfo> partInfos = new ArrayList<>();
//        do {
//            partListing = oss.listParts(listPartsRequest);
//            for (PartSummary part : partListing.getParts()) {
//                PartInfo partInfo = new PartInfo();
//                partInfo.setUploadId(uploadId);
//                partInfo.setPartNumber(part.getPartNumber());
//                partInfo.setPartSize(part.getSize());
//                partInfo.setETag(part.getETag());
//                partInfos.add(partInfo);
//            }
//            // 指定List的起始位置，只有分片号大于此参数值的分片会被列出。
//            listPartsRequest.setPartNumberMarker(partListing.getNextPartNumberMarker());
//        } while (partListing.isTruncated());
//        return partInfos;
//    }
//
//    @Override
//    public CompleteMultipart completeMultipartUpload(String uploadId, String yourObjectName) {
//        List<PartInfo> partInfos = listParts(uploadId, yourObjectName);
//        List<PartETag> eTags = new ArrayList<>(partInfos.size());
//        for (PartInfo partInfo : partInfos) {
//            PartETag eTag = new PartETag(partInfo.getPartNumber(), partInfo.getETag());
//            eTags.add(eTag);
//        }
//        /* 步骤3：完成分片上传。 */
//        // 排序。partETags必须按分片号升序排列。
//        eTags.sort(Comparator.comparingInt(PartETag::getPartNumber));
//        // 在执行该操作时，需要提供所有有效的partETags。OSS收到提交的partETags后，会逐一验证每个分片的有效性。当所有的数据分片验证通过后，OSS将把这些分片组合成一个完整的文件。
//        CompleteMultipartUploadRequest completeMultipartUploadRequest =
//                new CompleteMultipartUploadRequest(bucketName, yourObjectName, uploadId, eTags);
//        CompleteMultipartUploadResult uploadResult = oss.completeMultipartUpload(completeMultipartUploadRequest);
//        return new CompleteMultipart(uploadResult.getETag(), yourObjectName);
//    }
//
//    @Override
//    public void abortMultipartUpload(String uploadId, String yourObjectName) {
//        // 取消分片上传，其中uploadId源自InitiateMultipartUpload。
//        AbortMultipartUploadRequest abortMultipartUploadRequest =
//                new AbortMultipartUploadRequest(bucketName, yourObjectName, uploadId);
//        oss.abortMultipartUpload(abortMultipartUploadRequest);
//    }
//}
