package com.github.ren.file.clients;

/**
 * 本地存储客户端
 */
public interface LocalClient extends FileClient {
    /**
     * 删除文件
     */
    void deleteFile(String filepath);
}
