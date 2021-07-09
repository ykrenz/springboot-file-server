package com.github.ren.file.client.s3;

import com.github.ren.file.client.fdfs.FastDfsClientBuilder;
import com.github.ren.file.client.fdfs.FastDfsStorageClient;
import com.github.ren.file.client.local.LocalFileOperation;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class Test {
    static String objectPath = "E:\\Xftp-7.0.0071p.exe";

    public static void main(String[] args) throws ExecutionException, InterruptedException, IOException {
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

        FastDfsStorageClient storageClient = new FastDfsClientBuilder().build("10.10.10.76:22122");

//        ClientGlobal.g_connection_pool_max_count_per_entry = 100000;
//        String file = storageClient.uploadFile(new File(objectPath), new NameValuePair[]{
//                new NameValuePair("nextPartNumber", "1")
//        });
//        ExecutorService executorService = Executors.newFixedThreadPool(10000);
//
//        System.out.println(file);
//        for (int i = 0; i < 100; i++) {
//            int j = 1;
//            executorService.submit(() -> {
//                String value = String.valueOf(++j);
//                boolean metadata = storageClient.mergeMetadata(file, new NameValuePair[]{
//                        new NameValuePair("nextPartNumber", value)
//                });
//                System.out.println("nextPartNumber=" + value);
//            });
//        }
//
//        for (int i = 0; i < 100; i++) {
//            executorService.submit(() -> {
//                NameValuePair[] metadata = storageClient.getMetadata(file);
//                if (metadata == null) {
//                    throw new RuntimeException("metadata为空了");
//                }
//                System.out.println(metadata[0].getName() + metadata[0].getValue());
//            });
//        }
//
//        System.out.println("======================>>");
//        long start = System.currentTimeMillis();
//        for (int i = 0; i < 1000; i++) {
//            NameValuePair[] metadata = storageClient.getMetadata(file);
//            for (NameValuePair metadatum : metadata) {
//                System.out.println("key=" + metadatum.getName() + "value=" + metadatum.getValue());
//            }
//        }
//        long end = System.currentTimeMillis();
//        System.out.println(end - start);
//        executorService.awaitTermination(10, TimeUnit.SECONDS);
//        System.out.println(storageClient.deleteFile(file));
//        executorService.shutdown();

//        UploadResponse uploadResponse2 = storageClient.uploadFile(new File(objectPath));
//        System.out.println(uploadResponse2.getFilePath());
//        System.out.println(storageClient.deleteFile(uploadResponse2.getGroup(), uploadResponse1.getPath()));
//        System.out.println(uploadResponse2.trackerServer().getInetSocketAddress().getHostName());

//        FastDfsStorageClient storageClient2 = new FastDfsClientBuilder2().build("10.10.10.178:22122");
//        UploadResponse uploadResponse = storageClient2.uploadFile(new File(objectPath));
//        System.out.println(storageClient2.deleteFile(uploadResponse.getFilePath()));

        File file = new File(objectPath);
        long length = file.length();
        long partSize = 1024 * 1024 * 5;
        String filePath = storageClient.initiateMultipartUpload(length, partSize, "exe");

        List<File> files = LocalFileOperation.chunkFile(file, "F:\\oss\\chunk", partSize);

        for (File file1 : files) {
            storageClient.uploadPart(filePath, Integer.parseInt(file1.getName()), file1.getAbsolutePath());
        }
        String upload = storageClient.completeMultipartUpload(filePath);
        System.out.println(upload);
        System.out.println(storageClient.abortMultipartUpload(filePath));
        System.out.println(storageClient.abortMultipartUpload(upload));
    }
}