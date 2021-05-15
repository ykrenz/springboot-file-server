package com.github.ren.file.sdk.part;

import com.github.ren.file.sdk.FileIOException;
import com.github.ren.file.sdk.UploadUtil;
import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @Description 本地分片客户端
 * @Author ren
 * @Since 1.0
 */
public class LocalPartStore extends AbstractPartStore {

    private static final Logger logger = LoggerFactory.getLogger(LocalPartStore.class);

    /**
     * 分片文件存放目录
     */
    private String partDir;

    public LocalPartStore(String partDir) {
        this.partDir = partDir;
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

    private final static String uploading = "uploading";

    private final static String complete = "complete";

    private File getUploadingFile(String uploadId, Integer partNumber) {
        return new File(getPartDir(), uploadId + UploadUtil.SLASH + uploading + UploadUtil.DASHED + partNumber);
    }

    private File getCompleteFile(String uploadId, Integer partNumber) {
        return new File(getPartDir(), uploadId + UploadUtil.SLASH + complete + UploadUtil.DASHED + partNumber);
    }

    @Override
    public String uploadPart(UploadPart part) {
        try (InputStream is = part.getInputStream()) {
            File uploadingFile = getUploadingFile(part.getUploadId(), part.getPartNumber());
            FileUtils.copyInputStreamToFile(is, uploadingFile);
            File completeFile = getCompleteFile(part.getUploadId(), part.getPartNumber());
            if (!uploadingFile.renameTo(completeFile) && !completeFile.setReadOnly()) {
                throw new FileIOException("LocalPart upload rename File error");
            }
            return UploadUtil.eTag(completeFile);
        } catch (IOException e) {
            throw new FileIOException("LocalPart upload InputStream error", e);
        } finally {
            UploadUtil.close(part.getInputStream());
        }
    }

    private List<File> listFile(String uploadId) {
        File chunkFolder = new File(getPartDir(), uploadId);
        File[] fileArray = chunkFolder.listFiles();
        if (fileArray == null || fileArray.length == 0) {
            return new ArrayList<>(0);
        }
        return Arrays.asList(fileArray);
    }

    private List<File> listCompleteFile(String uploadId) {
        File chunkFolder = new File(getPartDir(), uploadId);
        File[] fileArray = chunkFolder.listFiles(f -> f.getName().startsWith(complete));
        if (fileArray == null || fileArray.length == 0) {
            return new ArrayList<>(0);
        }
        return Arrays.asList(fileArray);
    }

    private Integer getPartNumber(String filename) {
        return Integer.parseInt(filename.substring(filename.lastIndexOf(UploadUtil.DASHED) + 1));
    }

    @Override
    public List<PartInfo> listParts(String uploadId, String yourObjectName) {
        List<File> files = listCompleteFile(uploadId);
        List<PartInfo> list = new ArrayList<>(files.size());
        for (File file : files) {
            PartInfo partInfo = new PartInfo();
            partInfo.setPartNumber(getPartNumber(file.getName()));
            partInfo.setPartSize(file.length());
            partInfo.setETag(UploadUtil.eTag(file));
            partInfo.setUploadId(uploadId);
            list.add(partInfo);
        }
        return list;
    }

    @Override
    public List<UploadPart> listUploadParts(String uploadId, String yourObjectName) {
        List<File> files = listCompleteFile(uploadId);
        List<UploadPart> parts = new ArrayList<>(files.size());
        for (File file : files) {
            FileInputStream inputStream = null;
            try {
                inputStream = new FileInputStream(file);
            } catch (FileNotFoundException e) {
                for (UploadPart part : parts) {
                    UploadUtil.close(part.getInputStream());
                }
                throw new FileIOException("get local part file InputStream error", e);
            }
            UploadPart uploadPart = new UploadPart(uploadId, yourObjectName, getPartNumber(file.getName()), file.length(), inputStream);
            parts.add(uploadPart);
        }
        return parts;
    }

    @Override
    public UploadPart getUploadPart(String uploadId, String yourObjectName, Integer partNumber) {
        File completeFile = getCompleteFile(uploadId, partNumber);
        try {
            return new UploadPart(uploadId, yourObjectName, partNumber, completeFile.length(), new FileInputStream(completeFile));
        } catch (FileNotFoundException e) {
            throw new FileIOException("get local part file InputStream error", e);
        }
    }

    @Override
    public boolean clear(String uploadId, String yourObjectName) {
        List<File> files = listFile(uploadId);
        for (File file : files) {
            if (!file.delete()) {
                return false;
            }
        }
        File chunkFolder = new File(getPartDir(), uploadId);
        return chunkFolder.delete();
    }

}