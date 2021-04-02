//package com.github.ren.file.service;
//
//import java.util.concurrent.locks.ReentrantLock;
//
///**
// * @author RenYinKui
// * @Description:
// * @date 2021/4/1 21:20
// */
//public class LocalLock implements FileLock {
//
//    private ReentrantLock reentrantLock = new ReentrantLock();
//
//
//    private int status;
//
//    @Override
//    public void lock(String key) {
//    }
//
//    @Override
//    public void unlock(String key) {
//
//    }
//
//    @Override
//    public int getStatus() {
//        return status;
//    }
//
//    @Override
//    public void setStatus(int status) {
//        this.status = status;
//    }
//}
