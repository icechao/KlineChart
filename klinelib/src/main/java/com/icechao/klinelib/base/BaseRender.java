package com.icechao.klinelib.base;

import android.graphics.Canvas;
import android.support.annotation.NonNull;

import com.icechao.klinelib.formatter.IValueFormatter;

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
public abstract class BaseRender {

    public abstract void render(Canvas canvas, float lastX, float curX, @NonNull BaseKLineChartView view, int position, float... values);

    public abstract void drawText(@NonNull Canvas canvas, @NonNull BaseKLineChartView view, float x, float y, int position, float[] values);

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

    public void renderMaxMinValue(Canvas canvas, BaseKLineChartView view,
                                  float maxX, float mainHighMaxValue,
                                  float minX, float mainLowMinValue) {
    }

    public abstract void startAnim(BaseKLineChartView view, float... values);

    public abstract IValueFormatter getValueFormatter();

    public abstract void setValueFormatter(IValueFormatter valueFormatter);

    public abstract void setItemCount(int mItemCount);

    public abstract void resetValues();

    public abstract void setLineWidth(float width);

    public abstract void setTextSize(float textSize);
}
