package com.icechao.klinelib.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.icechao.klinelib.R;
import com.icechao.klinelib.adapter.BaseKLineChartAdapter;
import com.icechao.klinelib.adapter.KLineChartAdapter;
import com.icechao.klinelib.base.BaseKChartView;
import com.icechao.klinelib.base.BaseRenderer;
import com.icechao.klinelib.callback.IMaxMinDeal;
import com.icechao.klinelib.callback.OnSelectedChangedListener;
import com.icechao.klinelib.callback.PriceLabelInLineClickListener;
import com.icechao.klinelib.callback.SlidListener;
import com.icechao.klinelib.formatter.IDateTimeFormatter;
import com.icechao.klinelib.formatter.IValueFormatter;
import com.icechao.klinelib.formatter.IYValueFormatter;
import com.icechao.klinelib.idraw.IRendererShape;
import com.icechao.klinelib.model.KLineEntity;
import com.icechao.klinelib.renderer.EmaRenderer;
import com.icechao.klinelib.renderer.KDJRenderer;
import com.icechao.klinelib.renderer.MACDRenderer;
import com.icechao.klinelib.renderer.MainRenderer;
import com.icechao.klinelib.renderer.RSIRenderer;
import com.icechao.klinelib.renderer.VolumeRenderer;
import com.icechao.klinelib.renderer.WRRenderer;
import com.icechao.klinelib.utils.DpUtil;
import com.icechao.klinelib.utils.Status;

import java.util.Set;

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
@SuppressWarnings("unused")
public class KChartView extends BaseKChartView {

    private View progressBar;

    public KChartView(Context context) {
        this(context, null);
        initView(context);
    }

