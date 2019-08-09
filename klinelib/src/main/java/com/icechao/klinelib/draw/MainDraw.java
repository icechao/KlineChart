package com.icechao.klinelib.draw;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.support.annotation.NonNull;
import com.icechao.klinelib.R;
import com.icechao.klinelib.base.BaseDraw;
import com.icechao.klinelib.base.BaseKLineChartView;
import com.icechao.klinelib.base.IValueFormatter;
import com.icechao.klinelib.formatter.ValueFormatter;
import com.icechao.klinelib.utils.Constants;
import com.icechao.klinelib.utils.LogUtil;
import com.icechao.klinelib.utils.NumberTools;
import com.icechao.klinelib.utils.Status;

/*************************************************************************
 * Description   :
 *
 * @PackageName  : com.icechao.klinelib.utils
 * @FileName     : MainDraw.java
 * @Author       : chao
 * @Date         : 2019/4/8
 * @Email        : icechliu@gmail.com
 * @version      : V1
 *************************************************************************/
public class MainDraw extends BaseDraw {

    private int itemCount;
    private String[] strings = new String[8];
    private IValueFormatter valueFormatter = new ValueFormatter();
    private float candleWidth, margin, padding, mainLengendMarginTop,
            maOne, maTwo, maThree, bollUp, bollMb, bollDn;
    private final int indexInterval;
    private String indexMa1, indexMa2, indexMa3, indexBoll, indexUb, indexLb;

    public void setItemCount(int mItemCount) {
        itemCount = mItemCount;
    }

    private Paint lineAreaPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private Paint linePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private Paint upPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private Paint upLinePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private Paint downPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private Paint downLinePaint = new Paint(Paint.ANTI_ALIAS_FLAG);


    private Paint indexPaintOne = new Paint(Paint.ANTI_ALIAS_FLAG);
    private Paint indexPaintTwo = new Paint(Paint.ANTI_ALIAS_FLAG);
    private Paint indexPaintThree = new Paint(Paint.ANTI_ALIAS_FLAG);

    private Paint selectorTextPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private Paint selectorBorderPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private Paint selectorBackgroundPaint = new Paint(Paint.ANTI_ALIAS_FLAG);

    private String[] marketInfoText = new String[8];


    public MainDraw(Context context) {
        indexInterval = Constants.getCount();
        selectorBorderPaint.setStyle(Paint.Style.STROKE);
        upLinePaint.setStyle(Paint.Style.STROKE);
        upLinePaint.setAntiAlias(true);
        downLinePaint.setStyle(Paint.Style.STROKE);
        downLinePaint.setAntiAlias(true);
        marketInfoText[0] = ("时间   ");
        marketInfoText[1] = ("开     ");
        marketInfoText[2] = ("高     ");
        marketInfoText[3] = ("低     ");
        marketInfoText[4] = ("收     ");
        marketInfoText[5] = ("涨跌额  ");
        marketInfoText[6] = ("涨跌幅  ");
        marketInfoText[7] = ("成交量  ");

        indexMa1 = String.format(context.getString(R.string.k_index_ma_formater), Constants.K_MA_NUMBER_1);
        indexMa2 = String.format(context.getString(R.string.k_index_ma_formater), Constants.K_MA_NUMBER_2);
        indexMa3 = String.format(context.getString(R.string.k_index_ma_formater), Constants.K_MA_NUMBER_3);

        indexBoll = context.getString(R.string.k_index_boll);
        indexUb = context.getString(R.string.k_index_ub);
        indexLb = context.getString(R.string.k_index_lb);
    }


