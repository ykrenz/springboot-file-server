package com.ykrenz.file.objectname;

import lombok.Data;

import java.util.UUID;

/**
 * @Description 根据uuid文件名称生成
 * @Author ren
 * @Since 1.0
 */
@Data
public class UuidGenerator implements ObjectNameGenerator {

    private String ext;

    public UuidGenerator(String ext) {
        this.ext = ext;
    }

    @Override
    public String generator() {
        String uuid = UUID.randomUUID().toString().replace("-", "");
        return uuid.substring(0, 4) + "/" + uuid + "." + ext;
    }
}
