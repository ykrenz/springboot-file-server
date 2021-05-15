package com.github.ren.file.sdk.model;

import com.github.ren.file.sdk.Util;
import lombok.*;

/**
 * @Description
 * @Author ren
 * @Since 1.0
 */
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@AllArgsConstructor
public class FdfsUploadResult extends UploadGenericResult {

    private String group;

    private String path;

    @Override
    public String getObjectName() {
        return group + Util.SLASH + path;
    }

    public String getGroup() {
        return group;
    }

    public void setGroup(String group) {
        this.group = group;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }
}
