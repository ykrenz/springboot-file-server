package com.ykrenz.file.upload.manager;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;

@EqualsAndHashCode(callSuper = true)
@Data
public class UploadModel extends InitUploadModel {

    /**
     * 分片唯一标识
     */
    private String uploadId;

    /**
     * 创建时间
     */
    private long createTime;

}
