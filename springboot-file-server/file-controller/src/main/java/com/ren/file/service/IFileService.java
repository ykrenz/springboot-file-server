package com.ren.file.service;

import com.ren.file.pojo.request.Chunk;
import com.ren.file.pojo.response.MergeRes;
import com.ren.file.util.R;

/**
 * @author RenYinKui
 * @Description:
 * @date 2020/6/2 15:43
 */
public interface IFileService {

    /**
     * 上传分片
     */
    String uploadChunk(Chunk chunk);

    /**
     * 秒传和续传
     */
    MergeRes checkChunk(Chunk chunk);

    /**
     * 合并分片
     */
    R<String> mergeChunk(String identifier, String filename);

}
