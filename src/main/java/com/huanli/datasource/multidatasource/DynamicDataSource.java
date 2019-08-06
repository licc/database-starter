package com.huanli.datasource.multidatasource;


import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;

/**
 * 动态数据源
 *
 * @date 2017年11月8日 上午9:10:58
 */
public class DynamicDataSource extends AbstractRoutingDataSource {
    /**
     * 数据源默认库
     */
    public static final String DEFAULT = "default";

    @Override
    protected Object determineCurrentLookupKey() {
        return DataSourceContextHolder.getDataSourceType();
    }

}
