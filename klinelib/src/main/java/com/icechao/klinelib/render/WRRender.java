package com.icechao.klinelib.render;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.support.annotation.NonNull;
import com.icechao.klinelib.base.BaseRender;
import com.icechao.klinelib.base.BaseKChartView;
import com.icechao.klinelib.formatter.IValueFormatter;
import com.icechao.klinelib.formatter.ValueFormatter;
import com.icechao.klinelib.utils.Constants;

/*************************************************************************
 * Description   :
 *
 * @PackageName  : com.icechao.klinelib.utils
 * @FileName     : WRDraw.java
 * @Author       : chao
 * @Date         : 2019/4/8
 * @Email        : icechliu@gmail.com
 * @version      : V1
 *************************************************************************/
public class WRRender extends BaseRender {

    private Paint r1Paint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private Paint r2Paint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private Paint r3Paint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private ValueFormatter valueFormatter = new ValueFormatter();
    private final int indexInterval;
    private final String legendText;

    public WRRender(Context context) {
        indexInterval = Constants.getCount();
        legendText = String.format(Constants.WR_TOP_TEXT_TEMPLATE, Constants.WR_1);

    }


    @Override
    public void render(Canvas canvas, float lastX, float curX, @NonNull BaseKChartView view, int position, float... values) {
        if (Float.MIN_VALUE != values[Constants.INDEX_WR_1]) {
            view.renderChildLine(canvas, r1Paint, lastX,
                    values[Constants.INDEX_WR_1],
                    curX,
                    values[Constants.INDEX_WR_1 + indexInterval]);
        }
    }

    @Override
    public void renderText(@NonNull Canvas canvas, @NonNull BaseKChartView view, float x, float y, int position, float... values) {
//        IWR point = (IWR) view.getItem(position);
        if (Float.MIN_VALUE != values[Constants.INDEX_WR_1]) {
            canvas.drawText(legendText, x, y, view.getCommonTextPaint());
            x += view.getCommonTextPaint().measureText(legendText);
            String temp = getValueFormatter().format(values[Constants.INDEX_WR_1]) + " ";
            canvas.drawText(temp, x, y, r1Paint);
        }
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
    public void startAnim(BaseKChartView view, float... values) {

    }

    @Override
    public float getMaxValue(float... values) {
        return values[Constants.INDEX_WR_1];
    }

    @Override
    public float getMinValue(float... values) {
        return values[Constants.INDEX_WR_1];
    }

    @Override
    public void resetValues() {

    }

    /**
     * 设置%R颜色
     */
    public void setR1Color(int color) {
        r1Paint.setColor(color);
    }

    /**
     * 设置%R颜色
     */
    public void setR2Color(int color) {
        r2Paint.setColor(color);
    }

    /**
     * 设置%R颜色
     */
    public void setR3Color(int color) {
        r3Paint.setColor(color);
    }

    /**
     * 设置曲线宽度
     */
    public void setLineWidth(float width) {
        r1Paint.setStrokeWidth(width);
        r2Paint.setStrokeWidth(width);
        r3Paint.setStrokeWidth(width);
    }

    /**
     * 设置文字大小
     */
    public void setTextSize(float textSize) {
        r1Paint.setTextSize(textSize);
        r2Paint.setTextSize(textSize);
        r3Paint.setTextSize(textSize);
    }
}
