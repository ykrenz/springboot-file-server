package com.github.ren.file.service.impl;

import com.github.ren.file.model.request.PartRequest;
import com.github.ren.file.sdk.FileClient;
import com.github.ren.file.sdk.part.CompleteMultipart;
import com.github.ren.file.sdk.part.PartInfo;
import com.github.ren.file.service.FileService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * @author RenYinKui
 * @Description: 文件逻辑类
 * @date 2021/5/13 10:41
 */
@Service
@Slf4j
public class FileServiceImpl implements FileService {

    @Autowired
    private FileClient fileClient;

    @Autowired
    private TPartServiceImpl tPartService;

    @Override
    public String upload(MultipartFile file) {
        return null;
    }

    @Override
    public String check(String md5) {
        return null;
    }

    @Override
    public String initiateMultipartUpload(String filename) {
        return null;
    }

    @Override
    public List<PartInfo> listParts(String uploadId) {
        return null;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public PartInfo uploadPart(PartRequest partRequest) {
        return null;
    }

    @Override
    public CompleteMultipart completeMultipartUpload(String uploadId, String md5) {
        return null;
    }

    @Override
    public String abortMultipartUpload(String uploadId) {
        return null;
    }
}
