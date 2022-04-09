package com.ykrenz.file.dao;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.ykrenz.file.dao.mapper.entity.FileEntity;
import com.ykrenz.file.dao.mapper.entity.FileStorageEntity;
import com.ykrenz.file.dao.mapper.FileStorageMapper;
import com.ykrenz.file.dao.mapper.FileMapper;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

@Repository
public class MapperDao implements FileDao {

    @Resource
    private FileStorageMapper fileStorageMapper;
    @Resource
    private FileMapper fileMapper;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public String save(FileModel fileModel) {
        FileStorageEntity fileStorageEntity = new FileStorageEntity();
        fileStorageEntity.setBucketName(fileModel.getBucketName());
        fileStorageEntity.setObjectName(fileModel.getObjectName());
        fileStorageEntity.setFileSize(fileModel.getFileSize());
        fileStorageEntity.setCrc(fileModel.getCrc());
        fileStorageEntity.setStatus(1);
        fileStorageMapper.insert(fileStorageEntity);

        FileEntity fileEntity = new FileEntity();
        fileEntity.setFileId(fileStorageEntity.getId());
        fileEntity.setFileName(fileModel.getFileName());
        fileMapper.insert(fileEntity);
        return fileEntity.getFileId();
    }

    @Override
    public FileModel getById(String id) {
        FileEntity fileEntity = fileMapper.selectById(id);
        if (fileEntity == null) {
            return null;
        }
        FileStorageEntity fileStorageEntity = fileStorageMapper.selectById(fileEntity.getFileId());
        if (fileStorageEntity == null) {
            return null;
        }
        return FileModel.builder()
                .fileName(fileEntity.getFileName())
                .fileSize(fileStorageEntity.getFileSize())
                .objectName(fileStorageEntity.getObjectName())
                .bucketName(fileStorageEntity.getBucketName())
                .crc(fileStorageEntity.getCrc())
                .build();
    }

    @Override
    public FileModel getOneByCrc(String crc) {
        FileStorageEntity fileStorageEntity = fileStorageMapper.selectOne(
                Wrappers.<FileStorageEntity>lambdaQuery()
                        .eq(FileStorageEntity::getCrc, crc).last(" limit 1"));
        return FileModel.builder()
                .fileSize(fileStorageEntity.getFileSize())
                .objectName(fileStorageEntity.getObjectName())
                .bucketName(fileStorageEntity.getBucketName())
                .crc(fileStorageEntity.getCrc())
                .build();
    }
}
