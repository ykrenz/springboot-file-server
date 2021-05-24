package com.github.ren.file.sdk.minio;

import com.github.ren.file.sdk.FileClient;
import com.github.ren.file.sdk.ex.ClientException;
import com.github.ren.file.sdk.ex.FileIOException;
import com.github.ren.file.sdk.model.UploadGenericResult;
import com.github.ren.file.sdk.part.CompleteMultipart;
import com.github.ren.file.sdk.part.InitMultipartResult;
import com.github.ren.file.sdk.part.PartInfo;
import com.github.ren.file.sdk.part.UploadPart;
import io.minio.*;
import io.minio.messages.Part;

import java.io.*;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * @Description minio文件客户端
 * @Author ren
 * @Since 1.0
 */
public class MinIoClient extends MinioClient implements FileClient {

    private String bucketName;

    protected MinIoClient(MinioClient client) {
        super(client);
    }

    public static MinIoClient build(MinioClient minioClient) {
        return new MinIoClient(minioClient);
    }

    public String getBucketName() {
        return bucketName;
    }

    public void setBucketName(String bucketName) {
        this.bucketName = bucketName;
    }

    @Override
    public UploadGenericResult upload(File file, String objectName) {
        try {
            UploadObjectArgs uploadObjectArgs = UploadObjectArgs
                    .builder()
                    .bucket(bucketName)
                    .object(objectName)
                    .filename(file.getAbsolutePath())
                    .build();
            ObjectWriteResponse objectWriteResponse = super.uploadObject(uploadObjectArgs);
            String etag = objectWriteResponse.etag();
            return new UploadGenericResult(objectName, etag);
        } catch (Exception e) {
            throw new ClientException(e);
        }
    }

    @Override
    public UploadGenericResult upload(InputStream is, String objectName) {
        try {
            PutObjectArgs putObjectArgs = PutObjectArgs
                    .builder()
                    .bucket(bucketName)
                    .object(objectName)
                    .stream(is, -1, 10485760)
                    .build();
            ObjectWriteResponse objectWriteResponse = super.putObject(putObjectArgs);
            String etag = objectWriteResponse.etag();
            return new UploadGenericResult(objectName, etag);
        } catch (Exception e) {
            throw new ClientException(e);
        }
    }

    @Override
    public UploadGenericResult upload(byte[] content, String objectName) {
        try (InputStream is = new ByteArrayInputStream(content)) {
            PutObjectArgs putObjectArgs = PutObjectArgs
                    .builder()
                    .bucket(bucketName)
                    .object(objectName)
                    .stream(is, -1, 10485760)
                    .build();
            ObjectWriteResponse objectWriteResponse = super.putObject(putObjectArgs);
            String etag = objectWriteResponse.etag();
            return new UploadGenericResult(objectName, etag);
        } catch (Exception e) {
            throw new ClientException(e);
        }
    }

    @Override
    public UploadGenericResult upload(String url, String objectName) {
        try (InputStream is = new URL(url).openStream()) {
            return this.upload(is, objectName);
        } catch (IOException e) {
            throw new FileIOException("minio upload url file error", e);
        }
    }

    @Override
    public InitMultipartResult initiateMultipartUpload(String objectName) {
        try {
            CreateMultipartUploadResponse multipartUpload = super.createMultipartUpload(bucketName, null, objectName, null, null);
            String uploadId = multipartUpload.result().uploadId();
            objectName = multipartUpload.result().objectName();
            return new InitMultipartResult(uploadId, objectName);
        } catch (Exception e) {
            throw new ClientException(e);
        }
    }

    @Override
    public PartInfo uploadPart(UploadPart part) {
        try {
            String objectName = part.getObjectName();
            InputStream inputStream = part.getInputStream();
            int partSize = (int) part.getPartSize();
            String uploadId = part.getUploadId();
            int partNumber = part.getPartNumber();
            BufferedInputStream bufferedInputStream = new BufferedInputStream(inputStream);
            UploadPartResponse uploadPartResponse = super.uploadPart(bucketName, null, objectName, bufferedInputStream, partSize, uploadId, partNumber, null, null);
            String etag = uploadPartResponse.etag();
            PartInfo partInfo = new PartInfo();
            partInfo.setPartNumber(partNumber);
            partInfo.setPartSize(partSize);
            partInfo.setUploadId(uploadId);
            partInfo.setETag(etag);
            return partInfo;
        } catch (Exception e) {
            throw new ClientException(e);
        }
    }

    @Override
    public List<PartInfo> listParts(String uploadId, String objectName) {
        try {
            ListPartsResponse listPartsResponse;
            Integer partNumberMarker = null;
            List<PartInfo> partInfos = new ArrayList<>();
            do {
                listPartsResponse = super.listParts(bucketName, null, objectName, partNumberMarker, null, uploadId, null, null);
                for (Part part : listPartsResponse.result().partList()) {
                    PartInfo partInfo = new PartInfo();
                    partInfo.setUploadId(uploadId);
                    partInfo.setPartNumber(part.partNumber());
                    partInfo.setPartSize(part.partSize());
                    partInfo.setETag(part.etag());
                    partInfos.add(partInfo);
                }
                // 指定List的起始位置，只有分片号大于此参数值的分片会被列出。
                partNumberMarker = listPartsResponse.result().nextPartNumberMarker();
            } while (listPartsResponse.result().isTruncated());
            return partInfos;
        } catch (Exception e) {
            throw new ClientException(e);
        }
    }

    @Override
    public CompleteMultipart completeMultipartUpload(String uploadId, String objectName, List<PartInfo> parts) {
        try {
            List<Part> partList = new ArrayList<>(parts.size());
            for (PartInfo partInfo : parts) {
                Part part = new Part(partInfo.getPartNumber(), partInfo.getETag());
                partList.add(part);
            }
            Part[] partArr = partList.toArray(new Part[]{});
            ObjectWriteResponse objectWriteResponse = super.completeMultipartUpload(bucketName, null, objectName, uploadId, partArr, null, null);
            String etag = objectWriteResponse.etag();
            return new CompleteMultipart(etag, objectName);
        } catch (Exception e) {
            throw new ClientException(e);
        }
    }

    @Override
    public void abortMultipartUpload(String uploadId, String objectName) {
        try {
            super.abortMultipartUpload(bucketName, null, objectName, uploadId, null, null);
        } catch (Exception e) {
            throw new ClientException(e);
        }
    }
}
