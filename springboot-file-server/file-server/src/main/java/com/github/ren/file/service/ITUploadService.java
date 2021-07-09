package com.github.ren.file.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.github.ren.file.client.UploadPartResponse;
import com.github.ren.file.entity.TUpload;

import java.util.List;

/**
 * <p>
 * 服务类
 * </p>
 *
 * @author Mr Ren
 * @since 2021-06-17
 */
public interface ITUploadService extends IService<TUpload> {

    /**
     * 查询上传任务
     *
     * @param uploadId
     * @return
     */
    TUpload selectUpload(String uploadId);

    /**
     * 完成上传
     *
     * @param uploadId
     * @return
     */
    boolean completeUpload(String uploadId);

    /**
     * 取消上传
     *
     * @param uploadId
     * @return
     */
    boolean abortUpload(String uploadId);

    /**
     * 保存Multipart数据
     *
     * @param tUpload
     * @param uploadPartResponse
     */
    void saveMultipart(TUpload tUpload, UploadPartResponse uploadPartResponse);

    /**
     * 查询Multipart
     *
     * @param tUpload
     * @return
     */
    List<UploadPartResponse> listMultipart(TUpload tUpload);
}
