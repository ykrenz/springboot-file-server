package com.github.ren.file.sdk.part;

import cn.hutool.crypto.digest.MD5;
import com.github.ren.file.sdk.FileIOException;
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
public class LocalPartClient implements UploadPartClient {

    private static final Logger logger = LoggerFactory.getLogger(LocalPartClient.class);

    /**
     * 分块文件存放目录
     */
    private final String partDir;

    public LocalPartClient(String partDir) {
        this.partDir = partDir;
    }

    private File getFile(UploadPart part) {
        String uploadId = part.getUploadId();
        return new File(partDir, uploadId + "-" + part.getPartNumber());
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
            if (is != null) {
                try {
                    is.close();
                } catch (IOException e) {
                    logger.error("LocalPart upload InputStream close error", e);
                }
            }

        }
    }

    @Override
    public void uploadParts(List<UploadPart> parts) {
        parts.forEach(this::uploadPart);
    }

    private List<File> listFile(String uploadId) {
        File chunkFolder = new File(partDir);
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
        return null;
    }

    @Override
    public void cancel(String uploadId, String yourObjectName) {
        listFile(uploadId).forEach(FileUtils::deleteQuietly);
        FileUtils.deleteQuietly(new File(partDir, yourObjectName));
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
                    try {
                        if (part.getInputStream() != null) {
                            part.getInputStream().close();
                        }
                    } catch (IOException ioException) {
                        logger.error("listUploadParts part InputStream close error", e);
                    }
                }
                throw new FileIOException("get local part file InputStream error", e);
            }
            UploadPart uploadPart = new UploadPart(uploadId, getPartNumber(file.getName()), file.length(), inputStream);
            parts.add(uploadPart);
        }
        return parts;
    }
}
