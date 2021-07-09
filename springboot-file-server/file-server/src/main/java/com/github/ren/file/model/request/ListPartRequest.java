package com.github.ren.file.model.request;

import io.swagger.annotations.ApiModel;
import lombok.Data;

/**
 * @Description 分片上传任务列表参数
 * @Author ren
 * @Since 1.0
 */
@Data
@ApiModel("分片上传任务列表参数")
public class ListPartRequest {
    private int page = 1;

    private int pageSize = 10;


}
