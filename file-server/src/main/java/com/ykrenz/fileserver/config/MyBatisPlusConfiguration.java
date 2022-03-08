package com.ykrenz.fileserver.config;

import com.baomidou.mybatisplus.annotation.DbType;
import com.baomidou.mybatisplus.extension.plugins.MybatisPlusInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.OptimisticLockerInnerInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.PaginationInnerInterceptor;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author Mr Ren
 * @Description: mybatis plus配置
 * @date 2021/2/24 14:04
 */
@Configuration
@MapperScan(MyBatisPlusConfiguration.MapperScanPackage)
public class MyBatisPlusConfiguration {

    public static final String MapperScanPackage = "com.ykrenz.fileserver.mapper";

    @Bean
    public MybatisPlusInterceptor mybatisPlusInterceptor() {
        MybatisPlusInterceptor interceptor = new MybatisPlusInterceptor();
        interceptor.addInnerInterceptor(new OptimisticLockerInnerInterceptor());
        addPaginationInnerInterceptor(interceptor);
        return interceptor;
    }

    private void addPaginationInnerInterceptor(MybatisPlusInterceptor interceptor) {
        interceptor.addInnerInterceptor(new PaginationInnerInterceptor(DbType.MYSQL));
    }
}