    @Override
    public void drawTranslated(Canvas canvas, float lastX, float curX, @NonNull BaseKLineChartView view, int position, float... values) {
        if (view.getKlineStatus().showLine()) {
            if (position == itemCount - 1) {
                float lastClosePrice = values[Constants.INDEX_CLOSE];
                view.drawEndLine(canvas, linePaint, lastX, lastClosePrice, curX);
                view.drawEndFill(canvas, lineAreaPaint, lastX, lastClosePrice, curX);

            } else if (position != 0) {
                float lastClosePrice = values[Constants.INDEX_CLOSE];
                float closePrice = values[Constants.INDEX_CLOSE + indexInterval];
                view.drawMainLine(canvas, linePaint, lastX, lastClosePrice, curX, closePrice);
                view.drawFill(canvas, lineAreaPaint, lastX, lastClosePrice, curX, closePrice);
            }

        } else {
            if (position == 0) {
                drawCandle(view, canvas, curX,
                        values[Constants.INDEX_HIGH],
                        values[Constants.INDEX_LOW],
                        values[Constants.INDEX_OPEN],
                        values[Constants.INDEX_CLOSE],
                        position);
            } else {
                drawCandle(view, canvas, curX,
                        values[Constants.INDEX_HIGH + indexInterval],
                        values[Constants.INDEX_LOW + indexInterval],
                        values[Constants.INDEX_OPEN + indexInterval],
                        values[Constants.INDEX_CLOSE + indexInterval],
                        position);
                Status.MainStatus status = view.getStatus();
                if (status == Status.MainStatus.MA) {
                    //画第一根ma
                    drawLine(lastX, curX, canvas, view, position,
                            values[Constants.INDEX_MA_1],
                            maOne, indexPaintOne,
                            values[Constants.INDEX_MA_1 + indexInterval]);
                    //画第二根ma
                    drawLine(lastX, curX, canvas, view, position,
                            values[Constants.INDEX_MA_2],
                            maTwo, indexPaintTwo,
                            values[Constants.INDEX_MA_2 + indexInterval]);
                    //画第三根ma
                    drawLine(lastX, curX, canvas, view, position,
                            values[Constants.INDEX_MA_3],
                            maThree, indexPaintThree,
                            values[Constants.INDEX_MA_3 + indexInterval]);
                } else if (status == Status.MainStatus.BOLL) {
                    //画boll
                    drawLine(lastX, curX, canvas, view, position,
                            values[Constants.INDEX_BOLL_UP],
                            bollUp, indexPaintTwo,
                            values[Constants.INDEX_BOLL_UP + indexInterval]);
                    drawLine(lastX, curX, canvas, view, position,
                            values[Constants.INDEX_BOLL_MB],
                            bollMb, indexPaintOne,
                            values[Constants.INDEX_BOLL_MB + indexInterval]);
                    drawLine(lastX, curX, canvas, view, position,
                            values[Constants.INDEX_BOLL_DN],
                            bollDn, indexPaintThree,
                            values[Constants.INDEX_BOLL_DN + indexInterval]);
                }
            }
        }
    }

    private void drawLine(float lastX, float curX, @NonNull Canvas canvas, @NonNull BaseKLineChartView view, int position, float start, float animEnd, Paint paint, float end) {
        if (Float.MIN_VALUE != start) {
            if (itemCount - 1 == position && 0 != animEnd && view.isAnimationLast()) {
                view.drawMainLine(canvas, paint, lastX, start, curX, animEnd);
            } else {
                view.drawMainLine(canvas, paint, lastX, start, curX, end);
            }
        }
    }


    @Override
    @SuppressWarnings("all")
    public void drawText(@NonNull Canvas canvas, @NonNull BaseKLineChartView view, float x, float y, int position, float[] values) {

        //修改头文字显示在顶部
        y = maTextHeight + mainLengendMarginTop;
        if (view.getKlineStatus().showLine()) {

        } else {
            Status.MainStatus status = view.getStatus();
            if (status == Status.MainStatus.MA) {
                String text;
                if (Float.MIN_VALUE != values[Constants.INDEX_MA_1]) {
                    text = indexMa1 + getValueFormatter().format(values[Constants.INDEX_MA_1]) + "  ";
                    canvas.drawText(text, x, y, indexPaintOne);
                    x += indexPaintOne.measureText(text);
                }
                if (Float.MIN_VALUE != values[Constants.INDEX_MA_2]) {
                    text = indexMa2 + getValueFormatter().format(values[Constants.INDEX_MA_2]) + "  ";
                    canvas.drawText(text, x, y, indexPaintTwo);
                    x += indexPaintTwo.measureText(text);
                }
                if (Float.MIN_VALUE != values[Constants.INDEX_MA_3]) {
                    text = indexMa3 + getValueFormatter().format(values[Constants.INDEX_MA_3]);
                    canvas.drawText(text, x, y, indexPaintThree);
                }
            } else if (status == Status.MainStatus.BOLL) {
                if (Float.MIN_VALUE != values[Constants.INDEX_BOLL_MB]) {
                    String text = indexBoll + view.formatValue(values[Constants.INDEX_BOLL_MB]) + "  ";
                    canvas.drawText(text, x, y, indexPaintOne);
                    x += indexPaintOne.measureText(text);
                    text = indexUb + view.formatValue(values[Constants.INDEX_BOLL_UP]) + "  ";
                    canvas.drawText(text, x, y, indexPaintTwo);
                    x += indexPaintTwo.measureText(text);
                    text = indexLb + view.formatValue(values[Constants.INDEX_BOLL_DN]);
                    canvas.drawText(text, x, y, indexPaintThree);
                }
            }
        }
        if (view.getShowSelected()) {
            drawSelector(view, canvas, values);
        }
    }


