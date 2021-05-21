//package com.github.ren.file.sdk.minio;
//
//import com.github.ren.file.sdk.FileClient;
//import com.github.ren.file.sdk.model.UploadGenericResult;
//import com.github.ren.file.sdk.part.CompleteMultipart;
//import com.github.ren.file.sdk.part.PartInfo;
//import com.github.ren.file.sdk.part.UploadPart;
//import io.minio.MinioClient;
//
//import java.io.File;
//import java.io.InputStream;
//import java.util.List;
//
///**
// * @Description minio文件客户端
// * @Author ren
// * @Since 1.0
// */
//public class MinClient implements FileClient {
//
//    @Override
//    public UploadGenericResult upload(File file, String yourObjectName) {
//        return null;
//    }
//
//    @Override
//    public UploadGenericResult upload(InputStream is, String yourObjectName) {
//        return null;
//    }
//
//    @Override
//    public UploadGenericResult upload(byte[] content, String yourObjectName) {
//        return null;
//    }
//
//    @Override
//    public UploadGenericResult upload(String url, String yourObjectName) {
//        return null;
//    }
//
//    @Override
//    public String initiateMultipartUpload(String yourObjectName) {
//        return null;
//    }
//
//    @Override
//    public PartInfo uploadPart(UploadPart part) {
//        MinioClient minioClient = MinioClient.builder().endpoint("192.168.231.140:9000")
//                .credentials("admin","12345678")
//                .build();
////        ComposeObjectArgs build = ComposeObjectArgs.builder()
////                .bucket("my-bucketname")
////                .object("my-objectname")
////                .sources(sourceObjectList)
////                .build();
////        minioClient.composeObject()
//        return null;
//    }
//
//    @Override
//    public List<PartInfo> listParts(String uploadId, String yourObjectName) {
//        return null;
//    }
//
//    @Override
//    public CompleteMultipart completeMultipartUpload(String uploadId, String yourObjectName) {
//        return null;
//    }
//
//    @Override
//    public CompleteMultipart completeMultipartUpload(String uploadId, String yourObjectName, String md5) {
//        return null;
//    }
//
//    @Override
//    public void abortMultipartUpload(String uploadId, String yourObjectName) {
//
//    }
//}
