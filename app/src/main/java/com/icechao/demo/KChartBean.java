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
    private float open;
    @SerializedName("High")
    private float high;
    @SerializedName("Low")
    private float low;
    @SerializedName("Close")
    private float close;
    @SerializedName("Volume")
    private float volume;

    public void setDate(String date) {
        this.date = date;
    }

    public float getOpen() {
        return open;
    }

    public void setOpen(float open) {
        this.open = open;
    }

    public float getHigh() {
        return high;
    }

    public void setHigh(float high) {
        this.high = high;
    }

    public float getLow() {
        return low;
    }

    public void setLow(float low) {
        this.low = low;
    }

    public float getClose() {
        return close;
    }

    public void setClose(float close) {
        this.close = close;
    }

    public void setVolume(float volume) {
        this.volume = volume;
    }


    @Override
    public String toString() {
        return "KChartBean{" +
                "date='" + date + '\'' +
                ", open=" + open +
                ", high=" + high +
                ", low=" + low +
                ", close=" + close +
                ", volume=" + volume +
                '}';
    }
}