    @Override
    public IValueFormatter getValueFormatter() {
        return valueFormatter;
    }

    @Override
    public void setValueFormatter(IValueFormatter valueFormatter) {
        this.valueFormatter = valueFormatter;
    }

    /**
     * 画Candle
     *
     * @param canvas canvas
     * @param x      x轴坐标
     * @param high   最高价
     * @param low    最低价
     * @param open   开盘价
     * @param close  收盘价
     */
    private void drawCandle(BaseKLineChartView view, Canvas canvas, float x, float high, float low, float open, float close, int position) {
        high = view.getMainY(high);
        low = view.getMainY(low);
        open = view.getMainY(open);
        if (position == itemCount - 1) {
            close = view.getMainY(view.getLastPrice());
        } else {
            close = view.getMainY(close);
        }
        float r = candleWidth / 2 * view.getScaleX();
        float cancleLeft = x - r;
        float candleright = x + r;
        if (open < close) {//跌
            drawCandle(canvas, x, high, low, open, close, cancleLeft, candleright, downPaint, downLinePaint);
        } else if (open > close) {//涨
            drawCandle(canvas, x, high, low, close, open, cancleLeft, candleright, upPaint, upLinePaint);
        } else {
            drawCandle(canvas, x, high, low, close - 1, open, cancleLeft, candleright, upPaint, upLinePaint);
        }
    }

    private void drawCandle(Canvas canvas, float x, float high, float low, float open, float close, float cancleLeft, float candleright, Paint paint, Paint linePaint) {
        canvas.drawRect(cancleLeft, close, candleright, open, paint);
        if (high < open) {
            canvas.drawLine(x, high, x, open, linePaint);
        }
        if (close < low) {
            canvas.drawLine(x, close, x, low, linePaint);
        }
    }

    /**
     * 选中的弹出窗口, selected data popupwindow
     *
     * @param view   view
     * @param canvas canvas
     * @param values
     */
    @SuppressLint("DefaultLocale")
    private void drawSelector(BaseKLineChartView view, Canvas canvas, float[] values) {

        int index = view.getSelectedIndex();

//        strings[0] = view.formatDateTime(view.getAdapter().getDate(index));
        strings[0] = view.getTime(index);
        strings[1] = view.getValueFormatter().format(values[Constants.INDEX_OPEN]);
        strings[2] = (view.getValueFormatter().format(values[Constants.INDEX_HIGH]));
        strings[3] = (view.getValueFormatter().format(values[Constants.INDEX_LOW]));
        strings[4] = (view.getValueFormatter().format(values[Constants.INDEX_CLOSE]));
        double tempDiffPrice = values[Constants.INDEX_CLOSE] - values[Constants.INDEX_OPEN];
        strings[5] = (view.getValueFormatter().format((float) tempDiffPrice));
        strings[6] = NumberTools.roundDown((tempDiffPrice * 100) / values[Constants.INDEX_OPEN], 2) + "%";
        strings[7] = NumberTools.formatAmount(valueFormatter.format(values[Constants.INDEX_VOL]));

        float width = 0, left, top = margin + view.getChartPaddingTop();
        //上下多加两个padding值的间隙
        int length = strings.length;
        float height = padding * ((length - 1) + 4) + selectedTextHeight * length;
        for (int i = 0; i < length; i++) {
            String tempString = marketInfoText[i] + strings[i];
            width = Math.max(width, selectorTextPaint.measureText(tempString));
        }
        width += padding * 2;

        float x = view.translateXtoX(view.getX(index));
        if (x > view.getViewWidth() / 2) {
            left = margin;
        } else {
            left = view.getViewWidth() - width - margin;
        }

        float right = left + width;
        RectF r = new RectF(left, top, right, top + height);
        canvas.drawRoundRect(r, padding / 2, padding / 2, selectorBackgroundPaint);
        canvas.drawRoundRect(r, padding / 2, padding / 2, selectorBorderPaint);
        float y = top + padding * 2 + selectedTextBaseLine;
        float tempX = right - padding;
        for (int i = 0; i < length; i++) {
            String s = strings[i];
            canvas.drawText(marketInfoText[i], left + padding, y, selectorTextPaint);
            if (i == 5 || i == 6) {
                if (tempDiffPrice >= 0) {
                    canvas.drawText(s, tempX - selectorTextPaint.measureText(s), y, upPaint);
                } else {
                    canvas.drawText(s, tempX - selectorTextPaint.measureText(s), y, downPaint);
                }
            } else {
                canvas.drawText(s, tempX - selectorTextPaint.measureText(s), y, selectorTextPaint);
            }
            y += selectedTextHeight + padding;
        }

    }

