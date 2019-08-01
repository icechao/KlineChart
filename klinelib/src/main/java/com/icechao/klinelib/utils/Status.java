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

    public enum CrossTouchModel {
        FOLLOW_FINGERS(true),//跟随手指移动
        SHOW_CLOSE(false); //y值显示当前价格

        private boolean state;

        CrossTouchModel(boolean state) {
            this.state = state;
        }

        public boolean getStateValue() {
            return state;
        }

    }

    public enum ChildStatus {
        MAIN_ONLY(0), //只显示主图
        MAIN_VOL(1), //显示主图量视图
        MAIN_INDEX(2), //主图指标图
        MAIN_VOL_INDEX(3);//主图交易量指标图

        ChildStatus(int statu) {
            this.statu = statu;
        }

        private int statu;

        public int getStatu() {
            return statu;
        }
    }

    public enum IndexStatus {
        NONE(-1), MACD(0), KDJ(1), RSI(2), WR(3);
        private int statu;

        IndexStatus(int statu) {
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

    public enum ShowCrossModle {
        SELECT_TOUCHE(0),//touch modle
        SELECT_PRESS(1),//long press modle
        SELECT_BOTH(2),//both modle
        SELECT_NONE(3);// can't touch
        private int model;

        ShowCrossModle(int model) {
            this.model = model;
        }
    }
}


