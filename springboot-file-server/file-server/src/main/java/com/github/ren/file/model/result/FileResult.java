package com.github.ren.file.model.result;

import io.swagger.annotations.ApiModel;
import lombok.Data;

import java.io.Serializable;

/**
 * @Description 文件信息
 * @Author ren
 * @Since 1.0
 */
@Data
@ApiModel("文件信息")
public class FileResult implements Serializable {
    private static final long serialVersionUID = 1L;

    private String objectName;

    private long fileSize;

}
