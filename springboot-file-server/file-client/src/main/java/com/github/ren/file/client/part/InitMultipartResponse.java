package com.github.ren.file.client.part;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @Description 初始化分片上传结果
 * @Author ren
 * @Since 1.0
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class InitMultipartResponse implements Serializable {

    /**
     * 分片上传标识
     */
    private String uploadId;

    /**
     * 文件存储位置
     */
    private String objectName;

}
