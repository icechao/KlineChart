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
import android.graphics.*;
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
    private int numberLine;
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

        /**初始化边框文本画笔*/
        textPaint = new Paint();
        initPaint(textPaint);

        textPaint.setTextSize(borderTextSize);
        textPaint.setTextAlign(Paint.Align.LEFT);
        textPaint.setColor(ContextCompat.getColor(getContext(), R.color.color_6D87A8));
        /**初始化边框线画笔*/
        borderLinePaint = new Paint();
        initPaint(borderLinePaint);


        borderLinePaint.setTextSize(borderLineTextSize);
        borderLinePaint.setStrokeWidth(borderWidth);
        borderLinePaint.setColor(borderLineColor);

        /**初始化折线画笔*/

        brokenLineBuyPaint = new Paint();
        initPaint(brokenLineBuyPaint);
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


        brokenLineSellPaint = new Paint();
        initPaint(brokenLineSellPaint);
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


        /**横线画笔*/

        horizontalLinePaint = new Paint();
        initPaint(horizontalLinePaint);


        horizontalLinePaint.setStrokeWidth(borderTransverseLineWidth);
        horizontalLinePaint.setColor(borderTransverseLineColor);

    }

    private void init() {
        numberLine = 5;
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


    /**
     * 初始化画笔默认属性
     */
    private void initPaint(Paint paint) {
        paint.setAntiAlias(true);
        paint.setStyle(Paint.Style.STROKE);
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

        /**根据数据绘制线*/
        drawBuyLine(canvas);

        drawSellLine(canvas);

        DrawBorderLineAndText(canvas);


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

            Rect valueRect = new Rect();
            RectF valueRect2 = new RectF();
            textPaint.getTextBounds(touchVol, 0, touchVol.length(), valueRect);
            valueRect2.set(0, touchPoint.y, valueRect.width() + 2 * Dputil.Dp2Px(context, 5), touchPoint.y + h);
            valueRect2.offset(getWidth() - valueRect2.width(), -valueRect2.height() / 2);
            if (valueRect2.bottom > y) {
                valueRect2.offset(0, y - valueRect2.bottom);
            } else if (valueRect2.top < brokenLineMarginTop) {
                valueRect2.offset(0, brokenLineMarginTop - valueRect2.bottom);
            }


            Rect volRect = new Rect();
            RectF volRect2 = new RectF();
            textPaint.getTextBounds(touchValue, 0, touchValue.length(), volRect);
            float baseLine = y + h / 2 - volRect.exactCenterY();

            volRect2.set(0, y, volRect.width() + 2 * Dputil.Dp2Px(context, 5), y + h);
            float offsetX = Math.min(Math.max(0, touchPoint.x - volRect2.width() / 2), getWidth() - volRect2.width());
            volRect2.offset(offsetX, 0);

            center.offset(touchPoint.x, touchPoint.y);
            ring.offset(touchPoint.x, touchPoint.y);

            canvas.drawArc(ring, 0, 360, true, touchBgPaint);
            canvas.drawArc(ring, 0, 360, false, showBuy ? brokenLineBuyPaint : brokenLineSellPaint);
            canvas.drawArc(center, 0, 360, true, showBuy ? brokenLineBuyPaint : brokenLineSellPaint);

            textPaint.setTextAlign(Paint.Align.RIGHT);
            canvas.save();
            canvas.clipRect(valueRect2);
            canvas.drawColor(axisTouchRectBgColor);
            canvas.restore();
            textPaint.setColor(axisTouchRectBoundColor);
            canvas.drawRect(valueRect2, textPaint);
            textPaint.setColor(axisTouchTextColor);
            canvas.drawText(touchVol, getWidth() - verticaLabelMarginRight, valueRect2.centerY() - valueRect.centerY(), textPaint);

            textPaint.setTextAlign(Paint.Align.CENTER);
            canvas.save();
            canvas.clipRect(volRect2);
            canvas.drawColor(axisTouchRectBgColor);
            canvas.restore();
            textPaint.setColor(axisTouchRectBoundColor);
            canvas.drawRect(volRect2, textPaint);
            textPaint.setColor(axisTouchTextColor);
            canvas.drawText(touchValue, volRect2.centerX(), baseLine, textPaint);

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
        averageYValue = calculateYValue / (numberLine - 0.5);
        calculateXValue = maxXVlaue - minXValue;
        averageXValue = calculateXValue / (horinzontalNumberLine - 1);
    }

    public void drawBuyLine(Canvas canvas) {
        Path mPath = new Path();
        Path fillPath = new Path();

        for (int j = 0; j < buysPoint.length; j++) {
            PointF startp = buysPoint[j];
            PointF endp;
            if (j != buysPoint.length - 1) {
                endp = buysPoint[j + 1];
                float wt = (startp.x + endp.x) / 2;
                PointF p3 = new PointF();
                PointF p4 = new PointF();
                p3.y = startp.y;
                p3.x = wt;
                p4.y = endp.y;
                p4.x = wt;
                if (j == 0) {
                    mPath.moveTo(startp.x, startp.y);
                    fillPath.moveTo(startp.x, startp.y);
                }

                mPath.cubicTo(p3.x, p3.y, p4.x, p4.y, endp.x, endp.y);
                fillPath.cubicTo(p3.x, p3.y, p4.x, p4.y, endp.x, endp.y);
            } else {
                mPath.lineTo(startp.x, realDrawHeight + brokenLineMarginTop);
                fillPath.lineTo(startp.x, realDrawHeight + brokenLineMarginTop);
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
            PointF startp = sellsPoint[j];
            PointF endp;
            if (j != sellsPoint.length - 1) {
                endp = sellsPoint[j + 1];
                float wt = (startp.x + endp.x) / 2;
                PointF p3 = new PointF();
                PointF p4 = new PointF();
                p3.y = startp.y;
                p3.x = wt;
                p4.y = endp.y;
                p4.x = wt;
                if (j == 0) {
                    path.moveTo(startp.x, startp.y);

                    fillPath.moveTo(startp.x, startp.y);
                }

                path.cubicTo(p3.x, p3.y, p4.x, p4.y, endp.x, endp.y);
                fillPath.cubicTo(p3.x, p3.y, p4.x, p4.y, endp.x, endp.y);
            } else {
                path.lineTo(brokenLineMarginLeft + realDrawWidth, startp.y);
                fillPath.lineTo(brokenLineMarginLeft + realDrawWidth, startp.y);
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
    private void DrawBorderLineAndText(Canvas canvas) {

        //纵向的坐标
        float averageHeight = realDrawHeight / numberLine;
        textPaint.setTextAlign(TextPaint.Align.RIGHT);
        textPaint.setColor(axisTextColor);
        for (int i = 0; i < numberLine; i++) {
            float nowadayHeight = averageHeight * i;
            double v = averageYValue * (numberLine - i) + minYValue;
            canvas.drawText(verticalCoordinatePlace(v) + "", getWidth() - verticaLabelMarginRight, nowadayHeight + brokenLineMarginTop, textPaint);
        }
        //横向的坐标
        float averageWidth = realDrawWidth / (horinzontalNumberLine - 1);
        textPaint.setTextAlign(TextPaint.Align.LEFT);
        for (int i = 0; i < horinzontalNumberLine; i++) {
            float nowadayWidth = averageWidth * i;
            float v = (float) (averageXValue * i + minXValue);
            String text = valueFormatter.format(v);
            Rect bounds = new Rect();
            textPaint.getTextBounds(text, 0, text.length(), bounds);

            float y = brokenLineMarginTop + realDrawHeight + bounds.height() + horizontalLabelMarginTop;

            if (i == 0) {
                canvas.drawText(text + "", brokenLineMarginLeft + nowadayWidth, y, textPaint);
            } else if (i == horinzontalNumberLine - 1) {
                canvas.drawText(text + "", brokenLineMarginLeft + nowadayWidth - bounds.width(), y, textPaint);
            } else {
                canvas.drawText(text + "", brokenLineMarginLeft + nowadayWidth - bounds.width() / 2, y, textPaint);
            }
        }
    }

    /**
     *
     */
    private String verticalCoordinatePlace(double f) {
        return NumberTools.formatAmount(valueFormatter.format((float) f));
    }


    /**
     * 根据值计算在该值的 x，y坐标
     */
    public PointF[] getPoints(List<MarketDepthPercentItem> values, double height, double width, double left, double top) {
        int size = values.size();
        PointF[] points = new PointF[size];
        double maxY = averageYValue * numberLine;
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
    public void seMargins(float l, float t, float r, float b) {
        brokenLineMarginLeft = Dputil.Dp2Px(context, l);
        brokenLineMarginTop = Dputil.Dp2Px(context, t);
        brokenLinerMarginRight = Dputil.Dp2Px(context, r);
        brokenLineMarginBottom = Dputil.Dp2Px(context, b);
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
     * 图表显示最大值
     */
    public void setMaxYVlaue(float maxYVlaue) {
        this.maxYVlaue = maxYVlaue;
    }

    /**
     * 图表显示最小值
     */
    public void setMinYValue(float minYValue) {
        this.minYValue = minYValue;
    }

    /**
     * 图表横线数量
     */
    public void setNumberLine(int numberLine) {
        this.numberLine = numberLine;
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

    /**
     * 获取框线画笔
     */
    public Paint getBorderLinePaint() {
        return borderLinePaint;
    }

    /**
     * 获取边框文本画笔
     */
    public Paint getTextPaint() {
        return textPaint;
    }

    /**
     * 获取折线画笔
     */
    public Paint getBrokenLinePaint() {
        return brokenLineBuyPaint;
    }

    /**
     * 获取边框横线画笔
     */
    public Paint getHorizontalLinePaint() {
        return horizontalLinePaint;
    }


    public void setSymbol(String symbol) {
        String mSymbol = symbol;
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
