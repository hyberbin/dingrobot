/**
 * hyberbin.com Inc.
 * Copyright (c) 2004-2017 All Rights Reserved.
 */
package org.jplus.daily;

import lombok.Data;

/**
 * @author hyberbin
 * @version $Id: DailyMan.java, v 0.1 2017年10月07日 14:52 hyberbin Exp $
 */
@Data
public class DailyMan {
    private Integer id;
    /**值班人姓名*/
    private String name;
    /**排序编号*/
    private Integer orderNo;
    /**是否当前值班人*/
    private Integer daily;
}