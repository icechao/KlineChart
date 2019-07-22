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


    public static String KDJ_TOP_TEXT_TAMPLATE = "KDJ(%s,%s,%s)  ";
    public static int KDJ_K = 14;
    public static int KDJ_D = 1;
    public static int KDJ_J = 3;

    public static void setKdj(int k, int d, int j) {
        KDJ_K = k;
        KDJ_D = d;
        KDJ_J = j;
    }

    public static String MACD_TOP_TEXT_TAMPLATE = "MACD(%s,%s,%s)  ";
    public static int MACD_S = 12;
    public static int MACD_L = 26;
    public static int MACD_M = 9;

    public static void setMacd(int s, int l, int m) {
        MACD_S = s;
        MACD_L = l;
        MACD_M = m;
    }

    public static String RSI_TOP_TEXT_TAMPLATE = "RSI(%s)  ";
    public static int RSI_1 = 14;

    public static void setRsi(int r) {
        RSI_1 = r;
    }

    public static String WR_TOP_TEXT_TEMPLATE = "WR(%s):";
    public static int WR_1 = 14;

    public static void setWr(int wr) {
        WR_1 = wr;
    }

    public static int K_MA_NUMBER_1 = 5;
    public static int K_MA_NUMBER_2 = 10;
    public static int K_MA_NUMBER_3 = 30;

    public static void setKlineMa(int ma1, int ma2, int ma3) {
        K_MA_NUMBER_1 = ma1;
        K_MA_NUMBER_2 = ma2;
        K_MA_NUMBER_3 = ma3;
    }

    public static int K_VOL_MA_NUMBER_1 = 5;
    public static int K_VOL_MA_NUMBER_2 = 10;

    public static void setVolMa(int ma1, int ma2) {
        K_VOL_MA_NUMBER_1 = ma1;
        K_VOL_MA_NUMBER_2 = ma2;
    }


    private static int i = 0;
    public static final int INDEX_DATE = i;
    public static final int INDEX_HIGH = ++i;
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
        return i;
    }

}
