package com.icechao.klinelib.base;

import android.animation.ValueAnimator;
import android.content.Context;
import android.database.DataSetObserver;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RadialGradient;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Shader;
import android.support.v4.view.GestureDetectorCompat;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;

import com.icechao.klinelib.adapter.KLineChartAdapter;
import com.icechao.klinelib.formatter.DateFormatter;
import com.icechao.klinelib.formatter.IDateTimeFormatter;
import com.icechao.klinelib.formatter.IValueFormatter;
import com.icechao.klinelib.formatter.ValueFormatter;
import com.icechao.klinelib.render.MainRender;
import com.icechao.klinelib.render.VolumeRender;
import com.icechao.klinelib.utils.Constants;
import com.icechao.klinelib.utils.LogUtil;
import com.icechao.klinelib.utils.NumberTools;
import com.icechao.klinelib.utils.SlidListener;
import com.icechao.klinelib.utils.Status;
import com.icechao.klinelib.utils.Status.YLabelModel;

import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/*************************************************************************
 * Description   : 绘制分类,BaseKLineChartView只绘制所有视图通用的图形 其他区域的绘制分别由对应的绘制类完成
 *
 * @PackageName  : com.icechao.klinelib.base
 * @FileName     : BaseKLineChartView.java
 * @Author       : chao
 * @Date         : 2019/4/8
 * @Email        : icechliu@gmail.com
 * @version      : V2
 *************************************************************************/
public abstract class BaseKChartView extends ScrollAndScaleView {

    //是否以动画的方式绘制最后一根线
    protected boolean isAnimationLast = true;

    protected boolean loadDataWithAnim = true;

    protected Bitmap logoBitmap;

    /**
     * 是否正在显示loading
     */
    protected boolean isShowLoading;

    /**
     * 动画执行时长
     */
    protected long duration = 400;

    /**
     * 当前子视图的索引
     */
    protected Status.IndexStatus indexDrawPosition = Status.IndexStatus.NONE;

    /**
     * 绘制K线时画板平移的距离
     */
    private float canvasTranslateX;

    protected float viewWidth, viewHeight;
    protected float renderWidth;

    /**
     * 整体上部的padding
     */
    protected int chartPaddingTop;

    /**
     * 最视图和子试图上方padding
     */
    protected float childViewPaddingTop;
    /**
     * 整体底部padding
     */
    protected int chartPaddingBottom;
    /**
     * y轴的缩放比例
     */
    private double mainScaleY = 1;

    /**
     * 成交量y轴缩放比例
     */
    private float volScaleY = 1;
    /**
     * 子视图y轴缩放比例
     */
    private float indexScaleY = 1;

    /**
     * 主视图的最大值
     */
    private double mainMaxValue = Float.MAX_VALUE;
    /**
     * 主视图的最小值
     */
    private double mainMinValue = Float.MIN_VALUE;

    /**
     * 主视图K线的的最大值
     */
    protected float mainHighMaxValue;
    /**
     * 主视图的K线的最小值
     */
    protected float mainLowMinValue;
    /**
     * X轴最大值坐标索引
     */
    protected int mainMaxIndex;
    /**
     * X轴最小值坐标索引
     */
    protected int mainMinIndex;

    /**
     * 成交量最大值
     */
    protected float volMaxValue = Float.MAX_VALUE;
    /**
     * 成交量最小值
     */
    protected float indexMaxValue = Float.MAX_VALUE;
    /**
     * 当前显示K线最左侧的索引
     */
    protected int screenLeftIndex = 0;
    /**
     * 当前显示K线最右侧的索引
     */
    protected int screenRightIndex = 0;

    /**
     * K线宽度
     */
    protected float chartItemWidth;

    /**
     * K线网格行数
     */
    protected int gridRows;

    /**
     * K线网格列数
     */
    protected int gridColumns;

    /**
     * 尾线画笔
     */
    protected Paint lineEndPointPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    /**
     * 尾线下填充画笔
     */
    protected Paint lineEndFillPointPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    /**
     * 视图背景画笔
     */
    protected Paint backgroundPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    /**
     * logo paint
     */
    protected Paint logoPaint = new Paint(Paint.ANTI_ALIAS_FLAG);

    /**
     * price line right box paint
     */
    protected Paint rightPriceBoxPaint = new Paint(Paint.ANTI_ALIAS_FLAG);

    /**
     * 网络画笔
     */
    protected Paint gridPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    /**
     * 十字线交点小点画笔
     */
    protected Paint selectedCrossPaint = new Paint(Paint.ANTI_ALIAS_FLAG);

    /**
     * 十字线交点大圆画笔
     */
    protected Paint selectedbigCrossPaint = new Paint(Paint.ANTI_ALIAS_FLAG);

    /**
     * 文字画笔
     */
    protected Paint commonTextPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    /**
     * 在价格线上的Label 画笔
     */
    protected Paint labelInPriceLinePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    /**
     * 选中时X坐标画笔
     */
    protected Paint selectedXLabelPaint = new Paint(Paint.ANTI_ALIAS_FLAG);

    /**
     * 文字画笔
     */
    protected Paint yLabelPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    protected Paint xLabelPaint = new Paint(Paint.ANTI_ALIAS_FLAG);


    /**
     * 十字线横线画笔
     */
    protected Paint selectedXLinePaint = new Paint(Paint.ANTI_ALIAS_FLAG);

    /**
     * 十字线竖线画笔
     */
    protected Paint selectedYLinePaint = new Paint(Paint.ANTI_ALIAS_FLAG);

    /**
     * 十字线Y坐标显示背景画笔
     */
    protected Paint selectedPriceBoxBackgroundPaint = new Paint(Paint.ANTI_ALIAS_FLAG);

    /**
     * 十字线Y坐标显示背景画笔
     */
    protected Paint selectorXBackgroundPaint = new Paint(Paint.ANTI_ALIAS_FLAG);

    /**
     * 十字线坐标显示边框画笔
     */
    protected Paint selectorXFramePaint = new Paint(Paint.ANTI_ALIAS_FLAG);

    /**
     * 价格线画笔
     */
    protected Paint priceLinePaint = new Paint(Paint.ANTI_ALIAS_FLAG);

    /**
     * 当前价格边框画笔
     */
    protected Paint priceLineBoxPaint = new Paint(Paint.ANTI_ALIAS_FLAG);

    /**
     * 当前价格背景画笔
     */
    protected Paint priceLineBoxBgPaint = new Paint(Paint.ANTI_ALIAS_FLAG);

    /**
     * 价格线右侧虚线画笔
     */
    protected Paint priceLineRightPaint = new Paint(Paint.ANTI_ALIAS_FLAG);

    /**
     * 价格线右侧虚线画笔
     */
    protected Paint priceLineRightTextPaint = new Paint(Paint.ANTI_ALIAS_FLAG);

    /**
     * 价格框高度
     */
    protected float priceLabelInLineBoxHeight = 40;

    /**
     * 价格框圆角半径
     */
    protected float priceLabelInLineBoxRadius = 20;

    /**
     * 价格框内边距
     */
    protected float priceLineBoxPadidng = 20;

    /**
     * 价格线图形的高
     */
    protected float priceShapeHeight = 20;


    /**
     * 价格线图形的宽
     */
    protected float priceShapeWidth = 10;

    /**
     * 价格线文字与图形的间隔
     */
    protected float priceBoxShapeTextMargin = 10;


    /**
     * 价格线的文字框距离屏幕右侧的边距
     */
    protected float priceLineBoxMarginRight = 120;


    /**
     * 主视视图
     */
    protected MainRender mainRender;

    /**
     * 交易量视图
     */
    protected VolumeRender volumeRender;

    /**
     * 子视图
     */
    protected BaseRender indexRender;

    /**
     * 当前K线的最新价
     */
    protected float lastPrice, lastVol;

    /**
     * K线数据适配器
     */
    protected KLineChartAdapter dataAdapter;

    /**
     * 量视图是否显示为线
     */
    protected Status.KlineStatus klineStatus = Status.KlineStatus.K_LINE;

    /**
     * 量视图是否显示为线
     */
    protected Status.VolChartStatus volChartStatus;

    /**
     * 数据
     */
    protected float[] points;

    /**
     * 数组间隔
     */
    private int indexInterval;

    /**
     * 统一文字高度
     */
    protected float textHeight;

    /**
     * 统一文字基础线
     */
    protected float baseLine;
    /**
     * 统一文字基础线
     */
    protected float legendMarginLeft;

    /**
     * 统一文字Decent
     */
    protected float textDecent;

