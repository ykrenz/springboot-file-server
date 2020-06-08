package com.ren.file.service;

import com.github.ren.file.clients.FastDfsFileClient;
import com.github.ren.file.clients.LocalFileClient;
import com.github.ren.file.properties.LocalFileProperties;
import com.ren.file.pojo.request.Chunk;
import com.ren.file.pojo.response.MergeRes;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author RenYinKui
 * @Description:
 * @date 2020/6/2 16:16
 */
@Service
public class FileServiceImpl implements IFileService {

    @Autowired
    private LocalFileClient localFileClient;

    @Autowired
    private FastDfsFileClient fastDfsFileClient;

    @Autowired
    private LocalFileProperties localFileProperties;

    @Override
    public String uploadChunk(Chunk chunk) {
        //直接上传到文件服务
        if (chunk.getTotalSize() <= chunk.getChunkSize()) {
            try (InputStream inputStream = chunk.getFile().getInputStream()) {
                return localFileClient.uploadFile(inputStream, chunk.getIdentifier().concat(".") + FilenameUtils.getExtension(chunk.getFilename()));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        } else {
            //上传到分块目录
            MultipartFile file = chunk.getFile();
            Integer chunkNumber = chunk.getChunkNumber();
            String identifier = chunk.getIdentifier();
            String ch = this.generateChunkPath(identifier);
            try {
                File chunkFile = new File(ch, String.valueOf(chunkNumber));
                if (chunkFile.exists() && !chunkFile.delete()) {
                    throw new RuntimeException("临时文件删除失败");
                }
                file.transferTo(chunkFile);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        return "SUCCESS";
    }

    public String generateChunkPath(String identifier) {
        File file = Paths.get(localFileProperties.getFileStoragePath(), identifier).toFile();
        file.mkdirs();
        if (!file.exists()) {
            throw new RuntimeException("文件夹创建失败");
        }
        return file.getAbsolutePath();
    }

    @Override
    public MergeRes checkChunk(Chunk chunk) {
        MergeRes mergeRes = new MergeRes();
        String identifier = chunk.getIdentifier();
        String chunkPath = this.generateChunkPath(identifier);
        List<File> mergeFileList = fastDfsFileClient.getMergeFileList(chunkPath,Comparator.comparingInt(o->Integer.parseInt(o.getName())));
        List<Integer> chunkNumbers = mergeFileList.stream().map(f -> Integer.parseInt(f.getName())).sorted(Comparator.comparing(Integer::new)).collect(Collectors.toList());
        mergeRes.setChunkNumbers(chunkNumbers);
        if (chunk.getTotalChunks() == chunkNumbers.size()) {
            mergeRes.setMerge(true);
        } else {
            mergeRes.setMerge(false);
        }
        mergeRes.setUploaded(false);
        return mergeRes;
    }

    @Override
    public String mergeChunk(String identifier, String filename) {
        String mergePath = this.generateChunkPath(identifier);
        try {
            return fastDfsFileClient.uploadPart(fastDfsFileClient.getMergeFileList(mergePath, Comparator.comparingInt(o -> Integer.parseInt(o.getName()))), filename);
//            String md5 = localFileClient.mergeFile(mergePath, new File(localFileProperties.getFileStoragePath(), identifier.concat(".").concat(FilenameUtils.getExtension(filename))));
//            if (identifier.equals(md5)) {
//            return md5;
//            } else {
//                throw new RuntimeException("md5验证错误,上传失败");
//            }
        } finally {
            try {
                FileUtils.deleteDirectory(new File(localFileProperties.getFileStoragePath(), identifier));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
