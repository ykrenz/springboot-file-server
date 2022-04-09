package com.ykrenz.file.upload.storage;

import com.ykrenz.file.upload.storage.model.*;

import java.io.IOException;
import java.util.List;

/**
 * @author ykren
 * @date 2022/3/1
 */
public interface FileServerClient {

    /**
     * 存储类型
     *
     * @return
     */
    StorageType type();

    /**
     * 校验码方式
     *
     * @return
     */
    String crc();

    /**
     * 上传
     *
     * @param request
     * @return
     * @throws IOException
     */
    UploadResponse upload(UploadRequest request) throws IOException;

    /**
     * 初始化
     *
     * @param request
     * @return
     */
    InitResponse initMultipart(InitRequest request);

    /**
     * 上传分片
     *
     * @param request
     * @throws IOException
     */
    void uploadMultipart(MultipartRequest request) throws IOException;

    /**
     * 完成上传
     *
     * @param request
     * @return
     */
    UploadResponse completeMultipart(CompleteRequest request);

    /**
     * 取消上传
     *
     * @param uploadId
     */
    void abortMultipart(String uploadId);

    /**
     * 分片查询
     *
     * @param uploadId
     * @return
     */
    MultiPartUploadResponse uploadParts(String uploadId);

    /**
     * 上传任务
     *
     * @return
     */
    List<UploadTask> listUpload();
}
