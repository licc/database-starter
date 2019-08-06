package com.huanli.datasource.database.service;

import com.huanli.datasource.multidatasource.annotion.MultiDataSource;
import com.huanli.datasource.database.kernel.entity.UserBalanceDO;
import com.huanli.datasource.database.kernel.mapper.UserBalanceMapper;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @uther: lihuan
 * @Date: 2019-07-18 23:27
 * @Description:
 */
@Service
public class UserBalanceService {
    @Resource
    private UserBalanceMapper userBalanceDOMapper;

    @MultiDataSource("read")
    public UserBalanceDO findModelById(Long id) {

        return userBalanceDOMapper.selectByPrimaryKey(id);

    }
}
