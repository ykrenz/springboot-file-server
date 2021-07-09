package com.github.ren.file.server.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.ren.file.server.client.PartResult;
import com.github.ren.file.server.config.FileUploadProperties;
import com.github.ren.file.server.entity.TUpload;
import com.github.ren.file.server.mapper.TUploadMapper;
import com.github.ren.file.server.service.ITUploadService;
import com.github.ren.file.server.service.RedisService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Set;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author Mr Ren
 * @since 2021-06-17
 */
@Service
public class TUploadServiceImpl extends ServiceImpl<TUploadMapper, TUpload> implements ITUploadService {

    @Autowired
    private FileUploadProperties fileUploadProperties;

    @Autowired
    private RedisService redisService;

    private final int storage;

    /**
     * 缓存60s
     */
    private static final int expire = 60;

    public TUploadServiceImpl(FileUploadProperties fileUploadProperties) {
        this.fileUploadProperties = fileUploadProperties;
        this.storage = fileUploadProperties.getStorageTypeInt();
    }

    @Override
    public TUpload selectUpload(String uploadId) {
        TUpload tUpload = (TUpload) redisService.get(uploadId + "-upload");
        if (tUpload != null) {
            return tUpload;
        }
        tUpload = this.getBaseMapper().selectOne(Wrappers.<TUpload>lambdaQuery()
                .eq(TUpload::getId, uploadId)
                .eq(TUpload::getStorage, storage)
                .eq(TUpload::getStatus, 0)
        );
        redisService.set(uploadId + "-upload", tUpload, expire);
        return tUpload;
    }

    @Override
    public boolean save(TUpload entity) {
        boolean save = super.save(entity);
        String uploadId = entity.getUploadId();
        redisService.set(uploadId + "-upload", entity, expire);
        return save;
    }

    @Override
    public boolean completeUpload(String uploadId) {
        TUpload tUpload = selectUpload(uploadId);
        if (tUpload == null) {
            return false;
        }
        tUpload.setStatus(1);
        boolean update = super.updateById(tUpload);
        redisService.delete(uploadId);
        return update;
    }

    @Override
    public boolean abortUpload(String uploadId) {
        TUpload tUpload = selectUpload(uploadId);
        if (tUpload == null) {
            return false;
        }
        tUpload.setStatus(-1);
        boolean update = super.updateById(tUpload);
        redisService.delete(uploadId);
        return update;
    }

    @Override
    public void saveMultipart(TUpload tUpload, PartResult partResult) {
        Long id = tUpload.getId();
        redisService.sSet(id + "-Multipart", partResult);
        redisService.expire(id + "-Multipart", expire);
    }

    @Override
    public List<PartResult> listMultipart(TUpload tUpload) {
        List<PartResult> list = new ArrayList<>();
        Set<Object> objects = redisService.sGet(tUpload.getId() + "-Multipart");
        for (Object object : objects) {
            list.add((PartResult) object);
        }
        list.sort(Comparator.comparing(PartResult::getPartNumber));
        return list;
    }
}
