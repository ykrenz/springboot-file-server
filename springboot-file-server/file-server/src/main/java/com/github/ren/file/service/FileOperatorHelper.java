package com.github.ren.file.service;

import cn.hutool.core.util.StrUtil;
import com.github.ren.file.config.FileServerProperties;
import com.github.ren.file.model.ChunkRequest;
import com.github.ren.file.model.ChunkType;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * @author Mr Ren
 * @Description: 文件操作帮助类
 * @date 2021/4/8 13:01
 */
@Component
public class FileOperatorHelper {

    @Autowired
    private FileServerProperties fileServerProperties;

    public ChunkOperator getChunkOperator(ChunkRequest chunkRequest) {
        String chunkTempPath = fileServerProperties.getChunkTempPath();
        ChunkType chunk = fileServerProperties.getChunk();
        switch (chunk) {
            case LOCAL:
                return new LocalChunkOperator(
                        chunkTempPath,
                        chunkRequest.getIdentifier(),
                        chunkRequest.getChunkNumber(),
                        chunkRequest.getChunkSize());
            case REDIS:
                return null;
            default:
                return null;
        }
    }

    /**
     * 创建上传文件名称
     *
     * @param fileName
     * @param md5
     * @return
     */
    public String createObjectName(String fileName, String md5) {
        String extension = FilenameUtils.getExtension(fileName);
        String prefix = "";
        if (extension != null) {
            switch (extension.toLowerCase()) {
                case "png":
                case ".png":
                case "jpg":
                case ".jpg":
                case "jpeg":
                case ".jpeg":
                case "gif":
                case ".gif":
                case "bmp":
                case ".bmp":
                    prefix = "pics/";
                    break;
                default:
                    break;
            }
        }
        return DateFormatUtils.format(new Date(), "yyyyMMdd")
                + StrUtil.SLASH + prefix + md5 + StrUtil.DOT + extension;
    }

}
