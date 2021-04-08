package com.github.ren.file.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.ren.file.entity.TbFile;
import com.github.ren.file.mapper.TbFileMapper;
import com.github.ren.file.service.TbFileService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * @author Mr Ren
 * @Description: TbFileServiceImpl
 * @date 2021/4/8 9:59
 */
@Service("TbFileServiceImpl")
@Slf4j
public class TbFileServiceImpl extends ServiceImpl<TbFileMapper, TbFile> implements TbFileService {
}

