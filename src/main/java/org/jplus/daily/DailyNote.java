/**
 * hyberbin.com Inc.
 * Copyright (c) 2004-2017 All Rights Reserved.
 */
package org.jplus.daily;

import lombok.Data;

/**
 * @author hyberbin
 * @version $Id: DailyNote.java, v 0.1 2017年10月07日 16:59 hyberbin Exp $
 */
@Data
public class DailyNote {
    private Integer id;
    private Integer day;
    private String note;
}