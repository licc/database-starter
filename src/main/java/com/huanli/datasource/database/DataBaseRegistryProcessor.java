package com.huanli.datasource.database;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.BeanDefinitionRegistryPostProcessor;
import org.springframework.boot.context.properties.bind.Binder;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.EnvironmentAware;
import org.springframework.core.env.Environment;
import org.springframework.util.StringUtils;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;

/**
 * @uther: lihuan
 * @Date: 2019-07-31 16:44
 * @Description:
 */
@Slf4j
public class DataBaseRegistryProcessor implements
        BeanDefinitionRegistryPostProcessor,
        ApplicationContextAware, EnvironmentAware {

    private ApplicationContext ctx;


    private Environment environment;


    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {

        ctx = applicationContext;
    }

    @Override
    public void postProcessBeanDefinitionRegistry(BeanDefinitionRegistry registry) throws BeansException {


        DataBaseProperties dataBaseProperties = Binder.get(environment).bind(DataBaseProperties.PREFIX,
                DataBaseProperties.class).get();


        dataBaseProperties.getDatabase().stream().forEach(item -> {

            log.info("start create database[{}]...", item.getName());
            //registry datasource


            Map<Object, Object> dataSourceMap = new HashMap<>();

            for (Map.Entry<String, Map<String, String>> k : item.getDatasource().entrySet()) {

                String dsName =
                        item.getName() + StringUtils.capitalize(k.getKey()) + StringUtils.capitalize(DataBaseUtils.DATA_SOURCE);

                BeanDefinitionBuilder ds = DataBaseUtils.createDataSource(item.getDataSourceType(), k.getValue());

                registry.registerBeanDefinition(dsName, ds.getBeanDefinition());

                dataSourceMap.put(k.getKey(), ctx.getBean(dsName));
                log.info("registry {} bean ", dsName);

            }

            DataSource groupDataSource;
            //datasource
            // Routing Multi DataSource
            if (item.isGroupDataSource() || item.getDatasource().size() > 1) {
                String dsGroupName = item.getName() + "Group" + StringUtils.capitalize(DataBaseUtils.DATA_SOURCE);
                BeanDefinitionBuilder ds = DataBaseUtils.createDynamicDataSource(dataSourceMap);
                registry.registerBeanDefinition(dsGroupName, ds.getBeanDefinition());
                groupDataSource = (DataSource) ctx.getBean(dsGroupName);
            } else {
                groupDataSource = (DataSource) dataSourceMap.entrySet().stream().findFirst().get();
            }


            DataSource dataSource;
            //Gtx
            if (item.isGlobalTransactional()) {
                String dsGtxName = item.getName() + "Gtx" + StringUtils.capitalize(DataBaseUtils.DATA_SOURCE);
                BeanDefinitionBuilder ds = DataBaseUtils.createGtxDataSourceProxy(groupDataSource);
                registry.registerBeanDefinition(dsGtxName, ds.getBeanDefinition());
                dataSource = (DataSource) ctx.getBean(dsGtxName);

            } else {
                dataSource = groupDataSource;
            }


            //registry sessionFactory

            String sfName = item.getName() + StringUtils.capitalize(DataBaseUtils.SQL_SESSION_FACTORY);
            BeanDefinitionBuilder sqlSessionFactory = DataBaseUtils.createSqlSessionFactory(dataSource, item);
            registry.registerBeanDefinition(sfName, sqlSessionFactory.getBeanDefinition());
            log.info("registry {} bean ", sfName);


            // registry mapperscan

            String scName = item.getName() + StringUtils.capitalize(DataBaseUtils.MAPPER_SCANNER_CONFIGURER);
            BeanDefinitionBuilder scanner = DataBaseUtils.createMapperScannerConfigurer(item);
            registry.registerBeanDefinition(scName, scanner.getBeanDefinition());
            log.info("registry {} bean ", scName);

            // registry TX
            String txName = item.getName() + StringUtils.capitalize(DataBaseUtils.TRANSACTION_MANAGER);
            BeanDefinitionBuilder tm = DataBaseUtils.createAnnotationDrivenTransactionManager(dataSource);
            registry.registerBeanDefinition(txName, tm.getBeanDefinition());
            log.info("registry {} bean ", txName);

            log.info("end create database[{}]...", item.getName());


        });

    }


    @Override
    public void setEnvironment(Environment environment) {
        this.environment = environment;
    }

    @Override
    public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {

    }


}

