package com.github.ren.file.sdk.fdfs;

import com.alibaba.fastjson.JSON;
import com.github.ren.file.sdk.FileClient;
import com.github.ren.file.sdk.ex.ClientException;
import com.github.ren.file.sdk.ex.FileIOException;
import com.github.ren.file.sdk.model.FastDFSUploadResult;
import com.github.ren.file.sdk.part.*;
import com.github.ren.file.sdk.util.Util;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.csource.common.MyException;
import org.csource.common.NameValuePair;
import org.csource.fastdfs.ProtoCommon;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.UUID;

import static com.aliyun.oss.common.utils.CodingUtils.assertParameterNotNull;

/**
 * @Description fastdfs文件客戶端
 * @Author ren
 * @Since 1.0
 */
public class FastDFSClient implements FileClient {

    private static final Logger logger = LoggerFactory.getLogger(FastDFSClient.class);

    private String group;

    private static class SingletonHolder {
        private static final FastDFSClient INSTANCE = new FastDFSClient();
    }

    public static FastDFSClient getInstance() {
        return SingletonHolder.INSTANCE;
    }

    private FastDFSClient() {
    }

    public String getGroup() {
        return group;
    }

    public void setGroup(String group) {
        this.group = group;
    }

    private FastDFS client() {
        return FastDFSBuilder.build();
    }

    @Override
    public FastDFSUploadResult upload(File file, String objectName) {
        try (InputStream is = new FileInputStream(file.getAbsolutePath())) {
            FastDFS client = client();
            byte[] content = IOUtils.toByteArray(is);
            String[] result = client.upload_file(group, content, FilenameUtils.getExtension(file.getName()), null);
            String group = result[0];
            String path = result[1];
            return new FastDFSUploadResult(group, path, Util.eTag(content));
        } catch (IOException | MyException e) {
            throw new ClientException(e.getMessage());
        }
    }

    @Override
    public FastDFSUploadResult upload(InputStream is, String objectName) {
        try {
            FastDFS client = client();
            byte[] content = IOUtils.toByteArray(is);
            String[] result = client.upload_file(group, content, FilenameUtils.getExtension(objectName), null);
            String group = result[0];
            String path = result[1];
            return new FastDFSUploadResult(group, path, Util.eTag(content));
        } catch (IOException | MyException e) {
            throw new ClientException(e.getMessage());
        }
    }

    @Override
    public FastDFSUploadResult upload(byte[] content, String objectName) {
        try {
            FastDFS client = client();
            String[] result = client.upload_file(group, content, FilenameUtils.getExtension(objectName), null);
            String group = result[0];
            String path = result[1];
            return new FastDFSUploadResult(group, path, Util.eTag(content));
        } catch (IOException | MyException e) {
            throw new ClientException(e.getMessage());
        }
    }

    @Override
    public FastDFSUploadResult upload(String url, String objectName) {
        try (InputStream is = new URL(url).openStream()) {
            return this.upload(is, objectName);
        } catch (IOException e) {
            throw new FileIOException("fdfs upload url file error", e);
        }
    }

    protected String getExt(String fileName) {
        String extension = "";
        if (fileName != null) {
            extension = FilenameUtils.getExtension(fileName);
        }
        return extension;
    }

    @Override
    public InitMultipartResponse initMultipartUpload(InitMultipartUploadArgs args) {
        assertParameterNotNull(args, "InitMultipartUploadArgs");
        long fileSize = args.getFileSize();
        if (fileSize <= 0) {
            throw new IllegalArgumentException("文件总大小参数必须大于0");
        }

        try {
            FastDFS client = client();
            String objectName = args.getObjectName();
            //初始化 upload append文件
            objectName = client.upload_appender_file1(group, new byte[]{}, getExt(objectName), null);
            client.truncate_file1(objectName, fileSize);
            String uploadId = UUID.randomUUID().toString();
            return new InitMultipartResponse(uploadId, objectName);
        } catch (IOException | MyException e) {
            throw new ClientException("init part upload error", e);
        }
    }

    @Override
    public UploadMultipartResponse uploadMultipart(UploadPartArgs part) {
        try {
            String uploadId = part.getUploadId();
            String objectName = part.getObjectName();
            long partSize = part.getPartSize();
            int partNumber = part.getPartNumber();
            FastDFS client = client();
            long offset = 0;
            if (partNumber != 1) {
                offset = (partNumber - 1) * partSize;
            }
            long fileSize = part.getFileSize();
            //处理最后一个分片
            if (offset + partSize > fileSize) {
                offset = fileSize - partSize;
            }
            client.modify_file1(objectName, offset, IOUtils.toByteArray(part.getInputStream()));
            UploadMultipartResponse uploadMultipartResponse = new UploadMultipartResponse();
            uploadMultipartResponse.setUploadId(uploadId);
            uploadMultipartResponse.setPartSize(partSize);
            uploadMultipartResponse.setPartNumber(partNumber);
            //uploadMultipartResponse.setETag();
            NameValuePair[] metaList = new NameValuePair[]{
                    new NameValuePair("part" + partNumber, JSON.toJSONString(uploadMultipartResponse))
            };
            client.set_metadata1(objectName, metaList, ProtoCommon.STORAGE_SET_METADATA_FLAG_MERGE);
            return uploadMultipartResponse;
        } catch (IOException | MyException e) {
            throw new ClientException("upload part error", e);
        }
    }

    @Override
    public List<UploadMultipartResponse> listMultipartUpload(ListMultipartUploadArgs args) {
        String objectName = args.getObjectName();
        try {
            FastDFS client = client();
            List<UploadMultipartResponse> uploadMultipartResponses = new ArrayList<>();
            NameValuePair[] metadata = client.get_metadata1(objectName);
            if (metadata != null) {
                for (NameValuePair pair : metadata) {
                    String name = pair.getName();
                    String value = pair.getValue();
                    if (StringUtils.startsWith(name, "part") && JSON.isValid(value)) {
                        uploadMultipartResponses.add(JSON.parseObject(value, UploadMultipartResponse.class));
                    }
                }
            }
            uploadMultipartResponses.sort(Comparator.comparingInt(UploadMultipartResponse::getPartNumber));
            return uploadMultipartResponses;
        } catch (IOException | MyException e) {
            throw new ClientException("list part error", e);
        }
    }

    @Override
    public CompleteMultipartResponse completeMultipartUpload(String uploadId, String objectName, List<UploadMultipartResponse> parts) {
        try {
            FastDFS client = client();
            client.set_metadata1(objectName, null, ProtoCommon.STORAGE_SET_METADATA_FLAG_OVERWRITE);
            //转换成普通文件
            String reObjectName = client.regenerate_appender_filename1(objectName);
            if (reObjectName != null) {
                objectName = reObjectName;
            }
            String eTag = Util.completeMultipartMd5(parts);
            return new CompleteMultipartResponse(eTag, objectName);
        } catch (IOException | MyException e) {
            throw new ClientException("complete part error", e);
        }
    }

    @Override
    public void abortMultipartUpload(String uploadId, String objectName) {
        try {
            FastDFS client = client();
            client.delete_file1(objectName);
        } catch (IOException | MyException e) {
            throw new ClientException("abort part upload error", e);
        }
    }

    @Override
    public int getPartExpirationDays() {
        return -1;
    }

}
