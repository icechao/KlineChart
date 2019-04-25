package com.icechao.klinelib.base;

import android.graphics.Canvas;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.icechao.klinelib.draw.Status;
import com.icechao.klinelib.entity.ICandle;


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
public interface IChartDraw<T> {

    /**
     * 需要滑动 物体draw方法
     *
     * @param canvas    canvas
     * @param view      k线图View
     * @param position  当前点的位置
     * @param lastPoint 上一个点
     * @param curPoint  当前点
     * @param lastX     上一个点的x坐标
     * @param curX      当前点的X坐标
     */
//    void drawTranslated(@Nullable T lastPoint, @NonNull T curPoint, float lastX, float curX, @NonNull Canvas canvas, @NonNull BaseKLineChartView view, int position);

    /**
     * 需要滑动 物体draw方法
     *
     * @param canvas   canvas
     * @param view     k线图View
     * @param position 当前点的位置
     * @param lastX    上一个点的x坐标
     * @param curX     当前点的X坐标
     */
    void drawTranslated(Canvas canvas, float lastX, float curX, @NonNull BaseKLineChartView view, int position, float... values);

    /**
     * @param canvas
     * @param view
     * @param position 该点的位置
     * @param x        x的起始坐标
     * @param y        y的起始坐标
     */
    void drawText(@NonNull Canvas canvas, @NonNull BaseKLineChartView view, int position, float x, float y);


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
     * @return
     */
    float getMinValue(float... values);

    /**
     * 获取value格式化器
     */
    com.icechao.klinelib.base.IValueFormatter getValueFormatter();

    void setValueFormatter(com.icechao.klinelib.base.IValueFormatter valueFormatter);

    void setItemCount(int mItemCount);


    /**
     * 开启动画变换数字
     *
     * @param view
     * @param values
     */
    void startAnim(BaseKLineChartView view, float... values);

    void resetValues();

}
