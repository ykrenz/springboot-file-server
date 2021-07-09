package com.github.ren.file.server.client;

import lombok.Builder;
import lombok.Data;

/**
 * @Description
 * @Author ren
 * @Since 1.0
 */
@Data
@Builder
public class ListPartsArgs {
    private String uploadId;

    private String objectName;
}
