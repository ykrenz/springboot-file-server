package com.github.ren.file.client.s3;

import com.github.ren.file.client.fdfs.FastDfsClientBuilder2;
import com.github.ren.file.client.fdfs.FastDfsStorageClient;

import java.io.File;

public class Test {
    static String objectPath = "E:\\Xftp-7.0.0071p.exe";

    public static void main(String[] args) {
//        AWSCredentials credentials = new BasicAWSCredentials("LTAI5tHxmHNVkR5RHmD5xjyU","35iSNN2FJCJyRVw7nRfCGYJKJ7Lzga");
//
//        AmazonS3 s3Client = AmazonS3ClientBuilder.standard()
//                .withCredentials(new AWSStaticCredentialsProvider(credentials))
//                .withEndpointConfiguration(new AwsClientBuilder.EndpointConfiguration("oss-cn-hangzhou.aliyuncs.com","oss-cn-hangzhou")).build();
//
//        S3CommonApi s3CommonApi = new S3(s3Client);
//        CreateBucketRequest request = new CreateBucketRequest("renyin2", Region.valueOf("oss-cn-hangzhou"));
//        Bucket bucket = s3CommonApi.createBucket(request);
//


        FastDfsStorageClient storageClient = new FastDfsClientBuilder2().build("10.10.10.76:22122","group2");
        String file = storageClient.uploadFile(new File(objectPath));
        System.out.println(file);
        System.out.println(storageClient.deleteFile(file));

//        UploadResponse uploadResponse2 = storageClient.uploadFile(new File(objectPath));
//        System.out.println(uploadResponse2.getFilePath());
//        System.out.println(storageClient.deleteFile(uploadResponse2.getGroup(), uploadResponse1.getPath()));
//        System.out.println(uploadResponse2.trackerServer().getInetSocketAddress().getHostName());

//        FastDfsStorageClient storageClient2 = new FastDfsClientBuilder2().build("10.10.10.178:22122");
//        UploadResponse uploadResponse = storageClient2.uploadFile(new File(objectPath));
//        System.out.println(storageClient2.deleteFile(uploadResponse.getFilePath()));
    }
}