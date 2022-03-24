package com.ykrenz.fileserver.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ykrenz.fileserver.entity.FilePartInfo;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * <p>
 * 文件信息主表 Mapper 接口
 * </p>
 *
 * @author Mr Ren
 * @since 2021-05-24
 */
public interface FilePartInfoMapper extends BaseMapper<FilePartInfo> {

    @Select({
            "select * from table where now() > date_add(createTime,interval #{expireDays} day) " +
                    "and partNumber = 0 and status = 1 limit 500"
    })
    List<FilePartInfo> selectExpireUploads(int expireDays);
}
