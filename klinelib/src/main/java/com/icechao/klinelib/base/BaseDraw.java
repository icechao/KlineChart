package com.icechao.klinelib.base;

import java.util.Arrays;

/*************************************************************************
 * Description   :
 *
 * @PackageName  : com.icechao.klinelib.base
 * @FileName     : BaseDraw.java
 * @Author       : chao
 * @Date         : 2019/4/17
 * @Email        : icechliu@gmail.com
 * @version      : V1
 *************************************************************************/
public abstract class BaseDraw implements IChartDraw {

    @Override
    public float getMaxValue(float... values) {
        if (values.length == 0) {
            return 0;
        }
        Arrays.sort(values);
        float value = values[values.length - 1];
        if (Float.MIN_VALUE != value) {
            return value;
        } else {
            return 0;
        }
    }

    @Override
    public float getMinValue(float... values) {
        int length = values.length;
        if (length == 0) {
            return 0;
        }
        Arrays.sort(values);
        for (int i = 0; i < length; i++) {
            if (Float.MIN_VALUE < values[i]) {
                return values[i];
            }
        }
        return 0;
    }
}
