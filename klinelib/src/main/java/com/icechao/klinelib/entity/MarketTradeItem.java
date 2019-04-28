package com.icechao.klinelib.entity;




import java.io.Serializable;

/*************************************************************************
 * Description   :
 *
 * @PackageName  : com.icechao.klinelib.utils
 * @FileName     : MarketTradeItem.java
 * @Author       : chao
 * @Date         : 2019/4/10
 * @Email        : icechliu@gmail.com
 * @version      : V1
 *************************************************************************/
public class MarketTradeItem implements Serializable {

    private double price;
    private double amount;
    private int type;
    private int length;
    private int position;
    private int progress;

    private boolean needDraw;

    private String symbol;

    private PriceItemClickListener listener;

    private boolean isOrderPlace;

    private int tradeType;

    public MarketTradeItem() {

    }

    public MarketTradeItem(int type, int tradeType) {
        this.type = type;
        this.tradeType = tradeType;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public int getLength() {
        return length;
    }

    public void setLength(int length) {
        this.length = length;
    }

    public PriceItemClickListener getListener() {
        return listener;
    }

    public void setListener(PriceItemClickListener listener) {
        this.listener = listener;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public int getProgress() {
        return progress;
    }

    public void setProgress(int progress) {
        this.progress = progress;
    }

    public boolean isNeedDraw() {
        return needDraw;
    }

    public void setNeedDraw(boolean needDraw) {
        this.needDraw = needDraw;
    }


    public void reset() {
        price = 0;
        amount = 0;
    }

    public int getTradeType() {
        return tradeType;
    }

    public void setTradeType(int tradeType) {
        this.tradeType = tradeType;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    public String getSymbol() {
        return symbol;
    }

    public boolean isOrderPlace() {
        return isOrderPlace;
    }

    public void setOrderPlace(boolean orderPlace) {
        isOrderPlace = orderPlace;
    }

    public static final int BUY_TYPE = 0;
    public static final int MARKET_TRADE = 2;

}
