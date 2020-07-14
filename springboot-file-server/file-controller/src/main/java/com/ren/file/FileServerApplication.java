package com.ren.file;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @Description: springboot启动类
 * @date 2020/5/28 18:05
 */
@SpringBootApplication
@MapperScan(basePackages = "com.ren.file.mapper")
public class FileServerApplication {
    public static void main(String[] args) {
        SpringApplication.run(FileServerApplication.class, args);
    }
}
