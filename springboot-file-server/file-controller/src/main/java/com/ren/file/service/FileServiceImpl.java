package com.ren.file.service;

import com.github.ren.file.clients.FastDfsFileClient;
import com.github.ren.file.clients.LocalFileClient;
import com.github.ren.file.properties.LocalFileProperties;
import com.ren.file.config.FileSreverProperties;
import com.ren.file.enums.RErrorEnum;
import com.ren.file.pojo.request.Chunk;
import com.ren.file.pojo.response.MergeRes;
import com.ren.file.pojo.response.UploadChunkRes;
import com.ren.file.util.R;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author RenYinKui
 * @Description:
 * @date 2020/6/2 16:16
 */
@Service
@Slf4j
public class FileServiceImpl implements IFileService {

    @Autowired
    private LocalFileClient localFileClient;

    @Autowired
    private FastDfsFileClient fastDfsFileClient;

    @Autowired
    private LocalFileProperties localFileProperties;

    @Autowired
    private FileSreverProperties fileSreverProperties;

    @Override
    public MergeRes checkChunk(Chunk chunk) {
        MergeRes mergeRes = new MergeRes();
        //查询数据库MD5校验文件是否已经上传,实现秒传,此处写死false
        mergeRes.setUploaded(false);
        String identifier = chunk.getIdentifier();
        List<File> mergeFileList = this.getMergeFileList(identifier);
        List<Integer> chunkNumbers = mergeFileList.stream().map(f -> Integer.parseInt(f.getName())).sorted(Comparator.comparing(Integer::new)).collect(Collectors.toList());
        mergeRes.setChunkNumbers(chunkNumbers);
        if (chunk.getTotalChunks() == chunkNumbers.size()) {
            mergeRes.setMerge(true);
        } else {
            mergeRes.setMerge(false);
        }
        return mergeRes;
    }

    @Override
    public R<UploadChunkRes> uploadChunk(Chunk chunk) {
        //直接上传到文件服务
        if (chunk.getTotalSize() <= chunk.getChunkSize()) {
            try (InputStream inputStream = chunk.getFile().getInputStream()) {
                UploadChunkRes uploadChunkRes = new UploadChunkRes();
                String url = fastDfsFileClient.uploadFile(inputStream,
                        chunk.getIdentifier().concat(".") + FilenameUtils.getExtension(chunk.getFilename()));
                uploadChunkRes.setUrl(url);
                uploadChunkRes.setMerge(false);
                return R.success(uploadChunkRes);
            } catch (IOException e) {
                return R.fail(RErrorEnum.UPLOAD_CHUNK_ERROR);
            }
        } else {
            //上传到分块目录
            MultipartFile file = chunk.getFile();
            Integer chunkNumber = chunk.getChunkNumber();
            String identifier = chunk.getIdentifier();
            String chunkTempPath = fileSreverProperties.getChunkTempPath();
            try {
                File chunkFile = new File(chunkTempPath, identifier.concat("-") + chunkNumber);
                if (chunkFile.exists() && chunkFile.length() == chunk.getCurrentChunkSize()) {
                    log.info("分块已经上传{}", chunk);
                } else {
                    file.transferTo(chunkFile);
                }
                UploadChunkRes uploadChunkRes = new UploadChunkRes();
                List<File> mergeFileList = this.getMergeFileList(identifier);
                if (chunk.getTotalChunks() == mergeFileList.size()) {
                    uploadChunkRes.setMerge(true);
                } else {
                    uploadChunkRes.setMerge(false);
                }
                return R.success(uploadChunkRes);
            } catch (Exception e) {
                log.error("上传异常", e);
                return R.fail(RErrorEnum.UPLOAD_CHUNK_ERROR);
            }
        }
    }

    private String getChunkTempPath() {
        return fileSreverProperties.getChunkTempPath();
    }

    private List<File> getMergeFileList(String chunkTempPath, String identifier) {
        return fastDfsFileClient.getMergeFileList(chunkTempPath, chunk1 -> chunk1.getName().startsWith(identifier),
                Comparator.comparing(File::getName));
    }

    private List<File> getMergeFileList(String identifier) {
        return fastDfsFileClient.getMergeFileList(this.getChunkTempPath(), chunk1 -> chunk1.getName().startsWith(identifier),
                Comparator.comparing(File::getName));
    }

    @Override
    public R<String> mergeChunk(String identifier, String filename) {
        try {
            List<File> mergeFileList = this.getMergeFileList(identifier);
            String url = fastDfsFileClient.uploadPart(mergeFileList, filename);
            mergeFileList.forEach(FileUtils::deleteQuietly);
//            String md5 = localFileClient.mergeFile(mergePath, new File(localFileProperties.getFileStoragePath(), identifier.concat(".").concat(FilenameUtils.getExtension(filename))));
//            if (identifier.equals(md5)) {
//            return md5;
//            } else {
//                throw new RuntimeException("md5验证错误,上传失败");
//            }
            return R.success(url);
        } catch (Exception e) {
            log.error("合并分片失败", e);
            return R.fail(RErrorEnum.UPLOAD_MERGE_ERROR);
        }
    }
}
