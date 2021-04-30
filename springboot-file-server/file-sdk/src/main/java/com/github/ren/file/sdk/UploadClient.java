package com.github.ren.file.sdk;

import java.io.File;
import java.io.InputStream;
import java.io.Serializable;

/**
 * @Description 上传文件客户端
 * @Author ren
 * @Since 1.0
 */
public interface UploadClient extends Serializable {

    /**
     * <yourObjectName>表示上传文件时需要指定包含文件后缀在内的完整路径，例如abc/efg/123.jpg。
     */

    /**
     * 本地文件上传
     *
     * @param file           文件
     * @param yourObjectName
     * @return
     */
    String upload(File file, String yourObjectName);

    /**
     * 上传文件流
     *
     * @param is             文件流
     * @param yourObjectName
     * @return
     */
    String upload(InputStream is, String yourObjectName);

    /**
     * 上传字节数组
     *
     * @param content        字节数组
     * @param yourObjectName
     * @return
     */
    String upload(byte[] content, String yourObjectName);

    /**
     * 上传网络流
     *
     * @param url            文件地址
     * @param yourObjectName
     * @return
     */
    String upload(String url, String yourObjectName);

}
