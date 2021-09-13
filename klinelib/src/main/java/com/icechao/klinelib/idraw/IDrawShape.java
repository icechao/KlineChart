package com.icechao.klinelib.idraw;

import android.graphics.Canvas;

public interface IDrawShape {

    void onTouchMove(double x, double y, double distanceX, double distanceY);

    void onTouchUp(double x, double y);

    void onTouchDown(double x, double y);

    void renderShape(Canvas canvas, float chartLeft, float chartRight, int top, int bottom);

    void scaleChange(float scaleX, float oldScaleX);

    void touchDown(double x, double y);

    boolean touchMove(double x, double y, double distanceX, double distanceY);

    boolean touchUp(double x, double y);

    void render(Canvas canvas, float chartLeft, float chartRight, int top, int bottom);

}
