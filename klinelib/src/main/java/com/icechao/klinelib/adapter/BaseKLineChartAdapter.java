package com.icechao.klinelib.adapter;

import android.database.DataSetObservable;
import android.database.DataSetObserver;
import android.os.Handler;
import android.os.Looper;

import java.util.Date;


/*************************************************************************
 * Description   :
 *
 * @PackageName  : com.icechao.klinelib.adapter
 * @FileName     : BaseKLineChartAdapter.java
 * @Author       : chao
 * @Date         : 2019/4/8
 * @Email        : icechliu@gmail.com
 * @version      : V1
 *************************************************************************/

public abstract class BaseKLineChartAdapter<T> implements java.io.Serializable {

    private final Handler handler = new Handler(Looper.getMainLooper());
    private final DataSetObservable dataSetObservable = new DataSetObservable();

    private final Runnable notifyDataChangeRunnable = dataSetObservable::notifyChanged;
    private final Runnable notifyDataWillChangeRunnable = dataSetObservable::notifyInvalidated;

    /**
     * 发生变化
     */
    public void notifyDataSetChanged() {
        handler.post(notifyDataChangeRunnable);
    }


    /**
     * 即将发生变化
     */
    public void notifyDataWillChanged() {
        handler.post(notifyDataWillChangeRunnable);
    }

    /**
     * 添加数据观察者
     *
     * @param observer {@link DataSetObserver}
     */
    public void registerDataSetObserver(DataSetObserver observer) {
        dataSetObservable.registerObserver(observer);
    }


    /**
     * 移除一个数据观察者
     *
     * @param observer {@link DataSetObserver}
     */
    public void unregisterDataSetObserver(DataSetObserver observer) {
        dataSetObservable.unregisterObserver(observer);
    }

    /**
     * 获取数据个数
     *
     * @return int
     */
    public abstract int getCount();

    /**
     * 获取某个数据时间
     *
     * @param position int
     * @return {@link Date}
     */
    public abstract Date getDate(int position);

    /**
     * 获取处理的数据
     */
    public abstract float[] getPoints();

    /**
     * 重置显示位置
     */
    public abstract boolean getResetShowPosition();

    /**
     * 设置重置显示位置
     */
    public abstract void setResetShowPosition(boolean resetShowPosition);


    /**
     * 设置重置显示位置
     */
    public abstract boolean getResetLastAnim();

    /**
     * 设置重置最后一根柱子动画
     */
    public abstract void setResetLastAnim(boolean resetLastAnim);


}
