package com.icechao.klinelib.base;

import android.database.DataSetObserver;
import com.icechao.klinelib.entity.KLineEntity;

import java.io.Serializable;
import java.util.Date;

/*************************************************************************
 * Description   :
 *
 * @PackageName  : com.icechao.klinelib.utils
 * @FileName     : IAdapter.java
 * @Author       : chao
 * @Date         : 2019/4/8
 * @Email        : icechliu@gmail.com
 * @version      : V1
 *************************************************************************/

public interface IAdapter<T> extends Serializable {
    /**
     * 获取数据个数
     *
     * @return
     */
    int getCount();

    /**
     * 获取某个数据
     *
     * @param position 对应的序号
     * @return 数据实体
     */
    KLineEntity getItem(int position);

    /**
     * 获取某个数据时间
     *
     * @param position
     * @return
     */
    Date getDate(int position);

    /**
     * 添加数据观察者
     *
     * @param observer 数据观察者
     */
    void registerDataSetObserver(DataSetObserver observer);

    /**
     * 移除一个数据观察者
     *
     * @param observer 数据观察者
     */
    void unregisterDataSetObserver(DataSetObserver observer);

    /**
     * 发生变化
     */
    void notifyDataSetChanged();

    /**
     * 即将发生变化
     */
    void notifyDataWillChanged();

    /**
     * 向尾部追加数据
     */
    void addLast(T entity);
}
