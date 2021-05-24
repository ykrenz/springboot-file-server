package com.github.ren.file.sdk.fdfs;

import java.util.Arrays;
import java.util.List;

/**
 * @Description fastdfs常量信息
 * @Author ren
 * @Since 1.0
 */
public interface FastDFSConstants {

    String UPLOAD_ID = "uploadId";

    String NEXT_PART_NUMBER_KEY = "nextPartNumber";

    String PART = "part";

    List<String> constants = Arrays.asList(UPLOAD_ID, NEXT_PART_NUMBER_KEY, PART);

}
