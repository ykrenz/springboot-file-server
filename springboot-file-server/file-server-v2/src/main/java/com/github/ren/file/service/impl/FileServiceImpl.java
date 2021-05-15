package com.github.ren.file.service.impl;

import com.github.ren.file.entity.TPart;
import com.github.ren.file.model.request.PartRequest;
import com.github.ren.file.sdk.FileClient;
import com.github.ren.file.sdk.objectname.UuidGenerator;
import com.github.ren.file.sdk.part.CompleteMultipart;
import com.github.ren.file.sdk.part.PartInfo;
import com.github.ren.file.service.FileService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @author RenYinKui
 * @Description:
 * @date 2021/5/13 10:41
 */
@Service
@Slf4j
public class FileServiceImpl implements FileService {

    @Autowired
    @Qualifier("LocalClient")
    private FileClient fileClient;

    @Autowired
    private TPartServiceImpl tPartService;

    @Autowired
    private FdfsService fdfsService;

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
        String objectName = new UuidGenerator(filename).generator();
        String uploadId = fileClient.initiateMultipartUpload(objectName);
        //TODO 保存到数据库 objectName uploadId
        //save to t_part uploadId objectName status = 0
        TPart tPart = new TPart();
        tPart.setObjectname(objectName);
        tPart.setUploadid(uploadId);
        tPart.setCreateTime(LocalDateTime.now());
        tPart.setStatus(1);
        tPartService.save(tPart);
        return uploadId;
    }

    @Override
    public List<PartInfo> listParts(String uploadId) {
        return null;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public PartInfo uploadPart(PartRequest partRequest) {
//        fileClient.uploadPart();
//        return fdfsService.uploadPart(partRequest);
        return null;
    }

    @Override
    public CompleteMultipart completeMultipartUpload(String uploadId, String md5) {
//        fileClient.completeMultipartUpload(uploadId, objectName);
        return null;
    }

    @Override
    public String abortMultipartUpload(String uploadId) {
        return null;
    }
}
