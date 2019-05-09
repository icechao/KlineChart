package com.icechao.klinelib.entity;


import com.google.gson.annotations.SerializedName;

import java.util.Date;

/*************************************************************************
 * Description   :
 *
 * @PackageName  : com.icechao.klinelib.utils
 * @FileName     : KLineEntity.java
 * @Author       : chao
 * @Date         : 2019/4/8
 * @Email        : icechliu@gmail.com
 * @version      : V1
 *************************************************************************/
public class KLineEntity implements IKLine {


    @Override
    public String getDate() {
        return new Date(getId()).toLocaleString();
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
    public float getMaOne() {
        return maOne;
    }

    @Override
    public float getMaTwo() {
        return maTwo;
    }

    @Override
    public float getMaThree() {
        return maThree;
    }

    @Override
    public float getDea() {
        return dea;
    }

    @Override
    public float getDif() {
        return dif;
    }

    @Override
    public float getMacd() {
        return macd;
    }

    @Override
    public float getK() {
        return k;
    }

    @Override
    public float getD() {
        return d;
    }

    @Override
    public float getJ() {
        return j;
    }

    @Override
    public float getUp() {
        return up;
    }

    @Override
    public float getMb() {
        return mb;
    }

    @Override
    public float getDn() {
        return dn;
    }

    @Override
    public float getVolume() {
        return volume;
    }

    @Override
    public float getMA5Volume() {
        return MA5Volume;
    }

    @Override
    public float getMA10Volume() {
        return MA10Volume;
    }


    public float getHigh() {
        return high;
    }

    public void setHigh(float high) {
        this.high = high;
    }

    public float getAmount() {
        return volume;
    }

    public void setAmount(float amount) {
        this.volume = amount;
    }

    public float getVol() {
        return volume;
    }

    public void setVol(float vol) {
        this.volume = vol;
    }

    public float getLow() {
        return low;
    }

    public void setLow(float low) {
        this.low = low;
    }


    public long getId() {
        return date;
    }

    public void setId(String id) {
        this.date = Long.parseLong(id);
    }

    @SerializedName("id")
    public long date;
    @SerializedName("open")
    public float open;
    @SerializedName("high")
    public float high;
    @SerializedName("low")
    public float low;
    @SerializedName("close")
    public float close;
    @SerializedName("volume")
    public float volume;

    public float maOne;

    public float maTwo;

    public float maThree;

    public float dea;

    public float dif;

    public float macd;

    public float k;

    public float d;

    public float j;

    public double rOne;
    public float rTwo;
    public float rThree;

    public double wrOne;
    public float wrTwo;
    public float wrThree;

    public float up;

    public float mb;

    public float dn;

    public float MA5Volume;

    public float MA10Volume;


    @Override
    public float getRsiOne() {
        return (float) rOne;
    }

    @Override
    public float getRsiTwo() {
        return rTwo;
    }

    @Override
    public float getRsiThree() {
        return rThree;
    }


    @Override
    public Float getWrOne() {
        return (float) wrOne;
    }

    @Override
    public Float getWrTwo() {
        return wrTwo;
    }

    @Override
    public Float getWrThree() {
        return wrThree;
    }
}
