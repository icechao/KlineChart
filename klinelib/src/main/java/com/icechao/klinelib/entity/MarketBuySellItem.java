package com.icechao.klinelib.entity;




import java.io.Serializable;

public class MarketBuySellItem implements Serializable {

    public static final int BUY_TYPE = 0;
    public static final int SELL_TYPE = 1;
    public static final int VERTICAL_TRADE = 1; //竖版交易的盘口
    public static final int HORIZONTAL_TRADE = 0; //横版交易的盘口
    public static final int MARKET_TRADE = 2; //k线的盘口


    private double price;
    private double amount;
    private int type;
    private int length;
    private int position;
    private int progress;

    private boolean needDraw;

    private String symbol;

    private PriceItemListener listener;

    private boolean isOrderPlace;

    private int tradeType;//交易的方向  横版或者竖版

    public MarketBuySellItem() {

    }

    public MarketBuySellItem(int type, int tradeType) {
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

    public PriceItemListener getListener() {
        return listener;
    }

    public void setListener(PriceItemListener listener) {
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
}
