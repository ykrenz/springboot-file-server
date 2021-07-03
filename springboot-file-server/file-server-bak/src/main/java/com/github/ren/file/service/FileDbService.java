package com.github.ren.file.service;

import com.github.ren.file.entity.TbFile;
import com.github.ren.file.entity.TbFileInfo;

/**
 * @author Mr Ren
 * @Description: 数据库操作接口
 * @date 2021/4/16 14:31
 */
public interface FileDbService {
    /**
     * 保存文件
     *
     * @return
     */
    TbFile saveFile(String md5, String path, long size);

    /**
     * 删除文件
     *
     * @param md5
     * @return
     */
    int deleteTbFile(String md5);

    /**
     * 获取文件信息
     *
     * @param id
     * @return
     */
    TbFile getTbFile(String id);

    /**
     * 保存文件信息
     *
     * @return
     */
    TbFileInfo saveInfo(String md5, String filename);
}
