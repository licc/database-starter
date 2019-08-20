package com.huanli.datasource;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.PropertySource;

/**
 * @Author: lihuan
 * @Date: 2019-07-30 16:18
 * @Description:
 */
@PropertySource(value = {"classpath:application-h2-local.properties"})    //可以引用自定义名字的配置文件
@SpringBootApplication
public class ApplicationTest {


    public static void main(String[] args) {

        SpringApplication.run(ApplicationTest.class, args);
    }


}
