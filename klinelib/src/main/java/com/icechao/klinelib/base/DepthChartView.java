package com.icechao.klinelib.base;

/*************************************************************************
 * Description   :
 *
 * @PackageName  : com.icechao.klinelib.view
 * @FileName     : DepthChart.java
 * @Author       : chao
 * @Date         : 2019/4/23
 * @Email        : icechliu@gmail.com
 * @version      : V1
 *************************************************************************/

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PointF;
import android.graphics.Rect;
import android.graphics.RectF;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;

import com.icechao.klinelib.R;
import com.icechao.klinelib.entity.MarketDepthPercentItem;
import com.icechao.klinelib.formatter.ValueFormatter;
import com.icechao.klinelib.utils.Dputil;
import com.icechao.klinelib.utils.NumberTools;

import java.util.ArrayList;
import java.util.List;

public class DepthChartView extends View implements GestureDetector.OnGestureListener {


    private IValueFormatter valueFormatter = new ValueFormatter();

    public void setValueFormatter(IValueFormatter valueFormatter) {
        this.valueFormatter = valueFormatter;
    }

    /**
     * View宽度
     */
    private int viewWidth;
    /**
     * View高度
     */
    private int viewHeight;
    /**
     * 边框线画笔
     */
    private Paint borderLinePaint;
    /**
     * 文本画笔
     */
    private Paint textPaint;
    /**
     * 要绘制的折线线画笔
     */
    private Paint brokenLineBuyPaint;

    private Paint brokenLineFillBuyPaint;

    private Paint brokenLineSellPaint;

    private Paint brokenLineFillSellPaint;

    private Paint touchBgPaint;

    /**
     * 横线画笔
     */
    private Paint horizontalLinePaint;
    /**
     * 边框的左边距
     */
    private float brokenLineMarginLeft;
    /**
     * 边框的上边距
     */
    private float brokenLineMarginTop;
    /**
     * 边框的下边距
     */
    private float brokenLineMarginBottom;
    /**
     * 边框的右边距
     */
    private float brokenLinerMarginRight;
    /**
     * 需要绘制的宽度
     */
    private float realDrawWidth;
    /**
     * 需要绘制的高度
     */
    private float realDrawHeight;
    /**
     * 数据值
     */
    private List<MarketDepthPercentItem> mBuyDataList = new ArrayList<>();

    private List<MarketDepthPercentItem> mSellDataList = new ArrayList<>();
    /**
     * 图表的y轴最大值
     */
    private double maxYVlaue;
    /**
     * 图表的y轴最小值
     */
    private double minYValue;
    /**
     * 图表的y轴最大值
     */
    private double maxXVlaue;
    /**
     * 图表的y轴最小值
     */
    private double minXValue;
    /**
     * 要计算的总值
     */
    private double calculateXValue;
    /**
     * 框线平均值
     */
    private double averageXValue;

    /**
     * 要计算的总值
     */
    private double calculateYValue;
    /**
     * 框线平均值
     */
    private double averageYValue;
    /**
     * 横线数量
     */
    private int lineCount;
    /**
     * 竖线数量
     */
    private int horinzontalNumberLine;
    /**
     * 边框线颜色
     */
    private int borderLineColor;
    /**
     * 边框线的宽度
     */
    private int borderWidth;
    /**
     * 边框文本颜色
     */
    private int mBorderTextColor = Color.GRAY;
    /**
     * 边框文本大小
     */
    private float borderTextSize;
    /**
     * 边框文本大小
     */
    private float borderLineTextSize;
    /**
     * 边框横线颜色
     */
    private int borderTransverseLineColor;
    /**
     * 边框横线宽度
     */
    private float borderTransverseLineWidth;
    /**
     * 折线颜色
     */
    private int mBrokenLineBuyColor;

    /**
     * 折线宽度
     */
    private float brokenLineWidth;

    /**
     * 折线宽度
     */
    private float brokenLineFillWidth;
    /**
     * 计算后的x，y坐标
     */
    public PointF[] buysPoint;

    public PointF[] sellsPoint;

    private int horizontalLabelMarginTop;
    private int verticaLabelMarginRight;
    private int horizontalLabelMarginBottom;

