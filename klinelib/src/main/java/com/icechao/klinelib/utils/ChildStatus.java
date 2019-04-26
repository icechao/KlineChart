package com.icechao.klinelib.utils;

/*************************************************************************
 * Description   :
 *
 * @PackageName  : com.icechao.klinelib.utils
 * @FileName     : ChildStatus.java
 * @Author       : chao
 * @Date         : 2019-04-26
 * @Email        : icechliu@gmail.com
 * @version      : V1
 *************************************************************************/
public enum ChildStatus {
    NONE(-1), MACD(0), KDJ(1), RSI(2), WR(3);
    private int statu;

    ChildStatus(int statu) {
        this.statu = statu;
    }

    public int getStatu() {
        return statu;
    }

}
