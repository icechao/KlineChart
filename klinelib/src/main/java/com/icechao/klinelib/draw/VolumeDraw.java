package com.icechao.klinelib.draw;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;

import com.icechao.klinelib.base.BaseDraw;
import com.icechao.klinelib.base.BaseKLineChartView;
import com.icechao.klinelib.base.IValueFormatter;
import com.icechao.klinelib.formatter.ValueFormatter;
import com.icechao.klinelib.utils.Constants;
import com.icechao.klinelib.utils.NumberTools;
import com.icechao.klinelib.utils.Dputil;
import com.icechao.klinelib.R;

/*************************************************************************
 * Description   :
 *
 * @PackageName  : com.icechao.klinelib.utils
 * @FileName     : VolumeDraw.java
 * @Author       : chao
 * @Date         : 2019/4/8
 * @Email        : icechliu@gmail.com
 * @version      : V1
 *************************************************************************/
public class VolumeDraw extends BaseDraw {

    private Paint linePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private Paint redPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private Paint greenPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private Paint maOnePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private Paint maTwoPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private Paint volLeftPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private float volWidth = 0;
    private IValueFormatter valueFormatter = new ValueFormatter();
    private float lineVolWidth;
    private int itemsCount;
    private final int indexInterval;
    private String volMaIndex2;
    private String volMaIndex1;
    private String volIndex;

    public VolumeDraw(Context context) {
        redPaint.setColor(ContextCompat.getColor(context, R.color.color_03C087));
        greenPaint.setColor(ContextCompat.getColor(context, R.color.color_FF605A));
        volWidth = Dputil.Dp2Px(context, 4);
        indexInterval = Constants.getCount();
        volIndex = context.getString(R.string.k_index_vol);
        volMaIndex1 = context.getString(R.string.k_index_vol_ma1);
        volMaIndex2 = context.getString(R.string.k_index_vol_ma2);
    }

    private float endMaOne = 0;
    private float endMaTwo = 0;

    @Override
    public void drawTranslated(Canvas canvas, float lastX, float curX, @NonNull BaseKLineChartView view, int position, float... values) {
        if (position == 0) {
            drawHistogram(canvas, curX,
                    values[Constants.INDEX_VOL],
                    values[Constants.INDEX_OPEN],
                    values[Constants.INDEX_CLOSE]
                    , view, position);
        } else {
            drawHistogram(canvas, curX,
                    values[Constants.INDEX_VOL + indexInterval],
                    values[Constants.INDEX_OPEN + indexInterval],
                    values[Constants.INDEX_CLOSE + indexInterval]
                    , view, position);
            drawLine(lastX, curX, canvas, view, position,
                    values[Constants.INDEX_VOL_MA_1],
                    endMaOne, maOnePaint,
                    values[Constants.INDEX_VOL_MA_1 + indexInterval]);
            drawLine(lastX, curX, canvas, view, position,
                    values[Constants.INDEX_VOL_MA_1],
                    endMaTwo, maTwoPaint,
                    values[Constants.INDEX_VOL_MA_1 + indexInterval]);
        }

    }

    private void drawLine(float lastX, float curX, @NonNull Canvas canvas, @NonNull BaseKLineChartView view, int position, float lastMa, float endMa5, Paint ma5Paint, float currentMa) {
        if (Float.MIN_VALUE != lastMa) {
            if (position == itemsCount - 1 && 0 != endMa5 && view.isAnimationLast()) {
                view.drawVolLine(canvas, ma5Paint, lastX, lastMa, curX, endMa5);
            } else {
                view.drawVolLine(canvas, ma5Paint, lastX, lastMa, curX, currentMa);
            }
        }
    }

