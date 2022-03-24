package com.ykrenz.fileserver.service.client;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;

public class FastDfsClearApplicationListener implements ApplicationListener<ContextRefreshedEvent> {

    private static final Logger log = LoggerFactory.getLogger(FastDfsClearApplicationListener.class);
    private final FastDfsServerClient client;

    public FastDfsClearApplicationListener(FastDfsServerClient client) {
        this.client = client;
    }

    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        log.info("{} init fastdfs clear part task", client.getClass().getSimpleName());
        client.initClearTask(7);
    }

}