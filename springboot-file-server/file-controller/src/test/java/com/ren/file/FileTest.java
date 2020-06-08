package com.ren.file;

import com.github.ren.file.clients.AliOssFileClient;
import com.github.ren.file.clients.LocalFileClient;
import com.github.ren.file.clients.LocalFileServerClient;
import com.github.ren.file.properties.LocalFileProperties;
import com.github.tobato.fastdfs.service.AppendFileStorageClient;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

/**
 * @author RenYinKui
 * @Description:
 * @date 2020/5/29 14:15
 */
@SpringBootTest
@RunWith(SpringRunner.class)
public class FileTest {
    @Autowired
    private LocalFileClient localFileClient;

    @Test
    public void test() throws IOException {
        String path = "E:\\gitee\\test\\1.avi";
//        "E:\\迅雷下载\\CentOS-7-x86_64-DVD-1908.iso"
        String chunkPath = "E:\\gitee\\test\\chunk\\";
        splitFile(path, chunkPath);
//        localFileServerClient.uploadFile(new File("E:\\迅雷下载\\CentOS-7-x86_64-DVD-1908.iso"));
        File[] files = new File(chunkPath).listFiles();
//        String fileStoragePath = localFileProperties.getFileStoragePath();
        Path path1 = Paths.get(DateFormatUtils.format(new Date(), DateFormatUtils.ISO_8601_EXTENDED_DATE_FORMAT.getPattern())
                , String.valueOf(System.nanoTime()), UUID.randomUUID().toString() + "." + FilenameUtils.getExtension(path));
        if (files != null && files.length > 0) {
            //转成集合，便于排序
            List<File> fileList = new ArrayList<>(Arrays.asList(files));
            //从小到大排序
            fileList.sort(Comparator.comparingInt(o -> Integer.parseInt(o.getName())));
            localFileClient.uploadPart(fileList, path1.toString());
        }
    }

    //拆分文件
    public static void splitFile(String path, String chunkPath) throws IOException {
        // 每次读10个字节
//        int chunkSize = 1 << 30;
        int chunkSize = 1 << 20;
        // 获取文件内容的长度
        long length = new File(path).length();
        // 分割文件的数量
        int chunkNum = Math.max(1, (int) Math.ceil(length / (float) chunkSize));
        System.out.println(chunkNum);
        System.out.println(Math.max(1, (int) Math.ceil(0)));
        //缓冲区大小
        byte[] buff = new byte[1024 * 1024];
        //使用RandomAccessFile访问文件
        RandomAccessFile raf_read = new RandomAccessFile(new File(path), "r");
        //分块
        for (int i = 0; i < chunkNum; i++) {
            //创建分块文件
            File file = new File(chunkPath + i);
            if (file.exists()) {
                file.delete();
            }
            //向分块文件中写数据
            RandomAccessFile raf_write = new RandomAccessFile(file, "rw");
            int len = -1;
            while ((len = raf_read.read(buff)) != -1) {
                raf_write.write(buff, 0, len);
                if (file.length() > chunkSize) {
                    break;
                }
            }
            raf_write.close();
        }
        raf_read.close();
    }
}
