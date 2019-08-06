package com.huanli.datasource.database;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.validation.annotation.Validated;

import javax.sql.DataSource;
import javax.validation.constraints.NotNull;
import java.io.IOException;
import java.util.*;

/**
 * @uther: lihuan
 * @Date: 2019-07-26 14:22
 * @Description:
 */
@Slf4j
@Setter
@Getter
@Validated
@ConfigurationProperties(prefix = DataBaseProperties.PREFIX)
public class DataBaseProperties implements InitializingBean {
    public static final String PREFIX = "spring";

    private static final ResourcePatternResolver resourceResolver = new PathMatchingResourcePatternResolver();


    private List<DataBaseItem> database = new ArrayList<>();


    @Override
    public void afterPropertiesSet() {
        log.info("DataBaseProperties... ");
    }


    @Getter
    @Setter
    public static class DataBaseItem {

        /**
         * 连接池组名
         */
        @NotNull
        private String name;


        /**
         * 全局事物
         */
        private boolean globalTransactional = false;
        /**
         * 组数据源:读写分离
         */
        private boolean groupDataSource = false;

        /**
         * 连接池类型
         */
        @NotNull
        private Class<? extends DataSource> dataSourceType;
        /**
         * mybatis 或者或者扩展
         * - tk.mybatis.spring.mapper.MapperScannerConfigurer
         * - org.mybatis.spring.mapper.MapperScannerConfigurer
         */
        @NotNull
        private Class<?> mapperScannerConfigurer;

        private MybatisProperties mybatis = new MybatisProperties();

        @NotNull
        private Map<String, Map<String, String>> datasource = new HashMap<>();


    }

    @Getter
    @Setter
    public static class MybatisProperties {
        private String[] basePackages;

        /**
         * Locations of MyBatis mapper files.
         */
        private String[] mapperLocations;

        /**
         * Packages to search type aliases. (Package delimiters are ",; \t\n")
         */
        private String typeAliasesPackage;

        public Resource[] resolveMapperLocations() {
            List<Resource> resources = new ArrayList<>();
            if (this.mapperLocations != null) {
                for (String mapperLocation : this.mapperLocations) {
                    resources.addAll(Arrays.asList(getResources(mapperLocation)));
                }
            }
            return resources.toArray(new Resource[resources.size()]);
        }

        private Resource[] getResources(String location) {
            try {
                return resourceResolver.getResources(location);
            } catch (IOException e) {
                log.error("load resources  fail ", e);
                return new Resource[0];
            }
        }

    }

}
