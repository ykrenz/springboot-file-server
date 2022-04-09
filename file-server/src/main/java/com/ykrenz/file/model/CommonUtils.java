package com.ykrenz.file.model;

import org.apache.commons.io.FilenameUtils;

import java.time.Instant;
import java.util.concurrent.TimeUnit;

public final class CommonUtils {
    private CommonUtils() {
    }

    public static String getFileExtension(String fileName) {
        return FilenameUtils.getExtension(fileName);
    }

    public static long plusDays(long time, int day) {
        return Instant.ofEpochMilli(time)
                .plusMillis(TimeUnit.DAYS.toMillis(day)).toEpochMilli();
    }
}
