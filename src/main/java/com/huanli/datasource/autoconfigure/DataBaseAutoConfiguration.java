package com.huanli.datasource.autoconfigure;

import com.huanli.datasource.database.DataBaseProperties;
import com.huanli.datasource.database.DataBaseRegistryProcessor;
import com.huanli.datasource.multidatasource.aop.MultiDataSourceAop;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @Author: lihuan
 * @Date: 2019-08-02 16:54
 * @Description:
 */

@Configuration
@EnableConfigurationProperties(DataBaseProperties.class)
public class DataBaseAutoConfiguration implements InitializingBean {


    @Bean
    public MultiDataSourceAop multiDataSourceAop() {

        return new MultiDataSourceAop();
    }

    @Bean
    public DataBaseRegistryProcessor dataBaseRegistryInit() {

        return new DataBaseRegistryProcessor();
    }

    @Override
    public void afterPropertiesSet() throws Exception {


    }
}
