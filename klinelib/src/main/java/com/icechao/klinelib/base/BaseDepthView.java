package com.icechao.klinelib.base;

import android.content.Context;
import android.database.DataSetObserver;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.os.Build;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import com.icechao.klinelib.draw.DepthDraw;
import com.icechao.klinelib.draw.DepthLabelDraw;
import com.icechao.klinelib.utils.Constants;


/*************************************************************************
 * Description   :
 *
 * @PackageName  : com.icechao.klinelib.draw
 * @FileName     : BaseDepth.java
 * @Author       : chao
 * @Date         : 2019/4/8
 * @Email        : icechliu@gmail.com
 * @version      : V1
 *************************************************************************/
public class BaseDepthView extends View implements View.OnTouchListener {

    private BaseDepthAdapter dataAdapter;

    private float labelHeight = 40;

    private boolean isShowLegent = true;
    private float legentHeight = 10;
    private float defaultPadding = 8;
    private float legentTextSize = 25;

    private int leftColor = Color.GREEN;
    private int rightColor = Color.RED;
    private int legentTextColor = Color.GRAY;
    private boolean isColorSameAsLegent;
    private boolean isSelected;

    private String leftLegentText;
    private String rightLegentText;

    private int backGroundColor = Color.DKGRAY;
    private Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);

    protected int height;
    protected int width;
    private DepthDraw depthDraw;
    private DepthLabelDraw labelDraw;
    private DataSetObserver observer;
    private float selectedPointX;

    public BaseDepthView(Context context) {
        super(context);
        initView(context);
    }

    public BaseDepthView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initView(context);
    }

    public BaseDepthView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

    }


    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public BaseDepthView(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        initView(context);
    }

    @SuppressWarnings("all")
    private void initView(Context context) {

        setOnTouchListener(this);
        labelDraw = new DepthLabelDraw(5, 5);
        depthDraw = new DepthDraw();

        paint.setTextSize(legentTextSize);
        paint.setStyle(Paint.Style.FILL);
        observer = new DataSetObserver() {
            @Override
            public void onChanged() {
                super.onChanged();
                calcBaseValues();
                invalidate();
            }
        };
    }

    private void calcBaseValues() {

    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        height = getMeasuredHeight();
        width = getMeasuredWidth();
        labelDraw.setHeight(height);
        labelDraw.setWidth(width);
        depthDraw.setWidth(width);
        depthDraw.setHeight(height);

    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        drawBackground(canvas);
        if (isShowLegent) {
            drawLegent(canvas);
        }

        if (dataAdapter != null && dataAdapter.getCount() > 0) {
            //基础深度图
            float[] tempLeftDatas = dataAdapter.getTempLeftDatas();
            float[] tempRightDatas = dataAdapter.getTempRightDatas();
            //坐标系
            labelDraw.drawLabels(canvas,
                    dataAdapter.getMaxIndex(), dataAdapter.getMaxValue(),
                    dataAdapter.getMinIndex(), dataAdapter.getMinValue());
            depthDraw.drawDepth(canvas, tempLeftDatas, tempRightDatas,
                    dataAdapter.getMaxValue(), dataAdapter.getMinValue(),
                    dataAdapter.getMaxIndex(), dataAdapter.getMinIndex());
            //选中
            if (isSelected) {
                labelDraw.drawSelectedLables(canvas, depthDraw.drawSelected(canvas, selectedPointX, tempLeftDatas, tempRightDatas, dataAdapter.getMinValue()));
            }

        }


    }

    /**
     * 绘制深度图图例
     *
     * @param canvas canvas
     */
    private void drawLegent(Canvas canvas) {
        float halfWidth = width >> 1;
        float legentTop;
        float leftLegentLeft;
        float leftLegentRight;
        float legentBottom;
        float rightLegentLeft = halfWidth + defaultPadding;
        float rightLegentRight = halfWidth + defaultPadding + legentHeight;
        if (TextUtils.isEmpty(rightLegentText) || TextUtils.isEmpty(leftLegentText)) {
            legentTop = defaultPadding;
            leftLegentLeft = halfWidth - legentHeight - defaultPadding;
            leftLegentRight = halfWidth - defaultPadding;
            legentBottom = legentHeight + defaultPadding;

        } else {


            float leftWidth = paint.measureText(leftLegentText);

            leftLegentLeft = halfWidth - defaultPadding * 2 - leftWidth - legentHeight;
            leftLegentRight = halfWidth - defaultPadding * 2 - leftWidth;

            legentTop = defaultPadding;
            legentBottom = defaultPadding + legentHeight;
            //绘制图例文字

            float rightTextX = rightLegentRight + defaultPadding;
            float leftTextX = leftLegentRight + defaultPadding;
            float textY = fixTextY(defaultPadding + legentHeight / 2);


            if (isColorSameAsLegent) {
                paint.setColor(leftColor);
                canvas.drawText(leftLegentText, leftTextX, textY, paint);
                paint.setColor(rightColor);
                canvas.drawText(rightLegentText, rightTextX, textY, paint);
            } else {
                paint.setColor(legentTextColor);
                canvas.drawText(leftLegentText, leftTextX, textY, paint);
                canvas.drawText(rightLegentText, rightTextX, textY, paint);
            }

        }
        paint.setColor(leftColor);
        canvas.drawRect(new RectF(leftLegentLeft, legentTop, leftLegentRight, legentBottom), paint);
        paint.setColor(rightColor);
        canvas.drawRect(new RectF(rightLegentLeft, legentTop, rightLegentRight, legentBottom), paint);

        depthDraw.setTopPadding(40);
        labelDraw.setTopPading(40);
    }

    /**
     * 解决text居中的问题
     */
    public float fixTextY(float y) {
        Paint.FontMetrics fontMetrics = paint.getFontMetrics();
        return (y + (fontMetrics.descent - fontMetrics.ascent) / 2 - fontMetrics.descent);
    }

    /**
     * 绘制深度图背景
     *
     * @param canvas canvas
     */
    private void drawBackground(Canvas canvas) {
        paint.setStyle(Paint.Style.FILL);
        paint.setColor(backGroundColor);
        canvas.drawRect(new RectF(0, 0, width, height), paint);
    }

    @SuppressWarnings("all")
    public void setDataAdapter(BaseDepthAdapter dataAdapter) {
        if (null != this.dataAdapter) {
            dataAdapter.unregisterDataSetObserver(observer);
        }
        this.dataAdapter = dataAdapter;
        dataAdapter.registerDataSetObserver(observer);
    }

    @SuppressWarnings("all")
    public void setShowLegent(boolean showLegent) {
        isShowLegent = showLegent;
    }

    @SuppressWarnings("all")
    public void setLegentHeight(float legentHeight) {
        this.legentHeight = legentHeight;
    }

    @SuppressWarnings("all")
    public void setDefaultPadding(float defaultPadding) {
        this.defaultPadding = defaultPadding;
    }

    @SuppressWarnings("all")
    public void setLegentTextSize(float legentTextSize) {
        this.legentTextSize = legentTextSize;
    }

    /**
     * 左侧图例文字颜色
     *
     * @param legentTextColor
     */
    @SuppressWarnings("all")
    public void setLegentTextColor(int legentTextColor) {
        this.legentTextColor = legentTextColor;
    }

    /**
     * 文本颜色是否和图例相同
     *
     * @param colorSameAsLegent
     */
    @SuppressWarnings("all")
    public void setLegnetTextColorSameAsLegent(boolean colorSameAsLegent) {
        isColorSameAsLegent = colorSameAsLegent;
    }

    /**
     * 左侧图例文本
     *
     * @param leftLegentText text
     */
    public void setLeftLegentText(String leftLegentText) {
        this.leftLegentText = leftLegentText;
    }

    /**
     * 右侧图例文本
     *
     * @param rightLegentText text
     */
    public void setRightLegentText(String rightLegentText) {
        this.rightLegentText = rightLegentText;
    }

    /**
     * 背景颜色
     *
     * @param backGroundColor 背景颜色
     */
    public void setBackGroundColor(int backGroundColor) {
        this.backGroundColor = backGroundColor;
    }

    private float startX;
    private float startY;

    private long startTime;

    private Runnable longPressRunnable = new Runnable() {
        @Override
        public void run() {
            isSelected = changeState;
            invalidate();
        }
    };

    private boolean changeState;


    @Override
    public boolean onTouch(View v, MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                changeState = true;
                startTime = System.currentTimeMillis();
                postDelayed(longPressRunnable, Constants.LONG_PRESS_TIME);
                startX = event.getX();
                startY = event.getY();
                selectedPointX = startX;
                break;
            case MotionEvent.ACTION_UP:
                if (System.currentTimeMillis() - startTime < Constants.LONG_PRESS_TIME) {
                    isSelected = false;
                    removeCallbacks(longPressRunnable);
                }
                invalidate();
                break;
            case MotionEvent.ACTION_MOVE:
                float x = event.getX();
                float y = event.getY();
                float diffX = Math.abs(x - startX);
                float diffY = Math.abs(y - startY);
                if (!isSelected && (diffX > 10 || diffY > 10 ||
                        (System.currentTimeMillis() - startTime > Constants.LONG_PRESS_TIME))) {
                    changeState = false;
                }
                startX = x;
                startY = y;
                selectedPointX = startX;
                invalidate();
                break;
        }
        return true;
    }


    /**
     * 左侧线和图例的颜色
     *
     * @param leftColor color
     */
    public void setLeftColor(int leftColor) {
        depthDraw.setLeftColor(leftColor);
        this.leftColor = leftColor;
    }

    /**
     * 左侧区域颜色
     *
     * @param leftAreaColor Color
     */
    public void setLeftAreaColor(int leftAreaColor) {
        depthDraw.setLeftAreaColor(leftAreaColor);

    }

    /**
     * 右侧线和图例颜色
     *
     * @param rightColor Color
     */
    public void setRightColor(int rightColor) {
        depthDraw.setRightColor(rightColor);
        this.rightColor = rightColor;

    }

    /**
     * 右侧区域颜色
     *
     * @param rightAreaColor Color
     */
    public void setRightAreaColor(int rightAreaColor) {
        depthDraw.setRightAreaColor(rightAreaColor);
    }


    /**
     * 先中实心点半径
     *
     * @param selectedPointRadius 半径
     */
    public void setSelectedPointRadius(float selectedPointRadius) {
        depthDraw.setSelectedPointRadius(selectedPointRadius);
    }

    /**
     * 选中外圆半径
     *
     * @param selectedCircleRadius 半径
     */
    public void setSelectedCircleRadius(float selectedCircleRadius) {
        depthDraw.setSelectedCircleRadius(selectedCircleRadius);
    }

    /**
     * 选中外圆线宽
     *
     * @param selectedCricleRadiusWidth 线宽
     */
    public void setSelectedCricleRadiusWidth(float selectedCricleRadiusWidth) {
        depthDraw.setSelectedCricleRadiusWidth(selectedCricleRadiusWidth);
    }

    /**
     * 深度线宽
     *
     * @param depthLineWidth 线宽
     */
    public void setDepthLineWidth(float depthLineWidth) {
        depthDraw.setDepthLineWidth(depthLineWidth);
    }


    /**
     * 选中的label颜色
     *
     * @param selectedLabelColor Color
     */
    public void setSelectedLabelColor(int selectedLabelColor) {
        labelDraw.setSelectedLabelColor(selectedLabelColor);
    }

    /**
     * 选中框的填充色
     *
     * @param selectedBoxColor Color
     */
    public void setSelectedBoxColor(int selectedBoxColor) {
        labelDraw.setSelectedBoxColor(selectedBoxColor);
    }

    /**
     * 选中框的边框颜色
     *
     * @param selectedBoxBorderColor Color
     */
    public void setSelectedBoxBorderColor(int selectedBoxBorderColor) {
        labelDraw.setSelectedBoxBorderColor(selectedBoxBorderColor);
    }

    /**
     * 选中框的padding
     *
     * @param selectedBoxPadding paddint
     */
    @SuppressWarnings("all")
    public void setSelectedBoxPadding(int selectedBoxPadding) {
        labelDraw.setSelectedBoxPadding(selectedBoxPadding);
    }

    /**
     * 是否显示左label
     *
     * @param showLeftLabel 是否显示
     */
    public void setShowLeftLabel(boolean showLeftLabel) {
        labelDraw.setShowLeftLabel(showLeftLabel);
    }

    /**
     * 是否显示右label
     *
     * @param showRightLabel 是否显示
     */
    @SuppressWarnings("all")
    public void setShowRightLabel(boolean showRightLabel) {
        labelDraw.setShowRightLabel(showRightLabel);
    }

    /**
     * 是否显示底label
     *
     * @param showBottomLabel 是否显示
     */
    @SuppressWarnings("all")
    public void setShowBottomLabel(boolean showBottomLabel) {
        labelDraw.setShowBottomLabel(showBottomLabel);
    }

    /**
     * 选中框边框
     *
     * @param selectedBorderWitdh 宽度
     */
    public void setSelectedBorderWitdh(float selectedBorderWitdh) {
        labelDraw.setSelectedBorderWitdh(selectedBorderWitdh);
    }

    public void setLabelColor(int labelColor) {
        labelDraw.setLabelColor(labelColor);
    }

    @SuppressWarnings("all")
    public void setxLabelCount(int xLabelCount) {
        labelDraw.setxLabelCount(xLabelCount);
    }

    @SuppressWarnings("all")
    public void setyLabelCount(int yLabelCount) {
        labelDraw.setyLabelCount(yLabelCount);
    }

    public void setLabelHeight(float labelHeight) {
        labelDraw.setLabelHeight(labelHeight);
        depthDraw.setBottomPadding(labelHeight);
        labelDraw.setLabelHeight(labelHeight);
    }

    @SuppressWarnings("all")
    public void setColorSameAsLegent(boolean colorSameAsLegent) {
        isColorSameAsLegent = colorSameAsLegent;
    }

    public void setTextLabelTextSize(int size) {
        labelDraw.setTextSize(size);
    }
}
