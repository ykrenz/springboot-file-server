package com.github.ren.file.server.client;

import lombok.Builder;
import lombok.Data;

import java.util.List;

/**
 * @author RenYinKui
 * @Description:
 * @date 2021/7/8 11:42
 */
@Data
@Builder
public class CompleteMultiPartArgs {

    private String uploadId;

    private String objectName;

    List<PartResult> parts;
}
