package com.github.ren.test;

import com.github.ren.file.FileServerApplication;
import com.github.ren.file.sdk.FileClient;
import com.github.ren.file.sdk.objectname.TimestampGenerator;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.File;

/**
 * @Description 上传测试文件
 * @Author ren
 * @Since 1.0
 */
@Slf4j
@SpringBootTest(classes = FileServerApplication.class)
public class UploadTest {

    @Autowired
    private FileClient fileClient;

    @Test
    public void uploadTest() {
        String filename = "F:\\oss\\test\\test2.mp4";
        String objectName = new TimestampGenerator(filename).generator();
        fileClient.upload(new File(filename), objectName);
    }
}
