package com.ykrenz.file.dao;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.ykrenz.file.dao.mapper.entity.FileStorageEntity;
import com.ykrenz.file.dao.mapper.FileStorageMapper;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

@Repository
public class MapperDao implements FileDao {

    @Resource
    private FileStorageMapper fileStorageMapper;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public String save(FileModel fileModel) {
        FileStorageEntity fileStorageEntity = new FileStorageEntity();
        fileStorageEntity.setFileName(fileModel.getFileName());
        fileStorageEntity.setBucketName(fileModel.getBucketName());
        fileStorageEntity.setObjectName(fileModel.getObjectName());
        fileStorageEntity.setFileSize(fileModel.getFileSize());
        fileStorageEntity.setHash(fileModel.getHash());
        fileStorageEntity.setStatus(1);
        fileStorageMapper.insert(fileStorageEntity);
        return fileStorageEntity.getId();
    }

    @Override
    public FileModel getById(String id) {
        FileStorageEntity fileStorageEntity = fileStorageMapper.selectById(id);
        if (fileStorageEntity == null) {
            return null;
        }
        return FileModel.builder()
                .fileName(fileStorageEntity.getFileName())
                .fileSize(fileStorageEntity.getFileSize())
                .objectName(fileStorageEntity.getObjectName())
                .bucketName(fileStorageEntity.getBucketName())
                .hash(fileStorageEntity.getHash())
                .build();
    }

    @Override
    public FileModel getOneByHash(String hash) {
        FileStorageEntity fileStorageEntity = fileStorageMapper.selectOne(
                Wrappers.<FileStorageEntity>lambdaQuery()
                        .eq(FileStorageEntity::getHash, hash)
                        .last(" limit 1"));
        return FileModel.builder()
                .fileSize(fileStorageEntity.getFileSize())
                .objectName(fileStorageEntity.getObjectName())
                .bucketName(fileStorageEntity.getBucketName())
                .hash(fileStorageEntity.getHash())
                .build();
    }

}
