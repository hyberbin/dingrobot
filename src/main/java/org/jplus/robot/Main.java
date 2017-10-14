/**
 * hyberbin.com Inc.
 * Copyright (c) 2004-2017 All Rights Reserved.
 */
package org.jplus.robot;

import org.jplus.database.DataBaseUtils;
import org.jplus.quartz.QuartzManager;
import org.springframework.boot.Banner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextStartedEvent;


/**
 * @author hyberbin
 * @version $Id: Main.java, v 0.1 2017年09月25日 20:26 hyberbin Exp $
 */
public class Main {

    public static void main(String[] args) throws Exception {
        SpringApplication application = new SpringApplication(RobotController.class);
        application.setBannerMode(Banner.Mode.OFF);
        application.addListeners(new ApplicationListener<ApplicationReadyEvent>() {

            @Override
            public void onApplicationEvent(ApplicationReadyEvent applicationEvent) {
                DataBaseUtils.init();
                RobotUtils.sendMsg("机器人服务已启动");
                QuartzManager.initFromDb();
                SpringContextUtil.initSpelVars();
            }
        });
        application.run(args);
    }
}