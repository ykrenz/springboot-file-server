package com.github.ren.file.server.client;

import lombok.Builder;
import lombok.Data;

import java.io.InputStream;

/**
 * @author RenYinKui
 * @Description:
 * @date 2021/7/8 11:30
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
