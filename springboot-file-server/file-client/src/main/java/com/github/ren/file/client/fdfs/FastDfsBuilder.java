package com.github.ren.file.client.fdfs;

/**
 * @Description fastdfs文件客戶端Builder
 * @Author ren
 * @Since 1.0
 */
public interface FastDfsBuilder {

    String CONFIG_FILE = "fdfs_client.conf";

    String PROPERTIES_FILE = "fastdfs-client.properties";

    /**
     * 根据fdfs_client.conf文件构建
     *
     * @return
     */
    FastDfsClient configFile();

    /**
     * 根据fastdfs-client.properties文件构建
     *
     * @return
     */
    FastDfsClient propertiesFile();

    /**
     * 根据config文件构建
     *
     * @param configFilePath config文件
     * @return
     */
    FastDfsClient configFile(String configFilePath);

    /**
     * 根据properties文件构建
     *
     * @param propertiesFilePath properties文件
     * @return
     */
    FastDfsClient propertiesFile(String propertiesFilePath);

    /**
     * 根据group config文件构建
     *
     * @param group          fastdfs group
     * @param configFilePath config文件
     * @return
     */
    FastDfsClient configFile(String group, String configFilePath);

    /**
     * 根据group properties文件构建
     *
     * @param group              fastdfs group
     * @param propertiesFilePath properties文件
     * @return
     */
    FastDfsClient propertiesFile(String group, String propertiesFilePath);

    /**
     * 根据trackerServers构建
     *
     * @param trackerServers trackerServers 多个server用逗号隔开
     * @return
     */
    FastDfsClient build(String trackerServers);

    /**
     * 根据group trackerServers构建
     *
     * @param group          fastdfs group
     * @param trackerServers trackerServers 多个server用逗号隔开
     * @return
     */
    FastDfsClient build(String group, String trackerServers);

    /**
     * 根据trackerServers clientConfiguration构建
     *
     * @param trackerServers      trackerServers 多个server用逗号隔开
     * @param clientConfiguration fastdfs配置类
     * @return
     */
    FastDfsClient build(String trackerServers, FastDfsClientConfiguration clientConfiguration);

    /**
     * 根据group trackerServers clientConfiguration构建
     *
     * @param group               fastdfs group
     * @param trackerServers      trackerServers 多个server用逗号隔开
     * @param clientConfiguration fastdfs配置类
     * @return
     */
    FastDfsClient build(String group, String trackerServers, FastDfsClientConfiguration clientConfiguration);

}
