package com.icechao.klinelib.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.annotation.ColorRes;
import android.support.annotation.DimenRes;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import com.icechao.klinelib.R;
import com.icechao.klinelib.adapter.KLineChartAdapter;
import com.icechao.klinelib.base.BaseKLineChartView;
import com.icechao.klinelib.base.IDateTimeFormatter;
import com.icechao.klinelib.base.IValueFormatter;
import com.icechao.klinelib.draw.*;
import com.icechao.klinelib.utils.*;

/*************************************************************************
 * Description   :
 *
 * @PackageName  : com.icechao.klinelib.utils
 * @FileName     : KLineChartView.java
 * @Author       : chao
 * @Date         : 2019/4/8
 * @Email        : icechliu@gmail.com
 * @version      : V1
 *************************************************************************/
public class KLineChartView extends BaseKLineChartView {

    private View progressBar;

    private MACDDraw macdDraw;
    private RSIDraw rsiDraw;
    private KDJDraw kdjDraw;
    private WRDraw wrDraw;


    public KLineChartView(Context context) {
        this(context, null);
        initView(context);
    }

    public KLineChartView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public KLineChartView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context);
        initAttrs(attrs, context);
    }

    private void initView(Context context) {

        mainDraw = new MainDraw(context);
        macdDraw = new MACDDraw(context);
        volDraw = new VolumeDraw(context);
        wrDraw = new WRDraw(context);
        kdjDraw = new KDJDraw(context);
        rsiDraw = new RSIDraw(context);
        addIndexDraw(macdDraw);
        addIndexDraw(kdjDraw);
        addIndexDraw(rsiDraw);
        addIndexDraw(wrDraw);

    }

    private void initAttrs(AttributeSet attrs, Context context) {
        TypedArray array = getContext().obtainStyledAttributes(attrs, R.styleable.KLineChartView);
        if (null != array) {
            try {

                setLogoResouce(array.getResourceId(R.styleable.KLineChartView_chartLogo, 0));
                //最大最小值
                mainDraw.setLimitTextSize(array.getDimension(R.styleable.KLineChartView_limitTextSize, getDimension(R.dimen.chart_text_size)));
                mainDraw.setLimitTextColor(array.getColor(R.styleable.KLineChartView_limitTextColor, getColor(R.color.color_6D87A8)));
                //全局
                setBetterX(array.getBoolean(R.styleable.KLineChartView_fitXLabel, true));
                setyLabelMarginRight(array.getDimension(R.styleable.KLineChartView_yLabelMarginRight, 10));
                setLineWidth(array.getDimension(R.styleable.KLineChartView_lineWidth, getDimension(R.dimen.chart_line_width)));
                setTextSize(array.getDimension(R.styleable.KLineChartView_textSize, getDimension(R.dimen.chart_text_size)));
                setTextColor(array.getColor(R.styleable.KLineChartView_textColor, getColor(R.color.color_6D87A8)));
                setChartPaddingTop(array.getDimension(R.styleable.KLineChartView_paddingTop, getDimension(R.dimen.chart_top_padding)));
                setChildPaddingTop(array.getDimension(R.styleable.KLineChartView_childPaddingTop, getDimension(R.dimen.child_top_padding)));
                setChartPaddingBottom(array.getDimension(R.styleable.KLineChartView_paddingBottom, getDimension(R.dimen.chart_bottom_padding)));
                //网格
                setGridLineWidth(array.getDimension(R.styleable.KLineChartView_gridLineWidth, getDimension(R.dimen.chart_grid_line_width)));
                setGridLineColor(array.getColor(R.styleable.KLineChartView_gridLineColor, getColor(R.color.color_223349)));
                //图例
                setVolLengendColor(array.getColor(R.styleable.KLineChartView_volLengendColor, getResources().getColor(R.color.color_6D87A8)));
                setMainLengendMarginTop(array.getDimension(R.styleable.KLineChartView_mainLengendMarginTop, 10f));
                setVolLengendMarginTop(array.getDimension(R.styleable.KLineChartView_volLengendMarginTop, 10f));

                //价格线
                setPriceLineWidth(array.getDimension(R.styleable.KLineChartView_priceLineWidth, Dputil.Dp2Px(context, 1)));
                setPriceLineColor(array.getColor(R.styleable.KLineChartView_priceLineColor, getResources().getColor(R.color.color_6D87A8)));
                setPriceLineRightColor(array.getColor(R.styleable.KLineChartView_priceLineRightColor, getResources().getColor(R.color.color_4B85D6)));
                setPriceLineRightTextColor(array.getColor(R.styleable.KLineChartView_priceLineRightTextColor, getResources().getColor(R.color.color_4B85D6)));
                setPriceBoxColor(array.getColor(R.styleable.KLineChartView_priceLineBoxColor, getContext().getResources().getColor(R.color.color_131F30)));
                setPriceLineBoxBgColor(array.getColor(R.styleable.KLineChartView_priceLineBackgroundColor, getContext().getResources().getColor(R.color.color_CFD3E9)));
                setPricelineBoxBorderColor(array.getColor(R.styleable.KLineChartView_priceLineBoxBorderColor, getContext().getResources().getColor(R.color.color_CFD3E9)));
                setPricelineBoxBorderWidth(array.getDimension(R.styleable.KLineChartView_priceLineBoxBorderWidth, 1));
                setPriceLineBoxMarginRight(array.getDimension(R.styleable.KLineChartView_priceLineBoxMarginRight, 120));
                setPriceLineBoxePadding(array.getDimension(R.styleable.KLineChartView_priceLineBoxPadding, 20));
                setPriceLineShapeWidth(array.getDimension(R.styleable.KLineChartView_priceLineBoxShapeWidth, 10));
                setPriceLineShapeHeight(array.getDimension(R.styleable.KLineChartView_priceLineBoxShapeHeight, 20));
                setPriceLineBoxHeight(array.getDimension(R.styleable.KLineChartView_priceLineBoxHeight, 40));
                setPriceBoxShapeTextMargin(array.getDimension(R.styleable.KLineChartView_priceLineBoxShapeTextMargin, 10));
                //十字线
                setSelectCrossBigColor(array.getColor(R.styleable.KLineChartView_selectCrossBigColor, getResources().getColor(R.color.color_9ACFD3E9)));
                setSelectedPointColor(array.getColor(R.styleable.KLineChartView_selectCrossPointRadiu, Color.WHITE));
                setSelectedPointRadius(array.getColor(R.styleable.KLineChartView_selectCrossPointColor, 5));
                setSelectedShowCrossPoint(array.getBoolean(R.styleable.KLineChartView_selectShowCrossPoint, true));
                setSelectedXLineWidth(array.getDimension(R.styleable.KLineChartView_selectXLineWidth, getDimension(R.dimen.chart_line_width)));
                setSelectedLabelBorderWidth(array.getDimension(R.styleable.KLineChartView_selectLabelBoderWidth, 2));
                setSelectedLabelBorderColor(array.getColor(R.styleable.KLineChartView_selectLabelBoderColor, Color.WHITE));
                setSelectedYLineWidth(array.getDimension(R.styleable.KLineChartView_selectYLineWidth, getDimension(R.dimen.chart_point_width)));
                setSelectedXLineColor(array.getColor(R.styleable.KLineChartView_selectXLineColor, getResources().getColor(R.color.color_CFD3E9)));
                setSelectedYLineColor(array.getColor(R.styleable.KLineChartView_selectYLineColor, getResources().getColor(R.color.color_1ACFD3E9)));
                setSelectedYColor(array.getColor(R.styleable.KLineChartView_selectYColor, getResources().getColor(R.color.color_CFD3E9)));
                setSelectPriceBoxBackgroundColor(array.getColor(R.styleable.KLineChartView_selectPriceBoxBackgroundColor, getColor(R.color.color_081724)));
                setSelectorBackgroundColor(array.getColor(R.styleable.KLineChartView_selectInfoBoxBackgroundColor, getColor(R.color.color_EA111725)));
                setSelectorTextSize(array.getDimension(R.styleable.KLineChartView_selectTextSize, getDimension(R.dimen.chart_selector_text_size)));
                setSelectPriceBoxHorizentalPadding(array.getDimension(R.styleable.KLineChartView_selectPriceBoxHorizentalPadding, getDimension(R.dimen.price_box_horizental)));
                setSelectPriceboxVerticalPadding(array.getDimension(R.styleable.KLineChartView_selectPriceBoxVerticalPadding, getDimension(R.dimen.price_box_vertical)));
                setSelectInfoBoxMargin(array.getDimension(R.styleable.KLineChartView_selectInfoBoxMargin, getDimension(R.dimen.price_box_horizental)));
                setSelectedInfoBoxColors(
                        array.getColor(R.styleable.KLineChartView_selectInfoBoxTextColor, Color.WHITE),
                        array.getColor(R.styleable.KLineChartView_selectInfoBoxBorderColor, Color.WHITE),
                        array.getColor(R.styleable.KLineChartView_selectInfoBoxBackgroundColor, Color.DKGRAY)
                );
                setSelectInfoBoxPadding(array.getDimension(R.styleable.KLineChartView_selectInfoBoxPadding, getDimension(R.dimen.price_box_horizental)));

                //K线
                setIncreaseColor(array.getColor(R.styleable.KLineChartView_increaseColor, getResources().getColor(R.color.color_03C087)));
                setDecreaseColor(array.getColor(R.styleable.KLineChartView_decreaseColor, getResources().getColor(R.color.color_FF605A)));
                setChartItemWidth(array.getDimension(R.styleable.KLineChartView_itemWidth, getDimension(R.dimen.chart_point_width)));
                setCandleWidth(array.getDimension(R.styleable.KLineChartView_candleWidth, getDimension(R.dimen.chart_candle_width)));
                setCandleLineWidth(array.getDimension(R.styleable.KLineChartView_candleLineWidth, getDimension(R.dimen.chart_candle_line_width)));
                setKlineRightPadding(array.getDimension(R.styleable.KLineChartView_candleRightPadding, 0));

                //背景添加渐变色
                setBgColor(array.getColor(R.styleable.KLineChartView_backgroundColor, getResources().getColor(R.color.color_1C1E27)));
                setBackGroundFillTopColor(array.getColor(R.styleable.KLineChartView_backgroundFillTopLolor, getResources().getColor(R.color.color_1C1E27)));
                setBackGroundFillBottomColor(array.getColor(R.styleable.KLineChartView_backgroundFillBottomColor, getResources().getColor(R.color.color_4B85D6)));
                setBackGroundAlpha(array.getInt(R.styleable.KLineChartView_backgroundAlpha, 18));
                setBackGroudFillAlpha(array.getInt(R.styleable.KLineChartView_backgroundFillAlpha, 150));

                // time line
                setTimeLineColor(array.getColor(R.styleable.KLineChartView_timeLineColor, getResources().getColor(R.color.color_4B85D6)));
                setTimeLineFillTopColor(array.getColor(R.styleable.KLineChartView_timeLineFillTopColor, getResources().getColor(R.color.color_404B85D6)));
                setTimeLineFillBottomColor(array.getColor(R.styleable.KLineChartView_timeLineFillBottomColor, getResources().getColor(R.color.color_004B85D6)));
                setTimeLineEndColor(array.getColor(R.styleable.KLineChartView_timeLineEndPointColor, Color.WHITE));
                setTimeLineEndRadiu(array.getDimension(R.styleable.KLineChartView_timeLineEndRadiu, Dputil.Dp2Px(context, 4)));
                setTimeLineEndMultiply(array.getFloat(R.styleable.KLineChartView_timeLineEndMultiply, 3f));


                //macd
                setMacdChartColor(array.getColor(R.styleable.KLineChartView_macdIncreaseColor, getResources().getColor(R.color.color_03C087)),
                        array.getColor(R.styleable.KLineChartView_macdDecreaseColor, getResources().getColor(R.color.color_FF605A)));
                setMACDWidth(array.getDimension(R.styleable.KLineChartView_macdWidth, getDimension(R.dimen.chart_candle_width)));
                setDIFColor(array.getColor(R.styleable.KLineChartView_difColor, getColor(R.color.color_F6DC93)));
                setDEAColor(array.getColor(R.styleable.KLineChartView_deaColor, getColor(R.color.color_61D1C0)));
                setMACDColor(array.getColor(R.styleable.KLineChartView_macdColor, getColor(R.color.color_CB92FE)));
                //kdj
                setKColor(array.getColor(R.styleable.KLineChartView_difColor, getColor(R.color.color_F6DC93)));
                setDColor(array.getColor(R.styleable.KLineChartView_deaColor, getColor(R.color.color_61D1C0)));
                setJColor(array.getColor(R.styleable.KLineChartView_macdColor, getColor(R.color.color_CB92FE)));
                //wr
                setR1Color(array.getColor(R.styleable.KLineChartView_wr1Color, getColor(R.color.color_F6DC93)));
                setR2Color(array.getColor(R.styleable.KLineChartView_wr2Color, getColor(R.color.color_61D1C0)));
                setR3Color(array.getColor(R.styleable.KLineChartView_wr3Color, getColor(R.color.color_CB92FE)));
                //rsi
                setRSI1Color(array.getColor(R.styleable.KLineChartView_difColor, getColor(R.color.color_F6DC93)));
                setRSI2Color(array.getColor(R.styleable.KLineChartView_deaColor, getColor(R.color.color_61D1C0)));
                setRSI3Color(array.getColor(R.styleable.KLineChartView_macdColor, getColor(R.color.color_CB92FE)));
                //main
                setMa1Color(array.getColor(R.styleable.KLineChartView_difColor, getColor(R.color.color_F6DC93)));
                setMa2Color(array.getColor(R.styleable.KLineChartView_deaColor, getColor(R.color.color_61D1C0)));
                setMa3Color(array.getColor(R.styleable.KLineChartView_macdColor, getColor(R.color.color_CB92FE)));
                setCandleSolid(array.getBoolean(R.styleable.KLineChartView_candleSolid, false));


            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                array.recycle();
            }
        }
    }

    /**
     * 设置选中时是否显示十字线的交点圆
     *
     * @param showCrossPoint default true
     */
    private KLineChartView setSelectedShowCrossPoint(boolean showCrossPoint) {
        this.showCrossPoint = showCrossPoint;
        return this;
    }

    /**
     * 选中时坐标边框线宽
     *
     * @param width width
     */
    public KLineChartView setSelectedLabelBorderWidth(float width) {
        selectorFramePaint.setStrokeWidth(width);
        return this;
    }

    /**
     * 子视图的padding
     *
     * @param childViewPaddingTop padding
     */
    public KLineChartView setChildPaddingTop(float childViewPaddingTop) {
        this.childViewPaddingTop = childViewPaddingTop;
        return this;
    }

    /**
     * 选中时坐标边框线颜色
     *
     * @param color color
     */
    public KLineChartView setSelectedLabelBorderColor(int color) {
        selectorFramePaint.setColor(color);
        return this;
    }


    /**
     * 价格线框边框宽度
     *
     * @param width width
     */
    public KLineChartView setPricelineBoxBorderWidth(float width) {
        priceLineBoxPaint.setStrokeWidth(width);
        return this;
    }

    /**
     * 价格线框边框颜色
     *
     * @param color color
     */
    public KLineChartView setPricelineBoxBorderColor(int color) {
        priceLineBoxPaint.setColor(color);
        return this;
    }

    /**
     * 设置macd 柱状图颜色
     *
     * @param inColor 上升颜色
     * @param deColor 下降颜色
     */
    public KLineChartView setMacdChartColor(int inColor, int deColor) {
        macdDraw.setMacdChartColor(inColor, deColor);
        return this;
    }


    private float getDimension(@DimenRes int resId) {
        return getResources().getDimension(resId);
    }

    private int getColor(@ColorRes int resId) {
        return ContextCompat.getColor(getContext(), resId);
    }


    /**
     * 仅显示LoadingView同时显示K线,如果调用方法前没设置过setProgressBar 会自动加载一个progressBar
     */
    public KLineChartView showLoading() {
        if (null != progressBar) {
            if (progressBar.getVisibility() != View.VISIBLE) {
                post(() -> {
                    progressBar.setVisibility(View.VISIBLE);
                    isAnimationLast = false;
                });
            }
        } else {
            setLoadingView(new ProgressBar(getContext()));
            showLoading();
        }
        return this;
    }

    /**
     * 仅显示LoadingView不显示K线
     */
    public KLineChartView justShowLoading() {
        showLoading();
        isShowLoading = true;
        return this;
    }


    /**
     * 切换时可能会引起动画效果
     * 延迟500换秒隐藏动画以免动画效果显示
     */
    public KLineChartView hideLoading() {
        if (null != progressBar && progressBar.getVisibility() == View.VISIBLE) {
            post(() -> {
                progressBar.setVisibility(View.GONE);
                isShowLoading = false;
            });
        }
        return this;
    }

    /**
     * 隐藏选择器内容
     */
    public KLineChartView hideSelectData() {
        showSelected = false;
        return this;
    }


    /**
     * 设置是否可以缩放
     *
     * @param scaleEnable
     */

    public void setScaleEnable(boolean scaleEnable) {
        super.setScaleEnable(scaleEnable);
    }


    public void setScrollEnable(boolean scrollEnable) {
        super.setScrollEnable(scrollEnable);
    }

    /**
     * 设置DIF颜色
     */
    public KLineChartView setDIFColor(int color) {
        macdDraw.setDIFColor(color);
        return this;
    }

    /**
     * 设置DEA颜色
     */
    public KLineChartView setDEAColor(int color) {
        macdDraw.setDEAColor(color);
        return this;
    }

    /**
     * 设置MACD颜色
     */
    public KLineChartView setMACDColor(int color) {
        macdDraw.setMACDColor(color);
        return this;
    }

    /**
     * 设置MACD的宽度
     *
     * @param MACDWidth width
     */
    public KLineChartView setMACDWidth(float MACDWidth) {
        macdDraw.setMACDWidth(MACDWidth);
        return this;
    }

    /**
     * 设置K颜色
     */
    public KLineChartView setKColor(int color) {
        kdjDraw.setKColor(color);
        return this;
    }

    /**
     * 设置D颜色
     */
    public KLineChartView setDColor(int color) {
        kdjDraw.setDColor(color);
        return this;
    }

    /**
     * 设置J颜色
     */
    public KLineChartView setJColor(int color) {
        kdjDraw.setJColor(color);
        return this;
    }

    /**
     * 设置R颜色
     */
    public KLineChartView setR1Color(int color) {
        wrDraw.setR1Color(color);
        return this;
    }

    /**
     * 设置R颜色
     */
    public KLineChartView setR2Color(int color) {
        wrDraw.setR2Color(color);
        return this;
    }

    /**
     * 设置R颜色
     */
    public KLineChartView setR3Color(int color) {
        wrDraw.setR3Color(color);
        return this;
    }

    /**
     * 设置ma5颜色
     *
     * @param color color
     */
    public KLineChartView setMa1Color(int color) {
        mainDraw.setMaOneColor(color);
        volDraw.setMaOneColor(color);
        return this;
    }

    /**
     * 设置ma10颜色
     *
     * @param color color
     */
    public KLineChartView setMa2Color(int color) {
        mainDraw.setMaTwoColor(color);
        volDraw.setMaTwoColor(color);
        return this;
    }

    /**
     * 设置ma20颜色
     *
     * @param color color
     */
    public KLineChartView setMa3Color(int color) {
        mainDraw.setMaThreeColor(color);
        return this;
    }

    /**
     * 设置选择器文字大小
     *
     * @param textSize textsize
     */
    public KLineChartView setSelectorTextSize(float textSize) {
        mainDraw.setSelectorTextSize(textSize);
        return this;
    }

    /**
     * 设置选择器背景
     *
     * @param color Color
     */
    public KLineChartView setSelectorBackgroundColor(int color) {
        mainDraw.setSelectorBackgroundColor(color);
        return this;
    }

    /**
     * 设置蜡烛宽度
     *
     * @param candleWidth candleWidth
     */
    public KLineChartView setCandleWidth(float candleWidth) {
        mainDraw.setCandleWidth(candleWidth);
        volDraw.setBarWidth(candleWidth);
        return this;
    }

    /**
     * 设置蜡烛线画笔宽度
     *
     * @param candleLineWidth candleLineWidth
     */
    public KLineChartView setCandleLineWidth(float candleLineWidth) {
        mainDraw.setCandleLineWidth(candleLineWidth);
        return this;
    }

    /**
     * 蜡烛是否空心
     */
    public KLineChartView setCandleSolid(boolean candleSolid) {
        mainDraw.setStroke(candleSolid);
        return this;
    }


    /**
     * 设置十字线跟随手势移动
     *
     * @param model {@link Status.CrossTouchModel} default  SHOW_CLOSE
     */
    public KLineChartView setCrossFollowTouch(Status.CrossTouchModel model) {
        this.crossTouchModel = model;
        return this;
    }

    public KLineChartView setRSI1Color(int color) {
        rsiDraw.setRSI1Color(color);
        return this;
    }

    public KLineChartView setRSI2Color(int color) {
        rsiDraw.setRSI2Color(color);
        return this;
    }

    public KLineChartView setRSI3Color(int color) {
        rsiDraw.setRSI3Color(color);
        return this;
    }


    public KLineChartView setLineWidth(float lineWidth) {
        mainDraw.setLineWidth(lineWidth);
        rsiDraw.setLineWidth(lineWidth);
        macdDraw.setLineWidth(lineWidth);
        kdjDraw.setLineWidth(lineWidth);
        wrDraw.setLineWidth(lineWidth);
        volDraw.setLineWidth(lineWidth);
        return this;
    }


    @Override
    public void onSelectedChange(MotionEvent e) {
        if (!isShowLoading) {
            super.onSelectedChange(e);
        }
    }

    /**
     * 设置主视图Y轴上的Label向上的偏移量
     *
     * @param mainYMoveUpInterval default 5
     */
    @SuppressWarnings("unused")
    public KLineChartView setMainYMoveUpInterval(int mainYMoveUpInterval) {
        this.mainYMoveUpInterval = mainYMoveUpInterval;
        return this;
    }

    /**
     * 设置y轴上Label与视图右边距
     *
     * @param yLabelMarginRight default 10
     */
    public KLineChartView setyLabelMarginRight(float yLabelMarginRight) {
        this.yLabelMarginRight = yLabelMarginRight;
        return this;
    }


    public KLineChartView setMainPercent(float mainPercent) {
        this.mainPercent = mainPercent;
        return this;
    }

    public KLineChartView setVolPercent(float volPresent) {
        this.volPercent = volPresent;
        return this;
    }

    public KLineChartView setIndexPercent(float childPresent) {
        this.IndexPercent = childPresent;
        return this;
    }


    /**
     * 获取ValueFormatter
     *
     * @return IValueFormatter
     */
    public IValueFormatter getValueFormatter() {
        return super.getValueFormatter();
    }

    /**
     * 设置ValueFormatter,Y轴价格的格式化器
     *
     * @param valueFormatter value格式化器
     */
    public KLineChartView setValueFormatter(IValueFormatter valueFormatter) {
        this.valueFormatter = valueFormatter;
        mainDraw.setValueFormatter(valueFormatter);
        for (int i = 0; i < indexDraws.size(); i++) {
            indexDraws.get(i).setValueFormatter(valueFormatter);
        }
        return this;
    }

    /**
     * 获取DatetimeFormatter
     *
     * @return 时间格式化器
     */
    public IDateTimeFormatter getDateTimeFormatter() {
        return dateTimeFormatter;
    }

    /**
     * 设置dateTimeFormatter,X轴上label的格式化器
     *
     * @param dateTimeFormatter 时间格式化器
     */
    public KLineChartView setDateTimeFormatter(IDateTimeFormatter dateTimeFormatter) {
        this.dateTimeFormatter = dateTimeFormatter;
        return this;
    }

    /**
     * 设置K线的显示状态
     *
     * @param klineState {@link Status.KlineStatus}
     */
    public KLineChartView setKlineState(Status.KlineStatus klineState) {
        if (this.klineStatus != klineState) {
            this.klineStatus = klineState;
            switch (klineState) {
                case K_LINE:
                    stopFreshPage();
                    break;
                case TIME_LINE:
                    startFreshPage();
                    break;
            }
            setItemCount(0);
            dataAdapter.setResetShowPosition(true);
            invalidate();
        }
        return this;
    }


    /**
     * 设置价格框离右边的距离
     *
     * @param priceBoxMarginRight priceLIneBoxMarginRight
     */
    public KLineChartView setPriceLineBoxMarginRight(float priceBoxMarginRight) {
        this.priceLineBoxMarginRight = priceBoxMarginRight;
        return this;
    }

    /**
     * 价格框内边距
     */
    public KLineChartView setPriceLineBoxePadding(float padding) {
        this.priceLineBoxPadidng = padding;
        return this;
    }

    /**
     * 价格线图形的高
     */
    public KLineChartView setPriceLineShapeHeight(float priceLineShapeHeight) {
        this.priceShapeHeight = priceLineShapeHeight;
        return this;
    }

    /**
     * 价格线图形的宽
     */
    public KLineChartView setPriceLineShapeWidth(float width) {
        this.priceShapeWidth = width;
        return this;
    }

    /**
     * 价格线文字与图形的间隔
     */
    public KLineChartView setPriceBoxShapeTextMargin(float margin) {
        this.priceBoxShapeTextMargin = margin;
        return this;
    }

    /**
     * 设置价格框高度
     *
     * @param priceLineBoxHeight priceLineBoxHeight
     */
    public KLineChartView setPriceLineBoxHeight(float priceLineBoxHeight) {
        this.priceLineBoxHeight = priceLineBoxHeight;
        return this;
    }

    /**
     * 设置选中框前面的文本
     *
     * @param marketInfoText 默认中文 国际化手动调用
     */
    public KLineChartView setMarketInfoText(String[] marketInfoText) {
        mainDraw.setMarketInfoText(marketInfoText);
        return this;
    }

    /**
     * 设置价格框背景色
     *
     * @param color default black
     */
    public KLineChartView setPriceLineBoxBgColor(int color) {
        priceLineBoxBgPaint.setColor(color);
        return this;
    }

    /**
     * 十字线交点小圆颜色
     *
     * @param color default wihte
     */
    public KLineChartView setSelectedPointColor(int color) {
        selectedCrossPaint.setColor(color);
        return this;
    }

    /**
     * 设置选中点外圆颜色
     *
     * @param color default wihte
     */
    public KLineChartView setSelectCrossBigColor(int color) {
        selectedbigCrossPaint.setColor(color);
        return this;
    }

    /**
     * 设置圆点半径
     *
     * @param radius selected circle radius
     */
    public KLineChartView setSelectedPointRadius(float radius) {
        selectedPointRadius = radius;
        return this;
    }


    /**
     * 选中的线的Y轴颜色
     *
     * @param color select y color
     */
    public KLineChartView setSelectedYColor(int color) {
        this.selectedYColor = color;
        return this;
    }

    /**
     * 背景色顶部颜色
     *
     * @param color backgroud linearGrident top
     */
    public KLineChartView setBackGroundFillTopColor(int color) {
        this.backGroundFillTopColor = color;
        return this;
    }

    /**
     * 背景色底部颜色
     *
     * @param color backgroud linearGrident bottom
     */
    public KLineChartView setBackGroundFillBottomColor(int color) {
        this.backGroundFillBottomColor = color;
        return this;
    }

    /**
     * 设置涨的颜色
     *
     * @param color increase color
     */
    public KLineChartView setIncreaseColor(int color) {
        mainDraw.setIncreaseColor(color);
        volDraw.setIncreaseColor(color);
        return this;
    }

    /**
     * 设置跌的颜色
     *
     * @param color decrease color
     */
    public KLineChartView setDecreaseColor(int color) {
        mainDraw.setDecreaseColor(color);
        volDraw.setDecreaseColor(color);
        return this;
    }

    /**
     * 设置分时线颜色
     *
     * @param color time line color
     */
    public KLineChartView setTimeLineColor(int color) {
        mainDraw.setMinuteLineColor(color);
        volDraw.setMinuteColor(color);
        return this;
    }

    /**
     * 设置分时线填充渐变的下部颜色
     *
     * @param color time line top fill color
     */
    public KLineChartView setTimeLineFillTopColor(int color) {
        this.timeLineFillTopColor = color;
        return this;
    }

    /**
     * 设置分时线填充渐变的下部颜色
     *
     * @param color time line bottom fill color
     */
    public KLineChartView setTimeLineFillBottomColor(int color) {
        this.timeLineFillBottomColor = color;
        return this;
    }


    /**
     * 设置主实图指定文字距离视图上边缘的距离,
     *
     * @param mainLengendMarginTop Lengend margin top , default 0
     */
    public KLineChartView setMainLengendMarginTop(float mainLengendMarginTop) {
        mainDraw.setMainLengendMarginTop(mainLengendMarginTop);
        return this;
    }

    /**
     * 交易量图例上边距
     *
     * @param volLengendMarginTop margin default 0
     */
    private KLineChartView setVolLengendMarginTop(float volLengendMarginTop) {
        volDraw.setVolLengendMarginTop(volLengendMarginTop);
        return this;
    }

    /**
     * 交易量图例颜色
     *
     * @param color color
     */
    public KLineChartView setVolLengendColor(int color) {
        volDraw.setVolLengendColor(color);
        return this;
    }

    /**
     * 设置是否自适应X左右边轴坐标的位置,默认true
     *
     * @param betterX true会自动缩进左右两边的label更好的展示
     */
    public KLineChartView setBetterX(boolean betterX) {
        this.betterX = betterX;
        return this;
    }

    public KLineChartView setPriceBoxColor(int backgroundFillPaint) {
        this.rightPriceBoxPaint.setColor(backgroundFillPaint);
        return this;
    }


    /**
     * 价格线右侧的颜色
     *
     * @param color price line right color
     */
    public KLineChartView setPriceLineRightColor(int color) {
        priceLineRightPaint.setColor(color);
        return this;
    }

    /**
     * 价格线右侧价格文字的颜色
     *
     * @param color price line right color
     */
    public KLineChartView setPriceLineRightTextColor(int color) {
        priceLineRightTextPaint.setColor(color);
        return this;
    }


    public float getOverScrollRange() {
        return overScrollRange;
    }

    /**
     * 设置每个点的宽度
     */
    public KLineChartView setChartItemWidth(float pointWidth) {
        chartItemWidth = pointWidth;
        return this;
    }

    /**
     * 获取K线宽度
     *
     * @return chartWidth
     */
    public float getChartItemWidth() {
        return chartItemWidth;
    }


    /**
     * 分时线呼吸灯的颜色
     *
     * @param color pop color
     */
    public KLineChartView setTimeLineEndColor(int color) {
        lineEndPointPaint.setColor(color);
        lineEndFillPointPaint.setColor(color);
        return this;
    }

    /**
     * 分时线呼吸灯的颜色半径
     *
     * @param width pop width
     */
    public KLineChartView setTimeLineEndRadiu(float width) {
        this.lineEndRadiu = width;
        return this;
    }

    /**
     * 分时线尾部呼吸最大倍数
     *
     * @param multiply 倍数
     */
    public KLineChartView setTimeLineEndMultiply(float multiply) {
        this.lineEndMaxMultiply = multiply;
        return this;
    }

    /**
     * 设置价格线的宽度
     *
     * @param lineWidth price line width
     */
    public KLineChartView setPriceLineWidth(float lineWidth) {
        priceLinePaint.setStrokeWidth(lineWidth);
        priceLinePaint.setStyle(Paint.Style.STROKE);
        return this;
    }


    /**
     * 统一设置设置文字大小
     */
    public KLineChartView setTextSize(float textSize) {
        textPaint.setTextSize(textSize);
        Paint.FontMetrics fm = textPaint.getFontMetrics();
        textHeight = fm.descent - fm.ascent;
        textDecent = fm.descent;
        baseLine = (textHeight - fm.bottom - fm.top) / 2;

        priceLineRightPaint.setTextSize(textSize);
        priceLineRightTextPaint.setTextSize(textSize);

        mainDraw.setTextSize(textSize);
        rsiDraw.setTextSize(textSize);
        macdDraw.setTextSize(textSize);
        kdjDraw.setTextSize(textSize);
        wrDraw.setTextSize(textSize);
        volDraw.setTextSize(textSize);
        return this;
    }

    public KLineChartView setPriceLineColor(int color) {
        priceLinePaint.setColor(color);
        return this;
    }


    /**
     * 设置背景颜色
     */
    public KLineChartView setBgColor(int color) {
        this.backgroundPaint.setColor(color);
        backGroundColor = color;
        return this;
    }

    /**
     * 设置选中point 值显示背景
     */
    public KLineChartView setSelectPriceBoxBackgroundColor(int color) {
        selectedPriceBoxBackgroundPaint.setColor(color);
        return this;
    }


    /**
     * 设置超出右方后可滑动的范围
     */
    public KLineChartView setOverScrollRange(float overScrollRange) {
        if (overScrollRange < 0) {
            overScrollRange = 0;
        }
        this.overScrollRange = overScrollRange;
        setScrollX((int) -overScrollRange);
        return this;
    }


    /**
     * 设置上方padding
     *
     * @param chartPaddingTop chatPaddingTop 横线网格和K线会绘制在这个位置的下方
     */
    public KLineChartView setChartPaddingTop(float chartPaddingTop) {
        this.chartPaddingTop = (int) chartPaddingTop;
        return this;
    }

    /**
     * 设置下方padding
     *
     * @param chartPaddingBottom chatPaddingTop 横线网格和K线会绘制在这个位置的下方
     */
    public KLineChartView setChartPaddingBottom(float chartPaddingBottom) {
        this.chartPaddingBottom = (int) chartPaddingBottom;
        return this;
    }


    /**
     * 设置下方padding
     *
     * @param chartPaddingBottom chartPaddingBottom
     */
    @SuppressWarnings("unused")
    public KLineChartView setChartPaddingBottom(int chartPaddingBottom) {
        this.chartPaddingBottom = chartPaddingBottom;
        return this;
    }

    /**
     * 设置表格线宽度
     */
    public KLineChartView setGridLineWidth(float width) {
        gridPaint.setStrokeWidth(width);
        return this;
    }

    /**
     * 设置表格线颜色
     */
    public KLineChartView setGridLineColor(int color) {
        gridPaint.setColor(color);
        return this;
    }

    /**
     * 设置选择器横线宽度
     */
    public KLineChartView setSelectedXLineWidth(float width) {
        selectedXLinePaint.setStrokeWidth(width);
        return this;
    }

    /**
     * 设置选择器横线颜色
     */
    public KLineChartView setSelectedXLineColor(int color) {
        selectedXLinePaint.setColor(color);
        return this;
    }

    /**
     * 设置选择器竖线宽度
     */
    public KLineChartView setSelectedYLineWidth(float width) {
        selectedWidth = width;
        return this;
    }

    /**
     * 设置选择器竖线颜色
     */
    public KLineChartView setSelectedYLineColor(int color) {
        selectedYLinePaint.setColor(color);
        return this;
    }

    /**
     * 设置文字颜色
     */
    public KLineChartView setTextColor(int color) {
        textPaint.setColor(color);
        return this;
    }

    /**
     * 设置选择器弹出框相关颜色 selected popupwindow text color
     *
     * @param textColor       文字
     * @param borderColor     边框
     * @param backgroundColor 背景
     */
    public KLineChartView setSelectedInfoBoxColors(int textColor, int borderColor, int backgroundColor) {
        mainDraw.setSelectorTextColor(textColor, borderColor, backgroundColor);
        return this;
    }


    /**
     * 获取图的宽度
     *
     * @return 宽度
     */
    public float getViewWidth() {
        return super.getViewWidth();
    }


    /**
     * 获取上方padding
     */
    public float getChartPaddingTop() {
        return super.getChartPaddingTop();
    }

    /**
     * 获取子试图上方padding
     */
    @SuppressWarnings("unused")
    public float getChildPadding() {
        return childViewPaddingTop;
    }


    /**
     * 设置表格行数
     */
    public KLineChartView setGridRows(int gridRows) {
        if (gridRows < 1) {
            gridRows = 1;
        }
        rowSpace = displayHeight / gridRows;
        this.gridRows = gridRows;
        return this;
    }

    /**
     * 设置表格列数
     */
    public KLineChartView setGridColumns(int gridColumns) {
        if (gridColumns < 1) {
            gridColumns = 1;
        }
        columnSpace = width / gridColumns;
        this.gridColumns = gridColumns;
        return this;
    }

    /**
     * 获取交易量区域的IchartDraw
     *
     * @return IChartDraw
     */
    public KLineChartView setVolFormatter(IValueFormatter valueFormatter) {
        volDraw.setValueFormatter(valueFormatter);
        return this;
    }


    /**
     * 设置数据适配器
     */
    public KLineChartView setAdapter(KLineChartAdapter adapter) {
        if (null != dataAdapter && null != dataSetObserver) {
            dataAdapter.unregisterDataSetObserver(dataSetObserver);
        }
        dataAdapter = adapter;
        if (null == dataAdapter || null == dataSetObserver) {
            itemsCount = 0;
        } else {
            dataAdapter.registerDataSetObserver(dataSetObserver);
            if (dataAdapter.getCount() > 0) {
                dataAdapter.notifyDataSetChanged();
            }
        }
        return this;
    }


    /**
     * 设置选择监听
     */
    @SuppressWarnings("unused")
    public KLineChartView setOnSelectedChangedListener(OnSelectedChangedListener l) {
        this.mOnSelectedChangedListener = l;
        return this;
    }


    /**
     * 设置当前显示子图
     *
     * @param position {@link Status.IndexStatus}
     */
    public KLineChartView setIndexDraw(Status.IndexStatus position) {
        if (indexDrawPosition.getStatu() == position.getStatu()) {
            return null;
        }

        if (position == Status.IndexStatus.NONE) {
            indexDraw = null;
            if (chartShowStatue == Status.ChildStatus.MAIN_INDEX) {
                chartShowStatue = Status.ChildStatus.MAIN_ONLY;
            } else if (chartShowStatue == Status.ChildStatus.MAIN_VOL_INDEX) {
                chartShowStatue = Status.ChildStatus.MAIN_VOL;
            }
        } else {
            indexDraw = indexDraws.get(position.getStatu());
            if (chartShowStatue == Status.ChildStatus.MAIN_ONLY) {
                chartShowStatue = Status.ChildStatus.MAIN_INDEX;
            } else if (chartShowStatue == Status.ChildStatus.MAIN_VOL) {
                chartShowStatue = Status.ChildStatus.MAIN_VOL_INDEX;
            }
        }
        indexDrawPosition = position;
        initRect();
        invalidate();
        return this;
    }

    /**
     * MA/BOLL切换及隐藏
     *
     * @param status {@link Status.MainStatus}
     */
    public KLineChartView changeMainDrawType(Status.MainStatus status) {
        if (this.status != status) {
            this.status = status;
            invalidate();
        }
        return this;
    }

    /**
     * 设置加载数据时是否使用动画
     *
     * @param withAnim true load data with anim ; default  true
     */
    public KLineChartView setAnimLoadData(boolean withAnim) {
        loadDataWithAnim = withAnim;
        return this;
    }

    /**
     * 获取当前主图状态
     *
     * @return {@link Status.MainStatus}
     */
    public Status.MainStatus getStatus() {
        return super.getStatus();
    }


    /**
     * 设置加载loading
     *
     * @param progressBar loading View
     */
    public KLineChartView setLoadingView(View progressBar) {
        this.progressBar = progressBar;
        LayoutParams layoutParams = new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        layoutParams.addRule(CENTER_IN_PARENT);
        progressBar.setVisibility(INVISIBLE);
        addView(progressBar, layoutParams);
        return this;
    }

    /**
     * 设置滑动监听
     *
     * @param slidListener 监听对象
     */
    public KLineChartView setSlidListener(SlidListener slidListener) {
        this.slidListener = slidListener;
        return this;
    }

    /**
     * 设置加载数据动画时间
     */
    @SuppressWarnings("unused")
    public KLineChartView setAnimationDuration(long duration) {
        if (null != showAnim) {
            showAnim.setDuration(duration);
        }
        return this;
    }


    /**
     * add logo in Kline View
     *
     * @param bitmap logo bitmap
     */
    public KLineChartView setLogoBigmap(Bitmap bitmap) {
        if (null != bitmap) {
            this.logoBitmap = bitmap;
        }
        return this;
    }

    /**
     * add logo in Kline View
     *
     * @param bitmapRes logo resource
     */
    public KLineChartView setLogoResouce(int bitmapRes) {
        if (bitmapRes == 0) {
            logoBitmap = null;
        }
        setLogoBigmap(BitmapFactory.decodeResource(
                getContext().getResources(), bitmapRes));
        return this;
    }

    /**
     * 设置logo透明度
     *
     * @param alpha set the alpha component [0..255] of the paint's color.
     */
    public KLineChartView setLogoAlpha(int alpha) {
        logoPaint.setAlpha(alpha);
        return this;
    }

    /**
     * set logo location  defual left bottom
     *
     * @param left logo left location default 0
     * @param top  logo top location default  -1 when top is -1 the logo will show in bottom
     */
    public KLineChartView setLogoLeftTop(float left, float top) {
        logoLeft = left;
        logoTop = top;
        return this;
    }

    /**
     * set view backGround alpha
     *
     * @param alpha default 18
     */
    public KLineChartView setBackGroundAlpha(int alpha) {
        backgroundPaint.setAlpha(alpha);
        return this;
    }

    /**
     * set cross line show modle
     *
     * @param showCrossModle {@link Status.ShowCrossModle} default SELECTBOTH
     */
    public KLineChartView setSelectedTouchModle(Status.ShowCrossModle showCrossModle) {
        this.modle = showCrossModle;
        return this;
    }


    /**
     * set KlineChartView right Padding,Kline will show in this point left
     *
     * @param klineRightPadding default 0
     */
    public KLineChartView setKlineRightPadding(float klineRightPadding) {
        this.klinePaddingRight = klineRightPadding;
        return this;
    }

    /**
     * set background fill color alpha
     *
     * @param alpha default 150
     */
    public KLineChartView setBackGroudFillAlpha(int alpha) {
        rightPriceBoxPaint.setAlpha(alpha);
        return this;
    }

    /**
     * 当时显示是否是分时线
     *
     * @return isLine
     */
    public boolean isLine() {
        return klineStatus.showLine();
    }

    /**
     * 选中时价格5边弄的横向padding
     *
     * @param padding padding, 带角的3角形的高为 横+纵padding
     */
    public KLineChartView setSelectPriceBoxHorizentalPadding(float padding) {
        this.selectPriceBoxHorizentalPadding = padding;
        return this;
    }

    /**
     * 选中时价格5边弄的纵向padding
     *
     * @param padding padding 带角的3角形的高为 横+纵padding
     */
    public KLineChartView setSelectPriceboxVerticalPadding(float padding) {
        this.selectPriceBoxVerticalPadding = padding;
        return this;
    }

    /**
     * 选中行弹出行情图的margin
     *
     * @param margin
     */
    public KLineChartView setSelectInfoBoxMargin(float margin) {
        mainDraw.setSelectInfoBoxMargin(margin);
        return this;
    }

    /**
     * 选中行弹出行情图的padding,上下为此值*2
     *
     * @param padding
     */
    public KLineChartView setSelectInfoBoxPadding(float padding) {
        mainDraw.setSelectInfoBoxPadding(padding);
        return this;
    }

    /**
     * 当前指标图状态
     *
     * @return {@link Status.IndexStatus}
     */
    public Status.IndexStatus getIndexDrawPosition() {
        return indexDrawPosition;
    }

    /**
     * 切换显示/隐藏交易量
     *
     * @param show default true
     */
    public void setChartVolState(boolean show) {
        switch (chartShowStatue) {
            case MAIN_ONLY:
                if (show) {
                    chartShowStatue = Status.ChildStatus.MAIN_VOL;
                }
                break;
            case MAIN_INDEX:
                if (show) {
                    chartShowStatue = Status.ChildStatus.MAIN_VOL_INDEX;
                }
                break;
            case MAIN_VOL_INDEX:
                if (!show) {
                    chartShowStatue = Status.ChildStatus.MAIN_INDEX;
                }
                break;
            case MAIN_VOL:
                if (!show) {
                    chartShowStatue = Status.ChildStatus.MAIN_ONLY;
                }
                break;
        }
        initRect();
    }


    /**
     * 当时交易量状态
     *
     * @return 显示true 隐藏false
     */
    public boolean getChartVolState() {
        return (chartShowStatue == Status.ChildStatus.MAIN_VOL || chartShowStatue == Status.ChildStatus.MAIN_VOL_INDEX);
    }
}
