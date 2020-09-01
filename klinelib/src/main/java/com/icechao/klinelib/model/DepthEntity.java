package com.icechao.klinelib.model;

/*************************************************************************
 * Description   :
 *
 * @PackageName  : com.icechao.klinelib.model
 * @FileName     : DepthEntity.java
 * @Author       : chao
 * @Date         : 2019/4/9
 * @Email        : icechliu@gmail.com
 * @version      : V1
 *************************************************************************/


public class DepthEntity implements com.icechao.klinelib.model.IDepth {

    private float price;
    private float vol;

    @Override
    public float getPrice() {
        return price;
    }

    @Override
    public float getVol() {
        return vol;
    }

    public void setPrice(float price) {
        this.price = price;
    }

    public void setVol(float vol) {
        this.vol = vol;
    }
}
