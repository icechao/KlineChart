package com.icechao.klinelib.render;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.support.annotation.NonNull;

import com.icechao.klinelib.base.BaseKChartView;
import com.icechao.klinelib.base.BaseRender;
import com.icechao.klinelib.formatter.IValueFormatter;
import com.icechao.klinelib.formatter.ValueFormatter;
import com.icechao.klinelib.utils.Constants;

import java.util.Arrays;

/*************************************************************************
 * Description   :
 *
 * @PackageName  : com.icechao.klinelib.utils
 * @FileName     : RSIDraw.java
 * @Author       : chao
 * @Date         : 2019/4/8
 * @Email        : icechliu@gmail.com
 * @version      : V1
 *************************************************************************/
public class EmaRender extends BaseRender {

    private final String legendText1;
    private final String legendText2;
    private final String legendText3;
    private Paint rsi1Paint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private Paint rsi2Paint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private Paint rsi3Paint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private ValueFormatter valueFormatter = new ValueFormatter();
    private final int indexInterval;

    public EmaRender(Context context) {
        indexInterval = Constants.getCount();
        legendText1 = String.format(Constants.EMA_TOP_TEXT_TAMPLATE, Constants.getEma1());
        legendText2 = String.format(Constants.EMA_TOP_TEXT_TAMPLATE, Constants.getEma2());
        legendText3 = String.format(Constants.EMA_TOP_TEXT_TAMPLATE, Constants.getEma3());
    }


    @Override
    public void render(Canvas canvas, float lastX, float curX, @NonNull BaseKChartView view, int position, float... values) {
        if (Constants.K_EMA_NUMBER_1 > 0 && Float.MIN_VALUE != values[Constants.EMA_INDEX_1] && position != 0) {
            view.renderChildLine(canvas, rsi1Paint, lastX,
                    values[Constants.EMA_INDEX_1],
                    curX, values[Constants.EMA_INDEX_1 + indexInterval]);
        }
        if (Constants.K_EMA_NUMBER_2 > 0 && Float.MIN_VALUE != values[Constants.EMA_INDEX_2] && position != 0) {
            view.renderChildLine(canvas, rsi2Paint, lastX,
                    values[Constants.EMA_INDEX_2],
                    curX, values[Constants.EMA_INDEX_2 + indexInterval]);
        }
        if (Constants.K_EMA_NUMBER_3 > 0 && Float.MIN_VALUE != values[Constants.EMA_INDEX_3] && position != 0) {
            view.renderChildLine(canvas, rsi3Paint, lastX,
                    values[Constants.EMA_INDEX_3],
                    curX, values[Constants.EMA_INDEX_3 + indexInterval]);
        }
    }

    @Override
    public void renderText(@NonNull Canvas canvas, @NonNull BaseKChartView view, float x, float y, int position, float[] values) {
        if (Constants.K_EMA_NUMBER_1 != -1 && Float.MIN_VALUE != values[Constants.INDEX_RSI_1]) {
            canvas.drawText(legendText1, x, y, rsi1Paint);
            x += rsi1Paint.measureText(legendText1);
            String text = getValueFormatter().format(values[Constants.INDEX_RSI_1]);
            canvas.drawText(text + " ", x, y, rsi1Paint);
            x += rsi1Paint.measureText(text);
        }
        if (Constants.K_EMA_NUMBER_2 != -1 && Float.MIN_VALUE != values[Constants.INDEX_RSI_2]) {
            canvas.drawText(legendText2, x, y, rsi2Paint);
            x += rsi2Paint.measureText(legendText2);
            String text = getValueFormatter().format(values[Constants.INDEX_RSI_2]);
            canvas.drawText(text + " ", x, y, rsi2Paint);
            x += rsi2Paint.measureText(text);
        }
        if (Constants.K_EMA_NUMBER_3 != -1 && Float.MIN_VALUE != values[Constants.INDEX_RSI_3]) {
            canvas.drawText(legendText3, x, y, rsi3Paint);
            x += rsi3Paint.measureText(legendText3);
            String text = getValueFormatter().format(values[Constants.INDEX_RSI_3]);
            canvas.drawText(text, x, y, rsi3Paint);
        }
    }

    @Override
    public float getMaxValue(float... values) {
        float[] temp = {values[Constants.EMA_INDEX_1], values[Constants.EMA_INDEX_2], values[Constants.EMA_INDEX_3]};
        Arrays.sort(temp);
        return temp[temp.length - 1];
    }

    @Override
    public float getMinValue(float... values) {
        float[] temp = {values[Constants.EMA_INDEX_1], values[Constants.EMA_INDEX_2], values[Constants.EMA_INDEX_3]};
        Arrays.sort(temp);
        for (float v : temp) {
            if (Float.MIN_VALUE != v) {
                return v;
            }
        }
        return 0;
    }

    @Override
    public void startAnim(BaseKChartView view, float... values) {

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

    public void setRSI1Color(int color) {
        rsi1Paint.setColor(color);
    }

    public void setRSI2Color(int color) {
        rsi2Paint.setColor(color);
    }

    public void setRSI3Color(int color) {
        rsi3Paint.setColor(color);
    }

    /**
     * 设置曲线宽度
     */
    @Override
    public void setLineWidth(float width) {
        rsi1Paint.setStrokeWidth(width);
        rsi2Paint.setStrokeWidth(width);
        rsi3Paint.setStrokeWidth(width);
    }

    /**
     * 设置文字大小
     */
    @Override
    public void setTextSize(float textSize) {
        rsi1Paint.setTextSize(textSize);
        rsi2Paint.setTextSize(textSize);
        rsi3Paint.setTextSize(textSize);
    }
}
