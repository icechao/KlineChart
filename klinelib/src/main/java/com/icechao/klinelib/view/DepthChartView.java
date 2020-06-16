package com.icechao.klinelib.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PointF;
import android.graphics.Rect;
import android.graphics.RectF;
import android.support.annotation.Nullable;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;

import com.icechao.klinelib.R;
import com.icechao.klinelib.formatter.IValueFormatter;
import com.icechao.klinelib.formatter.ValueFormatter;
import com.icechao.klinelib.model.MarketDepthPercentItem;
import com.icechao.klinelib.utils.DpUtil;
import com.icechao.klinelib.utils.NumberTools;

import java.util.ArrayList;
import java.util.List;

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

public class DepthChartView extends View implements GestureDetector.OnGestureListener {


    private IValueFormatter valueFormatter = new ValueFormatter();
    private int axisTouchRectBgColor;
    private int axisTouchRectBoundColor;
    private int axisTextColor;
    private int axisTouchTextColor;
    private float ringRectWidth;
    private int padding;

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
     * 文本画笔
     */
    private Paint textPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    /**
     * 要绘制的折线线画笔
     */
    private Paint brokenLineBuyPaint = new Paint(Paint.ANTI_ALIAS_FLAG);

    private Paint brokenLineFillBuyPaint = new Paint(Paint.ANTI_ALIAS_FLAG);

    private Paint brokenLineSellPaint = new Paint(Paint.ANTI_ALIAS_FLAG);

    private Paint brokenLineFillSellPaint = new Paint(Paint.ANTI_ALIAS_FLAG);

    private Paint touchBgPaint = new Paint(Paint.ANTI_ALIAS_FLAG);

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
    private int gridRows;
    /**
     * 竖线数量
     */
    private int gridColumns;
    /**
     * 边框线颜色
     */
    private int borderLineColor;
    /**
     * 边框线的宽度
     */
    private float borderWidth;
    /**
     * 边框文本颜色
     */
    private int mBorderTextColor = Color.GRAY;
    /**
     * 边框文本大小
     */
    private float commonTextSize;
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

    private float horizontalLabelMarginTop;
    private float verticalLabelMarginRight;
    private float horizontalLabelMarginBottom;

    private float ringPadding;

    private GestureDetector detector;

    public DepthChartView(Context context) {
        super(context);
        init(context, null);
    }

    public DepthChartView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    /**
     * 初始化画笔
     */
    private void initPaint() {

        textPaint.setTextSize(commonTextSize);
        textPaint.setTextAlign(Paint.Align.LEFT);
        textPaint.setColor(Color.parseColor("#6D87A8"));

        /**
         * 初始化折线画笔
         */
        brokenLineBuyPaint.setStrokeWidth(brokenLineWidth);
        mBrokenLineBuyColor = Color.parseColor("#03C087");
        brokenLineBuyPaint.setColor(mBrokenLineBuyColor);


        brokenLineFillBuyPaint.setStyle(Paint.Style.FILL);
        brokenLineFillBuyPaint.setStrokeWidth(brokenLineFillWidth);

        int mBrokenLineFillBuyColor = Color.parseColor("#103E41");
        brokenLineFillBuyPaint.setColor(mBrokenLineFillBuyColor);

        brokenLineSellPaint.setStrokeWidth(brokenLineWidth);
        int mBrokenLineSellColor = Color.parseColor("#FF605A");
        brokenLineSellPaint.setColor(mBrokenLineSellColor);

        brokenLineFillSellPaint.setStyle(Paint.Style.FILL);
        brokenLineFillSellPaint.setStrokeWidth(brokenLineFillWidth);
        int mBrokenLineFillSellColor = Color.parseColor("#3D2E33");
        brokenLineFillSellPaint.setColor(mBrokenLineFillSellColor);

        touchBgPaint.setStyle(Paint.Style.FILL);

    }

