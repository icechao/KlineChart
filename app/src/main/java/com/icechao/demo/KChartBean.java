package com.icechao.demo;

import com.icechao.klinelib.model.KLineEntity;

/*************************************************************************
 * Description   : 定义bean继承 {@link KLineEntity}
 *
 * @PackageName  : com.icechao.demo
 * @FileName     : KChartBean.java
 * @Author       : chao
 * @Date         : 2019-06-06
 * @Email        : icechliu@gmail.com
 * @version      : V1
 *************************************************************************/
public class KChartBean extends KLineEntity {

    /*-------------------------------------*/
    //实现数据返回
    @Override
    public Long getDate() {
        return id * 1000;
    }

    @Override
    public float getOpenPrice() {
        return (float) open;
    }

    @Override
    public float getHighPrice() {
        return (float) high;
    }

    @Override
    public float getLowPrice() {
        return (float) low;
    }

    @Override
    public float getClosePrice() {
        return (float) close;
    }

    @Override
    public float getVolume() {
        return (float) amount;
    }

    /*-------------------------------------*/


    public double open;
    public double close;
    public double high;
    public double low;
    public double amount;
    public long id;

}
