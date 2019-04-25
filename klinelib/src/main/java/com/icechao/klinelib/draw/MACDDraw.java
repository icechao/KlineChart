package com.icechao.klinelib.draw;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.icechao.klinelib.base.BaseDraw;
import com.icechao.klinelib.base.BaseKLineChartView;
import com.icechao.klinelib.base.IChartDraw;
import com.icechao.klinelib.base.IValueFormatter;
import com.icechao.klinelib.entity.ICandle;
import com.icechao.klinelib.entity.IMACD;
import com.icechao.klinelib.formatter.ValueFormatter;
import com.icechao.klinelib.utils.Constants;
import com.icechao.klinelib.R;

/*************************************************************************
 * Description   :
 *
 * @PackageName  : com.icechao.klinelib.utils
 * @FileName     : MACDDraw.java
 * @Author       : chao
 * @Date         : 2019/4/8
 * @Email        : icechliu@gmail.com
 * @version      : V1
 *************************************************************************/

public class MACDDraw extends BaseDraw {

    private Paint mRedPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private Paint mGreenPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private Paint mDIFPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private Paint mDEAPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private Paint mMACDPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    /**
     * macd 中柱子的宽度
     */
    private float mMACDWidth = 0;
    private ValueFormatter valueFormatter = new ValueFormatter();
    private final int indexInterval;
    private String macdIndexLabel;
    private String difIndexLabel;
    private String deaIndexLabel;

    public MACDDraw(Context context, int upColor, int downColor) {
        mRedPaint.setColor(upColor);
        mGreenPaint.setColor(downColor);
        indexInterval = Constants.getCount();
        macdIndexLabel = context.getString(R.string.k_index_macd);
        difIndexLabel = context.getString(R.string.k_index_dif);
        deaIndexLabel = context.getString(R.string.k_index_dea);
    }


    @Override
    public void drawTranslated(Canvas canvas, float lastX, float curX, @NonNull BaseKLineChartView view, int position, float... values) {
        drawMACD(canvas, view, curX, values[Constants.INDEX_MACD_MACD]);
        if (Float.MIN_VALUE != values[Constants.INDEX_MACD_DEA]) {
            view.drawChildLine(canvas, mDEAPaint, lastX, values[Constants.INDEX_MACD_DEA], curX, values[Constants.INDEX_MACD_DEA + indexInterval]);
        }
        if (Float.MIN_VALUE != values[Constants.INDEX_MACD_DIF]) {
            view.drawChildLine(canvas, mDIFPaint, lastX, values[Constants.INDEX_MACD_DIF], curX, values[Constants.INDEX_MACD_DIF + indexInterval]);
        }
    }

    @Override
    public void drawText(@NonNull Canvas canvas, @NonNull BaseKLineChartView view, float x, float y,int position, float[] values) {
//        IMACD point = (IMACD) view.getItem(position);
        String text = String.format(Constants.MACD_TOP_TEXT_TAMPLATE, Constants.MACD_S, Constants.MACD_L, Constants.MACD_M);
        canvas.drawText(text, x, y, view.getTextPaint());
        x += mMACDPaint.measureText(text);

        text = macdIndexLabel + view.formatValue(values[Constants.INDEX_MACD_MACD]) + "  ";
        canvas.drawText(text, x, y, mMACDPaint);
        x += mMACDPaint.measureText(text);
        text = difIndexLabel + view.formatValue(values[Constants.INDEX_MACD_DIF]) + "  ";
        canvas.drawText(text, x, y, mDIFPaint);
        x += mDIFPaint.measureText(text);
        text = deaIndexLabel + view.formatValue(values[Constants.INDEX_MACD_DEA]);
        canvas.drawText(text, x, y, mDEAPaint);
    }


    @Override
    public float getMaxValue(float... values) {
        return Math.max(values[Constants.INDEX_MACD_MACD], Math.max(values[Constants.INDEX_MACD_DEA], values[Constants.INDEX_MACD_DIF]));
    }

    @Override
    public float getMinValue(float... values) {
        return Math.min(values[Constants.INDEX_MACD_MACD], Math.min(values[Constants.INDEX_MACD_DEA], values[Constants.INDEX_MACD_DIF]));
    }

    @Override
    public void startAnim(BaseKLineChartView view, float... values) {

    }


    @Override
    public IValueFormatter getValueFormatter() {

        return valueFormatter;
    }

    @Override
    public void setValueFormatter(IValueFormatter valueFormatter) {
        this.valueFormatter = new ValueFormatter();
    }

    @Override
    public void setItemCount(int mItemCount) {

    }


    @Override
    public void resetValues() {

    }

    /**
     * 画macd
     *
     * @param canvas
     * @param x
     * @param macd
     */
    private void drawMACD(Canvas canvas, BaseKLineChartView view, float x, float macd) {
        float r = mMACDWidth / 2 * view.getScaleX();
        if (macd >= 0) {
            canvas.drawRect(x - r, view.getChildY(macd), x + r, view.getChildY(0), mRedPaint);
        } else {
            canvas.drawRect(x - r, view.getChildY(0), x + r, view.getChildY(macd), mGreenPaint);
        }
    }

    /**
     * 设置DIF颜色
     */
    public void setDIFColor(int color) {
        this.mDIFPaint.setColor(color);
    }

    /**
     * 设置DEA颜色
     */
    public void setDEAColor(int color) {
        this.mDEAPaint.setColor(color);
    }

    /**
     * 设置MACD颜色
     */
    public void setMACDColor(int color) {
        this.mMACDPaint.setColor(color);
    }

    /**
     * 设置MACD的宽度
     *
     * @param MACDWidth
     */
    public void setMACDWidth(float MACDWidth) {
        mMACDWidth = MACDWidth;
    }

    /**
     * 设置曲线宽度
     */
    public void setLineWidth(float width) {
        mDEAPaint.setStrokeWidth(width);
        mDIFPaint.setStrokeWidth(width);
        mMACDPaint.setStrokeWidth(width);
    }

    /**
     * 设置文字大小
     */
    public void setTextSize(float textSize) {
        mDEAPaint.setTextSize(textSize);
        mDIFPaint.setTextSize(textSize);
        mMACDPaint.setTextSize(textSize);
    }
}
