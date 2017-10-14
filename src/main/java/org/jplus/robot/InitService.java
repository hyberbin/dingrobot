/**
 * hyberbin.com Inc.
 * Copyright (c) 2004-2017 All Rights Reserved.
 */
package org.jplus.robot;

import org.jplus.database.DailyJobDao;
import org.jplus.database.DailyManDao;
import org.jplus.database.DailyNoteDao;
import org.jplus.quartz.QuartzManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @author hyberbin
 * @version $Id: InitService.java, v 0.1 2017年10月11日 20:28 hyberbin Exp $
 */
@Service(value = "initService")
public class InitService {

    private static final Logger log = LoggerFactory.getLogger(InitService.class);

    @Resource(name = "dailyManDao")
    private DailyManDao dailyManDao;

    @Resource(name = "dailyJobDao")
    private DailyJobDao dailyJobDao;


    @Resource(name = "dailyNoteDao")
    private DailyNoteDao dailyNoteDao;

    private static final String names="诗音,古函,诗晴,一灯,紫空,雾亥,伯通,陈俊辉,张晨晨,泞坊,DivineWill,一叶";

    public boolean init(){
        try {
            for(String name:names.split(",")){
                dailyManDao.addDailyMan(name);
            }
            dailyManDao.setNextDaily();
            dailyNoteDao.addDailyNote(0,"星期天不要忘记多学习一下哦！");
            dailyNoteDao.addDailyNote(1,"又是一个美好的开始！");
            dailyNoteDao.addDailyNote(2,"daily是不是该考虑借会议教室了？");
            dailyNoteDao.addDailyNote(3,"学习任务有没有抓紧？");
            dailyNoteDao.addDailyNote(4,"要抓紧完成学习任务哦，不然周报怎么写？");
            dailyNoteDao.addDailyNote(5,"不要忘记写周报哦，抓紧时间吧");
            dailyNoteDao.addDailyNote(6,"daily准备组织会议了吗？该好好总结一下了");
            //每周一上午1点换班
            dailyJobDao.addDailyJob("dailyManJob","0 0 1 ? * MON", "org.jplus.quartz.ChangeDaily");
            //每天上午9点提醒
            dailyJobDao.addDailyJob("dailyNoteJob","0 0 9 * * ?", "org.jplus.quartz.ShowDailyNote");
            QuartzManager.initFromDb();
            return true;
        }catch (Throwable throwable){
            log.error("init error!",throwable);
        }
        return false;
    }
}