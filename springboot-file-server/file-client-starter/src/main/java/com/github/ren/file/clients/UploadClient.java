package com.github.ren.file.clients;

import java.io.File;
import java.io.InputStream;
import java.util.List;

/**
 * @author Mr Ren
 * @Description: 上传文件客户端
 * @date 2021/3/31 14:41
 */
public interface UploadClient {

    /**
     * <yourObjectName>表示上传文件时需要指定包含文件后缀在内的完整路径，例如abc/efg/123.jpg。
     */

    /**
     * 本地上传
     */
    String uploadFile(File file, String yourObjectName);

    /**
     * 上传文件流
     */
    String uploadFile(InputStream is, String yourObjectName);

    /**
     * 上传字节数组
     */
    String uploadFile(byte[] content, String yourObjectName);

    /**
     * 上传网络流
     */
    String uploadFile(String url, String yourObjectName);

    /**
     * 分片上传
     */
    String uploadPart(List<File> files, String yourObjectName);

}
