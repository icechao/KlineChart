package com.icechao.klinelib.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.AnimationDrawable;
import android.support.annotation.ColorRes;
import android.support.annotation.DimenRes;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import com.icechao.klinelib.R;
import com.icechao.klinelib.adapter.KLineChartAdapter;
import com.icechao.klinelib.base.BaseKLineChartView;
import com.icechao.klinelib.base.IDateTimeFormatter;
import com.icechao.klinelib.base.IValueFormatter;
import com.icechao.klinelib.draw.*;
import com.icechao.klinelib.utils.ChildStatus;
import com.icechao.klinelib.utils.Dputil;
import com.icechao.klinelib.utils.MainStatus;

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

    private ImageView progressBar;

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
        initView(context);
        initAttrs(attrs);
    }

    public KLineChartView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context);
        initAttrs(attrs);
    }

    private void initView(Context context) {

        volDraw = new VolumeDraw(context);
        macdDraw = new MACDDraw(context, getResources().getColor(R.color.color_03C087), getResources().getColor(R.color.color_FF605A));
        wrDraw = new WRDraw(context);
        kdjDraw = new KDJDraw(context);
        rsiDraw = new RSIDraw(context);
        mainDraw = new MainDraw(context);
        addChildDraw(macdDraw);
        addChildDraw(kdjDraw);
        addChildDraw(rsiDraw);
        addChildDraw(wrDraw);

        volDraw.setVolLeftColor(getResources().getColor(R.color.color_6D87A8));
        setPriceLineWidth(Dputil.Dp2Px(context, 1));
        setPriceLineColor(getResources().getColor(R.color.color_6D87A8));
        setPriceLineRightColor(getResources().getColor(R.color.color_4B85D6));
        setSelectCrossBigColor(getResources().getColor(R.color.color_9ACFD3E9));
        setSelectCrossColor(getResources().getColor(R.color.color_CFD3E9));
        setSelectedYColor(getResources().getColor(R.color.color_CFD3E9));

        setUpColor(getResources().getColor(R.color.color_03C087));
        setDownColor(getResources().getColor(R.color.color_FF605A));
        setMinuteLineColor(getResources().getColor(R.color.color_4B85D6));


        //背景添加渐变色
        setBackgroundStartColor(getResources().getColor(R.color.color_1C1E27));
        setBackgroundEmdColor(getResources().getColor(R.color.color_4B85D6));

        setAreaTopColor(getResources().getColor(R.color.color_404B85D6));
        setAreaBottomColor(getResources().getColor(R.color.color_004B85D6));

        setEndPointColor(Color.WHITE);
        setLineEndPointWidth(Dputil.Dp2Px(context, 4));

        setBackgroundFillColor(getContext().getResources().getColor(R.color.color_131F30));
    }

    private void initAttrs(AttributeSet attrs) {
        TypedArray array = getContext().obtainStyledAttributes(attrs, R.styleable.KLineChartView);
        if (null != array) {
            try {
                mainDraw.setLimitTextSize(array.getDimension(R.styleable.KLineChartView_kc_text_size, getDimension(R.dimen.chart_text_size)));
                mainDraw.setLimitTextColor(array.getColor(R.styleable.KLineChartView_kc_text_color, getColor(R.color.color_6D87A8)));
                setChartItemWidth(array.getDimension(R.styleable.KLineChartView_kc_point_width, getDimension(R.dimen.chart_point_width)));
                setTextSize(array.getDimension(R.styleable.KLineChartView_kc_text_size, getDimension(R.dimen.chart_text_size)));

                setTextColor(array.getColor(R.styleable.KLineChartView_kc_text_color, getColor(R.color.color_6D87A8)));
                setGridLineWidth(array.getDimension(R.styleable.KLineChartView_kc_grid_line_width, getDimension(R.dimen.chart_grid_line_width)));
                setGridLineColor(array.getColor(R.styleable.KLineChartView_kc_grid_line_color, getColor(R.color.color_223349)));
                //macd
                setMACDWidth(array.getDimension(R.styleable.KLineChartView_kc_macd_width, getDimension(R.dimen.chart_candle_width)));
                setDIFColor(array.getColor(R.styleable.KLineChartView_kc_dif_color, getColor(R.color.color_F6DC93)));
                setDEAColor(array.getColor(R.styleable.KLineChartView_kc_dea_color, getColor(R.color.color_61D1C0)));
                setMACDColor(array.getColor(R.styleable.KLineChartView_kc_macd_color, getColor(R.color.color_CB92FE)));
                //kdj
                setKColor(array.getColor(R.styleable.KLineChartView_kc_dif_color, getColor(R.color.color_F6DC93)));
                setDColor(array.getColor(R.styleable.KLineChartView_kc_dea_color, getColor(R.color.color_61D1C0)));
                setJColor(array.getColor(R.styleable.KLineChartView_kc_macd_color, getColor(R.color.color_CB92FE)));
                //wr
                setR1Color(array.getColor(R.styleable.KLineChartView_kc_dif_color, getColor(R.color.color_F6DC93)));
                setR2Color(array.getColor(R.styleable.KLineChartView_kc_dif_color, getColor(R.color.color_61D1C0)));
                setR3Color(array.getColor(R.styleable.KLineChartView_kc_dif_color, getColor(R.color.color_CB92FE)));
                //rsi
                setRSI1Color(array.getColor(R.styleable.KLineChartView_kc_dif_color, getColor(R.color.color_F6DC93)));
                setRSI2Color(array.getColor(R.styleable.KLineChartView_kc_dea_color, getColor(R.color.color_61D1C0)));
                setRSI3Color(array.getColor(R.styleable.KLineChartView_kc_macd_color, getColor(R.color.color_CB92FE)));
                //main
                setMaOneColor(array.getColor(R.styleable.KLineChartView_kc_dif_color, getColor(R.color.color_F6DC93)));
                setMaTwoColor(array.getColor(R.styleable.KLineChartView_kc_dea_color, getColor(R.color.color_61D1C0)));
                setMaThreeColor(array.getColor(R.styleable.KLineChartView_kc_macd_color, getColor(R.color.color_CB92FE)));

                setSelectedXLineWidth(getDimension(R.dimen.chart_line_width));
                setSelectedYLineWidth(getDimension(R.dimen.chart_point_width));
                setSelectedXLineColor(getResources().getColor(R.color.color_CFD3E9));
                setSelectedYLineColor(getResources().getColor(R.color.color_1ACFD3E9));
                setSelectPointColor(array.getColor(R.styleable.KLineChartView_kc_background_color, getColor(R.color.color_081724)));
                setSelectorBackgroundColor(array.getColor(R.styleable.KLineChartView_kc_selector_background_color, getColor(R.color.color_EA111725)));
                setSelectorTextSize(array.getDimension(R.styleable.KLineChartView_kc_selector_text_size, getDimension(R.dimen.chart_selector_text_size)));
                setCandleSolid(array.getBoolean(R.styleable.KLineChartView_kc_candle_solid, false));

                setLineWidth(array.getDimension(R.styleable.KLineChartView_kc_line_width, getDimension(R.dimen.chart_line_width)));
                setCandleWidth(array.getDimension(R.styleable.KLineChartView_kc_candle_width, getDimension(R.dimen.chart_candle_width)));
                setCandleLineWidth(array.getDimension(R.styleable.KLineChartView_kc_candle_line_width, getDimension(R.dimen.chart_candle_line_width)));

            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                array.recycle();
            }
        }
    }

    private float getDimension(@DimenRes int resId) {
        return getResources().getDimension(resId);
    }

    private int getColor(@ColorRes int resId) {
        return ContextCompat.getColor(getContext(), resId);
    }

    @Override
    public void onLeftSide() {
        showLoading();
    }

    @Override
    public void onRightSide() {
    }


    /**
     * 仅显示LoadingView同时显示K线
     */
    public void showLoading() {
        if (null != progressBar && progressBar.getVisibility() != View.VISIBLE) {
            post(() -> {
                progressBar.setVisibility(View.VISIBLE);
                AnimationDrawable drawable = (AnimationDrawable) progressBar.getDrawable();
                drawable.start();
                isAnimationLast = false;
            });
        }
    }

    /**
     * 仅显示LoadingView不显示K线
     */
    public void justShowLoading() {
        showLoading();
        isShowLoading = true;
    }


    /**
     * 切换时可能会引起动画效果
     * 延迟500换秒隐藏动画以免动画效果显示
     */
    public void hideLoading() {
        if (null != progressBar && progressBar.getVisibility() == View.VISIBLE) {
            post(() -> {
                progressBar.setVisibility(View.GONE);
                AnimationDrawable drawable = (AnimationDrawable) progressBar.getDrawable();
                drawable.stop();
                isShowLoading = false;
            });
        }
    }

    /**
     * 隐藏选择器内容
     */
    public void hideSelectData() {
        isLongPress = false;
    }


    @Override
    public void setScaleEnable(boolean scaleEnable) {
        super.setScaleEnable(scaleEnable);

    }

    @Override
    public void setScrollEnable(boolean scrollEnable) {
        super.setScrollEnable(scrollEnable);
    }

    /**
     * 设置DIF颜色
     */
    public void setDIFColor(int color) {
        macdDraw.setDIFColor(color);
    }

    /**
     * 设置DEA颜色
     */
    public void setDEAColor(int color) {
        macdDraw.setDEAColor(color);
    }

    /**
     * 设置MACD颜色
     */
    public void setMACDColor(int color) {
        macdDraw.setMACDColor(color);
    }

    /**
     * 设置MACD的宽度
     *
     * @param MACDWidth width
     */
    public void setMACDWidth(float MACDWidth) {
        macdDraw.setMACDWidth(MACDWidth);
    }

    /**
     * 设置K颜色
     */
    public void setKColor(int color) {
        kdjDraw.setKColor(color);
    }

    /**
     * 设置D颜色
     */
    public void setDColor(int color) {
        kdjDraw.setDColor(color);
    }

    /**
     * 设置J颜色
     */
    public void setJColor(int color) {
        kdjDraw.setJColor(color);
    }

    /**
     * 设置R颜色
     */
    public void setR1Color(int color) {
        wrDraw.setR1Color(color);
    }

    /**
     * 设置R颜色
     */
    public void setR2Color(int color) {
        wrDraw.setR2Color(color);
    }

    /**
     * 设置R颜色
     */
    public void setR3Color(int color) {
        wrDraw.setR3Color(color);
    }

    /**
     * 设置ma5颜色
     *
     * @param color color
     */
    public void setMaOneColor(int color) {
        mainDraw.setMaOneColor(color);
        volDraw.setMa5Color(color);
    }

    /**
     * 设置ma10颜色
     *
     * @param color color
     */
    public void setMaTwoColor(int color) {
        mainDraw.setMaTwoColor(color);
        volDraw.setMa10Color(color);
    }

    /**
     * 设置ma20颜色
     *
     * @param color color
     */
    public void setMaThreeColor(int color) {
        mainDraw.setMaThreeColor(color);
    }

    /**
     * 设置选择器文字大小
     *
     * @param textSize textsize
     */
    public void setSelectorTextSize(float textSize) {
        mainDraw.setSelectorTextSize(textSize);
    }

    /**
     * 设置选择器背景
     *
     * @param color Color
     */
    public void setSelectorBackgroundColor(int color) {
        mainDraw.setSelectorBackgroundColor(color);
    }

    /**
     * 设置蜡烛宽度
     *
     * @param candleWidth candleWidth
     */
    public void setCandleWidth(float candleWidth) {
        mainDraw.setCandleWidth(candleWidth);
        volDraw.setBarWidth(candleWidth);
    }

    /**
     * 设置蜡烛线宽度
     *
     * @param candleLineWidth candleLineWidth
     */
    public void setCandleLineWidth(float candleLineWidth) {
        mainDraw.setCandleLineWidth(candleLineWidth);
    }

    /**
     * 蜡烛是否空心
     */
    public void setCandleSolid(boolean candleSolid) {
        mainDraw.setStroke(candleSolid);
    }


    /**
     * 设置十字线跟随手势移动
     *
     * @param crossFollowTouch true跟随false不跟随,十字线的指示框只会显示当前K线的收盘价
     */
    public void setCrossFollowTouch(boolean crossFollowTouch) {
        this.crossFollowTouch = crossFollowTouch;
    }

    public void setRSI1Color(int color) {
        rsiDraw.setRSI1Color(color);
    }

    public void setRSI2Color(int color) {
        rsiDraw.setRSI2Color(color);
    }

    public void setRSI3Color(int color) {
        rsiDraw.setRSI3Color(color);
    }


    public void setLineWidth(float lineWidth) {
        mainDraw.setLineWidth(lineWidth);
        rsiDraw.setLineWidth(lineWidth);
        macdDraw.setLineWidth(lineWidth);
        kdjDraw.setLineWidth(lineWidth);
        wrDraw.setLineWidth(lineWidth);
        volDraw.setLineWidth(lineWidth);
    }


    public void setMainDrawLine(boolean isLine) {
        setShowLine(isLine);
    }

    @Override
    public void onLongPress(MotionEvent e) {
        if (!isShowLoading) {
            super.onLongPress(e);
        }
    }

    /**
     * 设置主视图Y轴上的Label向上的便宜量
     *
     * @param mainYMoveUpInterval default 5
     */
    @SuppressWarnings("unused")
    public void setMainYMoveUpInterval(int mainYMoveUpInterval) {
        this.mainYMoveUpInterval = mainYMoveUpInterval;
    }

    /**
     * 设置y轴上Label距离右侧的空隙
     *
     * @param yLabelMarginRight default 10
     */
    @SuppressWarnings("unused")
    public void setyLabelMarginRight(int yLabelMarginRight) {
        this.yLabelMarginRight = yLabelMarginRight;
    }


    @SuppressWarnings("unused")
    public void setMainPresent(float mainPresent) {
        this.mainPresent = mainPresent;
    }

    @SuppressWarnings("unused")
    public void setVolPresent(float volPresent) {
        this.volPresent = volPresent;
    }

    @SuppressWarnings("unused")
    public void setChildPresent(float childPresent) {
        this.childPresent = childPresent;
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
    public void setValueFormatter(IValueFormatter valueFormatter) {
        this.valueFormatter = valueFormatter;
        mainDraw.setValueFormatter(valueFormatter);
        for (int i = 0; i < mChildDraws.size(); i++) {
            mChildDraws.get(i).setValueFormatter(valueFormatter);
        }
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
    public void setDateTimeFormatter(IDateTimeFormatter dateTimeFormatter) {
        this.dateTimeFormatter = dateTimeFormatter;
    }

    public void setShowLine(boolean isLine) {
        if (isLine && !this.isLine) {
            startFreshPage();
            this.isLine = true;
        } else if (this.isLine && !isLine) {
            stopFreshPage();
            this.isLine = false;
        }
        setResetTranslate(true);
        setItemCount(0);
        invalidate();
    }


    /**
     * 设置价格框离右边的距离
     *
     * @param priceBoxMarginRight priceBoxMarginRight
     */
    @SuppressWarnings("unused")
    public void setPriceBoxMarginRight(float priceBoxMarginRight) {
        this.priceBoxMarginRight = priceBoxMarginRight;
    }

    /**
     * 设置价格框高度
     *
     * @param priceLineBoxHeight priceLineBoxHeight
     */
    @SuppressWarnings("unused")
    public void setPriceLineBoxHeight(int priceLineBoxHeight) {
        this.priceLineBoxHeight = priceLineBoxHeight;
    }

    /**
     * 设置选中框前面的文本
     *
     * @param marketInfoText 默认中文
     */
    @SuppressWarnings("unused")
    public void setMarketInfoText(String[] marketInfoText) {
        mainDraw.setMarketInfoText(marketInfoText);
    }

    /**
     * 设置价格框背景色
     *
     * @param color default black
     */
    @SuppressWarnings("unused")
    public void setPriceBoxBgColor(int color) {
        priceLineBoxBgPaint.setColor(color);
    }

    /**
     * 设置选中点的颜色
     *
     * @param color default wihte
     */
    @SuppressWarnings("unused")
    public void setSelectCrossColor(int color) {
        selectedCrossPaint.setColor(color);
    }

    /**
     * 设置选中点外圆颜色
     *
     * @param color default wihte
     */
    @SuppressWarnings("unused")
    public void setSelectCrossBigColor(int color) {
        selectedbigCrossPaint.setColor(color);
    }

    /**
     * 设置价格框边框颜色
     *
     * @param color default wihte
     */
    @SuppressWarnings("unused")
    public void setPriceBoxBorderColor(int color) {
        priceLineBoxPaint.setColor(color);
    }

    /**
     * 设置价格框边框宽度
     *
     * @param width default 1
     */
    @SuppressWarnings("unused")
    public void setPriceBoxBorderWidth(int width) {
        priceLineBoxPaint.setStrokeWidth(width);
    }

    /**
     * 设置圆点半径
     *
     * @param radius selected circle radius
     */
    public void setSelectedPointRadius(float radius) {
        selectedPointRadius = radius;
    }


    /**
     * 选中的线的Y轴颜色
     *
     * @param color select y color
     */
    public void setSelectedYColor(int color) {
        this.selectedYColor = color;
    }

    /**
     * 背景色顶部颜色
     *
     * @param color backgroud linearGrident top
     */
    public void setBackgroundStartColor(int color) {
        this.backGroundTopColor = color;
    }

    /**
     * 背景色底部颜色
     *
     * @param color backgroud linearGrident bottom
     */
    public void setBackgroundEmdColor(int color) {
        this.backGroundBottomColor = color;
    }

    /**
     * 设置涨的颜色
     *
     * @param color increase color
     */
    public void setUpColor(int color) {
        mainDraw.setUpColor(color);

    }

    /**
     * 设置跌的颜色
     *
     * @param color uncrease color
     */
    public void setDownColor(int color) {
        mainDraw.setDownColor(color);
    }

    /**
     * 设置分时线颜色
     *
     * @param color time line color
     */
    public void setMinuteLineColor(int color) {
        mainDraw.setMinuteLineColor(color);
        ((VolumeDraw) volDraw).setMinuteColor(color);
    }

    /**
     * 设置分时线填充渐变的下部颜色
     *
     * @param color time line top fill color
     */
    public void setAreaTopColor(int color) {
        this.areaTopColor = color;
    }

    /**
     * 设置分时线填充渐变的下部颜色
     *
     * @param color time line bottom fill color
     */
    public void setAreaBottomColor(int color) {
        this.areaBottomColor = color;
    }


    /**
     * 设置主实图指定文字距离视图上边缘的距离,
     *
     * @param indexPaddingTop default 0
     */
    public void setIndexPaddingTop(int indexPaddingTop) {
        mainDraw.setIndexPaddingTop(indexPaddingTop);
    }

    /**
     * 设置是否自适应X左右边轴坐标的位置,默认true
     *
     * @param betterX true会自动缩进左右两边的label更好的展示
     */
    public void setBetterX(boolean betterX) {
        this.betterX = betterX;
    }

    public void setBackgroundFillColor(int backgroundFillPaint) {
        this.backgroundFillPaint.setColor(backgroundFillPaint);
    }


    /**
     * 价格线右侧的颜色
     *
     * @param color price line right color
     */
    public void setPriceLineRightColor(int color) {
        priceLineBoxRightPaint.setColor(color);
    }

    public float getOverScrollRange() {
        return overScrollRange;
    }

    /**
     * 设置每个点的宽度
     */
    public void setChartItemWidth(float pointWidth) {
        chartItemWidth = pointWidth;
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
    public void setEndPointColor(int color) {
        lineEndPointPaint.setColor(color);
        lineEndFillPointPaint.setColor(color);
    }

    /**
     * 分时线呼吸灯的颜色半径
     *
     * @param width pop width
     */
    public void setLineEndPointWidth(float width) {
        this.lineEndPointWidth = width;
    }


    /**
     * 设置价格线的宽度
     *
     * @param lineWidth price line width
     */
    public void setPriceLineWidth(float lineWidth) {
        priceLinePaint.setStrokeWidth(lineWidth);
        priceLinePaint.setStyle(Paint.Style.STROKE);
    }


    /**
     * 统一设置设置文字大小
     */
    public void setTextSize(float textSize) {
        textPaint.setTextSize(textSize);
        Paint.FontMetrics fm = textPaint.getFontMetrics();
        textHeight = fm.descent - fm.ascent;
        textDecent = fm.descent;
        baseLine = (textHeight - fm.bottom - fm.top) / 2;
        priceLineBoxRightPaint.setTextSize(textSize);

        mainDraw.setTextSize(textSize);
        rsiDraw.setTextSize(textSize);
        macdDraw.setTextSize(textSize);
        kdjDraw.setTextSize(textSize);
        wrDraw.setTextSize(textSize);
        volDraw.setTextSize(textSize);
    }

    public void setPriceLineColor(int color) {
        priceLinePaint.setColor(color);
    }


    /**
     * 设置背景颜色
     */
    public void setBackgroundColor(int color) {
        this.backgroundPaint.setColor(color);
    }

    /**
     * 设置选中point 值显示背景
     */
    public void setSelectPointColor(int color) {
        selectedPointPaint.setColor(color);
    }


    /**
     * 设置超出右方后可滑动的范围
     */
    public void setOverScrollRange(float overScrollRange) {
        if (overScrollRange < 0) {
            overScrollRange = 0;
        }
        this.overScrollRange = overScrollRange;
        setScrollX((int) -overScrollRange);
    }


    /**
     * 设置上方padding
     *
     * @param topPadding topPadding
     */
    @SuppressWarnings("unused")
    public void setTopPadding(int topPadding) {
        this.topPadding = topPadding;
    }

    /**
     * 设置下方padding
     *
     * @param bottomPadding bottomPadding
     */
    @SuppressWarnings("unused")
    public void setBottomPadding(int bottomPadding) {
        this.bottomPadding = bottomPadding;
    }

    /**
     * 设置表格线宽度
     */
    public void setGridLineWidth(float width) {
        gridPaint.setStrokeWidth(width);
    }

    /**
     * 设置表格线颜色
     */
    public void setGridLineColor(int color) {
        gridPaint.setColor(color);
    }

    /**
     * 设置选择器横线宽度
     */
    public void setSelectedXLineWidth(float width) {
        selectedXLinePaint.setStrokeWidth(width);
    }

    /**
     * 设置选择器横线颜色
     */
    public void setSelectedXLineColor(int color) {
        selectedXLinePaint.setColor(color);
    }

    /**
     * 设置选择器竖线宽度
     */
    public void setSelectedYLineWidth(float width) {
        selectedWidth = width;
    }

    /**
     * 设置选择器竖线颜色
     */
    public void setSelectedYLineColor(int color) {
        selectedYLinePaint.setColor(color);
    }

    /**
     * 设置文字颜色
     */
    public void setTextColor(int color) {
        textPaint.setColor(color);
    }

    /**
     * 设置选中文字的颜色
     *
     * @param color
     */
    public void setSelectedTextColor(int color) {
        mainDraw.setSelectorTextColor(color);
    }


    /**
     * 获取图的宽度
     *
     * @return 宽度
     */
    public int getChartWidth() {
        return super.getChartWidth();
    }


    /**
     * 获取上方padding
     */
    public float getTopPadding() {
        return super.getTopPadding();
    }

    /**
     * 获取子试图上方padding
     */
    @SuppressWarnings("unused")
    public float getChildPadding() {
        return childPadding;
    }


    /**
     * 设置表格行数
     */
    public void setGridRows(int gridRows) {
        if (gridRows < 1) {
            gridRows = 1;
        }
        rowSpace = displayHeight / gridRows;
        this.gridRows = gridRows;
    }

    /**
     * 设置表格列数
     */
    public void setGridColumns(int gridColumns) {
        if (gridColumns < 1) {
            gridColumns = 1;
        }
        columnSpace = (float) width / gridColumns;
        this.gridColumns = gridColumns;
    }

    /**
     * 获取交易量区域的IchartDraw
     *
     * @return IChartDraw
     */
    public void setVolFormatter(IValueFormatter valueFormatter) {
        volDraw.setValueFormatter(valueFormatter);
    }


    /**
     * 设置数据适配器
     */
    public void setAdapter(KLineChartAdapter adapter) {
        if (null != dataAdapter && null != dataSetObserver) {
            dataAdapter.unregisterDataSetObserver(dataSetObserver);
        }
        dataAdapter = adapter;
        if (null == dataAdapter || null == dataSetObserver) {
            itemsCount = 0;
            return;

        }
        dataAdapter.registerDataSetObserver(dataSetObserver);
        if (dataAdapter.getCount() > 0) {
            dataAdapter.notifyDataSetChanged();
        }
    }


    /**
     * 设置选择监听
     */
    @SuppressWarnings("unused")
    public void setOnSelectedChangedListener(OnSelectedChangedListener l) {
        this.mOnSelectedChangedListener = l;
    }


    /**
     * 设置当前显示子图
     *
     * @param position {@link ChildStatus}
     */
    @SuppressWarnings(value = {"unchecked", "rawtypes"})
    public void setChildDraw(ChildStatus position) {
        if (childDrawPosition.getStatu() != position.getStatu()) {
            childDrawPosition = position;
            childDraw = mChildDraws.get(position.getStatu());
            initRect();
            invalidate();
        }
    }

    /**
     * 隐藏子图
     */
    public void hideChildDraw() {
        childDrawPosition = ChildStatus.NONE;
        childDraw = null;
        initRect();
        invalidate();
    }

    /**
     * MA/BOLL切换及隐藏
     *
     * @param status {@link MainStatus}
     */
    public void changeMainDrawType(MainStatus status) {
        if (this.status != status) {
            this.status = status;
            invalidate();
        }
    }

    /**
     * 获取当前主图状态
     *
     * @return {@link MainStatus}
     */
    public MainStatus getStatus() {
        return super.getStatus();
    }

    /**
     * 设置分时线尾部呼吸灯最大半径
     *
     * @param endShadowLayerWidth
     */
    public void setEndShadowLayerWidth(int endShadowLayerWidth) {
        this.endShadowLayerWidth = endShadowLayerWidth;
    }
}
