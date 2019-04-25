package com.icechao.klinelib.draw;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;

/*************************************************************************
 * Description   :
 *
 * @PackageName  : com.icechao.klinelib.draw
 * @FileName     : DepthLabelDraw.java
 * @Author       : chao
 * @Date         : 2019/4/9
 * @Email        : icechliu@gmail.com
 * @version      : V1
 *************************************************************************/
public class DepthLabelDraw {

    private Paint labelPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private Paint selectedLabelPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private Paint selectedBoxPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private Paint selectedBoxBorderPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private int selectedBoxColor = Color.BLACK;
    private int selectedBoxBorderColor = Color.WHITE;
    private int selectedBoxPadding = 3;

    private float topPading;
    private float labelHeight;

    private boolean isShowLeftLabel = true;
    private boolean isShowRightLabel = true;
    private boolean isShowBottomLabel = true;

    private int xLabelCount;
    private int yLabelCount;

    private int xDecimal = 2;
    private int yDecimal = 2;
    private float selectedBorderWitdh = 1;

    public void setLabelColor(int labelColor) {
        labelPaint.setColor(labelColor);
    }

    public void setxLabelCount(int xLabelCount) {
        this.xLabelCount = xLabelCount;
    }

    public void setyLabelCount(int yLabelCount) {
        this.yLabelCount = yLabelCount;
    }

    public DepthLabelDraw(int xLabelCount, int yLabelCount) {
        this.xLabelCount = xLabelCount;
        this.yLabelCount = yLabelCount;

        selectedBoxPaint.setColor(selectedBoxColor);
        selectedBoxPaint.setStyle(Paint.Style.FILL);
        selectedBoxBorderPaint.setColor(selectedBoxBorderColor);
        labelPaint.setStyle(Paint.Style.STROKE);
        selectedBoxBorderPaint.setStyle(Paint.Style.STROKE);
        selectedLabelPaint.setTextAlign(Paint.Align.CENTER);


    }

    private float width;
    private float height;

    public float getWidth() {
        return width;
    }

    public void setWidth(float width) {
        this.width = width;
    }

    public float getHeight() {
        return height;
    }

    public void setHeight(float height) {
        this.height = height;
    }

    public void drawLabels(Canvas canvas, float xMax, float yMax, float xMin, float yMin) {
        drawXlable(canvas, xMax, xMin);
        drawYLable(canvas, yMax, yMin);
    }

    private void drawYLable(Canvas canvas, float yMax, float yMin) {
        float[] yLabels = calclabels(yMax, yMin, yLabelCount);
        float yInterval = (height - topPading - labelHeight) / (yLabelCount - 1);
        if (isShowLeftLabel) {
            for (int i = 1; i < yLabels.length; i++) {
                labelPaint.setTextAlign(Paint.Align.LEFT);
                canvas.drawText(String.format("%." + yDecimal + "f", yLabels[i]), 0, fixTextYBaseBottom(height - labelHeight - i * yInterval), labelPaint);
            }
        }
        if (isShowRightLabel) {
            for (int i = 1; i < yLabels.length; i++) {
                labelPaint.setTextAlign(Paint.Align.RIGHT);
                canvas.drawText(String.format("%." + yDecimal + "f", yLabels[i]), width, fixTextYBaseBottom(height - labelHeight - i * yInterval), labelPaint);
            }
        }

    }

    private float[] calclabels(float max, float min, int count) {
        float[] labels = new float[count];
        float temp = (max - min) / (count - 1);
        for (int i = 0; i < labels.length; i++) {
            labels[i] = min + temp * i;
        }
        return labels;

    }

    private void drawXlable(Canvas canvas, float xMax, float xMin) {
        float[] calclabels = calclabels(xMax, xMin, xLabelCount);
        float x = 0f;
        float xInterval = width / (xLabelCount - 1);
        if (isShowBottomLabel) {
            float y = fixTextYBaseBottom(height - labelHeight / 2);

            for (int i = 0; i < calclabels.length; i++) {
                if (i == 0) {
                    labelPaint.setTextAlign(Paint.Align.LEFT);
                } else if (i == calclabels.length - 1) {
                    labelPaint.setTextAlign(Paint.Align.RIGHT);
                    x = width;
                } else {
                    labelPaint.setTextAlign(Paint.Align.CENTER);
                    x = xInterval * i;
                }
                canvas.drawText(String.format("%." + xDecimal + "f", calclabels[i]), x, y, labelPaint);

            }
        }
    }

