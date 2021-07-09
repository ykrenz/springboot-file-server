package com.github.ren.file.client;

import lombok.Builder;
import lombok.Data;

import java.util.List;

/**
 * @Description
 * @Author ren
 * @Since 1.0
 */
@Data
@Builder
public class CompleteMultiPartArgs {

    private String uploadId;

    private String objectName;

    List<UploadPartResponse> parts;
}
