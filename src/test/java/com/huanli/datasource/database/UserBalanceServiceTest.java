package com.huanli.datasource.database;

import com.huanli.datasource.ApplicationTest;
import com.huanli.datasource.database.service.UserBalanceService;
import com.huanli.datasource.database.service.UserInfoService;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;

/**
 * @uther: lihuan
 * @Date: 2019-07-31 20:28
 * @Description:
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = ApplicationTest.class)
public class UserBalanceServiceTest {

    @Autowired
    UserBalanceService userBalanceService;

    @Autowired
    UserInfoService userInfoService;

    @Test
    public void userBalanceServiceTest() {

        Assert.assertNotNull(userBalanceService.findModelById(12l));


    }

    @Test
    public void userInfoServiceTest() {

        Assert.assertNull(userInfoService.findModelById(12l));


    }
}