    /**
     * 设置蜡烛宽度
     *
     * @param candleWidth candle width
     */
    public void setCandleWidth(float candleWidth) {
        this.candleWidth = candleWidth;
    }

    /**
     * 设置蜡烛线宽度
     *
     * @param candleLineWidth lineWidth
     */
    public void setCandleLineWidth(float candleLineWidth) {
        downLinePaint.setStrokeWidth(candleLineWidth);
        upLinePaint.setStrokeWidth(candleLineWidth);
    }

    /**
     * 设置ma1颜色 ma1 color
     *
     * @param color color
     */
    public void setMaOneColor(int color) {
        this.indexPaintOne.setColor(color);
    }

    /**
     * 设置ma2颜色, ma2 color
     *
     * @param color color
     */
    public void setMaTwoColor(int color) {
        this.indexPaintTwo.setColor(color);
    }

    /**
     * 设置ma3颜色 ,ma3 color
     *
     * @param color color
     */
    public void setMaThreeColor(int color) {
        this.indexPaintThree.setColor(color);
    }

    /**
     * 设置选择器弹出框相关颜色 selected popupwindow text color
     *
     * @param textColor       文字
     * @param boderColor      边框
     * @param backgroundColor 背景
     */
    public void setSelectorTextColor(int textColor, int boderColor, int backgroundColor) {
        selectorTextPaint.setColor(textColor);
        selectorBorderPaint.setColor(boderColor);
        selectorBackgroundPaint.setColor(backgroundColor);
    }

    private float selectedTextHeight;
    private float selectedTextBaseLine;

    /**
     * 设置选择器文字大小 selected popupwindow text size
     *
     * @param textSize textsize
     */
    public void setSelectorTextSize(float textSize) {
        selectorTextPaint.setTextSize(textSize);
        downPaint.setTextSize(textSize);
        upPaint.setTextSize(textSize);
        Paint.FontMetrics metrics = selectorTextPaint.getFontMetrics();
        selectedTextHeight = metrics.descent - metrics.ascent;
        selectedTextBaseLine = (selectedTextHeight - metrics.bottom - metrics.top) / 2;

    }

    /**
     * 设置选择器背景 selected popupwindow  background color
     *
     * @param color color
     */
    public void setSelectorBackgroundColor(int color) {
        selectorBackgroundPaint.setColor(color);
    }

    /**
     * 设置曲线宽度 line width
     */
    public void setLineWidth(float width) {
        indexPaintThree.setStrokeWidth(width);
        indexPaintTwo.setStrokeWidth(width);
        indexPaintOne.setStrokeWidth(width);
        linePaint.setStrokeWidth(width);
        selectorBorderPaint.setStrokeWidth(width);

    }

    private float maTextHeight;

    /**
     * 设置文字大小 text size
     */
    public void setTextSize(float textSize) {
        indexPaintThree.setTextSize(textSize);
        indexPaintTwo.setTextSize(textSize);
        indexPaintOne.setTextSize(textSize);
        Paint.FontMetrics metrics = indexPaintOne.getFontMetrics();
        maTextHeight = metrics.descent - metrics.ascent;
    }


    @Override
    public void startAnim(BaseKLineChartView view, float[] values) {

        switch (view.getStatus()) {
            case MA:
                if (maOne == 0) {
                    maOne = values[Constants.INDEX_MA_1];
                    maTwo = values[Constants.INDEX_MA_2];
                    maThree = values[Constants.INDEX_MA_3];
                    return;
                }
                view.generaterAnimator(maOne, values[Constants.INDEX_MA_1], animation -> maOne = (float) animation.getAnimatedValue());
                view.generaterAnimator(maTwo, values[Constants.INDEX_MA_2], animation -> maTwo = (float) animation.getAnimatedValue());
                view.generaterAnimator(maThree, values[Constants.INDEX_MA_3], animation -> maThree = (float) animation.getAnimatedValue());
                break;
            case BOLL:
                if (bollUp == 0 && view.getStatus() == Status.MainStatus.BOLL) {
                    bollUp = values[Constants.INDEX_BOLL_UP];
                    bollDn = values[Constants.INDEX_BOLL_DN];
                    bollMb = values[Constants.INDEX_BOLL_MB];
                    return;
                }
                view.generaterAnimator(bollMb, values[Constants.INDEX_BOLL_UP], animation -> bollMb = (float) animation.getAnimatedValue());
                view.generaterAnimator(bollDn, values[Constants.INDEX_BOLL_DN], animation -> bollDn = (float) animation.getAnimatedValue());
                view.generaterAnimator(bollUp, values[Constants.INDEX_BOLL_MB], animation -> bollUp = (float) animation.getAnimatedValue());
                break;

        }

    }

