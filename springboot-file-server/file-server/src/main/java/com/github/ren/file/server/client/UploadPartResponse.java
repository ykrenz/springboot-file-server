package com.github.ren.file.server.client;

import lombok.Data;

import java.io.Serializable;

/**
 * @author RenYinKui
 * @Description:
 * @date 2021/7/8 11:59
 */
@Data
public class UploadPartResponse implements Serializable {
    private Integer partNumber;
    private Long partSize;
    private String eTag;
}
