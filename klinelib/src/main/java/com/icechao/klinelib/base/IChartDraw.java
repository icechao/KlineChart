package com.icechao.klinelib.base;

import android.graphics.Canvas;
import android.support.annotation.NonNull;


/*************************************************************************
 * Description   :
 *
 * @PackageName  : com.icechao.klinelib.utils
 * @FileName     : IChartDraw.java
 * @Author       : chao
 * @Date         : 2019/4/8
 * @Email        : icechliu@gmail.com
 * @version      : V1
 *************************************************************************/
public interface IChartDraw {

    /**
     * 绘制
     *
     * @param canvas   {@link Canvas}
     * @param lastX    Y轴的value
     * @param curX     X点坐标
     * @param view     {@link BaseKLineChartView}
     * @param position 当前点index
     * @param values   前一个点和后一个点的值
     */
    void drawTranslated(Canvas canvas, float lastX, float curX, @NonNull BaseKLineChartView view, int position, float... values);

    /**
     * 绘制文本
     *
     * @param canvas   {@link Canvas}
     * @param view     {@link BaseKLineChartView}
     * @param x        X点坐标
     * @param y        Y轴的value
     * @param position 当前点index
     * @param values   前一个点和后一个点的值
     */
    void drawText(@NonNull Canvas canvas, @NonNull BaseKLineChartView view, float x, float y, int position, float... values);


    /**
     * 获取当前实体中最大的值
     *
     * @param values
     * @return
     */
    float getMaxValue(float... values);

    /**
     * 获取当前实体中最小的值
     *
     * @param values
     * @return value
     */
    float getMinValue(float... values);

    /**
     * 获取value格式化对象
     */
    IValueFormatter getValueFormatter();

    void setValueFormatter(com.icechao.klinelib.base.IValueFormatter valueFormatter);

    void setItemCount(int mItemCount);


    /**
     * 开启动画
     *
     * @param view
     * @param values
     */
    void startAnim(BaseKLineChartView view, float... values);

    void resetValues();

}
