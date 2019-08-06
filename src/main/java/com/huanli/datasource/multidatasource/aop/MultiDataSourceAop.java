package com.huanli.datasource.multidatasource.aop;


import com.huanli.datasource.multidatasource.DataSourceContextHolder;
import com.huanli.datasource.multidatasource.DynamicDataSource;
import com.huanli.datasource.multidatasource.annotion.MultiDataSource;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.core.Ordered;
import org.springframework.util.ReflectionUtils;
import org.springframework.util.StringUtils;

import java.lang.reflect.Method;

/**
 * 必须设置DEFAULT的数据源
 * 动态数据源切换
 * @since 2017年11月8日 上午9:20:28
 */
@Slf4j
@Aspect
public class MultiDataSourceAop implements Ordered {


    @Pointcut("@annotation(com.huanli.datasource.multidatasource.annotion.MultiDataSource)")
    private void cut() {
        log.trace("DS切面日志");

    }

    @Around("cut()")
    public Object around(ProceedingJoinPoint point) throws Throwable {

        //只使用最先设定的数据源
        String dsName = DataSourceContextHolder.getDataSourceType();
        if (StringUtils.isEmpty(dsName)) {
            Signature signature = point.getSignature();
            MethodSignature methodSignature = (MethodSignature) signature;

            Object target = point.getTarget();
            Method currentMethod = target.getClass().getMethod(methodSignature.getName(),
                    methodSignature.getParameterTypes());

            MultiDataSource datasource = currentMethod.getAnnotation(MultiDataSource.class);

            if (datasource == null) {
                Method interMethod = ReflectionUtils.findMethod(target.getClass().getInterfaces()[0],
                        currentMethod.getName()
                        , currentMethod.getParameterTypes()
                );
                datasource = interMethod.getAnnotation(MultiDataSource.class);
            }

            dsName = DynamicDataSource.DEFAULT;
            if (datasource != null) {
                dsName = StringUtils.isEmpty(datasource.value()) ? dsName : datasource.value();
            }
            log.info("设置method:{} 数据源为:{}", currentMethod.getName(), dsName);
            DataSourceContextHolder.setDataSourceType(dsName);
        } else {
            log.info("当前数据源为:{}", dsName);
        }
        try {
            return point.proceed();
        } finally {
            DataSourceContextHolder.clearDataSourceType();
            log.info("清除数据源:{}", dsName);

        }
    }

    /**
     * aop的顺序要早于spring的事务
     */
    @Override
    public int getOrder() {
        return 1;
    }

}
