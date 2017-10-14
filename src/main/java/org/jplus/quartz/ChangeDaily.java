/**
 * hyberbin.com Inc.
 * Copyright (c) 2004-2017 All Rights Reserved.
 */
package org.jplus.quartz;

import org.jplus.database.DailyManDao;
import org.jplus.robot.SpringContextUtil;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

/**
 * @author hyberbin
 * @version $Id: ChangeDaily.java, v 0.1 2017年10月11日 20:18 hyberbin Exp $
 */
public class ChangeDaily implements Job {
    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        DailyManDao dailyManDao= SpringContextUtil.getBean(DailyManDao.class);
        dailyManDao.setNextDaily();
    }
}