    /**
     * y轴label向上偏移的距离
     */
    protected int mainYMoveUpInterval = 5;

    /**
     * Y轴label与右边缘距离
     */
    protected float yLabelMarginBorder;

    /**
     * 分时线阴影的半径
     */
    private float endShadowLayerWidth;

    /**
     * 十字线相交点圆半径
     */
    protected float selectedPointRadius;

    /**
     * 滑动监听
     */
    protected SlidListener slidListener;

    /**
     * refresh time limit
     */
    private long time;

    /**
     * 显示十字线的点
     */
    protected boolean showCrossPoint;

    /**
     * 强制隐藏信息框
     */
    protected boolean hideMarketInfo;

    protected Status.MaxMinCalcModel calcModel = Status.MaxMinCalcModel.CALC_NORMAL_WITH_LAST;

    /**
     * 数据观察者,当数据变化
     */
    protected DataSetObserver dataSetObserver = new DataSetObserver() {
        @Override
        public void onChanged() {
            //从没数据变成有数据时,显示动画加载效果
            int currentCount = BaseKChartView.this.itemsCount;
            if (loadDataWithAnim && currentCount == 0) {
                startAnimation();
            }
            int dataCount = dataAdapter.getCount();
            setItemsCount(dataCount);
            if (dataCount == 0) {
                return;
            }
            points = dataAdapter.getPoints();
            int temp = (dataAdapter.getCount() - 1) * indexInterval;
            setItemsCount(dataCount);
            if (dataCount > currentCount) {
                lastPrice = points[temp + Constants.INDEX_CLOSE];
                lastVol = points[temp + Constants.INDEX_VOL];
                if (screenRightIndex >= currentCount - 2) {
                    changeTranslated(canvasTranslateX - chartItemWidth * getScaleX());
                }
            } else if (currentCount == dataCount) {
                lastChange();
            } else {
                lastPrice = points[temp + Constants.INDEX_CLOSE];
                lastVol = points[temp + Constants.INDEX_VOL];
                changeTranslated(getMinTranslate());
            }
            if (!isAnimationLast && !dataAdapter.getResetShowPosition()) {
                if (dataCount > currentCount) {
                    changeTranslated(tempTranslation - (dataCount - currentCount) * chartItemWidth * getScaleX());
                } else {
                    changeTranslated(getMaxTranslate());
                }
            } else if (!isAnimationLast) {
                changeTranslated(getMinTranslate());
            }
            //再次开启动画
            action.run();
            needRender = true;
        }

        @Override
        public void onInvalidated() {
            tempTranslation = canvasTranslateX;
            isAnimationLast = false;
            overScroller.forceFinished(true);
            needRender = false;
        }
    };

    private boolean needRender = true;

    private float tempTranslation;


    /**
     * 当重置数据时,延时400ms显示最后的加载动画
     */
    protected Runnable action = () -> {
        isAnimationLast = true;
    };


    private void lastChange() {
        if (isAnimationLast) {
            int tempIndex = (itemsCount - 1) * indexInterval;
            generaterAnimator(lastVol, points[tempIndex + Constants.INDEX_VOL], animation -> lastVol = (Float) animation.getAnimatedValue());
            generaterAnimator(lastPrice, points[tempIndex + Constants.INDEX_CLOSE], animation -> {
                lastPrice = (Float) animation.getAnimatedValue();
                if (klineStatus.showLine()) {
                    return;
                }
                animInvalidate();
            });
            float[] tempData = Arrays.copyOfRange(points, tempIndex, points.length);
            mainRender.startAnim(BaseKChartView.this, tempData);
            volumeRender.startAnim(BaseKChartView.this, tempData);
        }
    }

    /**
     * 当前数据个数
     */
    protected int itemsCount;


    /**
     * 指标视图
     */
    protected Map<String, BaseRender> indexRenders = new HashMap<>();

    /**
     * Y轴上值的格式化
     */
    protected IValueFormatter valueFormatter = new ValueFormatter();

    /**
     * 日期格式化
     */
    protected IDateTimeFormatter dateTimeFormatter = new DateFormatter();

    /**
     * K线显示动画
     */
    protected ValueAnimator showAnim;

    /**
     * 空白区域
     */
    protected float overScrollRange = 0;

    /**
     * 选中变化监听
     */
    protected OnSelectedChangedListener selectedChangedListener = null;

    /**
     * 主视图
     */
    protected Rect mainRect;

    /**
     * 量视图
     */
    private Rect volRect;

    /**
     * 子视图
     */
    private Rect indexRect;

    /**
     * 分时线尾部点半径
     */
    protected float lineEndRadius;
    /**
     * 分时线尾部点半径
     */
    protected float lineEndMaxMultiply;

    /**
     * 最新数据变化的执行动画
     */
    private ValueAnimator valueAnimator;

    /**
     * 分时线填充渐变的上部颜色
     */
    protected int timeLineFillTopColor;

    /**
     * 分时线填充渐变的下部颜色
     */
    protected int timeLineFillBottomColor;

    /**
     * 十字线Y轴的宽度
     */
    protected float selectedWidth;

    /**
     * 十字线Y轴的颜色
     */
    protected int selectedYColor = -1;

    /**
     * 背景色渐变上部颜色
     */
    protected int backGroundFillTopColor;

    /**
     * 背景色渐变下部颜色
     */
    protected int backGroundFillBottomColor;

    /**
     * 网格行间距
     */
    protected float rowSpace;

    /**
     * 网格列间距
     */
    protected float columnSpace;

    /**
     * 展示区域的高度
     */
    protected float displayHeight = 0;

    /**
     * 主图占比
     */
    protected float mainPercent = 8f;

    /**
     * 交易量图占比
     */
    protected float volPercent = 2f;

    /**
     * 子图占比
     */
    protected float IndexPercent = 2f;

    /**
     * 是否适配X label
     */
    protected boolean betterX;
    protected boolean betterSelectedX;

    /**
     * 是否十字线跟随手指移动(Y轴)
     */
    protected Status.CrossTouchModel crossTouchModel;

    /**
     * 选中价格框横向padding
     */
    protected float selectedPriceBoxHorizontalPadding;
    /**
     * 选中日期框横向padding
     */
    protected float selectedDateBoxHorizontalPadding;

    /**
     * 选中日期框纵向padding
     */
    protected float dateBoxVerticalPadding;
    /**
     * 选中价格框纵向padding
     */
    protected float selectedPriceBoxVerticalPadding;

    public BaseKChartView(Context context) {
        super(context);
        init();
    }

    public BaseKChartView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public BaseKChartView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    /**
     * 初始化
     */
    protected void init() {
        setWillNotDraw(false);
        indexInterval = Constants.getCount();
        gestureDetector = new GestureDetectorCompat(getContext(), this);
        scaleDetector = new ScaleGestureDetector(getContext(), this);
        showAnim = ValueAnimator.ofFloat(0f, 1f);
        showAnim.setDuration(duration);
        showAnim.addUpdateListener(animation -> invalidate());
        selectorXFramePaint.setStyle(Paint.Style.STROKE);
        priceLinePaint.setAntiAlias(true);
        priceLineRightPaint.setStyle(Paint.Style.STROKE);
        rightPriceBoxPaint.setStyle(Paint.Style.FILL_AND_STROKE);
        priceLineBoxPaint.setStyle(Paint.Style.STROKE);
        selectedXLinePaint.setStyle(Paint.Style.STROKE);
        xLabelPaint.setTextAlign(Paint.Align.CENTER);
    }


    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        //如果没设置chartPaddingBottom
        if (0 == chartPaddingBottom) {
            chartPaddingBottom = (int) (dateBoxVerticalPadding * 2 + selectorXFramePaint.getStrokeWidth() * 2 + textHeight);
        }
        displayHeight = h - chartPaddingTop - (dateBoxVerticalPadding * 2 + selectorXFramePaint.getStrokeWidth() * 2 + textHeight);
        rowSpace = displayHeight / gridRows;
        this.viewWidth = w;
        this.viewHeight = h;

