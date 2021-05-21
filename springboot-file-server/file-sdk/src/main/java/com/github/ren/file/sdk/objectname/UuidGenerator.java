package com.github.ren.file.sdk.objectname;

import com.github.ren.file.sdk.util.Util;
import lombok.Data;
import org.apache.commons.io.FilenameUtils;

import java.util.UUID;

/**
 * @Description 根据uuid文件名称生成
 * @Author ren
 * @Since 1.0
 */
@Data
public class UuidGenerator implements ObjectNameGenerator {

    private String filename;

    public UuidGenerator(String filename) {
        this.filename = filename;
    }

    @Override
    public String generator() {
        String uuid = UUID.randomUUID().toString().replace(Util.DASHED, "");
        return uuid.substring(0, 4) + Util.SLASH + uuid + Util.DOT + FilenameUtils.getExtension(filename);
    }
}
