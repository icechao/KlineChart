

package com.icechao.klinelib.utils;


import com.icechao.klinelib.entity.KLineEntity;

import java.util.List;

/*************************************************************************
 * Description   :
 *
 * @PackageName  : com.icechao.klinelib.utils
 * @FileName     : DataHelper.java
 * @Author       : chao
 * @Date         : 2019/4/8
 * @Email        : icechliu@gmail.com
 * @version      : V1
 *************************************************************************/
public class DataHelper {


    /**
     * 计算  重构:计算方法,过多循环~  把循环尽量放到一个循环种加快计算速度
     *
     * @param dataList
     */
    static float[] calculate(List<KLineEntity> dataList, float bollP, int bollN,
                             float priceMaOne, float priceMaTwo, float priceMaThree,
                             int s, int l, int m,
                             float maOne, float maTwo, float maThree,
                             int kdjDay, int rsi,
                             int wr1, int wr2, int wr3) {
        float maSum1 = 0;
        float maSum2 = 0;
        float maSum3 = 0;


        float volumeMaOne = 0;
        float volumeMaTwo = 0;


        float preEma12 = 0;
        float preEma26 = 0;

        float preDea = 0;

        int indexInterval = Constants.getCount();
        float[] points = new float[dataList.size() * indexInterval];


        int size = dataList.size();
        for (int i = 0; i < size; i++) {
            KLineEntity point = dataList.get(i);
            float closePrice = point.getClosePrice();


            points[indexInterval * i + Constants.INDEX_RSI_1] = Float.MIN_VALUE;
            points[indexInterval * i + Constants.INDEX_DATE] = point.high;
            points[indexInterval * i + Constants.INDEX_HIGH] = point.high;
            points[indexInterval * i + Constants.INDEX_OPEN] = point.open;
            points[indexInterval * i + Constants.INDEX_LOW] = point.low;
            points[indexInterval * i + Constants.INDEX_CLOSE] = point.close;
            points[indexInterval * i + Constants.INDEX_VOL] = point.volume;


            //ma计算
            maSum1 += closePrice;
            maSum2 += closePrice;
            maSum3 += closePrice;

            if (i == priceMaOne - 1) {
                point.maOne = maSum1 / priceMaOne;
            } else if (i >= priceMaOne) {
                maSum1 -= dataList.get((int) (i - priceMaOne)).getClosePrice();
                point.maOne = maSum1 / priceMaOne;
            } else {
                point.maOne = Float.MIN_VALUE;
            }
            points[indexInterval * i + Constants.INDEX_MA_1] = point.maOne;

            if (i == priceMaTwo - 1) {
                point.maTwo = maSum2 / priceMaTwo;
            } else if (i >= priceMaTwo) {
                maSum2 -= dataList.get((int) (i - priceMaTwo)).getClosePrice();
                point.maTwo = maSum2 / priceMaTwo;
            } else {
                point.maTwo = Float.MIN_VALUE;
            }
            points[indexInterval * i + Constants.INDEX_MA_2] = point.maTwo;

            if (i == priceMaThree - 1) {
                point.maThree = maSum3 / priceMaThree;
            } else if (i >= priceMaThree) {
                maSum3 -= dataList.get((int) (i - priceMaThree)).getClosePrice();
                point.maThree = maSum3 / priceMaThree;
            } else {
                point.maThree = Float.MIN_VALUE;
            }
            points[indexInterval * i + Constants.INDEX_MA_3] = point.maThree;


            //macd
            if (s > 0 && l > 0 && m > 0) {
                if (size >= m + l - 2) {
                    if (i < l - 1) {
                        point.dif = 0;
                    }

                    if (i >= s - 1) {
                        float ema12 = calculateEMA(dataList, s, i, preEma12);
                        preEma12 = ema12;
                        if (i >= l - 1) {
                            float ema26 = calculateEMA(dataList, l, i, preEma26);
                            preEma26 = ema26;
                            point.dif = (ema12 - ema26);
                        } else {
                            point.dif = Float.MIN_VALUE;
                        }
                    } else {
                        point.dif = Float.MIN_VALUE;
                    }

                    if (i >= m + l - 2) {
                        boolean isFirst = i == m + l - 2;
                        float dea = calculateDEA(dataList, l, m, i, preDea, isFirst);
                        preDea = dea;
                        point.dea = (dea);
                    } else {
                        point.dea = Float.MIN_VALUE;
                    }

                    if (i >= m + l - 2) {
                        point.macd = point.getDif() - point.getDea();
                    } else {
                        point.macd = 0;
                    }

                } else {
                    point.macd = 0;
                }
            }
            points[indexInterval * i + Constants.INDEX_MACD_DIF] = point.dif;
            points[indexInterval * i + Constants.INDEX_MACD_MACD] = point.macd;
            points[indexInterval * i + Constants.INDEX_MACD_DEA] = point.dea;


            //boll计算
            if (i >= bollN - 1) {
                float boll = calculateBoll(dataList, i, bollN);
                float highBoll = boll + bollP * STD(dataList, i, bollN);
                float lowBoll = boll - bollP * STD(dataList, i, bollN);
                point.up = highBoll;
                point.mb = boll;
                point.dn = lowBoll;
            } else {
                point.up = Float.MIN_VALUE;
                point.mb = Float.MIN_VALUE;
                point.dn = Float.MIN_VALUE;
            }

            points[indexInterval * i + Constants.INDEX_BOLL_UP] = point.up;
            points[indexInterval * i + Constants.INDEX_BOLL_MB] = point.mb;
            points[indexInterval * i + Constants.INDEX_BOLL_DN] = point.dn;


            //vol ma计算
            volumeMaOne += point.getVolume();
            volumeMaTwo += point.getVolume();
            float ma;
            if (i == maOne - 1) {
                ma = (volumeMaOne / maOne);
            } else if (i > maOne - 1) {
                volumeMaOne -= dataList.get((int) (i - maOne)).getVolume();
                ma = volumeMaOne / maOne;
            } else {
                ma = Float.MIN_VALUE;
            }
            point.MA5Volume = ma;
            points[indexInterval * i + Constants.INDEX_VOL_MA_1] = point.MA5Volume;

            if (i == maTwo - 1) {
                ma = (volumeMaTwo / maTwo);
            } else if (i > maTwo - 1) {
                volumeMaTwo -= dataList.get((int) (i - maTwo)).getVolume();
                ma = volumeMaTwo / maTwo;
            } else {
                ma = Float.MIN_VALUE;
            }
            point.MA10Volume = ma;
            points[indexInterval * i + Constants.INDEX_VOL_MA_2] = point.MA10Volume;

//            if (i == maThree - 1) {
//                ma = (volumeMaThree / maThree);
//            } else if (i > maThree - 1) {
//                volumeMaThree -= dataList.get((int) (i - maThree)).getVolume();
//                ma = volumeMaThree / maThree;
//            } else {
//                ma = Float.MIN_VALUE;
//            }
//            point.MAVolume = ma;

            //kdj
            calcKdj(dataList, kdjDay, i, point, closePrice);
            points[indexInterval * i + Constants.INDEX_KDJ_K] = point.k;
            points[indexInterval * i + Constants.INDEX_KDJ_D] = point.d;
            points[indexInterval * i + Constants.INDEX_KDJ_J] = point.j;

            //计算3个 wr指标
            point.wrOne = getValueWR(dataList, wr1, i);
            point.wrTwo = getValueWR(dataList, wr2, i);
            point.wrThree = getValueWR(dataList, wr3, i);
            points[indexInterval * i + Constants.INDEX_WR_1] = (float) point.wrOne;


//           以每一日的收盘价减去上一日的收盘价，得到14个数值，
//           这些数值有正有负。这样，RSI指标的计算公式具体如下：
//           A=14个数字中正数之和
//　　        B=14个数字中负数之和乘以(—1)
//　　        RSI(14)=A÷(A＋B)×100
            if (rsi <= i) {
                double a = 0, b = 0;
                for (int j = i; j >= i - 14; j--) {
                    float temp = points[indexInterval * j + Constants.INDEX_CLOSE] - points[indexInterval * j + Constants.INDEX_OPEN];
                    if (temp > 0) {
                        a += temp;
                    } else {
                        b += temp;
                    }
                }
                points[indexInterval * i + Constants.INDEX_RSI_1] = (float) (a / (a - b) * 100d);
            } else {
                points[indexInterval * i + Constants.INDEX_RSI_1] = Float.MIN_VALUE;
            }

        }

        return points;
    }

