package com.github.ren.file.server.client;

import lombok.Builder;
import lombok.Data;

/**
 * @author RenYinKui
 * @Description:
 * @date 2021/7/9 13:27
 */
@Data
@Builder
public class ListPartsArgs {
    private String uploadId;

    private String objectName;
}
