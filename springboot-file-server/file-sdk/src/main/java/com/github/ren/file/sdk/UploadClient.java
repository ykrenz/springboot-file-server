package com.github.ren.file.sdk;

import com.github.ren.file.sdk.model.UploadGenericResult;

import java.io.File;
import java.io.InputStream;
import java.io.Serializable;

/**
 * @Description 简单上传文件客户端
 * @Author ren
 * @Since 1.0
 */
public interface UploadClient extends Serializable {

    /**
     * <objectName>表示上传文件时需要指定包含文件后缀在内的完整路径，例如abc/efg/123.jpg。
     */

    /**
     * 本地文件上传
     *
     * @param file       文件
     * @param objectName
     * @return
     */
    UploadGenericResult upload(File file, String objectName);

    /**
     * 上传文件流
     *
     * @param is         文件流
     * @param objectName
     * @return
     */
    UploadGenericResult upload(InputStream is, String objectName);

    /**
     * 上传字节数组
     *
     * @param content    字节数组
     * @param objectName
     * @return
     */
    UploadGenericResult upload(byte[] content, String objectName);

    /**
     * 上传网络流
     *
     * @param url        文件地址
     * @param objectName
     * @return
     */
    UploadGenericResult upload(String url, String objectName);

}
