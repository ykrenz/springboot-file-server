package com.github.ren.file.clients;

import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import com.aliyun.oss.model.*;
import com.github.ren.file.properties.AliYunOssProperties;
import com.github.tobato.fastdfs.domain.proto.storage.DownloadCallback;
import org.apache.commons.lang3.StringUtils;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/**
 * 阿里云oss实现类
 */
public class AliOssFileClient extends AbstractServerClient implements AliOssClient {
    private final AliYunOssProperties aliYunOssProperties;

    private final String bucketName;

    public AliOssFileClient(AliYunOssProperties aliYunOssProperties) {
        this.aliYunOssProperties = aliYunOssProperties;
        this.bucketName = aliYunOssProperties.getBucketName();
    }

    @Override
    public OSS getOssClient() {
        return new OSSClientBuilder()
                .build(aliYunOssProperties.getEndpoint(),
                        aliYunOssProperties.getAccessKeyId(),
                        aliYunOssProperties.getAccessKeySecret());
    }

    @Override
    public String getWebServerUrl() {
        return aliYunOssProperties.getWebServerUrl();
    }

    @Override
    public boolean isExist(String objectName) {
        // 创建OSSClient实例。
        OSS ossClient = this.getOssClient();
        // 判断文件是否存在。如果返回值为true，则文件存在，否则存储空间或者文件不存在。
        // 设置是否进行重定向或者镜像回源。默认值为true，表示忽略302重定向和镜像回源；如果设置isINoss为false，则进行302重定向或者镜像回源。
        //boolean isINoss = true;
        // 填写Bucket名称和Object完整路径。Object完整路径中不能包含Bucket名称。
        boolean found = ossClient.doesObjectExist(bucketName, objectName);
        //boolean found = ossClient.doesObjectExist("examplebucket", "exampleobject.txt", isINoss);
        ossClient.shutdown();
        return found;
    }

    @Override
    public String uploadFile(File file, String yourObjectName) {
        // 创建OSSClient实例。
        OSS ossClient = this.getOssClient();
        // 创建PutObjectRequest对象。
        // <yourObjectName>表示上传文件到OSS时需要指定包含文件后缀在内的完整路径，例如abc/efg/123.jpg。
        PutObjectRequest putObjectRequest = new PutObjectRequest(bucketName, yourObjectName, file);
        // 如果需要上传时设置存储类型与访问权限，请参考以下示例代码。
        ObjectMetadata metadata = new ObjectMetadata();
        metadata.setContentDisposition("attachment;fileName=" + file.getName());
        metadata.setContentType(getContentType(file));
        // metadata.setHeader(OSSHeaders.OSS_STORAGE_CLASS, StorageClass.Standard.toString());
        // metadata.setObjectAcl(CannedAccessControlList.Private);
        // putObjectRequest.setMetadata(metadata);
        // 上传
        ossClient.putObject(putObjectRequest);
        // 关闭OSSClient。
        ossClient.shutdown();
        return yourObjectName;
    }

    @Override
    public String uploadFile(InputStream is, String yourObjectName) {
        try {
            // 创建OSSClient实例。
            OSS ossClient = this.getOssClient();
            // 创建PutObjectRequest对象。
            // <yourObjectName>表示上传文件到OSS时需要指定包含文件后缀在内的完整路径，例如abc/efg/123.jpg。
            PutObjectRequest putObjectRequest = new PutObjectRequest(bucketName, yourObjectName, is);
            // 如果需要上传时设置存储类型与访问权限，请参考以下示例代码。
            // ObjectMetadata metadata = new ObjectMetadata();
            // metadata.setHeader(OSSHeaders.OSS_STORAGE_CLASS, StorageClass.Standard.toString());
            // metadata.setObjectAcl(CannedAccessControlList.Private);
            // putObjectRequest.setMetadata(metadata);
            // 上传字符串。
            ossClient.putObject(putObjectRequest);
            // 关闭OSSClient。
            ossClient.shutdown();
            return yourObjectName;
        } finally {
            super.close(is);
        }
    }

    @Override
    public String uploadFile(byte[] content, String yourObjectName) {
        OSS ossClient = this.getOssClient();
        ossClient.putObject(bucketName, yourObjectName, new ByteArrayInputStream(content));
        ossClient.shutdown();
        return yourObjectName;
    }

