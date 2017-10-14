/**
 * hyberbin.com Inc.
 * Copyright (c) 2004-2017 All Rights Reserved.
 */
package org.jplus.quartz;

/**
 * @author hyberbin
 * @version $Id: QuartzManager.java, v 0.1 2017年10月07日 15:25 hyberbin Exp $
 */

import org.jplus.daily.DailyJob;
import org.jplus.database.DailyJobDao;
import org.jplus.robot.RobotUtils;
import org.jplus.robot.SpringContextUtil;
import org.quartz.JobKey;
import org.quartz.SchedulerFactory;

import org.quartz.CronTrigger;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.TriggerKey;
import org.quartz.impl.JobDetailImpl;
import org.quartz.impl.StdSchedulerFactory;
import org.quartz.impl.triggers.CronTriggerImpl;
import org.springframework.util.CollectionUtils;

import java.util.List;

/**
 * @author Comsys-LZP
 * @version V2.0
 * @Description: 定时任务管理类
 * @ClassName: QuartzManager
 * @Copyright: Copyright (c) 2014
 * @date 2014-6-26 下午03:15:52
 */
public class QuartzManager {
    private static SchedulerFactory gSchedulerFactory = new StdSchedulerFactory();

    /**
     * @param jobName 任务名
     * @param cls     任务
     * @param time    时间设置，参考quartz说明文档
     * @Description: 添加一个定时任务，使用默认的任务组名，触发器名，触发器组名
     * @Title: QuartzManager.java
     * @Copyright: Copyright (c) 2014
     * @author Comsys-LZP
     * @date 2014-6-26 下午03:47:44
     * @version V2.0
     */
    @SuppressWarnings("unchecked")
    public static void addJob(String jobName, Class cls, String time) {
        try {
            Scheduler sched = gSchedulerFactory.getScheduler();
            JobDetailImpl jobDetail = new JobDetailImpl();// 任务名，任务组，任务执行类
            jobDetail.setKey(new JobKey(jobName));
            jobDetail.setJobClass(cls);
            // 触发器
            CronTriggerImpl trigger = new CronTriggerImpl();// 触发器名,触发器组
            trigger.setKey(new TriggerKey(jobName));
            trigger.setCronExpression(time);// 触发器时间设定
            sched.scheduleJob(jobDetail, trigger);
            // 启动
            if (!sched.isShutdown()) {
                sched.start();
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }


    /**
     * @param jobName
     * @param time
     * @Description: 修改一个任务的触发时间(使用默认的任务组名，触发器名，触发器组名)
     * @Title: QuartzManager.java
     * @Copyright: Copyright (c) 2014
     * @author Comsys-LZP
     * @date 2014-6-26 下午03:49:21
     * @version V2.0
     */
    @SuppressWarnings("unchecked")
    public static void modifyJobTime(String jobName, String time) {
        try {
            Scheduler sched = gSchedulerFactory.getScheduler();
            CronTrigger trigger = (CronTriggerImpl) sched.getTrigger(new TriggerKey(jobName));
            if (trigger == null) {
                return;
            }
            String oldTime = trigger.getCronExpression();
            if (!oldTime.equalsIgnoreCase(time)) {
                JobDetail jobDetail = sched.getJobDetail(new JobKey(jobName));
                Class objJobClass = jobDetail.getJobClass();
                removeJob(jobName);
                addJob(jobName, objJobClass, time);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }


    /**
     * @param jobName
     * @Description: 移除一个任务(使用默认的任务组名，触发器名，触发器组名)
     * @Title: QuartzManager.java
     * @Copyright: Copyright (c) 2014
     * @author Comsys-LZP
     * @date 2014-6-26 下午03:49:51
     * @version V2.0
     */
    public static void removeJob(String jobName) {
        try {
            Scheduler sched = gSchedulerFactory.getScheduler();
            TriggerKey triggerKey = new TriggerKey(jobName);
            sched.pauseTrigger(triggerKey);// 停止触发器
            sched.unscheduleJob(triggerKey);// 移除触发器
            JobKey jobKey = new JobKey(jobName);
            sched.deleteJob(jobKey);// 删除任务
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }



    /**
     * @Description:启动所有定时任务
     * @Title: QuartzManager.java
     * @Copyright: Copyright (c) 2014
     * @author Comsys-LZP
     * @date 2014-6-26 下午03:50:18
     * @version V2.0
     */
    public static void startJobs() {
        try {
            Scheduler sched = gSchedulerFactory.getScheduler();
            sched.start();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * @Description:关闭所有定时任务
     * @Title: QuartzManager.java
     * @Copyright: Copyright (c) 2014
     * @author Comsys-LZP
     * @date 2014-6-26 下午03:50:26
     * @version V2.0
     */
    public static void shutdownJobs() {
        try {
            Scheduler sched = gSchedulerFactory.getScheduler();
            if (!sched.isShutdown()) {
                sched.shutdown();
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 从数据库初始化
     */
    public static void initFromDb(){
        DailyJobDao dailyJobDao = SpringContextUtil.getBean(DailyJobDao.class);
        List<DailyJob> jobs = dailyJobDao.getJobs();
        if (!CollectionUtils.isEmpty(jobs)) {
            for (DailyJob dailyJob : jobs) {
                try {
                    QuartzManager.addJob(dailyJob.getName(), Class.forName(dailyJob.getClazz()), dailyJob.getTime());
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }
            }
            RobotUtils.sendMsg("已启动定时任务" + jobs.size() + "个");
        }
    }
}
