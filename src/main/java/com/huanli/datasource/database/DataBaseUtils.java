package com.huanli.datasource.database;

import com.huanli.datasource.multidatasource.DynamicDataSource;
import io.seata.rm.datasource.DataSourceProxy;
import lombok.extern.slf4j.Slf4j;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.core.io.Resource;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.util.ReflectionUtils;
import org.springframework.util.StringUtils;

import javax.sql.DataSource;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @uther: lihuan
 * @Date: 2019-07-31 16:46
 * @Description:
 */
@Slf4j
public class DataBaseUtils {
    public static final String DATA_SOURCE = "dataSource";
    public static final String SQL_SESSION_FACTORY = "sqlSessionFactory";
    public static final String MAPPER_SCANNER_CONFIGURER = "mapperScannerConfigurer";
    public static final String TRANSACTION_MANAGER = "transactionManager";


    /**
     * 创建基本数据源
     *
     * @param dataSourceType
     * @param properties
     * @return
     */
    public static BeanDefinitionBuilder createDataSource(Class<? extends DataSource> dataSourceType,
                                                         Map<String, String> properties) {

        BeanDefinitionBuilder beanDefinitionBuilder = BeanDefinitionBuilder
                .genericBeanDefinition(dataSourceType);
        Method[] methods = ReflectionUtils.getUniqueDeclaredMethods(dataSourceType);
        Set<String> pros =
                Stream.of(methods).filter(method -> method.getName().startsWith("set")).map(m -> m.getName().substring(3).toLowerCase())
                        .collect(Collectors.toSet());

        for (Map.Entry<String, String> p : properties.entrySet()) {
            if (pros.contains(p.getKey().toLowerCase())) {
                beanDefinitionBuilder.addPropertyValue(p.getKey(), p.getValue());
            }
        }
        return beanDefinitionBuilder;
    }


    /**
     * 创建mybatis sqlsession
     *
     * @param dataSource
     * @param dataBaseItem
     * @return
     */
    public static BeanDefinitionBuilder createSqlSessionFactory(DataSource dataSource,
                                                                DataBaseProperties.DataBaseItem dataBaseItem) {
        BeanDefinitionBuilder beanDefinitionBuilder = BeanDefinitionBuilder
                .genericBeanDefinition(SqlSessionFactoryBean.class);

        beanDefinitionBuilder.addPropertyValue("dataSource", dataSource);
        beanDefinitionBuilder.addPropertyValue("typeAliasesPackage",
                dataBaseItem.getMybatis().getTypeAliasesPackage());

        Resource[] rs = dataBaseItem.getMybatis().resolveMapperLocations();

        log.info("{} get Mapper Resources size:{}", dataBaseItem.getName(), rs.length);

        beanDefinitionBuilder.addPropertyValue("mapperLocations", rs);
        return beanDefinitionBuilder;
    }


    /**
     * 创建mapper扫描
     *
     * @param dataBaseItem
     * @return
     */
    public static BeanDefinitionBuilder createMapperScannerConfigurer(DataBaseProperties.DataBaseItem
                                                                              dataBaseItem) {

        BeanDefinitionBuilder beanDefinitionBuilder = BeanDefinitionBuilder
                .genericBeanDefinition(dataBaseItem.getMapperScannerConfigurer());
        beanDefinitionBuilder.addPropertyValue("sqlSessionFactoryBeanName", dataBaseItem.getName() +
                "SqlSessionFactory");
        List<String> list =
                Arrays.asList(StringUtils.commaDelimitedListToStringArray(dataBaseItem.getMybatis().getBasePackages()));
        String basePackage = StringUtils.collectionToCommaDelimitedString(list);
        beanDefinitionBuilder.addPropertyValue("basePackage", basePackage);
        return beanDefinitionBuilder;
    }


    /**
     * 创建本地事物
     *
     * @param dataSource
     * @return
     */
    public static BeanDefinitionBuilder createAnnotationDrivenTransactionManager(DataSource dataSource) {
        BeanDefinitionBuilder beanDefinitionBuilder = BeanDefinitionBuilder
                .genericBeanDefinition(DataSourceTransactionManager.class);
        beanDefinitionBuilder.addConstructorArgValue(dataSource);

        return beanDefinitionBuilder;
    }

    /**
     * 创建动态数据源
     *
     * @param dataSourceMap
     * @return
     */
    public static BeanDefinitionBuilder createDynamicDataSource(Map<Object, Object> dataSourceMap) {
        BeanDefinitionBuilder beanDefinitionBuilder = BeanDefinitionBuilder
                .genericBeanDefinition(DynamicDataSource.class);
        beanDefinitionBuilder.addPropertyValue("defaultTargetDataSource", dataSourceMap.get(DynamicDataSource.DEFAULT));
        beanDefinitionBuilder.addPropertyValue("targetDataSources", dataSourceMap);
        return beanDefinitionBuilder;
    }


    /**
     * 创建seata分布式事物数据源代理
     *
     * @param dataSource
     * @return
     */
    public static BeanDefinitionBuilder createGtxDataSourceProxy(DataSource dataSource) {
        BeanDefinitionBuilder beanDefinitionBuilder = BeanDefinitionBuilder
                .genericBeanDefinition(DataSourceProxy.class);
        beanDefinitionBuilder.addConstructorArgValue(dataSource);
        return beanDefinitionBuilder;
    }


}
