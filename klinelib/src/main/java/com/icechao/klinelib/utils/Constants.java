package com.icechao.klinelib.utils;


/*************************************************************************
 * Description   :
 *
 * @PackageName  : com.icechao.klinelib.utils
 * @FileName     : Constants.java
 * @Author       : chao
 * @Date         : 2019/4/10
 * @Email        : icechliu@gmail.com
 * @version      : V1
 *************************************************************************/
public class Constants {


    public static final long LONG_PRESS_TIME = 1000L;

    public static final String KDJ_TOP_TEXT_TAMPLATE = "KDJ(%s,%s,%s)  ";
    public static final int KDJ_K = 14;
    public static final int KDJ_D = 1;
    public static final int KDJ_J = 3;
    public static final String MACD_TOP_TEXT_TAMPLATE = "MACD(%s,%s,%s)  ";
    //"MACD(12,26,9)  ";
    public static final int MACD_S = 12;
    public static final int MACD_L = 26;
    public static final int MACD_M = 9;
    public static final String RSI_TOP_TEXT_TAMPLATE = "RSI(%s)  ";
    public static final int RSI_1 = 14;
    public static final String WR_TOP_TEXT_TEMPLATE = "WR(%s):";
    public static final int WR_1 = 14;


    private static int i = 0;
    public static final int INDEX_HIGH = i;
    public static final int INDEX_OPEN = ++i;
    public static final int INDEX_LOW = ++i;
    public static final int INDEX_CLOSE = ++i;
    public static final int INDEX_VOL = ++i;
    public static final int INDEX_MA_1 = ++i;
    public static final int INDEX_MA_2 = ++i;
    public static final int INDEX_MA_3 = ++i;
    public static final int INDEX_KDJ_K = ++i;
    public static final int INDEX_KDJ_D = ++i;
    public static final int INDEX_KDJ_J = ++i;
    public static final int INDEX_MACD_DIF = ++i;
    public static final int INDEX_MACD_DEA = ++i;
    public static final int INDEX_MACD_MACD = ++i;
    public static final int INDEX_BOLL_UP = ++i;
    public static final int INDEX_BOLL_MB = ++i;
    public static final int INDEX_BOLL_DN = ++i;
    public static final int INDEX_VOL_MA = ++i;
    public static final int INDEX_VOL_MA_1 = ++i;
    public static final int INDEX_VOL_MA_2 = ++i;
    public static final int INDEX_RSI_1 = ++i;
    public static final int INDEX_RSI_2 = ++i;
    public static final int INDEX_RSI_3 = ++i;
    public static final int INDEX_WR_1 = ++i;
    public static final int INDEX_WR_2 = ++i;
    public static final int INDEX_WR_3 = ++i;


    public static int getCount() {
        return INDEX_WR_3 + 1;
    }

}
