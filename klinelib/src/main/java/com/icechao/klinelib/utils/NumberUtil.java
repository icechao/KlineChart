package com.icechao.klinelib.utils;

import android.text.Editable;
import android.text.TextUtils;


import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Locale;
import java.util.regex.Pattern;

/**
 * 数字相关的处理
 * Created by wangsai on 2017/5/9.
 */

public class NumberUtil {

    private static NumberFormat nf = NumberFormat.getNumberInstance(Locale.getDefault());
    public final static int INVALID_INT = Integer.MIN_VALUE;


    /**
     * 是否是数字
     *
     * @param str
     * @return
     */
    public static boolean isNumeric(String str) {
        if (str != null && !"".equals(str.trim())) {
            return str.matches("^(-?\\d+)(\\.\\d+)?$");
        }

        return false;
    }

    /**
     * <p>Checks whether the String a valid Java number.</p>
     * <p>
     * <p>Valid numbers include hexadecimal marked with the <code>0x</code> or
     * <code>0X</code> qualifier, octal numbers, scientific notation and
     * numbers marked with a type qualifier (e.g. 123L).</p>
     * <p>
     * <p>Non-hexadecimal strings beginning with a leading zero are
     * treated as octal values. Thus the string <code>09</code> will return
     * <code>false</code>, since <code>9</code> is not a valid octal value.
     * However, numbers beginning with {@code 0.} are treated as decimal.</p>
     * <p>
     * <p><code>null</code> and empty/blank {@code String} will return
     * <code>false</code>.</p>
     *
     * @param str the <code>String</code> to check
     * @return <code>true</code> if the string is a correctly formatted number
     * @since 3.5 the code supports the "+" suffix on numbers except for integers in Java 1.6
     */
    public static boolean isCreatable(final String str) {
        if (TextUtils.isEmpty(str)) {
            return false;
        }
        final char[] chars = str.toCharArray();
        int sz = chars.length;
        boolean hasExp = false;
        boolean hasDecPoint = false;
        boolean allowSigns = false;
        boolean foundDigit = false;
        // deal with any possible sign up front
        final int start = chars[0] == '-' || chars[0] == '+' ? 1 : 0;
        final boolean hasLeadingPlusSign = start == 1 && chars[0] == '+';
        if (sz > start + 1 && chars[start] == '0') { // leading 0
            if (chars[start + 1] == 'x' || chars[start + 1] == 'X') { // leading 0x/0X
                int i = start + 2;
                if (i == sz) {
                    return false; // str == "0x"
                }
                // checking hex (it can't be anything else)
                for (; i < chars.length; i++) {
                    if ((chars[i] < '0' || chars[i] > '9')
                            && (chars[i] < 'a' || chars[i] > 'f')
                            && (chars[i] < 'A' || chars[i] > 'F')) {
                        return false;
                    }
                }
                return true;
            } else if (Character.isDigit(chars[start + 1])) {
                // leading 0, but not hex, must be octal
                int i = start + 1;
                for (; i < chars.length; i++) {
                    if (chars[i] < '0' || chars[i] > '7') {
                        return false;
                    }
                }
                return true;
            }
        }
        sz--; // don't want to loop to the last char, check it afterwords
        // for type qualifiers
        int i = start;
        // loop to the next to last char or to the last char if we need another digit to
        // make a valid number (e.g. chars[0..5] = "1234E")
        while (i < sz || i < sz + 1 && allowSigns && !foundDigit) {
            if (chars[i] >= '0' && chars[i] <= '9') {
                foundDigit = true;
                allowSigns = false;

            } else if (chars[i] == '.') {
                if (hasDecPoint || hasExp) {
                    // two decimal points or dec in exponent
                    return false;
                }
                hasDecPoint = true;
            } else if (chars[i] == 'e' || chars[i] == 'E') {
                // we've already taken care of hex.
                if (hasExp) {
                    // two E's
                    return false;
                }
                if (!foundDigit) {
                    return false;
                }
                hasExp = true;
                allowSigns = true;
            } else if (chars[i] == '+' || chars[i] == '-') {
                if (!allowSigns) {
                    return false;
                }
                allowSigns = false;
                foundDigit = false; // we need a digit after the E
            } else {
                return false;
            }
            i++;
        }
        if (i < chars.length) {
            if (chars[i] >= '0' && chars[i] <= '9') {
                if (SystemUtils.IS_JAVA_1_6 && hasLeadingPlusSign && !hasDecPoint) {
                    return false;
                }
                // no type qualifier, OK
                return true;
            }
            if (chars[i] == 'e' || chars[i] == 'E') {
                // can't have an E at the last byte
                return false;
            }
            if (chars[i] == '.') {
                if (hasDecPoint || hasExp) {
                    // two decimal points or dec in exponent
                    return false;
                }
                // single trailing decimal point after non-exponent is ok
                return foundDigit;
            }
            if (!allowSigns
                    && (chars[i] == 'd'
                    || chars[i] == 'D'
                    || chars[i] == 'f'
                    || chars[i] == 'F')) {
                return foundDigit;
            }
            if (chars[i] == 'l'
                    || chars[i] == 'L') {
                // not allowing L with an exponent or decimal point
                return foundDigit && !hasExp && !hasDecPoint;
            }
            // last character is illegal
            return false;
        }
        // allowSigns is true iff the val ends in 'E'
        // found digit it to make sure weird stuff like '.' and '1E-' doesn't pass
        return !allowSigns && foundDigit;
    }

