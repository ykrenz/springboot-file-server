package com.github.ren.file.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.ren.file.entity.TbFileInfo;
import com.github.ren.file.mapper.TbFileInfoMapper;
import com.github.ren.file.service.TbFileInfoService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * @author Mr Ren
 * @Description: TbFileInfoServiceImpl
 * @date 2021/4/8 9:59
 */
@Service("TbFileInfoServiceImpl")
@Slf4j
public class TbFileInfoServiceImpl extends ServiceImpl<TbFileInfoMapper, TbFileInfo> implements TbFileInfoService {

}

