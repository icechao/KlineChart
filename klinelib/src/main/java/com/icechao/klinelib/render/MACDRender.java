package com.icechao.klinelib.render;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.support.annotation.NonNull;

import com.icechao.klinelib.R;
import com.icechao.klinelib.base.BaseRender;
import com.icechao.klinelib.base.BaseKChartView;
import com.icechao.klinelib.formatter.IValueFormatter;
import com.icechao.klinelib.formatter.ValueFormatter;
import com.icechao.klinelib.utils.Constants;
import com.icechao.klinelib.utils.Status;

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

public class MACDRender extends BaseRender {

    private Paint redPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private Paint greenPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private Paint redStrokePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private Paint greenStrokePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
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
    private Status.HollowModel macdStrokeModel = Status.HollowModel.NONE_HOLLOW;

    public MACDRender(Context context) {
        indexInterval = Constants.getCount();
        macdIndexLabel = context.getString(R.string.k_index_macd);
        difIndexLabel = context.getString(R.string.k_index_dif);
        deaIndexLabel = context.getString(R.string.k_index_dea);
        greenStrokePaint.setStyle(Paint.Style.STROKE);
        redStrokePaint.setStyle(Paint.Style.STROKE);
    }


    @Override
    public void render(Canvas canvas, float lastX, float curX, @NonNull BaseKChartView view, int position, float... values) {

        switch (macdStrokeModel) {
            default:
            case NONE_HOLLOW:
                drawMACD(canvas, view, curX, values[Constants.INDEX_MACD_MACD + (position == 0 ? 0 : indexInterval)], redPaint, greenPaint);
                break;
            case ALL_HOLLOW:
                drawMACD(canvas, view, curX, values[Constants.INDEX_MACD_MACD + (position == 0 ? 0 : indexInterval)], redStrokePaint, greenStrokePaint);
                break;
            case DECREASE_HOLLOW:
                if (values.length <= Constants.getCount() || values[Constants.INDEX_MACD_MACD] < values[Constants.INDEX_MACD_MACD + indexInterval]) {
                    drawMACD(canvas, view, curX, values[Constants.INDEX_MACD_MACD + (position == 0 ? 0 : indexInterval)], redPaint, greenPaint);
                    drawMACD(canvas, view, curX, values[Constants.INDEX_MACD_MACD + (position == 0 ? 0 : indexInterval)], redPaint, greenPaint);
                } else {
                    drawMACD(canvas, view, curX, values[Constants.INDEX_MACD_MACD + (position == 0 ? 0 : indexInterval)], redStrokePaint, greenStrokePaint);
                }
                break;
            case INCREASE_HOLLOW:
                if (values.length <= Constants.getCount() || values[Constants.INDEX_MACD_MACD] > values[Constants.INDEX_MACD_MACD + indexInterval]) {
                    drawMACD(canvas, view, curX, values[Constants.INDEX_MACD_MACD + (position == 0 ? 0 : indexInterval)], redPaint, greenPaint);
                    drawMACD(canvas, view, curX, values[Constants.INDEX_MACD_MACD + (position == 0 ? 0 : indexInterval)], redPaint, greenPaint);
                } else {
                    drawMACD(canvas, view, curX, values[Constants.INDEX_MACD_MACD + (position == 0 ? 0 : indexInterval)], redStrokePaint, greenStrokePaint);
                }
                break;
        }

        if (position == 0) {
            return;
        }
        if (Float.MIN_VALUE != values[Constants.INDEX_MACD_DEA]) {
            view.renderChildLine(canvas, deaPaint, lastX, values[Constants.INDEX_MACD_DEA], curX, values[Constants.INDEX_MACD_DEA + indexInterval]);
        }
        if (Float.MIN_VALUE != values[Constants.INDEX_MACD_DIF]) {
            view.renderChildLine(canvas, difPaint, lastX, values[Constants.INDEX_MACD_DIF], curX, values[Constants.INDEX_MACD_DIF + indexInterval]);
        }
    }

    @Override
    public void renderText(@NonNull Canvas canvas, @NonNull BaseKChartView view, float x, float y, int position, float[] values) {
        String text = String.format(Constants.MACD_TOP_TEXT_TAMPLATE, Constants.MACD_S, Constants.MACD_L, Constants.MACD_M);
        canvas.drawText(text, x, y, view.getCommonTextPaint());
        x += macdPaint.measureText(text);

        text = macdIndexLabel + getValueFormatter().format(values[Constants.INDEX_MACD_MACD]) + "  ";
        canvas.drawText(text, x, y, macdPaint);
        x += macdPaint.measureText(text);
        text = difIndexLabel + getValueFormatter().format(values[Constants.INDEX_MACD_DIF]) + "  ";
        canvas.drawText(text, x, y, difPaint);
        x += difPaint.measureText(text);
        text = deaIndexLabel + getValueFormatter().format(values[Constants.INDEX_MACD_DEA]);
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

    /**
     * 画macd
     *
     * @param canvas
     * @param x
     * @param macd
     */
    private void drawMACD(Canvas canvas, BaseKChartView view, float x, float macd, Paint redPaint, Paint greenPaint) {
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
     * 设置macd线stroke widht
     *
     * @param width
     */
    public void setMacdStrokeWidth(float width) {
        redStrokePaint.setStrokeWidth(width);
        greenStrokePaint.setStrokeWidth(width);
    }

    public void setStrokeModel(Status.HollowModel model) {
        this.macdStrokeModel = model;

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
        redStrokePaint.setColor(increaseColor);
        greenPaint.setColor(decreaseColor);
        greenStrokePaint.setColor(decreaseColor);
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
