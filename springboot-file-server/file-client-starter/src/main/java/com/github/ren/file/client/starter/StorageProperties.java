package com.github.ren.file.client.starter;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @Description:
 * @date 2020/6/10 11:35
 */
@Setter
@Getter
@ConfigurationProperties(Constants.StoragePrefix)
public class StorageProperties {

    /**
     * 存储方式
     */
    private StorageType type = StorageType.Local;

}
