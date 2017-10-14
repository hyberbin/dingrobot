/**
 * hyberbin.com Inc.
 * Copyright (c) 2004-2017 All Rights Reserved.
 */
package test;

import org.jplus.database.DailyManDao;
import org.jplus.robot.ELUtils;
import org.jplus.robot.RobotController;
import org.jplus.robot.SpringContextUtil;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * @author hyberbin
 * @version $Id: RobotTest.java, v 0.1 2017年10月07日 17:14 hyberbin Exp $
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {SpringContextUtil.class, RobotController.class})
@FixMethodOrder(value = MethodSorters.NAME_ASCENDING)
public class RobotTest {

    @Test
    public void test(){
        DailyManDao bean = SpringContextUtil.getBean(DailyManDao.class);
        System.out.println(bean);
        Object spelValue = ELUtils.getSpelValue("#dailyManDao.addDailyMan('test')");
        System.out.println(spelValue);
    }
}