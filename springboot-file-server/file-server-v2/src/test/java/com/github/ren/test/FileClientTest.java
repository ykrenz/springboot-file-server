package com.github.ren.test;

import cn.hutool.crypto.digest.MD5;
import com.github.ren.file.FileServerApplication;
import com.github.ren.file.sdk.FileClient;
import com.github.ren.file.sdk.fdfs.FastDFSClient;
import com.github.ren.file.sdk.local.LocalFileOperation;
import com.github.ren.file.sdk.objectname.Md5Generator;
import com.github.ren.file.sdk.part.CompleteMultipart;
import com.github.ren.file.sdk.part.UploadPart;
import com.github.tobato.fastdfs.service.AppendFileStorageClient;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.csource.common.MyException;
import org.csource.fastdfs.*;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;

@Slf4j
@SpringBootTest(classes = FileServerApplication.class)
public class FileClientTest {

    @Autowired
    @Qualifier("AliClient")
    private FileClient fileClient;

    @Test
    public void test() throws IOException {
        String filename = "F:\\oss\\test\\test2.mp4";
        File sourceFile = new File(filename);
        String chunkPath = "F:\\oss\\chunk";
        File chunkDir = new File(chunkPath);
        chunkDir.mkdirs();
        List<File> files = LocalFileOperation.chunkFile(sourceFile, chunkPath, 1024L * 1024L * 5);
        String md5 = MD5.create().digestHex(sourceFile);
        String yourObjectName = new Md5Generator(md5, filename).generator();
//        String yourObjectName = new TimestampGenerator(filename).generator();
        log.info(yourObjectName);
        String uploadId = fileClient.initiateMultipartUpload(yourObjectName);
        log.info("请求到uploadId={}", uploadId);
        for (File file : files) {
            UploadPart part = new UploadPart(uploadId, yourObjectName, Integer.parseInt(file.getName()), file.length(), new FileInputStream(file));
            String s = MD5.create().digestHex(file);
            log.info("文件md5={}", s);
            fileClient.uploadPart(part);
            log.info("上传分片成功={}", part.getPartNumber());
        }
//        new Thread(() -> {
//            try {
//                TimeUnit.MICROSECONDS.sleep(10);
//                log.info("开始取消");
//                fileClient.abortMultipartUpload(uploadId, yourObjectName);
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }
//        }).start();
        log.info("开始合并分片uploadId={}", uploadId);
        CompleteMultipart completeMultipart = fileClient.completeMultipartUpload(uploadId, yourObjectName);
        log.info("开始合并分片completeMultipart={}", completeMultipart);
        FileUtils.deleteQuietly(chunkDir);
    }

    @Autowired
    private AppendFileStorageClient appendFileStorageClient;

    @Test
    public void uploadAppenderFile() throws IOException {
        File file = new File("F:\\测试资源\\video\\2014年小学语文视频模块精讲班 - 副本.swf");
//        FileUtils.touch(file);
        FileInputStream inputStream = FileUtils.openInputStream(file);
        inputStream.getChannel().truncate(0);
//        StorePath storePath = appendFileStorageClient.uploadAppenderFile(null, inputStream, file.length(), "mp4");
//        log.info(storePath.toString());

    }


    @Test
    public void appendFile() throws IOException {
        String group1 = "group1";
        String path = "M00/01/4B/CgoKTGCee1iEM2qyAAAAAAAAAAA995.mp4";

        String filename = "F:\\oss\\test\\test2.mp4";
        File sourceFile = new File(filename);
        String chunkPath = "F:\\oss\\chunk";
        File chunkDir = new File(chunkPath);
        chunkDir.mkdirs();
        List<File> files = LocalFileOperation.chunkFile(sourceFile, chunkPath, 1024L * 1024L * 5);
        File file = files.get(0);
//        appendFileStorageClient.appendFile(group1, path, FileUtils.openInputStream(file), file.length());
        appendFileStorageClient.truncateFile(group1, path, 2);
//        File file1 = files.get(0);
//        appendFileStorageClient.modifyFile(group1,path,FileUtils.openInputStream(file1),file1.length(),0);
        for (int i = 0; i < files.size(); i++) {
//            File file = files.get(i);
//            if (i == 0) {
//                appendFileStorageClient.modifyFile(group1, path, FileUtils.openInputStream(file), file.length(),-file.length());
//            }

//            appendFileStorageClient.appendFile(group1, path, FileUtils.openInputStream(file), file.length());
        }

    }

    @Test
    void upload() {
        FastDFSClient fastDFSClient = new FastDFSClient();
        String filename = "F:\\oss\\test\\test2.mp4";
        String upload = fastDFSClient.upload(new File(filename), filename);
        System.out.println(upload);
    }

    @Test
    public void testUpload() {

        try {
            ClientGlobal.init("fdfs_client.conf");
            TrackerClient tracker = new TrackerClient();
            TrackerServer trackerServer = tracker.getTrackerServer();
            StorageServer storageServer = null;

            StorageClient storageClient = new StorageClient(trackerServer, storageServer);
//          NameValuePair nvp = new NameValuePair("age", "18");
//            NameValuePair nvp [] = new NameValuePair[]{
//                    new NameValuePair("age", "18"),
//                    new NameValuePair("sex", "male")
//            };
//            String filename = "F:\\oss\\test\\test2.mp4";
//            String fileIds[] = storageClient.upload_file(filename, "mp4", nvp);
//
//            System.out.println(fileIds.length);
//            System.out.println("组名：" + fileIds[0]);
//            System.out.println("路径: " + fileIds[1]);
            String group1 = "group1";
            String path = "M00/01/4B/CgoKTGCee1iEM2qyAAAAAAAAAAA995.mp4";
            int s = storageClient.truncate_file(group1, path, 1);
            System.out.println(s);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (MyException e) {
            e.printStackTrace();
        }
    }
}
