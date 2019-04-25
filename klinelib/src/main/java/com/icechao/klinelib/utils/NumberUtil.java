package com.icechao.klinelib.utils;

import android.text.TextUtils;

import java.math.BigDecimal;


public class NumberUtil {


    /**
     * 规范小数  向下取整
     *
     * @param num
     * @param len
     * @return
     */
    public static String roundFormatDown(double num, int len) {
        return roundFormatDown(new BigDecimal(num), len);
    }

    public static String roundFormatDown(BigDecimal num, int len) {
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
    public static String getTradeMarketAmount(String amount) {
        String result = "";
        if (TextUtils.isEmpty(amount)) {
            return result;
        }
        BigDecimal amountBigDecimal = new BigDecimal(amount);

        if (amountBigDecimal.compareTo(new BigDecimal("1000000000")) >= 0) {
            BigDecimal tAmountBigDecimal = amountBigDecimal.divide(new BigDecimal(1000000000));
            String tAmoutString = tAmountBigDecimal.toPlainString();
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
        } else if (amountBigDecimal.compareTo(new BigDecimal("1000000")) >= 0
                && amountBigDecimal.compareTo(new BigDecimal("1000000000")) < 0) {
            BigDecimal tAmountBigDecimal = amountBigDecimal.divide(new BigDecimal(1000000));
            String tAmoutString = tAmountBigDecimal.toPlainString();
            if (tAmoutString.length() > 4) {
                String sub = tAmoutString.substring(0, 4);
                if (sub.endsWith(".")) {//如果截取前四位后的数值最后一位是"."，则只截取前三位
                    result = tAmoutString.substring(0, 3) + "M";
                } else {
                    result = tAmoutString.substring(0, 4) + "M";
                }
            } else {
                result = tAmountBigDecimal.toPlainString() + "M";
            }
        } else if (amountBigDecimal.compareTo(new BigDecimal("1000")) >= 0
                && amountBigDecimal.compareTo(new BigDecimal("1000000")) < 0) {
            BigDecimal tAmountBigDecimal = amountBigDecimal.divide(new BigDecimal(1000));
            String tAmoutString = tAmountBigDecimal.toPlainString();
            //判断除千后的数字长度是否大于4位
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
        } else if (amountBigDecimal.compareTo(new BigDecimal("1000")) < 0 && amountBigDecimal.compareTo(BigDecimal.ONE) >= 0) { //数量在1到1000之间
            String tAmoutString = amountBigDecimal.toPlainString();
            //超过5位直接截取前5位，否者直接返回
            if (tAmoutString.length() > 5) {
                result = tAmoutString.substring(0, 5);
            } else {
                result = tAmoutString;
            }
        } else {//小于1的数量
            String tAmoutString = amountBigDecimal.toPlainString();
            if (tAmoutString.length() > 5) {
                //如果
                if (new BigDecimal(tAmoutString.substring(0, 5)).compareTo(BigDecimal.ZERO) > 0) {
                    result = tAmoutString.substring(0, 5);
                } else {
                    result = "0";
                }
            } else {
                result = tAmoutString;
            }
        }
        return result;
    }


}
