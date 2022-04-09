package com.ykrenz.file.upload.storage.model;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

@Data
public class MultipartRequest {
    /**
     * 上传唯一标识
     */
    private String uploadId;

    /**
     * file
     */
    private MultipartFile partFile;

    /**
     * 分片索引
     */
    private Integer partNumber;

}