        initRect();
    }

    protected float labelSpace = 0f;

    protected YLabelModel yLabelModel = YLabelModel.LABEL_NONE_GRID;

    protected Status.ChildStatus chartShowStatue = Status.ChildStatus.MAIN_VOL;

    /**
     * 初始化视图区域
     * 主视图
     * 成交量视图
     * 子视图
     */
    protected void initRect() {
        switch (yLabelModel) {
            case LABEL_NONE_GRID:
                this.renderWidth = viewWidth - labelSpace;
                columnSpace = renderWidth / gridColumns;
                break;
            case LABEL_WITH_GRID:
                this.renderWidth = viewWidth;
                columnSpace = viewWidth / gridColumns;
                break;
        }
        int tempMainHeight, tempVolHeight, tempChildHeight;
        switch (chartShowStatue) {
            case MAIN_VOL:
                tempMainHeight = (int) (displayHeight * mainPercent / 10f);
                tempVolHeight = (int) (displayHeight * IndexPercent / 10f);
                mainRect = new Rect(0, chartPaddingTop, (int) renderWidth, chartPaddingTop + tempMainHeight);
                volRect = new Rect(0, (int) (mainRect.bottom + childViewPaddingTop), (int) renderWidth, mainRect.bottom + tempVolHeight);
                indexRect = null;
                break;
            case MAIN_ONLY:
                mainRect = new Rect(0, chartPaddingTop, (int) renderWidth, (int) (chartPaddingTop + displayHeight));
                volRect = null;
                indexRect = null;
                break;
            case MAIN_INDEX:
                tempMainHeight = (int) (displayHeight * mainPercent / 10f);
                tempChildHeight = (int) (displayHeight * IndexPercent / 10f);
                mainRect = new Rect(0, chartPaddingTop, (int) renderWidth, chartPaddingTop + tempMainHeight);
                indexRect = new Rect(0, (int) (mainRect.bottom + childViewPaddingTop), (int) renderWidth, mainRect.bottom + tempChildHeight);
                volRect = null;
                break;
            default:
            case MAIN_VOL_INDEX:
                tempChildHeight = (int) (displayHeight * IndexPercent / 10f);
                tempVolHeight = (int) (displayHeight * volPercent / 10f);
                tempMainHeight = (int) (displayHeight * (10 - volPercent - IndexPercent) / 10f);
                mainRect = new Rect(0, chartPaddingTop, (int) renderWidth, chartPaddingTop + tempMainHeight);
                volRect = new Rect(0, (int) (mainRect.bottom + childViewPaddingTop), (int) renderWidth, mainRect.bottom + tempVolHeight);
                indexRect = new Rect(0, (int) (volRect.bottom + childViewPaddingTop), (int) renderWidth, volRect.bottom + tempChildHeight);
                break;
        }
        if (null != logoBitmap) {
            logoTop = mainRect.bottom - logoBitmap.getHeight() - logoPaddingBottom;
        }
        gridRowCountWithChild = 0;
        gridRowCountNoChild = 0;
        invalidate();
    }

    @Override
    public void onDraw(Canvas canvas) {
        if (needRender) {
            renderBackground(canvas);
            renderGrid(canvas);
            renderLogo(canvas);
            if (!isShowLoading &&
                    0 != viewWidth &&
                    0 != itemsCount &&
                    null != points &&
                    points.length != 0) {
                try {
                    calcValues();
                    renderXLabels(canvas);
                    renderK(canvas);
                    renderValue(canvas, getShowSelected() ? selectedIndex : itemsCount - 1);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }


    protected float logoTop;

    protected float logoPaddingLeft, logoPaddingBottom;

    protected void renderLogo(Canvas canvas) {
        if (null != logoBitmap) {
            canvas.drawBitmap(logoBitmap, logoPaddingLeft, logoTop, logoPaint);
        }
    }

    /**
     * 绘制整体背景
     *
     * @param canvas canvas
     */
    protected void renderBackground(Canvas canvas) {
        float mid = viewWidth / 2, bottom;
        int mainBottom = mainRect.bottom;
        backgroundPaint.setShader(new LinearGradient(mid, 0, mid, mainBottom, backGroundFillTopColor, backGroundFillBottomColor, Shader.TileMode.CLAMP));
        canvas.drawRect(0, 0, viewWidth + labelSpace, mainBottom, backgroundPaint);
        switch (chartShowStatue) {
            case MAIN_VOL:
                bottom = volRect.bottom + chartPaddingBottom;
                backgroundPaint.setShader(new LinearGradient(mid, mainBottom, mid, bottom, backGroundFillTopColor, backGroundFillBottomColor, Shader.TileMode.CLAMP));
                canvas.drawRect(0, mainBottom, viewWidth, bottom, backgroundPaint);
                break;
            case MAIN_INDEX:
                bottom = indexRect.bottom + chartPaddingBottom;
                backgroundPaint.setShader(new LinearGradient(mid, mainBottom, mid, bottom, backGroundFillTopColor, backGroundFillBottomColor, Shader.TileMode.CLAMP));
                canvas.drawRect(0, mainBottom, viewWidth, indexRect.bottom, backgroundPaint);
                break;
            case MAIN_VOL_INDEX:
                bottom = volRect.bottom;
                backgroundPaint.setShader(new LinearGradient(mid, mainBottom, mid, bottom, backGroundFillTopColor, backGroundFillBottomColor, Shader.TileMode.CLAMP));
                canvas.drawRect(0, mainBottom, viewWidth, bottom, backgroundPaint);
                float indexBottom = indexRect.bottom;
                backgroundPaint.setShader(new LinearGradient(mid, bottom, mid, indexBottom, backGroundFillTopColor, backGroundFillBottomColor, Shader.TileMode.CLAMP));
                canvas.drawRect(0, bottom, viewWidth, indexBottom, backgroundPaint);
                break;
            case MAIN_ONLY:
                backgroundPaint.setShader(new LinearGradient(mid, 0, mid, mainBottom, backGroundFillTopColor, backGroundFillBottomColor, Shader.TileMode.CLAMP));
                canvas.drawRect(0, 0, viewWidth, mainBottom, backgroundPaint);
                break;
        }
        if (yLabelModel == YLabelModel.LABEL_NONE_GRID && null != yLabelBackgroundPaint && labelSpace > 0) {
            canvas.drawRect(viewWidth - labelSpace,
                    0,
                    viewWidth,
                    yLabelBackgroundHeightSameChart ? viewHeight - chartPaddingBottom : viewHeight,
                    yLabelBackgroundPaint);
        }

    }

    protected Paint yLabelBackgroundPaint;
    protected boolean yLabelBackgroundHeightSameChart;

    /**
     * 设置当前K线总数据个数
     *
     * @param itemCount items count
     */
    protected void setItemsCount(int itemCount) {
        this.itemsCount = itemCount;
        mainRender.setItemCount(itemsCount);
        mainRender.resetValues();
        volumeRender.setItemCount(itemsCount);
        volumeRender.resetValues();
        if (null != indexRender) {
            indexRender.setItemCount(itemCount);
            indexRender.resetValues();
        }
        fixScrollEnable(getDataLength());
        invalidate();
    }

    /**
     * 计算主View的value对应的Y值
     *
     * @param value value
     * @return location Y
     */
    public float getMainY(float value) {
        double v = mainRect.top + ((mainMaxValue - value) * mainScaleY);
        if (v > mainRect.bottom) {
            v = mainRect.bottom;
        } else if (v < mainRect.top) {
            v = mainRect.top;
        }
        return (float) v;
    }

    /**
     * 计算成交量View的Y值
     *
     * @param value value
     * @return location y
     */
    // TODO: 2020/7/8 所有交易量都为0时存在精度差
    public float getVolY(float value) {
        return volRect.top + ((volMaxValue - value) * volScaleY);
    }

    /**
     * 计算子View的Y值
     *
     * @param value value
     * @return location y
     */
    public float getChildY(float value) {
        return indexRect.top + ((indexMaxValue - value) * indexScaleY);
    }


    /**
     * 画网格
     *
     * @param canvas canvas
     */
    protected void renderGrid(Canvas canvas) {
        float right = renderWidth;
        for (int i = 0; i <= gridRows; i++) {
            float y = rowSpace * i + chartPaddingTop;
            canvas.drawLine(0, y, right, y, gridPaint);
        }
        for (int i = 0; i <= gridColumns; i++) {
            float stopX = columnSpace * i;
            canvas.drawLine(stopX, 0, stopX, displayHeight + chartPaddingTop, gridPaint);
        }
    }


    /**
     * 绘制k线图
     *
     * @param canvas canvas
     */
    protected void renderK(Canvas canvas) {
        canvas.save();
        canvas.translate(canvasTranslateX, 0);
        switch (yLabelModel) {
            case LABEL_NONE_GRID:
                canvas.save();
                canvas.clipRect(-canvasTranslateX, chartPaddingTop, -canvasTranslateX + renderWidth, getChartPaddingTop() + displayHeight);
                renderKCandle(canvas);
                mainRender.renderMaxMinValue(canvas, this, getX(mainMaxIndex), mainHighMaxValue, getX(mainMinIndex), mainLowMinValue);
                canvas.restore();
                break;
            case LABEL_WITH_GRID:
                renderKCandle(canvas);
                mainRender.renderMaxMinValue(canvas, this, getX(mainMaxIndex), mainHighMaxValue, getX(mainMinIndex), mainLowMinValue);
                break;
        }

        renderYLabels(canvas);
        if (getShowSelected()) {
            renderSelected(canvas, getX(selectedIndex));
        }
        canvas.restore();
    }

    private void renderKCandle(Canvas canvas) {
        for (int i = screenLeftIndex; i <= screenRightIndex && i >= 0; i++) {
            float curX = getX(i), lastX;
            int lastTemp = 0;
            int nextTemp = indexInterval * i + indexInterval;
            float[] values;
            if (i == 0) {
                lastX = curX;
                values = Arrays.copyOfRange(points, lastTemp, indexInterval);
            } else {
                lastX = getX(i - 1);
                lastTemp = indexInterval * i - indexInterval;
                values = Arrays.copyOfRange(points, lastTemp, nextTemp);
            }

            switch (chartShowStatue) {
                case MAIN_VOL_INDEX:
                    indexRender.render(canvas, lastX, curX, this, i, values);
                case MAIN_VOL:
                    volumeRender.render(canvas, lastX, curX, this, i, values);
                case MAIN_ONLY:
                    mainRender.render(canvas, lastX, curX, this, i, values);
                    break;
                case MAIN_INDEX:
                    indexRender.render(canvas, lastX, curX, this, i, values);
                    mainRender.render(canvas, lastX, curX, this, i, values);
                    break;
            }
        }
    }


    public void renderSelected(Canvas canvas, float x) {
        float y, textWidth;
        String text;
        //十字线竖线
        float halfWidth = selectedWidth / 2 * scaleX;
        float left = x - halfWidth;
        float right = x + halfWidth;
        float bottom = displayHeight + chartPaddingTop;
        Path path = new Path();
        path.moveTo(left, chartPaddingTop);
        path.lineTo(right, chartPaddingTop);
        path.lineTo(right, bottom);
        path.lineTo(left, bottom);
        path.close();
        if (-1 != selectedYColor) {
            LinearGradient linearGradient = new LinearGradient(x, chartPaddingTop, x, bottom,
                    new int[]{Color.TRANSPARENT, selectedYColor, selectedYColor, Color.TRANSPARENT},
                    new float[]{0f, 0.2f, 0.8f, 1f}, Shader.TileMode.CLAMP);
            selectedYLinePaint.setShader(linearGradient);
        }
        canvas.drawPath(path, selectedYLinePaint);
        //画X值
        String date = formatDateTime(dataAdapter.getDate(selectedIndex));
        textWidth = commonTextPaint.measureText(date);
        //向下多移动出一个像素(如果有必要可以设置多移动网络线宽度)
        y = chartPaddingTop + displayHeight + 1;
        float temp = textWidth / 2;
        right = x + temp + selectedDateBoxHorizontalPadding;
        left = x - temp - selectedDateBoxHorizontalPadding;
        float screenRightX = xToTranslateX(renderWidth);
        if (betterSelectedX) {
            if (right > screenRightX) {
                right = screenRightX;
                left = right - selectedDateBoxHorizontalPadding * 2 - textWidth;
            } else if (left < xToTranslateX(0)) {
                left = xToTranslateX(0);
                right = left + selectedDateBoxHorizontalPadding * 2 + textWidth;
            }
        }

        bottom = y + textHeight + dateBoxVerticalPadding * 2;
        canvas.drawRect(left, y, right, bottom, selectorXBackgroundPaint);
        canvas.drawRect(left, y, right, bottom, selectorXFramePaint);
        canvas.drawText(date, left + selectedPriceBoxHorizontalPadding, y + baseLine + dateBoxVerticalPadding, selectedXLabelPaint);
        //十字线Y值判断
        //十字线横线
        if (crossTouchModel.getStateValue()) {
            y = selectedY;
            if (selectedY < mainRect.top + chartPaddingTop) {
                return;
            } else if (selectedY < mainRect.bottom) {
                text = valueFormatter.format((float) (mainMinValue + (mainMaxValue - mainMinValue) / (mainRect.bottom - chartPaddingTop) * (mainRect.bottom - selectedY)));
            } else if (null != volRect && selectedY < volRect.top) {
                return;
            } else if (null != volRect && selectedY < volRect.bottom) {
                text = NumberTools.formatAmount(volumeRender.getValueFormatter().format((volMaxValue / volRect.height() * (volRect.bottom - selectedY))));
            } else if (null != indexRender && selectedY < indexRect.bottom) {
                text = getValueFormatter().format((indexMaxValue / indexRect.height() * (indexRect.bottom - selectedY)));
            } else if (null != indexRender && selectedY < indexRect.top) {
                return;
            } else {
                return;
            }
        } else {
            text = valueFormatter.format(points[selectedIndex * indexInterval + Constants.INDEX_CLOSE]);
            y = getMainY(points[selectedIndex * indexInterval + Constants.INDEX_CLOSE]);
        }
        canvas.drawLine(-canvasTranslateX, y, -canvasTranslateX + renderWidth, y, selectedXLinePaint);
        //十字线交点
        if (showCrossPoint) {
            canvas.drawCircle(x, y, chartItemWidth, selectedbigCrossPaint);
            canvas.drawCircle(x, y, selectedPointRadius, selectedCrossPaint);
        }
        switch (yLabelModel) {
            case LABEL_NONE_GRID:
                textWidth = labelSpace;
                x = -canvasTranslateX + viewWidth - textWidth;
                break;
            case LABEL_WITH_GRID:
                textWidth = commonTextPaint.measureText(text);
                x = -canvasTranslateX + viewWidth - textWidth - 1 - 2 * selectedPriceBoxHorizontalPadding - selectedPriceBoxVerticalPadding;
                break;
        }
        temp = textHeight / 2 + selectedPriceBoxVerticalPadding;
        float tempX = textWidth + 2 * selectedPriceBoxHorizontalPadding;
        float boxTop = y - temp;
        float boxBottom = y + temp;
        if (yLabelModel == Status.YLabelModel.LABEL_NONE_GRID || getX(selectedIndex - screenLeftIndex) > renderWidth / 2) {
            path = new Path();
            path.moveTo(x, y);
            path.lineTo(x + selectedPriceBoxHorizontalPadding + selectedPriceBoxVerticalPadding, boxBottom);
            path.lineTo(-canvasTranslateX + viewWidth - 2, boxBottom);
            path.lineTo(-canvasTranslateX + viewWidth - 2, boxTop);
            path.lineTo(x + selectedPriceBoxHorizontalPadding + selectedPriceBoxVerticalPadding, boxTop);
            path.close();
            canvas.drawPath(path, selectedPriceBoxBackgroundPaint);
            canvas.drawPath(path, selectedXLinePaint);
            canvas.drawText(text, x + selectedPriceBoxVerticalPadding + selectedPriceBoxHorizontalPadding, boxTop + selectedPriceBoxVerticalPadding + baseLine, commonTextPaint);
        } else {
            x = -canvasTranslateX;
            path = new Path();
            path.moveTo(x, boxTop);
            path.lineTo(x, boxBottom);
            path.lineTo(tempX + x, boxBottom);
            path.lineTo(tempX + x + selectedPriceBoxHorizontalPadding + selectedPriceBoxVerticalPadding, y);
            path.lineTo(tempX + x, boxTop);
            path.close();
            canvas.drawPath(path, selectedPriceBoxBackgroundPaint);
            canvas.drawPath(path, selectedXLinePaint);
            canvas.drawText(text, x + selectedPriceBoxHorizontalPadding, boxTop + selectedPriceBoxVerticalPadding + baseLine, commonTextPaint);
        }
    }

    /**
     * 绘制所有的Labels
     *
     * @param canvas canvas
     */
    protected void renderXLabels(Canvas canvas) {

        float y = chartPaddingTop + displayHeight + baseLine + dateBoxVerticalPadding;
        float halfWidth = chartItemWidth / 2 * getScaleX();
        float startX = getX(screenLeftIndex) - halfWidth;
        float stopX = getX(screenRightIndex) + halfWidth;
        int startLabelCount;
        int endLabelCount;
        if (betterX) {
            //X轴最左侧的值
            float translateX = xToTranslateX(0);
            if (translateX >= startX && translateX <= stopX) {
                String text = formatDateTime(getAdapter().getDate(screenLeftIndex));
                canvas.drawText(text, commonTextPaint.measureText(text) / 2, y, xLabelPaint);
            }
            //X轴最右侧的值
            translateX = xToTranslateX(renderWidth);
            if (translateX >= startX && translateX <= stopX) {
                String text = formatDateTime(getAdapter().getDate(screenRightIndex));
                canvas.drawText(text, renderWidth - commonTextPaint.measureText(text) / 2, y, xLabelPaint);
            }
            startLabelCount = 1;
            endLabelCount = gridColumns;
        } else {
            startLabelCount = 0;
            endLabelCount = gridColumns + 1;
        }
        for (; startLabelCount < endLabelCount; startLabelCount++) {
            float tempX = columnSpace * startLabelCount;
            float translateX = xToTranslateX(tempX);
            if (translateX >= startX && translateX <= stopX) {
                int index = indexOfTranslateX(translateX);
                String text = formatDateTime(dataAdapter.getDate(index));
                canvas.drawText(text, tempX, y, xLabelPaint);
            }
        }
    }

    private double rowValueNoChild, rowValueWithChild;
    protected int gridRowCountNoChild;
    protected int gridRowCountWithChild;

    protected Status.YLabelAlign yLabelAlign;
    protected float yLabelX;

    /**
     * 绘制Y轴上的所有label
     *
     * @param canvas canvas
     */
    protected void renderYLabels(Canvas canvas) {

        double rowValue;
        int gridRowCount;
        String maxVol, childLabel;
        float tempLeft = -canvasTranslateX;
        switch (chartShowStatue) {
            case MAIN_VOL_INDEX:
                if (0 == gridRowCountWithChild) {
                    gridRowCountWithChild = gridRows - 2;
                    rowValueWithChild = (mainMaxValue - mainMinValue) / gridRowCountWithChild;
                }
                gridRowCount = gridRowCountWithChild;
                rowValue = rowValueWithChild;
                //Y轴上网络的值
                for (int i = 0; i <= gridRowCount; i++) {
                    String text = valueFormatter.format((float) (mainMaxValue - i * rowValue));
                    float v = rowSpace * i + chartPaddingTop;
                    canvas.drawText(text, tempLeft + yLabelX, v - mainYMoveUpInterval, yLabelPaint);
                }
                maxVol = NumberTools.formatAmount(volumeRender.getValueFormatter().
                        format(volMaxValue));
                canvas.drawText(maxVol, tempLeft + yLabelX, mainRect.bottom + baseLine, yLabelPaint);
                //子图Y轴label
                childLabel = indexRender.getValueFormatter().format(indexMaxValue);
                canvas.drawText(childLabel, tempLeft + yLabelX, volRect.bottom + baseLine, yLabelPaint);
                break;
            case MAIN_VOL:
                if (0 == gridRowCountNoChild) {
                    gridRowCountNoChild = gridRows - 1;
                    rowValueNoChild = (mainMaxValue - mainMinValue) / gridRowCountNoChild;
                }
                gridRowCount = gridRowCountNoChild;
                rowValue = rowValueNoChild;
                //Y轴上网络的值
                for (int i = 0; i <= gridRowCount; i++) {
                    String text = valueFormatter.format((float) (mainMaxValue - i * rowValue));
                    float v = rowSpace * i + chartPaddingTop;
                    canvas.drawText(text, tempLeft + yLabelX, v - mainYMoveUpInterval, yLabelPaint);

                }
                maxVol = NumberTools.formatAmount(volumeRender.getValueFormatter().
                        format(volMaxValue));
                canvas.drawText(maxVol, tempLeft + yLabelX, mainRect.bottom + baseLine, yLabelPaint);
                break;

            case MAIN_ONLY:
                if (0 == gridRowCountNoChild) {
                    gridRowCountNoChild = gridRows;
                    rowValueNoChild = (mainMaxValue - mainMinValue) / gridRowCountNoChild;
                }
                gridRowCount = gridRowCountNoChild;
                rowValue = rowValueNoChild;
                //Y轴上网格的值
                for (int i = 0; i <= gridRowCount; i++) {
                    String text = valueFormatter.format((float) (mainMaxValue - i * rowValue));
                    float v = rowSpace * i + chartPaddingTop;
                    canvas.drawText(text, tempLeft + yLabelX, v - mainYMoveUpInterval, yLabelPaint);
                }
                break;
            case MAIN_INDEX:
                if (0 == gridRowCountNoChild) {
                    gridRowCountNoChild = gridRows - 1;
                    rowValueNoChild = (mainMaxValue - mainMinValue) / gridRowCountNoChild;
                }
                gridRowCount = gridRowCountNoChild;
                rowValue = rowValueNoChild;
                //Y轴上网络的值
                for (int i = 0; i <= gridRowCount; i++) {
                    String text = valueFormatter.format((float) (mainMaxValue - i * rowValue));
                    float v = rowSpace * i + chartPaddingTop;
                    canvas.drawText(text, tempLeft + yLabelX, v - mainYMoveUpInterval, yLabelPaint);
                }
                childLabel = indexRender.getValueFormatter().format(indexMaxValue);
                canvas.drawText(childLabel, tempLeft + yLabelX, indexRect.top - childViewPaddingTop + baseLine, yLabelPaint);
                break;
        }
        renderPriceLine(canvas, tempLeft + getViewWidth());
    }

    protected float priceDotLineItemWidth;
    protected float priceDotLineItemSpace;

    private void renderPriceLine(Canvas canvas, float tempRight) {
        float y = getMainY(lastPrice);
        String priceText = valueFormatter.format(lastPrice);
        float textWidth = commonTextPaint.measureText(priceText);
        float textLeft = tempRight - textWidth - yLabelMarginBorder;
        float klineRight = getX(screenRightIndex);
        if (screenRightIndex == itemsCount - 1 && klineRight < textLeft) {
            renderDotLine(canvas, klineRight, textLeft - priceLineMarginPriceLabel, y, priceLineRightPaint);
            if (klineStatus.showLine() && klineRight < tempRight - labelSpace) {
                drawEndPoint(canvas, klineRight);
                canvas.drawCircle(klineRight, y, lineEndRadius, lineEndFillPointPaint);
            }
            renderRightPriceLabel(canvas, y, priceText, textWidth, textLeft);
            showPriceLabelInLine = false;
        } else {
            if (yLabelModel == YLabelModel.LABEL_WITH_GRID) {
                renderDotLine(canvas, 0, tempRight - priceLineMarginPriceLabel, y, priceLinePaint);
                float halfPriceBoxHeight = priceLabelInLineBoxHeight / 2;
                priceLabelInLineBoxRight = tempRight - priceLineBoxMarginRight;
                priceLabelInLineBoxLeft = priceLabelInLineBoxRight - textWidth - priceShapeWidth - priceLineBoxPadidng * 2 - priceBoxShapeTextMargin;
                priceLabelInLineBoxTop = y - halfPriceBoxHeight;
                priceLabelInLineBoxBottom = y + halfPriceBoxHeight;
                renderPriceLabelInPriceLine(canvas,
                        priceLabelInLineBoxLeft,
                        priceLabelInLineBoxTop,
                        priceLabelInLineBoxRight,
                        priceLabelInLineBoxBottom,
                        priceLabelInLineBoxRadius,
                        y,
                        priceText);
                showPriceLabelInLine = true;
            } else {
                renderDotLine(canvas, 0, tempRight - labelSpace, y, priceLinePaint);
                renderRightPriceLabel(canvas, y, priceText, textWidth, textLeft);
                showPriceLabelInLine = false;
            }
        }
    }


    protected boolean showPriceLabelInLine;
    protected boolean priceLabelInLineClickable;
    protected float priceLabelInLineBoxRight, priceLabelInLineBoxLeft, priceLabelInLineBoxTop, priceLabelInLineBoxBottom;

    @Override
    public boolean onSingleTapUp(MotionEvent e) {
        if (priceLabelInLineClickable && showPriceLabelInLine) {
            float x = xToTranslateX(e.getX());
            float y = e.getY();
            if (priceLabelInLineBoxTop < y && priceLabelInLineBoxBottom > y && priceLabelInLineBoxLeft < x && priceLabelInLineBoxRight > x) {
                ValueAnimator valueAnimator = ValueAnimator.ofFloat(getTranslateX(), getMinTranslate());
                valueAnimator.setDuration(duration).addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                    @Override
                    public void onAnimationUpdate(ValueAnimator animation) {
                        changeTranslated((Float) animation.getAnimatedValue());
                        animInvalidate();
                    }
                });
                valueAnimator.setRepeatCount(0);
                valueAnimator.start();
                selectedIndex = -1;
                return true;
            }
        }
        return super.onSingleTapUp(e);

    }

    /**
     * 横屏价格线
     *
     * @param canvas canvas
     * @param y      y
     */
    private void renderDotLine(Canvas canvas, float left, float right, float y, Paint paint) {
        float dotWidth = priceDotLineItemWidth + priceDotLineItemSpace;
        for (; left < right; left += dotWidth) {
            canvas.drawLine(left, y, left + priceDotLineItemWidth, y, paint);
        }
    }

    private float priceLabelInLineLeft, priceLabelInLineRight, priceLabelInLineTop, priceLabelInLineBottom;

    /**
     * 价格线上的价格label
     *
     * @param canvas    canvas
     * @param y         y
     * @param priceText priceText
     */
    private void renderPriceLabelInPriceLine(Canvas canvas, float boxLeft, float boxTop, float boxRight, float boxBottom, float rectRadius, float y, String priceText) {

        LogUtil.e("left : " + boxLeft + "     top" + boxTop + "   box");

        canvas.drawRoundRect(new RectF(boxLeft, boxTop, boxRight, boxBottom), rectRadius, rectRadius, priceLineBoxBgPaint);
        canvas.drawRoundRect(new RectF(boxLeft, boxTop, boxRight, boxBottom), rectRadius, rectRadius, priceLineBoxPaint);
        float temp = priceShapeHeight / 2;
        float shapeLeft = boxRight - priceShapeWidth - priceLineBoxPadidng;
        //价格线三角形
        Path shape = new Path();
        shape.moveTo(shapeLeft, y - temp);
        shape.lineTo(shapeLeft, y + temp);
        shape.lineTo(shapeLeft + priceShapeWidth, y);
        shape.close();
        canvas.drawPath(shape, commonTextPaint);
        canvas.drawText(priceText, boxLeft + priceLineBoxPadidng, (y + (textHeight / 2 - textDecent)), labelInPriceLinePaint);
    }


    /**
     * 绘制最后一个呼吸灯效果
     *
     * @param canvas canvas
     * @param stopX  x
     */
    public void drawEndPoint(Canvas canvas, float stopX) {
        RadialGradient radialGradient = new RadialGradient(stopX, getMainY(lastPrice), endShadowLayerWidth, lineEndPointPaint.getColor(), Color.TRANSPARENT, Shader.TileMode.CLAMP);
        lineEndPointPaint.setShader(radialGradient);
        canvas.drawCircle(stopX, getMainY(lastPrice), lineEndRadius * lineEndMaxMultiply, lineEndPointPaint);
    }

    protected float priceLineMarginPriceLabel = 5;


    /**
     * 价格线label  右侧
     *
     * @param canvas      canvas
     * @param y           y
     * @param priceString priceString
     * @param textWidth   textWidth
     * @param textLeft    textLeft
     */
    private void renderRightPriceLabel(Canvas canvas, float y, String priceString, float textWidth, float textLeft) {
        float halfTextHeight = textHeight / 2;
        float top = y - halfTextHeight;
        canvas.drawRect(new Rect((int) textLeft, (int) top, (int) (textLeft + textWidth), (int) (y + halfTextHeight)), rightPriceBoxPaint);
        canvas.drawText(priceString, textLeft, top + baseLine, priceLineRightTextPaint);
    }

    /**
     * 数据总长
     *
     * @return float
     */
    protected float getDataLength() {
        return chartItemWidth * getScaleX() * (itemsCount - 1) + overScrollRange;
    }

    /**
     * 开启循环刷新绘制
     */
    public void startFreshPage() {
        if (null != valueAnimator && valueAnimator.isRunning()) {
            return;
        }
        valueAnimator = ValueAnimator.ofFloat(lineEndRadius, lineEndRadius * lineEndMaxMultiply);
        valueAnimator.setRepeatMode(ValueAnimator.REVERSE);
        valueAnimator.setDuration(duration);
        valueAnimator.setRepeatCount(10000);
        valueAnimator.addUpdateListener(animation -> {
            endShadowLayerWidth = (Float) animation.getAnimatedValue();
            animInvalidate();
        });
        valueAnimator.start();
    }

    /**
     * 关闭循环刷新
     */
    public void stopFreshPage() {
        if (null != valueAnimator && valueAnimator.isRunning()) {
            valueAnimator.cancel();
        }
        valueAnimator = null;
    }

    /**
     * 画值
     *
     * @param canvas   canvas
     * @param position 显示某个点的值
     */
    protected void renderValue(Canvas canvas, int position) {
        int temp = indexInterval * position;
        if (temp < points.length && position >= 0) {
            float[] tempValues = Arrays.copyOfRange(points, temp, temp + indexInterval);
            float x = legendMarginLeft;
            switch (chartShowStatue) {
                case MAIN_INDEX:
                    mainRender.renderText(canvas, this, x, mainRect.top + baseLine - textHeight / 2, position, tempValues);
                    indexRender.renderText(canvas, this, x, mainRect.bottom + baseLine, position, tempValues);
                    break;
                case MAIN_VOL_INDEX:
                    indexRender.renderText(canvas, this, x, volRect.bottom + baseLine, position, tempValues);
                case MAIN_VOL:
                    volumeRender.renderText(canvas, this, x, mainRect.bottom + baseLine, position, tempValues);
                case MAIN_ONLY:
                    mainRender.renderText(canvas, this, x, mainRect.top + baseLine - textHeight / 2, position, tempValues);
                    break;
            }
        }
    }


    /**
     * 当前主视图显示的指标
     */
    protected Status.MainStatus status = Status.MainStatus.MA;

    /**
     * 计算当前选中item的X的坐标
     *
     * @param x index of selected item
     */
    protected void calculateSelectedX(float x) {
        selectedIndex = indexOfTranslateX(xToTranslateX(x));
        if (selectedIndex > screenRightIndex) {
            selectedIndex = screenRightIndex;
        }
        if (selectedIndex < screenLeftIndex) {
            selectedIndex = screenLeftIndex;
        }
    }

    private float selectedY = 0;

    @Override
    public void onSelectedChange(MotionEvent e) {
        int lastIndex = selectedIndex;
        calculateSelectedX(e.getX());
        selectedY = e.getY();
        if (lastIndex != selectedIndex) {
            int temp = selectedIndex * indexInterval;
            if (null != this.selectedChangedListener) {
                selectedChangedListener.onSelectedChanged(this, selectedIndex, Arrays.copyOfRange(points, temp, temp + indexInterval));
            }
        }
        invalidate();
    }

    /**
     * 获取画板的最小位移
     *
     * @return translate value
     */
    private float getMinTranslate() {

        float dataLength = getDataLength();
        if (dataLength > renderWidth) {
            float minValue = -(dataLength - renderWidth);
            return (overScrollRange == 0) ? minValue - chartItemWidth * getScaleX() / 2 : minValue;
        } else {
            return chartItemWidth * scaleX / 2;
        }
    }


    /**
     * 获取平移的最大值
     *
     * @return 最大值
     */
    private float getMaxTranslate() {
        float dataLength = getDataLength();
        if (dataLength > renderWidth) {
            return klineStatus.showLine() ? 0 : chartItemWidth * getScaleX() / 2;
        }
        return renderWidth - dataLength + overScrollRange - (klineStatus.showLine() ? 0 : chartItemWidth * getScaleX() / 2);
    }

    @Override
    protected void onScrollChanged(int l, int t, int oldl, int oldt) {
        super.onScrollChanged(l, t, oldl, oldt);
        changeTranslated(canvasTranslateX + (l - oldl));
        if (klineStatus.showLine() && getX(screenRightIndex) + canvasTranslateX <= renderWidth) {
            startFreshPage();
        } else {
            stopFreshPage();
        }
        animInvalidate();
    }

    @Override
    protected void onScaleChanged(float scale, float oldScale) {
        //通过 放大和左右左右个数设置左移
        if (scale == oldScale) {
            return;
        }
        float tempWidth = chartItemWidth * scale;
        float newCount = (renderWidth / tempWidth);
        float oldCount = (renderWidth / chartItemWidth / oldScale);
        float difCount = (newCount - oldCount) / 2;
        float dataLength = getDataLength();
        if (screenLeftIndex > 0) {
            changeTranslated(canvasTranslateX / oldScale * scale + difCount * tempWidth);
        } else {
            if (dataLength < renderWidth) {
                changeTranslated(-(renderWidth - dataLength));
            } else {
                changeTranslated(getMaxTranslate());
            }
        }
        fixScrollEnable(dataLength);
        animInvalidate();
    }

    private void fixScrollEnable(float dataLength) {
        if (autoFixScrollEnable) {
            if (dataLength <= renderWidth && isScrollEnable()) {
                setScrollEnable(false);
            } else if (!isScrollEnable() && dataLength > renderWidth) {
                setScrollEnable(true);
            }
        }
    }

    protected boolean autoFixScrollEnable = true;


    /**
     * 设置当前平移
     *
     * @param translateX canvasTranslateX
     */
    protected void changeTranslated(float translateX) {
        if (translateX < getMinTranslate()) {
            translateX = getMinTranslate();
            if (null != slidListener && itemsCount > (renderWidth / chartItemWidth)) {
                overScroller.forceFinished(true);
                slidListener.onSlidRight();
            }
        } else if (translateX > getMaxTranslate()) {
            translateX = getMaxTranslate();
            if (null != slidListener && itemsCount > (renderWidth / chartItemWidth)) {
                overScroller.forceFinished(true);
                slidListener.onSlidLeft();
            }
        }
        this.canvasTranslateX = translateX;
    }

    /**
     * 计算当前显示的数据以及显示的数据的最大最小值
     */
    protected void calcValues() {
        float scaleWidth = chartItemWidth * scaleX;
        if (canvasTranslateX <= scaleWidth / 2) {
            screenLeftIndex = (int) ((-canvasTranslateX) / scaleWidth);
            if (screenLeftIndex < 0) {
                screenLeftIndex = 0;
            }
            screenRightIndex = (int) (screenLeftIndex + (renderWidth / scaleWidth) + 0.5) + 1;
        } else {
            screenLeftIndex = 0;
            screenRightIndex = itemsCount - 1;
        }
        if (screenRightIndex > itemsCount - 1) {
            screenRightIndex = itemsCount - 1;
        }

        volMaxValue = Float.MIN_VALUE;
        float volMinValue = Float.MAX_VALUE;
        indexMaxValue = Float.MIN_VALUE;
        float mChildMinValue = Float.MAX_VALUE;
        mainMaxIndex = screenLeftIndex;
        mainMinIndex = screenLeftIndex;

        switch (calcModel) {
            case CALC_NORMAL_WITH_LAST:
            case CALC_CLOSE_WITH_LAST:
                mainMaxValue = getLastPrice();
                mainMinValue = getLastPrice();
                mainHighMaxValue = Float.MIN_VALUE;
                mainLowMinValue = Float.MAX_VALUE;
                break;
            case CALC_CLOSE_WITH_SHOW:
            case CALC_NORMAL_WITH_SHOW:
                mainMaxValue = Float.MIN_VALUE;
                mainMinValue = Float.MAX_VALUE;
                mainHighMaxValue = Float.MIN_VALUE;
                mainLowMinValue = Float.MAX_VALUE;
                break;
        }

        int tempLeft = screenLeftIndex > 0 ? screenLeftIndex + 1 : 0;
        for (int i = tempLeft; i <= screenRightIndex; i++) {
            int tempIndex = indexInterval * i;
            switch (calcModel) {
                case CALC_NORMAL_WITH_LAST:
                    if (i != itemsCount - 1) {
                        calcMainMaxValue(tempIndex);
                        calcMainMinValue(tempIndex);
                    }
                    if (mainHighMaxValue < points[tempIndex + Constants.INDEX_HIGH]) {
                        mainHighMaxValue = points[tempIndex + Constants.INDEX_HIGH];
                        mainMaxIndex = i;
                    }
                    if (mainLowMinValue >= points[tempIndex + Constants.INDEX_LOW]) {
                        mainLowMinValue = points[tempIndex + Constants.INDEX_LOW];
                        mainMinIndex = i;
                    }
                    break;
                case CALC_NORMAL_WITH_SHOW:
                    calcMainMaxValue(tempIndex);
                    calcMainMinValue(tempIndex);
                    if (mainHighMaxValue < points[tempIndex + Constants.INDEX_HIGH]) {
                        mainHighMaxValue = points[tempIndex + Constants.INDEX_HIGH];
                        mainMaxIndex = i;
                    }
                    if (mainLowMinValue >= points[tempIndex + Constants.INDEX_LOW]) {
                        mainLowMinValue = points[tempIndex + Constants.INDEX_LOW];
                        mainMinIndex = i;
                    }
                    break;
                case CALC_CLOSE_WITH_LAST:
                    float pointClose;
                    if (i != itemsCount - 1) {
                        pointClose = points[tempIndex + Constants.INDEX_CLOSE];
                        if (mainHighMaxValue < pointClose) {
                            mainHighMaxValue = pointClose;
                            mainMaxIndex = i;
                            mainMaxValue = pointClose;
                        }
                        if (mainLowMinValue >= pointClose) {
                            mainLowMinValue = pointClose;
                            mainMinIndex = i;
                            mainMinValue = pointClose;
                        }
                    }

                    break;
                case CALC_CLOSE_WITH_SHOW:
                    pointClose = points[tempIndex + Constants.INDEX_CLOSE];
                    if (mainHighMaxValue < pointClose) {
                        mainHighMaxValue = pointClose;
                        mainMaxValue = pointClose;
                        mainMaxIndex = i;
                    }
                    if (mainLowMinValue >= pointClose) {
                        mainLowMinValue = pointClose;
                        mainMinValue = pointClose;
                        mainMinIndex = i;
                    }
                    break;
            }


            switch (chartShowStatue) {
                case MAIN_VOL_INDEX:
                case MAIN_VOL:
                    float volume = points[tempIndex + Constants.INDEX_VOL];
                    volMaxValue = volumeRender.getMaxValue(
                            volMaxValue,
                            points[tempIndex + Constants.INDEX_VOL],
                            points[tempIndex + Constants.INDEX_VOL_MA_1],
                            points[tempIndex + Constants.INDEX_VOL_MA_2]);
                    if (screenRightIndex != itemsCount - 1) {
                        volMinValue = volumeRender.getMinValue(volMinValue,
                                points[tempIndex + Constants.INDEX_VOL],
                                points[tempIndex + Constants.INDEX_VOL_MA_1],
                                points[tempIndex + Constants.INDEX_VOL_MA_2]);
                    }
                    if (volume < volMinValue) {
                        volMinValue = volume;
                    }

                    if (chartShowStatue == Status.ChildStatus.MAIN_VOL) {
                        break;
                    }
                case MAIN_INDEX:
                    indexMaxValue = Math.max(indexMaxValue, indexRender.getMaxValue(Arrays.copyOfRange(points, tempIndex, tempIndex + indexInterval)));
                    mChildMinValue = Math.min(mChildMinValue, indexRender.getMinValue(Arrays.copyOfRange(points, tempIndex, tempIndex + indexInterval)));
                    break;

            }


        }


        if (mainMaxValue == mainMinValue) {
            //当最大值和最小值都相等的时候 分别增大最大值和 减小最小值
            mainMaxValue += Math.abs(mainMaxValue * 0.05f);
            mainMinValue -= Math.abs(mainMinValue * 0.05f);
        }
        double padding = (mainMaxValue - mainMinValue) * 0.05f;

        mainMaxValue += padding;
        mainMinValue = padding < mainMinValue ? mainMinValue -= padding : 0;

        switch (chartShowStatue) {
            case MAIN_VOL_INDEX:
            case MAIN_VOL:
                if (volMaxValue < 0.01f) {
                    volMaxValue = 0.01f;
                }
                volScaleY = volRect.height() * 1f / (volMaxValue - volMinValue);
                if (chartShowStatue == Status.ChildStatus.MAIN_VOL) {
                    break;
                }

            case MAIN_INDEX:
                indexMaxValue += Math.abs(indexMaxValue * 0.05f);
                mChildMinValue -= Math.abs(mChildMinValue * 0.05f);
                if (indexMaxValue == 0) {
                    indexMaxValue = 1f;
                }
                indexScaleY = indexRect.height() * 1f / (indexMaxValue - mChildMinValue);
                break;
        }
        mainScaleY = mainRect.height() * 1f / (mainMaxValue - mainMinValue);
        if (showAnim.isRunning()) {
            float value = (float) showAnim.getAnimatedValue();
            this.screenRightIndex = screenLeftIndex + Math.round(value * (this.screenRightIndex - screenLeftIndex));
        }
    }

    private void calcMainMinValue(int tempIndex) {
        mainMinValue = (status == Status.MainStatus.MA || status == Status.MainStatus.NONE) ?
                mainRender.getMinValue((float) mainMinValue,
                        points[tempIndex + Constants.INDEX_LOW],
                        points[tempIndex + Constants.INDEX_MA_1],
                        points[tempIndex + Constants.INDEX_MA_2],
                        points[tempIndex + Constants.INDEX_MA_3]) :
                mainRender.getMinValue((float) mainMinValue,
                        points[tempIndex + Constants.INDEX_LOW],
                        points[tempIndex + Constants.INDEX_BOLL_DN],
                        points[tempIndex + Constants.INDEX_BOLL_UP],
                        points[tempIndex + Constants.INDEX_BOLL_MB]);
    }

    private void calcMainMaxValue(int tempIndex) {
        mainMaxValue = (status == Status.MainStatus.MA || status == Status.MainStatus.NONE) ?
                mainRender.getMaxValue((float) mainMaxValue,
                        points[tempIndex + Constants.INDEX_HIGH],
                        points[tempIndex + Constants.INDEX_MA_1],
                        points[tempIndex + Constants.INDEX_MA_2],
                        points[tempIndex + Constants.INDEX_MA_3]) :
                mainRender.getMaxValue((float) mainMaxValue,
                        points[tempIndex + Constants.INDEX_HIGH],
                        points[tempIndex + Constants.INDEX_BOLL_DN],
                        points[tempIndex + Constants.INDEX_BOLL_UP],
                        points[tempIndex + Constants.INDEX_BOLL_MB]);
    }

    /**
     * 通过平移的位置获取X轴上的索引
     *
     * @param translateX mTranslateX
     * @return canvasTranslateX
     */
    public int indexOfTranslateX(float translateX) {
        float dataLength = getDataLength();
        if (dataLength < renderWidth) {
            return (int) ((translateX + canvasTranslateX) / chartItemWidth / scaleX + 0.5);
        } else {
            return (int) (translateX / chartItemWidth / getScaleX());
        }
    }


    /**
     * 在主区域画线
     *
     * @param startX    开始点的横坐标
     * @param stopX     开始点的值
     * @param stopValue 结束点的值
     */
    public void renderMainLine(Canvas canvas, Paint paint, float startX, float startValue, float stopX,
                               float stopValue) {
        canvas.drawLine(startX, getMainY(startValue), stopX, getMainY(stopValue), paint);
//        canvas.drawLine(startX, getMainY(startValue), stopX, getMainY(stopValue), commonTextPaint);

    }


    /**
     * 在主区域画分时线
     *
     * @param startX    开始点的横坐标
     * @param stopX     开始点的值
     * @param stopValue 结束点的值
     */
    public void renderLineFill(Canvas canvas, Paint paint, float startX, float startValue, float stopX,
                               float stopValue) {

        float y = displayHeight + chartPaddingTop + chartPaddingBottom;
        LinearGradient linearGradient = new LinearGradient(startX, chartPaddingTop,
                stopX, y, timeLineFillTopColor, timeLineFillBottomColor, Shader.TileMode.CLAMP);
        paint.setShader(linearGradient);
        float mainY = getMainY(stopValue);
        Path path = new Path();
        path.moveTo(startX, y);
        path.lineTo(startX, getMainY(startValue));
        path.lineTo(stopX, mainY);
        path.lineTo(stopX, y);
        path.close();
        canvas.drawPath(path, paint);
    }

    /**
     * 在子区域画线
     *
     * @param startX     开始点的横坐标
     * @param startValue 开始点的值
     * @param stopX      结束点的横坐标
     * @param stopValue  结束点的值
     */
    public void renderChildLine(Canvas canvas, Paint paint, float startX, float startValue, float stopX,
                                float stopValue) {
        canvas.drawLine(startX, getChildY(startValue), stopX, getChildY(stopValue), paint);
    }

    /**
     * 在子区域画线
     *
     * @param startX     开始点的横坐标
     * @param startValue 开始点的值
     * @param stopX      结束点的横坐标
     * @param stopValue  结束点的值
     */
    public void renderVolLine(Canvas canvas, Paint paint, float startX, float startValue, float stopX,
                              float stopValue) {
        canvas.drawLine(startX, getVolY(startValue), stopX, getVolY(stopValue), paint);
    }


    /**
     * 画板上的坐标点
     *
     * @param position 索引值
     * @return x坐标
     */
    public float getX(int position) {
        return position * chartItemWidth * scaleX;
    }

    /**
     * 获取适配器
     *
     * @return 获取适配器
     */
    public KLineChartAdapter getAdapter() {
        return dataAdapter;
    }


    /**
     * 获取某个点的time
     *
     * @param index index
     * @return time string
     */
    public String getTime(int index) {
        return dateTimeFormatter.format(dataAdapter.getDate(index));
    }

    /**
     * 给子区域添加画图方法
     *
     * @param childDraw IChartDraw
     */
    public void addIndexDraw(String tag, BaseRender childDraw) {
        indexRenders.put(tag, childDraw);
    }

    /**
     * 格式化时间,
     *
     * @param date date
     */
    public String formatDateTime(Date date) {
        return dateTimeFormatter.format(date);
    }


    /**
     * 开始动画
     */
    @SuppressWarnings("unused")
    public void startAnimation() {
        if (null != showAnim) {
            showAnim.start();
        }
    }

    /**
     * view中的x转化为TranslateX
     *
     * @param x x
     * @return translateX
     */
    protected float xToTranslateX(float x) {
        return -canvasTranslateX + x;
    }

    /**
     * translateX
     *
     * @return x
     */
    public float getTranslateX() {
        return canvasTranslateX;
    }

    /**
     * 是否显示选中
     */
    public boolean getShowSelected() {
        return showSelected;
    }

    /**
     * 是否显示选中
     */
    public boolean hideMarketInfo() {
        return hideMarketInfo;
    }

    /**
     * 获取选择索引
     */
    public int getSelectedIndex() {
        return selectedIndex;
    }


    public int getVolRectBottom() {
        return volRect.bottom;
    }

    /**
     * 获取新的价格
     *
     * @return new price
     */
    public float getLastPrice() {
        return lastPrice;
    }

    /**
     * 获取最新的成交量
     *
     * @return new vol value
     */
    public float getLastVol() {
        return lastVol;
    }

    public float getTranslationScreenMid() {
        return getX(screenLeftIndex) + (renderWidth / 2);
    }

    /**
     * 选中点变化时的监听
     */
    public interface OnSelectedChangedListener {
        /**
         * 当选点中变化时
         *
         * @param view  当前view
         * @param index 选中点的索引
         */
        void onSelectedChanged(BaseKChartView view, int index, float... values);
    }

    /**
     * 执行一个动画变换
     *
     * @param start    start
     * @param end      end
     * @param listener listener
     * @return ValueAnimator
     */
    @SuppressWarnings("all")
    public void generaterAnimator(Float start, float end, ValueAnimator.AnimatorUpdateListener listener) {
        ValueAnimator animator = ValueAnimator.ofFloat(0 == start ? end - 0.001f : start, end);
        animator.setDuration(duration);
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                if (isAnimationLast) {
                    listener.onAnimationUpdate(valueAnimator);
                }
            }
        });
        animator.start();
    }


    /**
     * 15毫秒内不重复刷新页面
     */
    public void animInvalidate() {
        if (System.currentTimeMillis() - time > 15) {
            invalidate();
            time = System.currentTimeMillis();
        }
    }

    /**
     * 设置是否以动画的方式变化最后一根线
     *
     * @return whether show last chart with anim
     */
    public boolean isAnimationLast() {
        return isAnimationLast;
    }

    /**
     * 获取文字画笔
     *
     * @return textPaint
     */
    public Paint getCommonTextPaint() {
        return commonTextPaint;
    }

    public IValueFormatter getValueFormatter() {
        return valueFormatter;
    }

    /**
     * 获取上方padding
     */
    public float getChartPaddingTop() {
        return chartPaddingTop;
    }

    /**
     * 获取当前主图状态
     *
     * @return {@link Status.MainStatus}
     */
    public Status.MainStatus getStatus() {
        return this.status;
    }

    public Status.KlineStatus getKlineStatus() {
        return klineStatus;
    }

    public Status.VolChartStatus getVolChartStatus() {
        return volChartStatus;
    }

    public float getViewWidth() {
        return viewWidth;
    }

    public float getChartWidth() {
        if (yLabelModel == YLabelModel.LABEL_NONE_GRID) {
            return renderWidth;
        } else {
            return viewWidth;
        }
    }

    @Override
    protected void onDetachedFromWindow() {
        logoBitmap = null;
        super.onDetachedFromWindow();
    }
}
