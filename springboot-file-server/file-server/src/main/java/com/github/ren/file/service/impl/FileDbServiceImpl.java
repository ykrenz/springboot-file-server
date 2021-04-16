package com.github.ren.file.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.github.ren.file.entity.TbFile;
import com.github.ren.file.entity.TbFileInfo;
import com.github.ren.file.service.FileDbService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author Mr Ren
 * @Description: 逻辑类
 * @date 2021/4/16 14:31
 */
@Service
@Slf4j
public class FileDbServiceImpl implements FileDbService {

    @Autowired
    @Qualifier("TbFileServiceImpl")
    private TbFileServiceImpl tbFileService;

    @Autowired
    @Qualifier("TbFileInfoServiceImpl")
    private TbFileInfoServiceImpl tbFileInfoService;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public TbFile saveFile(String md5, String path, long size) {
        TbFile tbFile = new TbFile();
        tbFile.setMd5(md5);
        tbFile.setPath(path);
        tbFile.setFilesize(size);
        tbFileService.save(tbFile);
        return tbFile;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int deleteTbFile(String md5) {
        return tbFileService.getBaseMapper().delete(Wrappers.<TbFile>lambdaQuery().eq(TbFile::getMd5, md5));
    }

    @Override
    public TbFile getTbFile(String id) {
        return tbFileService.getBaseMapper()
                .selectOne(Wrappers.<TbFile>lambdaQuery()
                        .eq(TbFile::getMd5, id));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public TbFileInfo saveInfo(String md5, String filename) {
        TbFileInfo tbFileInfo = new TbFileInfo();
        tbFileInfo.setFilename(filename);
        tbFileInfo.setMd5(md5);
        tbFileInfoService.save(tbFileInfo);
        return tbFileInfo;
    }
}
