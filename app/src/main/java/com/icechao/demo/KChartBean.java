package com.icechao.demo;

import com.google.gson.annotations.SerializedName;
import com.icechao.klinelib.entity.KLineEntity;

import java.text.ParseException;
import java.text.SimpleDateFormat;

/*************************************************************************
 * Description   :
 *
 * @PackageName  : com.icechao.demo
 * @FileName     : KChartBean.java
 * @Author       : chao
 * @Date         : 2019-06-06
 * @Email        : icechliu@gmail.com
 * @version      : V1
 *************************************************************************/
public class KChartBean extends KLineEntity {
    @Override
    public Long getDate() {
        try {
            return new SimpleDateFormat("yyy/MM/dd").parse(date).getTime();
        } catch (ParseException e) {
            e.printStackTrace();
            return 0L;
        }
    }

    @Override
    public float getOpenPrice() {
        return open;
    }

    @Override
    public float getHighPrice() {
        return high;
    }

    @Override
    public float getLowPrice() {
        return low;
    }

    @Override
    public float getClosePrice() {
        return close;
    }

    @Override
    public float getVolume() {
        return volume;
    }

    @SerializedName("Date")
    public String date;
    @SerializedName("Open")
    public float open;
    @SerializedName("High")
    public float high;
    @SerializedName("Low")
    public float low;
    @SerializedName("Close")
    public float close;
    @SerializedName("Volume")
    public float volume;
}
