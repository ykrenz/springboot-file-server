package com.github.ren.file.client.part;

import com.github.ren.file.client.util.Util;
import com.github.ren.file.client.ex.FileIOException;
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
public class LocalPartStore implements PartStore {

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
        return new File(getPartDir(), uploadId + Util.SLASH + uploading + Util.DASHED + partNumber);
    }

    private File getCompleteFile(String uploadId, Integer partNumber) {
        return new File(getPartDir(), uploadId + Util.SLASH + complete + Util.DASHED + partNumber);
    }

    @Override
    public String uploadPart(UploadPartArgs part) {
        try (InputStream is = part.getInputStream()) {
            File uploadingFile = getUploadingFile(part.getUploadId(), part.getPartNumber());
            FileUtils.copyInputStreamToFile(is, uploadingFile);
            File completeFile = getCompleteFile(part.getUploadId(), part.getPartNumber());
            if (!uploadingFile.renameTo(completeFile) && !completeFile.setReadOnly()) {
                throw new FileIOException("LocalPart upload rename File error");
            }
            return Util.eTag(completeFile);
        } catch (IOException e) {
            throw new FileIOException("LocalPart upload InputStream error", e);
        } finally {
            Util.close(part.getInputStream());
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
        return Integer.parseInt(filename.substring(filename.lastIndexOf(Util.DASHED) + 1));
    }

    @Override
    public List<UploadMultipartResponse> listParts(String uploadId, String yourObjectName) {
        List<File> files = listCompleteFile(uploadId);
        List<UploadMultipartResponse> list = new ArrayList<>(files.size());
        for (File file : files) {
            UploadMultipartResponse uploadMultipartResponse = new UploadMultipartResponse();
            uploadMultipartResponse.setPartNumber(getPartNumber(file.getName()));
            uploadMultipartResponse.setPartSize(file.length());
            uploadMultipartResponse.setETag(Util.eTag(file));
            uploadMultipartResponse.setUploadId(uploadId);
            list.add(uploadMultipartResponse);
        }
        return list;
    }

    @Override
    public UploadPartArgs getUploadPart(String uploadId, String yourObjectName, Integer partNumber) {
        File completeFile = getCompleteFile(uploadId, partNumber);
        try {
            return new UploadPartArgs(uploadId, yourObjectName, partNumber, completeFile.length(), new FileInputStream(completeFile));
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