    /**
     * 是否是整数
     *
     * @param str
     * @return
     */
    public static boolean isInteger(String str) {
        Pattern pattern = Pattern.compile("^[-\\+]?[\\d]*$");
        return pattern.matcher(str).matches();
    }

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

    public static String roundFormatDown2(double num, int len) {
        String strTemp = "";
        try {
            return new BigDecimal(num).setScale(len, BigDecimal.ROUND_DOWN).toPlainString();
        } catch (Exception ex) {

            strTemp = "";
        }

        return strTemp;
    }

    /**
     * 规范小数  向下取整
     *
     * @param num
     * @param len
     * @return
     */
    public static String roundFormatDown(String num, int len) {
        if (!isCreatable(num))
            return num;

        return roundFormatDown(new BigDecimal(num), len);
    }

    /**
     * 规范小数 向上取整
     *
     * @param num
     * @param len
     * @return
     */
    public static String roundFormatUp(String num, int len) {
        String strTemp = "";
        if (!isNumeric(num))
            return strTemp;

        try {
            BigDecimal b = new BigDecimal(num);
            BigDecimal f1 = b.setScale(len, BigDecimal.ROUND_UP);
            strTemp = f1.toPlainString();
        } catch (Exception ex) {
            strTemp = "";
        }

        return strTemp;
    }

    public static String roundHalfUp(double num, int len) {
        String strTemp = "";
        try {
            BigDecimal b = new BigDecimal(num);
            BigDecimal f1 = b.setScale(len, BigDecimal.ROUND_HALF_UP);
            strTemp = f1.toPlainString();
        } catch (Exception ex) {
            strTemp = "";
        }

        return strTemp;
    }

    public static String roundFloor(String num, int len) {
        String strTemp = "";
        try {
            BigDecimal b = new BigDecimal(num);
            BigDecimal f1 = b.setScale(len, BigDecimal.ROUND_FLOOR);
            strTemp = f1.toPlainString();
        } catch (Exception ex) {
            strTemp = "";
        }

        return strTemp;
    }

    public static String roundFloor(BigDecimal num, int len) {
        String strTemp = "";
        try {
            BigDecimal f1 = num.setScale(len, BigDecimal.ROUND_FLOOR);
            strTemp = f1.toPlainString();
        } catch (Exception ex) {

            strTemp = "";
        }

        return strTemp;
    }


    public static String roundCeiling(String num, int len) {
        String strTemp = "";
        try {
            BigDecimal b = new BigDecimal(num);
            BigDecimal f1 = b.setScale(len, BigDecimal.ROUND_CEILING);
            strTemp = f1.toPlainString();
        } catch (Exception ex) {

            strTemp = "";
        }

        return strTemp;
    }

    public static String roundCeiling(BigDecimal num, int len) {
        String strTemp = "";
        try {
            BigDecimal f1 = num.setScale(len, BigDecimal.ROUND_CEILING);
            strTemp = f1.toPlainString();
        } catch (Exception ex) {

            strTemp = "";
        }

        return strTemp;
    }

    public static String roundHalfUp(String num, int len) {
        String strTemp = "";
        if (!isNumeric(num))
            return strTemp;

        try {
            BigDecimal b = new BigDecimal(num);
            BigDecimal f1 = b.setScale(len, BigDecimal.ROUND_HALF_UP);
            strTemp = f1.toPlainString();
        } catch (Exception ex) {

            strTemp = "";
        }

        return strTemp;
    }