    /**
     * 绘制选中的lable
     *
     * @param floats x,y ,x position,y position
     */
    public void drawSelectedLables(Canvas canvas, float[] floats) {
        //X轴lablel
        selectedLabelPaint.setTextAlign(Paint.Align.CENTER);
        String xValue = String.format("%." + xDecimal + "f", floats[0]);
        float textWidth = selectedLabelPaint.measureText(xValue) + 10;
        float halfTextWidth = textWidth / 2;
        float halfSelectedBorder = selectedBorderWitdh / 2;
        if (floats[2] < halfTextWidth + halfSelectedBorder) {
            floats[2] = halfTextWidth + halfSelectedBorder;
        }
        if (floats[2] > width - halfTextWidth - halfSelectedBorder) {
            floats[2] = width - halfTextWidth - halfSelectedBorder;
        }
        float left = floats[2] - halfTextWidth;
        float right = floats[2] + halfTextWidth;
        float top = height - labelHeight + halfSelectedBorder;
        float bottom = height - halfSelectedBorder;
        canvas.drawRect(new RectF(left, top, right, bottom), selectedBoxPaint);
        canvas.drawRect(new RectF(left, top, right, bottom), selectedBoxBorderPaint);
        canvas.drawText(xValue, floats[2], fixTextYBaseBottom(height - labelHeight / 2), selectedLabelPaint);
        //Y轴label

        String yValue = String.format("%." + xDecimal + "f", floats[1]);
        textWidth = selectedLabelPaint.measureText(yValue) + 5;
        selectedLabelPaint.setTextAlign(Paint.Align.RIGHT);
        left = width - textWidth - selectedBoxPadding * 2 - selectedBorderWitdh;
        right = width - halfSelectedBorder;
        top = floats[3] - labelHeight / 2 + halfSelectedBorder;
        bottom = floats[3] + labelHeight / 2 - halfSelectedBorder;
        canvas.drawRect(new RectF(left, top, right, bottom), selectedBoxPaint);
        canvas.drawRect(new RectF(left, top, right, bottom), selectedBoxBorderPaint);
        canvas.drawText(yValue, width - selectedBorderWitdh - selectedBoxPadding, fixTextYBaseBottom(floats[3]), selectedLabelPaint);


    }


    public void setTopPading(float topPading) {
        this.topPading = topPading;
    }

    public void setLabelHeight(float labelHeight) {
        this.labelHeight = labelHeight;
    }

    /**
     * 解决text居中的问题
     */
    public float fixTextYBaseBottom(float y) {

        return y + textBaseLine;
    }

    public void setSelectedLabelColor(int selectedLabelColor) {
        selectedLabelPaint.setColor(selectedLabelColor);
    }

    public void setSelectedBoxColor(int selectedBoxColor) {
        this.selectedBoxColor = selectedBoxColor;
    }

    public void setSelectedBoxBorderColor(int selectedBoxBorderColor) {
        this.selectedBoxBorderColor = selectedBoxBorderColor;
    }

    public void setSelectedBoxPadding(int selectedBoxPadding) {
        this.selectedBoxPadding = selectedBoxPadding;
    }

    public void setShowLeftLabel(boolean showLeftLabel) {
        isShowLeftLabel = showLeftLabel;
    }

    public void setShowRightLabel(boolean showRightLabel) {
        isShowRightLabel = showRightLabel;
    }

    public void setShowBottomLabel(boolean showBottomLabel) {
        isShowBottomLabel = showBottomLabel;
    }

    public void setxDecimal(int xDecimal) {
        this.xDecimal = xDecimal;
    }

    public void setyDecimal(int yDecimal) {
        this.yDecimal = yDecimal;
    }

    public void setSelectedBorderWitdh(float selectedBorderWitdh) {
        this.selectedBorderWitdh = selectedBorderWitdh;
        selectedBoxPaint.setStrokeWidth(selectedBorderWitdh);
    }

    private float textBaseLine;

    public void setTextSize(int textSize) {
        labelPaint.setTextSize(textSize);
        selectedLabelPaint.setTextSize(textSize);
        Paint.FontMetrics fontMetrics = labelPaint.getFontMetrics();
        textBaseLine = (fontMetrics.descent - fontMetrics.ascent) / 2 - fontMetrics.descent;
    }
}
