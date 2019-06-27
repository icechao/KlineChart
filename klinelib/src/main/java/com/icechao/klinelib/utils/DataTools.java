

package com.icechao.klinelib.utils;


import com.icechao.klinelib.entity.KLineEntity;

import java.util.List;

/*************************************************************************
 * Description   :
 *
 * @PackageName  : com.icechao.klinelib.utils
 * @FileName     : DataTools.java
 * @Author       : chao
 * @Date         : 2019/4/8
 * @Email        : icechliu@gmail.com
 * @version      : V1
 *************************************************************************/
public class DataTools {


    /**
     * 计算MA BOLL RSI KDJ MACD
     *
     * @param dataList
     */
    public static float[] calculate(List<? extends KLineEntity> dataList) {
        float[] points = calculate(dataList, 2, 20,
                Constants.K_MA_NUMBER_1, Constants.K_MA_NUMBER_2, Constants.K_MA_NUMBER_3,
                Constants.MACD_S, Constants.MACD_L, Constants.MACD_M,
                Constants.K_VOL_MA_NUMBER_1, Constants.K_VOL_MA_NUMBER_2,
                Constants.KDJ_K, Constants.RSI_1,
                Constants.WR_1, 0, 0);
        return points;
    }


    /**
     * 计算
     *
     * @param dataList
     * @param bollP
     * @param bollN
     * @param priceMaOne
     * @param priceMaTwo
     * @param priceMaThree
     * @param s
     * @param l
     * @param m
     * @param maOne
     * @param maTwo
     * @param kdjDay
     * @param rsi
     * @param wr1
     * @param wr2
     * @param wr3
     * @return
     */
    static float[] calculate(List<? extends KLineEntity> dataList, float bollP, int bollN,
                             double priceMaOne, double priceMaTwo, double priceMaThree,
                             int s, int l, int m,
                             double maOne, double maTwo,
                             int kdjDay, int rsi,
                             int wr1, int wr2, int wr3) {
        double maSum1 = 0;
        double maSum2 = 0;
        double maSum3 = 0;


        double volumeMaOne = 0;
        double volumeMaTwo = 0;


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
//            points[indexInterval * i + Constants.INDEX_DATE] = point.getHighPrice();
            points[indexInterval * i + Constants.INDEX_HIGH] = point.getHighPrice();
            points[indexInterval * i + Constants.INDEX_OPEN] = point.getOpenPrice();
            points[indexInterval * i + Constants.INDEX_LOW] = point.getLowPrice();
            points[indexInterval * i + Constants.INDEX_CLOSE] = point.getClosePrice();
            points[indexInterval * i + Constants.INDEX_VOL] = point.getVolume();


            //ma计算
            maSum1 += closePrice;
            maSum2 += closePrice;
            maSum3 += closePrice;

            if (i == priceMaOne - 1) {
                point.setMaOne((float) (maSum1 / priceMaOne));
            } else if (i >= priceMaOne) {
                maSum1 -= dataList.get((int) (i - priceMaOne)).getClosePrice();
                point.setMaOne((float) (maSum1 / priceMaOne));
            } else {
                point.setMaOne(Float.MIN_VALUE);
            }
            points[indexInterval * i + Constants.INDEX_MA_1] = point.getMaOne();

            if (i == priceMaTwo - 1) {
                point.setMaTwo((float) (maSum2 / priceMaTwo));
            } else if (i >= priceMaTwo) {
                maSum2 -= dataList.get((int) (i - priceMaTwo)).getClosePrice();
                point.setMaTwo((float) (maSum2 / priceMaTwo));
            } else {
                point.setMaTwo(Float.MIN_VALUE);
            }
            points[indexInterval * i + Constants.INDEX_MA_2] = point.getMaTwo();

            if (i == priceMaThree - 1) {
                point.setMaThree((float) (maSum3 / priceMaThree));
            } else if (i >= priceMaThree) {
                maSum3 -= dataList.get((int) (i - priceMaThree)).getClosePrice();
                point.setMaThree((float) (maSum3 / priceMaThree));
            } else {
                point.setMaThree(Float.MIN_VALUE);
            }
            points[indexInterval * i + Constants.INDEX_MA_3] = point.getMaThree();


            //macd
            if (s > 0 && l > 0 && m > 0) {
                if (size >= m + l - 2) {
                    if (i < l - 1) {
                        point.setDif(0);
                    }

                    if (i >= s - 1) {
                        float ema12 = calculateEMA(dataList, s, i, preEma12);
                        preEma12 = ema12;
                        if (i >= l - 1) {
                            float ema26 = calculateEMA(dataList, l, i, preEma26);
                            preEma26 = ema26;
                            point.setDif(ema12 - ema26);
                        } else {
                            point.setDif(Float.MIN_VALUE);
                        }
                    } else {
                        point.setDif(Float.MIN_VALUE);
                    }

                    if (i >= m + l - 2) {
                        boolean isFirst = i == m + l - 2;
                        float dea = calculateDEA(dataList, l, m, i, preDea, isFirst);
                        preDea = dea;
                        point.setDea(dea);
                    } else {
                        point.setDea(Float.MIN_VALUE);
                    }

                    if (i >= m + l - 2) {
                        point.setMacd(point.getDif() - point.getDea());
                    } else {
                        point.setMacd(0);
                    }

                } else {
                    point.setMacd(0);
                }
            }
            points[indexInterval * i + Constants.INDEX_MACD_DIF] = point.getDif();
            points[indexInterval * i + Constants.INDEX_MACD_MACD] = point.getMacd();
            points[indexInterval * i + Constants.INDEX_MACD_DEA] = point.getDea();


            //boll计算
            if (i >= bollN - 1) {
                float boll = calculateBoll(dataList, i, bollN);
                float highBoll = boll + bollP * STD(dataList, i, bollN);
                float lowBoll = boll - bollP * STD(dataList, i, bollN);
                point.setUp(highBoll);
                point.setMb(boll);
                point.setDn(lowBoll);
            } else {
                point.setUp(Float.MIN_VALUE);
                point.setMb(Float.MIN_VALUE);
                point.setDn(Float.MIN_VALUE);
            }

            points[indexInterval * i + Constants.INDEX_BOLL_UP] = point.getUp();
            points[indexInterval * i + Constants.INDEX_BOLL_MB] = point.getMb();
            points[indexInterval * i + Constants.INDEX_BOLL_DN] = point.getDn();


            //vol ma计算
            volumeMaOne += point.getVolume();
            volumeMaTwo += point.getVolume();
            float ma;
            if (i == maOne - 1) {
                ma = (float) (volumeMaOne / maOne);
            } else if (i > maOne - 1) {
                volumeMaOne -= dataList.get((int) (i - maOne)).getVolume();
                ma = (float) (volumeMaOne / maOne);
            } else {
                ma = Float.MIN_VALUE;
            }
            point.setMA5Volume(ma);
            points[indexInterval * i + Constants.INDEX_VOL_MA_1] = point.getMA5Volume();

            if (i == maTwo - 1) {
                ma = (float) (volumeMaTwo / maTwo);
            } else if (i > maTwo - 1) {
                volumeMaTwo -= dataList.get((int) (i - maTwo)).getVolume();
                ma = (float) (volumeMaTwo / maTwo);
            } else {
                ma = Float.MIN_VALUE;
            }
            point.setMA10Volume(ma);
            points[indexInterval * i + Constants.INDEX_VOL_MA_2] = point.getMA10Volume();

            //kdj
            calcKdj(dataList, kdjDay, i, point, closePrice);
            points[indexInterval * i + Constants.INDEX_KDJ_K] = point.getK();
            points[indexInterval * i + Constants.INDEX_KDJ_D] = point.getD();
            points[indexInterval * i + Constants.INDEX_KDJ_J] = point.getJ();

            //计算3个 wr指标
            point.setWrOne(getValueWR(dataList, wr1, i));
            point.setWrTwo(getValueWR(dataList, wr2, i));
            point.setWrThree(getValueWR(dataList, wr3, i));
            points[indexInterval * i + Constants.INDEX_WR_1] = (float) point.getWrOne();


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

    private static float getValueWR(List<? extends KLineEntity> dataList, int wr1, int i) {
        float valueWR;
        if (wr1 != 0 && i >= wr1 - 1) {
            valueWR = -calcWr(dataList, i, wr1);
        } else {
            valueWR = Float.MIN_VALUE;
        }
        return valueWR;
    }

    private static void calcKdj(List<? extends KLineEntity> dataList, int kdjDay, int i, KLineEntity point, float closePrice) {
        float k, d;
        if (i < kdjDay - 1 || 0 == i) {
            point.setK(Float.MIN_VALUE);
            point.setD(Float.MIN_VALUE);
            point.setJ(Float.MIN_VALUE);
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
            point.setK(k);
            point.setD(d);
            point.setJ(3f * k - 2 * d);
        }
    }

    private static float calculateEMA(List<? extends KLineEntity> list, int n, int index, float preEma) {
        float y = 0;
        try {
            if (index + 1 < n) {
                return y;
            } else if (index + 1 == n) {
                for (int i = 0; i < n; i++) {
                    y += list.get(i).getClosePrice();
                }
                return y / n;
            } else {
                return (preEma * (n - 1) + list.get(index).getClosePrice() * 2) / (n + 1);
            }
        } catch (Exception e) {
            return y;
        }
    }


    private static float calculateDEA(List<? extends KLineEntity> list, int l, int m, int index,
                                      float preDea,
                                      boolean isFirst) {

        try {
            float y = 0;
            if (isFirst) {
                for (int i = l - 1; i <= m + l - 2; i++) {
                    y += list.get(i).getDif();
                }
                return y / m;
            } else {
                return ((preDea * (m - 1) + list.get(index).getDif() * 2) / (m + 1));
            }
        } catch (Exception e) {
            return 0;
        }
    }


    public static float calcWr(List<? extends KLineEntity> dataDiction, int nIndex, int n) {
        float result;
        float lowInNLowsValue = getMin(dataDiction, nIndex, n);   //N日内最低价的最低值
        float highInHighsValue = getMax(dataDiction, nIndex, n);   //N日内最低价的最低值
        float valueSpan = highInHighsValue - lowInNLowsValue;
        if (valueSpan > 0) {
            KLineEntity kLineData = dataDiction.get(nIndex);
            result = 100 * (highInHighsValue - kLineData.getClosePrice()) / valueSpan;
        } else
            result = 0;

        return result;
    }


    public static float getMin(List<? extends KLineEntity> valuesArray, int fromIndex, int nCount) {
        float result = Float.MAX_VALUE;
        int endIndex = fromIndex - (nCount - 1);
        if (fromIndex >= endIndex) {
            for (int itemIndex = fromIndex + 1; itemIndex > endIndex; itemIndex--) {
                KLineEntity klineData = valuesArray.get(itemIndex - 1);
                float lowPrice = klineData.getLowPrice();
                result = result <= lowPrice ? result : lowPrice;
            }
        }

        return result;
    }


    public static float getMax(List<? extends KLineEntity> valuesArray,
                               int fromIndex, int nCount) {
        float result = Float.MIN_VALUE;
        int endIndex = fromIndex - (nCount - 1);
        if (fromIndex >= endIndex) {
            for (int itemIndex = fromIndex + 1; itemIndex > endIndex; itemIndex--) {
                KLineEntity klineData = valuesArray.get(itemIndex - 1);
                float highPrice = klineData.getHighPrice();
                result = result >= highPrice ? result : highPrice;
            }
        }
        return result;
    }

    private static float calculateBoll(List<? extends KLineEntity> payloads, int position, int maN) {
        float sum = 0;
        for (int i = position; i >= position - maN + 1; i--) {
            sum = (sum + payloads.get(i).getClosePrice());
        }
        return sum / maN;

    }

    private static float STD(List<? extends KLineEntity> payloads, int positon, int maN) {

        float sum = 0f, std = 0f;
        for (int i = positon; i >= positon - maN + 1; i--) {
            sum += payloads.get(i).getClosePrice();
        }
        float avg = sum / maN;
        for (int i = positon; i >= positon - maN + 1; i--) {
            std += (payloads.get(i).getClosePrice() - avg) * (payloads.get(i).getClosePrice() - avg);
        }
        return (float) Math.sqrt(std / maN);
    }


}