    public KChartView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public KChartView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context);
        initAttrs(attrs, context);
    }

    private void initView(Context context) {
        mainRenderer = new MainRenderer(context);
        volumeRenderer = new VolumeRenderer(context);
        addIndexDraw(Status.INDEX_MACD, new MACDRenderer(context));
        addIndexDraw(Status.INDEX_KDJ, new KDJRenderer(context));
        addIndexDraw(Status.INDEX_RSI, new RSIRenderer(context));
        addIndexDraw(Status.INDEX_WR, new WRRenderer(context));
        addIndexDraw(Status.INDEX_EMA, new EmaRenderer(context));
    }

    private void initAttrs(AttributeSet attrs, Context context) {
        TypedArray array = getContext().obtainStyledAttributes(attrs, R.styleable.KChartView);
        parseAttrs(array, context);
    }

    public void parseAttrs(TypedArray array, Context context) {
        if (null != array) {
            try {
                setLogoResouce(array.getResourceId(R.styleable.KChartView_chartLogo, 0));
                //最大最小值
                setLimitTextSize(array.getDimension(R.styleable.KChartView_limitTextSize, DpUtil.Dp2Px(context, 10)));
                setLimitTextColor(array.getColor(R.styleable.KChartView_limitTextColor, Color.parseColor("#6D87A8")));
                //全局
                setBetterX(array.getBoolean(R.styleable.KChartView_betterXLabel, true));
                setBetterSelectedX(array.getBoolean(R.styleable.KChartView_betterSelectedXLabel, true));
                setyLabelMarginBorder(array.getDimension(R.styleable.KChartView_yLabelMarginBorder, 10));
                setCrossFollowTouch(array.getInt(R.styleable.KChartView_closeFollowTouch, Status.TOUCH_SHOW_CLOSE));
                setLineWidth(array.getDimension(R.styleable.KChartView_lineWidth, DpUtil.Dp2Px(context, 0.8f)));
                setCommonTextSize(array.getDimension(R.styleable.KChartView_commonTextSize, DpUtil.Dp2Px(context, 10)));
                setMacdStrokeWidth(array.getDimension(R.styleable.KChartView_macdStrokeWidth, DpUtil.Dp2Px(context, 0.8f)));
                setXLabelTextSize(array.getDimension(R.styleable.KChartView_labelTextSize, DpUtil.Dp2Px(context, 10)));
                setYLabelTextSize(array.getDimension(R.styleable.KChartView_labelTextSize, DpUtil.Dp2Px(context, 10)));
                setCommonTextColor(array.getColor(R.styleable.KChartView_commonTextColor, Color.parseColor("#6D87A8")));
                setXlabelTextColor(array.getColor(R.styleable.KChartView_labelTextColor, Color.parseColor("#6D87A8")));
                setYLabelTextColor(array.getColor(R.styleable.KChartView_labelTextColor, Color.parseColor("#6D87A8")));
                setYlabelAlign(array.getBoolean(R.styleable.KChartView_yLabelAlign, false));
                setChartPaddingTop(array.getDimension(R.styleable.KChartView_paddingTop, DpUtil.Dp2Px(context, 20)));
                setChildPaddingTop(array.getDimension(R.styleable.KChartView_childPaddingTop, DpUtil.Dp2Px(context, 10)));
                setChartPaddingBottom(array.getDimension(R.styleable.KChartView_paddingBottom, 0));
                //网格
                setGridLineWidth(array.getDimension(R.styleable.KChartView_gridLineWidth, DpUtil.Dp2Px(context, 0.8f)));
                setGridColumns(array.getInteger(R.styleable.KChartView_gridLineColumns, 5));
                setGridRows(array.getInteger(R.styleable.KChartView_gridLineRows, 5));
                setGridLineColor(array.getColor(R.styleable.KChartView_gridLineColor, Color.parseColor("#1ACFD3A9")));
                //图例
                setVolLegendColor(array.getColor(R.styleable.KChartView_volLegendColor, Color.parseColor("#6D87A8")));
                setMainLegendMarginTop(array.getDimension(R.styleable.KChartView_mainLegendMarginTop, 10f));
                setLegendMarginLeft(array.getDimension(R.styleable.KChartView_legendMarginLeft, 0f));
                setVolLegendMarginTop(array.getDimension(R.styleable.KChartView_volLegendMarginTop, 10f));
                //成交量
                setVolLineChartColor(array.getColor(R.styleable.KChartView_volLineChartColor, Color.parseColor("#4B85A6")));

                //价格线
                setShowPriceLine(array.getBoolean(R.styleable.KChartView_showPriceLine, true));
                setShowPriceLabelInLine(array.getBoolean(R.styleable.KChartView_showPriceLabelInLine, true));
                setPriceLineWidth(array.getDimension(R.styleable.KChartView_priceLineWidth, DpUtil.Dp2Px(context, 0.8f)));
                setPriceLineColor(array.getColor(R.styleable.KChartView_priceLineColor, Color.parseColor("#6D87A8")));
                setPriceLineRightColor(array.getColor(R.styleable.KChartView_priceLineRightColor, Color.parseColor("#4B85A6")));
                setPriceLabelRightTextColor(array.getColor(R.styleable.KChartView_priceLabelRightTextColor, Color.parseColor("#4B85A6")));
                setPriceLabelRightBackgroundColor(array.getColor(R.styleable.KChartView_priceLabelRightBackgroundColor, Color.parseColor("#131F30")));
                setPriceLabelInLineBoxBackgroundColor(array.getColor(R.styleable.KChartView_priceLabelInLineBoxBackgroundColor, Color.parseColor("#CFD3E9")));
                setPriceLabelInLineBoxBorderColor(array.getColor(R.styleable.KChartView_priceLabelInLineBoxBorderColor, Color.parseColor("#CFD3E9")));
                setPriceLabelInLineBoxBorderWidth(array.getDimension(R.styleable.KChartView_priceLabelInLineBoxBorderWidth, 0.8f));
                setPriceLabelInLineBoxPadding(array.getDimension(R.styleable.KChartView_priceLabelInLineBoxPadding, 20));
                setPriceLabelInLineMarginRight(array.getDimension(R.styleable.KChartView_priceLabelInLineBoxMarginRight, 120));
                setPriceLabelInLineBoxHeight(array.getDimension(R.styleable.KChartView_priceLabelInLineBoxHeight, 40));
                setPriceLabelInLineBoxRadius(array.getDimension(R.styleable.KChartView_priceLabelInLineBoxRadius, 20));
                setPriceLabelInLineShapeWidth(array.getDimension(R.styleable.KChartView_priceLabelInLineShapeWidth, 10));
                setPriceLabelInLineShapeHeight(array.getDimension(R.styleable.KChartView_priceLabelInLineShapeHeight, 20));
                setPriceLabelInLineShapeTextMargin(array.getDimension(R.styleable.KChartView_priceLabelInLineBoxShapeTextMargin, 10));
                setPriceLabelInLineClickable(array.getBoolean(R.styleable.KChartView_priceLabelInLineClickable, false));
                setPriceLineDotSolidWidth(array.getDimension(R.styleable.KChartView_priceLineDotSolidWidth, 8f));
                setPriceLineDotStrokeWidth(array.getDimension(R.styleable.KChartView_priceLineDotStrokeWidth, 4f));
                setShowRightDotPriceLine(array.getBoolean(R.styleable.KChartView_showRightDotPriceLine, true));
                setPriceLabelRightBackgroundAlpha(array.getInt(R.styleable.KChartView_priceLineRightLabelBackGroundAlpha, 255));
                setPriceLabelInLineTextColor(array.getInt(R.styleable.KChartView_priceLabelInLineTextColor, Color.parseColor("#6D87A8")));
                setPriceLabelInLineTextSize(array.getDimension(R.styleable.KChartView_priceLabelInLineTextSize, DpUtil.Dp2Px(context, 10)));

                //十字线
                setSelectedCrossBigColor(array.getColor(R.styleable.KChartView_selectedCrossBigColor, Color.parseColor("#9ACFD3E9")));
                setSelectedPointColor(array.getColor(R.styleable.KChartView_selectedCrossPointColor, Color.WHITE));
                setSelectedPointRadius(array.getDimension(R.styleable.KChartView_selectedCrossPointRadius, DpUtil.Dp2Px(context, 0.8f)));
                setSelectedShowCrossPoint(array.getBoolean(R.styleable.KChartView_selectedShowCrossPoint, true));
                setSelectedXLineWidth(array.getDimension(R.styleable.KChartView_selectedXLineWidth, DpUtil.Dp2Px(context, 0.8f)));
                setSelectedXLabelBorderWidth(array.getDimension(R.styleable.KChartView_selectedXLabelBorderWidth, DpUtil.Dp2Px(context, 0.8f)));
                setSelectedXLabelBorderColor(array.getColor(R.styleable.KChartView_selectedXLabelBorderColor, Color.WHITE));
                setSelectedXLabelBackgroundColor(array.getColor(R.styleable.KChartView_selectedXLabelBackgroundColor, Color.parseColor("#CFD3E9")));
                setSelectedYLineWidth(array.getDimension(R.styleable.KChartView_selectedYLineWidth, 7));
                setSelectedXLineColor(array.getColor(R.styleable.KChartView_selectedXLineColor, Color.parseColor("#CFD3E9")));
                setSelectedYLineColor(array.getColor(R.styleable.KChartView_selectedYLineColor, Color.parseColor("#1ACFD3E9")));
                setSelectedYColor(array.getColor(R.styleable.KChartView_selectedYColor, Color.parseColor("#CFD3E9")));
                setSelectedPriceBoxBackgroundColor(array.getColor(R.styleable.KChartView_selectedPriceBoxBackgroundColor, Color.parseColor("#081724")));
                setSelectedPriceBoxHorizentalPadding(array.getDimension(R.styleable.KChartView_selectedPriceBoxHorizontalPadding, DpUtil.Dp2Px(context, 5)));
                setSelectedPriceboxVerticalPadding(array.getDimension(R.styleable.KChartView_selectedPriceBoxVerticalPadding, DpUtil.Dp2Px(context, 0.8f)));
                setSelectedDateBoxHorizontalPadding(array.getDimension(R.styleable.KChartView_selectedDateBoxHorizontalPadding, DpUtil.Dp2Px(context, 5)));
                setSelectedDateBoxVerticalPadding(array.getDimension(R.styleable.KChartView_selectedDateBoxVerticalPadding, DpUtil.Dp2Px(context, 0.8f)));
                setSelectInfoBoxMargin(array.getDimension(R.styleable.KChartView_selectedInfoBoxMargin, DpUtil.Dp2Px(context, 5)));
                setSelectInfoBoxColors(
                        array.getColor(R.styleable.KChartView_selectedInfoBoxTextColor, Color.WHITE),
                        array.getColor(R.styleable.KChartView_selectedInfoBoxBorderColor, Color.WHITE),
                        array.getColor(R.styleable.KChartView_selectedInfoBoxBackgroundColor, Color.DKGRAY)
                );
                setSelectedInfoTextSize(array.getDimension(R.styleable.KChartView_selectedInfoTextSize, DpUtil.Dp2Px(context, 10)));
                setSelectInfoBoxPadding(array.getDimension(R.styleable.KChartView_selectedInfoBoxPadding, 4));
                setSelectedInfoLabels((String[]) array.getTextArray(R.styleable.KChartView_selectedInfoLabels));
                setSelectedXLabelTextColor(array.getColor(R.styleable.KChartView_selectedXLabelTextColor, Color.parseColor("#6D87A8")));
                setSelectedXLabelTextSize(array.getDimension(R.styleable.KChartView_selectedXLabelTextSize, DpUtil.Dp2Px(context, 10)));
                //K线
                setIncreaseColor(array.getColor(R.styleable.KChartView_increaseColor, Color.GREEN));
                setDecreaseColor(array.getColor(R.styleable.KChartView_decreaseColor, Color.RED));
                setChartItemWidth(array.getDimension(R.styleable.KChartView_itemWidth, 25));
                setCandleWidth(array.getDimension(R.styleable.KChartView_candleWidth, 20));
                setCandleLineWidth(array.getDimension(R.styleable.KChartView_candleLineWidth, DpUtil.Dp2Px(context, 0.8f)));

                //背景添加渐变色
                setBackGroundFillTopColor(array.getColor(R.styleable.KChartView_backgroundFillTopColor, Color.TRANSPARENT));
                setBackGroundFillBottomColor(array.getColor(R.styleable.KChartView_backgroundFillBottomColor, Color.TRANSPARENT));

                // time line
                int defValue = Color.parseColor("#4B85D6");
                int color = array.getColor(R.styleable.KChartView_timeLineColor, defValue);
                setTimeLineColor(color);
                setTimeLineFillTopColor(array.getColor(R.styleable.KChartView_timeLineFillTopColor, Color.parseColor("#404B85D6")));
                setTimeLineFillBottomColor(array.getColor(R.styleable.KChartView_timeLineFillBottomColor, Color.parseColor("#004B85D6")));
                setTimeLineEndColor(array.getColor(R.styleable.KChartView_timeLineEndPointColor, Color.WHITE));
                setTimeLineEndRadius(array.getDimension(R.styleable.KChartView_timeLineEndRadius, DpUtil.Dp2Px(context, 4)));
                setTimeLineEndMultiply(array.getFloat(R.styleable.KChartView_timeLineEndMultiply, 3f));


                //macd
                setMacdChartColor(array.getColor(R.styleable.KChartView_macdIncreaseColor, Color.parseColor("#03C087")),
                        array.getColor(R.styleable.KChartView_macdDecreaseColor, Color.parseColor("#FF605A")));
                setMACDWidth(array.getDimension(R.styleable.KChartView_macdWidth, 10));
                setDIFColor(array.getColor(R.styleable.KChartView_difColor, Color.parseColor("#F6DC93")));
                setDEAColor(array.getColor(R.styleable.KChartView_deaColor, Color.parseColor("#61D1C0")));
                setMACDColor(array.getColor(R.styleable.KChartView_macdColor, Color.parseColor("#CB92FE")));
                //kdj
                setKColor(array.getColor(R.styleable.KChartView_kColor, Color.parseColor("#F6DC93")));
                setDColor(array.getColor(R.styleable.KChartView_dColor, Color.parseColor("#61D1C0")));
                setJColor(array.getColor(R.styleable.KChartView_jColor, Color.parseColor("#CB92FE")));
                //wr
                setR1Color(array.getColor(R.styleable.KChartView_wr1Color, Color.parseColor("#F6DC93")));
                setR2Color(array.getColor(R.styleable.KChartView_wr2Color, Color.parseColor("#61D1C0")));
                setR3Color(array.getColor(R.styleable.KChartView_wr3Color, Color.parseColor("#CB92FE")));
                //rsi
                setRSI1Color(array.getColor(R.styleable.KChartView_rsi1Color, Color.parseColor("#F6DC93")));
                setRSI2Color(array.getColor(R.styleable.KChartView_rsi2Color, Color.parseColor("#61D1C0")));
                setRSI3Color(array.getColor(R.styleable.KChartView_ris3Color, Color.parseColor("#CB92FE")));
                //main
                setMa1Color(array.getColor(R.styleable.KChartView_ma1Color, Color.parseColor("#F6DC93")));
                setMa2Color(array.getColor(R.styleable.KChartView_ma2Color, Color.parseColor("#61D1C0")));
                setMa3Color(array.getColor(R.styleable.KChartView_ma3Color, Color.parseColor("#CB92FE")));

                setVolMa1Color(array.getColor(R.styleable.KChartView_volMa1Color, Color.parseColor("#F6DC93")));
                setVolMa2Color(array.getColor(R.styleable.KChartView_volMa2Color, Color.parseColor("#61D1C0")));
                setMaxMinCoefficient(array.getFloat(R.styleable.KChartView_maxMinCoefficient, 0.05f));

                setCandleHollow(array.getInteger(R.styleable.KChartView_candleHollow, Status.NONE_HOLLOW));
                setMacdHollow(array.getInteger(R.styleable.KChartView_macdHollow, Status.NONE_HOLLOW));

                setKlineState(array.getInt(R.styleable.KChartView_klineState, Status.KLINE_SHOW_TIME_LINE));

            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                array.recycle();
            }
        }
        animInvalidate();
    }

    /**
     * 设置选中X坐标文字颜色
     *
     * @param color {@link Color}
     * @return {@link KChartView}
     */
    public KChartView setSelectedXLabelTextColor(int color) {
        selectedXLabelPaint.setColor(color);
        return this;
    }

    /**
     * 设置价格线上的文字大小
     *
     * @param size size
     * @return {@link KChartView}
     */
    public KChartView setPriceLabelInLineTextSize(float size) {
        labelInPriceLinePaint.setTextSize(size);
        return this;
    }

    /**
     * 设置价格线上的文字颜色
     *
     * @param color {@link Color}
     * @return {@link KChartView}
     */
    public KChartView setPriceLabelInLineTextColor(int color) {
        labelInPriceLinePaint.setColor(color);
        return this;
    }

    /**
     * 设置选中X坐标文字大小
     *
     * @param size size
     * @return {@link KChartView}
     */
    public KChartView setSelectedXLabelTextSize(float size) {
        selectedXLabelPaint.setTextSize(size);
        return this;
    }

    /**
     * 设置主视图最大/最小值文字颜色
     *
     * @param color {@link Color}
     * @return {@link KChartView}
     */
    public KChartView setLimitTextColor(int color) {
        mainRenderer.setLimitTextColor(color);
        return this;
    }

    /**
     * 设置主图片最大/最小值文字大小
     *
     * @param dimension textSize
     * @return {@link KChartView}
     */
    public KChartView setLimitTextSize(float dimension) {
        mainRenderer.setLimitTextSize(dimension);
        return this;
    }

    /**
     * 设置选中时是否显示十字线的交点圆
     *
     * @param showCrossPoint default true
     * @return {@link KChartView}
     */
    public KChartView setSelectedShowCrossPoint(boolean showCrossPoint) {
        this.showCrossPoint = showCrossPoint;
        return this;
    }

    /**
     * 选中时X坐标边框线宽
     *
     * @param width width
     * @return {@link KChartView}
     */
    public KChartView setSelectedXLabelBorderWidth(float width) {
        selectorXFramePaint.setStrokeWidth(width);
        return this;
    }

    /**
     * 选中时X坐标背景颜色
     *
     * @param color width
     * @return {@link KChartView}
     */
    public KChartView setSelectedXLabelBackgroundColor(int color) {
        selectorXBackgroundPaint.setColor(color);
        return this;
    }

    /**
     * 子视图的顶部padding
     *
     * @param childViewPaddingTop padding
     * @return {@link KChartView}
     */
    public KChartView setChildPaddingTop(float childViewPaddingTop) {
        this.childViewPaddingTop = childViewPaddingTop;
        return this;
    }

    /**
     * 选中时X坐标边框线颜色
     *
     * @param color color
     * @return {@link KChartView}
     */
    public KChartView setSelectedXLabelBorderColor(int color) {
        selectorXFramePaint.setColor(color);
        return this;
    }

    /**
     * 价格线虚线实心宽度
     *
     * @param width width
     * @return {@link KChartView}
     */
    public KChartView setPriceLineDotSolidWidth(float width) {
        priceDotLineItemWidth = width;
        return this;
    }

    /**
     * 价格线虚线间隙
     *
     * @param width width
     * @return {@link KChartView}
     */
    public KChartView setPriceLineDotStrokeWidth(float width) {
        priceDotLineItemSpace = width;
        return this;
    }

    /**
     * 价格线框边框宽度
     *
     * @param width width
     * @return {@link KChartView}
     */
    public KChartView setPriceLabelInLineBoxBorderWidth(float width) {
        priceLineBoxPaint.setStrokeWidth(width);
        return this;
    }

    /**
     * 设置价格线右侧框边框颜色
     *
     * @param color color
     * @return {@link KChartView}
     */
    public KChartView setPriceLabelInLineBoxBorderColor(int color) {
        priceLineBoxPaint.setColor(color);
        return this;
    }

    /**
     * 设置macd 柱状图颜色
     *
     * @param inColor 上升颜色
     * @param deColor 下降颜色
     */
    public KChartView setMacdChartColor(int inColor, int deColor) {
        BaseRenderer baseRender = indexRenders.get(Status.INDEX_MACD);
        if (baseRender instanceof MACDRenderer) {
            ((MACDRenderer) baseRender).setMacdChartColor(inColor, deColor);
        }
        return this;
    }

    /**
     * 设置macd 线空心模式
     *
     * @param model {@link Status.HollowModel}
     * @return {@link KChartView}
     */
    public KChartView setMacdHollow(@Status.HollowModel int model) {
        BaseRenderer baseRender = indexRenders.get(Status.INDEX_MACD);
        if (baseRender instanceof MACDRenderer) {
            ((MACDRenderer) baseRender).setStrokeModel(model);
        }
        return this;
    }

    /**
     * macd空心时线宽
     *
     * @param lineWidth 线宽 defaut 0.8dp
     * @return {@link KChartView}
     */
    public KChartView setMacdStrokeWidth(float lineWidth) {
        BaseRenderer baseRender = indexRenders.get(Status.INDEX_MACD);
        if (baseRender instanceof MACDRenderer) {
            ((MACDRenderer) baseRender).setMacdStrokeWidth(lineWidth);
        }
        return this;
    }

    /**
     * 仅显示LoadingView同时显示K线,如果调用方法前没设置过setProgressBar 会自动加载一个progressBar
     *
     * @return {@link KChartView}
     */
    public KChartView showLoading() {
        if (null != progressBar) {
            if (progressBar.getVisibility() != View.VISIBLE) {
                post(() -> {
                    progressBar.setVisibility(View.VISIBLE);
//                    isAnimationLast = false;
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
     *
     * @return {@link KChartView}
     */
    public KChartView justShowLoading() {
        showLoading();
        isShowLoading = true;
        return this;
    }

    /**
     * 切换时可能会引起动画效果
     * 延迟500换秒隐藏动画以免动画效果显示
     *
     * @return {@link KChartView}
     */
    public KChartView hideLoading() {
        if (null != progressBar) {
            postDelayed(() -> {
                progressBar.setVisibility(View.GONE);
                isShowLoading = false;
            }, 500);
        }
        return this;
    }

    /**
     * 隐藏选择器内容
     *
     * @return {@link KChartView}
     */
    public KChartView hideSelectData() {
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


    /**
     * 设置是否可以 滑动
     *
     * @param scrollEnable
     */
    public void setScrollEnable(boolean scrollEnable) {
        super.setScrollEnable(scrollEnable);
    }

    /**
     * 设置DIF颜色
     *
     * @return {@link KChartView}
     */
    public KChartView setDIFColor(int color) {
        BaseRenderer baseRender = indexRenders.get(Status.INDEX_MACD);
        if (baseRender instanceof MACDRenderer) {
            ((MACDRenderer) baseRender).setDIFColor(color);
        }
        return this;
    }

    /**
     * 设置DEA颜色
     *
     * @return {@link KChartView}
     */
    public KChartView setDEAColor(int color) {
        BaseRenderer baseRender = indexRenders.get(Status.INDEX_MACD);
        if (baseRender instanceof MACDRenderer) {
            ((MACDRenderer) baseRender).setDEAColor(color);
        }
        return this;
    }

    /**
     * 设置MACD颜色
     *
     * @return {@link KChartView}
     */
    public KChartView setMACDColor(int color) {
        BaseRenderer baseRender = indexRenders.get(Status.INDEX_MACD);
        if (baseRender instanceof MACDRenderer) {
            ((MACDRenderer) baseRender).setMACDColor(color);
        }
        return this;
    }

    /**
     * 设置MACD的宽度
     *
     * @param MACDWidth width
     * @return {@link KChartView}
     */
    public KChartView setMACDWidth(float MACDWidth) {
        BaseRenderer baseRender = indexRenders.get(Status.INDEX_MACD);
        if (baseRender instanceof MACDRenderer) {
            ((MACDRenderer) baseRender).setMACDWidth(MACDWidth);
        }
        return this;
    }

    /**
     * 设置K颜色
     *
     * @return {@link KChartView}
     */
    public KChartView setKColor(int color) {
        BaseRenderer baseRender = indexRenders.get(Status.INDEX_KDJ);
        if (baseRender instanceof KDJRenderer) {
            ((KDJRenderer) baseRender).setKColor(color);
        }
        return this;
    }

    /**
     * 设置D颜色
     *
     * @return {@link KChartView}
     */
    public KChartView setDColor(int color) {
        BaseRenderer baseRender = indexRenders.get(Status.INDEX_KDJ);
        if (baseRender instanceof KDJRenderer) {
            ((KDJRenderer) baseRender).setDColor(color);
        }
        return this;
    }

    /**
     * 设置J颜色
     *
     * @return {@link KChartView}
     */
    public KChartView setJColor(int color) {
        BaseRenderer baseRender = indexRenders.get(Status.INDEX_KDJ);
        if (baseRender instanceof KDJRenderer) {
            ((KDJRenderer) baseRender).setJColor(color);
        }
        return this;
    }

    /**
     * 设置R颜色
     *
     * @return {@link KChartView}
     */
    public KChartView setR1Color(int color) {
        BaseRenderer baseRender = indexRenders.get(Status.INDEX_WR);
        if (baseRender instanceof WRRenderer) {
            ((WRRenderer) baseRender).setR1Color(color);
        }
        return this;
    }

    /**
     * 设置R颜色
     *
     * @return {@link KChartView}
     */
    public KChartView setR2Color(int color) {
        BaseRenderer baseRender = indexRenders.get(Status.INDEX_WR);
        if (baseRender instanceof WRRenderer) {
            ((WRRenderer) baseRender).setR2Color(color);
        }
        return this;
    }

    /**
     * 设置R颜色
     *
     * @return {@link KChartView}
     */
    public KChartView setR3Color(int color) {
        BaseRenderer baseRender = indexRenders.get(Status.INDEX_WR);
        if (baseRender instanceof WRRenderer) {
            ((WRRenderer) baseRender).setR3Color(color);
        }
        return this;
    }

    public KChartView setVolMa1Color(int color) {
        volumeRenderer.setMaOneColor(color);
        return this;
    }

    public KChartView setVolMa2Color(int color) {
        volumeRenderer.setMaTwoColor(color);
        return this;
    }

    /**
     * 设置ma5颜色
     *
     * @param color color
     * @return {@link KChartView}
     */
    public KChartView setMa1Color(int color) {
        mainRenderer.setMaOneColor(color);
        return this;
    }

    /**
     * 设置ma10颜色
     *
     * @param color color
     * @return {@link KChartView}
     */
    public KChartView setMa2Color(int color) {
        mainRenderer.setMaTwoColor(color);
        return this;
    }

    /**
     * 设置ma20颜色
     *
     * @param color color
     * @return {@link KChartView}
     */
    public KChartView setMa3Color(int color) {
        mainRenderer.setMaThreeColor(color);
        return this;
    }

    /**
     * 设置选择器文字大小
     *
     * @param textSize textsize
     * @return {@link KChartView}
     */
    public KChartView setSelectedInfoTextSize(float textSize) {
        mainRenderer.setSelectorTextSize(textSize);
        return this;
    }

    /**
     * 设置每根蜡烛图宽度
     *
     * @param candleWidth candleWidth
     * @return {@link KChartView}
     */
    public KChartView setCandleWidth(float candleWidth) {
        mainRenderer.setCandleWidth(candleWidth);
        volumeRenderer.setBarWidth(candleWidth);
        return this;
    }

    /**
     * 设置蜡烛线画笔宽(空心时的线宽)
     *
     * @param candleLineWidth candleLineWidth
     * @return {@link KChartView}
     */
    public KChartView setCandleLineWidth(float candleLineWidth) {
        mainRenderer.setCandleLineWidth(candleLineWidth);
        return this;
    }

    /**
     * 蜡烛空心模式
     *
     * @return {@link KChartView}
     */
    public KChartView setCandleHollow(@Status.HollowModel int hollowModel) {
        mainRenderer.setStroke(hollowModel);
        return this;
    }

    /**
     * 设置十字线跟随手势移动/十字线显示收盘价
     *
     * @param model {@link Status.TouchModel} default  SHOW_CLOSE
     * @return {@link KChartView}
     */
    public KChartView setCrossFollowTouch(@Status.TouchModel int model) {
        this.crossTouchModel = model;
        return this;
    }

    public KChartView setRSI1Color(int color) {
        BaseRenderer baseRender = indexRenders.get(Status.INDEX_RSI);
        if (baseRender instanceof RSIRenderer) {
            ((RSIRenderer) baseRender).setRSI1Color(color);
        }
        return this;
    }

    public KChartView setRSI2Color(int color) {
        BaseRenderer baseRender = indexRenders.get(Status.INDEX_RSI);
        if (baseRender instanceof RSIRenderer) {
            ((RSIRenderer) baseRender).setRSI2Color(color);
        }
        return this;
    }

    public KChartView setRSI3Color(int color) {
        BaseRenderer baseRender = indexRenders.get(Status.INDEX_RSI);
        if (baseRender instanceof RSIRenderer) {
            ((RSIRenderer) baseRender).setRSI3Color(color);
        }
        return this;
    }

    /**
     * 全局通用线宽
     *
     * @param lineWidth 线宽 defaut 0.8dp
     * @return {@link KChartView}
     */
    public KChartView setLineWidth(float lineWidth) {
        mainRenderer.setLineWidth(lineWidth);
        volumeRenderer.setLineWidth(lineWidth);
        if (null != indexRenders && indexRenders.size() > 0) {
            Set<Integer> integers = indexRenders.keySet();
            for (Integer integer : integers) {
                BaseRenderer baseRender = indexRenders.get(integer);
                if (null != baseRender) {
                    baseRender.setLineWidth(lineWidth);
                }
            }
        }

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
     * @return {@link KChartView}
     */
    @SuppressWarnings("unused")
    public KChartView setMainYMoveUpInterval(int mainYMoveUpInterval) {
        this.mainYMoveUpInterval = mainYMoveUpInterval;
        return this;
    }

    /**
     * 设置y轴上Label与视图右边距
     *
     * @param yLabelMarginRight default 10
     * @return {@link KChartView}
     */
    public KChartView setyLabelMarginBorder(float yLabelMarginRight) {
        this.yLabelMarginBorder = yLabelMarginRight;
        return this;
    }

    /**
     * 主是视图占比最大 0-10
     *
     * @param mainPercent 0-10
     * @return {@link KChartView}
     */
    public KChartView setMainPercent(float mainPercent) {
        this.mainPercent = mainPercent;
        return this;
    }

    /**
     * 成交量图占比最大 0-10
     *
     * @param volPresent 0-10
     * @return {@link KChartView}
     */
    public KChartView setVolPercent(float volPresent) {
        this.volPercent = volPresent;
        return this;
    }

    /**
     * 指标图占比最大 0-10
     *
     * @param indexPercent 0-10
     * @return {@link KChartView}
     */
    public KChartView setIndexPercent(float indexPercent) {
        this.IndexPercent = indexPercent;
        return this;
    }

    /**
     * 设置Y轴价格的格式化器
     *
     * @param valueFormatter value格式化器
     * @return {@link KChartView}
     */
    public KChartView setYValueFormatter(IYValueFormatter valueFormatter) {
        this.valueFormatter = valueFormatter;
        return this;
    }

    /**
     * 设置主视图Formatter
     *
     * @param valueFormatter value格式化器
     * @return {@link KChartView}
     */
    public KChartView setMainValueFormatter(IValueFormatter valueFormatter) {
        mainRenderer.setValueFormatter(valueFormatter);
        return this;
    }

    /**
     * 设置子视图ValueFormatter,Y轴价格的格式化器
     *
     * @param valueFormatter value格式化器
     * @return {@link KChartView}
     */
    public KChartView setIndexValueFormatter(IValueFormatter valueFormatter) {
        if (null != indexRenders && indexRenders.size() > 0) {
            Set<Integer> integers = indexRenders.keySet();
            for (Integer integer : integers) {
                BaseRenderer baseRender = indexRenders.get(integer);
                if (null != baseRender) {
                    baseRender.setValueFormatter(valueFormatter);
                }
            }
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
     * @return {@link KChartView}
     */
    public KChartView setDateTimeFormatter(IDateTimeFormatter dateTimeFormatter) {
        this.dateTimeFormatter = dateTimeFormatter;
        return this;
    }

    /**
     * 设置K线的显示状态
     *
     * @param klineState {@link Status.KlineStatus}
     * @return {@link KChartView}
     */
    public KChartView setKlineState(@Status.KlineStatus int klineState) {
        if (this.klineStatus != klineState) {
            this.klineStatus = klineState;
            switch (klineState) {
                case Status.K_LINE_SHOW_CANDLE_LINE:
                    stopFreshPage();
                    break;
                case Status.KLINE_SHOW_TIME_LINE:
                    startFreshPage();
                    break;
            }
            setItemsCount(0);
            if (null != dataAdapter) {
                dataAdapter.setResetShowPosition(true);
            }
            invalidate();
        }
        return this;
    }


    /**
     * 设置价格线上价格框离右距离
     *
     * @param priceBoxMarginRight priceLIneBoxMarginRight
     * @return {@link KChartView}
     */
    public KChartView setPriceLabelInLineMarginRight(float priceBoxMarginRight) {
        this.priceLineBoxMarginRight = priceBoxMarginRight;
        return this;
    }

    /**
     * 设置价格线上价格框内边距
     *
     * @return {@link KChartView}
     */
    public KChartView setPriceLabelInLineBoxPadding(float padding) {
        this.priceLineBoxPadidng = padding;
        return this;
    }

    /**
     * 价格线上价格图形高
     *
     * @return {@link KChartView}
     */
    public KChartView setPriceLabelInLineShapeHeight(float priceLineShapeHeight) {
        this.priceShapeHeight = priceLineShapeHeight;
        return this;
    }

    /**
     * 价格线上价格图形宽
     *
     * @return {@link KChartView}
     */
    public KChartView setPriceLabelInLineShapeWidth(float width) {
        this.priceShapeWidth = width;
        return this;
    }

    /**
     * 价格线上价格文字与图形的间隔
     *
     * @return {@link KChartView}
     */
    public KChartView setPriceLabelInLineShapeTextMargin(float margin) {
        this.priceBoxShapeTextMargin = margin;
        return this;
    }

    /**
     * 设置价格线上价格框高度
     *
     * @param priceLineBoxHeight priceLineBoxHeight
     * @return {@link KChartView}
     */
    public KChartView setPriceLabelInLineBoxHeight(float priceLineBoxHeight) {
        this.priceLabelInLineBoxHeight = priceLineBoxHeight;
        return this;
    }

    /**
     * 设置价格线上价格框圆角半径
     *
     * @param radius priceLineBox radius
     * @return {@link KChartView}
     */
    public KChartView setPriceLabelInLineBoxRadius(float radius) {
        this.priceLabelInLineBoxRadius = radius;
        return this;
    }

    /**
     * 设置选中框前面的文本
     *
     * @param infoLabels 默认中文 国际化手动调用
     * @return {@link KChartView}
     */
    public KChartView setSelectedInfoLabels(String[] infoLabels) {
        if (null != infoLabels) {
            if (infoLabels.length != 8) {
                throw new RuntimeException("市场行情信息有且只有八个!请按顺序传入" +
                        "时间 " +
                        "开  " +
                        "高  " +
                        "低  " +
                        "收  " +
                        "涨跌额    " +
                        "涨跌幅    " +
                        "成交量  的翻译!");
            }
            mainRenderer.setMarketInfoText(infoLabels);

        }
        return this;
    }

    /**
     * 设置价格框背景色
     *
     * @param color default black
     * @return {@link KChartView}
     */
    public KChartView setPriceLabelInLineBoxBackgroundColor(int color) {
        priceLineBoxBgPaint.setColor(color);
        return this;
    }

    /**
     * 设置十字线交点小圆颜色
     *
     * @param color default wihte
     * @return {@link KChartView}
     */
    public KChartView setSelectedPointColor(int color) {
        selectedCrossPaint.setColor(color);
        return this;
    }

    /**
     * 设置都十字线选中点外圆颜色
     *
     * @param color default wihte
     * @return {@link KChartView}
     */
    public KChartView setSelectedCrossBigColor(int color) {
        selectedBigCrossPaint.setColor(color);
        return this;
    }

    /**
     * 设置十字线相交小圆半径
     *
     * @param radius selected circle radius
     * @return {@link KChartView}
     */
    public KChartView setSelectedPointRadius(float radius) {
        selectedPointRadius = radius;
        return this;
    }

    /**
     * 选中的线的Y轴颜色
     *
     * @param color select y color
     * @return {@link KChartView}
     */
    public KChartView setSelectedYColor(int color) {
        this.selectedYColor = color;
        return this;
    }

    /**
     * 设置背景色顶部颜色
     *
     * @param color backgroud linearGrident top
     * @return {@link KChartView}
     */
    public KChartView setBackGroundFillTopColor(int color) {
        this.backGroundFillTopColor = color;
        return this;
    }

    /**
     * 背景色底部颜色
     *
     * @param color backgroud linearGrident bottom
     * @return {@link KChartView}
     */
    public KChartView setBackGroundFillBottomColor(int color) {
        this.backGroundFillBottomColor = color;
        return this;
    }

    /**
     * 设置涨的颜色
     *
     * @param color increase color
     * @return {@link KChartView}
     */
    public KChartView setIncreaseColor(int color) {
        mainRenderer.setIncreaseColor(color);
        volumeRenderer.setIncreaseColor(color);
        return this;
    }

    /**
     * 设置跌的颜色
     *
     * @param color decrease color
     * @return {@link KChartView}
     */
    public KChartView setDecreaseColor(int color) {
        mainRenderer.setDecreaseColor(color);
        volumeRenderer.setDecreaseColor(color);
        return this;
    }

    /**
     * 设置分时线颜色
     *
     * @param color time line color
     * @return {@link KChartView}
     */
    public KChartView setTimeLineColor(int color) {
        mainRenderer.setTimeLineColor(color);
        return this;
    }

    /**
     * 设置分时线填充渐变的顶部颜色
     *
     * @param color time line top fill color
     * @return {@link KChartView}
     */
    public KChartView setTimeLineFillTopColor(int color) {
        this.timeLineFillTopColor = color;
        return this;
    }

    /**
     * 设置分时线填充渐变的下部颜色
     *
     * @param color time line bottom fill color
     * @return {@link KChartView}
     */
    public KChartView setTimeLineFillBottomColor(int color) {
        this.timeLineFillBottomColor = color;
        return this;
    }

    /**
     * 设置主实图图例距离主视图上边缘的距离
     *
     * @param mainLegendMarginTop Legend margin top , default 10
     * @return {@link KChartView}
     */
    public KChartView setMainLegendMarginTop(float mainLegendMarginTop) {
        mainRenderer.setMainLegendMarginTop(mainLegendMarginTop);
        return this;
    }

    /**
     * 设置主实图图例距离主视图上边缘的距离
     *
     * @param legendMarginLeft Legend margin left , default 0
     * @return {@link KChartView}
     */
    public KChartView setLegendMarginLeft(float legendMarginLeft) {
        this.legendMarginLeft = legendMarginLeft;
        return this;
    }

    /**
     * 交易量图例距离量视图上边缘的距离
     *
     * @param volLegendMarginTop margin default 0
     * @return {@link KChartView}
     */
    public KChartView setVolLegendMarginTop(float volLegendMarginTop) {
        volumeRenderer.setVolLegendMarginTop(volLegendMarginTop);
        return this;
    }

    /**
     * 交易量图例颜色
     *
     * @param color color
     * @return {@link KChartView}
     */
    public KChartView setVolLegendColor(int color) {
        volumeRenderer.setVolLegendColor(color);
        return this;
    }

    /**
     * 设置是否自适应X左右边轴坐标的位置,默认true
     *
     * @param betterSelectedX true会自动缩进选中两边的label更好的展示
     * @return {@link KChartView}
     */
    public KChartView setBetterSelectedX(boolean betterSelectedX) {
        this.betterSelectedX = betterSelectedX;
        return this;
    }

    /**
     * 设置是否自适应X左右边轴坐标的位置,默认true
     *
     * @param betterX true会自动缩进左右两边的label更好的展示
     * @return {@link KChartView}
     */
    public KChartView setBetterX(boolean betterX) {
        this.betterX = betterX;
        return this;
    }

    /**
     * 使用setPriceLineRightLabelBackGroundColor
     *
     * @param priceRightBoxBackGroundColor backgroundFillPaint
     * @return {@link KChartView}
     */
    @Deprecated
    public KChartView setPriceBoxColor(int priceRightBoxBackGroundColor) {
        this.rightPriceBoxPaint.setColor(priceRightBoxBackGroundColor);
        return this;
    }

    /**
     * 价格线右侧框背景
     *
     * @param priceLIneRightLabelBackGroundColor PriceLineRightLabelBackGroundColor
     * @return {@link KChartView}
     */
    public KChartView setPriceLabelRightBackgroundColor(int priceLIneRightLabelBackGroundColor) {
        this.rightPriceBoxPaint.setColor(priceLIneRightLabelBackGroundColor);
        return this;
    }


    /**
     * 价格线右侧的颜色
     *
     * @param color price line right color
     * @return {@link KChartView}
     */
    public KChartView setPriceLineRightColor(int color) {
        priceLineRightPaint.setColor(color);
        return this;
    }

    /**
     * 使用 setPriceLineRightLabelTextColor
     *
     * @param color price line right color
     * @return {@link KChartView}
     */
    @Deprecated
    public KChartView setPriceLineRightTextColor(int color) {
        priceLineRightTextPaint.setColor(color);
        return this;
    }

    /**
     * 价格线右侧价格文字的颜色
     *
     * @param color price line right color
     * @return {@link KChartView}
     */
    public KChartView setPriceLabelRightTextColor(int color) {
        priceLineRightTextPaint.setColor(color);
        return this;
    }


    /**
     * 设置每根K线总宽度(包含外间隙)
     *
     * @return {@link KChartView}
     */
    public KChartView setChartItemWidth(float pointWidth) {
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
     * @return {@link KChartView}
     */
    public KChartView setTimeLineEndColor(int color) {
        lineEndPointPaint.setColor(color);
        lineEndFillPointPaint.setColor(color);
        return this;
    }

    /**
     * 分时线呼吸灯的颜色半径
     *
     * @param width pop width
     * @return {@link KChartView}
     */
    public KChartView setTimeLineEndRadius(float width) {
        this.lineEndRadius = width;
        return this;
    }

    /**
     * 分时线尾部呼吸最大倍数
     *
     * @param multiply 倍数
     * @return {@link KChartView}
     */
    public KChartView setTimeLineEndMultiply(float multiply) {
        this.lineEndMaxMultiply = multiply;
        return this;
    }

    /**
     * 设置价格线的宽度
     *
     * @param lineWidth price line width
     * @return {@link KChartView}
     */
    public KChartView setPriceLineWidth(float lineWidth) {
        priceLinePaint.setStrokeWidth(lineWidth);
        priceLineRightPaint.setStrokeWidth(lineWidth);
        priceLinePaint.setStyle(Paint.Style.STROKE);
        priceLineRightPaint.setStyle(Paint.Style.STROKE);
        return this;
    }


    /**
     * 统一设置设置文字大小
     *
     * @return {@link KChartView}
     */
    public KChartView setCommonTextSize(float textSize) {
        commonTextPaint.setTextSize(textSize);
        mainRenderer.setTextSize(textSize);
        volumeRenderer.setTextSize(textSize);

        if (indexRenders.size() > 0) {
            Set<Integer> integers = indexRenders.keySet();
            for (Integer integer : integers) {
                BaseRenderer baseRender = indexRenders.get(integer);
                if (null != baseRender) {
                    baseRender.setTextSize(textSize);
                }
            }
        }
        Paint.FontMetrics fm = commonTextPaint.getFontMetrics();
        textHeight = fm.descent - fm.ascent;
        textDecent = fm.descent;
        baseLine = (textHeight - fm.bottom - fm.top) / 2;
        priceLineRightPaint.setTextSize(textSize);
        priceLineRightTextPaint.setTextSize(textSize);

        return this;
    }

    /**
     * 设置价格线颜色
     *
     * @param color {@link Color}
     * @return {@link KChartView}
     */
    public KChartView setPriceLineColor(int color) {
        priceLinePaint.setColor(color);
        return this;
    }


    /**
     * 设置选中Y值背景色
     *
     * @param color {@link Color}
     * @return {@link KChartView}
     */
    public KChartView setSelectedPriceBoxBackgroundColor(int color) {
        selectedPriceBoxBackgroundPaint.setColor(color);
        return this;
    }


    /**
     * 设置K线右侧超出范围
     *
     * @return {@link KChartView}
     */
    public KChartView setOverScrollRange(float overScrollRange) {
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
    public KChartView setChartPaddingTop(float chartPaddingTop) {
        this.chartPaddingTop = (int) chartPaddingTop;
        return this;
    }

    /**
     * 设置下方padding
     *
     * @param chartPaddingBottom chartPaddingBottom 默认为0由XLabel高度决定
     * @return {@link KChartView}
     */
    public KChartView setChartPaddingBottom(float chartPaddingBottom) {
        this.chartPaddingBottom = (int) chartPaddingBottom;
        return this;
    }

    /**
     * 背景网格线宽
     *
     * @return {@link KChartView}
     */
    public KChartView setGridLineWidth(float width) {
        gridPaint.setStrokeWidth(width);
        return this;
    }

    /**
     * 设置表格线颜色
     *
     * @param color {@link Color}
     * @return {@link KChartView}
     */
    public KChartView setGridLineColor(int color) {
        gridPaint.setColor(color);
        return this;
    }

    /**
     * 设置选择器横线宽
     *
     * @param width width
     * @return {@link KChartView}
     */
    public KChartView setSelectedXLineWidth(float width) {
        selectedXLinePaint.setStrokeWidth(width);
        return this;
    }

    /**
     * 设置选择器横线颜色
     *
     * @param color {@link Color}
     * @return {@link KChartView}
     */
    public KChartView setSelectedXLineColor(int color) {
        selectedXLinePaint.setColor(color);
        return this;
    }

    /**
     * 设置十字线竖线宽度
     *
     * @param width width
     * @return {@link KChartView}
     */
    public KChartView setSelectedYLineWidth(float width) {
        selectedWidth = width;
        return this;
    }

    /**
     * 设置选择器竖线颜色
     *
     * @param color {@link Color}
     * @return {@link KChartView}
     */
    public KChartView setSelectedYLineColor(int color) {
        selectedYLinePaint.setColor(color);
        return this;
    }

    /**
     * 设置通用文字颜色
     *
     * @param color {@link Color}
     * @return {@link KChartView}
     */
    public KChartView setCommonTextColor(int color) {
        commonTextPaint.setColor(color);
        return this;
    }

    public KChartView setYLabelTextSize(float textSize) {
        yLabelPaint.setTextSize(textSize);
        return this;
    }

    /**
     * 设置X轴坐标文字大小
     *
     * @param textSize textSie
     * @return
     */
    public KChartView setXLabelTextSize(float textSize) {
        xLabelPaint.setTextSize(textSize);
        return this;
    }

    public KChartView setYLabelTextColor(int color) {
        yLabelPaint.setColor(color);
        return this;
    }

    /**
     * 设置Y轴显示在左侧/右侧
     *
     * @param alignLeft true右侧 左侧false
     * @return {@link KChartView}
     */
    public KChartView setYlabelAlign(boolean alignLeft) {
        if (alignLeft) {
            yLabelPaint.setTextAlign(Paint.Align.LEFT);
            yLabelX = yLabelMarginBorder;
        } else {
            yLabelPaint.setTextAlign(Paint.Align.RIGHT);
            post(() -> yLabelX = getViewWidth() - yLabelMarginBorder);
        }
        return this;
    }


    /**
     * 设置X轴坐标文字颜色
     *
     * @param color {@link Color}
     * @return {@link KChartView}
     */
    public KChartView setXlabelTextColor(int color) {
        xLabelPaint.setColor(color);
        return this;
    }

    /**
     * 设置选择器弹出框相关颜色 selected popupwindow text color
     *
     * @param textColor       文字
     * @param borderColor     边框
     * @param backgroundColor 背景
     * @return {@link KChartView}
     */
    public KChartView setSelectInfoBoxColors(int textColor, int borderColor, int backgroundColor) {
        mainRenderer.setSelectorColors(textColor, borderColor, backgroundColor);
        return this;
    }


    /**
     * 设置表格行数
     *
     * @return {@link KChartView}
     */
    public KChartView setGridRows(int gridRows) {
        if (gridRows < 1) {
            gridRows = 1;
        }
        this.gridRows = gridRows;
        if (0 != displayHeight) {
            rowSpace = displayHeight / gridRows;
        }
        return this;
    }

    /**
     * 设置表格列数
     *
     * @return {@link KChartView}
     */
    public KChartView setGridColumns(int gridColumns) {
        if (gridColumns < 1) {
            gridColumns = 1;
        }
        if (0 != viewWidth) {
            columnSpace = viewWidth / gridColumns;
        }
        this.gridColumns = gridColumns;
        return this;
    }

    /**
     * 获取交易量区域的IchartDraw
     *
     * @return {@link KChartView}
     */
    public KChartView setVolFormatter(IValueFormatter valueFormatter) {
        volumeRenderer.setValueFormatter(valueFormatter);
        return this;
    }


    /**
     * 设置数据适配器
     *
     * @param adapter {@link KLineChartAdapter}
     * @return {@link KChartView}
     */
    public KChartView setAdapter(KLineChartAdapter adapter) {
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
     *
     * @param l {@link OnSelectedChangedListener}
     * @return {@link KChartView}
     */
    @SuppressWarnings("unused")
    public KChartView setOnSelectedChangedListener(OnSelectedChangedListener l) {
        this.selectedChangedListener = l;
        return this;
    }

    /**
     * 设置当前显示子图
     *
     * @param position {@link Status.IndexStatus}
     * @return {@link KChartView}
     */
    public KChartView setIndexDraw(@Status.IndexStatus int position) {
        if (indexDrawPosition == position) {
            return this;
        }

        if (position == Status.INDEX_NONE) {
            indexRenderer = null;
            if (chartShowStatue == Status.MAIN_INDEX) {
                chartShowStatue = Status.MAIN_ONLY;
            } else if (chartShowStatue == Status.MAIN_VOL_INDEX) {
                chartShowStatue = Status.MAIN_VOL;
            }
        } else {
            indexRenderer = this.indexRenders.get(position);
            if (chartShowStatue == Status.MAIN_ONLY) {
                chartShowStatue = Status.MAIN_INDEX;
            } else if (chartShowStatue == Status.MAIN_VOL) {
                chartShowStatue = Status.MAIN_VOL_INDEX;
            }
        }
        indexDrawPosition = position;
        initRect();
        invalidate();
        return this;
    }

    /**
     * 主视图MA/BOLL显示状态
     *
     * @param status {@link Status.MainStatus}
     * @return {@link KChartView}
     */
    public KChartView changeMainDrawType(@Status.MainStatus int status) {
        if (this.mainStatus != status) {
            this.mainStatus = status;
            invalidate();
        }
        return this;
    }

    /**
     * 设置加载数据时是否使用动画
     *
     * @param withAnim true load data with anim ; default  true
     * @return {@link KChartView}
     */
    public KChartView setAnimLoadData(boolean withAnim) {
        loadDataWithAnim = withAnim;
        return this;
    }


    /**
     * 设置k线加载中loading
     *
     * @param progressBar loading View
     * @return {@link KChartView}
     */
    public KChartView setLoadingView(View progressBar) {
        this.progressBar = progressBar;
        LayoutParams layoutParams = new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        layoutParams.addRule(CENTER_IN_PARENT);
        progressBar.setVisibility(INVISIBLE);
        addView(progressBar, layoutParams);
        return this;
    }

    /**
     * 设置K线滑动到边缘的监听
     *
     * @param slidListener 监听对象
     * @return {@link KChartView}
     */
    public KChartView setSlidListener(SlidListener slidListener) {
        this.slidListener = slidListener;
        return this;
    }

    /**
     * 设置加载数据动画时间
     *
     * @param duration 动画执行时长
     * @return {@link KChartView}
     */
    @SuppressWarnings("unused")
    public KChartView setAnimationDuration(long duration) {
        if (null != showAnim) {
            showAnim.setDuration(duration);
        }
        return this;
    }

    /**
     * 设置价格线价格框可点击
     *
     * @param clickable true可点击,点击重置k线显示
     * @return {@link KChartView}
     */
    @SuppressWarnings("unused")
    public KChartView setPriceLabelInLineClickable(boolean clickable) {
        this.priceLabelInLineClickable = clickable;
        return this;
    }

    /**
     * 设置K线右侧运虚线价格线
     *
     * @param showRightDotPriceLine true显示
     * @return {@link KChartView}
     */
    public KChartView setShowRightDotPriceLine(boolean showRightDotPriceLine) {
        this.showRightDotPriceLine = showRightDotPriceLine;
        return this;
    }

    /**
     * /**
     * 设置横屏线价格线显示
     *
     * @param showPriceLine true显示
     * @return {@link KChartView}
     */
    @SuppressWarnings("unused")
    public KChartView setShowPriceLine(boolean showPriceLine) {
        this.showPriceLine = showPriceLine;
        return this;
    }

    /**
     * 设置横屏线价格线上label显示
     *
     * @param showPriceLabelInLine true显示
     * @return {@link KChartView}
     */
    @SuppressWarnings("unused")
    public KChartView setShowPriceLabelInLine(boolean showPriceLabelInLine) {
        this.showPriceLabelInLine = showPriceLabelInLine;
        return this;
    }

    /**
     * 设置横屏线价格线上的Label点击事件
     *
     * @param labelInLineClickListener {@link PriceLabelInLineClickListener}
     * @return {@link KChartView}
     */
    @SuppressWarnings("unused")
    public KChartView setLabelInLineClickListener(PriceLabelInLineClickListener labelInLineClickListener) {
        this.labelInLineClickListener = labelInLineClickListener;
        return this;
    }

    /**
     * 设置K线显示的logo
     *
     * @param bitmap logo bitmap
     * @return {@link KChartView}
     */
    public KChartView setLogoBitmap(Bitmap bitmap) {
        if (null != bitmap) {
            this.logoBitmap = bitmap;
            initRect();
        }
        return this;
    }

    /**
     * 设置K线显示的logo
     *
     * @param bitmapRes logo resource
     * @return {@link KChartView}
     */
    public KChartView setLogoResouce(int bitmapRes) {
        if (bitmapRes == 0) {
            logoBitmap = null;
        } else {
            setLogoBitmap(BitmapFactory.decodeResource(
                    getContext().getResources(), bitmapRes));
        }
        return this;
    }

    /**
     * 设置logo透明度
     *
     * @param alpha set the alpha component [0..255] of the paint's color.
     * @return {@link KChartView}
     */
    public KChartView setLogoAlpha(int alpha) {
        logoPaint.setAlpha(alpha);
        return this;
    }

    /**
     * 设置K线logo与主视图的左边距离,下边距
     *
     * @param leftMargin   logo left location default 0
     * @param bottomMargin logo top location default  -1 when top is -1 the logo will show in bottom
     * @return {@link KChartView}
     */
    public KChartView setLogoMargin(float leftMargin, float bottomMargin) {
        this.logoPaddingLeft = leftMargin;
        this.logoPaddingBottom = bottomMargin;
        initRect();
        return this;
    }

    /**
     * 设置K线显示选中的点击模式
     *
     * @param showCrossModel {@link Status.ShowCrossModel} default SELECT_BOTH
     * @return {@link KChartView}l
     */
    public KChartView setSelectedTouchModel(@Status.ShowCrossModel int showCrossModel) {
        this.model = showCrossModel;
        return this;
    }

    /**
     * 设置价格线右侧标签的背景透明度
     *
     * @param alpha
     * @return
     */
    public KChartView setPriceLabelRightBackgroundAlpha(int alpha) {
        rightPriceBoxPaint.setAlpha(alpha);
        return this;
    }

    /**
     * 当时显示是否是分时线
     *
     * @return isLine
     */
    public boolean isLine() {
        return klineStatus == Status.KLINE_SHOW_TIME_LINE;
    }

    /**
     * 选中时价格label的横向padding
     *
     * @param padding padding, 带角的3角形的高为 横+纵padding
     * @return {@link KChartView}
     */
    public KChartView setSelectedPriceBoxHorizentalPadding(float padding) {
        this.selectedPriceBoxHorizontalPadding = padding;
        return this;
    }

    /**
     * 选中时价格label的纵向padding
     *
     * @param padding padding 带角的3角形的高为 横+纵padding
     * @return {@link KChartView}
     */
    public KChartView setSelectedPriceboxVerticalPadding(float padding) {
        this.selectedPriceBoxVerticalPadding = padding;
        return this;
    }

    /**
     * 选中行弹出框与边缘的间距
     *
     * @param margin
     * @return {@link KChartView}
     */
    public KChartView setSelectInfoBoxMargin(float margin) {
        mainRenderer.setSelectInfoBoxMargin(margin);
        return this;
    }

    /**
     * 选中信息框内边距,上下为此值*2
     *
     * @param padding float
     * @return {@link KChartView}
     */
    public KChartView setSelectInfoBoxPadding(float padding) {
        mainRenderer.setSelectorInfoBoxPadding(padding);
        return this;
    }

    /**
     * 设置当前成交里是否显示
     *
     * @param show true show vol view
     * @return {@link KChartView}
     */
    public KChartView setVolShowState(boolean show) {
        switch (chartShowStatue) {
            case Status.MAIN_ONLY:
                if (show) {
                    chartShowStatue = Status.MAIN_VOL;
                }
                break;
            case Status.MAIN_INDEX:
                if (show) {
                    chartShowStatue = Status.MAIN_VOL_INDEX;
                }
                break;
            case Status.MAIN_VOL_INDEX:
                if (!show) {
                    chartShowStatue = Status.MAIN_INDEX;
                }
                break;
            case Status.MAIN_VOL:
                if (!show) {
                    chartShowStatue = Status.MAIN_ONLY;
                }
                break;
        }
        initRect();
        return this;
    }

    /**
     * 获取当前成交量视图晚是否显示
     *
     * @return 显示true 隐藏false
     */
    public boolean getVolShowState() {
        return (chartShowStatue == Status.MAIN_VOL || chartShowStatue == Status.MAIN_VOL_INDEX);
    }

    /**
     * 设置当成交量视图显示为线时的颜色
     *
     * @param color line color
     * @return {@link KChartView}
     */
    public KChartView setVolLineChartColor(int color) {
        volumeRenderer.setLineChartColor(color);
        return this;
    }

    /**
     * 设置成交量视图显示 柱/线
     *
     * @param volChartStatus defaul time line show line chart , K line show barChart
     * @return {@link KChartView}
     */
    public KChartView setVolChartStatues(@Status.VolChartStatus int volChartStatus) {
        this.volChartStatus = volChartStatus;
        return this;
    }

    /**
     * 隐藏选中信息框
     */
    public KChartView hideSelectedInfoBox() {
        this.hideMarketInfo = true;
        animInvalidate();
        return this;
    }

    /**
     * 替换MainRender  自定义MainRender
     *
     * @param t   MainRender子类对象
     * @param <T> 泛型控制
     * @return {@link KChartView}
     */
    public <T extends MainRenderer> KChartView resetMainRender(T t) {
        this.mainRenderer = t;
        return this;
    }

    /**
     * 替换volRender  自定义volRender使用
     *
     * @param t   VolumeRender子类对象
     * @param <T> 泛型控制
     * @return {@link KChartView}
     */
    public <T extends VolumeRenderer> KChartView resetVolRender(T t) {
        this.volumeRenderer = t;
        return this;
    }

    /**
     * 选中十字线X轴坐标连框纵向内边距
     *
     * @param padding float
     * @return {@link KChartView}
     */
    public KChartView setSelectedDateBoxVerticalPadding(float padding) {
        dateBoxVerticalPadding = padding;
        return this;
    }

    /**
     * 选中十字线X轴坐标连框横向内边距
     *
     * @param padding float
     * @return {@link KChartView}
     */
    public KChartView setSelectedDateBoxHorizontalPadding(float padding) {
        selectedDateBoxHorizontalPadding = padding;
        return this;
    }

    /**
     * 设置最大值最小值的缩放系数
     *
     * @param coefficient double
     * @return {@link KChartView}
     */
    public KChartView setMaxMinCoefficient(double coefficient) {
        maxMinCoefficient = coefficient;
        return this;
    }

    /**
     * 设置主视图最大最小值的计算模式
     *
     * @param model {@link Status.MaxMinCalcModel}
     * @return {@link KChartView}
     */
    public KChartView setMaxMinCalcModel(@Status.MaxMinCalcModel int model) {
        this.calcModel = model;
        return this;
    }

    /**
     * 设置Y轴标签显示模式
     *
     * @param model {@link Status.YLabelShowModel}  独立显示不与K线重叠/显示在K线之上
     * @return {@link KChartView}
     */
    public KChartView setYLabelState(@Status.YLabelShowModel int model) {
        this.yLabelModel = model;
        initRect();
        return this;
    }

    /**
     * 当设置Y轴标签显示模式设置为不重叠时Y轴标签的宽度
     *
     * @param width width  float
     * @return {@link KChartView}
     */
    public KChartView setLabelSpace(float width) {
        labelSpace = width;
        return this;
    }

    /**
     * 设置Y轴显示实时价格标签的背景色
     *
     * @param color           -1清空
     * @param heightSameChart true与图表同高 false 与控件同高
     * @return {@link KChartView}
     */
    public KChartView setYLabelBackgroundColor(int color, boolean heightSameChart) {
        if (-1 != color) {
            this.yLabelBackgroundPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
            this.yLabelBackgroundPaint.setColor(color);
            this.yLabelBackgroundPaint.setStyle(Paint.Style.FILL_AND_STROKE);
            this.yLabelBackgroundHeightSameChart = heightSameChart;
        } else {
            this.yLabelBackgroundPaint = null;
        }
        return this;
    }

    /**
     * 设置是否自动修复滑动
     *
     * @param autoFixScrollEnable boolean
     */
    public void setAutoFixScrollEnable(boolean autoFixScrollEnable) {
        this.autoFixScrollEnable = autoFixScrollEnable;
    }

    /**
     * 替换指标图
     *
     * @param t   IChartRender子类对象
     * @param <T> 泛型控制
     * @return {@link KChartView}
     */
    public <T extends BaseRenderer> KChartView resetIndexRender(@Status.IndexStatus int status, T t) {
        this.indexRenders.put(status, t);
        return this;
    }

    /**
     * 获取适配器
     *
     * @return {@link BaseKLineChartAdapter }
     */
    public BaseKLineChartAdapter<? extends KLineEntity> getAdapter() {
        return dataAdapter;
    }

    /**
     * 给子区域添加画图方法
     *
     * @param childDraw IChartDraw
     */
    public void addIndexDraw(@Status.IndexStatus int tag, BaseRenderer childDraw) {
        indexRenders.put(tag, childDraw);
    }

    /**
     * 获取ValueFormatter
     *
     * @return IValueFormatter
     */
    public IYValueFormatter getValueFormatter() {
        return valueFormatter;
    }

    /**
     * 添加一个绘制图形
     *
     * @param drawShape {@link IMaxMinDeal}
     */
    public void addDrawShape(IRendererShape drawShape) {
        drawShapeEnable = true;
        this.drawShape = drawShape;
        showSelected = false;
        fixScrollEnable(getDataLength());
    }

    /**
     * 移除正在绘制的图形
     *
     * @param drawShape {@link IMaxMinDeal}
     */
    public void removeDrawShape(IRendererShape drawShape) {
        drawShapeEnable = false;
        this.drawShape = drawShape;
        fixScrollEnable(getDataLength());
    }

    /**
     * 移除正在绘制的图形
     */
    public void removeAllDrawShapes() {
        drawShapeEnable = false;
        this.drawShape = null;
        fixScrollEnable(getDataLength());
    }

    /**
     * 设置特别的最大最小值处理方式
     *
     * @param maxMinDeal {@link IMaxMinDeal}
     */
    public void setMaxMinDeal(IMaxMinDeal maxMinDeal) {
        this.maxMinDeal = maxMinDeal;
    }

}