    @Override
    public void resetValues() {
        maOne = 0;
        maTwo = 0;
        maThree = 0;

        bollUp = 0;
        bollMb = 0;
        bollDn = 0;
    }

    public void setMarketInfoText(String[] marketInfoText) {
        this.marketInfoText = marketInfoText;
    }

    public void setStroke(boolean isStroke) {
        if (isStroke) {
            upPaint.setStyle(Paint.Style.STROKE);
            downPaint.setStyle(Paint.Style.STROKE);
        } else {
            upPaint.setStyle(Paint.Style.FILL);
            downPaint.setStyle(Paint.Style.FILL);
        }
    }


    public void setIncreaseColor(int color) {
        upPaint.setColor(color);
        upLinePaint.setColor(color);

    }

    public void setDecreaseColor(int color) {
        downPaint.setColor(color);
        downLinePaint.setColor(color);
    }

    public void drawMaxMinValue(Canvas canvas, BaseKLineChartView view,
                                float maxX, float mainHighMaxValue,
                                float minX, float mainLowMinValue) {
        if (!view.getKlineStatus().showLine()) {
            //绘制最大值和最小值
            float y = view.getMainY(mainLowMinValue);
            //计算显示位置
            y = fixTextYBaseBottom(y);
            String LowString;
            float stringWidth, screenMid = view.getTranslationScreenMid();
            if (minX < screenMid) {
                LowString = "── " + getValueFormatter().format(mainLowMinValue);
            } else {
                LowString = getValueFormatter().format(mainLowMinValue) + " ──";
                stringWidth = maxMinPaint.measureText(LowString);
                minX -= stringWidth;
            }
            canvas.drawText(LowString, minX, y, maxMinPaint);

            y = view.getMainY(mainHighMaxValue);
            String highString;
            y = fixTextYBaseBottom(y);
            if (maxX < screenMid) {
                highString = "── " + getValueFormatter().format(mainHighMaxValue);
            } else {
                highString = getValueFormatter().format(mainHighMaxValue) + " ──";
                stringWidth = maxMinPaint.measureText(highString);
                maxX -= stringWidth;
            }
            canvas.drawText(highString, maxX, y, maxMinPaint);
        }
    }


    /**
     * 最大值最小值画笔  max value /min value paint
     */
    private Paint maxMinPaint = new Paint(Paint.ANTI_ALIAS_FLAG);

    /**
     * 设置最大值/最小值文字颜色 max value /min value text color
     */
    public void setLimitTextColor(int color) {
        maxMinPaint.setColor(color);
    }

    private float limitTextHigh;
    private float limitTextDecent;

    /**
     * 设置最大值/最小值文字大小 max value /min value  text size
     */
    public void setLimitTextSize(float textSize) {
        maxMinPaint.setTextSize(textSize);
        Paint.FontMetrics fm = maxMinPaint.getFontMetrics();
        limitTextHigh = fm.descent - fm.ascent;
        limitTextDecent = fm.descent;

    }

    /**
     * 解决text居中的问题 fix text align center
     */
    public float fixTextYBaseBottom(float y) {
        return y + (limitTextHigh) / 2 - limitTextDecent;
    }


    /**
     * 分时线颜色, minute line color
     *
     * @param color
     */
    public void setMinuteLineColor(int color) {
        linePaint.setColor(color);
    }

    /**
     * top padding main area
     *
     * @param mainLengendMarginTop
     */
    public void setMainLengendMarginTop(float mainLengendMarginTop) {
        this.mainLengendMarginTop = mainLengendMarginTop;
    }

    public void setSelectInfoBoxMargin(float margin) {
        this.margin = margin;
    }

    public void setSelectInfoBoxPadding(float padding) {
        this.padding = padding;
    }
}
