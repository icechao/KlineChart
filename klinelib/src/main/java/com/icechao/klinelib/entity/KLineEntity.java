package com.icechao.klinelib.entity;


/*************************************************************************
 * Description   :
 *
 * @PackageName  : com.icechao.klinelib.utils
 * @FileName     : KLineEntity.java
 * @Author       : chao
 * @Date         : 2019/4/8
 * @Email        : icechliu@gmail.com
 * @version      : V1
 *************************************************************************/
public abstract class KLineEntity implements IKLine {


    /**
     * return the data time
     *
     * @return {@link Long} TimeMillis
     */
    @Override
    public abstract Long getDate();

    /**
     * return open price
     *
     * @return {@link Float} price
     */
    @Override
    public abstract float getOpenPrice();

    /**
     * return high price
     *
     * @return {@link Float} price
     */
    @Override
    public abstract float getHighPrice();

    /**
     * return low price
     *
     * @return {@link Float} price
     */
    @Override
    public abstract float getLowPrice();

    /**
     * return close price
     *
     * @return {@link Float} price
     */
    @Override
    public abstract float getClosePrice();

    /**
     * return trade vol
     *
     * @return {@link Float} vol
     */
    @Override
    public abstract float getVolume();


    @Override
    public float getMaOne() {
        return maOne;
    }

    @Override
    public float getMaTwo() {
        return maTwo;
    }

    @Override
    public float getMaThree() {
        return maThree;
    }

    @Override
    public float getDea() {
        return dea;
    }

    @Override
    public float getDif() {
        return dif;
    }

    @Override
    public float getMacd() {
        return macd;
    }

    @Override
    public float getK() {
        return k;
    }

    @Override
    public float getD() {
        return d;
    }

    @Override
    public float getJ() {
        return j;
    }

    @Override
    public float getUp() {
        return up;
    }

    @Override
    public float getMb() {
        return mb;
    }

    @Override
    public float getDn() {
        return dn;
    }

    @Override
    public float getMA5Volume() {
        return MA5Volume;
    }

    @Override
    public float getMA10Volume() {
        return MA10Volume;
    }

    private float maOne = Float.MIN_VALUE;

    private float maTwo = Float.MIN_VALUE;

    private float maThree = Float.MIN_VALUE;

    private float dea = Float.MIN_VALUE;

    private float dif = Float.MIN_VALUE;

    private float macd = Float.MIN_VALUE;

    private float k = Float.MIN_VALUE;

    private float d = Float.MIN_VALUE;

    private float j = Float.MIN_VALUE;

    private double rOne = Float.MIN_VALUE;
    private double rTwo = Float.MIN_VALUE;
    private double rThree = Float.MIN_VALUE;

    private double wrOne = Float.MIN_VALUE;
    private float wrTwo = Float.MIN_VALUE;
    private float wrThree = Float.MIN_VALUE;

    private float up = Float.MIN_VALUE;

    private float mb = Float.MIN_VALUE;

    private float dn = Float.MIN_VALUE;

    private float MA5Volume = Float.MIN_VALUE;

    private float MA10Volume = Float.MIN_VALUE;


    @Override
    public float getRsiOne() {
        return (float) rOne;
    }

    @Override
    public float getRsiTwo() {
        return (float) rTwo;
    }

    @Override
    public float getRsiThree() {
        return (float) rThree;
    }


    @Override
    public Float getWrOne() {
        return (float) wrOne;
    }

    @Override
    public Float getWrTwo() {
        return wrTwo;
    }

    @Override
    public Float getWrThree() {
        return wrThree;
    }

    public void setMaOne(float maOne) {
        this.maOne = maOne;
    }

    public void setMaTwo(float maTwo) {
        this.maTwo = maTwo;
    }

    public void setMaThree(float maThree) {
        this.maThree = maThree;
    }

    public void setDea(float dea) {
        this.dea = dea;
    }

    public void setDif(float dif) {
        this.dif = dif;
    }

    public void setMacd(float macd) {
        this.macd = macd;
    }

    public void setK(float k) {
        this.k = k;
    }

    public void setD(float d) {
        this.d = d;
    }

    public void setJ(float j) {
        this.j = j;
    }

    public void setrOne(double rOne) {
        this.rOne = rOne;
    }

    public void setrTwo(double rTwo) {
        this.rTwo = rTwo;
    }

    public void setrThree(double rThree) {
        this.rThree = rThree;
    }

    public void setWrOne(double wrOne) {
        this.wrOne = wrOne;
    }

    public void setWrTwo(float wrTwo) {
        this.wrTwo = wrTwo;
    }

    public void setWrThree(float wrThree) {
        this.wrThree = wrThree;
    }

    public void setUp(float up) {
        this.up = up;
    }

    public void setMb(float mb) {
        this.mb = mb;
    }

    public void setDn(float dn) {
        this.dn = dn;
    }

    public void setMA5Volume(float MA5Volume) {
        this.MA5Volume = MA5Volume;
    }

    public void setMA10Volume(float MA10Volume) {
        this.MA10Volume = MA10Volume;
    }
}
