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

    public enum PriceLineLabelState {
        PRICE_LINE_WITH_LABEL, PRICE_LINE_RIGHT_LABEL;
    }

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
        MAIN_ONLY(0), //显示主图交易量时隐藏交易量
        MAIN_VOL(1), //显示主图时显示交易量
        MAIN_INDEX(2), //显示主图交易量指标图时隐藏交易量
        MAIN_VOL_INDEX(3);//显示主图指标图时显示交易量

        ChildStatus(int statu) {
            this.statu = statu;
        }

        private int statu;

        public int getStatu() {
            return statu;
        }
    }

    public enum IndexStatus {
        NONE("none"), MACD("macd"), KDJ("kdj"), RSI("rsi"), WR("wr"), EMA("ema");
        private String statu;

        IndexStatus(String statu) {
            this.statu = statu;
        }

        public String getStatu() {
            return statu;
        }

    }

    public enum VolChartStatus {
        BAR_CHART(true),//柱状图
        LINE_CHART(false); //线状图

        private boolean statu;

        VolChartStatus(boolean statu) {
            this.statu = statu;
        }

        public boolean showLine() {
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

    public enum ShowCrossModel {
        SELECT_TOUCHE(0),//touch modle
        SELECT_PRESS(1),//long press modle
        SELECT_BOTH(2),//both modle
        SELECT_NONE(3);// can't touch
        private int model;

        ShowCrossModel(int model) {
            this.model = model;
        }
    }

    public enum HollowModel {
        NONE_HOLLOW(0),//全实心
        ALL_HOLLOW(1), //全空心
        DECREASE_HOLLOW(2),//涨空心
        INCREASE_HOLLOW(3);//跌实心

        private int model;

        HollowModel(int model) {
            this.model = model;
        }

        public static HollowModel getStrokeModel(int integer) {
            switch (integer) {
                default:
                case 0:
                    return NONE_HOLLOW;
                case 1:
                    return ALL_HOLLOW;
                case 2:
                    return DECREASE_HOLLOW;
                case 3:
                    return INCREASE_HOLLOW;
            }
        }
    }

    public enum MaxMinCalcModel {

        CALC_NORMAL_WITH_SHOW,//计算显示的线全部值
        CALC_CLOSE_WITH_SHOW,//计算显示的线的close值
        CALC_NORMAL_WITH_LAST,//计算显示的线和最新线的全部值
        CALC_CLOSE_WITH_LAST,//计算显示的线和最新线的close值
    }

    public enum YLabelModel {
        LABEL_WITH_GRID,
        LABEL_NONE_GRID
    }

    public enum YLabelAlign {
        LEFT,
        RIGHT
    }

}