    public static double doubleV(String num) {
        if (!isNumeric(num))
            return 0;

        return Double.valueOf(num);
    }

    public static BigDecimal bigDecimal(String num) {
        if (!isNumeric(num))
            return BigDecimal.ZERO;

        return new BigDecimal(num);
    }

    public static int intV(String num) {
        if (!isInteger(num))
            return 0;

        return Integer.valueOf(num);
    }

    public static long longV(String num) {
        if (!isInteger(num))
            return 0;

        return Long.valueOf(num);
    }

    public static float floatV(String num) {
        if (!isNumeric(num))
            return 0;

        return Float.valueOf(num);
    }


    public static String checkNumValid(Editable num, int floatPartLength) {
        if (num.length() > num.toString().trim().length()) {
            //有空格
            return num.toString().trim();
        }

        //没有输入
        if (num.toString().trim().length() == 0)
            return null;

        int dotPos = num.toString().indexOf(".");

        if (dotPos < 0) {
            return null;
        }

        if (dotPos == 0) {//小数点开头
            return num.delete(dotPos, dotPos + 1).toString();
        }

        if (num.length() - dotPos - 1 > floatPartLength) { //小数部分超长
            return num.delete(dotPos + floatPartLength + 1, dotPos + floatPartLength + 2).toString();
        }

        return null;
    }

    public static int getNumberDecimalDigits(String balance) {
        int dcimalDigits = -1;
        if (TextUtils.isEmpty(balance)) {
            return dcimalDigits;
        }
        String balanceStr = new BigDecimal(balance).toString();
        int indexOf = balanceStr.indexOf('.');
        if (indexOf > 0) {
            dcimalDigits = balanceStr.length() - 1 - indexOf;
        }
        return dcimalDigits;
    }

    /*
    按照期望的格式返回数据
    @param num:传入的数据
    @param totalLen：小数，小数点，整数部分的总和加起来最长长度
    @param maxFractionalLen: 小数部分的最长长度
     */

