package com.github.ren.file.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * @Description: 枚举类
 * @date 2020/5/22 14:40
 */
@Getter
@AllArgsConstructor
@NoArgsConstructor
public enum SuccessCode {

    /**
     * 上传失败
     */
    UPLOAD_ERROR(200, "上传成功"),
    ;

    /**
     * 返回码
     */
    private int code;
    /**
     * 返回消息
     */
    private String message;
}
