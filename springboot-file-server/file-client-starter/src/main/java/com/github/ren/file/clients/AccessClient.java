package com.github.ren.file.clients;

/**
 * @author Mr Ren
 * @Description: 访问文件
 * @date 2021/3/31 15:35
 */
public interface AccessClient {
    /**
     * 获取文件访问前缀
     *
     * @return
     */
    String getWebServerUrl();

    /**
     * 获取访问地址
     *
     * @return
     */
    String getAccessPath(String objectName);

    /**
     * 判断文件是否存在
     *
     * @param objectName
     * @return
     */
    boolean isExist(String objectName);
}
