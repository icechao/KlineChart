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

/**
 * KDJ实现类
 * Created by tifezh on 2016/6/19.
 */

public class KDJDraw extends BaseDraw {

    private Paint mKPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private Paint mDPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private Paint mJPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private ValueFormatter valueFormatter = new ValueFormatter();
    private final int indexInterval;
    private String kIndexLabel,dIndexLabel,jIndexLabel;

    public KDJDraw(Context context) {
        indexInterval = Constants.getCount();
        kIndexLabel = context.getString(R.string.k_index_k);
        dIndexLabel = context.getString(R.string.k_index_d);
        jIndexLabel = context.getString(R.string.k_index_j);
    }


    @Override
    public void drawTranslated(Canvas canvas, float lastX, float curX, @NonNull BaseKLineChartView view, int position, float... values) {
        if (position == 0) {
            return;
        }
        if (Float.MIN_VALUE != values[Constants.INDEX_KDJ_K]) {
            view.drawChildLine(canvas, mKPaint, lastX,
                    values[Constants.INDEX_KDJ_K], curX,
                    values[Constants.INDEX_KDJ_K + indexInterval]);
        }
        if (Float.MIN_VALUE != values[Constants.INDEX_KDJ_D]) {
            view.drawChildLine(canvas, mDPaint, lastX,
                    values[Constants.INDEX_KDJ_D], curX,
                    values[Constants.INDEX_KDJ_D + indexInterval]);
        }
        if (Float.MIN_VALUE != values[Constants.INDEX_KDJ_J]) {
            view.drawChildLine(canvas, mJPaint, lastX,
                    values[Constants.INDEX_KDJ_J], curX,
                    values[Constants.INDEX_KDJ_J + indexInterval]);
        }
    }

    @Override
    public void drawText(@NonNull Canvas canvas, @NonNull BaseKLineChartView view, float x, float y, int position, float[] values) {
//        IKDJ point = (IKDJ) view.getItem(position);
        if (Float.MIN_VALUE != values[Constants.KDJ_K]) {
            String text = String.format(Constants.KDJ_TOP_TEXT_TAMPLATE, Constants.KDJ_K, Constants.KDJ_D, Constants.KDJ_J);
            canvas.drawText(text, x, y, view.getTextPaint());
            x += view.getTextPaint().measureText(text);

            text = kIndexLabel + view.formatValue(values[Constants.KDJ_K]) + " ";
            canvas.drawText(text, x, y, mKPaint);
            x += mKPaint.measureText(text);
            if (Float.MIN_VALUE != values[Constants.KDJ_D]) {
                text = dIndexLabel + view.formatValue(values[Constants.KDJ_D]) + " ";
                canvas.drawText(text, x, y, mDPaint);
                x += mDPaint.measureText(text);
                text = jIndexLabel + view.formatValue(values[Constants.KDJ_J]) + " ";
                canvas.drawText(text, x, y, mJPaint);
            }
        }
    }

    @Override
    public float getMaxValue(float... values) {
        return Math.max(values[Constants.INDEX_KDJ_K], Math.max(values[Constants.INDEX_KDJ_D], values[Constants.INDEX_KDJ_J]));
    }

    @Override
    public float getMinValue(float... values) {
        return Math.min(values[Constants.INDEX_KDJ_K], Math.min(values[Constants.INDEX_KDJ_D], values[Constants.INDEX_KDJ_J]));

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

    private float kdjK;
    private float kdjD;
    private float kdjJ;


    @Override
    public void startAnim(BaseKLineChartView view, float... values) {
        if (kdjK != 0) {
            view.generaterAnimator(kdjK, values[Constants.INDEX_KDJ_K], (valueAnimator) -> kdjK = (float) valueAnimator.getAnimatedValue());
            view.generaterAnimator(kdjD, values[Constants.INDEX_KDJ_D], (valueAnimator) -> kdjD = (float) valueAnimator.getAnimatedValue());
            view.generaterAnimator(kdjJ, values[Constants.INDEX_KDJ_J], (valueAnimator) -> kdjJ = (float) valueAnimator.getAnimatedValue());
        } else {
            kdjK = values[Constants.INDEX_KDJ_K];
            kdjD = values[Constants.INDEX_KDJ_D];
            kdjJ = values[Constants.INDEX_KDJ_J];
        }
    }

    @Override
    public void resetValues() {

    }

    /**
     * 设置K颜色
     */
    public void setKColor(int color) {
        mKPaint.setColor(color);
    }

    /**
     * 设置D颜色
     */
    public void setDColor(int color) {
        mDPaint.setColor(color);
    }

    /**
     * 设置J颜色
     */
    public void setJColor(int color) {
        mJPaint.setColor(color);
    }

    /**
     * 设置曲线宽度
     */
    public void setLineWidth(float width) {
        mKPaint.setStrokeWidth(width);
        mDPaint.setStrokeWidth(width);
        mJPaint.setStrokeWidth(width);
    }

    /**
     * 设置文字大小
     */
    public void setTextSize(float textSize) {
        mKPaint.setTextSize(textSize);
        mDPaint.setTextSize(textSize);
        mJPaint.setTextSize(textSize);
    }
}
