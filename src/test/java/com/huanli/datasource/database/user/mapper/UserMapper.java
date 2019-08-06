package com.huanli.datasource.database.user.mapper;


import com.huanli.datasource.multidatasource.annotion.MultiDataSource;
import com.huanli.datasource.database.user.entity.UserDO;
import org.apache.ibatis.annotations.Param;
import tk.mybatis.mapper.common.Mapper;

public interface UserMapper extends Mapper<UserDO>{

    @MultiDataSource("read")
    UserDO findByMyId(@Param("id") Long id);
}