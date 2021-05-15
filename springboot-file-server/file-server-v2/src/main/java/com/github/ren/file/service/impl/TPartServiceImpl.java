package com.github.ren.file.service.impl;

import com.github.ren.file.entity.TPart;
import com.github.ren.file.mapper.TPartMapper;
import com.github.ren.file.service.ITPartService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 文件分片信息表 服务实现类
 * </p>
 *
 * @author Mr Ren
 * @since 2021-05-14
 */
@Service("TPartServiceImpl")
public class TPartServiceImpl extends ServiceImpl<TPartMapper, TPart> implements ITPartService {

}
