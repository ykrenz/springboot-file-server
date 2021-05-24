package com.github.ren.file;

import com.github.ren.file.sdk.FileClient;
import com.github.ren.file.sdk.minio.MinIoClient;
import io.minio.MinioClient;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@EnableTransactionManagement
@SpringBootApplication
public class FileServerApplication {

    public static void main(String[] args) {
        SpringApplication.run(FileServerApplication.class, args);
    }

    @Bean
    public FileClient fileClient() {
//        return new LocalClient("F:\\oss\\upload",new LocalPartStore("F:\\oss\\part"));
//        return FastDFSClient.getInstance();
//        AliClient aliClient = AliClient.getInstance();
//        OSS oss = new OSSClientBuilder()
//                .build("",
//                        "",
//                        "");
//        aliClient.setOss(oss);
//        aliClient.setBucketName("");
//        return aliClient;

        MinioClient client = MinioClient.builder().endpoint("http://192.168.231.140:9000")
                .credentials("admin", "12345678").build();
        MinIoClient minIoClient = MinIoClient.build(client);
        minIoClient.setBucketName("test");
        return minIoClient;
    }
}
