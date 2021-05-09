package com.github.ren.file.sdk.part;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Description 完成上传文件信息
 * @Author ren
 * @Since 1.0
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CompleteMultipart {

    private String eTag;

    private String objectName;
}
