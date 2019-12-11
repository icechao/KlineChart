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
import com.icechao.klinelib.draw.MainDraw;
import com.icechao.klinelib.draw.VolumeDraw;
import com.icechao.klinelib.formatter.DateFormatter;
import com.icechao.klinelib.formatter.ValueFormatter;
import com.icechao.klinelib.utils.Constants;
import com.icechao.klinelib.utils.NumberTools;
import com.icechao.klinelib.utils.SlidListener;
import com.icechao.klinelib.utils.Status;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

/*************************************************************************
 * Description   : 绘制分类,BaseKLineChartView只绘制所有视图通用的图形 其他区域的绘制分别由对应的绘制类完成
 *
 * @PackageName  : com.icechao.klinelib.utils
 * @FileName     : BaseKLineChartView.java
 * @Author       : chao
 * @Date         : 2019/4/8
 * @Email        : icechliu@gmail.com
 * @version      : V2
 *************************************************************************/
public abstract class BaseKLineChartView extends ScrollAndScaleView {

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
    protected long duration = 300;

    /**
     * 当前子视图的索引
     */
    protected Status.IndexStatus indexDrawPosition = Status.IndexStatus.NONE;

    /**
     * 绘制K线时画板平移的距离
     */
    private float canvasTranslateX;

    protected float width = 0;

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
    protected Paint textPaint = new Paint(Paint.ANTI_ALIAS_FLAG);


    /**
     * 十字线横线画笔
     */
    protected Paint selectedXLinePaint = new Paint(Paint.ANTI_ALIAS_FLAG);

    /**
     * 十字线竖线画笔
     */
    protected Paint selectedYLinePaint = new Paint(Paint.ANTI_ALIAS_FLAG);

    /**
     * 十字线坐标显示背景相交点画笔
     */
    protected Paint selectedPriceBoxBackgroundPaint = new Paint(Paint.ANTI_ALIAS_FLAG);

    /**
     * 十字线坐标显示边框画笔
     */
    protected Paint selectorFramePaint = new Paint(Paint.ANTI_ALIAS_FLAG);

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
    protected float priceLineBoxHeight = 40;

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
    protected MainDraw mainDraw;

    /**
     * 交易量视图
     */
    protected VolumeDraw volDraw;

    /**
     * 所有子视图
     */
    protected IChartDraw indexDraw;

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
    protected float yLabelMarginRight;

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
     * kline right padding
     */
    protected float klinePaddingRight;

    /**
     * refresh time limit
     */
    private long time;

    /**
     * 背景颜色
     */
    protected int backGroundColor;

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
     * 显示右侧虚线
     */
//    protected boolean showRightDotLine = true;

    /**
     * 数据观察者,当数据变化
     */
    protected DataSetObserver dataSetObserver = new DataSetObserver() {
        @Override
        public void onChanged() {
            //从没数据变成有数据时,显示动画加载效果
            int currentCount = BaseKLineChartView.this.itemsCount;
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
            gridRowCountWithChild = 0;
            gridRowCountNoChild = 0;
            //再次开启动画
            action.run();
        }

        @Override
        public void onInvalidated() {
            tempTranslation = canvasTranslateX;
            isAnimationLast = false;
            overScroller.forceFinished(true);
            setScrollEnable(false);
            setScaleEnable(false);
        }
    };

    private float tempTranslation;


