/**
 * hyberbin.com Inc.
 * Copyright (c) 2004-2017 All Rights Reserved.
 */
package org.jplus.daily;

import lombok.Data;
import org.jplus.database.DailyManDao;
import org.jplus.robot.SpringContextUtil;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

/**
 * @author hyberbin
 * @version $Id: DailyJob.java, v 0.1 2017年10月07日 15:39 hyberbin Exp $
 */
@Data
public class DailyJob  {
    private Integer id;
    private String name;
    private String time;
    private String memo;
    private String clazz;
}