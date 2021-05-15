package com.github.ren.file.service;

import com.github.ren.file.model.request.PartRequest;
import com.github.ren.file.sdk.part.CompleteMultipart;
import com.github.ren.file.sdk.part.PartInfo;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * @author RenYinKui
 * @Description: 文件接口
 * @date 2021/5/13 10:36
 */
public interface FileService {

    /**
     * 上传简单文件
     *
     * @param file
     * @return
     */
    String upload(MultipartFile file);

    /**
     * 检测文件 秒传
     *
     * @param md5
     * @return
     */
    String check(String md5);

    /**
     * 初始化分片上传任务
     *
     * @param filename
     * @return
     */
    String initiateMultipartUpload(String filename);

    /**
     * 获取分片列表 断点续传
     *
     * @param uploadId
     * @return
     */
    List<PartInfo> listParts(String uploadId);

    /**
     * 上传文件分片
     *
     * @param partRequest
     * @return
     */
    PartInfo uploadPart(PartRequest partRequest);

    /**
     * 合并文件分片
     *
     * @param uploadId
     * @param md5
     * @return
     */
    CompleteMultipart completeMultipartUpload(String uploadId, String md5);

    /**
     * 取消分片上传
     *
     * @param uploadId
     * @return
     */
    String abortMultipartUpload(String uploadId);
}
