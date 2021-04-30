package com.github.ren.file.sdk.part;

/**
 * @Description redis 取消状态 适用于集群环境
 * @Author ren
 * @Since 1.0
 */
public class RedisCancel implements PartCancel {

    @Override
    public void setCancel(String uploadId) {

    }

    @Override
    public boolean needCancel(String uploadId) {
        return false;
    }

    @Override
    public void cancelComplete(String uploadId) {

    }

    @Override
    public boolean cancelSuccess(String uploadId) {
        return false;
    }
}
