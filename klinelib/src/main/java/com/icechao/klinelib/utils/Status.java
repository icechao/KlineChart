package com.icechao.klinelib.utils;

import android.support.annotation.IntDef;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

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


    public static final int MAIN_MA = 1001;
    public static final int MAIN_BOLL = 1002;
    public static final int MAIN_NONE = 1003;


    /**
     * 当前主图状态
     */
    @Documented
    @IntDef({Status.MAIN_MA, Status.MAIN_BOLL, Status.MAIN_NONE})
    @Target({ElementType.PARAMETER, ElementType.FIELD, ElementType.METHOD,})
    @Retention(RetentionPolicy.SOURCE)
    public @interface MainStatus {
    }


    public static final int TOUCH_FOLLOW_FINGERS = 2001;//跟随手指移动
    public static final int TOUCH_SHOW_CLOSE = 2002; //y值显示当前价格
    /**
     * 点击显示模式
     */
    @Documented
    @IntDef({Status.TOUCH_FOLLOW_FINGERS, Status.TOUCH_SHOW_CLOSE})
    @Target({ElementType.PARAMETER, ElementType.FIELD, ElementType.METHOD})
    @Retention(RetentionPolicy.SOURCE)
    public @interface TouchModel {
    }


    public static final int MAIN_ONLY = 3001; //显示主图交易量时隐藏交易量
    public static final int MAIN_VOL = 3002; //显示主图时显示交易量
    public static final int MAIN_INDEX = 3003; //显示主图交易量指标图时隐藏交易量
    public static final int MAIN_VOL_INDEX = 3004;//显示主图指标图时显示交易量
    /**
     * 点击显示模式
     */
    @Documented
    @IntDef({Status.MAIN_ONLY, Status.MAIN_VOL, Status.MAIN_INDEX, Status.MAIN_VOL_INDEX})
    @Target({ElementType.PARAMETER, ElementType.FIELD, ElementType.METHOD})
    @Retention(RetentionPolicy.SOURCE)
    public @interface ChildStatus {
    }


    public static final int INDEX_NONE = 4001;
    public static final int INDEX_MACD = 4002;
    public static final int INDEX_KDJ = 4003;
    public static final int INDEX_RSI = 4004;
    public static final int INDEX_WR = 4005;
    public static final int INDEX_EMA = 4006;

    @Documented
    @IntDef({INDEX_NONE, INDEX_MACD, INDEX_KDJ, INDEX_RSI, INDEX_WR, INDEX_EMA})
    @Target({ElementType.FIELD, ElementType.METHOD, ElementType.PARAMETER})
    @Retention(RetentionPolicy.SOURCE)
    public @interface IndexStatus {
    }



    public static final int VOL_SHOW_BAR_CHART = 4100;
    public static final int VOL_SHOW_VERTICAL_BAR = 4101;
    public static final int KLINE_SHOW_TIME_LINE = 4102;
    public static final int K_LINE_SHOW_CANDLE_LINE = 4104;

    /**
     * 成交里显示模式
     */
    @Documented
    @IntDef({VOL_SHOW_BAR_CHART, VOL_SHOW_VERTICAL_BAR})
    @Target({ElementType.FIELD, ElementType.METHOD, ElementType.PARAMETER})
    @Retention(RetentionPolicy.SOURCE)
    public @interface VolChartStatus {
    }

    /**
     * K线显示类型
     */
    @Documented
    @IntDef({KLINE_SHOW_TIME_LINE, K_LINE_SHOW_CANDLE_LINE})
    @Target({ElementType.FIELD, ElementType.METHOD, ElementType.PARAMETER})
    @Retention(RetentionPolicy.SOURCE)
    public @interface  KlineStatus {
    }

    public static final int SELECT_TOUCHE = 5001;//touch modle
    public static final int SELECT_PRESS = 5002;//long press modle
    public static final int SELECT_BOTH = 5003;//both modle
    public static final int SELECT_NONE = 5004;// can't touch

    /**
     * 点击模式
     */
    @Documented
    @IntDef({SELECT_TOUCHE, SELECT_PRESS, SELECT_BOTH, SELECT_NONE})
    @Target({ElementType.FIELD, ElementType.METHOD, ElementType.PARAMETER})
    @Retention(RetentionPolicy.SOURCE)
    public @interface ShowCrossModel {
    }

    public static final int NONE_HOLLOW = 6001;//全实心
    public static final int ALL_HOLLOW = 6002; //全空心
    public static final int DECREASE_HOLLOW = 6003;//涨空心
    public static final int INCREASE_HOLLOW = 6004;//跌实心

    /**
     * 空心模式
     */
    @Documented
    @IntDef({NONE_HOLLOW, ALL_HOLLOW, DECREASE_HOLLOW, INCREASE_HOLLOW})
    @Target({ElementType.PARAMETER, ElementType.FIELD, ElementType.METHOD})
    @Retention(RetentionPolicy.SOURCE)
    public @interface HollowModel {

    }

    public static final int CALC_NORMAL_WITH_SHOW = 7001;//计算显示的线全部值
    public static final int CALC_CLOSE_WITH_SHOW = 7002;//计算显示的线的close值
    public static final int CALC_NORMAL_WITH_LAST = 7003;//计算显示的线和最新线的全部值
    public static final int CALC_CLOSE_WITH_LAST = 7004;


    /**
     * Y轴最大最小值计算模式
     */
    @Documented
    @IntDef({CALC_NORMAL_WITH_SHOW, CALC_CLOSE_WITH_SHOW, CALC_NORMAL_WITH_LAST, CALC_CLOSE_WITH_LAST})
    @Target({ElementType.PARAMETER, ElementType.FIELD, ElementType.METHOD})
    @Retention(RetentionPolicy.SOURCE)
    public @interface MaxMinCalcModel {
    }

    public static final int LABEL_WITH_GRID = 8001;
    public static final int LABEL_NONE_GRID = 8002;

    /**
     * Y轴坐标显示模式
     */
    @Documented
    @IntDef({LABEL_WITH_GRID, LABEL_NONE_GRID})
    @Target({ElementType.PARAMETER, ElementType.FIELD, ElementType.METHOD})
    @Retention(RetentionPolicy.SOURCE)
    public @interface YLabelShowModel {
    }

    public static final int LEFT = 9001;
    public static final int TOP = 9002;
    public static final int RIGHT = 9003;
    public static final int BOTTOM = 9004;
    public static final int CENTER = 9005;


    /**
     * 位置
     */
    @Documented
    @IntDef({LEFT, TOP, RIGHT, BOTTOM, CENTER})
    @Target({ElementType.PARAMETER, ElementType.FIELD, ElementType.METHOD})
    @Retention(RetentionPolicy.SOURCE)
    public @interface Align {
    }


}




