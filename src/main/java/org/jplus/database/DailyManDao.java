/**
 * hyberbin.com Inc.
 * Copyright (c) 2004-2017 All Rights Reserved.
 */
package org.jplus.database;

import org.jplus.daily.DailyMan;
import org.jplus.hyb.database.crud.Hyberbin;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.sql.SQLException;
import java.util.Collections;
import java.util.List;

/**
 * @author hyberbin
 * @version $Id: DailyManDao.java, v 0.1 2017年10月07日 16:02 hyberbin Exp $
 */
@Service("dailyManDao")
public class DailyManDao {
    private static final Logger log = LoggerFactory.getLogger(DailyManDao.class);

    public List<DailyMan> getAllDailyMan() {
        try {
            List<DailyMan> dailyMans = DataBaseUtils.selectAll(" order by orderNo", DailyMan.class);
            return dailyMans;
        } catch (SQLException e) {
            log.error("getAllDailyMan error!", e);
        }
        return Collections.EMPTY_LIST;
    }

    public String getAllDailyManStr() {
        List<DailyMan> allDailyMan = getAllDailyMan();
        StringBuilder stringBuilder = new StringBuilder();
        int i = 0;
        for (DailyMan dailyMan : allDailyMan) {
            stringBuilder.append(" - ").append(++i).append(".").append(dailyMan.getName()).append("\n");
        }
        return stringBuilder.toString();
    }

    public String getDailyDetailStr() {
        DailyMan[] dailyDetail = getDailyDetail();
        if (dailyDetail == null || dailyDetail.length != 3) {
            return "当前没有值班人";
        } else {
            return "\n- 上次值班人:" + dailyDetail[0].getName() +
                    "\n- 当前值班人:" + dailyDetail[1].getName() +
                    "\n- 下次值班人:" + dailyDetail[2].getName();
        }
    }

    /**
     * 获取当前值班人
     *
     * @return
     */
    public DailyMan getCurrentDailyMan() {
        try {
            List<DailyMan> dailyMans = DataBaseUtils.selectAll(" where daily=1 ", DailyMan.class);
            return dailyMans.size() > 0 ? dailyMans.get(0) : null;
        } catch (SQLException e) {
            log.error("getCurrentDailyMan error!", e);
        }
        return null;
    }

    /**
     * 获取值班详细
     * 上次值班人，今天值班人，下次值班人
     *
     * @return
     */
    public DailyMan[] getDailyDetail() {
        try {
            List<DailyMan> allDailyMan = getAllDailyMan();
            if (CollectionUtils.isEmpty(allDailyMan)) {
                log.warn("没有设置值班人!");
                return null;
            }
            DailyMan currentDailyMan = getCurrentDailyMan();
            DailyMan nextDailyMan = null;
            DailyMan beforeDailyMan = null;
            if (currentDailyMan == null) {
                nextDailyMan = allDailyMan.get(0);
            } else {
                for (int i = 0; i < allDailyMan.size(); i++) {
                    DailyMan dailyMan = allDailyMan.get(i);
                    if (i == allDailyMan.size() - 1) {
                        nextDailyMan = allDailyMan.get(0);
                        beforeDailyMan = allDailyMan.get(allDailyMan.size() - 2);
                        break;
                    } else if (dailyMan.getId().equals(currentDailyMan.getId())) {
                        nextDailyMan = allDailyMan.get(i + 1);
                        if (i == 0) {
                            beforeDailyMan = allDailyMan.get(allDailyMan.size() - 1);
                        } else {
                            beforeDailyMan = allDailyMan.get(i - 1);
                        }
                        break;
                    }
                }
            }
            return new DailyMan[]{beforeDailyMan, currentDailyMan, nextDailyMan};
        } catch (Throwable e) {
            log.error("getDailyDetail error!", e);
        }
        return null;
    }

    /**
     * 设置下一个值班人
     *
     * @return
     */
    public boolean setNextDaily() {
        try {
            List<DailyMan> allDailyMan = getAllDailyMan();
            if (CollectionUtils.isEmpty(allDailyMan)) {
                log.warn("没有设置值班人!");
                return false;
            }
            DailyMan currentDailyMan = getCurrentDailyMan();
            DailyMan nextDailyMan = null;
            if (currentDailyMan == null) {
                nextDailyMan = allDailyMan.get(0);
            } else {
                for (int i = 0; i < allDailyMan.size(); i++) {
                    DailyMan dailyMan = allDailyMan.get(i);
                    if (i == allDailyMan.size() - 1) {
                        nextDailyMan = allDailyMan.get(0);
                        break;
                    } else if (dailyMan.getId().equals(currentDailyMan.getId())) {
                        nextDailyMan = allDailyMan.get(i + 1);
                        break;
                    }
                }
            }
            log.info("下一个值班人为：{}", nextDailyMan.getName());
            int execute = DataBaseUtils.execute("update DailyMan set daily=0");
            int execute1 = DataBaseUtils.execute("update DailyMan set daily=1 where id=" + nextDailyMan.getId());
            return execute > 0 && execute1 > 0;
        } catch (Throwable e) {
            log.error("setNextDaily error!", e);
        }
        return false;
    }

    public boolean addDailyMan(String name) {
        try {
            DailyMan dailyMan = new DailyMan();
            List<DailyMan> allDailyMan = getAllDailyMan();
            if (CollectionUtils.isEmpty(allDailyMan)) {
                dailyMan.setOrderNo(10);
            } else {
                int lastOrderNo = allDailyMan.get(allDailyMan.size() - 1).getOrderNo();
                dailyMan.setOrderNo((lastOrderNo / 10 + 1) * 10);
            }
            dailyMan.setName(name);
            Hyberbin hyberbin = new Hyberbin(dailyMan);
            int insert = hyberbin.insert("id");
            return insert > 0;
        } catch (SQLException e) {
            log.error("addDailyMan error!", e);
        }
        return false;
    }
}