    private void init(Context context, AttributeSet attributeSet) {

        TypedArray typedArray = context.obtainStyledAttributes(attributeSet, R.styleable.DepthChartView);
        if (null != typedArray) {
            gridRows = typedArray.getInt(R.styleable.DepthChartView_gridLineRows, 5);
            gridColumns = typedArray.getInt(R.styleable.DepthChartView_gridLineColumns, 3);
            borderWidth = typedArray.getDimension(R.styleable.DepthChartView_borderWidth, DpUtil.Dp2Px(context, 2));
            commonTextSize = typedArray.getDimension(R.styleable.DepthChartView_commonTextSize, DpUtil.Dp2Px(context, 10));
            borderTransverseLineColor = typedArray.getColor(R.styleable.DepthChartView_commonTextSize, Color.GRAY);
            borderTransverseLineWidth = typedArray.getDimension(R.styleable.DepthChartView_borderTransverseLineWidth, DpUtil.Dp2Px(context, 2));
            brokenLineWidth = typedArray.getDimension(R.styleable.DepthChartView_brokenLineWidth, DpUtil.Dp2Px(context, 2));
            borderLineColor = typedArray.getColor(R.styleable.DepthChartView_brokenLineWidth, Color.BLACK);
            brokenLineFillWidth = typedArray.getDimension(R.styleable.DepthChartView_brokenLineFillWidth, DpUtil.Dp2Px(context, 2));
            brokenLineMarginLeft = typedArray.getDimension(R.styleable.DepthChartView_brokenLineMarginLeft, DpUtil.Dp2Px(context, 0));
            brokenLineMarginTop = typedArray.getDimension(R.styleable.DepthChartView_brokenLineMarginTop, DpUtil.Dp2Px(context, 20));
            brokenLineMarginBottom = typedArray.getDimension(R.styleable.DepthChartView_brokenLineMarginBottom, DpUtil.Dp2Px(context, 20));
            brokenLinerMarginRight = typedArray.getDimension(R.styleable.DepthChartView_brokenLinerMarginRight, DpUtil.Dp2Px(context, 0));
            horizontalLabelMarginTop = typedArray.getDimension(R.styleable.DepthChartView_horizontalLabelMarginTop, DpUtil.Dp2Px(context, 5));
            horizontalLabelMarginBottom = typedArray.getDimension(R.styleable.DepthChartView_horizontalLabelMarginBottom, DpUtil.Dp2Px(context, 5));
            verticalLabelMarginRight = typedArray.getDimension(R.styleable.DepthChartView_verticalLabelMarginRight, DpUtil.Dp2Px(context, 5));

            touchBgPaint.setColor(typedArray.getColor(R.styleable.DepthChartView_brokenLineWidth, Color.parseColor("#33081724")));
            axisTouchRectBgColor = typedArray.getColor(R.styleable.DepthChartView_axisTouchRectBgColor, Color.parseColor("#E6081724"));
            axisTouchRectBoundColor = typedArray.getColor(R.styleable.DepthChartView_axisTouchRectBoundColor, Color.parseColor("#6D87A8"));
            axisTextColor = typedArray.getColor(R.styleable.DepthChartView_axisTextColor, Color.parseColor("#6D87A8"));
            axisTouchTextColor = typedArray.getColor(R.styleable.DepthChartView_axisTouchTextColor, Color.parseColor("#CFD3E9"));
            ringRectWidth = typedArray.getDimension(R.styleable.DepthChartView_ringRectWidth, DpUtil.Dp2Px(context, 5));
            typedArray.recycle();
        }
        ringPadding = DpUtil.Dp2Px(context, 5);
        initPaint();

        detector = new GestureDetector(getContext(), this);
    }


    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        viewHeight = w;
        viewWidth = h;
        calcWidthHeight();
    }

    private void calcWidthHeight() {
        realDrawWidth = viewWidth - brokenLineMarginLeft - brokenLinerMarginRight;
        realDrawHeight = viewHeight - brokenLineMarginTop - brokenLineMarginBottom;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        calculateMaxMin();
        calculateAverage();
        buysPoint = calcValues(mBuyDataList, realDrawHeight, realDrawWidth, brokenLineMarginLeft, brokenLineMarginTop);
        sellsPoint = calcValues(mSellDataList, realDrawHeight, realDrawWidth, brokenLineMarginLeft, brokenLineMarginTop);

        renderLeft(canvas);
        renderRight(canvas);
        renderLabel(canvas);

        if (touchMode) {
            renderTouch(canvas);
        }
    }

    private void renderTouch(Canvas canvas) {
        boolean safePoint = showBuy ? buysPoint != null && (showIndex < buysPoint.length && showIndex > 0) :
                sellsPoint != null && (showIndex < sellsPoint.length && showIndex > 0);
        if (!safePoint) return;
        RectF center = new RectF(0, 0, 8, 8);
        RectF ring = new RectF(0, 0, ringRectWidth, ringRectWidth);
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
        rect2.set(0, touchPoint.y, rect.width() + 2 * ringPadding, touchPoint.y + h);
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


        rect2.set(0, y, rect.width() + 2 * ringPadding, y + h);
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
        canvas.drawText(touchVol, getWidth() - verticalLabelMarginRight, rect2.centerY() - rect.centerY(), textPaint);

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
        averageYValue = calculateYValue / (gridRows - 0.5);
        calculateXValue = maxXVlaue - minXValue;
        averageXValue = calculateXValue / (gridColumns - 1);
    }

    public void renderLeft(Canvas canvas) {
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

    public void renderRight(Canvas canvas) {
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
    private void renderLabel(Canvas canvas) {

        //纵向的坐标
        float averageHeight = realDrawHeight / gridRows;
        textPaint.setTextAlign(TextPaint.Align.RIGHT);
        textPaint.setColor(axisTextColor);
        for (int i = 0; i < gridRows; i++) {
            float nowadayHeight = averageHeight * i;
            double v = averageYValue * (gridRows - i) + minYValue;
            canvas.drawText(verticalCoordinatePlace(v) + "", getWidth() - verticalLabelMarginRight, nowadayHeight + brokenLineMarginTop, textPaint);
        }
        //横向的坐标
        float averageWidth = realDrawWidth / (gridColumns - 1);
        textPaint.setTextAlign(TextPaint.Align.LEFT);
        for (int i = 0; i < gridColumns; i++) {
            float tempWidth = averageWidth * i;
            float v = (float) (averageXValue * i + minXValue);
            String text = valueFormatter.format(v);
            Rect bounds = new Rect();
            textPaint.getTextBounds(text, 0, text.length(), bounds);

            float y = brokenLineMarginTop + realDrawHeight + bounds.height() + horizontalLabelMarginTop;

            if (i == 0) {
                canvas.drawText(text + "", brokenLineMarginLeft + tempWidth, y, textPaint);
            } else if (i == gridColumns - 1) {
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
    public PointF[] calcValues(List<MarketDepthPercentItem> values, double height, double width, double left, double top) {
        int size = values.size();
        PointF[] points = new PointF[size];
        double maxY = averageYValue * gridRows;
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

    public PointF[] calcValues() {
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
    public void setGridRows(int gridRows) {
        this.gridRows = gridRows;
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
    public void setCommonTextSize(float commonTextSize) {
        this.commonTextSize = commonTextSize;
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
        this.borderWidth = borderWidth;
    }

    /**
     * 边框横线宽度
     */
    public void setBorderTransverseLineWidth(float borderTransverseLineWidth) {
        this.borderTransverseLineWidth = borderTransverseLineWidth;
    }

    /**
     * 折线宽度
     */
    public void setBrokenLineWidth(float brokenLineWidth) {
        this.brokenLineWidth = brokenLineWidth;
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
