package com.icechao.klinelib.formatter;

import com.icechao.klinelib.base.IDateTimeFormatter;
import com.icechao.klinelib.utils.DateUtil;

import java.util.Date;

/*************************************************************************
 * Description   :
 *
 * @PackageName  : com.icechao.klinelib.utils
 * @FileName     : TimeFormatter.java
 * @Author       : chao
 * @Date         : 2019/4/8
 * @Email        : icechliu@gmail.com
 * @version      : V1
 *************************************************************************/
public class TimeFormatter implements IDateTimeFormatter {
    @Override
    public String format(Date date) {
        if (date == null) {
            return "";
        }
        return DateUtil.HHMMTimeFormat.format(date);
    }
}