    private static float getValueWR(List<KLineEntity> dataList, int wr1, int i) {
        float valueWR;
        if (wr1 != 0 && i >= wr1) {
            valueWR = -calcWr(dataList, i, wr1);
        } else {
            valueWR = Float.MIN_VALUE;
        }
        return valueWR;
    }

    private static void calcKdj(List<KLineEntity> dataList, int kdjDay, int i, KLineEntity point, float closePrice) {
        float k;
        float d;
        if (i < kdjDay - 1 || 0 == i) {
            point.k = Float.MIN_VALUE;
            point.d = Float.MIN_VALUE;
            point.j = Float.MIN_VALUE;
        } else {
            int startIndex = i - kdjDay + 1;
            float maxRsi = Float.MIN_VALUE;
            float minRsi = Float.MAX_VALUE;
            for (int index = startIndex; index <= i; index++) {
                maxRsi = Math.max(maxRsi, dataList.get(index).getHighPrice());
                minRsi = Math.min(minRsi, dataList.get(index).getLowPrice());
            }
            float rsv;
            try {
                rsv = 100f * (closePrice - minRsi) / (maxRsi - minRsi);
            } catch (Exception e) {
                rsv = 0f;
            }
            KLineEntity kLineEntity = dataList.get(i - 1);
            float k1 = kLineEntity.getK();
            k = 2f / 3f * (k1 == Float.MIN_VALUE ? 50 : k1) + 1f / 3f * rsv;
            float d1 = kLineEntity.getD();
            d = 2f / 3f * (d1 == Float.MIN_VALUE ? 50 : d1) + 1f / 3f * k;
            point.k = k;
            point.d = d;
            point.j = 3f * k - 2 * d;
        }
    }

