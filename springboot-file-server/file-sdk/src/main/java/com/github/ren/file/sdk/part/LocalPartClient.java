package com.github.ren.file.sdk.part;

import cn.hutool.crypto.digest.MD5;
import com.github.ren.file.sdk.FileIOException;
import com.github.ren.file.sdk.local.LocalFileOperation;
import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

/**
 * @Description 本地分片客户端
 * @Author ren
 * @Since 1.0
 */
public class LocalPartClient implements PartStore {

    private static final Logger logger = LoggerFactory.getLogger(LocalPartClient.class);

    /**
     * 分片文件存放目录
     */
    private String partStore;

    public LocalPartClient(String partStore) {
        this.partStore = partStore;
    }

    public String getPartStore() {
        File fileDir = new File(partStore);
        if (!fileDir.exists() && !fileDir.mkdirs()) {
            throw new RuntimeException("local partDir mkdirs error");
        }
        return partStore;
    }

    public void setPartStore(String partStore) {
        this.partStore = partStore;
    }

    private File getFile(UploadPart part) {
        String uploadId = part.getUploadId();
        return new File(getPartStore(), uploadId + "-" + part.getPartNumber());
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

    private List<File> listFile(String uploadId) {
        File chunkFolder = new File(getPartStore());
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

    @Override
    public void del(String uploadId) {
        listFile(uploadId).forEach(FileUtils::deleteQuietly);
    }
}
