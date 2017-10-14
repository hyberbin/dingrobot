package org.jplus.robot;

import lombok.Data;

import java.util.Map;

/**
 * Created by hyberbin on 2017/8/26.
 */
@Data
public class RobotResult {
    private boolean success;
    private String errorCode;
    private String errorMsg;
    private Map<String,String> fields;

}
