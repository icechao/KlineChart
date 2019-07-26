package com.icechao.klinelib.draw;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.support.annotation.NonNull;
import com.icechao.klinelib.base.BaseDraw;
import com.icechao.klinelib.base.BaseKLineChartView;
import com.icechao.klinelib.base.IValueFormatter;
import com.icechao.klinelib.formatter.ValueFormatter;
import com.icechao.klinelib.utils.Constants;

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
public class RSIDraw extends BaseDraw {

    private final String lengentText;
    private Paint mRSI1Paint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private Paint mRSI2Paint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private Paint mRSI3Paint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private ValueFormatter valueFormatter = new ValueFormatter();
    private final int indexInterval;

    public RSIDraw(Context context) {
        indexInterval = Constants.getCount();
        lengentText = String.format(Constants.RSI_TOP_TEXT_TAMPLATE, Constants.RSI_1);

    }


    @Override
    public void drawTranslated(Canvas canvas, float lastX, float curX, @NonNull BaseKLineChartView view, int position, float... values) {
        if (Float.MIN_VALUE != values[Constants.INDEX_RSI_1] && position != 0) {
            view.drawChildLine(canvas, mRSI1Paint, lastX,
                    values[Constants.INDEX_RSI_1],
                    curX, values[Constants.INDEX_RSI_1 + indexInterval]);
        }
    }

    @Override
    public void drawText(@NonNull Canvas canvas, @NonNull BaseKLineChartView view, float x, float y, int position, float[] values) {
        if (Float.MIN_VALUE != values[Constants.INDEX_RSI_1]) {
            Paint textPaint = view.getTextPaint();
            canvas.drawText(lengentText, x, y, textPaint);
            x += textPaint.measureText(lengentText);
            String text = view.formatValue(values[Constants.INDEX_RSI_1]);
            canvas.drawText(text, x, y, mRSI1Paint);
        }
    }

    @Override
    public float getMaxValue(float... values) {
        return values[Constants.INDEX_RSI_1];
    }

    @Override
    public float getMinValue(float... values) {
        return values[Constants.INDEX_RSI_1];
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

    public void setRSI1Color(int color) {
        mRSI1Paint.setColor(color);
    }

    public void setRSI2Color(int color) {
        mRSI2Paint.setColor(color);
    }

    public void setRSI3Color(int color) {
        mRSI3Paint.setColor(color);
    }

    /**
     * 设置曲线宽度
     */
    public void setLineWidth(float width) {
        mRSI1Paint.setStrokeWidth(width);
        mRSI2Paint.setStrokeWidth(width);
        mRSI3Paint.setStrokeWidth(width);
    }

    /**
     * 设置文字大小
     */
    public void setTextSize(float textSize) {
        mRSI2Paint.setTextSize(textSize);
        mRSI3Paint.setTextSize(textSize);
        mRSI1Paint.setTextSize(textSize);
    }
}
