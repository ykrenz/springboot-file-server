package com.ykrenz.file.upload.storage.model;

import lombok.Data;

import java.util.List;

@Data
public class MultiPartUploadResponse {

    /**
     * 分片唯一标识
     */
    private String uploadId;
    /**
     * 文件大小
     */
    private long fileSize;

    /**
     * 分片大小
     */
    private long partSize;

    /**
     * 分片
     */
    private List<PartResponse> parts;

}
