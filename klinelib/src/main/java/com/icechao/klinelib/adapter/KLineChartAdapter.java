package com.icechao.klinelib.adapter;

import com.icechao.klinelib.base.BaseKLineChartAdapter;
import com.icechao.klinelib.entity.KLineEntity;
import com.icechao.klinelib.utils.Constants;
import com.icechao.klinelib.utils.DataHelper;
import com.icechao.klinelib.utils.LogUtil;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 数据适配器
 * Created by tifezh on 2016/6/18.
 */
public class KLineChartAdapter<T extends KLineEntity> extends BaseKLineChartAdapter<T> {

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
        if (position >= dataCount) {
            return new Date();
        }
        return new Date(datas.get(position).getDate());
    }


    public void resetData(List<T> data) {
        notifyDataWillChanged();
        datas = data;
        if (null != data && data.size() > 0) {
            this.dataCount = datas.size();
            points = DataHelper.calculate((List<KLineEntity>) datas);
            notifyDataSetChanged();
        } else {
            points = new float[]{};
            this.dataCount = 0;
        }
    }

    /**
     * 向尾部添加数据
     */
    @Override
    public void addLast(T entity) {
        if (null != entity) {
            datas.add(entity);
            this.dataCount++;
            points = DataHelper.calculate((List<KLineEntity>) datas);
            notifyDataSetChanged();
        }
    }

    /**
     * 改变某个点的值
     *
     * @param position 索引值
     */
    public void changeItem(int position, T data) {
        datas.set(position, data);
        points = DataHelper.calculate((List<KLineEntity>) datas);
        notifyDataSetChanged();
    }
}