    private void drawHistogram(Canvas canvas, float curX, float vol, float open, float close, BaseKLineChartView view, int position) {

        float top, r = volWidth / 2 * view.getScaleX();
        int bottom = view.getVolRect().bottom;
        if (position == itemsCount - 1 && view.isAnimationLast()) {
            top = view.getVolY(view.getLastVol());
        } else {
            top = view.getVolY(vol);
        }
        if (0 != vol && top > bottom - 1) {
            top = bottom - 1;
        }
        if (view.isLine()) {
            canvas.drawRect(curX - lineVolWidth, top, curX + lineVolWidth, bottom, linePaint);
        } else if (close >= open) {//涨
            canvas.drawRect(curX - r, top, curX + r, bottom, redPaint);
        } else {
            canvas.drawRect(curX - r, top, curX + r, bottom, greenPaint);
        }

    }

    @Override
    public void drawText(@NonNull Canvas canvas, @NonNull BaseKLineChartView view, float x, float y, int position, float[] values) {
        String text;
        if (position == itemsCount - 1 && view.isAnimationLast()) {
            text = volIndex + NumberTools.getTradeMarketAmount(getValueFormatter().format(view.getLastVol())) + "  ";
        } else {
            text = volIndex + NumberTools.getTradeMarketAmount(getValueFormatter().format(values[Constants.INDEX_VOL])) + "  ";
        }
        canvas.drawText(text, x, y, volLeftPaint);
        x += view.getTextPaint().measureText(text);

        if (position == itemsCount - 1 && view.isAnimationLast()) {
            text = volMaIndex1 + NumberTools.getTradeMarketAmount(getValueFormatter().format(endMaOne)) + "  ";
        } else {
            text = volMaIndex1 + NumberTools.getTradeMarketAmount(getValueFormatter().format(values[Constants.INDEX_VOL_MA_1])) + "  ";
        }
        canvas.drawText(text, x, y, maOnePaint);
        x += maOnePaint.measureText(text);
        if (position == itemsCount - 1 && view.isAnimationLast()) {

            text = volMaIndex1 + NumberTools.getTradeMarketAmount(getValueFormatter().format(endMaTwo)) + "  ";
        } else {
            text = volMaIndex2 + NumberTools.getTradeMarketAmount(getValueFormatter().format(values[Constants.INDEX_VOL_MA_2])) + "  ";
        }
        canvas.drawText(text, x, y, maTwoPaint);
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
        this.itemsCount = mItemCount;
    }

    @Override
    public void startAnim(BaseKLineChartView view, float... values) {
        if (0 == endMaOne) {
            endMaOne = values[Constants.INDEX_VOL_MA_1];
            endMaTwo = values[Constants.INDEX_VOL_MA_2];
        } else {
            view.generaterAnimator(endMaOne, values[Constants.INDEX_VOL_MA_1], animation -> endMaOne = (float) animation.getAnimatedValue());
            view.generaterAnimator(endMaTwo, values[Constants.INDEX_VOL_MA_2], animation -> endMaTwo = (float) animation.getAnimatedValue());
        }
    }

    /**
     * 设置 MA5 线的颜色
     *
     * @param color
     */
    public void setMa5Color(int color) {
        this.maOnePaint.setColor(color);
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
        this.maTwoPaint.setColor(color);
    }


    public void setLineWidth(float width) {
        this.linePaint.setStrokeWidth(width);
        this.maOnePaint.setStrokeWidth(width);
        this.maTwoPaint.setStrokeWidth(width);
        this.lineVolWidth = width / 2;
    }

    /**
     * 设置文字大小
     *
     * @param textSize
     */
    public void setTextSize(float textSize) {
        this.maOnePaint.setTextSize(textSize);
        this.maTwoPaint.setTextSize(textSize);
        this.volLeftPaint.setTextSize(textSize);
    }

    public void setBarWidth(float candleWidth) {
        volWidth = candleWidth;
    }


    @Override
    public void resetValues() {
        endMaOne = 0;
        endMaTwo = 0;
    }

    public void setMinuteColor(int color) {
        linePaint.setColor(color);
    }
}
