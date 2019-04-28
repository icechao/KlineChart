package com.icechao.klinelib.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.drawable.AnimationDrawable;
import android.support.annotation.ColorRes;
import android.support.annotation.DimenRes;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;

import com.icechao.klinelib.base.BaseKLineChartView;
import com.icechao.klinelib.draw.*;
import com.icechao.klinelib.draw.KDJDraw;
import com.icechao.klinelib.draw.MACDDraw;
import com.icechao.klinelib.draw.MainDraw;
import com.icechao.klinelib.draw.RSIDraw;
import com.icechao.klinelib.draw.WRDraw;
import com.icechao.klinelib.utils.Dputil;
import com.icechao.klinelib.R;

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
    private MainDraw mainDraw;
    private KDJDraw kdjDraw;
    private WRDraw wrDraw;
    private VolumeDraw volumeDraw;


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

        volumeDraw = new VolumeDraw(context);
        macdDraw = new MACDDraw(context, getResources().getColor(R.color.color_03C087), getResources().getColor(R.color.color_FF605A));
        wrDraw = new WRDraw(context);
        kdjDraw = new KDJDraw(context);
        rsiDraw = new RSIDraw(context);
        mainDraw = new MainDraw(context);
        addChildDraw(macdDraw);
        addChildDraw(kdjDraw);
        addChildDraw(rsiDraw);
        addChildDraw(wrDraw);
        setVolDraw(volumeDraw);
        setMainDraw(mainDraw);

        volumeDraw.setVolLeftColor(getResources().getColor(R.color.color_6D87A8));
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

        setBackgroundFillPaint(getContext().getResources().getColor(R.color.color_131F30));
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
        volumeDraw.setMa5Color(color);
    }

    /**
     * 设置ma10颜色
     *
     * @param color color
     */
    public void setMaTwoColor(int color) {
        mainDraw.setMaTwoColor(color);
        volumeDraw.setMa10Color(color);
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
        volumeDraw.setBarWidth(candleWidth);
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

    public void setRSI1Color(int color) {
        rsiDraw.setRSI1Color(color);
    }

    public void setRSI2Color(int color) {
        rsiDraw.setRSI2Color(color);
    }

    public void setRSI3Color(int color) {
        rsiDraw.setRSI3Color(color);
    }

    @Override
    public void setTextSize(float textSize) {
        super.setTextSize(textSize);
        mainDraw.setTextSize(textSize);
        rsiDraw.setTextSize(textSize);
        macdDraw.setTextSize(textSize);
        kdjDraw.setTextSize(textSize);
        wrDraw.setTextSize(textSize);
        volumeDraw.setTextSize(textSize);
    }

    public void setLineWidth(float lineWidth) {
        mainDraw.setLineWidth(lineWidth);
        rsiDraw.setLineWidth(lineWidth);
        macdDraw.setLineWidth(lineWidth);
        kdjDraw.setLineWidth(lineWidth);
        wrDraw.setLineWidth(lineWidth);
        volumeDraw.setLineWidth(lineWidth);
    }

    @Override
    public void setTextColor(int color) {
        super.setTextColor(color);
        mainDraw.setSelectorTextColor(color);
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
}
