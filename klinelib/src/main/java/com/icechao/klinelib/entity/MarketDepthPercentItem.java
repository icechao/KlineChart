package com.icechao.klinelib.entity;

/*************************************************************************
 * Description   :
 *
 * @PackageName  : com.icechao.klinelib.utils
 * @FileName     : MarketDepthPercentItem.java
 * @Author       : chao
 * @Date         : 2019/4/10
 * @Email        : icechliu@gmail.com
 * @version      : V1
 *************************************************************************/
public class MarketDepthPercentItem {

    private double price;

    private double amount;

    private String symbol;

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }
}
