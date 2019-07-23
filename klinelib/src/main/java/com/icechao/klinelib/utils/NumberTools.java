package com.icechao.klinelib.utils;

import android.text.TextUtils;

import java.math.BigDecimal;

/*************************************************************************
 * Description   :
 *
 * @PackageName  : com.icechao.klinelib.utils
 * @FileName     : NumberTools.java
 * @Author       : chao
 * @Date         : 2019/4/8
 * @Email        : icechliu@gmail.com
 * @version      : V1
 *************************************************************************/
public class NumberTools {


    /**
     * 规范小数  向下取整
     *
     * @param num
     * @param len
     * @return
     */
    public static String roundDown(double num, int len) {
        return roundDown(new BigDecimal(num), len);
    }

    public static String roundDown(BigDecimal num, int len) {
        String strTemp = "";
        try {
            String string = num.setScale(len + 2, BigDecimal.ROUND_HALF_UP).toPlainString();
            return new BigDecimal(string).setScale(len, BigDecimal.ROUND_DOWN).toPlainString();
        } catch (Exception ex) {
            strTemp = "";
        }

        return strTemp;
    }

    /**
     * 买卖盘的数量折合
     *
     * @param amount
     * @return
     */
    public static String formatAmount(String amount) {
        String result;
        if (TextUtils.isEmpty(amount)) {
            result = "0";
        } else {
            BigDecimal amountBigDecimal = new BigDecimal(amount);
            String tAmoutString = amountBigDecimal.toPlainString();
            if (new BigDecimal(amount).compareTo(BigDecimal.ONE) < 0) {

                if (tAmoutString.length() > 5) {
                    String temp = tAmoutString.substring(0, 5);
                    if (new BigDecimal(temp).compareTo(BigDecimal.ZERO) > 0) {
                        result = temp;
                    } else {
                        result = "0";
                    }
                } else {
                    result = tAmoutString;
                }
            } else if (amountBigDecimal.compareTo(new BigDecimal(1000)) < 0) {
                if (tAmoutString.length() > 5) {
                    result = tAmoutString.substring(0, 5);
                } else {
                    result = tAmoutString;
                }
            } else if (amountBigDecimal.compareTo(new BigDecimal(1000000)) < 0) {
                BigDecimal tAmountBigDecimal = amountBigDecimal.divide(new BigDecimal(1000));
                if (tAmoutString.length() > 4) {
                    String sub = tAmoutString.substring(0, 4);
                    if (sub.endsWith(".")) {//如果截取前四位后的数值最后一位是"."，则只截取前三位
                        result = tAmoutString.substring(0, 3) + "K";
                    } else {
                        result = tAmoutString.substring(0, 4) + "K";
                    }
                } else {
                    result = tAmountBigDecimal.toPlainString() + "K";
                }
            } else if (amountBigDecimal.compareTo(new BigDecimal(1000000000)) < 0) {

                BigDecimal tAmountBigDecimal = amountBigDecimal.divide(new BigDecimal(1000000));

                if (tAmoutString.length() > 4) {
                    String sub = tAmoutString.substring(0, 4);
                    if (sub.endsWith(".")) {
                        result = tAmoutString.substring(0, 3) + "M";
                    } else {
                        result = tAmoutString.substring(0, 4) + "M";
                    }
                } else {
                    result = tAmountBigDecimal.toPlainString() + "M";
                }
            } else {
                BigDecimal tAmountBigDecimal = amountBigDecimal.divide(new BigDecimal(1000000000));

                if (tAmoutString.length() > 4) {
                    String sub = tAmoutString.substring(0, 4);
                    if (sub.endsWith(".")) {
                        result = tAmoutString.substring(0, 3) + "B";
                    } else {
                        result = tAmoutString.substring(0, 4) + "B";
                    }
                } else {
                    result = tAmountBigDecimal.toPlainString() + "B";
                }
            }
        }

        return result;
    }

}
