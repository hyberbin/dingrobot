/**
 * hyberbin.com Inc.
 * Copyright (c) 2004-2017 All Rights Reserved.
 */
package org.jplus.database;

import org.jplus.daily.DailyJob;
import org.jplus.daily.DailyMan;
import org.jplus.daily.DailyNote;
import org.jplus.hyb.database.config.ConfigCenter;
import org.jplus.hyb.database.config.DbConfig;
import org.jplus.hyb.database.config.SimpleConfigurator;
import org.jplus.hyb.database.crud.DatabaseAccess;
import org.jplus.hyb.database.crud.Hyberbin;
import org.jplus.util.NumberUtils;
import org.jplus.util.Reflections;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.beans.Transient;
import java.lang.reflect.Field;
import java.sql.SQLException;
import java.util.List;

import static org.jplus.hyb.database.sqlite.SqliteUtil.getManager;

/**
 * @author hyberbin
 * @version $Id: DataBaseUtils.java, v 0.1 2017年10月07日 14:28 hyberbin Exp $
 */
public class DataBaseUtils {
    private static final Logger log = LoggerFactory.getLogger(DataBaseUtils.class);


    static {
        init();
    }

    public static void init() {
        SimpleConfigurator.addConfigurator(new DbConfig("org.sqlite.JDBC", "jdbc:sqlite:data.db", "", "", DbConfig.DEFAULT_CONFIG_NAME));
        try {
            createTable(DailyMan.class);
            createTable(DailyJob.class);
            createTable(DailyNote.class);
        } catch (SQLException e) {
            log.error("init error!",e);
        }
    }


    public static List select(String sql) throws SQLException {
        Hyberbin hyberbin = new Hyberbin();
        return hyberbin.getMapList(sql);
    }

    public static <T> List<T> selectAll(String where, Class<? extends T> cls) throws SQLException {
        Hyberbin hyberbin = new Hyberbin(Reflections.instance(cls.getName()));
        return hyberbin.showAll(where);
    }

    public static <T> T selectOne(String where, Class<? extends T> cls) throws SQLException {
        Hyberbin hyberbin = new Hyberbin(Reflections.instance(cls.getName()));
        return (T) hyberbin.showOne(where);
    }

    public static int execute(String sql) throws SQLException {
        DatabaseAccess databaseAccess = new DatabaseAccess(ConfigCenter.INSTANCE.getManager());
        return databaseAccess.update(sql);
    }

    public static void createTable(Class bean) throws SQLException {
        String tableName = bean.getSimpleName();
        if (!existTable(tableName)) {
            StringBuilder sql = new StringBuilder("create table ").append(tableName).append("(id integer primary key autoincrement");
            List<Field> allFields = Reflections.getAllFields(bean);
            for (Field allField : allFields) {
                if (allField.getName().toLowerCase().equals("id") || allField.isAnnotationPresent(Transient.class)) {
                    continue;
                }
                sql.append(",").append(allField.getName());
                if (Number.class.isAssignableFrom(allField.getType())) {
                    sql.append(" int ");
                } else {
                    sql.append(" text ");
                }
            }
            sql.append(")");
            DatabaseAccess databaseAccess = new DatabaseAccess(ConfigCenter.INSTANCE.getManager());
            databaseAccess.update(sql.toString());
        }
    }

    private static boolean existTable(String table) throws SQLException {
        String sql = "SELECT count(*) FROM sqlite_master where type='table' and name='" + table + "'";
        DatabaseAccess databaseAccess = new DatabaseAccess(getManager());
        int count = NumberUtils.parseInt(databaseAccess.queryUnique(sql));
        return count > 0;
    }
}