package com.github.ren.fastdfs.fdfs;

/**
 * @Description fastdfs文件客戶端Builder
 * @Author ren
 * @Since 1.0
 */
public interface FastDfsBuilder {

    //    https://github.com/happyfish100/fastdfs-client-java

    /**
     * 默认加载config文件名
     */
    String CONFIG_FILE = "fdfs_client.conf";

    /**
     * 默认加载properties文件名
     */
    String PROPERTIES_FILE = "fastdfs-client.properties";

    /**
     * 根据fdfs_client.conf文件构建
     *
     * @return
     */
    FastDfsStorageClient configFile();

    /**
     * 根据fastdfs-client.properties文件构建
     *
     * @return
     */
    FastDfsStorageClient propertiesFile();

    /**
     * 根据config文件构建
     *
     * @param configFilePath config文件
     * @return
     */
    FastDfsStorageClient configFile(String configFilePath);

    /**
     * 根据properties文件构建
     *
     * @param propertiesFilePath properties文件
     * @return
     */
    FastDfsStorageClient propertiesFile(String propertiesFilePath);

    /**
     * 根据trackerServers构建
     *
     * @param trackerServers trackerServers 多个server用逗号隔开
     * @return
     */
    FastDfsStorageClient build(String trackerServers);

    /**
     * 根据trackerServers构建
     *
     * @param trackerServers trackerServers 多个server用逗号隔开
     * @param groupName      组名
     * @return
     */
    FastDfsStorageClient build(String trackerServers, String groupName);

    /**
     * 根据trackerServers clientConfiguration构建
     *
     * @param trackerServers      trackerServers 多个server用逗号隔开
     * @param clientConfiguration fastdfs配置类
     * @return
     */
    FastDfsStorageClient build(String trackerServers, FastDfsClientConfiguration clientConfiguration);

    /**
     * 根据trackerServers构建
     *
     * @param trackerServers      trackerServers 多个server用逗号隔开
     * @param groupName           组名
     * @param clientConfiguration fastdfs配置类
     * @return
     */
    FastDfsStorageClient build(String trackerServers, String groupName, FastDfsClientConfiguration clientConfiguration);

}
