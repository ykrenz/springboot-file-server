package com.github.ren.file.service.impl;

import com.github.ren.file.model.request.PartRequest;
import com.github.ren.file.sdk.part.PartInfo;

/**
 * @author RenYinKui
 * @Description:
 * @date 2021/5/14 14:34
 */
public interface FdfsService {
    /**
     * 上传文件分片
     *
     * @param partRequest
     * @return
     */
    PartInfo uploadPart(PartRequest partRequest);
}
