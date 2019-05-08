package com.icechao.klinelib.draw;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;

/*************************************************************************
 * Description   : 修改,不为index为基础绘制X,而是为X坐标为基础绘制
 *
 * @PackageName  : com.icechao.klinelib.draw
 * @FileName     : DepthDraw.java
 * @Author       : chao
 * @Date         : 2019/4/9
 * @Email        : icechliu@gmail.com
 * @version      : V1
 *************************************************************************/
public class DepthDraw {

    private Paint depthPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private Paint areaPaint = new Paint(Paint.ANTI_ALIAS_FLAG);

    private int leftColor = Color.GREEN;
    private int leftAreaColor = Color.parseColor("#005000");
    private int rightColor = Color.RED;
    private int rightAreaColor = Color.parseColor("#500000");

    private Paint selectedPaint = new Paint(Paint.ANTI_ALIAS_FLAG);

    private float selectedPointRadius = 2;
    private float selectedCircleRadius = 8;
    private float selectedCricleRadiusWidth = 2;

    private double height;
    private double width;
    private double widthInterval;
    private double heightInterval;
    private double topPadding;
    private double bottomPadding;

    public void setHeight(float height) {
        this.height = height;
    }

    public void setWidth(float width) {
        this.width = width;
    }

    public DepthDraw() {
        depthPaint.setAntiAlias(true);
        depthPaint.setStyle(Paint.Style.STROKE);
    }


    /**
     * 绘制深度数据,需要提前将数据加工好 数组中  基数为X  偶数为Y
     *
     * @param leftDatas
     * @param rightDatas
     * @param maxY
     */
    public void drawDepth(Canvas canvas, float[] leftDatas, float[] rightDatas, float maxY, float minY, float maxX, float minX) {
        if (null == leftDatas && null == rightDatas) {
            return;
        }
        widthInterval = width / (maxX - minX);
        double depthBottom = this.height - bottomPadding;
        heightInterval = (depthBottom - topPadding) / (maxY - minY);

        Path leftLine = new Path();
        Path leftArea = new Path();
        Path rightLine = new Path();
        Path rightArea = new Path();
        float rightStart = 0;

        if (null != leftDatas && leftDatas.length > 0) {

            leftArea.moveTo(0, (float) depthBottom);
            leftLine.moveTo(0, (float) (depthBottom - ((leftDatas[1] - minY) * heightInterval)));
            leftArea.lineTo(0, (float) (depthBottom - ((leftDatas[1] - minY) * heightInterval)));
            for (int i = 2; i < leftDatas.length - 1; i += 2) {
                float tempX = (float) ((leftDatas[i] - minX) * widthInterval);
                float tempY = (float) (depthBottom - ((leftDatas[i + 1] - minY) * heightInterval));
                leftLine.lineTo(tempX, tempY);
                leftArea.lineTo(tempX, tempY);
            }
            rightStart = (float) ((leftDatas.length - 2) / 2 * widthInterval);
            leftArea.lineTo(rightStart, (float) depthBottom);
            leftArea.close();

            //绘制左侧的区域
            areaPaint.setColor(leftAreaColor);
            canvas.drawPath(leftArea, areaPaint);
            //绘制左侧的线
            depthPaint.setColor(leftColor);
            canvas.drawPath(leftLine, depthPaint);
        }

        if (null != rightDatas && rightDatas.length > 0) {
            //配置右侧path
            float tempStart = (float) (rightDatas[0] * widthInterval);
            float y = (float) (depthBottom - ((rightDatas[1] - minY) * heightInterval));
            rightLine.moveTo(tempStart, y);
            rightArea.moveTo(tempStart, (float) depthBottom);
            rightArea.lineTo(tempStart, y);
            for (int i = 0; i < rightDatas.length - 1; i += 2) {
                float tempX = (float) ((rightDatas[i] - minX) * widthInterval);
                float tempY = (float) (depthBottom - ((rightDatas[i + 1] - minY) * heightInterval));
                rightLine.lineTo(tempX, tempY);
                rightArea.lineTo(tempX, tempY);
            }
            rightArea.lineTo((float) width, (float) depthBottom);
            rightArea.close();

            //绘制右侧的区域
            areaPaint.setColor(rightAreaColor);
            canvas.drawPath(rightArea, areaPaint);
            //绘制右侧的线
            depthPaint.setColor(rightColor);
            canvas.drawPath(rightLine, depthPaint);
        }
    }

