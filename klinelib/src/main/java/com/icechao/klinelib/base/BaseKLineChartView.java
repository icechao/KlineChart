package com.icechao.klinelib.base;

import android.animation.ValueAnimator;
import android.content.Context;
import android.database.DataSetObserver;
import android.graphics.*;
import android.support.v4.view.GestureDetectorCompat;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import com.icechao.klinelib.R;
import com.icechao.klinelib.adapter.KLineChartAdapter;
import com.icechao.klinelib.draw.MainDraw;
import com.icechao.klinelib.draw.VolumeDraw;
import com.icechao.klinelib.entity.ICandle;
import com.icechao.klinelib.formatter.DateFormatter;
import com.icechao.klinelib.formatter.TimeFormatter;
import com.icechao.klinelib.formatter.ValueFormatter;
import com.icechao.klinelib.utils.*;

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
    public boolean isAnimationLast = true;

    /**
     * 是否正在显示loading
     */
    protected boolean isShowLoading;

    /**
     * 动画执行时长
     */
    private long duration = 500;

    /**
     * 价格框距离屏幕右侧的边距
     */
    protected float priceBoxMarginRight = 120;

    /**
     * 当前子视图的索引
     */
    protected ChildStatus childDrawPosition = ChildStatus.NONE;

    /**
     * 绘制K线时画板平移的距离
     */
    private float canvasTranslateX;

    protected int width = 0;

    /**
     * 整体上部的padding
     */
    protected int topPadding;
    /**
     * 子试图上方padding
     */
    protected int childPadding;
    /**
     * 整体底部padding
     */
    protected int bottomPadding;
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
    private float childScaleY = 1;

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
    protected float mainHighMaxValue = 0;
    /**
     * 主视图的K线的最小值
     */
    protected float mainLowMinValue = 0;
    /**
     * X轴最大值坐标索引
     */
    protected int mainMaxIndex = 0;
    /**
     * X轴最小值坐标索引
     */
    protected int mainMinIndex = 0;

    /**
     * 成交量最大值
     */
    protected float volMaxValue = Float.MAX_VALUE;
    /**
     * 成交量最小值
     */
    protected float childMaxValue = Float.MAX_VALUE;
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
    protected float chartItemWidth = 6;

    /**
     * K线网格行数
     */
    protected int gridRows = 5;

    /**
     * K线网格列数
     */
    protected int gridColumns = 5;

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
    protected Paint backgroundFillPaint = new Paint(Paint.ANTI_ALIAS_FLAG);

    /**
     * 网络画笔
     */
    protected Paint gridPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    /**
     * 十字线画笔
     */
    protected Paint selectedCrossPaint = new Paint(Paint.ANTI_ALIAS_FLAG);

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
     * 十字线相交点画笔
     */
    protected Paint selectedPointPaint = new Paint(Paint.ANTI_ALIAS_FLAG);

    /**
     * 十字线边框画笔
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
    protected Paint priceLineBoxRightPaint = new Paint(Paint.ANTI_ALIAS_FLAG);


    /**
     * 价格框高度
     */
    protected int priceLineBoxHeight = 40;

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
    protected IChartDraw<ICandle> childDraw;

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
    protected boolean isLine;

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
     * 同意文字基础线
     */
    protected float baseLine;

    /**
     * 同意文字Decent
     */
    protected float textDecent;

    /**
     * y轴label向上偏移的距离
     */
    protected int mainYMoveUpInterval = 5;

    /**
     * Y轴label与右边缘距离
     */
    protected int yLabelMarginRight = 10;

    /**
     * 分时线阴影的半径
     */
    protected float endShadowLayerWidth = 20;

    /**
     * 十字线相交点圆半径
     */
    protected float selectedPointRadius = 5;

    /**
     * 滑动监听
     */
    protected SlidListener slidListener;

    private long time;


    /**
     * 数据观察者,当数据变化
     */
    protected DataSetObserver dataSetObserver = new DataSetObserver() {
        @Override
        public void onChanged() {
            //从没数据变成有数据时,显示动画加载效果
            if (itemsCount == 0) {
                startAnimation();
            }
            int dataCount = dataAdapter.getCount();
            points = dataAdapter.getPoints();
            int temp = (dataAdapter.getCount() - 1) * indexInterval;
            if (dataCount == 0) {
                setItemCount(0);
                notifyChanged();
            } else if (dataCount > itemsCount) {
                lastPrice = points[temp + Constants.INDEX_CLOSE];
                lastVol = points[temp + Constants.INDEX_VOL];
                if (screenRightIndex == itemsCount - 2) {
                    setTranslatedX(canvasTranslateX - chartItemWidth * getScaleX());
                }
                setItemCount(dataCount);
            } else if (itemsCount == dataCount) {
                laseChange();
            }
            notifyChanged();
        }

        @Override
        public void onInvalidated() {
            isAnimationLast = false;
            setItemCount(0);
            points = null;
            postDelayed(action, 500);
        }
    };

    private boolean resetTranslate = true;

    public void setResetTranslate(boolean resetTranslate) {
        this.resetTranslate = resetTranslate;
    }


    /**
     * 当重置数据时,延时1s显示最后的加载动画
     */
    protected Runnable action = () -> isAnimationLast = true;


    private void laseChange() {
        int tempIndex = (itemsCount - 1) * indexInterval;
        generaterAnimator(lastVol, points[tempIndex + Constants.INDEX_VOL], animation -> lastVol = (Float) animation.getAnimatedValue());
        generaterAnimator(lastPrice, points[tempIndex + Constants.INDEX_CLOSE], animation -> {
            lastPrice = (Float) animation.getAnimatedValue();
            if (isLine) {
                return;
            }
            animInvalidate();
        });
        if (isAnimationLast) {
            float[] tempData = Arrays.copyOfRange(points, tempIndex, points.length);
            mainDraw.startAnim(BaseKLineChartView.this, tempData);
            volDraw.startAnim(BaseKLineChartView.this, tempData);
        }
    }

    /**
     * 当前数据个数
     */
    protected int itemsCount;
    protected List<IChartDraw> mChildDraws = new ArrayList<>();

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
    private ValueAnimator showAnim;

    protected float overScrollRange = 0;

    protected OnSelectedChangedListener mOnSelectedChangedListener = null;

    /**
     * 主视图
     */
    private Rect mainRect;

    /**
     * 量视图
     */
    private Rect volRect;

    /**
     * 子视图
     */
    private Rect childRect;

    /**
     * 分时线尾部点半径
     */
    protected float lineEndPointWidth;

    /**
     * 最新数据变化的执行动画
     */
    private ValueAnimator valueAnimator;

    /**
     * 分时线填充渐变的上部颜色
     */
    protected int areaTopColor;

    /**
     * 分时线填充渐变的下部颜色
     */
    protected int areaBottomColor;

    /**
     * 十字线Y轴的宽度
     */
    protected float selectedWidth;

    /**
     * 十字线Y轴的颜色
     */
    protected int selectedYColor;

    /**
     * 背景色渐变上部颜色
     */
    protected int backGroundTopColor;

    /**
     * 背景色渐变下部颜色
     */
    protected int backGroundBottomColor;

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
    protected float mainPresent = 6f;

    /**
     * 交易量图占比
     */
    protected float volPresent = 2f;

    /**
     * 子图占比
     */
    protected float childPresent = 2f;

    /**
     * 是否适配X label
     */
    protected boolean betterX = true;

    /**
     * 是否十字线跟随手指移动(Y轴)
     */
    protected boolean crossFollowTouch = false;


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
    private void init() {
        setWillNotDraw(false);
        indexInterval = Constants.getCount();
        gestureDetector = new GestureDetectorCompat(getContext(), this);
        scaleDetector = new ScaleGestureDetector(getContext(), this);
        topPadding = (int) getResources().getDimension(R.dimen.chart_top_padding);
        childPadding = (int) getResources().getDimension(R.dimen.child_top_padding);
        bottomPadding = (int) getResources().getDimension(R.dimen.chart_bottom_padding);

        showAnim = ValueAnimator.ofFloat(0f, 1f);
        showAnim.setDuration(duration);
        showAnim.addUpdateListener(animation -> invalidate());
        selectorFramePaint.setStrokeWidth(Dputil.Dp2Px(getContext(), 0.6f));
        selectorFramePaint.setStyle(Paint.Style.STROKE);
        selectorFramePaint.setColor(Color.WHITE);
        priceLinePaint.setAntiAlias(true);
        priceLineBoxRightPaint.setStyle(Paint.Style.STROKE);
        priceLineBoxPaint.setColor(Color.WHITE);
        priceLineBoxBgPaint.setColor(Color.BLACK);
        backgroundFillPaint.setStyle(Paint.Style.FILL_AND_STROKE);
        priceLineBoxPaint.setStyle(Paint.Style.STROKE);
        priceLineBoxPaint.setStrokeWidth(1);

    }


    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        this.width = w;
        displayHeight = h - topPadding - bottomPadding;
        initRect();
        rowSpace = displayHeight / gridRows;
        columnSpace = (float) width / gridColumns;
    }

    /**
     * 初始化视图区域
     * 主视图
     * 成交量视图
     * 子视图
     */
    protected void initRect() {
        if (null != childDraw) {
            int tempMainHeight = (int) (displayHeight * mainPresent / 10f);
            int tempVolHeight = (int) (displayHeight * volPresent / 10f);
            int tempChildHeight = (int) (displayHeight * childPresent / 10f);
            mainRect = new Rect(0, topPadding, width, topPadding + tempMainHeight);
            volRect = new Rect(0, mainRect.bottom + childPadding, width, mainRect.bottom + tempVolHeight);
            childRect = new Rect(0, volRect.bottom + childPadding, width, volRect.bottom + tempChildHeight);
        } else {
            int mMainHeight = (int) (displayHeight * 0.8f);
            int mVolHeight = (int) (displayHeight * 0.2f);
            mainRect = new Rect(0, topPadding, width, topPadding + mMainHeight);
            volRect = new Rect(0, mainRect.bottom + childPadding, width, mainRect.bottom + mVolHeight);
        }
    }

    @Override
    public void onDraw(Canvas canvas) {
        drawBackground(canvas);
        drawGirdLines(canvas);
        if (!isShowLoading && width != 0 && 0 != itemsCount && null != points && points.length != 0) {
            try {
                initValues();
                drawK(canvas);
                drawYLabels(canvas);
                drawXLabels(canvas);
//                drawSelected(canvas);
                drawPriceLine(canvas);
                drawValue(canvas, isLongPress ? selectedIndex : screenRightIndex);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 绘制整体背景
     *
     * @param canvas canvas
     */
    private void drawBackground(Canvas canvas) {
        int mid = width >> 1;
        backgroundPaint.setAlpha(18);
        backgroundPaint.setShader(new LinearGradient(mid, 0, mid, mainRect.bottom, backGroundTopColor, backGroundBottomColor, Shader.TileMode.CLAMP));
        canvas.drawRect(0, 0, width, mainRect.bottom, backgroundPaint);
        backgroundPaint.setShader(new LinearGradient(mid, volRect.top - topPadding, mid, volRect.bottom, backGroundTopColor, backGroundBottomColor, Shader.TileMode.CLAMP));
        canvas.drawRect(0, mainRect.bottom, width, volRect.bottom, backgroundPaint);
        if (null != childDraw) {
            backgroundPaint.setShader(new LinearGradient(mid, childRect.top - topPadding, mid, childRect.bottom, backGroundTopColor, backGroundBottomColor, Shader.TileMode.CLAMP));
            canvas.drawRect(0, childRect.top, width, childRect.bottom, backgroundPaint);
        }
    }

    /**
     * 重置所有数据
     * <p>
     * 可能会出现没效果,动画执行过程中,值改变
     */
    protected void resetValues() {
        lastPrice = 0;
        lastVol = 0;
        lastPrice = 0;
        selectedIndex = -1;
        itemsCount = 0;
        screenLeftIndex = 0;
        screenRightIndex = 0;
    }


    /**
     * 设置当前K线总数据个数
     *
     * @param itemCount items count
     */
    public void setItemCount(int itemCount) {
        //数据个数为0时重置本地保存数据,重置平移
        if (itemCount == 0) {
            resetValues();
        }
        this.itemsCount = itemCount;
        mainDraw.setItemCount(itemsCount);
        mainDraw.resetValues();
        volDraw.setItemCount(itemsCount);
        volDraw.resetValues();
        int size = mChildDraws.size();
        for (int i = 0; i < size; i++) {
            IChartDraw iChartDraw = mChildDraws.get(i);
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

    private float borderCheck(double v, int bottom) {
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
        float v = childMaxValue - value;
        if (v < 0) {
            return childRect.top;
        }
        return borderCheck(v * childScaleY + childRect.top, childRect.bottom);
    }

    /**
     * 修复text位置
     */
    protected float fixTextYBaseBottom(float y) {
        return y + (textHeight) / 2 - textDecent;
    }

    /**
     * 画网格
     *
     * @param canvas canvas
     */
    private void drawGirdLines(Canvas canvas) {

        for (int i = 0; i <= gridRows; i++) {
            float y = rowSpace * i + topPadding;
            canvas.drawLine(0, y, width, y, gridPaint);
        }
        canvas.drawLine(0, volRect.bottom, width, volRect.bottom, gridPaint);
        for (int i = 1; i < gridColumns; i++) {
            float stopX = columnSpace * i;
            canvas.drawLine(stopX, 0, stopX, mainRect.bottom, gridPaint);
            canvas.drawLine(stopX, mainRect.bottom, stopX, volRect.bottom, gridPaint);
            if (null != childDraw) {
                canvas.drawLine(stopX, volRect.bottom, stopX, childRect.bottom, gridPaint);
            }
        }
    }


    /**
     * 绘制k线图
     *
     * @param canvas canvas
     */
    private void drawK(Canvas canvas) {
        canvas.save();
        canvas.translate(canvasTranslateX, 0);
        for (int i = screenLeftIndex; i <= screenRightIndex && i >= 0; i++) {
            float curX = getX(i);
            int nextTemp = indexInterval * i + indexInterval;
            if (i == 0) {

                mainDraw.drawTranslated(canvas, curX, curX, this, i,
                        Arrays.copyOfRange(points, 0, indexInterval)
                );
                volDraw.drawTranslated(canvas, curX, curX, this, i,
                        Arrays.copyOfRange(points, 0, indexInterval));
                if (null != childDraw) {
                    childDraw.drawTranslated(canvas, curX, curX, this, i,
                            Arrays.copyOfRange(points, 0, indexInterval));
                }
            } else {
                float lastX = getX(i - 1);
                int lastTemp = indexInterval * i - indexInterval;
                mainDraw.drawTranslated(canvas, lastX, curX, this, i,
                        Arrays.copyOfRange(points, lastTemp, nextTemp)
                );
                volDraw.drawTranslated(canvas, lastX, curX, this, i,
                        Arrays.copyOfRange(points, lastTemp, nextTemp));
                if (null != childDraw) {
                    childDraw.drawTranslated(canvas, lastX, curX, this, i,
                            Arrays.copyOfRange(points, lastTemp, nextTemp));
                }
            }

        }
        mainDraw.drawMaxMinValue(canvas, this, getX(mainMaxIndex), mainHighMaxValue, getX(mainMinIndex), mainLowMinValue);
        canvas.restore();
    }

    public void drawSelected(Canvas canvas, float x) {
        float textHorizentalPadding = Dputil.Dp2Px(getContext(), 5);
        float textVerticalPadding = Dputil.Dp2Px(getContext(), 3);
        float y, textWidth;
        String text;
        //十字线竖线
        float halfWidth = selectedWidth / 2 * scaleX;
        float left = x - halfWidth;
        float right = x + halfWidth;
        float bottom = displayHeight + topPadding;
        Path path = new Path();
        path.moveTo(left, topPadding);
        path.lineTo(right, topPadding);
        path.lineTo(right, bottom);
        path.lineTo(left, bottom);
        path.close();
        LinearGradient linearGradient = new LinearGradient(x, topPadding, x, bottom,
                new int[]{Color.TRANSPARENT, selectedYColor, selectedYColor, Color.TRANSPARENT},
                new float[]{0f, 0.2f, 0.8f, 1f}, Shader.TileMode.CLAMP);
        selectedYLinePaint.setShader(linearGradient);
        canvas.drawPath(path, selectedYLinePaint);

        //画X值
        String date = formatDateTime(dataAdapter.getDate(selectedIndex));
        textWidth = textPaint.measureText(date);
        float r = textHeight / 2;
        if (null != childDraw) {
            y = childRect.bottom;
        } else {
            y = volRect.bottom;
        }
        float halfTextWidth = textWidth / 2;
        float tempLeft = x - halfTextWidth;
        left = tempLeft - textHorizentalPadding;
        right = x + halfTextWidth + textHorizentalPadding;
        bottom = y + baseLine + r - 2;
        canvas.drawRect(left, y, right, bottom, selectedPointPaint);
        canvas.drawRect(left, y, right, bottom, selectorFramePaint);
        canvas.drawText(date, tempLeft, fixTextYBaseBottom((bottom + y) / 2), textPaint);
        //十字线Y值判断
        if (crossFollowTouch) {
            y = selectedY;
            if (selectedY < mainRect.top + topPadding) {
                return;
            } else if (selectedY < mainRect.bottom) {
                text = valueFormatter.format((float) (mainMinValue + (mainMaxValue - mainMinValue) / (mainRect.bottom - topPadding) * (mainRect.bottom - selectedY)));
            } else if (selectedY < volRect.top) {
                return;
            } else if (selectedY < volRect.bottom) {
                text = NumberTools.getTradeMarketAmount(volDraw.getValueFormatter().format((volMaxValue / volRect.height() * (volRect.bottom - selectedY))));
            } else if (null != childDraw && selectedY < childRect.bottom) {
                text = childDraw.getValueFormatter().format((childMaxValue / volRect.height() * (volRect.bottom - selectedY)));
            } else if (null != childDraw && selectedY < childRect.top) {
                return;
            } else {
                return;
            }
        } else {
            text = formatValue(points[selectedIndex * indexInterval + Constants.INDEX_CLOSE]);
            y = getMainY(points[selectedIndex * indexInterval + Constants.INDEX_CLOSE]);
        }

        //十字线横线
        canvas.drawLine(-canvasTranslateX, y, -canvasTranslateX + width, y, selectedXLinePaint);

        //十字线交点
        if (!crossFollowTouch) {
            canvas.drawCircle(x, y, chartItemWidth, selectedbigCrossPaint);
            canvas.drawCircle(x, y, selectedPointRadius, selectedCrossPaint);
        }

        // 选中状态下的Y值
        textWidth = textPaint.measureText(text);
        r = textHeight / 2 + textVerticalPadding;
        float tempX = textWidth + 2 * textHorizentalPadding;
        //左侧框
        float boxTop = y - r;
        float boXBottom = y + r;
        if (getX(selectedIndex - screenLeftIndex) > width / 2) {
            x = -canvasTranslateX;
            path = new Path();
            path.moveTo(x, boxTop);
            path.lineTo(x, boXBottom);
            path.lineTo(tempX + x, boXBottom);
            path.lineTo(tempX + x + textVerticalPadding * 2, y);
            path.lineTo(tempX + x, boxTop);
            path.close();
            canvas.drawPath(path, selectedPointPaint);
            canvas.drawPath(path, selectorFramePaint);
            canvas.drawText(text, x + textHorizentalPadding, fixTextYBaseBottom(y), textPaint);
        } else {//右侧框
            x = -canvasTranslateX + width - textWidth - 1 - 2 * textHorizentalPadding - textVerticalPadding;
            path = new Path();
            path.moveTo(x, y);
            path.lineTo(x + textVerticalPadding * 2, boXBottom);
            path.lineTo(-canvasTranslateX + width - 2, boXBottom);
            path.lineTo(-canvasTranslateX + width - 2, boxTop);
            path.lineTo(x + textVerticalPadding * 2, boxTop);
            path.close();
            canvas.drawPath(path, selectedPointPaint);
            canvas.drawPath(path, selectorFramePaint);
            canvas.drawText(text, x + textVerticalPadding + textHorizentalPadding, fixTextYBaseBottom(y), textPaint);
        }
    }


    /**
     * 绘制所有的Labels
     *
     * @param canvas canvas
     */
    private void drawXLabels(Canvas canvas) {

        float y;
        if (null != childDraw) {
            y = childRect.bottom + baseLine + 5;
        } else {
            y = volRect.bottom + baseLine + 5;
        }

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

    /**
     * 绘制Y轴上的所有label
     *
     * @param canvas canvase
     */
    private void drawYLabels(Canvas canvas) {
        int gridRowCount;
        double rowValue;//当显示子视图时,y轴label减少显示一个
        if (null != childDraw) {
            gridRowCount = gridRows - 2;
            rowValue = (mainMaxValue - mainMinValue) / gridRowCount;
        } else {
            gridRowCount = gridRows - 1;
            rowValue = (mainMaxValue - mainMinValue) / gridRowCount;
        }

        int tempYLabelX = width - yLabelMarginRight;
        //Y轴上网络的值
        for (int i = 0; i <= gridRowCount; i++) {
            String text = formatValue(mainMaxValue - i * rowValue);
            float v = rowSpace * i + topPadding;
            canvas.drawText(text, tempYLabelX -
                    textPaint.measureText(text), v - mainYMoveUpInterval, textPaint);

        }

        //交易量图的Y轴label
        String maxVol = NumberTools.getTradeMarketAmount(volDraw.getValueFormatter().
                format(volMaxValue));
        canvas.drawText(maxVol, tempYLabelX -
                textPaint.measureText(maxVol), mainRect.bottom + baseLine, textPaint);


        //子图Y轴label
        if (null != childDraw) {
            String childLable = childDraw.getValueFormatter().format(childMaxValue);
            canvas.drawText(childLable, tempYLabelX -
                    textPaint.measureText(childLable), volRect.bottom + baseLine, textPaint);

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
        canvas.drawCircle(stopX, getMainY(lastPrice), lineEndPointWidth * 4, lineEndPointPaint);

    }


    /**
     * 绘制横向的价格线
     *
     * @param canvas canvase
     */
    private void drawPriceLine(Canvas canvas) {
        float y = getMainY(lastPrice);
        String priceString = valueFormatter.format(lastPrice);
        //多加2个像素防止文字宽度有小的变化
        float textWidth = textPaint.measureText(priceString);
        float textLeft = width - textWidth;
        float endLineRight = getX(screenRightIndex) + canvasTranslateX;
        if (screenRightIndex == itemsCount - 1 && endLineRight < textLeft) {
            drawKlineRightSpace(canvas, y, priceString, textWidth, textLeft, endLineRight);
        } else {
            float halfPriceBoxHeight = priceLineBoxHeight >> 1;
            //修改价格信息框Y轴计算保证,只会绘制在主区域中
            if (lastPrice > mainMaxValue) {
                y = mainRect.top + halfPriceBoxHeight;
            } else if (lastPrice < mainMinValue) {
                y = mainRect.bottom - halfPriceBoxHeight;
            }

            fullScreenPriceLine(canvas, y, priceString, textWidth, halfPriceBoxHeight);
        }
    }

    /**
     * 绘制价格线
     *
     * @param canvas             canvas
     * @param y                  y
     * @param priceString        priceString
     * @param textWidth          textWidth
     * @param halfPriceBoxHeight halfPriceBoxHeight
     */
    private void fullScreenPriceLine(Canvas canvas, float y, String priceString, float textWidth, float halfPriceBoxHeight) {
        //绘制右侧虚线
        for (int i = 0; i < width; i += 12) {
            canvas.drawLine(i, y, i + 8, y, priceLinePaint);
        }
        float halfHeight = textHeight / 2;
        float boxRight = width - priceBoxMarginRight;
        float boxLeft = boxRight - textWidth - priceLineBoxHeight - halfHeight;
        float boxTop = y - halfPriceBoxHeight;
        float boxBottom = y + halfPriceBoxHeight;
        canvas.drawRoundRect(new RectF(boxLeft, boxTop, boxRight, boxBottom), halfPriceBoxHeight, halfPriceBoxHeight, priceLineBoxBgPaint);
        canvas.drawRoundRect(new RectF(boxLeft, boxTop, boxRight, boxBottom), halfPriceBoxHeight, halfPriceBoxHeight, priceLineBoxPaint);

        float top = y - halfHeight / 2;
        float bottom = y + halfHeight / 2;
        float shapeLeft = boxRight - halfPriceBoxHeight;
        Path shape = new Path();
        shape.moveTo(shapeLeft, top);
        shape.lineTo(shapeLeft, bottom);
        shape.lineTo(shapeLeft + halfHeight / 2, y);
        shape.close();
        canvas.drawPath(shape, textPaint);
        canvas.drawText(priceString, shapeLeft - textWidth - halfHeight, (y + (halfHeight - textDecent)), textPaint);
    }

    private void drawKlineRightSpace(Canvas canvas, float y, String priceString, float textWidth, float textLeft, float endLineRight) {
        //两个价格图层在点之间所以放在价格线中绘制
        if (isLine) {
            drawEndPoint(canvas, endLineRight);
        }
        for (float i = endLineRight; i < textLeft - 5; i += 12) {
            canvas.drawLine(i, y, i + 8, y, priceLineBoxRightPaint);
        }
        float textY = fixTextYBaseBottom(y);
        backgroundFillPaint.setAlpha(150);
        canvas.drawRect(new Rect((int) textLeft, (int) (y - textHeight / 2), (int) (textLeft + textWidth), (int) (y + textHeight / 2)), backgroundFillPaint);
        canvas.drawText(priceString, textLeft, textY, priceLineBoxRightPaint);
        //绘制价格圆点
        if (isLine) {
            canvas.drawCircle(endLineRight, y, lineEndPointWidth, lineEndFillPointPaint);
        }
    }

    protected float getDataLength() {
        float length = chartItemWidth * getScaleX() * (itemsCount - 1) + overScrollRange;
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
        valueAnimator = ValueAnimator.ofFloat(lineEndPointWidth, lineEndPointWidth * 4);
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
    private void drawValue(Canvas canvas, int position) {
        int temp = indexInterval * position;
        if (temp < points.length && position >= 0) {
            float[] tempValues = Arrays.copyOfRange(points, temp, temp + indexInterval);
            float x = getPaddingLeft();
            mainDraw.drawText(canvas, this, x, mainRect.top + baseLine - textHeight / 2, position, tempValues);
            volDraw.drawText(canvas, this, x, mainRect.bottom + baseLine, position, tempValues);
            if (null != childDraw) {
                childDraw.drawText(canvas, this, x, volRect.bottom + baseLine, position, tempValues);
            }
        }
    }

    /**
     * 格式化值
     */
    public String formatValue(double value) {
        if (null == valueFormatter) {
            valueFormatter = new ValueFormatter();
        }
        return valueFormatter.format((float) value);
    }

    /**
     * 重新计算并刷新线条
     */
    public void notifyChanged() {
        if (resetTranslate && width != 0) {
            setTranslatedX(getMinTranslate());
            resetTranslate = false;
        }
        invalidate();
    }


    /**
     * 当前主视图显示的指标
     */
    protected MainStatus status = MainStatus.MA;

    /**
     * 计算当前选中item的X的坐标
     *
     * @param x index of selected item
     */
    private void calculateSelectedX(float x) {
        selectedIndex = indexOfTranslateX(xToTranslateX(x));
        if (selectedIndex > screenRightIndex) {
            selectedIndex = screenRightIndex;
        }
        if (selectedIndex < screenLeftIndex) {
            selectedIndex = screenLeftIndex;
        }
    }

    /**
     * 计算当前选中item的Y的坐标
     *
     * @param y index of selected item
     */
    @SuppressWarnings("unused")
    private double calculateSelectedY(float y) {
        if (y < mainRect.top + topPadding) {
            return mainMaxValue;
        } else if (y < mainRect.bottom) {
            return mainRect.height() / (mainMaxValue - mainMinValue) * (mainRect.bottom - y);
        } else if (y < volRect.top) {
            return 0;
        } else if (y < volRect.bottom) {
            return 0;
        } else if (null != childDraw && y < childRect.top) {
            return 0;
        } else if (null != childDraw && y < childRect.bottom) {
            return 0;
        }
        return 0;
    }

    private float selectedY = 0;

    @Override
    public void onLongPress(MotionEvent e) {
        super.onLongPress(e);
        int lastIndex = selectedIndex;
        calculateSelectedX(e.getX());
        selectedY = e.getY();
        if (lastIndex != selectedIndex) {
            int temp = selectedIndex * indexInterval;
            if (null != this.mOnSelectedChangedListener) {
                mOnSelectedChangedListener.onSelectedChanged(this, selectedIndex, Arrays.copyOfRange(points, temp, temp + indexInterval));
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
        if (dataLength >= width) {
            return -(dataLength - width);
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
        if (dataLength >= width) {
            return isLine ? 0 : chartItemWidth * getScaleX() / 2;
        }
        return width - dataLength + overScrollRange - (isLine ? 0 : chartItemWidth * getScaleX() / 2);
    }

    @Override
    protected void onScrollChanged(int l, int t, int oldl, int oldt) {
        super.onScrollChanged(l, t, oldl, oldt);
        setTranslatedX(canvasTranslateX + (l - oldl));
        if (isLine && getX(screenRightIndex) + canvasTranslateX <= width) {
            startFreshPage();
        } else {
            stopFreshPage();
        }
        invalidate();
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
        if (screenLeftIndex != 0) {
            setTranslatedX(canvasTranslateX / oldScale * scale + difCount * tempWidth);
        } else {
            if (getDataLength() < width) {
                setTranslatedX(-(width - getDataLength()));
            } else {
                setTranslatedX(getMaxTranslate());
            }
        }
        invalidate();
    }


    /**
     * 设置当前平移
     *
     * @param mTranslateX canvasTranslateX
     */
    private void setTranslatedX(float mTranslateX) {
        if (mTranslateX < getMinTranslate()) {
            mTranslateX = getMinTranslate();
            if (null != slidListener && mTranslateX == getMinTranslate()) {
                slidListener.onSlidRight();
            }
        } else if (mTranslateX > getMaxTranslate()) {
            mTranslateX = getMaxTranslate();
            if (null != slidListener) {
                slidListener.onSlidLeft();
            }
        }
        this.canvasTranslateX = mTranslateX;
    }

    /**
     * 计算当前显示的数据以及显示的数据的最大最小值
     */
    private void initValues() {
        float scaleWidth = chartItemWidth * scaleX;

        if (canvasTranslateX <= scaleWidth / 2) {
            screenLeftIndex = (int) ((-canvasTranslateX) / scaleWidth);
            if (screenLeftIndex < 0) {
                screenLeftIndex = 0;
            }
            screenRightIndex = (int) (screenLeftIndex + width / scaleWidth + 0.5) + 1;

        } else {
            screenLeftIndex = 0;
            screenRightIndex = itemsCount - 1;
        }
        if (screenRightIndex > itemsCount - 1) {
            screenRightIndex = itemsCount - 1;
        }
        mainMaxValue = Float.MIN_VALUE;
        mainMinValue = Float.MAX_VALUE;
        volMaxValue = Float.MIN_VALUE;
        float volMinValue = Float.MAX_VALUE;
        childMaxValue = Float.MIN_VALUE;
        float mChildMinValue = Float.MAX_VALUE;
        mainMaxIndex = screenLeftIndex;
        mainMinIndex = screenLeftIndex;
        mainHighMaxValue = Float.MIN_VALUE;
        mainLowMinValue = Float.MAX_VALUE;
        int tempLeft = screenLeftIndex > 0 ? screenLeftIndex + 1 : 0;
        for (int i = tempLeft; i <= screenRightIndex; i++) {
            int tempIndex = indexInterval * i;
            mainMaxValue = (status == MainStatus.MA || status == MainStatus.NONE) ?
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
            mainMinValue = (status == MainStatus.MA || status == MainStatus.NONE) ?
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
            float max = Math.max(points[tempIndex + Constants.INDEX_LOW], points[tempIndex + Constants.INDEX_HIGH]);
            float min = Math.min(points[tempIndex + Constants.INDEX_LOW], points[tempIndex + Constants.INDEX_HIGH]);


            if (mainHighMaxValue < max) {
                mainHighMaxValue = max;
                mainMaxIndex = i;
            }
            if (mainLowMinValue >= min) {
                mainLowMinValue = min;
                mainMinIndex = i;
            }
            float volume = points[tempIndex + Constants.INDEX_VOL];
//
            volMaxValue = volDraw.getMaxValue(
                    volMaxValue,
                    points[tempIndex + Constants.INDEX_VOL],
                    points[tempIndex + Constants.INDEX_VOL_MA_1],
                    points[tempIndex + Constants.INDEX_VOL_MA_2]);
            //当最新的K
            if (screenRightIndex != itemsCount - 1) {
                volMinValue = volDraw.getMinValue(volMinValue,
                        points[tempIndex + Constants.INDEX_VOL],
                        points[tempIndex + Constants.INDEX_VOL_MA_1],
                        points[tempIndex + Constants.INDEX_VOL_MA_2]);
            }
            if (volume < volMinValue) {
                volMinValue = volume;
            }

            if (null != childDraw) {
                childMaxValue = Math.max(childMaxValue, childDraw.getMaxValue(Arrays.copyOfRange(points, tempIndex, tempIndex + indexInterval)));
                mChildMinValue = Math.min(mChildMinValue, childDraw.getMinValue(Arrays.copyOfRange(points, tempIndex, tempIndex + indexInterval)));
            }
        }
        if (mainMaxValue == mainMinValue) {
            //当最大值和最小值都相等的时候 分别增大最大值和 减小最小值
            mainMaxValue += Math.abs(mainMaxValue * 0.05f);
            mainMinValue -= Math.abs(mainMinValue * 0.05f);
        }
        double padding = (mainMaxValue - mainMinValue) * 0.05f;
        mainMaxValue += padding;
        mainMinValue -= padding;
        if (volMaxValue < 0.001) {
            volMaxValue = 0.1f;
        }
        if (null != childDraw) {
            childMaxValue += Math.abs(childMaxValue * 0.03f);
            mChildMinValue -= Math.abs(mChildMinValue * 0.03f);
            if (childMaxValue == 0) {
                childMaxValue = 1f;
            }
        }
        mainScaleY = mainRect.height() * 1f / (mainMaxValue - mainMinValue);
        volScaleY = volRect.height() * 1f / (volMaxValue - volMinValue);
        if (null != childRect)
            childScaleY = childRect.height() * 1f / (childMaxValue - mChildMinValue);
        if (showAnim.isRunning()) {
            float value = (float) showAnim.getAnimatedValue();
            this.screenRightIndex = screenLeftIndex + Math.round(value * (this.screenRightIndex - screenLeftIndex));
        }
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
     * 绘制分时线尾部
     *
     * @param canvas     canvase
     * @param paint      paint
     * @param startX     startx
     * @param startValue start value
     * @param stopX      stopx
     */
    public void drawEndLine(Canvas canvas, Paint paint, float startX, float startValue, float stopX) {
        canvas.drawLine(startX, getMainY(startValue), stopX, getMainY(lastPrice), paint);
    }


    /**
     * 绘制分时线尾部填充色
     *
     * @param canvas     canvase
     * @param paint      paint
     * @param startX     start x
     * @param startValue start value
     * @param stopX      stopx
     */
    public void drawEndFill(Canvas canvas, Paint paint, float startX, float startValue, float stopX) {
        float y = displayHeight + topPadding + bottomPadding;
        LinearGradient linearGradient = new LinearGradient(startX, topPadding,
                stopX, y, areaTopColor, areaBottomColor, Shader.TileMode.CLAMP);
        paint.setShader(linearGradient);
        Path path = new Path();
        path.moveTo(startX, y);
        path.lineTo(startX, getMainY(startValue));
        path.lineTo(stopX, getMainY(lastPrice));
        path.lineTo(stopX, y);
        path.close();
        canvas.drawPath(path, paint);


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

        float y = displayHeight + topPadding + bottomPadding;
        LinearGradient linearGradient = new LinearGradient(startX, topPadding,
                stopX, y, areaTopColor, areaBottomColor, Shader.TileMode.CLAMP);
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
    public void addChildDraw(IChartDraw childDraw) {
        mChildDraws.add(childDraw);
    }

    /**
     * 格式化时间,
     *
     * @param date date
     */
    @SuppressWarnings("unused")
    public String formatDateTime(Date date) {
        if (null == dateTimeFormatter) {
            dateTimeFormatter = new TimeFormatter();
        }
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
     * 设置动画时间
     */
    @SuppressWarnings("unused")
    public void setAnimationDuration(long duration) {
        if (null != showAnim) {
            showAnim.setDuration(duration);
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
     * translateX转化为view中的x
     *
     * @param translateX translateX
     * @return x
     */
    public float translateXtoX(float translateX) {
        return translateX + canvasTranslateX;
    }

    /**
     * 是否长按
     */
    public boolean isLongPress() {
        return isLongPress;
    }

    /**
     * 获取选择索引
     */
    public int getSelectedIndex() {
        return selectedIndex;
    }


    public Rect getVolRect() {
        return volRect;
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
        return getX(screenLeftIndex) + (width >> 1);
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
    public ValueAnimator generaterAnimator(Float start, float end, ValueAnimator.AnimatorUpdateListener listener) {
        ValueAnimator animator = ValueAnimator.ofFloat(0 == start ? end - 0.01f : start, end);
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
        return animator;
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
     * 当时显示是否是分时线
     *
     * @return isLine
     */
    public boolean isLine() {
        return isLine;
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
    public float getTopPadding() {
        return topPadding;
    }

    /**
     * 获取图的宽度
     *
     * @return 宽度
     */
    public int getChartWidth() {
        return width;
    }

    /**
     * 获取当前主图状态
     *
     * @return {@link MainStatus}
     */
    public MainStatus getStatus() {
        return this.status;
    }


    public void setSlidListener(SlidListener slidListener) {
        this.slidListener = slidListener;
    }
}
