package com.huanli.datasource.database;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * @uther: lihuan
 * @Date: 2019-07-29 13:52
 * @Description:
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = {ConfigurationTest.TestConfiguration.class} )
public class ConfigurationTest {
    static {
        System.setProperty("spring.config.location", "classpath:application-h2.properties");
    }
    @Autowired
    DataBaseProperties myOrmProperties;
    @Autowired
    Environment environment;



    @Test
    public void testOrmConfiguration() {

        System.out.println(myOrmProperties.getDatabase().get(0).getName());

        String  ddd="333";

    }




    @EnableConfigurationProperties(DataBaseProperties.class)
    public static class TestConfiguration {
        // nothing
    }
}
