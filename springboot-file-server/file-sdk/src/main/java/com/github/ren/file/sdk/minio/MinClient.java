//package com.github.ren.file.sdk.minio;
//
//import io.minio.MinioClient;
//import io.minio.errors.InvalidEndpointException;
//import io.minio.errors.InvalidPortException;
//import okhttp3.HttpUrl;
//import okhttp3.OkHttpClient;
//
//import java.net.URL;
//
///**
// * @Description minio文件客户端
// * @Author ren
// * @Since 1.0
// */
//public class MinClient {
//
//    private MinioClient minioClient;
//
//    public MinClient(String endpoint) throws InvalidEndpointException, InvalidPortException {
//        minioClient = new MinioClient(endpoint);
//    }
//
//    public MinClient(URL url) throws InvalidEndpointException, InvalidPortException {
//        super(url);
//    }
//
//    public MinClient(HttpUrl url) throws InvalidEndpointException, InvalidPortException {
//        super(url);
//    }
//
//    public MinClient(String endpoint, String accessKey, String secretKey) throws InvalidEndpointException, InvalidPortException {
//        super(endpoint, accessKey, secretKey);
//    }
//
//    public MinClient(String endpoint, String accessKey, String secretKey, String region) throws InvalidEndpointException, InvalidPortException {
//        super(endpoint, accessKey, secretKey, region);
//    }
//
//    public MinClient(URL url, String accessKey, String secretKey) throws InvalidEndpointException, InvalidPortException {
//        super(url, accessKey, secretKey);
//    }
//
//    public MinClient(HttpUrl url, String accessKey, String secretKey) throws InvalidEndpointException, InvalidPortException {
//        super(url, accessKey, secretKey);
//    }
//
//    public MinClient(String endpoint, int port, String accessKey, String secretKey) throws InvalidEndpointException, InvalidPortException {
//        super(endpoint, port, accessKey, secretKey);
//    }
//
//    public MinClient(String endpoint, String accessKey, String secretKey, boolean secure) throws InvalidEndpointException, InvalidPortException {
//        super(endpoint, accessKey, secretKey, secure);
//    }
//
//    public MinClient(String endpoint, int port, String accessKey, String secretKey, boolean secure) throws InvalidEndpointException, InvalidPortException {
//        super(endpoint, port, accessKey, secretKey, secure);
//    }
//
//    public MinClient(String endpoint, int port, String accessKey, String secretKey, String region, boolean secure) throws InvalidEndpointException, InvalidPortException {
//        super(endpoint, port, accessKey, secretKey, region, secure);
//    }
//
//    public MinClient(String endpoint, int port, String accessKey, String secretKey, String region, boolean secure, OkHttpClient httpClient) throws InvalidEndpointException, InvalidPortException {
//        super(endpoint, port, accessKey, secretKey, region, secure, httpClient);
//    }
//}
