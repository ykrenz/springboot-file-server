package com.github.ren.file.sdk.objectname;

import com.github.ren.file.sdk.util.Util;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;

/**
 * @Description 根据时间戳文件名称生成
 * @Author ren
 * @Since 1.0
 */
@Data
public class TimestampGenerator implements ObjectNameGenerator {

    private String ext;

    public TimestampGenerator(String ext) {
        this.ext = ext;
    }

    @Override
    public String generator() {
        long millis = System.currentTimeMillis();
        return StringUtils.reverse(String.valueOf(millis)) + Util.DOT + ext;
    }
}
