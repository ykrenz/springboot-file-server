package com.github.ren.file.sdk.part;

import com.github.ren.file.sdk.lock.FileLock;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * @author RenYinKui
 * @Description:
 * @date 2021/5/18 9:43
 */
@Data
public class AppendFile implements FileLock{

    private List<PartInfo> partInfos = new ArrayList<>();

    private String filePath;

    public AppendFile(String filePath) {
        this.filePath = filePath;
    }

    @Override
    public void lock(String key) {

    }

    @Override
    public void unlock(String key) {

    }
}
