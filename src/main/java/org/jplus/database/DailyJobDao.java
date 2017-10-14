/**
 * hyberbin.com Inc.
 * Copyright (c) 2004-2017 All Rights Reserved.
 */
package org.jplus.database;

import org.jplus.daily.DailyJob;
import org.jplus.hyb.database.crud.Hyberbin;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.sql.SQLException;
import java.util.Collections;
import java.util.List;

/**
 * @author hyberbin
 * @version $Id: DailyJobDao.java, v 0.1 2017年10月10日 20:48 hyberbin Exp $
 */
@Service("dailyJobDao")
public class DailyJobDao {
    private static final Logger log = LoggerFactory.getLogger(DailyJobDao.class);

    public boolean addDailyJob(String name, String time, String clazz) {
        DailyJob job = new DailyJob();
        job.setName(name);
        job.setTime(time);
        job.setClazz(clazz);
        Hyberbin hyberbin = new Hyberbin(job);
        int insert = 0;
        try {
            insert = hyberbin.insert("id");
        } catch (SQLException e) {
            log.error("addDailyJob error!", e);
        }
        return insert > 0;
    }

    public List<DailyJob> getJobs() {
        Hyberbin hyberbin = new Hyberbin(new DailyJob());
        try {
            return hyberbin.showAll();
        } catch (SQLException e) {
            log.error("getJobs error!", e);
        }
        return Collections.EMPTY_LIST;
    }
}