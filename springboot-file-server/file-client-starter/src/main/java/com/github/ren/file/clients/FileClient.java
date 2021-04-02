package com.github.ren.file.clients;

import java.io.Serializable;

/**
 * 文件上传客户端
 */
public interface FileClient extends
        LocalFileOperations,
        UploadClient,
        DownLoadClient,
        AccessClient, Serializable {

}