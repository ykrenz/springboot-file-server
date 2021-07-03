package com.github.ren.file.client.local;

import com.github.ren.file.client.util.Util;

import javax.activation.MimetypesFileTypeMap;
import java.io.*;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import java.util.List;

/**
 * @Description 本地文件操作类
 * @Author ren
 * @Since 1.0
 */
public final class LocalFileOperation {

    public static void copyFile(File inputFile, File outFile) throws IOException {
        // <2G 采用Channel方式高效率复制文件
        if (inputFile.length() < Integer.MAX_VALUE) {
            try (FileChannel inChannel = new FileInputStream(inputFile).getChannel();
                 FileChannel outChannel = new FileOutputStream(outFile).getChannel()) {
                //同步nio 方式对分片进行合并, 有效的避免文件过大导致内存溢出
                int position = 0;
                long size = inChannel.size();
                while (0 < size) {
                    long count = inChannel.transferTo(position, size, outChannel);
                    if (count > 0) {
                        position += count;
                        size -= count;
                    }
                }
            }
        } else {
            try (FileInputStream fileInputStream = new FileInputStream(inputFile);
                 FileOutputStream fileOutputStream = new FileOutputStream(outFile)) {
                //1M
                byte[] buffer = new byte[1 << 20];
                int length;
                while ((length = fileInputStream.read(buffer)) != -1) {
                    fileOutputStream.write(buffer, 0, length);
                }
            }
        }
    }

    public static void copyFile(InputStream inputStream, File outFile) throws IOException {
        try (FileOutputStream fileOutputStream = new FileOutputStream(outFile)) {
            //1M
            byte[] buffer = new byte[1 << 20];
            int length;
            while ((length = inputStream.read(buffer)) != -1) {
                fileOutputStream.write(buffer, 0, length);
            }
        }
    }

    public static List<File> chunkFile(File file, String chunkPath, long chunkSize) throws IOException {
        // 分割文件的数量
        int chunkNum = Math.max(1, (int) Math.ceil(file.length() / (float) chunkSize));
        List<File> chunks = new ArrayList<>(chunkNum);
        //缓冲区大小
        byte[] buff = new byte[1024 * 1024];
        //使用RandomAccessFile访问文件
        RandomAccessFile rafRead = new RandomAccessFile(file, "r");
        //分块
        for (int i = 1; i <= chunkNum; i++) {
            //创建分块文件
            File chunkFile = new File(chunkPath, String.valueOf(i));
            if (chunkFile.exists() && !chunkFile.delete()) {
                throw new RuntimeException("临时分块文件删除失败");
            }
            //向分块文件中写数据
            RandomAccessFile rafWrite = new RandomAccessFile(chunkFile, "rw");
            int len = -1;
            while ((len = rafRead.read(buff)) != -1) {
                rafWrite.write(buff, 0, len);
                if (chunkFile.length() >= chunkSize) {
                    break;
                }
            }
            rafWrite.close();
            chunks.add(chunkFile);
        }
        rafRead.close();
        return chunks;
    }

    public static String mergeFile(List<File> files, File mergeFile) throws IOException {
        //合并文件
        if (mergeFile.exists() && !mergeFile.delete()) {
            throw new RuntimeException("文件删除失败");
        }
        //创建新的合并文件
        RandomAccessFile rafWrite = new RandomAccessFile(mergeFile, "rw");
        //指针指向文件顶端
        rafWrite.seek(0);
        //缓冲区
        byte[] b = new byte[1024];
        //合并文件
        for (File chunkFile : files) {
            RandomAccessFile rafRead2 = new RandomAccessFile(chunkFile, "rw");
            int len = -1;
            while ((len = rafRead2.read(b)) != -1) {
                rafWrite.write(b, 0, len);
            }
            rafRead2.close();
        }
        rafWrite.close();
        return Util.eTag(mergeFile);
    }

    public static String getContentType(File file) {
        return new MimetypesFileTypeMap().getContentType(file);
    }
}
