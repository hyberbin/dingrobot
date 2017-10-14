/**
 * hyberbin.com Inc.
 * Copyright (c) 2004-2017 All Rights Reserved.
 */
package org.jplus.robot;

import com.alibaba.fastjson.JSON;
import org.jplus.database.DailyManDao;
import org.jplus.database.DataBaseUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.context.embedded.ConfigurableEmbeddedServletContainer;
import org.springframework.boot.context.embedded.EmbeddedServletContainerCustomizer;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

/**
 * @author hyberbin
 * @version $Id: RobotController.java, v 0.1 2017年09月25日 20:17 hyberbin Exp $
 */
@Controller
@ComponentScan(basePackages = {"org.jplus.robot","org.jplus.database"})
@EnableAutoConfiguration
public class RobotController implements EmbeddedServletContainerCustomizer {

    private static final Logger log = LoggerFactory.getLogger(RobotController.class);

    @Resource(name = "dailyManDao")
    private DailyManDao dailyManDao;

    @RequestMapping("/")
    @ResponseBody
    public Object send(HttpServletRequest request) {
        log.info("requestData:{}",JSON.toJSONString(request.getParameterMap()));
        RobotResult robotResult = new RobotResult();
        robotResult.setErrorCode("200");
        String rfPlainText = request.getHeader("rfPlainText");
        String rfCipherText = request.getHeader("rfCipherText");
        String msg = request.getParameter("sys.用户输入");
        if(msg!=null){
            Map<String, String> fieldMap = new HashMap<>();
            robotResult.setFields(fieldMap);
            fieldMap.put("msg", execute(msg));
            robotResult.setSuccess(true);
            robotResult.setErrorCode("200");
            robotResult.setErrorMsg("SUCCESS");
            log.info("returnData:{}",JSON.toJSONString(robotResult));
            return robotResult;
        }else {
            return null;
        }
    }



    private String execute(String msg) {
        try {
            String msgType = null;
            String content = null;
            if (msg.contains(":")) {
                int index = msg.indexOf(":");
                msgType = msg.substring(0, index).toLowerCase();
                content = msg.substring(index + 1);
            } else {
                msgType = msg;
            }
            switch (msgType) {
                case "select":
                    return JSON.toJSONString(DataBaseUtils.select(content));
                case "execute":
                    return JSON.toJSONString(DataBaseUtils.execute(content));
                case "daily":
                    return dailyManDao.getDailyDetailStr();
                case "allDailyMan":
                    return "排班列表：：\n"+ dailyManDao.getAllDailyManStr();
                case "spel":
                    return "执行表达式：\n"+ ELUtils.getSpelValue(content);
                default:
                    return "看不懂你在说什么\n" +
                            "- 查询指令:select:sql\n" +
                            "- 更新指令:execute:sql\n"+
                            "- [排班列表](dtmd://dingtalkclient/sendMessage?content=allDailyMan)\n"+
                            "- [当前值班人](dtmd://dingtalkclient/sendMessage?content=daily)\n"+
                            "- 执行表达式:spel:expression\n";
            }
        } catch (Throwable t) {
            StackTraceElement[] stackTrace = t.getStackTrace();
            StringBuilder stringBuilder = new StringBuilder(t.getMessage()).append("\n");
            for (StackTraceElement element : stackTrace) {
                stringBuilder.append(element.toString()).append("\n");
            }
            return stringBuilder.toString();
        }

    }


    @Override
    public void customize(ConfigurableEmbeddedServletContainer configurableEmbeddedServletContainer) {
        configurableEmbeddedServletContainer.setPort(80);
    }


}