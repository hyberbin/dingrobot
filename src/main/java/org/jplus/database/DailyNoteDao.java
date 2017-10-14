/**
 * hyberbin.com Inc.
 * Copyright (c) 2004-2017 All Rights Reserved.
 */
package org.jplus.database;

import org.jplus.daily.DailyNote;
import org.jplus.hyb.database.crud.Hyberbin;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.sql.SQLException;
import java.util.Date;

/**
 * @author hyberbin
 * @version $Id: DailyNoteDao.java, v 0.1 2017年10月11日 20:11 hyberbin Exp $
 */
@Service("dailyNoteDao")
public class DailyNoteDao {
    private static final Logger log = LoggerFactory.getLogger(DailyNoteDao.class);


    @Resource(name = "dailyManDao")
    private DailyManDao dailyManDao;

    public boolean addDailyNote(int day, String note) {
        DailyNote dailyNote = new DailyNote();
        dailyNote.setDay(day);
        dailyNote.setNote(note);
        try {
            new Hyberbin(dailyNote).delete(" where day=" + day);
            Hyberbin hyberbin = new Hyberbin(dailyNote);
            return hyberbin.insert("id") > 0;
        } catch (SQLException e) {
            log.error("addDailyNote error!", e);
        }
        return false;
    }

    public DailyNote getDailyNote(int day) {
        try {
            Hyberbin<DailyNote> hyberbin = new Hyberbin(new DailyNote());
            return hyberbin.showOne("select * from DailyNote where day=?", day);
        } catch (SQLException e) {
            log.error("getDailyNote error!", e);
        }
        return null;
    }

    public String getDailyNoteStr() {
        DailyNote dailyNote = getDailyNote(new Date().getDay());
        String dailyDetailStr = dailyManDao.getDailyDetailStr();
        StringBuilder note = new StringBuilder();
        note.append(dailyDetailStr);
        if (dailyNote != null) {
            note.append("\n\n####  ").append(dailyNote.getNote());
        }
        return note.toString();
    }
}