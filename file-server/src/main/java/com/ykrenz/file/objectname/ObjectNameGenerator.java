package com.ykrenz.file.objectname;

import java.io.Serializable;

/**
 * @Description 文件名称生成
 * @Author ren
 * @Since 1.0
 */
public interface ObjectNameGenerator extends Serializable {
    /**
     * 创建文件名称
     *
     * @return
     */
    String generator();
}
