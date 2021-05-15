package com.github.ren.file.sdk.objectname;

import com.github.ren.file.sdk.UploadUtil;
import lombok.Data;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.StringUtils;

/**
 * @Description 根据时间戳文件名称生成
 * @Author ren
 * @Since 1.0
 */
@Data
public class TimestampGenerator implements ObjectNameGenerator {

    private String filename;

    public TimestampGenerator(String filename) {
        this.filename = filename;
    }

    @Override
    public String generator() {
        long millis = System.currentTimeMillis();
        return StringUtils.reverse(String.valueOf(millis)) + UploadUtil.DOT + FilenameUtils.getExtension(filename);
    }
}