    /**
     * 计算MA BOLL RSI KDJ MACD
     *
     * @param dataList
     */
    public static float[] calculate(List<KLineEntity> dataList) {
        float[] points = calculate(dataList, 2, 20,
                Constants.K_MA_NUMBER_1, Constants.K_MA_NUMBER_2, Constants.K_MA_NUMBER_3,
                Constants.MACD_S, Constants.MACD_L, Constants.MACD_M,
                Constants.K_MA_NUMBER_1, Constants.K_MA_NUMBER_2, Constants.K_MA_NUMBER_3,
                Constants.KDJ_K, Constants.RSI_1,
                Constants.WR_1, 0, 0);


//        for (int i = 0; i < dataList.size(); i++) {
//            LogUtil.e(points[Constants.getCount() * i + Constants.INDEX_RSI_1]);
//        }

        return points;
    }


    /**
     * 计算ema指标
     *
     * @param n      s或l
     * @param index  index
     * @param preEma 上一个ema
     * @return ema
     */
    private static float calculateEMA(List<KLineEntity> list, int n, int index, float preEma) {
        float y = 0;
        try {
            if (index + 1 < n) {
                return y;
            } else if (index + 1 == n) {
                for (int i = 0; i < n; i++) {
                    y += list.get(i).close;
                }
                return y / n;
            } else {
                return (preEma * (n - 1) + list.get(index).close * 2) / (n + 1);
            }
        } catch (Exception e) {
            return y;
        }
    }

