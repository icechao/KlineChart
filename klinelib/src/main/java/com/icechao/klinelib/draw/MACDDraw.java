package com.icechao.klinelib.draw;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.support.annotation.NonNull;
import com.icechao.klinelib.R;
import com.icechao.klinelib.base.BaseDraw;
import com.icechao.klinelib.base.BaseKLineChartView;
import com.icechao.klinelib.base.IValueFormatter;
import com.icechao.klinelib.formatter.ValueFormatter;
import com.icechao.klinelib.utils.Constants;

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

    private Paint redPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private Paint greenPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private Paint difPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private Paint deaPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private Paint macdPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    /**
     * macd 中柱子的宽度
     */
    private float macdWidth = 0;
    private ValueFormatter valueFormatter = new ValueFormatter();
    private final int indexInterval;
    private String macdIndexLabel, difIndexLabel, deaIndexLabel;

    public MACDDraw(Context context) {
        indexInterval = Constants.getCount();
        macdIndexLabel = context.getString(R.string.k_index_macd);
        difIndexLabel = context.getString(R.string.k_index_dif);
        deaIndexLabel = context.getString(R.string.k_index_dea);
    }


    @Override
    public void drawTranslated(Canvas canvas, float lastX, float curX, @NonNull BaseKLineChartView view, int position, float... values) {
        drawMACD(canvas, view, curX, values[Constants.INDEX_MACD_MACD + (position == 0 ? 0 : indexInterval)]);
        if (position == 0) {
            return;
        }
        if (Float.MIN_VALUE != values[Constants.INDEX_MACD_DEA]) {
            view.drawChildLine(canvas, deaPaint, lastX, values[Constants.INDEX_MACD_DEA], curX, values[Constants.INDEX_MACD_DEA + indexInterval]);
        }
        if (Float.MIN_VALUE != values[Constants.INDEX_MACD_DIF]) {
            view.drawChildLine(canvas, difPaint, lastX, values[Constants.INDEX_MACD_DIF], curX, values[Constants.INDEX_MACD_DIF + indexInterval]);
        }
    }

    @Override
    public void drawText(@NonNull Canvas canvas, @NonNull BaseKLineChartView view, float x, float y, int position, float[] values) {
        String text = String.format(Constants.MACD_TOP_TEXT_TAMPLATE, Constants.MACD_S, Constants.MACD_L, Constants.MACD_M);
        canvas.drawText(text, x, y, view.getTextPaint());
        x += macdPaint.measureText(text);

        text = macdIndexLabel + view.formatValue(values[Constants.INDEX_MACD_MACD]) + "  ";
        canvas.drawText(text, x, y, macdPaint);
        x += macdPaint.measureText(text);
        text = difIndexLabel + view.formatValue(values[Constants.INDEX_MACD_DIF]) + "  ";
        canvas.drawText(text, x, y, difPaint);
        x += difPaint.measureText(text);
        text = deaIndexLabel + view.formatValue(values[Constants.INDEX_MACD_DEA]);
        canvas.drawText(text, x, y, deaPaint);
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
        float r = macdWidth / 2 * view.getScaleX();
        if (macd >= 0) {
            canvas.drawRect(x - r, view.getChildY(macd), x + r, view.getChildY(0), redPaint);
        } else {
            canvas.drawRect(x - r, view.getChildY(0), x + r, view.getChildY(macd), greenPaint);
        }
    }

    /**
     * 设置DIF颜色
     */
    public void setDIFColor(int color) {
        this.difPaint.setColor(color);
    }

    /**
     * 设置DEA颜色
     */
    public void setDEAColor(int color) {
        this.deaPaint.setColor(color);
    }

    /**
     * 设置MACD颜色
     */
    public void setMACDColor(int color) {
        this.macdPaint.setColor(color);
    }

    /**
     * 设置MACD的宽度
     *
     * @param MACDWidth
     */
    public void setMACDWidth(float MACDWidth) {
        macdWidth = MACDWidth;
    }

    /**
     * 设置曲线宽度
     */
    public void setLineWidth(float width) {
        deaPaint.setStrokeWidth(width);
        difPaint.setStrokeWidth(width);
        macdPaint.setStrokeWidth(width);
    }

    public void setMacdChartColor(int increaseColor, int decreaseColor) {
        redPaint.setColor(increaseColor);
        greenPaint.setColor(decreaseColor);
    }

    /**
     * 设置文字大小
     */
    public void setTextSize(float textSize) {
        deaPaint.setTextSize(textSize);
        difPaint.setTextSize(textSize);
        macdPaint.setTextSize(textSize);
    }
}
