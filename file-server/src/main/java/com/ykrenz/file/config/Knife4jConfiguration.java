package com.ykrenz.file.config;

import io.swagger.annotations.ApiOperation;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2WebMvc;

/**
 * @author ykren
 * @description: swagger Knife4j
 */
@Configuration
@EnableSwagger2WebMvc
public class Knife4jConfiguration {

    @Bean()
    public Docket docket() {
        return new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(new ApiInfoBuilder()
                        .description("文件服务 fastdfs oss 七牛云等s3协议 大文件 断点续传")
                        .termsOfServiceUrl("https://github.com/ykrenz/springboot-file-server")
                        .contact(new Contact("ykren", "", "ykren888@163.com"))
                        .version("1.0")
                        .build())
                //分组名称
                .groupName("文件服务")
                .select()
                //这里指定Controller扫描包路径
                .apis(RequestHandlerSelectors.withMethodAnnotation(ApiOperation.class))
                .paths(PathSelectors.any())
                .build();
    }
}