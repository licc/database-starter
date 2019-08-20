package com.huanli.datasource.multidatasource;

/**
 * @Author: lihuan
 * datasource的上下文
 *
 * @since 2017年11月8日 上午9:10:58
 */
public class DataSourceContextHolder {

    private static final ThreadLocal<String> contextHolder = new ThreadLocal<>();

    private DataSourceContextHolder() {

    }

    /**
     * 设置数据源类型
     *
     * @param dataSourceType 数据库类型
     */
    public static void setDataSourceType(String dataSourceType) {
        contextHolder.set(dataSourceType);
    }


    /**
     * 获取数据源类型
     *
     * @return 返回到数据源标示
     */
    public static String getDataSourceType() {
        return contextHolder.get();
    }

    /**
     * 清除数据源类型
     */
    public static void clearDataSourceType() {
        contextHolder.remove();
    }
}
