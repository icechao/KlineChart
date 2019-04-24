package com.icechao.klinelib.base;

/*************************************************************************
 * Description   :
 *
 * @PackageName  : com.icechao.klinelib.view
 * @FileName     : DepthChart.java
 * @Author       : chao
 * @Date         : 2019/1/23
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

import com.icechao.klinelib.entity.MarketDepthPercentItem;
import com.icechao.klinelib.formatter.ValueFormatter;
import com.icechao.klinelib.utils.NumberUtil;
import com.icechao.klinelib.utils.ViewUtil;
import com.icechao.klinelib.R;

import java.util.ArrayList;
import java.util.List;

public class DepthChartView extends View implements GestureDetector.OnGestureListener {


    private IValueFormatter valueFormatter = new ValueFormatter();

    public void setValueFormatter(IValueFormatter valueFormatter) {
        this.valueFormatter = valueFormatter;
    }

    private int length;
    /**
     * View宽度
     */
    private int mViewWidth;
    /**
     * View高度
     */
    private int mViewHeight;
    /**
     * 边框线画笔
     */
    private Paint mBorderLinePaint;
    /**
     * 文本画笔
     */
    private Paint mTextPaint;
    /**
     * 要绘制的折线线画笔
     */
    private Paint mBrokenLineBuyPaint;

    private Paint mBrokenLineFillBuyPaint;

    private Paint mBrokenLineSellPaint;

    private Paint mBrokenLineFillSellPaint;

    private Paint mTouchBgPaint;

    /**
     * 横线画笔
     */
    private Paint mHorizontalLinePaint;
    /**
     * 边框的左边距
     */
    private float mBrokenLineMarginLeft;
    /**
     * 边框的上边距
     */
    private float mBrokenLineMarginTop;
    /**
     * 边框的下边距
     */
    private float mBrokenLineMarginBottom;
    /**
     * 边框的右边距
     */
    private float mBrokenLinerMarginRight;
    /**
     * 需要绘制的宽度
     */
    private float mNeedDrawWidth;
    /**
     * 需要绘制的高度
     */
    private float mNeedDrawHeight;
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
    private int mBorderLineColor;
    /**
     * 边框线的宽度
     */
    private int mBorderWidth;
    /**
     * 边框文本颜色
     */
    private int mBorderTextColor = Color.GRAY;
    /**
     * 边框文本大小
     */
    private float mBorderTextSize;
    /**
     * 边框文本大小
     */
    private float mBorderLineTextSize;
    /**
     * 边框横线颜色
     */
    private int mBorderTransverseLineColor;
    /**
     * 边框横线宽度
     */
    private float mBorderTransverseLineWidth;
    /**
     * 折线颜色
     */
    private int mBrokenLineBuyColor;

    private int mBrokenLineSellColor;

    /**
     * 折线宽度
     */
    private float mBrokenLineWidth;
    /**
     * 折线颜色
     */
    private int mBrokenLineFillBuyColor;

    private int mBrokenLineFillSellColor;

    /**
     * 折线宽度
     */
    private float mBrokenLineFillWidth;
    /**
     * 计算后的x，y坐标
     */
    public PointF[] mPointBuys;

    public PointF[] mPointSells;

    private String mSymbol;

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
        if (mTextPaint == null) {
            mTextPaint = new Paint();
            initPaint(mTextPaint);
        }
        mTextPaint.setTextSize(mBorderTextSize);
        mTextPaint.setTextAlign(Paint.Align.LEFT);
        mTextPaint.setColor(ContextCompat.getColor(getContext(), R.color.color_6D87A8));
        /**初始化边框线画笔*/
        if (mBorderLinePaint == null) {
            mBorderLinePaint = new Paint();
            initPaint(mBorderLinePaint);
        }

        mBorderLinePaint.setTextSize(mBorderLineTextSize);
        mBorderLinePaint.setStrokeWidth(mBorderWidth);
        mBorderLinePaint.setColor(mBorderLineColor);

        /**初始化折线画笔*/
        if (mBrokenLineBuyPaint == null) {
            mBrokenLineBuyPaint = new Paint();
            initPaint(mBrokenLineBuyPaint);
            mBrokenLineBuyPaint.setStrokeWidth(mBrokenLineWidth);
            mBrokenLineBuyColor = ContextCompat.getColor(getContext(), R.color.color_03C087);
            mBrokenLineBuyPaint.setColor(mBrokenLineBuyColor);
        }

        if (mBrokenLineFillBuyPaint == null) {
            mBrokenLineFillBuyPaint = new Paint();
            mBrokenLineFillBuyPaint.setAntiAlias(true);
            mBrokenLineFillBuyPaint.setStyle(Paint.Style.FILL);
            mBrokenLineFillBuyPaint.setStrokeWidth(mBrokenLineFillWidth);
            mBrokenLineFillBuyColor = ContextCompat.getColor(getContext(), R.color.color_103E41);
            mBrokenLineFillBuyPaint.setColor(mBrokenLineFillBuyColor);
        }

        if (mBrokenLineSellPaint == null) {
            mBrokenLineSellPaint = new Paint();
            initPaint(mBrokenLineSellPaint);
            mBrokenLineSellPaint.setStrokeWidth(mBrokenLineWidth);
            mBrokenLineSellColor = ContextCompat.getColor(getContext(), R.color.color_FF605A);
            mBrokenLineSellPaint.setColor(mBrokenLineSellColor);
        }

        if (mBrokenLineFillSellPaint == null) {
            mBrokenLineFillSellPaint = new Paint();
            mBrokenLineFillSellPaint.setAntiAlias(true);
            mBrokenLineFillSellPaint.setStyle(Paint.Style.FILL);
            mBrokenLineFillSellPaint.setStrokeWidth(mBrokenLineFillWidth);
            mBrokenLineFillSellColor = ContextCompat.getColor(getContext(), R.color.color_3D2E33);
            mBrokenLineFillSellPaint.setColor(mBrokenLineFillSellColor);
        }

        if (mTouchBgPaint == null) {
            mTouchBgPaint = new Paint();
            mTouchBgPaint.setAntiAlias(true);
            mTouchBgPaint.setStyle(Paint.Style.FILL);
            mTouchBgPaint.setColor(touchRingBgColor);
        }

        /**横线画笔*/
        if (mHorizontalLinePaint == null) {
            mHorizontalLinePaint = new Paint();
            initPaint(mHorizontalLinePaint);
        }

        mHorizontalLinePaint.setStrokeWidth(mBorderTransverseLineWidth);
        mHorizontalLinePaint.setColor(mBorderTransverseLineColor);

    }

    private void init() {
        numberLine = 5;
        horinzontalNumberLine = 3;
        mBorderWidth = 2;
        mBorderTextSize = ViewUtil.Dp2Px(context, 10);
        mBorderLineTextSize = 2;
        mBorderTransverseLineColor = Color.GRAY;
        mBorderTransverseLineWidth = 2;
        mBrokenLineWidth = 4;
        mBrokenLineFillWidth = 2;
        mBorderLineColor = Color.BLACK;

        mBrokenLineMarginLeft = ViewUtil.Dp2Px(context, 0);
        mBrokenLineMarginTop = ViewUtil.Dp2Px(context, 20);
        mBrokenLineMarginBottom = ViewUtil.Dp2Px(context, 20);
        mBrokenLinerMarginRight = ViewUtil.Dp2Px(context, 0);
        horizontalLabelMarginTop = ViewUtil.Dp2Px(context, 5);
        horizontalLabelMarginBottom = ViewUtil.Dp2Px(context, 5);
        verticaLabelMarginRight = ViewUtil.Dp2Px(context, 5);

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
        mViewHeight = getMeasuredHeight();
        mViewWidth = getMeasuredWidth();


        initNeedDrawWidthAndHeight();
    }

    /**
     * 初始化绘制折线图的宽高
     */
    private void initNeedDrawWidthAndHeight() {
        mNeedDrawWidth = mViewWidth - mBrokenLineMarginLeft - mBrokenLinerMarginRight;
        mNeedDrawHeight = mViewHeight - mBrokenLineMarginTop - mBrokenLineMarginBottom;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        calculateMaxMin();
        calculateAverage();
        mPointBuys = getPoints(mBuyDataList, mNeedDrawHeight, mNeedDrawWidth, mBrokenLineMarginLeft, mBrokenLineMarginTop);
        mPointSells = getPoints(mSellDataList, mNeedDrawHeight, mNeedDrawWidth, mBrokenLineMarginLeft, mBrokenLineMarginTop);

        /**根据数据绘制线*/
        DrawBuyLine(canvas);
        DrawSellLine(canvas);

        /**绘制边框线和边框文本*/
        DrawBorderLineAndText(canvas);


        if (touchMode) {
            boolean safePoint = false;
            safePoint = showBuy ? mPointBuys != null && (showIndex < mPointBuys.length && showIndex > 0) :
                    mPointSells != null && (showIndex < mPointSells.length && showIndex > 0);
            if (!safePoint) return;
            RectF center = new RectF(0, 0, 8, 8);
            RectF ring = new RectF(0, 0, ViewUtil.Dp2Px(context, 15), ViewUtil.Dp2Px(context, 15));
            center.offset(-center.width() / 2, -center.height() / 2);
            ring.offset(-ring.width() / 2, -ring.height() / 2);
            PointF touchPoint = showBuy ? mPointBuys[showIndex] : mPointSells[showIndex];

            MarketDepthPercentItem marketDepthPercentItem = showBuy ? mBuyDataList.get(showIndex) : mSellDataList.get(showIndex);


            String touchVol = verticalCoordinatePlace(marketDepthPercentItem.getAmount());
            String touchValue = valueFormatter.format((float) marketDepthPercentItem.getPrice());

            float y = mBrokenLineMarginTop + mNeedDrawHeight;
            float h = getHeight() - y - horizontalLabelMarginBottom;

            Rect valueRect = new Rect();
            RectF valueRect2 = new RectF();
            mTextPaint.getTextBounds(touchVol, 0, touchVol.length(), valueRect);
            valueRect2.set(0, touchPoint.y, valueRect.width() + 2 * ViewUtil.Dp2Px(context, 5), touchPoint.y + h);
            valueRect2.offset(getWidth() - valueRect2.width(), -valueRect2.height() / 2);
            if (valueRect2.bottom > y) {
                valueRect2.offset(0, y - valueRect2.bottom);
            } else if (valueRect2.top < mBrokenLineMarginTop) {
                valueRect2.offset(0, mBrokenLineMarginTop - valueRect2.bottom);
            }


            Rect volRect = new Rect();
            RectF volRect2 = new RectF();
            mTextPaint.getTextBounds(touchValue, 0, touchValue.length(), volRect);
            float baseLine = y + h / 2 - volRect.exactCenterY();

            volRect2.set(0, y, volRect.width() + 2 * ViewUtil.Dp2Px(context, 5), y + h);
            float offsetX = Math.min(Math.max(0, touchPoint.x - volRect2.width() / 2), getWidth() - volRect2.width());
            volRect2.offset(offsetX, 0);

            center.offset(touchPoint.x, touchPoint.y);
            ring.offset(touchPoint.x, touchPoint.y);

            canvas.drawArc(ring, 0, 360, true, mTouchBgPaint);
            canvas.drawArc(ring, 0, 360, false, showBuy ? mBrokenLineBuyPaint : mBrokenLineSellPaint);
            canvas.drawArc(center, 0, 360, true, showBuy ? mBrokenLineBuyPaint : mBrokenLineSellPaint);

            mTextPaint.setTextAlign(Paint.Align.RIGHT);
            canvas.save();
            canvas.clipRect(valueRect2);
            canvas.drawColor(axisTouchRectBgColor);
            canvas.restore();
            mTextPaint.setColor(axisTouchRectBoundColor);
            canvas.drawRect(valueRect2, mTextPaint);
            mTextPaint.setColor(axisTouchTextColor);
            canvas.drawText(touchVol, getWidth() - verticaLabelMarginRight, valueRect2.centerY() - valueRect.centerY(), mTextPaint);

            mTextPaint.setTextAlign(Paint.Align.CENTER);
            canvas.save();
            canvas.clipRect(volRect2);
            canvas.drawColor(axisTouchRectBgColor);
            canvas.restore();
            mTextPaint.setColor(axisTouchRectBoundColor);
            canvas.drawRect(volRect2, mTextPaint);
            mTextPaint.setColor(axisTouchTextColor);
            canvas.drawText(touchValue, volRect2.centerX(), baseLine, mTextPaint);

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
        /**计算总值*/
        calculateYValue = maxYVlaue - minYValue;
        /**计算框线横线间隔的数据平均值*/
        averageYValue = calculateYValue / (numberLine - 0.5);
        calculateXValue = maxXVlaue - minXValue;
        averageXValue = calculateXValue / (horinzontalNumberLine - 1);
    }


    /**
     * 根据值绘制折线
     */
    public void DrawBuyLine(Canvas canvas) {
        Path mPath = new Path();
        Path mFillPath = new Path();

        for (int j = 0; j < mPointBuys.length; j++) {
            PointF startp = mPointBuys[j];
            PointF endp;
            if (j != mPointBuys.length - 1) {
                endp = mPointBuys[j + 1];
                float wt = (startp.x + endp.x) / 2;
                PointF p3 = new PointF();
                PointF p4 = new PointF();
                p3.y = startp.y;
                p3.x = wt;
                p4.y = endp.y;
                p4.x = wt;
                if (j == 0) {
                    mPath.moveTo(startp.x, startp.y);
                    mFillPath.moveTo(startp.x, startp.y);
                }

                mPath.cubicTo(p3.x, p3.y, p4.x, p4.y, endp.x, endp.y);
                mFillPath.cubicTo(p3.x, p3.y, p4.x, p4.y, endp.x, endp.y);
            } else {
                /**连接到终点x,底部y*/
                mPath.lineTo(startp.x, mNeedDrawHeight + mBrokenLineMarginTop);

                /**连接到终点x,底部y*/
                mFillPath.lineTo(startp.x, mNeedDrawHeight + mBrokenLineMarginTop);
                /**连接到起点x,底部y*/
                mFillPath.lineTo(mBrokenLineMarginLeft, mNeedDrawHeight + mBrokenLineMarginTop);
                /**连接到起点x,起点y*/
                mFillPath.lineTo(mBrokenLineMarginLeft, mPointBuys[0].y);
            }
        }
        canvas.drawPath(mFillPath, mBrokenLineFillBuyPaint);
        canvas.drawPath(mPath, mBrokenLineBuyPaint);

    }

    public void DrawSellLine(Canvas canvas) {
        Path mPath = new Path();
        Path mFillPath = new Path();

        for (int j = 0; j < mPointSells.length; j++) {
            PointF startp = mPointSells[j];
            PointF endp;
            if (j != mPointSells.length - 1) {
                endp = mPointSells[j + 1];
                float wt = (startp.x + endp.x) / 2;
                PointF p3 = new PointF();
                PointF p4 = new PointF();
                p3.y = startp.y;
                p3.x = wt;
                p4.y = endp.y;
                p4.x = wt;
                if (j == 0) {
                    mPath.moveTo(startp.x, startp.y);

                    mFillPath.moveTo(startp.x, startp.y);
                }

                mPath.cubicTo(p3.x, p3.y, p4.x, p4.y, endp.x, endp.y);
                mFillPath.cubicTo(p3.x, p3.y, p4.x, p4.y, endp.x, endp.y);
            } else {
                /**连接到终点x,底部y*/
                mPath.lineTo(mBrokenLineMarginLeft + mNeedDrawWidth, startp.y);

                /**连接到终点x,底部y*/
                mFillPath.lineTo(mBrokenLineMarginLeft + mNeedDrawWidth, startp.y);
                /**连接到起点x,底部y*/
                mFillPath.lineTo(mBrokenLineMarginLeft + mNeedDrawWidth, mNeedDrawHeight + mBrokenLineMarginTop);
                /**连接到起点x,起点y*/
                mFillPath.lineTo(mPointSells[0].x, mBrokenLineMarginTop + mNeedDrawHeight);
            }
        }
        canvas.drawPath(mFillPath, mBrokenLineFillSellPaint);
        canvas.drawPath(mPath, mBrokenLineSellPaint);

    }

    /**
     * 绘制边框坐标
     */
    private void DrawBorderLineAndText(Canvas canvas) {

        //纵向的坐标
        float averageHeight = mNeedDrawHeight / numberLine;
        mTextPaint.setTextAlign(TextPaint.Align.RIGHT);
        mTextPaint.setColor(axisTextColor);
        for (int i = 0; i < numberLine; i++) {
            float nowadayHeight = averageHeight * i;
            double v = averageYValue * (numberLine - i) + minYValue;
            canvas.drawText(verticalCoordinatePlace(v) + "", getWidth() - verticaLabelMarginRight, nowadayHeight + mBrokenLineMarginTop, mTextPaint);
        }
        //横向的坐标
        float averageWidth = mNeedDrawWidth / (horinzontalNumberLine - 1);
        mTextPaint.setTextAlign(TextPaint.Align.LEFT);
        for (int i = 0; i < horinzontalNumberLine; i++) {
            float nowadayWidth = averageWidth * i;
            float v = (float) (averageXValue * i + minXValue);
            String text = valueFormatter.format(v);
            Rect bounds = new Rect();
            mTextPaint.getTextBounds(text, 0, text.length(), bounds);

            float y = mBrokenLineMarginTop + mNeedDrawHeight + bounds.height() + horizontalLabelMarginTop;

            if (i == 0) {
                canvas.drawText(text + "", mBrokenLineMarginLeft + nowadayWidth, y, mTextPaint);
            } else if (i == horinzontalNumberLine - 1) {
                canvas.drawText(text + "", mBrokenLineMarginLeft + nowadayWidth - bounds.width(), y, mTextPaint);
            } else {
                canvas.drawText(text + "", mBrokenLineMarginLeft + nowadayWidth - bounds.width() / 2, y, mTextPaint);
            }
        }
    }

    /**
     *
     */
    private String verticalCoordinatePlace(double f) {
        return NumberUtil.getTradeMarketAmount(valueFormatter.format((float) f));
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
    public void setBrokenLineLTRB(float l, float t, float r, float b) {
        mBrokenLineMarginLeft = ViewUtil.Dp2Px(context, l);
        mBrokenLineMarginTop = ViewUtil.Dp2Px(context, t);
        mBrokenLinerMarginRight = ViewUtil.Dp2Px(context, r);
        mBrokenLineMarginBottom = ViewUtil.Dp2Px(context, b);
    }

    public int getViewWidth() {
        return mViewWidth;
    }

    public int getViewHeight() {
        return mViewHeight;
    }

    public float getBrokenLineLeft() {
        return mBrokenLineMarginLeft;
    }

    public float getBrokenLineTop() {
        return mBrokenLineMarginTop;
    }

    public float getBrokenLineBottom() {
        return mBrokenLineMarginBottom;
    }

    public float getBrokenLinerRight() {
        return mBrokenLinerMarginRight;
    }

    public float getNeedDrawWidth() {
        return mNeedDrawWidth;
    }

    public float getNeedDrawHeight() {
        return mNeedDrawHeight;
    }

    public PointF[] getPoints() {
        return mPointBuys;
    }

    /**
     * 数据data
     */
    public void setBuyDataListValue(List<MarketDepthPercentItem> value) {
        mBuyDataList.clear();
        mBuyDataList.addAll(value);
    }

    /**
     * 数据data
     */
    public void setSellDataListValue(List<MarketDepthPercentItem> value) {
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
        mBorderLineColor = borderLineColor;
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
        mBorderTextSize = ViewUtil.Dp2Px(context, borderTextSize);
    }

    /**
     * 框线横线 颜色
     */
    public void setBorderTransverseLineColor(int borderTransverseLineColor) {
        mBorderTransverseLineColor = borderTransverseLineColor;
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
        mBorderWidth = ViewUtil.Dp2Px(context, borderWidth);
    }

    /**
     * 边框横线宽度
     */
    public void setBorderTransverseLineWidth(float borderTransverseLineWidth) {
        mBorderTransverseLineWidth = ViewUtil.Dp2Px(context, borderTransverseLineWidth);
    }

    /**
     * 折线宽度
     */
    public void setBrokenLineWidth(float brokenLineWidth) {
        mBrokenLineWidth = ViewUtil.Dp2Px(context, brokenLineWidth);
    }

    /**
     * 获取框线画笔
     */
    public Paint getBorderLinePaint() {
        return mBorderLinePaint;
    }

    /**
     * 获取边框文本画笔
     */
    public Paint getTextPaint() {
        return mTextPaint;
    }

    /**
     * 获取折线画笔
     */
    public Paint getBrokenLinePaint() {
        return mBrokenLineBuyPaint;
    }

    /**
     * 获取边框横线画笔
     */
    public Paint getHorizontalLinePaint() {
        return mHorizontalLinePaint;
    }


    public void setSymbol(String symbol) {
        mSymbol = symbol;
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
        if (mPointSells != null && mPointSells.length > 0 &&
                mPointBuys != null && mPointBuys.length > 0) {
            touchMode = true;
            float x = e.getX();
            float minDistanceSell = Integer.MAX_VALUE;
            float minDistanceBuy = Integer.MAX_VALUE;
            int indexSell = 0;
            int indexBuy = 0;

            for (int i = 0; i < mPointSells.length; i++) {
                float temp = Math.abs(mPointSells[i].x - x);
                if (temp < minDistanceSell) {
                    minDistanceSell = temp;
                    indexSell = i;
                }
            }
            for (int i = 0; i < mPointBuys.length; i++) {
                float temp = Math.abs(mPointBuys[i].x - x);
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
