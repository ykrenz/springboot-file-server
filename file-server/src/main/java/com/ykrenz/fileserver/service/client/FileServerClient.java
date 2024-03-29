package com.ykrenz.fileserver.service.client;

import com.ykrenz.fileserver.entity.FileInfo;
import com.ykrenz.fileserver.entity.FilePartInfo;
import com.ykrenz.fileserver.model.request.CancelPartRequest;
import com.ykrenz.fileserver.model.request.CompletePartRequest;
import com.ykrenz.fileserver.model.request.FileInfoRequest;
import com.ykrenz.fileserver.model.request.InitUploadMultipartRequest;
import com.ykrenz.fileserver.model.request.SimpleUploadRequest;
import com.ykrenz.fileserver.model.request.UploadMultipartRequest;
import com.ykrenz.fileserver.model.result.FileInfoResult;
import com.ykrenz.fileserver.model.result.InitMultipartResult;

import java.io.IOException;

/**
 * @author ykren
 * @date 2022/3/1
 */
public interface FileServerClient {

    /**
     * 上传文件
     *
     * @param request
     * @return
     * @throws IOException
     */
    FileInfo upload(SimpleUploadRequest request) throws IOException;

    /**
     * 初始化分片
     *
     * @param request
     * @return
     */
    InitMultipartResult initMultipart(InitUploadMultipartRequest request);

    /**
     * 上传分片
     *
     * @param request
     * @return
     * @throws IOException
     */
    FilePartInfo uploadMultipart(UploadMultipartRequest request) throws IOException;

    /**
     * 完成分片上传
     *
     * @param request
     * @return
     */
    FileInfo completeMultipart(CompletePartRequest request);

    /**
     * 取消分片上传
     *
     * @param request
     */
    void cancelMultipart(CancelPartRequest request);

    /**
     * 查询文件信息
     *
     * @param id
     * @return
     */
    FileInfoResult info(FileInfoRequest id);

    /**
     * 删除数据库所有文件
     */
    void deleteAllFiles();
}
