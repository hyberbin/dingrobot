/**
 * hyberbin.com Inc.
 * Copyright (c) 2004-2017 All Rights Reserved.
 */
package org.jplus.robot;

import com.alibaba.fastjson.JSON;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * @author hyberbin
 * @version $Id: RobotUtils.java, v 0.1 2017年10月07日 15:14 hyberbin Exp $
 */
public class RobotUtils {
    private static final Logger log = LoggerFactory.getLogger(RobotUtils.class);
    //private static final String webHook = "https://oapi.dingtalk.com/robot/send?access_token=XXX";
    private static final String webHook = System.getenv("webhook");

    public static void sendMsg(String msg) {
        Connection connect = Jsoup.connect(webHook);
        connect.header("Content-Type", "application/json");
        Map<String, Object> data = new HashMap<>();
        data.put("msgtype", "markdown");
        Map content = new HashMap<>();
        content.put("title", "系统提示");
        content.put("text", msg);
        Map<String, Object> at = new HashMap<>();
        at.put("isAtAll", true);
        content.put("at", at);
        data.put("markdown", content);
        connect.requestBody(JSON.toJSONString(data));
        connect.ignoreContentType(true);
        connect.method(Connection.Method.POST);
        try {
            Connection.Response execute = connect.execute();
            log.info("sendMsg response:{}", execute.body());
        } catch (IOException e) {
            log.error("sendMsg error!", e);
        }
    }
}