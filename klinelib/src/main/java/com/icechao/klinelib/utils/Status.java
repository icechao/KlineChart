package com.icechao.klinelib.utils;

/*************************************************************************
 * Description   :
 *
 * @PackageName  : com.icechao.klinelib.utils
 * @FileName     : Status.java
 * @Author       : chao
 * @Date         : 2019-04-26
 * @Email        : icechliu@gmail.com
 * @version      : V1
 *************************************************************************/
public class Status {

    public enum MainStatus {
        MA, BOLL, NONE
    }

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

    public enum KlineStatus {
        TIME_LINE(true),//分时线
        K_LINE(false); //K线

        private boolean statu;

        KlineStatus(boolean statu) {
            this.statu = statu;
        }

        public boolean showLine() {
            return statu;
        }
    }
}


