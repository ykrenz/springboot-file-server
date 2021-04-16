package com.github.ren.file.clients;

import com.github.ren.file.properties.QiNiuProperties;

import java.io.File;
import java.io.InputStream;
import java.util.List;

/**
 * @author Mr Ren
 * @Description: 七牛云客户端
 * @date 2021/4/2 16:01
 */
public class QiNiuFileClient extends AbstractServerClient implements QiNiuClient {

    private final QiNiuProperties qiNiuProperties;

    public QiNiuFileClient(QiNiuProperties qiNiuProperties) {
        this.qiNiuProperties = qiNiuProperties;
    }

    @Override
    public String getWebServerUrl() {
        return null;
    }

    @Override
    public boolean isExist(String objectName) {
        return false;
    }

    @Override
    public String uploadFile(File file, String yourObjectName) {
        return null;
    }

    @Override
    public String uploadFile(InputStream is, String yourObjectName) {
        return null;
    }

    @Override
    public String uploadFile(byte[] content, String yourObjectName) {
        return null;
    }

    @Override
    public String uploadPart(List<UploadPart> parts, String yourObjectName) {
        return null;
    }

}