    /**
     * 当重置数据时,延时400ms显示最后的加载动画
     */
    protected Runnable action = () -> {
        isAnimationLast = true;
        setScrollEnable(true);
        setScaleEnable(true);
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
            mainDraw.startAnim(BaseKLineChartView.this, tempData);
            volDraw.startAnim(BaseKLineChartView.this, tempData);
        }
    }

    /**
     * 当前数据个数
     */
    protected int itemsCount;


    /**
     * 指标视图
     */
    protected List<IChartDraw> indexDraws = new ArrayList<>();

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
    protected float lineEndRadiu;
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
    protected Status.CrossTouchModel crossTouchModel = Status.CrossTouchModel.FOLLOW_FINGERS;

    /**
     * 选中价格框横向padding
     */
    protected float selectedPriceBoxHorizentalPadding;
    /**
     * 选中日期框横向padding
     */
    protected float dateBoxlHorizentalPadding;

    /**
     * 选中日期框纵向padding
     */
    protected float dateBoxVerticalPadding;
    /**
     * 选中价格框纵向padding
     */
    protected float selectedPriceBoxVerticalPadding;

    public BaseKLineChartView(Context context) {
        super(context);
        init();
    }

    public BaseKLineChartView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public BaseKLineChartView(Context context, AttributeSet attrs, int defStyleAttr) {
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
        selectorFramePaint.setStyle(Paint.Style.STROKE);
        priceLinePaint.setAntiAlias(true);
        priceLineRightPaint.setStyle(Paint.Style.STROKE);
        rightPriceBoxPaint.setStyle(Paint.Style.FILL_AND_STROKE);
        priceLineBoxPaint.setStyle(Paint.Style.STROKE);
        selectedXLinePaint.setStyle(Paint.Style.STROKE);

    }


    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        this.width = w;
        //如果没设置chartPaddingBottom
        if (0 == chartPaddingBottom) {
            chartPaddingBottom = (int) (dateBoxVerticalPadding * 2 + selectorFramePaint.getStrokeWidth() * 2 + textHeight);
        }
        displayHeight = h - chartPaddingTop - (dateBoxVerticalPadding * 2 + selectorFramePaint.getStrokeWidth() * 2 + textHeight);
        rowSpace = displayHeight / gridRows;
        columnSpace = width / gridColumns;
        initRect();
    }

    protected Status.ChildStatus chartShowStatue = Status.ChildStatus.MAIN_VOL;

    /**
     * 初始化视图区域
     * 主视图
     * 成交量视图
     * 子视图
     */
    protected void initRect() {
        int tempMainHeight = 0, tempVolHeight, tempChildHeight;
        switch (chartShowStatue) {
            case MAIN_VOL:
                tempMainHeight = (int) (displayHeight * mainPercent / 10f);
                tempVolHeight = (int) (displayHeight * IndexPercent / 10f);
                mainRect = new Rect(0, chartPaddingTop, (int) width, chartPaddingTop + tempMainHeight);
                volRect = new Rect(0, (int) (mainRect.bottom + childViewPaddingTop), (int) width, mainRect.bottom + tempVolHeight);
                indexRect = null;
                break;
            case MAIN_ONLY:
                mainRect = new Rect(0, chartPaddingTop, (int) width, (int) (chartPaddingTop + displayHeight));
                volRect = null;
                indexRect = null;
                break;
            case MAIN_INDEX:
                tempMainHeight = (int) (displayHeight * mainPercent / 10f);
                tempChildHeight = (int) (displayHeight * IndexPercent / 10f);
                mainRect = new Rect(0, chartPaddingTop, (int) width, chartPaddingTop + tempMainHeight);
                indexRect = new Rect(0, (int) (mainRect.bottom + childViewPaddingTop), (int) width, mainRect.bottom + tempChildHeight);
                volRect = null;
                break;
            default:
            case MAIN_VOL_INDEX:
                tempChildHeight = (int) (displayHeight * IndexPercent / 10f);
                tempVolHeight = (int) (displayHeight * volPercent / 10f);
                tempMainHeight = (int) (displayHeight * (10 - volPercent - IndexPercent) / 10f);
                mainRect = new Rect(0, chartPaddingTop, (int) width, chartPaddingTop + tempMainHeight);
                volRect = new Rect(0, (int) (mainRect.bottom + childViewPaddingTop), (int) width, mainRect.bottom + tempVolHeight);
                indexRect = new Rect(0, (int) (volRect.bottom + childViewPaddingTop), (int) width, volRect.bottom + tempChildHeight);
                break;
        }
        if (null != logoBitmap) {
            logoTop = mainRect.bottom - logoBitmap.getHeight() - logoPaddingBottom;
        }
        gridRowCountWithChild = 0;
        gridRowCountNoChild = 0;
    }

    @Override
    public void onDraw(Canvas canvas) {
        drawBackground(canvas);
        drawGirdLines(canvas);
        drawLogo(canvas);
        if (!isShowLoading && width != 0 && 0 != itemsCount && null != points && points.length != 0) {
            try {
                calcValues();
                drawXLabels(canvas);
                drawK(canvas);
                drawValue(canvas, getShowSelected() ? selectedIndex : itemsCount - 1);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }


    protected float logoTop;

    protected float logoPaddingLeft, logoPaddingBottom;

    protected void drawLogo(Canvas canvas) {
        if (null != logoBitmap) {
            canvas.drawBitmap(logoBitmap, logoPaddingLeft, logoTop, logoPaint);
        }
    }

    /**
     * 绘制整体背景
     *
     * @param canvas canvas
     */
    protected void drawBackground(Canvas canvas) {
        float mid = width / 2, bottom;
        int mainBottom = mainRect.bottom;
        backgroundPaint.setShader(new LinearGradient(mid, 0, mid, mainBottom, backGroundFillTopColor, backGroundFillBottomColor, Shader.TileMode.CLAMP));
        canvas.drawRect(0, 0, width, mainBottom, backgroundPaint);
        switch (chartShowStatue) {
            case MAIN_VOL:
                bottom = volRect.bottom + chartPaddingBottom;
                backgroundPaint.setShader(new LinearGradient(mid, mainBottom, mid, bottom, backGroundFillTopColor, backGroundFillBottomColor, Shader.TileMode.CLAMP));
                canvas.drawRect(0, mainBottom, width, bottom, backgroundPaint);
                break;
            case MAIN_INDEX:
                bottom = indexRect.bottom + chartPaddingBottom;
                backgroundPaint.setShader(new LinearGradient(mid, mainBottom, mid, bottom, backGroundFillTopColor, backGroundFillBottomColor, Shader.TileMode.CLAMP));
                canvas.drawRect(0, mainBottom, width, indexRect.bottom, backgroundPaint);
                break;
            case MAIN_VOL_INDEX:
                bottom = volRect.bottom;
                backgroundPaint.setShader(new LinearGradient(mid, mainBottom, mid, bottom, backGroundFillTopColor, backGroundFillBottomColor, Shader.TileMode.CLAMP));
                canvas.drawRect(0, mainBottom, width, bottom, backgroundPaint);
                float indexBottom = indexRect.bottom;
                backgroundPaint.setShader(new LinearGradient(mid, bottom, mid, indexBottom, backGroundFillTopColor, backGroundFillBottomColor, Shader.TileMode.CLAMP));
                canvas.drawRect(0, bottom, width, indexBottom, backgroundPaint);
                break;
            case MAIN_ONLY:
                backgroundPaint.setShader(new LinearGradient(mid, 0, mid, mainBottom, backGroundFillTopColor, backGroundFillBottomColor, Shader.TileMode.CLAMP));
                canvas.drawRect(0, 0, width, mainBottom, backgroundPaint);
                break;
        }


    }

    /**
     * 重置所有数据
     * <p>
     * 可能会出现没效果,动画执行过程中,值改变
     */
    protected void resetValues() {
        lastVol = 0;
        lastPrice = 0;
        lastPrice = 0;
        itemsCount = 0;
        selectedIndex = -1;
        screenLeftIndex = 0;
        screenRightIndex = 0;
        gridRowCountNoChild = 0;
        gridRowCountWithChild = 0;
    }


    /**
     * 设置当前K线总数据个数
     *
     * @param itemCount items count
     */
    protected void setItemsCount(int itemCount) {

        this.itemsCount = itemCount;
        mainDraw.setItemCount(itemsCount);
        mainDraw.resetValues();
        volDraw.setItemCount(itemsCount);
        volDraw.resetValues();
        int size = indexDraws.size();
        for (int i = 0; i < size; i++) {
            IChartDraw iChartDraw = indexDraws.get(i);
            iChartDraw.setItemCount(0);
            iChartDraw.resetValues();
        }
        invalidate();
    }

    /**
     * 计算主View的value对应的Y值
     *
     * @param value value
     * @return location Y
     */
    public float getMainY(float value) {
        double v = mainMaxValue - value;
        if (v <= 0) {
            return mainRect.top + 1;
        }
        return borderCheck(v * mainScaleY + mainRect.top, mainRect.bottom);
    }

    /**
     * Y轴值的极限值检测
     *
     * @param v      view
     * @param bottom boder bottom
     * @return value
     */

    protected float borderCheck(double v, int bottom) {
        return v >= bottom ? bottom - 1 : (float) v;
    }


    /**
     * 计算成交量View的Y值
     *
     * @param value value
     * @return location y
     */
    public float getVolY(float value) {
        float v = volMaxValue - value;
        if (v <= 0) {
            return volRect.top + 1;
        }
        return borderCheck(v * volScaleY + volRect.top, volRect.bottom);
    }

    /**
     * 计算子View的Y值
     *
     * @param value value
     * @return location y
     */
    public float getChildY(float value) {
        float v = indexMaxValue - value;
        if (v < 0) {
            return indexRect.top;
        }
        return borderCheck(v * indexScaleY + indexRect.top, indexRect.bottom);
    }

//    /**
//     * 修复text位置
//     */
//    protected float fixTextYBaseBottom(float y) {
//        return y + (textHeight) / 2 - textDecent;
//    }

    /**
     * 画网格
     *
     * @param canvas canvas
     */
    protected void drawGirdLines(Canvas canvas) {

        for (int i = 0; i <= gridRows; i++) {
            float y = rowSpace * i + chartPaddingTop;
            canvas.drawLine(0, y, width, y, gridPaint);
        }
        for (int i = 1; i < gridColumns; i++) {
            float stopX = columnSpace * i;
            canvas.drawLine(stopX, 0, stopX, displayHeight + chartPaddingTop, gridPaint);
        }
    }


    /**
     * 绘制k线图
     *
     * @param canvas canvas
     */
    protected void drawK(Canvas canvas) {
        canvas.save();
        canvas.translate(canvasTranslateX, 0);
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
                    indexDraw.drawTranslated(canvas, lastX, curX, this, i, values);
                case MAIN_VOL:
                    volDraw.drawTranslated(canvas, lastX, curX, this, i, values);
                case MAIN_ONLY:
                    mainDraw.drawTranslated(canvas, lastX, curX, this, i, values);
                    break;
                case MAIN_INDEX:
                    indexDraw.drawTranslated(canvas, lastX, curX, this, i, values);
                    mainDraw.drawTranslated(canvas, lastX, curX, this, i, values);
                    break;
            }
        }
        mainDraw.drawMaxMinValue(canvas, this, getX(mainMaxIndex), mainHighMaxValue, getX(mainMinIndex), mainLowMinValue);
        drawYLabels(canvas);
        if (getShowSelected()) {
            drawSelected(canvas, getX(selectedIndex));
        }
        canvas.restore();
    }


    public void drawSelected(Canvas canvas, float x) {
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
        textWidth = textPaint.measureText(date);
        //向下多移动出一个像素(如果有必要可以设置多移动网络线宽度)
        y = chartPaddingTop + displayHeight + 1;
        float temp = textWidth / 2;
        right = x + temp + dateBoxlHorizentalPadding;
        left = x - temp - dateBoxlHorizentalPadding;
        float screenRightX = xToTranslateX(width);
        if (betterSelectedX) {
            if (right > screenRightX) {
                right = screenRightX;
                left = right - dateBoxlHorizentalPadding * 2 - textWidth;
            } else if (left < xToTranslateX(0)) {
                left = xToTranslateX(0);
                right = left + dateBoxlHorizentalPadding * 2 + textWidth;
            }
        }

        bottom = y + textHeight + dateBoxVerticalPadding * 2;
        canvas.drawRect(left, y, right, bottom, selectedPriceBoxBackgroundPaint);
        canvas.drawRect(left, y, right, bottom, selectorFramePaint);
        canvas.drawText(date, left + selectedPriceBoxHorizentalPadding, y + baseLine + dateBoxVerticalPadding, textPaint);
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
                text = NumberTools.formatAmount(volDraw.getValueFormatter().format((volMaxValue / volRect.height() * (volRect.bottom - selectedY))));
            } else if (null != indexDraw && selectedY < indexRect.bottom) {
                text = getValueFormatter().format((indexMaxValue / indexRect.height() * (indexRect.bottom - selectedY)));
            } else if (null != indexDraw && selectedY < indexRect.top) {
                return;
            } else {
                return;
            }
        } else {
            text = valueFormatter.format(points[selectedIndex * indexInterval + Constants.INDEX_CLOSE]);
            y = getMainY(points[selectedIndex * indexInterval + Constants.INDEX_CLOSE]);
        }
        canvas.drawLine(-canvasTranslateX, y, -canvasTranslateX + width, y, selectedXLinePaint);
        //十字线交点
        if (showCrossPoint) {
            canvas.drawCircle(x, y, chartItemWidth, selectedbigCrossPaint);
            canvas.drawCircle(x, y, selectedPointRadius, selectedCrossPaint);
        }
        // 选中状态下的Y值
        textWidth = textPaint.measureText(text);
        temp = textHeight / 2 + selectedPriceBoxVerticalPadding;
        float tempX = textWidth + 2 * selectedPriceBoxHorizentalPadding;
        float boxTop = y - temp;
        float boXBottom = y + temp;
        if (getX(selectedIndex - screenLeftIndex) > width / 2) {
            x = -canvasTranslateX + width - textWidth - 1 - 2 * selectedPriceBoxHorizentalPadding - selectedPriceBoxVerticalPadding;
            path = new Path();
            path.moveTo(x, y);
            path.lineTo(x + selectedPriceBoxHorizentalPadding + selectedPriceBoxVerticalPadding, boXBottom);
            path.lineTo(-canvasTranslateX + width - 2, boXBottom);
            path.lineTo(-canvasTranslateX + width - 2, boxTop);
            path.lineTo(x + selectedPriceBoxHorizentalPadding + selectedPriceBoxVerticalPadding, boxTop);
            path.close();
            canvas.drawPath(path, selectedPriceBoxBackgroundPaint);
            canvas.drawPath(path, selectedXLinePaint);
            canvas.drawText(text, x + selectedPriceBoxVerticalPadding + selectedPriceBoxHorizentalPadding, boxTop + selectedPriceBoxVerticalPadding + baseLine, textPaint);
        } else {
            x = -canvasTranslateX;
            path = new Path();
            path.moveTo(x, boxTop);
            path.lineTo(x, boXBottom);
            path.lineTo(tempX + x, boXBottom);
            path.lineTo(tempX + x + selectedPriceBoxHorizentalPadding + selectedPriceBoxVerticalPadding, y);
            path.lineTo(tempX + x, boxTop);
            path.close();
            canvas.drawPath(path, selectedPriceBoxBackgroundPaint);
            canvas.drawPath(path, selectedXLinePaint);
            canvas.drawText(text, x + selectedPriceBoxHorizentalPadding, boxTop + selectedPriceBoxVerticalPadding + baseLine, textPaint);
        }
    }


    /**
     * 绘制所有的Labels
     *
     * @param canvas canvas
     */
    protected void drawXLabels(Canvas canvas) {

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
                canvas.drawText(formatDateTime(getAdapter().getDate(screenLeftIndex)), 0, y, textPaint);
            }
            //X轴最右侧的值
            translateX = xToTranslateX(width);
            if (translateX >= startX && translateX <= stopX) {
                String text = formatDateTime(getAdapter().getDate(screenRightIndex));
                canvas.drawText(text, width - textPaint.measureText(text), y, textPaint);

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
                canvas.drawText(text, tempX - textPaint.measureText(text) / 2, y, textPaint);
            }
        }
    }

    private double rowValueNoChild, rowValueWithChild;
    protected int gridRowCountNoChild;
    protected int gridRowCountWithChild;

    /**
     * 绘制Y轴上的所有label
     *
     * @param canvas canvase
     */
    protected void drawYLabels(Canvas canvas) {
        double rowValue;
        float tempYLabelX;
        int gridRowCount;
        String maxVol, childLable;
        float tempRight = xToTranslateX(width);
        switch (chartShowStatue) {
            case MAIN_VOL_INDEX:
                if (0 == gridRowCountWithChild) {
                    gridRowCountWithChild = gridRows - 2;
                    rowValueWithChild = (mainMaxValue - mainMinValue) / gridRowCountWithChild;
                }
                gridRowCount = gridRowCountWithChild;
                rowValue = rowValueWithChild;
                tempYLabelX = tempRight - yLabelMarginRight;
                //Y轴上网络的值
                for (int i = 0; i <= gridRowCount; i++) {
                    String text = valueFormatter.format((float) (mainMaxValue - i * rowValue));
                    float v = rowSpace * i + chartPaddingTop;
                    canvas.drawText(text, tempYLabelX -
                            textPaint.measureText(text), v - mainYMoveUpInterval, textPaint);

                }

                maxVol = NumberTools.formatAmount(volDraw.getValueFormatter().
                        format(volMaxValue));
                canvas.drawText(maxVol, tempYLabelX -
                        textPaint.measureText(maxVol), mainRect.bottom + baseLine, textPaint);


                //子图Y轴label

                childLable = indexDraw.getValueFormatter().format(indexMaxValue);
                canvas.drawText(childLable, tempYLabelX -
                        textPaint.measureText(childLable), volRect.bottom + baseLine, textPaint);


                break;
            case MAIN_VOL:
                if (0 == gridRowCountNoChild) {
                    gridRowCountNoChild = gridRows - 1;
                    rowValueNoChild = (mainMaxValue - mainMinValue) / gridRowCountNoChild;
                }
                gridRowCount = gridRowCountNoChild;
                rowValue = rowValueNoChild;

                tempYLabelX = tempRight - yLabelMarginRight;
                //Y轴上网络的值
                for (int i = 0; i <= gridRowCount; i++) {
                    String text = valueFormatter.format((float) (mainMaxValue - i * rowValue));
                    float v = rowSpace * i + chartPaddingTop;
                    canvas.drawText(text, tempYLabelX -
                            textPaint.measureText(text), v - mainYMoveUpInterval, textPaint);

                }

                maxVol = NumberTools.formatAmount(volDraw.getValueFormatter().
                        format(volMaxValue));
                canvas.drawText(maxVol, tempYLabelX -
                        textPaint.measureText(maxVol), mainRect.bottom + baseLine, textPaint);


            case MAIN_ONLY:
                if (0 == gridRowCountNoChild) {
                    gridRowCountNoChild = gridRows;
                    rowValueNoChild = (mainMaxValue - mainMinValue) / gridRowCountNoChild;
                }
                gridRowCount = gridRowCountNoChild;
                rowValue = rowValueNoChild;

                tempYLabelX = tempRight - yLabelMarginRight;
                //Y轴上网格的值
                for (int i = 0; i <= gridRowCount; i++) {
                    String text = valueFormatter.format((float) (mainMaxValue - i * rowValue));
                    float v = rowSpace * i + chartPaddingTop;
                    canvas.drawText(text, tempYLabelX -
                            textPaint.measureText(text), v - mainYMoveUpInterval, textPaint);
                }
                break;
            case MAIN_INDEX:
                if (0 == gridRowCountNoChild) {
                    gridRowCountNoChild = gridRows - 1;
                    rowValueNoChild = (mainMaxValue - mainMinValue) / gridRowCountNoChild;
                }
                gridRowCount = gridRowCountNoChild;
                rowValue = rowValueNoChild;

                tempYLabelX = tempRight - yLabelMarginRight;
                //Y轴上网络的值
                for (int i = 0; i <= gridRowCount; i++) {
                    String text = valueFormatter.format((float) (mainMaxValue - i * rowValue));
                    float v = rowSpace * i + chartPaddingTop;
                    canvas.drawText(text, tempYLabelX - textPaint.measureText(text), v - mainYMoveUpInterval, textPaint);
                }
                childLable = indexDraw.getValueFormatter().format(indexMaxValue);
                canvas.drawText(childLable, tempYLabelX -
                        textPaint.measureText(childLable), indexRect.top - childViewPaddingTop + baseLine, textPaint);
                break;
        }
        drawPriceLine(canvas, tempRight);
    }

    private void drawPriceLine(Canvas canvas, float tempRight) {
        float y = getMainY(lastPrice);
        String priceText = valueFormatter.format(lastPrice);
        float textWidth = textPaint.measureText(priceText);
        float textLeft = tempRight - textWidth;
        float klineRight = getX(screenRightIndex);
        if (screenRightIndex == itemsCount - 1 && klineRight < textLeft) {
            drawKlineRightSpace(canvas, y, priceText, textWidth, textLeft, klineRight);
        } else {
            float halfPriceBoxHeight = priceLineBoxHeight / 2;
            //要想保证价格框仅绘制在主区域内,取消注释代码
//            if (lastPrice > mainMaxValue) {
//                y = mainRect.top + halfPriceBoxHeight;
//            } else if (lastPrice < mainMinValue) {
//                y = mainRect.bottom - halfPriceBoxHeight;
//            }
            for (int i = 0; i < tempRight; i += 12) {
                canvas.drawLine(i, y, i + 8, y, priceLinePaint);
            }
            float halfHeight = textHeight / 2;
            float boxRight = tempRight - priceLineBoxMarginRight;
            float boxLeft = boxRight - textWidth - priceShapeWidth - priceLineBoxPadidng * 2 - priceBoxShapeTextMargin;
            float boxTop = y - halfPriceBoxHeight;
            float boxBottom = y + halfPriceBoxHeight;
            canvas.drawRoundRect(new RectF(boxLeft, boxTop, boxRight, boxBottom), halfPriceBoxHeight, halfPriceBoxHeight, priceLineBoxBgPaint);
            canvas.drawRoundRect(new RectF(boxLeft, boxTop, boxRight, boxBottom), halfPriceBoxHeight, halfPriceBoxHeight, priceLineBoxPaint);

            float temp = priceShapeHeight / 2;
            float shapeLeft = boxRight - priceShapeWidth - priceLineBoxPadidng;
            //价格线三角形
            Path shape = new Path();
            shape.moveTo(shapeLeft, y - temp);
            shape.lineTo(shapeLeft, y + temp);
            shape.lineTo(shapeLeft + priceShapeWidth, y);
            shape.close();
            canvas.drawPath(shape, textPaint);
            canvas.drawText(priceText, boxLeft + priceLineBoxPadidng, (y + (halfHeight - textDecent)), textPaint);
        }
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
        canvas.drawCircle(stopX, getMainY(lastPrice), lineEndRadiu * lineEndMaxMultiply, lineEndPointPaint);
    }


    protected void drawKlineRightSpace(Canvas canvas, float y, String priceString, float textWidth, float textLeft, float endLineRight) {
        //两个价格图层在点之间所以放在价格线中绘制
        if (klineStatus.showLine()) {
            drawEndPoint(canvas, endLineRight);
        }
        for (float i = endLineRight; i < textLeft - 5; i += 12) {
            canvas.drawLine(i, y, i + 8, y, priceLineRightPaint);
        }
        float halfTextHeight = textHeight / 2;
        float top = y - halfTextHeight;
        canvas.drawRect(new Rect((int) textLeft, (int) top, (int) (textLeft + textWidth), (int) (y + halfTextHeight)), rightPriceBoxPaint);
        canvas.drawText(priceString, textLeft, top + baseLine, priceLineRightTextPaint);
        //绘制价格圆点
        if (klineStatus.showLine()) {
            canvas.drawCircle(endLineRight, y, lineEndRadiu, lineEndFillPointPaint);
        }
    }

    protected float getDataLength() {
        float length = chartItemWidth * getScaleX() * (itemsCount - 1) + getOverScrollRange();
        if (length <= width && isScrollEnable()) {
            setScrollEnable(false);
        } else if (!isScrollEnable() && length > width) {
            setScrollEnable(true);
        }
        return length;
    }

    /**
     * 开启循环刷新绘制
     */
    public void startFreshPage() {
        if (null != valueAnimator && valueAnimator.isRunning()) {
            return;
        }
        valueAnimator = ValueAnimator.ofFloat(lineEndRadiu, lineEndRadiu * lineEndMaxMultiply);
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
    protected void drawValue(Canvas canvas, int position) {
        int temp = indexInterval * position;
        if (temp < points.length && position >= 0) {
            float[] tempValues = Arrays.copyOfRange(points, temp, temp + indexInterval);
            float x = getPaddingLeft();
            switch (chartShowStatue) {
                case MAIN_INDEX:
                    mainDraw.drawText(canvas, this, x, mainRect.top + baseLine - textHeight / 2, position, tempValues);
                    indexDraw.drawText(canvas, this, x, mainRect.bottom + baseLine, position, tempValues);
                    break;
                case MAIN_VOL_INDEX:
                    indexDraw.drawText(canvas, this, x, volRect.bottom + baseLine, position, tempValues);
                case MAIN_VOL:
                    volDraw.drawText(canvas, this, x, mainRect.bottom + baseLine, position, tempValues);
                case MAIN_ONLY:
                    mainDraw.drawText(canvas, this, x, mainRect.top + baseLine - textHeight / 2, position, tempValues);
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
        if (width == 0) {
            width = getMeasuredWidth();
        }
        if (dataLength > width) {
            float minValue = -(dataLength - width);
            return (getOverScrollRange() == 0) ? minValue - chartItemWidth * getScaleX() / 2 : minValue;
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
        if (dataLength > width) {
            return klineStatus.showLine() ? 0 : chartItemWidth * getScaleX() / 2;
        }
        return width - dataLength + getOverScrollRange() - (klineStatus.showLine() ? 0 : chartItemWidth * getScaleX() / 2);
    }

    @Override
    protected void onScrollChanged(int l, int t, int oldl, int oldt) {
        super.onScrollChanged(l, t, oldl, oldt);
        changeTranslated(canvasTranslateX + (l - oldl));
        if (klineStatus.showLine() && getX(screenRightIndex) + canvasTranslateX <= width) {
            startFreshPage();
        } else {
            stopFreshPage();
        }
        gridRowCountWithChild = 0;
        gridRowCountNoChild = 0;
        animInvalidate();
    }

    @Override
    protected void onScaleChanged(float scale, float oldScale) {
        //通过 放大和左右左右个数设置左移
        if (scale == oldScale) {
            return;
        }
        float tempWidth = chartItemWidth * scale;
        float newCount = (width / tempWidth);
        float oldCount = (width / chartItemWidth / oldScale);
        float difCount = (newCount - oldCount) / 2;
        if (screenLeftIndex > 0) {
            changeTranslated(canvasTranslateX / oldScale * scale + difCount * tempWidth);
        } else {
            if (getDataLength() < width) {
                changeTranslated(-(width - getDataLength()));
            } else {
                changeTranslated(getMaxTranslate());
            }
        }
        gridRowCountWithChild = 0;
        gridRowCountNoChild = 0;
        animInvalidate();
    }


    /**
     * 设置当前平移
     *
     * @param translateX canvasTranslateX
     */
    protected void changeTranslated(float translateX) {
        if (translateX < getMinTranslate()) {
            translateX = getMinTranslate();
            if (null != slidListener && itemsCount > (width / chartItemWidth)) {
                overScroller.forceFinished(true);
                slidListener.onSlidRight();
            }
        } else if (translateX > getMaxTranslate()) {
            translateX = getMaxTranslate();
            if (null != slidListener && itemsCount > (width / chartItemWidth)) {
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
            screenRightIndex = (int) (screenLeftIndex + ((width - klinePaddingRight) / scaleWidth) + 0.5) + 1;
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
                mainHighMaxValue = getLastPrice();
                mainLowMinValue = getLastPrice();
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
                    volMaxValue = volDraw.getMaxValue(
                            volMaxValue,
                            points[tempIndex + Constants.INDEX_VOL],
                            points[tempIndex + Constants.INDEX_VOL_MA_1],
                            points[tempIndex + Constants.INDEX_VOL_MA_2]);
                    if (screenRightIndex != itemsCount - 1) {
                        volMinValue = volDraw.getMinValue(volMinValue,
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
                    indexMaxValue = Math.max(indexMaxValue, indexDraw.getMaxValue(Arrays.copyOfRange(points, tempIndex, tempIndex + indexInterval)));
                    mChildMinValue = Math.min(mChildMinValue, indexDraw.getMinValue(Arrays.copyOfRange(points, tempIndex, tempIndex + indexInterval)));
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
                if (volMaxValue < 0.001) {
                    volMaxValue = 0.1f;
                }
                volScaleY = volRect.height() * 1f / (volMaxValue - volMinValue);
                if (chartShowStatue == Status.ChildStatus.MAIN_VOL) {
                    break;
                }

            case MAIN_INDEX:
                indexMaxValue += Math.abs(indexMaxValue * 0.03f);
                mChildMinValue -= Math.abs(mChildMinValue * 0.03f);
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
                mainDraw.getMinValue((float) mainMinValue,
                        points[tempIndex + Constants.INDEX_LOW],
                        points[tempIndex + Constants.INDEX_MA_1],
                        points[tempIndex + Constants.INDEX_MA_2],
                        points[tempIndex + Constants.INDEX_MA_3]) :
                mainDraw.getMinValue((float) mainMinValue,
                        points[tempIndex + Constants.INDEX_LOW],
                        points[tempIndex + Constants.INDEX_BOLL_DN],
                        points[tempIndex + Constants.INDEX_BOLL_UP],
                        points[tempIndex + Constants.INDEX_BOLL_MB]);
    }

    private void calcMainMaxValue(int tempIndex) {
        mainMaxValue = (status == Status.MainStatus.MA || status == Status.MainStatus.NONE) ?
                mainDraw.getMaxValue((float) mainMaxValue,
                        points[tempIndex + Constants.INDEX_HIGH],
                        points[tempIndex + Constants.INDEX_MA_1],
                        points[tempIndex + Constants.INDEX_MA_2],
                        points[tempIndex + Constants.INDEX_MA_3]) :
                mainDraw.getMaxValue((float) mainMaxValue,
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
        if (width == 0) {
            width = getMeasuredWidth();
        }
        if (dataLength < width) {
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
    public void drawMainLine(Canvas canvas, Paint paint, float startX, float startValue, float stopX,
                             float stopValue) {
        canvas.drawLine(startX, getMainY(startValue), stopX, getMainY(stopValue), paint);
    }


    /**
     * 在主区域画分时线
     *
     * @param startX    开始点的横坐标
     * @param stopX     开始点的值
     * @param stopValue 结束点的值
     */
    public void drawFill(Canvas canvas, Paint paint, float startX, float startValue, float stopX,
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
    public void drawChildLine(Canvas canvas, Paint paint, float startX, float startValue, float stopX,
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
    public void drawVolLine(Canvas canvas, Paint paint, float startX, float startValue, float stopX,
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
    public void addIndexDraw(IChartDraw childDraw) {
        indexDraws.add(childDraw);
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
    public boolean forceHideMarket() {
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
        return getX(screenLeftIndex) + (width / 2);
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
        void onSelectedChanged(BaseKLineChartView view, int index, float... values);

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
     * 刷新界面
     */
    public void animInvalidate() {
        if (System.currentTimeMillis() - time > 16) {
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
    public Paint getTextPaint() {
        return textPaint;
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
     * 获取图的宽度
     *
     * @return 宽度
     */
    public float getViewWidth() {
        return width;
    }


    /**
     * 获取K线滑动超出范围
     *
     * @return
     */
    public float getOverScrollRange() {
        return overScrollRange;
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
}