    private GestureDetector detector;


    private int axisTextColor = Color.parseColor("#6D87A8");
    private int axisTouchTextColor = Color.parseColor("#CFD3E9");
    private int axisTouchRectBgColor = Color.parseColor("#E6081724");
    private int axisTouchRectBoundColor = Color.parseColor("#6D87A8");
    private int touchRingBgColor = Color.parseColor("#33081724");
    private Context context;

    public DepthChartView(Context context) {
        super(context);
        this.context = context;
        init();
    }

    public DepthChartView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        init();
    }

    /**
     * 初始化画笔
     */
    private void initPaint() {

        textPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        textPaint.setTextSize(borderTextSize);
        textPaint.setTextAlign(Paint.Align.LEFT);
        textPaint.setColor(ContextCompat.getColor(getContext(), R.color.color_6D87A8));
        borderLinePaint = new Paint(Paint.ANTI_ALIAS_FLAG);


        borderLinePaint.setTextSize(borderLineTextSize);
        borderLinePaint.setStrokeWidth(borderWidth);
        borderLinePaint.setColor(borderLineColor);

        /**
         * 初始化折线画笔
         */

        brokenLineBuyPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        brokenLineBuyPaint.setStrokeWidth(brokenLineWidth);
        mBrokenLineBuyColor = ContextCompat.getColor(getContext(), R.color.color_03C087);
        brokenLineBuyPaint.setColor(mBrokenLineBuyColor);


        brokenLineFillBuyPaint = new Paint();
        brokenLineFillBuyPaint.setAntiAlias(true);
        brokenLineFillBuyPaint.setStyle(Paint.Style.FILL);
        brokenLineFillBuyPaint.setStrokeWidth(brokenLineFillWidth);
        /**
         * 折线颜色
         */
        int mBrokenLineFillBuyColor = ContextCompat.getColor(getContext(), R.color.color_103E41);
        brokenLineFillBuyPaint.setColor(mBrokenLineFillBuyColor);


        brokenLineSellPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        brokenLineSellPaint.setStrokeWidth(brokenLineWidth);
        int mBrokenLineSellColor = ContextCompat.getColor(getContext(), R.color.color_FF605A);
        brokenLineSellPaint.setColor(mBrokenLineSellColor);


        brokenLineFillSellPaint = new Paint();
        brokenLineFillSellPaint.setAntiAlias(true);
        brokenLineFillSellPaint.setStyle(Paint.Style.FILL);
        brokenLineFillSellPaint.setStrokeWidth(brokenLineFillWidth);
        int mBrokenLineFillSellColor = ContextCompat.getColor(getContext(), R.color.color_3D2E33);
        brokenLineFillSellPaint.setColor(mBrokenLineFillSellColor);


        touchBgPaint = new Paint();
        touchBgPaint.setAntiAlias(true);
        touchBgPaint.setStyle(Paint.Style.FILL);
        touchBgPaint.setColor(touchRingBgColor);


        horizontalLinePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        horizontalLinePaint.setStrokeWidth(borderTransverseLineWidth);
        horizontalLinePaint.setColor(borderTransverseLineColor);

    }

    private void init() {
        lineCount = 5;
        horinzontalNumberLine = 3;
        borderWidth = 2;
        borderTextSize = Dputil.Dp2Px(context, 10);
        borderLineTextSize = 2;
        borderTransverseLineColor = Color.GRAY;
        borderTransverseLineWidth = 2;
        brokenLineWidth = 4;
        brokenLineFillWidth = 2;
        borderLineColor = Color.BLACK;

        brokenLineMarginLeft = Dputil.Dp2Px(context, 0);
        brokenLineMarginTop = Dputil.Dp2Px(context, 20);
        brokenLineMarginBottom = Dputil.Dp2Px(context, 20);
        brokenLinerMarginRight = Dputil.Dp2Px(context, 0);
        horizontalLabelMarginTop = Dputil.Dp2Px(context, 5);
        horizontalLabelMarginBottom = Dputil.Dp2Px(context, 5);
        verticaLabelMarginRight = Dputil.Dp2Px(context, 5);

        initPaint();

        detector = new GestureDetector(getContext(), this);
    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        viewHeight = getMeasuredHeight();
        viewWidth = getMeasuredWidth();


        initNeedDrawWidthAndHeight();
    }

    /**
     * 初始化绘制折线图的宽高
     */
    private void initNeedDrawWidthAndHeight() {
        realDrawWidth = viewWidth - brokenLineMarginLeft - brokenLinerMarginRight;
        realDrawHeight = viewHeight - brokenLineMarginTop - brokenLineMarginBottom;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        calculateMaxMin();
        calculateAverage();
        buysPoint = getPoints(mBuyDataList, realDrawHeight, realDrawWidth, brokenLineMarginLeft, brokenLineMarginTop);
        sellsPoint = getPoints(mSellDataList, realDrawHeight, realDrawWidth, brokenLineMarginLeft, brokenLineMarginTop);

        drawBuyLine(canvas);
        drawSellLine(canvas);

        drawBorderText(canvas);


        if (touchMode) {
            boolean safePoint = showBuy ? buysPoint != null && (showIndex < buysPoint.length && showIndex > 0) :
                    sellsPoint != null && (showIndex < sellsPoint.length && showIndex > 0);
            if (!safePoint) return;
            RectF center = new RectF(0, 0, 8, 8);
            RectF ring = new RectF(0, 0, Dputil.Dp2Px(context, 15), Dputil.Dp2Px(context, 15));
            center.offset(-center.width() / 2, -center.height() / 2);
            ring.offset(-ring.width() / 2, -ring.height() / 2);
            PointF touchPoint = showBuy ? buysPoint[showIndex] : sellsPoint[showIndex];

            MarketDepthPercentItem marketDepthPercentItem = showBuy ? mBuyDataList.get(showIndex) : mSellDataList.get(showIndex);


            String touchVol = verticalCoordinatePlace(marketDepthPercentItem.getAmount());
            String touchValue = valueFormatter.format((float) marketDepthPercentItem.getPrice());

            float y = brokenLineMarginTop + realDrawHeight;
            float h = getHeight() - y - horizontalLabelMarginBottom;

            Rect rect = new Rect();
            RectF rect2 = new RectF();
            textPaint.getTextBounds(touchVol, 0, touchVol.length(), rect);
            rect2.set(0, touchPoint.y, rect.width() + 2 * Dputil.Dp2Px(context, 5), touchPoint.y + h);
            rect2.offset(getWidth() - rect2.width(), -rect2.height() / 2);
            if (rect2.bottom > y) {
                rect2.offset(0, y - rect2.bottom);
            } else if (rect2.top < brokenLineMarginTop) {
                rect2.offset(0, brokenLineMarginTop - rect2.bottom);
            }


            rect = new Rect();
            rect2 = new RectF();
            textPaint.getTextBounds(touchValue, 0, touchValue.length(), rect);
            float baseLine = y + h / 2 - rect.exactCenterY();

            rect2.set(0, y, rect.width() + 2 * Dputil.Dp2Px(context, 5), y + h);
            float offsetX = Math.min(Math.max(0, touchPoint.x - rect2.width() / 2), getWidth() - rect2.width());
            rect2.offset(offsetX, 0);

            center.offset(touchPoint.x, touchPoint.y);
            ring.offset(touchPoint.x, touchPoint.y);

            canvas.drawArc(ring, 0, 360, true, touchBgPaint);
            canvas.drawArc(ring, 0, 360, false, showBuy ? brokenLineBuyPaint : brokenLineSellPaint);
            canvas.drawArc(center, 0, 360, true, showBuy ? brokenLineBuyPaint : brokenLineSellPaint);

            textPaint.setTextAlign(Paint.Align.RIGHT);
            canvas.save();
            canvas.clipRect(rect2);
            canvas.drawColor(axisTouchRectBgColor);
            canvas.restore();
            textPaint.setColor(axisTouchRectBoundColor);
            canvas.drawRect(rect2, textPaint);
            textPaint.setColor(axisTouchTextColor);
            canvas.drawText(touchVol, getWidth() - verticaLabelMarginRight, rect2.centerY() - rect.centerY(), textPaint);

            textPaint.setTextAlign(Paint.Align.CENTER);
            canvas.save();
            canvas.clipRect(rect2);
            canvas.drawColor(axisTouchRectBgColor);
            canvas.restore();
            textPaint.setColor(axisTouchRectBoundColor);
            canvas.drawRect(rect2, textPaint);
            textPaint.setColor(axisTouchTextColor);
            canvas.drawText(touchValue, rect2.centerX(), baseLine, textPaint);

        }
    }

    private void calculateMaxMin() {
        if (mSellDataList != null && mSellDataList.size() > 0) {
            maxXVlaue = mSellDataList.get(mSellDataList.size() - 1).getPrice();
            maxYVlaue = mSellDataList.get(mSellDataList.size() - 1).getAmount();
        }
        if (mBuyDataList != null && mBuyDataList.size() > 0) {
            minXValue = mBuyDataList.get(0).getPrice();
            double max = mBuyDataList.get(0).getAmount();
            maxYVlaue = max > maxYVlaue ? max : maxYVlaue;
        }
        minYValue = 0;
    }

    private void calculateAverage() {
        calculateYValue = maxYVlaue - minYValue;
        averageYValue = calculateYValue / (lineCount - 0.5);
        calculateXValue = maxXVlaue - minXValue;
        averageXValue = calculateXValue / (horinzontalNumberLine - 1);
    }

    public void drawBuyLine(Canvas canvas) {
        Path mPath = new Path();
        Path fillPath = new Path();

        for (int j = 0; j < buysPoint.length; j++) {
            PointF startPoint = buysPoint[j];
            PointF endPoint;
            if (j != buysPoint.length - 1) {
                endPoint = buysPoint[j + 1];
                float wt = (startPoint.x + endPoint.x) / 2;
                PointF p3 = new PointF();
                PointF p4 = new PointF();
                p3.y = startPoint.y;
                p3.x = wt;
                p4.y = endPoint.y;
                p4.x = wt;
                if (j == 0) {
                    mPath.moveTo(startPoint.x, startPoint.y);
                    fillPath.moveTo(startPoint.x, startPoint.y);
                }

                mPath.cubicTo(p3.x, p3.y, p4.x, p4.y, endPoint.x, endPoint.y);
                fillPath.cubicTo(p3.x, p3.y, p4.x, p4.y, endPoint.x, endPoint.y);
            } else {
                mPath.lineTo(startPoint.x, realDrawHeight + brokenLineMarginTop);
                fillPath.lineTo(startPoint.x, realDrawHeight + brokenLineMarginTop);
                fillPath.lineTo(brokenLineMarginLeft, realDrawHeight + brokenLineMarginTop);
                fillPath.lineTo(brokenLineMarginLeft, buysPoint[0].y);
            }
        }
        canvas.drawPath(fillPath, brokenLineFillBuyPaint);
        canvas.drawPath(mPath, brokenLineBuyPaint);

    }

    public void drawSellLine(Canvas canvas) {
        Path path = new Path();
        Path fillPath = new Path();

        for (int j = 0; j < sellsPoint.length; j++) {
            PointF startPoint = sellsPoint[j];
            PointF endPoint;
            if (j != sellsPoint.length - 1) {
                endPoint = sellsPoint[j + 1];
                float wt = (startPoint.x + endPoint.x) / 2;
                PointF p3 = new PointF();
                PointF p4 = new PointF();
                p3.y = startPoint.y;
                p3.x = wt;
                p4.y = endPoint.y;
                p4.x = wt;
                if (j == 0) {
                    path.moveTo(startPoint.x, startPoint.y);
                    fillPath.moveTo(startPoint.x, startPoint.y);
                }

                path.cubicTo(p3.x, p3.y, p4.x, p4.y, endPoint.x, endPoint.y);
                fillPath.cubicTo(p3.x, p3.y, p4.x, p4.y, endPoint.x, endPoint.y);
            } else {
                path.lineTo(brokenLineMarginLeft + realDrawWidth, startPoint.y);
                fillPath.lineTo(brokenLineMarginLeft + realDrawWidth, startPoint.y);
                fillPath.lineTo(brokenLineMarginLeft + realDrawWidth, realDrawHeight + brokenLineMarginTop);
                fillPath.lineTo(sellsPoint[0].x, brokenLineMarginTop + realDrawHeight);
            }
        }
        canvas.drawPath(fillPath, brokenLineFillSellPaint);
        canvas.drawPath(path, brokenLineSellPaint);

    }

    /**
     * 绘制边框坐标
     */
    private void drawBorderText(Canvas canvas) {

        //纵向的坐标
        float averageHeight = realDrawHeight / lineCount;
        textPaint.setTextAlign(TextPaint.Align.RIGHT);
        textPaint.setColor(axisTextColor);
        for (int i = 0; i < lineCount; i++) {
            float nowadayHeight = averageHeight * i;
            double v = averageYValue * (lineCount - i) + minYValue;
            canvas.drawText(verticalCoordinatePlace(v) + "", getWidth() - verticaLabelMarginRight, nowadayHeight + brokenLineMarginTop, textPaint);
        }
        //横向的坐标
        float averageWidth = realDrawWidth / (horinzontalNumberLine - 1);
        textPaint.setTextAlign(TextPaint.Align.LEFT);
        for (int i = 0; i < horinzontalNumberLine; i++) {
            float tempWidth = averageWidth * i;
            float v = (float) (averageXValue * i + minXValue);
            String text = valueFormatter.format(v);
            Rect bounds = new Rect();
            textPaint.getTextBounds(text, 0, text.length(), bounds);

            float y = brokenLineMarginTop + realDrawHeight + bounds.height() + horizontalLabelMarginTop;

            if (i == 0) {
                canvas.drawText(text + "", brokenLineMarginLeft + tempWidth, y, textPaint);
            } else if (i == horinzontalNumberLine - 1) {
                canvas.drawText(text + "", brokenLineMarginLeft + tempWidth - bounds.width(), y, textPaint);
            } else {
                canvas.drawText(text + "", brokenLineMarginLeft + tempWidth - (bounds.width() >> 1), y, textPaint);
            }
        }
    }

    private String verticalCoordinatePlace(double f) {
        return NumberTools.formatAmount(valueFormatter.format((float) f));
    }


    /**
     * 计算在该值的 xy
     */
    public PointF[] getPoints(List<MarketDepthPercentItem> values, double height, double width, double left, double top) {
        int size = values.size();
        PointF[] points = new PointF[size];
        double maxY = averageYValue * lineCount;
        for (int i = 0; i < size; i++) {
            double valueY = values.get(i).getAmount() - minYValue;
            //计算每点高度所以对应的值
            double meanY = (maxY - minYValue) / height;
            //获取要绘制的高度
            float drawHeight = (float) (valueY / meanY);
            int pointY = (int) (height + top - drawHeight);

            double valueX = values.get(i).getPrice() - minXValue;
            double meanX = (maxXVlaue - minXValue) / width;
            float drawWidth = (float) (valueX / meanX);

            int pointX = (int) (drawWidth + left);
            PointF point = new PointF(pointX, pointY);
            points[i] = point;
        }
        return points;
    }


    /**
     * 设置边框左上右下边距
     */
    public void seMargins(float left, float top, float right, float bottom) {
        brokenLineMarginLeft = left;
        brokenLineMarginTop = top;
        brokenLinerMarginRight = right;
        brokenLineMarginBottom = bottom;
    }

    public int getViewWidth() {
        return viewWidth;
    }

    public int getViewHeight() {
        return viewHeight;
    }

    public float getBrokenLineLeft() {
        return brokenLineMarginLeft;
    }

    public float getBrokenLineTop() {
        return brokenLineMarginTop;
    }

    public float getBrokenLineBottom() {
        return brokenLineMarginBottom;
    }

    public float getBrokenLinerRight() {
        return brokenLinerMarginRight;
    }

    public float getRealDrawWidth() {
        return realDrawWidth;
    }

    public float getRealDrawHeight() {
        return realDrawHeight;
    }

    public PointF[] getPoints() {
        return buysPoint;
    }

    /**
     * 数据data
     */
    public void setBuyList(List<MarketDepthPercentItem> value) {
        mBuyDataList.clear();
        mBuyDataList.addAll(value);
    }

    /**
     * 数据data
     */
    public void setSellList(List<MarketDepthPercentItem> value) {
        mSellDataList.clear();
        mSellDataList.addAll(value);
    }

    /**
     * 图表横线数量
     */
    public void setLineCount(int lineCount) {
        this.lineCount = lineCount;
    }

    /**
     * 边框线颜色
     */
    public void setBorderLineColor(int borderLineColor) {
        this.borderLineColor = borderLineColor;
    }

    /**
     * 边框文本颜色
     */
    public void setBorderTextColor(int borderTextColor) {
        mBorderTextColor = borderTextColor;
    }

    /**
     * 边框文本大小
     */
    public void setBorderTextSize(float borderTextSize) {
        this.borderTextSize = Dputil.Dp2Px(context, borderTextSize);
    }

    /**
     * 框线横线 颜色
     */
    public void setBorderTransverseLineColor(int borderTransverseLineColor) {
        this.borderTransverseLineColor = borderTransverseLineColor;
    }

    /**
     * 边框内折线颜色
     */
    public void setBrokenLineColor(int brokenLineColor) {
        mBrokenLineBuyColor = brokenLineColor;
    }

    /**
     * 边框线宽度
     */
    public void setBorderWidth(float borderWidth) {
        this.borderWidth = Dputil.Dp2Px(context, borderWidth);
    }

    /**
     * 边框横线宽度
     */
    public void setBorderTransverseLineWidth(float borderTransverseLineWidth) {
        this.borderTransverseLineWidth = Dputil.Dp2Px(context, borderTransverseLineWidth);
    }

    /**
     * 折线宽度
     */
    public void setBrokenLineWidth(float brokenLineWidth) {
        this.brokenLineWidth = Dputil.Dp2Px(context, brokenLineWidth);
    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {
        detector.onTouchEvent(event);
        switch (event.getAction() & MotionEvent.ACTION_MASK) {
            case MotionEvent.ACTION_DOWN:
                break;
            case MotionEvent.ACTION_MOVE:
                if (touchMode) {
                    calcTouchPoint(event);
                }
                break;
            case MotionEvent.ACTION_CANCEL:
            case MotionEvent.ACTION_UP:
                (getParent()).requestDisallowInterceptTouchEvent(false);
                break;

        }

        return true;
    }

    @Override
    public boolean onDown(MotionEvent e) {
        return true;
    }

    @Override
    public void onShowPress(MotionEvent e) {

    }

    @Override
    public boolean onSingleTapUp(MotionEvent e) {
        touchMode = false;
        this.invalidate();
        return true;
    }

    @Override
    public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
        return true;
    }

    private boolean touchMode = false;
    private boolean showBuy;
    private int showIndex;

    @Override
    public void onLongPress(MotionEvent e) {
        calcTouchPoint(e);
        (getParent()).requestDisallowInterceptTouchEvent(touchMode);
    }

    private void calcTouchPoint(MotionEvent e) {
        if (sellsPoint != null && sellsPoint.length > 0 &&
                buysPoint != null && buysPoint.length > 0) {
            touchMode = true;
            float x = e.getX();
            float minDistanceSell = Integer.MAX_VALUE;
            float minDistanceBuy = Integer.MAX_VALUE;
            int indexSell = 0;
            int indexBuy = 0;

            for (int i = 0; i < sellsPoint.length; i++) {
                float temp = Math.abs(sellsPoint[i].x - x);
                if (temp < minDistanceSell) {
                    minDistanceSell = temp;
                    indexSell = i;
                }
            }
            for (int i = 0; i < buysPoint.length; i++) {
                float temp = Math.abs(buysPoint[i].x - x);
                if (temp < minDistanceBuy) {
                    minDistanceBuy = temp;
                    indexBuy = i;
                }
            }

            showBuy = minDistanceBuy < minDistanceSell;
            showIndex = showBuy ? indexBuy : indexSell;
            invalidate();
        }
    }

    @Override
    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
        return true;
    }
}