    @Override
    public String uploadPart(List<UploadPart> parts, String yourObjectName) {
        try {
            OSS ossClient = this.getOssClient();
            parts.sort(Comparator.comparingInt(UploadPart::getPartNumber));
            //步骤1：初始化一个分片上传事件。
            InitiateMultipartUploadRequest request = new InitiateMultipartUploadRequest(bucketName, yourObjectName);
            InitiateMultipartUploadResult result = ossClient.initiateMultipartUpload(request);
            // 返回uploadId，它是分片上传事件的唯一标识，您可以根据这个ID来发起相关的操作，如取消分片上传、查询分片上传等。
            String uploadId = result.getUploadId();
            // partETags是PartETag的集合。PartETag由分片的ETag和分片号组成。
            List<PartETag> eTags = new ArrayList<PartETag>();
            parts.forEach(uploadPart -> {
                UploadPartRequest uploadPartRequest = new UploadPartRequest();

                uploadPartRequest.setBucketName(bucketName);
                uploadPartRequest.setKey(yourObjectName);
                uploadPartRequest.setUploadId(uploadId);
                uploadPartRequest.setInputStream(uploadPart.getInputStream());
                // 设置分片大小。除了最后一个分片没有大小限制，其他的分片最小为100KB。
                uploadPartRequest.setPartSize(uploadPart.getPartSize());
                // 设置分片号。每一个上传的分片都有一个分片号，取值范围是1~10000，如果超出这个范围，OSS将返回InvalidArgument的错误码。
                uploadPartRequest.setPartNumber(uploadPart.getPartNumber());
                // 每个分片不需要按顺序上传，甚至可以在不同客户端上传，OSS会按照分片号排序组成完整的文件。
                UploadPartResult uploadPartResult = ossClient.uploadPart(uploadPartRequest);
                // 每次上传分片之后，OSS的返回结果会包含一个PartETag。PartETag将被保存到partETags中。
                eTags.add(uploadPartResult.getPartETag());
            });
            /* 步骤3：完成分片上传。 */
            // 排序。partETags必须按分片号升序排列。
            eTags.sort(Comparator.comparingInt(PartETag::getPartNumber));
            // 在执行该操作时，需要提供所有有效的partETags。OSS收到提交的partETags后，会逐一验证每个分片的有效性。当所有的数据分片验证通过后，OSS将把这些分片组合成一个完整的文件。
            CompleteMultipartUploadRequest completeMultipartUploadRequest =
                    new CompleteMultipartUploadRequest(bucketName, yourObjectName, uploadId, eTags);
            CompleteMultipartUploadResult uploadResult = ossClient.completeMultipartUpload(completeMultipartUploadRequest);
            ossClient.shutdown();
        } finally {
            super.closePartsStream(parts);
        }
        return yourObjectName;
    }

    @Override
    public void deleteFile(String yourObjectName) {
        // 创建OSSClient实例。
        OSS ossClient = this.getOssClient();
        // 删除文件。如需删除文件夹，请将ObjectName设置为对应的文件夹名称。如果文件夹非空，则需要将文件夹下的所有object删除后才能删除该文件夹。
        ossClient.deleteObject(bucketName, yourObjectName);
        // 关闭OSSClient。
        ossClient.shutdown();
    }

    @Override
    public void downloadFile(String objectName, File file) {
        // 创建OSSClient实例。
        OSS ossClient = this.getOssClient();
        // 下载OSS文件到本地文件。如果指定的本地文件存在会覆盖，不存在则新建。
        ossClient.getObject(new GetObjectRequest(bucketName, objectName), file);
    }

    @Override
    public <T> T downloadFile(String objectName, DownloadCallback<T> callback) {
        OSS ossClient = this.getOssClient();
        OSSObject object = ossClient.getObject(bucketName, objectName);
        try {
            return callback.recv(object.getObjectContent());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public <T> T downloadFile(String objectName, long fileOffset, long fileSize, DownloadCallback<T> callback) {
        OSS ossClient = this.getOssClient();
        GetObjectRequest getObjectRequest = new GetObjectRequest(bucketName, objectName);
        // 对于大小为1000 Bytes的文件，正常的字节范围为0~999。
        // 获取0~999字节范围内的数据，包括0和999，共1000个字节的数据。如果指定的范围无效（比如开始或结束位置的指定值为负数，或指定值大于文件大小），则下载整个文件。
        getObjectRequest.setRange(fileOffset, fileSize);
        OSSObject object = ossClient.getObject(getObjectRequest);
        try {
            return callback.recv(object.getObjectContent());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void downloadFileCheckpoint(String objectName, String downloadFile) {
        this.downloadFileCheckpoint(objectName, downloadFile, null, null, null);
    }

    @Override
    public void downloadFileCheckpoint(String objectName, String downloadFile, Long partSize, Integer taskNum, String checkpointFile) {
        // Endpoint以杭州为例，其它Region请按实际情况填写。
        // 创建OSSClient实例。
        OSS ossClient = this.getOssClient();
        // 下载请求，10个任务并发下载，启动断点续传。
        DownloadFileRequest downloadFileRequest = new DownloadFileRequest(bucketName, objectName);
        downloadFileRequest.setDownloadFile(downloadFile);
        if (partSize != null && partSize > 0) {
            downloadFileRequest.setPartSize(partSize);
        }
        if (taskNum != null && taskNum > 0) {
            downloadFileRequest.setTaskNum(10);
        }
        if (StringUtils.isNotBlank(checkpointFile)) {
            downloadFileRequest.setCheckpointFile(checkpointFile);
        }
        downloadFileRequest.setEnableCheckpoint(true);
        // 下载文件。
        DownloadFileResult downloadRes = null;
        try {
            downloadRes = ossClient.downloadFile(downloadFileRequest);
        } catch (Throwable throwable) {
            throw new RuntimeException(throwable);
        }
        // 下载成功时，会返回文件元信息。
        downloadRes.getObjectMetadata();
        // 关闭OSSClient。
        ossClient.shutdown();
    }
}
