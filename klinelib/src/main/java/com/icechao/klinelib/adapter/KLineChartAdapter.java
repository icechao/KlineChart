package com.icechao.klinelib.adapter;

import com.icechao.klinelib.base.BaseKLineChartAdapter;
import com.icechao.klinelib.entity.KLineEntity;
import com.icechao.klinelib.utils.DataHelper;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 数据适配器
 * Created by tifezh on 2016/6/18.
 */
public class KLineChartAdapter<T extends KLineEntity> extends BaseKLineChartAdapter<T> {
//    private T lastData;

    private int dataCount;

    private List<T> datas = new ArrayList<>();
    private float[] points;

    public float[] getPoints() {
        return points;
    }

    public KLineChartAdapter() {

    }

    @Override
    public int getCount() {
        return datas.size();
    }

    @Override
    public T getItem(int position) {

        if (dataCount == 0 || position < 0 || position >= dataCount) {
            return null;
        }
        return datas.get(position);

    }

    @Override
    public Date getDate(int position) {
        return new Date(datas.get(position).getDate());
    }


    public void resetData(List<T> data) {
        notifyDataWillChanged();
        if (null != data && data.size() > 0) {
            datas = data;
            this.dataCount = datas.size();
//            this.lastData = data.get(data.size() - 1);
            points = DataHelper.calculate((List<KLineEntity>) datas);
            notifyDataSetChanged();
        }
    }

    /**
     * 向尾部添加数据
     */
    @Override
    public void addLast(T entity) {
        if (null != entity) {
            datas.add(entity);
            points = DataHelper.calculate((List<KLineEntity>) datas);
//            this.lastData = datas.get(datas.size() - 1);
            this.dataCount++;
            notifyDataSetChanged();
        }
    }


    /**
     * 获取当前K线最后一个数据
     *
     * @return 最后一根线的bean
     */
//    public KLineEntity getLastData() {
//        return lastData;
//    }

    /**
     * 改变某个点的值
     *
     * @param position 索引值
     */
    public void changeItem(int position, T data) {
        datas.set(position, data);
//        this.lastData = datas.get(datas.size() - 1);
        points = DataHelper.calculate((List<KLineEntity>) datas);
        notifyDataSetChanged();
    }

    /**
     * 数据清除
     */
    public void clearData() {
        this.datas.clear();
//        this.lastData = null;
        this.dataCount = datas.size();
        notifyDataSetChanged();
    }

    public List<T> getData() {
        return datas;
    }
}
