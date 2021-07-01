package com.github.ren.test;

import cn.hutool.crypto.digest.MD5;
import com.github.ren.file.FileServerApplication;
import com.github.ren.file.sdk.FileClient;
import com.github.ren.file.sdk.local.LocalFileOperation;
import com.github.ren.file.sdk.objectname.TimestampGenerator;
import com.github.ren.file.sdk.part.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 * @Description 分片上传测试文件
 * @Author ren
 * @Since 1.0
 */
@Slf4j
@SpringBootTest(classes = FileServerApplication.class)
public class PartUploadTest {

    @Autowired
    private FileClient fileClient;

    @Test
    public void upload() throws Exception {
        String filename = "F:\\oss\\test\\test2.mp4";
        String yourObjectName = new TimestampGenerator(filename).generator();

        InitMultipartUploadArgs args = InitMultipartUploadArgs.builder()
                .objectName(yourObjectName)
                .fileSize(new File(filename).length()).build();

        InitMultipartResponse initMultipartResponse = fileClient.initMultipartUpload(args);
        String objectName = initMultipartResponse.getObjectName();
        String uploadId = initMultipartResponse.getUploadId();
        log.info("初始化分片上传完成uploadId={} objectName={}", uploadId, objectName);

        File sourceFile = new File(filename);
        String chunkPath = "F:\\oss\\chunk";
        File chunkDir = new File(chunkPath);
        chunkDir.mkdirs();
        List<File> files = LocalFileOperation.chunkFile(sourceFile, chunkPath, 1024L * 1024L * 5);
        String md5 = MD5.create().digestHex(sourceFile);
        log.info("源文件md5={}", md5);

        //abort upload test
//        new Thread(() -> {
//            try {
//                TimeUnit.SECONDS.sleep(5);
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }
//            fileClient.abortMultipartUpload(uploadId, objectName);
//        }).start();

