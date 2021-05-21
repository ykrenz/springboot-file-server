package com.github.ren.file.sdk.part;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serializable;

/**
 * @Description 初始化分片上传
 * @Author ren
 * @Since 1.0
 */
@Data
@AllArgsConstructor
public class InitMultipartResult implements Serializable {
    private String uploadId;

    private String objectName;

}
