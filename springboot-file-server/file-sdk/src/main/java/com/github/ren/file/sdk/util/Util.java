package com.github.ren.file.sdk.util;

import com.github.ren.file.sdk.ex.FileIOException;
import com.github.ren.file.sdk.part.UploadMultipartResponse;
import org.apache.commons.codec.digest.DigestUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

/**
 * @Description 上传工具类
 * @Author ren
 * @Since 1.0
 */
public class Util implements StrPool {

    public static void close(InputStream is) {
        if (is != null) {
            try {
                is.close();
            } catch (IOException e) {
                throw new FileIOException("close InputStream error ", e);
            }
        }
    }

    public static String eTag(File file) {
        try {
            return DigestUtils.md5Hex(new FileInputStream(file));
        } catch (IOException e) {
            throw new FileIOException("eTag InputStream error", e);
        }
    }

    public static String eTag(InputStream inputStream) {
        try {
            return DigestUtils.md5Hex(inputStream);
        } catch (IOException e) {
            throw new FileIOException("eTag InputStream error", e);
        }
    }

    public static String eTag(String str) {
        return DigestUtils.md5Hex(str);
    }

    public static String eTag(byte[] data) {
        return DigestUtils.md5Hex(data);
    }

    public static String completeMultipartMd5(List<UploadMultipartResponse> uploadMultipartResponses) {
        StringBuilder md5Builder = new StringBuilder();
        for (UploadMultipartResponse uploadMultipartResponse : uploadMultipartResponses) {
            String eTag = uploadMultipartResponse.getETag();
            md5Builder.append(eTag);
        }
        return eTag(md5Builder.toString()) + DASHED + uploadMultipartResponses.size();
    }
}
