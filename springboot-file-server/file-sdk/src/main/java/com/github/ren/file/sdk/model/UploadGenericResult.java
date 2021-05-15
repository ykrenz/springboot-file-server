package com.github.ren.file.sdk.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @Description 上传文件结果
 * @Author ren
 * @Since 1.0
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UploadGenericResult implements Serializable {

    private static final long serialVersionUID = 1L;

    private String objectName;

    private String eTag;
}
