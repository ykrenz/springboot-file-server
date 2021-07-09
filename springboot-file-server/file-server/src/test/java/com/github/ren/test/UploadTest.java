//package com.github.ren.test;
//
//import com.github.ren.file.server.FileServerApplication;
//import com.github.ren.file.client.FileClient;
//import com.github.ren.file.client.fdfs.FastDFS;
//import com.github.ren.file.client.fdfs.FastDFSBuilder;
//import com.github.ren.file.client.fdfs.FastDFSConstants;
//import com.github.ren.file.client.local.LocalFileOperation;
//import com.github.ren.file.server.client.objectname.TimestampGenerator;
//import lombok.extern.slf4j.Slf4j;
//import org.apache.commons.io.FileUtils;
//import org.csource.common.MyException;
//import org.csource.common.NameValuePair;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//
//import java.io.File;
//import java.io.IOException;
//import java.nio.charset.StandardCharsets;
//import java.util.List;
//
///**
// * @Description 上传测试文件
// * @Author ren
// * @Since 1.0
// */
//@Slf4j
//@SpringBootTest(classes = FileServerApplication.class)
//public class UploadTest {
//
//    @Autowired
//    private FileClient fileClient;
//
//    @Test
//    public void uploadTest() {
//        String filename = "F:\\oss\\test\\test2.mp4";
//        String objectName = new TimestampGenerator(filename).generator();
//        fileClient.upload(new File(filename), objectName);
//    }
//
//    @Test
//    public void me() throws MyException, IOException {
//        FastDFS fastDFS = FastDFSBuilder.build();
//        NameValuePair[] metadata = fastDFS.get_metadata1("group1/M00/07/02/CgoKTGDL-ouEdQmsAAAAAAAAAAA013.exe");
//        if (metadata != null) {
//            for (NameValuePair pair : metadata) {
//                String name = pair.getName();
//                String value = pair.getValue();
//                if (FastDFSConstants.UPLOAD_ID.equals(name)) {
//                    continue;
//                }
//                if (FastDFSConstants.NEXT_PART_NUMBER_KEY.equals(name)) {
//                    continue;
//                }
//                log.info("name={} value={}", name, value);
//            }
//        }
//    }
//
////    @Autowired
////    private AppendFileStorageClient appendFileStorageClient;
////
////    @Test
////    public void upload_appender_file1() throws MyException, IOException {
////        StorePath storePath = null;
////        try {
////            String filename = "F:\\oss\\test\\test2.mp4";
////            FastDFS fastDFS = FastDFSBuilder.build();
//////        String filePath = fastDFS.upload_appender_file1(filename, "mp4", null);
//////        log.info("上传到fastdfs success {}", filePath);
////
////            File file = new File(filename);
////            storePath = appendFileStorageClient.uploadAppenderFile(null, FileUtils.openInputStream(file), file.length(), "mp4");
////            log.info("上传到fastdfs success {}", storePath);
////            appendFileStorageClient.truncateFile(storePath.getGroup(),storePath.getPath(),1024 * 1024 * 5);
////
////            int i = fastDFS.truncate_file1(storePath.getFullPath(), 1024 * 1024 * 50);
////            log.info("上传到fastdfs success {} {}", storePath, i);
////        }finally {
////
////            appendFileStorageClient.deleteFile(storePath.getGroup(),storePath.getPath());
////        }
////
////
////    }
//
//    @Test
//    public void truncate_file1() throws MyException, IOException {
//        String filename = "F:\\oss\\test\\test2.mp4";
//        FastDFS fastDFS = FastDFSBuilder.build();
//        String filePath = fastDFS.upload_appender_file1("".getBytes(StandardCharsets.UTF_8), "mp4", null);
//        log.info("上传到fastdfs success {}", filePath);
//
//        int i = fastDFS.truncate_file1(filePath, 1024 * 1024 * 50);
//        log.info("result ={}", i);
//        List<File> files = LocalFileOperation.chunkFile(new File(filename), "F:\\oss", 1024 * 1024 * 5);
//        for (File file : files) {
//            new Thread(() -> {
//                try {
//                    String name = file.getName();
//                    int partNumber = Integer.valueOf(name);
//                    long off = 0;
//                    if (partNumber != 1) {
//                        off = (partNumber - 1) * 1024 * 1024 * 5;
//                    }
//                    log.info("modify_file partNumber={} off={}  start={}", partNumber, off, Thread.currentThread().getId());
//                    fastDFS.modify_file1(filePath, off, FileUtils.readFileToByteArray(file));
//                    log.info("modify_file end={}", Thread.currentThread().getId());
//                } catch (IOException e) {
//                    e.printStackTrace();
//                } catch (MyException e) {
//                    e.printStackTrace();
//                }
//            }).start();
//        }
////        for (int j = 0; j < files.size(); j++) {
////            new Thread(() -> {
////                try {
////                    log.info("modify_file start={}", Thread.currentThread().getId());
////                    fastDFS.modify_file1(filePath, 0, new String("123123").getBytes(StandardCharsets.UTF_8));
////                    log.info("modify_file end={}", Thread.currentThread().getId());
////                } catch (IOException e) {
////                    e.printStackTrace();
////                } catch (MyException e) {
////                    e.printStackTrace();
////                }
////            }).start();
////        }
//
//        try {
//            Thread.sleep(10000);
//            fastDFS.truncate_file1(filePath, new File(filename).length());
//            log.info("truncate_file1 end={}", Thread.currentThread().getId());
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
//    }
//}
