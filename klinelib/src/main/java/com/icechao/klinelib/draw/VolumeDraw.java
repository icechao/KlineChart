package com.icechao.klinelib.draw;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import com.icechao.klinelib.base.BaseDraw;
import com.icechao.klinelib.base.BaseKLineChartView;
import com.icechao.klinelib.base.IValueFormatter;
import com.icechao.klinelib.entity.IVolume;
import com.icechao.klinelib.formatter.ValueFormatter;
import com.icechao.klinelib.utils.Constants;
import com.icechao.klinelib.utils.NumberUtil;
import com.icechao.klinelib.utils.ViewUtil;
import com.icechao.klinelib.R;

/*************************************************************************
 * Description   :
 *
 * @PackageName  : com.icechao.klinelib.utils
 * @FileName     : ViewUtil.java
 * @Author       : chao
 * @Date         : 2019/1/8
 * @Email        : icechliu@gmail.com
 * @version      : V1
 *************************************************************************/
public class VolumeDraw extends BaseDraw {

    private Paint linePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private Paint mRedPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private Paint mGreenPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private Paint ma5Paint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private Paint ma10Paint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private Paint volLeftPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private float pillarWidth = 0;
    private IValueFormatter valueFormatter = new ValueFormatter();
    //当前显示的是不是分钟线
    //分钟线下,量的柱状图宽度/2
    private float lineVolWidth;
    private int mItemCount;
    private final int indexInterval;

    public VolumeDraw(Context context) {
        mRedPaint.setColor(ContextCompat.getColor(context, R.color.color_03C087));
        mGreenPaint.setColor(ContextCompat.getColor(context, R.color.color_FF605A));
        pillarWidth = ViewUtil.Dp2Px(context, 4);
        indexInterval = Constants.getCount();
    }

    private float endMa5 = 0;
    private float endMa10 = 0;

    @Override
    public void drawTranslated(Canvas canvas, float lastX, float curX, @NonNull BaseKLineChartView view, int position, float... values) {
        drawHistogram(canvas, curX,
                values[Constants.INDEX_VOL + indexInterval],
                values[Constants.INDEX_OPEN + indexInterval],
                values[Constants.INDEX_CLOSE + indexInterval]
                , view, position);
        drawLine(lastX, curX, canvas, view, position,
                values[Constants.INDEX_VOL_MA_1],
                endMa5, ma5Paint,
                values[Constants.INDEX_VOL_MA_1 + indexInterval]);
        drawLine(lastX, curX, canvas, view, position,
                values[Constants.INDEX_VOL_MA_1],
                endMa10, ma10Paint,
                values[Constants.INDEX_VOL_MA_1 + indexInterval]);

    }

    private void drawLine(float lastX, float curX, @NonNull Canvas canvas, @NonNull BaseKLineChartView view, int position, float lastMa, float endMa5, Paint ma5Paint, float currentMa) {
        if (Float.MIN_VALUE != lastMa) {
            if (position == mItemCount - 1 && 0 != endMa5 && view.isAnimationLast()) {
                view.drawVolLine(canvas, ma5Paint, lastX, lastMa, curX, endMa5);
            } else {
                view.drawVolLine(canvas, ma5Paint, lastX, lastMa, curX, currentMa);
            }
        }
    }

    private void drawHistogram(
            Canvas canvas, float curX,
            float vol, float open, float close,
            BaseKLineChartView view, int position) {
        float r = pillarWidth / 2 * view.getScaleX();
        float top;
        if (position == mItemCount - 1 && view.isAnimationLast()) {
            top = view.getVolY(view.getLastVol());
        } else {
            top = view.getVolY(vol);
        }
        int bottom = view.getVolRect().bottom;
        if (0 != view.getLastVol() && top > bottom - 1) {
            top = bottom - 1;
        }
        if (view.isLine()) {
            canvas.drawRect(curX - lineVolWidth, top, curX + lineVolWidth, bottom, linePaint);
        } else if (close > open) {//涨
            canvas.drawRect(curX - r, top, curX + r, bottom, mRedPaint);
        } else {
            canvas.drawRect(curX - r, top, curX + r, bottom, mGreenPaint);
        }

    }