    public static String roundFormat(String num, int totalLen, int maxFractionalLen) {
        String result = "";
        if (TextUtils.isEmpty(num) || maxFractionalLen > totalLen) {
            return result;
        }

        int indexOf = num.indexOf('.');
        int size = totalLen - maxFractionalLen - 1;
        if (indexOf <= -1) {  //说明是整数
            int len = num.length();
            if (len >= totalLen) {
                return num;
            }
            size = totalLen - len - 1;
            if (size < maxFractionalLen) {
                result = roundFormatDown(num, size);
            } else {
                result = roundFormatDown(num, maxFractionalLen);
            }
        } else if (indexOf >= (totalLen - 1)) {//超过长度totalLen，变成整数
            result = roundFormatDown(num, 0);
        } else if (indexOf <= size) {//小数部分按照maxFractionalLen补齐
            result = roundFormatDown(num, maxFractionalLen);
        } else if (indexOf < (totalLen - 1)) {//小数部分按照totalLen补齐
            result = roundFormatDown(num, totalLen - 1 - indexOf);
        }


        return result;
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
            //判断除十亿后的数字长度是否大于4位
            if (tAmoutString.length() > 4) {
                String sub = tAmoutString.substring(0, 4);
                if (sub.endsWith(".")) {//如果截取前四位后的数值最后一位是"."，则只截取前三位
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
            //判断除百万后的数字长度是否大于4位
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

    public static int toInt(Object obj) {
        try {
            return Integer.parseInt(obj.toString());
        } catch (NumberFormatException e) {
            return -1;
        }
    }

    public static int toInt(byte[] bytes) {
        int result = 0;
        byte abyte;
        for (int i = 0; i < bytes.length; i++) {
            abyte = bytes[i];
            result += (abyte & 0xFF) << (8 * i);
        }
        return result;
    }

    public static int toShort(byte first, byte second) {
        return (first << 8) + (second & 0xFF);
    }

    public static long toLong(Object obj) {
        try {
            return Long.parseLong(obj.toString());
        } catch (NumberFormatException e) {
            return -1L;
        }
    }

    public static float toFloat(Object obj) {
        try {
            return Float.parseFloat(obj.toString());
        } catch (NumberFormatException e) {
            return -1f;
        }
    }

    /**
     * 四舍五入保留len位小数
     *
     * @param num
     * @param len
     * @return
     */
    static public String roundFormat(double num, int len) {
        String strTemp = "";

        try {
            BigDecimal b = new BigDecimal(String.valueOf(num));
            BigDecimal f1 = b.setScale(len, BigDecimal.ROUND_HALF_UP);
            strTemp = f1.toPlainString();
        } catch (Exception ex) {
            ex.printStackTrace();
            strTemp = "";
        }

        return strTemp;
    }

    /**
     * 四舍五入保留len位小数
     *
     * @param num
     * @param len
     * @return
     */
    static public String roundFormat(float num, int len) {
        String strTemp = "";

        try {
            BigDecimal b = new BigDecimal(String.valueOf(num));
            BigDecimal f1 = b.setScale(len, BigDecimal.ROUND_HALF_UP);
            strTemp = f1.toPlainString();
        } catch (Exception ex) {
            ex.printStackTrace();
            strTemp = "";
        }

        return strTemp;
    }

    /**
     * 保留len为小数 如果num 则返回""
     *
     * @param num
     * @param len
     * @return
     */
    static public String roundFormat(String num, int len) {
        String strTemp = "";

        try {
            if (num == null || num.equals("")) {
                return strTemp;
            }

            BigDecimal b = new BigDecimal(num);
            BigDecimal f1 = b.setScale(len, BigDecimal.ROUND_DOWN);
            strTemp = f1.toPlainString();
        } catch (Exception ex) {
            ex.printStackTrace();
            strTemp = "";
        }

        return strTemp;
    }

    /**
     * 保留len为小数 如果num 则返回 --
     *
     * @param num
     * @param len
     * @return
     */
    public static String roundFormatDefaultValue(String num, int len) {
        String strTemp = "--";

        try {
            if (TextUtils.isEmpty(num)) {
                return strTemp;
            }

            BigDecimal b = new BigDecimal(num);
            BigDecimal f1 = b.setScale(len, BigDecimal.ROUND_DOWN);
            strTemp = f1.toPlainString();
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return strTemp;
    }

    /**
     * 保留 len 位小数
     *
     * @param b
     * @param len
     * @return
     */
    static public String roundFormat(BigDecimal b, int len) {
        String strTemp = "";

        try {
            BigDecimal f1 = b.setScale(len, BigDecimal.ROUND_DOWN);
            strTemp = f1.toPlainString();
        } catch (Exception ex) {
            ex.printStackTrace();
            strTemp = "";
        }

        return strTemp;
    }

    /**
     * 保留 len 位小数
     *
     * @param b
     * @param len
     * @return
     */
    public static String roundFormatStripTrailingZeros(BigDecimal b, int len) {
        String strTemp = "";
        try {
            BigDecimal f1 = b.setScale(len, BigDecimal.ROUND_DOWN);
            f1 = f1.stripTrailingZeros();
            strTemp = f1.toPlainString();
        } catch (Exception ex) {
            ex.printStackTrace();
            strTemp = "";
        }
        return strTemp;
    }

    /**
     * 保留N位有效数字,返回值字符串
     *
     * @param value 原始数值
     * @param place 有效数字为数
     * @return
     */
    public static String decimalPlacesToStr(double value, int place) {
//        Assert.judge(place > 0, "有效位数需要>0");
        StringBuilder builder = new StringBuilder("#0.");
        for (int i = 0; i < place; i++) {
            builder.append("0");
        }

        DecimalFormat format = new DecimalFormat(builder.toString());
        return format.format(value);
    }

    /**
     * 用于数字截断，保留n位，如果有的话
     *
     * @param value
     * @param place
     * @return
     */
    public static float decimalPlaces(double value, int place) {
        int times = (int) Math.pow(10, place);
        return ((int) (value * times)) * 1f / times;
    }

    public static double convert(double value) {
        long l1 = Math.round(Math.rint(value) * 10); //四舍五入
        double ret = l1 / 10.0; //注意：使用 100.0 而不是 100
        return ret;
    }

    public static boolean safeNumForKline(double d) {
        return d != 0 && !Double.isNaN(d);
    }

    public static String roundSignFormatDown(double num, int len) {
        String sign = num > 0 ? "+" : "";
        return sign + roundFormatDown(new BigDecimal(num), len);
    }

    public static String formatSignPct(double ratio, int precisionNum) {
        String sign = ratio > 0 ? "+" : "";
        String unit = "%";
        String pctValue = roundFormatDown(ratio * 100, precisionNum);
        return sign + pctValue + unit;
    }

    public static String formatPct(double ratio, int precisionNum) {
        String unit = "%";
        String pctValue = roundFormatDown(ratio * 100, precisionNum);
        return pctValue + unit;
    }


    /**
     * 整型 带逗号不带小数点
     */
    public static String getIntegerCommaDotText(int num) {
        DecimalFormat df = new DecimalFormat("#,##0");
        return df.format(num);
    }

    /**
     * 浮点型 带逗号 两位小数
     */
    public static String getDoubleCommaDotText(double num) {
        DecimalFormat decimalFormat = new DecimalFormat("#,##0.00");
        return decimalFormat.format(num);

    }

    /**
     * 浮点型 带逗号 两位小数 抹零
     */
    public static String getDoubleCommaDotRemoveOText(double num) {
        DecimalFormat df = new DecimalFormat("#,##0.##");
        return df.format(num);
    }

    /**
     * 浮点型 不带逗号 两位小数
     */
    public static String getDoubleDotText(double num) {
        DecimalFormat df = new DecimalFormat("#0.00");
        return df.format(num);
    }

    /**
     * 保留两位小数
     */
    public static String decimalFormatRoundUp(double value) {
        DecimalFormat decimalFormat = new DecimalFormat("#0.00");
        return decimalFormat.format(value);
    }

    /**
     * 浮点型 不带逗号 一位小数
     */
    public static String getSingleDotText(float num) {
        DecimalFormat df = new DecimalFormat("#0.0");
        return df.format(num);
    }

    /**
     * 抹零保留小数
     */
    public static String getRemoveODec(double num) {
        DecimalFormat df = new DecimalFormat("#.##");
        return df.format(num);
    }

    /**
     * 规范小数  向下取整
     *
     * @param num
     * @param len
     * @return
     */
    public static String roundDotFormatDown(double num, int len) {
        return roundDotFormatDown(new BigDecimal(num), len);
    }

    /**
     * 向下取整 保留2位小数 并且带逗号 999,99.00
     *
     * @param num
     * @param len
     * @return
     */
    public static String roundDotFormatDown(BigDecimal num, int len) {
        String strTemp = "";
        try {
            String string = num.setScale(len + 2, BigDecimal.ROUND_HALF_UP).toPlainString();
            return getDoubleCommaDotText(new BigDecimal(string).setScale(len, BigDecimal.ROUND_DOWN).doubleValue());
        } catch (Exception ex) {

            strTemp = "";
        }

        return strTemp;
    }

    /**
     * 向下取整 保留2位小数 并且带逗号 999,99.00
     *
     * @param num
     * @param len
     * @return
     */
    public static String roundDotFormatDown2(double num, int len) {
        String strTemp = "";
        BigDecimal bigDecimal = new BigDecimal(num);
        try {
            return getDoubleCommaDotText(bigDecimal.setScale(len, BigDecimal.ROUND_DOWN).doubleValue());
        } catch (Exception ex) {

            strTemp = "";
        }

        return strTemp;
    }

    public static String formatNumber(double number, int decimalCount, boolean separateThousands) {
        try {
            if (decimalCount != INVALID_INT) {
                nf.setMinimumFractionDigits(decimalCount);
            }
            if (decimalCount != INVALID_INT) {
                nf.setMaximumFractionDigits(decimalCount);
            }
            nf.setRoundingMode(RoundingMode.DOWN);
            nf.setGroupingUsed(separateThousands);
            return nf.format(number);
        } catch (Exception ignored) {
            return "";
        }
    }

    public static int parseInt(String str) {
        if (str == null || str.length() == 0)
            return 0;

        try {
            return Integer.parseInt(str);
        } catch (NumberFormatException e) {


        }
        return 0;
    }

    public static long parseLong(String str) {
        if (str == null || str.length() == 0)
            return 0;

        try {
            return Long.parseLong(str);
        } catch (NumberFormatException e) {

        }
        return 0;
    }

    public static float parseFloat(String str) {
        if (str == null || str.length() == 0)
            return 0;
        try {
            return Float.parseFloat(str);
        } catch (NumberFormatException e) {


        }
        return 0;
    }

    public static Double parseDouble(String str) {
        if (str == null || str.length() == 0)
            return 0.0;

        try {
            return Double.parseDouble(str);
        } catch (NumberFormatException e) {


        }
        return 0.0;
    }


}
