package com.github.ren.file.server.client;

import lombok.Builder;
import lombok.Data;

/**
 * @author RenYinKui
 * @Description:
 * @date 2021/7/8 15:10
 */
@Data
@Builder
public class InitMultipartUploadArgs {

    private String objectName;

    private Long fileSize;

    private Long partSize;
}