    /**
     * 计算DEA
     *
     * @param list    列表
     * @param l       L26
     * @param m       M9
     * @param index   index
     * @param preDea  上一个dea
     * @param isFirst 是否是第一个
     * @return dea
     */
    private static float calculateDEA(List<KLineEntity> list, int l, int m, int index,
                                      float preDea,
                                      boolean isFirst) {
        float y = 0;
        try {
            if (isFirst) {
                for (int i = l - 1; i <= m + l - 2; i++) {
                    y += list.get(i).getDif();
                }
                return y / m;
            } else {
                return ((preDea * (m - 1) + list.get(index).getDif() * 2) / (m + 1));
            }
        } catch (Exception e) {
            return y;
        }
    }


    private static int findStartIndex(List<KLineEntity> klineInfos) {
        int startIndex = 0;
        for (int i = klineInfos.size() - 1; i > 0; i--) {
            double maValue = klineInfos.get(i).rOne;
            if (maValue != 0 && maValue != Float.MIN_VALUE) {
                startIndex = i + 1;
                break;
            }
        }
        return startIndex;
    }


    public static float calcWr(List<KLineEntity> dataDiction, int nIndex, int n) {
        float result = 0;
        float lowInNLowsValue = getLowestOfArray(dataDiction, nIndex, n);   //N日内最低价的最低值
        float highInHighsValue = getHighestOfArray(dataDiction, nIndex, n);   //N日内最低价的最低值
        float valueSpan = highInHighsValue - lowInNLowsValue;
        if (valueSpan > 0) {
            KLineEntity kLineData = dataDiction.get(nIndex);
            result = 100 * (highInHighsValue - kLineData.close) / valueSpan;
        } else
            result = 0;

        return result;
    }


    public static float getLowestOfArray(List<KLineEntity> valuesArray, int fromIndex, int nCount) {
        float result = Float.MAX_VALUE;
        int endIndex = fromIndex - (nCount - 1);
        if (fromIndex >= endIndex) {
            for (int itemIndex = fromIndex + 1; itemIndex > endIndex; itemIndex--) {
                KLineEntity klineData = valuesArray.get(itemIndex - 1);
                float lowPrice = klineData.low;
                result = result <= lowPrice ? result : lowPrice;
            }
        }

        return result;
    }


    public static float getHighestOfArray(List<KLineEntity> valuesArray,
                                          int fromIndex, int nCount) {
        float result = Float.MIN_VALUE;
        int endIndex = fromIndex - (nCount - 1);
        if (fromIndex >= endIndex) {
            for (int itemIndex = fromIndex + 1; itemIndex > endIndex; itemIndex--) {
                KLineEntity klineData = valuesArray.get(itemIndex - 1);
                float highPrice = klineData.high;
                result = result >= highPrice ? result : highPrice;
            }
        }
        return result;
    }

    public static void calculateBoLL(List<KLineEntity> klineInfos, int n, int p) {
        if (n == 0 || p == 0) {
            return;
        }
        if (klineInfos.size() < n) {
            return;
        }

        for (int i = n - 1; i < klineInfos.size(); i++) {
            float boll = calculateBoll(klineInfos, i, n);
            float highBoll = boll + p * STD(klineInfos, i, n);
            float lowBoll = boll - p * STD(klineInfos, i, n);
            klineInfos.get(i).up = (highBoll);
            klineInfos.get(i).mb = (boll);
            klineInfos.get(i).dn = (lowBoll);
        }
    }


    /**
     * 布林线指标计算
     */
    private static float calculateBoll(List<KLineEntity> payloads, int index, int maN) {
        float sum = 0;
        for (int i = index; i >= index - maN + 1; i--) {
            sum = (sum + payloads.get(i).close);
        }
        return sum / maN;

    }

    private static float STD(List<KLineEntity> payloads, int index, int maN) {

        float sum = 0f;
        float std = 0f;
        for (int i = index; i >= index - maN + 1; i--) {
            sum += payloads.get(i).close;
        }
        float avg = sum / maN;
        //float avg = payloads.get(index).bollMa;
        //不能提前计算,会出现0.001的误差
        for (int i = index; i >= index - maN + 1; i--) {
            std += (payloads.get(i).close - avg) * (payloads.get(i).close - avg);
        }
        return (float) Math.sqrt(std / maN);
    }


}
