package com.ykrenz.fileserver.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
import org.springframework.util.unit.DataSize;

/**
 * @Description: upload配置
 * @date 2020/6/10 11:35
 */
@Data
@ConfigurationProperties("file")
public class StorageProperties {

    /**
     * 存储方式
     */
    private StorageType storage = StorageType.fastdfs;

    /**
     * 普通上传支持最大文件Size
     */
    private DataSize maxUploadSize = DataSize.ofMegabytes(1L);
    /**
     * 上传分片支持最小Size
     */
    private DataSize multipartMinSize = DataSize.ofBytes(1L);
    /**
     * 上传分片支持最大Size
     */
    private DataSize multipartMaxSize = DataSize.ofBytes(1L);


    private FastDfs fastdfs = new FastDfs();

    @Data
    public static class FastDfs {
        /**
         * 分片过期天数 默认7天
         */
        private int partExpireDays = 7;

        /**
         * 清理分片周期 默认1小时
         */
        private long partEvictableSeconds = 3600L;
    }

}
