package com.github.ren.file;

import com.github.ren.file.sdk.FileClient;
import com.github.ren.file.sdk.local.LocalClient;
import com.github.ren.file.sdk.part.LocalPartStore;
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
//        OSS oss = new OSSClientBuilder()
//                .build("",
//                        "",
//                        "");
//        return new AliClient(oss, "");
        return new LocalClient("F:\\oss\\upload",new LocalPartStore("F:\\oss\\part"));
//        return FastDFSClient.getInstance();
    }
}
