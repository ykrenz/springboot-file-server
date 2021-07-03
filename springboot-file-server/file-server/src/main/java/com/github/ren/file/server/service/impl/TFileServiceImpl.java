package com.github.ren.file.server.service.impl;

import com.github.ren.file.server.entity.TFile;
import com.github.ren.file.server.mapper.TFileMapper;
import com.github.ren.file.server.service.ITFileService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 文件信息主表 服务实现类
 * </p>
 *
 * @author Mr Ren
 * @since 2021-05-24
 */
@Service
public class TFileServiceImpl extends ServiceImpl<TFileMapper, TFile> implements ITFileService {

}