    public void setTopPadding(float padding) {
        this.topPadding = padding;
    }

    public void setBottomPadding(float bottomPadding) {
        this.bottomPadding = bottomPadding;
    }

    public float[] drawSelected(Canvas canvas, float selectedPointX, float[] tempLeftDatas, float[] tempRightDatas, float minY) {

        float leftWidth = (float) ((tempLeftDatas[tempLeftDatas.length - 2] - tempLeftDatas[0]) * widthInterval);
        float noDataWidth = (float) ((tempRightDatas[0] - tempLeftDatas[tempLeftDatas.length - 2]) * widthInterval);

        float y;
        float x;
        if (leftWidth + noDataWidth / 2 >= selectedPointX) {
            int i = (int) (selectedPointX / widthInterval + 0.5);
            i = getIndex(tempLeftDatas, i);
            y = tempLeftDatas[i + 1];
            x = tempLeftDatas[i];
            selectedPaint.setColor(leftColor);

        } else {
            int i = (int) (((selectedPointX - leftWidth - noDataWidth) / widthInterval) + 0.5);
            i = getIndex(tempRightDatas, i);
            y = tempRightDatas[i + 1];
            x = tempRightDatas[i];
            selectedPaint.setColor(rightColor);
        }


        float positionY = (float) (height - bottomPadding - (y - minY) * heightInterval);

        float positionX = (float) ((x - tempLeftDatas[0]) * widthInterval);

        selectedPaint.setStyle(Paint.Style.FILL);
        canvas.drawCircle(positionX, positionY, selectedPointRadius, selectedPaint);
        selectedPaint.setStyle(Paint.Style.STROKE);
        selectedPaint.setStrokeWidth(selectedCricleRadiusWidth);
        canvas.drawCircle(positionX, positionY, selectedCircleRadius, selectedPaint);
        return new float[]{x, y, positionX, positionY};
    }

    /**
     * 校验拿到数据值
     *
     * @param tempDatas
     * @param i
     * @return
     */
    private int getIndex(float[] tempDatas, int i) {
        if (i < 0) {
            i = 0;
        }
        if (i % 2 == 1) {
            i -= 1;
        }
        return i;
    }


    /**
     * 左侧线的颜色
     *
     * @param leftColor
     */
    public void setLeftColor(int leftColor) {
        this.leftColor = leftColor;
    }

    /**
     * 左侧区域颜色
     *
     * @param leftAreaColor
     */
    public void setLeftAreaColor(int leftAreaColor) {
        this.leftAreaColor = leftAreaColor;
    }

    /**
     * 右侧线颜色
     *
     * @param rightColor
     */
    public void setRightColor(int rightColor) {
        this.rightColor = rightColor;
    }

    /**
     * 右侧区域颜色
     *
     * @param rightAreaColor
     */
    public void setRightAreaColor(int rightAreaColor) {
        this.rightAreaColor = rightAreaColor;
    }

    /**
     * 先中实心点半径
     *
     * @param selectedPointRadius
     */
    public void setSelectedPointRadius(float selectedPointRadius) {
        this.selectedPointRadius = selectedPointRadius;
    }

    /**
     * 选中外圆半径
     *
     * @param selectedCircleRadius
     */
    public void setSelectedCircleRadius(float selectedCircleRadius) {
        this.selectedCircleRadius = selectedCircleRadius;
    }

    /**
     * 选中外圆线宽
     *
     * @param selectedCricleRadiusWidth
     */
    public void setSelectedCricleRadiusWidth(float selectedCricleRadiusWidth) {
        this.selectedCricleRadiusWidth = selectedCricleRadiusWidth;
        selectedPaint.setStrokeWidth(selectedCricleRadiusWidth);
    }

    /**
     * 深度线宽
     *
     * @param depthLineWidth
     */
    public void setDepthLineWidth(float depthLineWidth) {
        depthPaint.setStrokeWidth(depthLineWidth);
    }
}
