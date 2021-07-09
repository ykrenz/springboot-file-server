package com.github.ren.file.client;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @Description
 * @Author ren
 * @Since 1.0
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CompleteMultipartResponse implements Serializable {

    /**
     * 文件服务器返回的eTag
     */
    private String eTag;

    /**
     * 文件位置
     */
    private String objectName;
}
