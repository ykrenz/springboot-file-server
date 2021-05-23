package com.github.ren.file.sdk.fdfs;

import java.util.Arrays;
import java.util.List;

/**
 * @author RenYinKui
 * @Description:
 * @date 2021/5/21 11:17
 */
public interface FastDfsConstants {

    String UPLOAD_ID = "uploadId";

    String NEXT_PART_NUMBER_KEY = "nextPartNumber";

    String PART = "part";

    List<String> constants = Arrays.asList(UPLOAD_ID, NEXT_PART_NUMBER_KEY, PART);

}
