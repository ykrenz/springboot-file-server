package com.github.ren.file.client.objectname;

import lombok.Data;

/**
 * @Description 根据md5文件名称生成
 * @Author ren
 * @Since 1.0
 */
@Data
public class Md5Generator implements ObjectNameGenerator {

    private String md5;

    private String ext;

    public Md5Generator(String md5, String ext) {
        this.md5 = md5;
        this.ext = ext;
    }

    @Override
    public String generator() {
        //https://help.aliyun.com/document_detail/64945.html?spm=a2c4g.11186623.6.1779.75e151b6GRk0Ab
        return md5.substring(0, 4) + "/" + md5 + "." + ext;
    }
}
