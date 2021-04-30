package com.github.ren.file.sdk.part;

import cn.hutool.crypto.digest.MD5;
import com.github.ren.file.sdk.FileIOException;
import com.github.ren.file.sdk.local.LocalFileOperation;
import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.nio.channels.FileChannel;
import java.nio.file.Paths;
import java.util.*;

/**
 * @Description 本地分片客户端
 * @Author ren
 * @Since 1.0
 */
public class LocalPartClient implements UploadPartClient {

    private static final Logger logger = LoggerFactory.getLogger(LocalPartClient.class);

    /**
     * 分块文件存放目录
     */
    private String partDir;

    /**
     * 文件存储目录
     */
    private String localStore;

    public LocalPartClient(String partDir, String localStore) {
        this.partDir = partDir;
        this.localStore = localStore;
    }

    public String getPartDir() {
        File fileDir = new File(partDir);
        if (!fileDir.exists() && !fileDir.mkdirs()) {
            throw new RuntimeException("local partDir mkdirs error");
        }
        return partDir;
    }

    public void setPartDir(String partDir) {
        this.partDir = partDir;
    }

    public String getLocalStore() {
        File fileDir = new File(localStore);
        if (!fileDir.exists() && !fileDir.mkdirs()) {
            throw new RuntimeException("localStore mkdirs error");
        }
        return localStore;
    }

    public void setLocalStore(String localStore) {
        this.localStore = localStore;
    }

    public File getOutFile(String yourObjectName) {
        String relativePath = Paths.get(getLocalStore(), yourObjectName).toString();
        File fileDir = new File(relativePath).getParentFile();
        if (!fileDir.exists() && !fileDir.mkdirs()) {
            throw new RuntimeException("local mkdirs error");
        }
        return new File(relativePath);
    }

    private File getFile(UploadPart part) {
        String uploadId = part.getUploadId();
        return new File(getPartDir(), uploadId + "-" + part.getPartNumber());
    }

    @Override
    public String initUpload() {
        return UUID.randomUUID().toString().replace("-", "");
    }

    @Override
    public void uploadPart(UploadPart part) {
        InputStream is = null;
        try {
            File file = getFile(part);
            is = part.getInputStream();
            FileUtils.copyInputStreamToFile(is, file);
        } catch (IOException e) {
            throw new FileIOException("LocalPart upload InputStream error", e);
        } finally {
            LocalFileOperation.close(part.getInputStream());
        }
    }

    @Override
    public void uploadParts(List<UploadPart> parts) {
        parts.forEach(this::uploadPart);
    }

    private List<File> listFile(String uploadId) {
        File chunkFolder = new File(getPartDir());
        //分块列表
        File[] fileArray = chunkFolder.listFiles(f -> f.getName().startsWith(uploadId));
        if (fileArray == null || fileArray.length == 0) {
            return new ArrayList<>(0);
        }
        return Arrays.asList(fileArray);
    }

    private Integer getPartNumber(String filename) {
        return Integer.parseInt(filename.substring(filename.lastIndexOf("-")));
    }

    private String getMd5Digest(File file) {
        return MD5.create().digestHex(file);
    }

    @Override
    public List<PartInfo> listParts(String uploadId) {
        List<File> files = listFile(uploadId);
        List<PartInfo> list = new ArrayList<>(files.size());
        for (File file : files) {
            PartInfo partInfo = new PartInfo();
            partInfo.setPartNumber(getPartNumber(file.getName()));
            partInfo.setPartSize(file.length());
            partInfo.setMd5Digest(getMd5Digest(file));
            partInfo.setUploadId(uploadId);
            list.add(partInfo);
        }
        return list;
    }

    @Override
    public String complete(String uploadId, String yourObjectName) {
        List<UploadPart> parts = listUploadParts(uploadId);
        parts.sort(Comparator.comparingInt(UploadPart::getPartNumber));
        File outFile = this.getOutFile(yourObjectName);
        try (FileChannel outChannel = new FileOutputStream(outFile).getChannel()) {
            //同步nio 方式对分片进行合并, 有效的避免文件过大导致内存溢出
            for (UploadPart uploadPart : parts) {
                long chunkSize = 1L << 32;
                if (uploadPart.getPartSize() >= chunkSize) {
                    throw new RuntimeException("文件分片必须<4G");
                }
                try (FileChannel inChannel = ((FileInputStream) uploadPart.getInputStream()).getChannel()) {
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
            }
        } catch (IOException e) {
            throw new FileIOException("local complete file error", e);
        } finally {
            for (UploadPart part : parts) {
                LocalFileOperation.close(part.getInputStream());
            }
        }
        return yourObjectName;
    }

    @Override
    public void cancel(String uploadId, String yourObjectName) {
        listFile(uploadId).forEach(FileUtils::deleteQuietly);
        FileUtils.deleteQuietly(new File(getPartDir(), yourObjectName));
    }

    @Override
    public List<UploadPart> listUploadParts(String uploadId) {
        List<File> files = listFile(uploadId);
        List<UploadPart> parts = new ArrayList<>(files.size());
        for (File file : files) {
            FileInputStream inputStream = null;
            try {
                inputStream = new FileInputStream(file);
            } catch (FileNotFoundException e) {
                for (UploadPart part : parts) {
                    LocalFileOperation.close(part.getInputStream());
                }
                throw new FileIOException("get local part file InputStream error", e);
            }
            UploadPart uploadPart = new UploadPart(uploadId, getPartNumber(file.getName()), file.length(), inputStream);
            parts.add(uploadPart);
        }
        return parts;
    }
}
