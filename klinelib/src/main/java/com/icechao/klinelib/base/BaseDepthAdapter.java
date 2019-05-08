package com.icechao.klinelib.base;

import android.database.DataSetObservable;
import android.database.DataSetObserver;
import android.os.Handler;
import android.os.Looper;

import java.util.ArrayList;
import java.util.List;

/*************************************************************************
 * Description   :
 *
 * @PackageName  : com.icechao.klinelib.base
 * @FileName     : BaseDepthAdapter.java
 * @Author       : chao
 * @Date         : 2019/4/9
 * @Email        : icechliu@gmail.com
 * @version      : V1
 *************************************************************************/
public abstract class BaseDepthAdapter implements com.icechao.klinelib.base.IAdapter {

    private Handler handler = new Handler(Looper.getMainLooper());
    private final DataSetObservable mDataSetObservable = new DataSetObservable();
    protected float[] tempLeftDatas;
    protected float[] tempRightDatas;
    private float maxValue = Float.MIN_VALUE;
    private float minValue = Float.MAX_VALUE;
    private float maxIndex = Float.MIN_VALUE;
    private float minIndex = Float.MAX_VALUE;

    private float leftMax;
    private float rightMax;

    protected List<List<Double>> iDepthsLeft = new ArrayList<>();
    protected List<List<Double>> iDepthsRight = new ArrayList<>();

    private Runnable notifyDataChangeRunable = mDataSetObservable::notifyChanged;
    private Runnable notifyDataWillChangeRunnable = mDataSetObservable::notifyInvalidated;

    @Override
    public void notifyDataSetChanged() {
        if (getCount() > 0) {
            handler.post(notifyDataChangeRunable);
        }
    }

    @Override
    public void notifyDataWillChanged() {
        handler.post(notifyDataWillChangeRunnable);
    }

    /**
     * 转换数据
     */
    protected float[] parseData(List<List<Double>> iDepths, boolean isLeft) {
        if (null == iDepths || iDepths.size() == 0) {
            return new float[0];
        }
        float maxValue = Float.MIN_VALUE;
        float[] tempDatas = new float[iDepths.size() * 2];
        for (int i = 0; i < iDepths.size(); i++) {
            List<Double> iDepth = iDepths.get(i);
            tempDatas[i * 2] = iDepth.get(0).floatValue();
            float vol = iDepth.get(1).floatValue();
            float price = iDepth.get(0).floatValue();
            tempDatas[i * 2 + 1] = vol;
            if (minValue > vol) {
                minValue = vol;
            }
            if (maxValue < vol) {
                maxValue = vol;
            }
            if (maxIndex < price) {
                maxIndex = price;
            }
            if (minIndex > price) {
                minIndex = price;
            }
        }
        if (this.maxValue < maxValue) {
            this.maxValue = maxValue;
        }
        if (isLeft) {
            leftMax = maxValue;
        } else {
            rightMax = maxValue;
        }
        return tempDatas;
    }

    public float[] getTempLeftDatas() {
        return tempLeftDatas;
    }

    public float[] getTempRightDatas() {
        return tempRightDatas;
    }

    public float getMaxValue() {
        return maxValue;
    }

    public float getMinValue() {
        return minValue;
    }

    public float getMaxIndex() {
        return maxIndex;
    }

    public float getMinIndex() {
        return minIndex;
    }

    @Override
    public void registerDataSetObserver(DataSetObserver observer) {
        mDataSetObservable.registerObserver(observer);
    }

    @Override
    public void unregisterDataSetObserver(DataSetObserver observer) {
        mDataSetObservable.unregisterObserver(observer);
    }

    public float getLeftMax() {
        return leftMax;
    }

    public float getRightMax() {
        return rightMax;
    }


}
