package com.ren.file.service;

import com.ren.file.dto.request.Chunk;
import com.ren.file.dto.request.MergeChunkDto;
import com.ren.file.dto.response.ChunkRes;
import com.ren.file.dto.response.MergeRes;
import com.ren.file.entity.Fileinfo;
import com.ren.file.util.R;
import org.springframework.web.multipart.MultipartFile;

/**
 * @author RenYinKui
 * @Description:
 * @date 2020/6/2 15:43
 */
public interface IFileService {

    /**
     * 秒传和续传
     * @return
     */
    R<MergeRes> checkChunk(Chunk chunk);

    /**
     * 上传分片
     */
    R<ChunkRes> uploadChunk(Chunk chunk, MultipartFile file);

    /**
     * 合并分片
     */
    R<Fileinfo> mergeChunk(MergeChunkDto mergeChunkDto);

}
