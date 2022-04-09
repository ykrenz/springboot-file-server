package com.ykrenz.file.upload.manager;

import java.util.List;

public interface UploadManager<I, U, P> {

    /**
     * 创建uploadId
     *
     * @return
     */
    U createUpload(I init);

    /**
     * 获取任务信息
     *
     * @param uploadId
     * @return
     */
    U getUpload(String uploadId);

    /**
     * 列举分片任务
     *
     * @param listUploadParam
     * @return
     */
    List<U> listUploads(ListUploadParam listUploadParam);

    /**
     * 保存分片信息
     *
     * @param part
     */
    void savePart(P part);

    /**
     * 列举分片记录
     *
     * @param uploadId
     * @return
     */
    List<P> listParts(String uploadId);

    /**
     * 清空分片记录
     *
     * @param uploadId
     */
    void clearParts(String uploadId);
}
