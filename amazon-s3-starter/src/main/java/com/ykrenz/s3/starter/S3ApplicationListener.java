package com.ykrenz.s3.starter;

import com.amazonaws.services.s3.AmazonS3;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextClosedEvent;

import java.util.Map;

public class S3ApplicationListener implements ApplicationListener<ContextClosedEvent> {

    private static final Logger log = LoggerFactory.getLogger(S3ApplicationListener.class);

    @Override
    public void onApplicationEvent(ContextClosedEvent event) {
        Map<String, AmazonS3> AmazonS3ClientMap = event.getApplicationContext()
                .getBeansOfType(AmazonS3.class);
        log.info("{} AmazonS3Clients will be shutdown soon", AmazonS3ClientMap.size());
        AmazonS3ClientMap.keySet().forEach(beanName -> {
            log.info("shutdown AmazonS3Client: {}", beanName);
            AmazonS3ClientMap.get(beanName).shutdown();
        });
    }

}