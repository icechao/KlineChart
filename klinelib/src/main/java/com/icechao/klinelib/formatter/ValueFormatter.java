package com.icechao.klinelib.formatter;


import com.icechao.klinelib.base.IValueFormatter;

import java.util.Locale;

/*************************************************************************
 * Description   :
 *
 * @PackageName  : com.icechao.klinelib.utils
 * @FileName     : ValueFormatter.java
 * @Author       : chao
 * @Date         : 2019/4/8
 * @Email        : icechliu@gmail.com
 * @version      : V1
 *************************************************************************/

public class ValueFormatter implements IValueFormatter {
    @Override
    public String format(float value) {
        return String.format(Locale.CHINA, "%.2f", value);
    }
}
