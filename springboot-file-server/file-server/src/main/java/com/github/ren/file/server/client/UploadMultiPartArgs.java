package com.github.ren.file.server.client;

import lombok.Builder;
import lombok.Data;

import java.io.InputStream;

/**
 * @Description
 * @Author ren
 * @Since 1.0
 */
@Data
@Builder
public class UploadMultiPartArgs {
    private String uploadId;
    private String objectName;
    private Integer partNumber;
    private Long partSize;
    private InputStream inputStream;
}