    @Override
    public void drawText(
            @NonNull Canvas canvas, @NonNull BaseKLineChartView view, int position, float x, float y) {
        IVolume point = (IVolume) view.getItem(position);
        String text;
        if (position == mItemCount - 1 && view.isAnimationLast()) {
            text = "VOL:" + NumberUtil.getTradeMarketAmount(getValueFormatter().format(view.getLastVol())) + "  ";
        } else {
            text = "VOL:" + NumberUtil.getTradeMarketAmount(getValueFormatter().format(point.getVolume())) + "  ";
        }
        canvas.drawText(text, x, y, volLeftPaint);
        x += view.getTextPaint().measureText(text);
        if (position == mItemCount - 1 && view.isAnimationLast()) {
            text = "MA5:" + NumberUtil.getTradeMarketAmount(getValueFormatter().format(endMa5)) + "  ";
        } else {
            text = "MA5:" + NumberUtil.getTradeMarketAmount(getValueFormatter().format(point.getMA5Volume())) + "  ";
        }
        canvas.drawText(text, x, y, ma5Paint);
        x += ma5Paint.measureText(text);
        if (position == mItemCount - 1 && view.isAnimationLast()) {
            text = "MA10:" + NumberUtil.getTradeMarketAmount(getValueFormatter().format(endMa10)) + "  ";
        } else {
            text = "MA10:" + NumberUtil.getTradeMarketAmount(getValueFormatter().format(point.getMA10Volume())) + "  ";
        }
        canvas.drawText(text, x, y, ma10Paint);
    }

    @Override
    public IValueFormatter getValueFormatter() {
        return valueFormatter;
    }

    @Override
    public void setValueFormatter(IValueFormatter valueFormatter) {
        this.valueFormatter = valueFormatter;
    }

    @Override
    public void setItemCount(int mItemCount) {
        this.mItemCount = mItemCount;
    }

    @Override
    public void startAnim(BaseKLineChartView view, float... values) {
        if (0 == endMa5) {
            endMa5 = values[Constants.INDEX_VOL_MA_1];
            endMa10 = values[Constants.INDEX_VOL_MA_2];
        } else {
            view.generaterAnimator(endMa5, values[Constants.INDEX_VOL_MA_1], animation -> endMa5 = (float) animation.getAnimatedValue());
            view.generaterAnimator(endMa10, values[Constants.INDEX_VOL_MA_2], animation -> endMa10 = (float) animation.getAnimatedValue());
        }
    }

    /**
     * 设置 MA5 线的颜色
     *
     * @param color
     */
    public void setMa5Color(int color) {
        this.ma5Paint.setColor(color);
    }

    /**
     * 设置 vol图左上角文字 线的颜色
     *
     * @param color
     */
    public void setVolLeftColor(int color) {
        this.volLeftPaint.setColor(color);
    }


    /**
     * 设置 MA10 线的颜色
     *
     * @param color
     */
    public void setMa10Color(int color) {
        this.ma10Paint.setColor(color);
    }


    public void setLineWidth(float width) {
        this.linePaint.setStrokeWidth(width);
        this.ma5Paint.setStrokeWidth(width);
        this.ma10Paint.setStrokeWidth(width);
        this.lineVolWidth = width / 2;
    }

    /**
     * 设置文字大小
     *
     * @param textSize
     */
    public void setTextSize(float textSize) {
        this.ma5Paint.setTextSize(textSize);
        this.ma10Paint.setTextSize(textSize);
        this.volLeftPaint.setTextSize(textSize);
    }

    public void setBarWidth(float candleWidth) {
        pillarWidth = candleWidth;
    }


    @Override
    public void resetValues() {
        endMa5 = 0;
        endMa10 = 0;
    }

    public void setMinuteColor(int color) {
        linePaint.setColor(color);
    }
}
