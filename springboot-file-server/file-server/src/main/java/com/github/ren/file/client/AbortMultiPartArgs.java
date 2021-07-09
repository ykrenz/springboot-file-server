package com.github.ren.file.client;

import lombok.Builder;
import lombok.Data;

/**
 * @Description
 * @Author ren
 * @Since 1.0
 */
@Data
@Builder
public class AbortMultiPartArgs {
    private String uploadId;

    private String objectName;
}
