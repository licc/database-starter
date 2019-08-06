package com.huanli.datasource.multidatasource.annotion;

import java.lang.annotation.*;

/**
 * 多数据源标识
 * @since 2017年11月8日 上午9:20:28
 */
@Inherited
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD})
public @interface MultiDataSource {


    /**
     * @return 数据源标示
     */
    String value() default "";
}
