package com.github.ren.file.server.service.impl;

import com.github.ren.file.server.entity.TPart;
import com.github.ren.file.server.mapper.TPartMapper;
import com.github.ren.file.server.service.ITPartService;
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
@Service
public class TPartServiceImpl extends ServiceImpl<TPartMapper, TPart> implements ITPartService {

}
