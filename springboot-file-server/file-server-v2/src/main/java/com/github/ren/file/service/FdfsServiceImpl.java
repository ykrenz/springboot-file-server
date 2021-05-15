package com.github.ren.file.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.github.ren.file.entity.TPart;
import com.github.ren.file.ex.ApiException;
import com.github.ren.file.model.ErrorCode;
import com.github.ren.file.model.request.PartRequest;
import com.github.ren.file.sdk.fdfs.FdfsClient;
import com.github.ren.file.sdk.part.PartInfo;
import com.github.ren.file.sdk.part.PartStore;
import com.github.ren.file.sdk.part.UploadPart;
import com.github.ren.file.service.impl.FdfsService;
import com.github.ren.file.service.impl.TPartServiceImpl;
import com.github.tobato.fastdfs.domain.fdfs.StorePath;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;

/**
 * @author RenYinKui
 * @Description:
 * @date 2021/5/14 14:34
 */
@Service
@Slf4j
public class FdfsServiceImpl implements FdfsService {
    @Autowired
    private TPartServiceImpl tPartService;

    @Autowired(required = false)
    @Qualifier("FdfsClient")
    private FdfsClient fdfsClient;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public PartInfo uploadPart(PartRequest partRequest) {
        try {
            String uploadId = partRequest.getUploadId();
            Integer partNumber = partRequest.getPartNumber();
            MultipartFile file = partRequest.getFile();
            long size = file.getSize();
            TPart tPartMaster = tPartService.getBaseMapper().selectOne(new LambdaQueryWrapper<TPart>()
                    .eq(TPart::getUploadid, uploadId)
                    .eq(TPart::getPartnumber, 0)
                    .eq(TPart::getStatus, 1));

            if (tPartMaster == null) {
                throw new ApiException(ErrorCode.UPLOAD_ID_NOT_FOUND);
            }

            String objectName = tPartMaster.getObjectname();

            UploadPart uploadPart = new UploadPart(uploadId, objectName, partNumber, size, file.getInputStream());

            PartStore partStore = fdfsClient.getPartStore();

            PartInfo partInfo = fdfsClient.uploadPart(uploadPart);

            //由于fastdfs分片上传后合并是利用追加上传的方式 completeMultipartUpload时合并文件太慢 这里采用分片上传完成后立马合并分片的机制
            //所以这里建议客户端顺序上传分片
            //第一个分片直接上传到fastdfs
            TPart tPart = new TPart();
            tPart.setObjectname(objectName);
            tPart.setUploadid(uploadId);
            tPart.setCreateTime(LocalDateTime.now());
            tPart.setPartnumber(partNumber);
            tPart.setPartsize(size);
            tPart.setEtag(partInfo.getETag());
            tPart.setStatus(1);

            TPart first = tPartService.getBaseMapper().selectOne(new LambdaQueryWrapper<TPart>()
                    .eq(TPart::getUploadid, uploadId)
                    .eq(TPart::getPartnumber, 1));

            if (partNumber == 1) {
                if (first != null) {
                    String fullpath = first.getObjectname();
                    String group = fullpath.substring(0, fullpath.indexOf("/"));
                    String path = fullpath.substring(fullpath.indexOf("/") + 1);
                    fdfsClient.truncateFile(group, path);
                }
                StorePath storePath = fdfsClient.uploadAppenderFile(null, file.getInputStream(), uploadPart.getPartSize(), FilenameUtils.getExtension(objectName));
                //设置文件已经被传到fastdfs状态
                tPart.setObjectname(storePath.getFullPath());
                tPart.setStatus(-1);
                tPartMaster.setObjectname(storePath.getFullPath());

                tPartService.updateById(tPartMaster);
                tPartService.getBaseMapper().delete(new LambdaQueryWrapper<TPart>()
                        .eq(TPart::getUploadid, uploadId)
                        .eq(TPart::getPartnumber, partNumber));
                tPartService.save(tPart);
                return partInfo;
            }

            tPartService.getBaseMapper().delete(new LambdaQueryWrapper<TPart>()
                    .eq(TPart::getUploadid, uploadId)
                    .eq(TPart::getPartnumber, partNumber));
            tPartService.save(tPart);

            //第一个分片已经上传完成
            if (first.getStatus() == -1) {
                String fullpath = first.getObjectname();
                String group = fullpath.substring(0, fullpath.indexOf("/"));
                String path = fullpath.substring(fullpath.indexOf("/") + 1);
                //如果是第二个分片 直接上传
                if (partNumber == 2) {
                    fdfsClient.modifyFile(group, path,
                            file.getInputStream(), first.getPartsize(), uploadPart.getPartSize());
                    //update db objectName = xxx
                    tPart.setObjectname(fullpath);
                    tPart.setStatus(-1);
                    tPartService.getBaseMapper().delete(new LambdaQueryWrapper<TPart>()
                            .eq(TPart::getUploadid, uploadId)
                            .eq(TPart::getPartnumber, partNumber));
                    tPartService.save(tPart);
                    return partInfo;
                }

                //获取比自己小的序号且未上传分片数据
                List<TPart> tParts = tPartService.getBaseMapper().selectList(new LambdaQueryWrapper<TPart>()
                        .eq(TPart::getUploadid, uploadId)
                        .ge(TPart::getPartnumber, partNumber)
                        .eq(TPart::getStatus, 1).orderByAsc(TPart::getPartnumber));

                int index = 0;

                for (int i = 0; i < tParts.size(); i++) {
                    TPart part = tParts.get(i);
                    if (i == 0) {
                        index = part.getPartnumber();
                    } else if (index != part.getPartnumber()) {
                        break;
                    }
                    UploadPart storeUploadPart = partStore.getUploadPart(uploadId, objectName, part.getPartnumber());
                    fdfsClient.appendFile(group, path,
                            storeUploadPart.getInputStream(), storeUploadPart.getPartSize());
                    //update db objectName = xxx
                    part.setObjectname(fullpath);
                    part.setStatus(-1);
                    tPartService.updateById(part);
                    index++;
                }
            }
            return partInfo;
        } catch (IOException e) {
            return null;
        }
    }
}