        //模拟多线程上传
        List<Callable<UploadMultipartResponse>> tasks = new ArrayList<>();
        for (File file : files) {
            PartTask partTask = new PartTask(uploadId, objectName, file);
            tasks.add(partTask);
        }
        ExecutorService executorService = Executors.newFixedThreadPool(3);
        List<Future<UploadMultipartResponse>> futures = executorService.invokeAll(tasks);
        List<UploadMultipartResponse> uploadMultipartResponses = new ArrayList<>();
        for (Future<UploadMultipartResponse> future : futures) {
            uploadMultipartResponses.add(future.get());
        }
        executorService.shutdown();
        long l = System.currentTimeMillis();
        log.info("开始合并分片uploadId={}", uploadId);
        uploadMultipartResponses.sort(Comparator.comparingInt(UploadMultipartResponse::getPartNumber));
        CompleteMultipartResponse completeMultipartResponse = fileClient.completeMultipartUpload(uploadId, objectName, uploadMultipartResponses);
        long l1 = System.currentTimeMillis();
        log.info("合并文件完成{} 耗时={}", completeMultipartResponse, (l1 - l));
        FileUtils.deleteQuietly(chunkDir);
    }

    @Test
    public void initMultipart() throws IOException {
        String filename = "F:\\oss\\test\\test2.mp4";
        String yourObjectName = new TimestampGenerator(filename).generator();

        InitMultipartUploadArgs args = InitMultipartUploadArgs.builder()
                .objectName(yourObjectName)
                .fileSize(new File(filename).length()).build();

        InitMultipartResponse initMultipartResponse = fileClient.initMultipartUpload(args);
        String objectName = initMultipartResponse.getObjectName();
        String uploadId = initMultipartResponse.getUploadId();
        log.info("初始化分片上传完成uploadId={} objectName={}", uploadId, objectName);
    }

    public String getUploadId() {
        return "e4d7f19b-4bfe-455e-b0af-7067e1f66e3d";
    }

    public String getObjectName() {
        return "6360172481261.mp4";
    }

    @Test
    public void uploadPart() throws IOException, InterruptedException {
        String uploadId = getUploadId();
        String objectName = getObjectName();
        String filename = "F:\\oss\\test\\test2.mp4";
        File sourceFile = new File(filename);
        String chunkPath = "F:\\oss\\chunk";
        File chunkDir = new File(chunkPath);
        chunkDir.mkdirs();
        List<File> files = LocalFileOperation.chunkFile(sourceFile, chunkPath, 1024L * 1024L * 5);
        String md5 = MD5.create().digestHex(sourceFile);
        log.info(md5);
        for (File file : files) {
            UploadPartArgs part = new UploadPartArgs(uploadId, objectName, Integer.parseInt(file.getName()), file.length(), new FileInputStream(file));
            String s = MD5.create().digestHex(file);
            log.info("文件md5={}", s);
            fileClient.uploadMultipart(part);
            log.info("上传分片成功={}", part.getPartNumber());
        }

        //模拟分片改变的情况
//        File file1 = files.get(0);
//        UploadPart part = new UploadPart(uploadId, objectName, 2, file1.length(), new FileInputStream(file1));
//        String s = MD5.create().digestHex(file1);
//        log.info("文件md5={}", s);
//        fastDFSClient.uploadPart(part);
//        log.info("上传分片成功={}", part.getPartNumber());
        FileUtils.deleteQuietly(chunkDir);
    }

    class PartTask implements Callable<UploadMultipartResponse> {
        private String uploadId;
        private String objectName;
        private File file;

        public PartTask(String uploadId, String objectName, File file) {
            this.uploadId = uploadId;
            this.objectName = objectName;
            this.file = file;
        }

        @Override
        public UploadMultipartResponse call() throws Exception {
            UploadPartArgs part = new UploadPartArgs(uploadId, objectName, Integer.parseInt(file.getName()), file.length(), new FileInputStream(file));
            log.info("开始上传分片uploadId={} number={} objectName={}", uploadId, part.getPartNumber(), objectName);
            UploadMultipartResponse uploadMultipartResponse = fileClient.uploadMultipart(part);
            log.info("上传分片成功uploadId={} number={}  objectName={}", uploadId, part.getPartNumber(), objectName);
            return uploadMultipartResponse;
        }
    }

    @Test
    public void listParts() throws IOException {
        String uploadId = getUploadId();
        String objectName = getObjectName();
        long l = System.currentTimeMillis();
        ListMultipartUploadArgs args = ListMultipartUploadArgs.builder().uploadId(uploadId).objectName(objectName).build();
        List<UploadMultipartResponse> uploadMultipartResponses = fileClient.listMultipartUpload(args);
        long l1 = System.currentTimeMillis();
        log.info("查询到分片文件 {} 耗时={}", uploadMultipartResponses, (l1 - l));
    }

    @Test
    public void completePart() throws IOException {
        String uploadId = getUploadId();
        String objectName = getObjectName();
        long l = System.currentTimeMillis();
        log.info("开始合并分片uploadId={}", uploadId);
        ListMultipartUploadArgs args = ListMultipartUploadArgs.builder().uploadId(uploadId).objectName(objectName).build();
        List<UploadMultipartResponse> uploadMultipartResponses = fileClient.listMultipartUpload(args);
        uploadMultipartResponses.sort(Comparator.comparingInt(UploadMultipartResponse::getPartNumber));
        CompleteMultipartResponse completeMultipartResponse = fileClient.completeMultipartUpload(uploadId, objectName, uploadMultipartResponses);
        long l1 = System.currentTimeMillis();
        log.info("合并文件完成{} 耗时={}", completeMultipartResponse, (l1 - l));
    }

    @Test
    public void abortMultipartUpload() throws IOException {
        String uploadId = getUploadId();
        String objectName = getObjectName();
        long l = System.currentTimeMillis();
        log.info("取消分片uploadId={}", uploadId);
        fileClient.abortMultipartUpload(uploadId, objectName);
        long l1 = System.currentTimeMillis();
        log.info("取消完成 耗时={}", (l1 - l));
    }

}
