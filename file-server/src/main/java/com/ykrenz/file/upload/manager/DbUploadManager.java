package com.ykrenz.file.upload.manager;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ykrenz.file.dao.mapper.entity.FilePartEntity;
import com.ykrenz.file.dao.mapper.entity.FileUploadEntity;
import com.ykrenz.file.dao.mapper.FilePartMapper;
import com.ykrenz.file.dao.mapper.FileUploadMapper;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class DbUploadManager implements UploadManager<InitUploadModel, UploadModel, PartModel> {

    @Resource
    private FilePartMapper filePartMapper;

    @Resource
    private FileUploadMapper fileUploadMapper;

    @Override
    public UploadModel createUpload(InitUploadModel init) {
        String uploadId = UUID.randomUUID().toString();
        FileUploadEntity fileUploadEntity = new FileUploadEntity();
        fileUploadEntity.setUploadId(uploadId);
        fileUploadEntity.setFileName(init.getFileName());
        fileUploadEntity.setFileSize(init.getFileSize());
        fileUploadEntity.setPartSize(init.getPartSize());
        fileUploadEntity.setBucketName(init.getBucketName());
        fileUploadEntity.setObjectName(init.getObjectName());
        fileUploadMapper.insert(fileUploadEntity);

        UploadModel uploadModel = new UploadModel();
        BeanUtils.copyProperties(init, uploadModel);
        uploadModel.setUploadId(uploadId);
        uploadModel.setCreateTime(fileUploadEntity.getCreateTime().getTime());
        return uploadModel;
    }

    @Override
    public UploadModel getUpload(String uploadId) {
        FileUploadEntity fileUploadEntity = fileUploadMapper.selectOne(
                Wrappers.<FileUploadEntity>lambdaQuery()
                        .eq(FileUploadEntity::getUploadId, uploadId)
                        .eq(FileUploadEntity::getStatus, 1)
        );

        if (Objects.isNull(fileUploadEntity)) {
            return null;
        }
        return convertInitUpload(fileUploadEntity);
    }

    private UploadModel convertInitUpload(FileUploadEntity fileUploadEntity) {
        UploadModel uploadModel = new UploadModel();
        uploadModel.setUploadId(fileUploadEntity.getUploadId());
        uploadModel.setCreateTime(fileUploadEntity.getCreateTime().getTime());
        uploadModel.setFileName(fileUploadEntity.getFileName());
        uploadModel.setFileSize(fileUploadEntity.getFileSize());
        uploadModel.setBucketName(fileUploadEntity.getBucketName());
        uploadModel.setObjectName(fileUploadEntity.getObjectName());
        uploadModel.setPartSize(fileUploadEntity.getPartSize());
        return uploadModel;
    }

    @Override
    public List<UploadModel> listUploads(ListUploadParam listUploadParam) {
        IPage<FileUploadEntity> page = new Page<>(listUploadParam.getPage(), listUploadParam.getLimit());
        return fileUploadMapper.selectPage(page,
                Wrappers.<FileUploadEntity>lambdaQuery()
                        .eq(FileUploadEntity::getStatus, 1)
                        .orderByAsc(FileUploadEntity::getCreateTime)
        ).getRecords().stream().map(this::convertInitUpload).collect(Collectors.toList());
    }

    @Override
    public void savePart(PartModel partModel) {
        FilePartEntity filePartEntity = new FilePartEntity();
        filePartEntity.setUploadId(partModel.getUploadId());
        filePartEntity.setBucketName(partModel.getBucketName());
        filePartEntity.setObjectName(partModel.getObjectName());
        filePartEntity.setFileSize(partModel.getSize());
        filePartEntity.setPartNumber(partModel.getPartNumber());
        String eTag = partModel.getETag();
        filePartEntity.setStatus(1);
        filePartMapper.insert(filePartEntity);
    }

    @Override
    public List<PartModel> listParts(String uploadId) {
        return filePartMapper.selectList(
                Wrappers.<FilePartEntity>lambdaQuery()
                        .eq(FilePartEntity::getUploadId, uploadId)
                        .eq(FilePartEntity::getStatus, 1)
                        .gt(FilePartEntity::getPartNumber, 0))
                .stream().map(this::convertPart).collect(Collectors.toList());
    }

    private PartModel convertPart(FilePartEntity filePartEntity) {
        PartModel partModel = new PartModel();
        partModel.setUploadId(filePartEntity.getUploadId());
        partModel.setSize(filePartEntity.getFileSize());
        partModel.setBucketName(filePartEntity.getBucketName());
        partModel.setObjectName(filePartEntity.getObjectName());
        partModel.setPartNumber(filePartEntity.getPartNumber());
        return partModel;
    }

    @Override
    public void clearParts(String uploadId) {
        fileUploadMapper.delete(Wrappers.<FileUploadEntity>lambdaQuery().eq(FileUploadEntity::getUploadId, uploadId));
        filePartMapper.delete(Wrappers.<FilePartEntity>lambdaQuery().eq(FilePartEntity::getUploadId, uploadId));
    }

}
