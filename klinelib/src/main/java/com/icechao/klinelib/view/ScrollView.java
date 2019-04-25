package com.icechao.klinelib.view;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.widget.NestedScrollView;
import android.util.AttributeSet;
import android.view.MotionEvent;

/*************************************************************************
 * Description   : 不拦截横向滑动的ScrollView
 *
 * @PackageName  : com.icechao.klinelib.view
 * @FileName     : ScrollView.java
 * @Author       : chao
 * @Date         : 2019/4/10
 * @Email        : icechliu@gmail.com
 * @version      : V1
 *************************************************************************/
public class ScrollView extends NestedScrollView {

    private boolean isOldEvent;

    public ScrollView(@NonNull Context context) {
        super(context);
    }

    public ScrollView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public ScrollView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    private float downX;
    private float downY;

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        if (isOldEvent) {
            return false;
        }
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                downX = ev.getX();
                downY = ev.getY();
                break;
            case MotionEvent.ACTION_MOVE:
                float moveX = ev.getX();
                float moveY = ev.getY();
                float difX = Math.abs(moveX - downX);
                float difY = Math.abs(moveY - downY);
                if (difX > difY) {
                    downX = moveX;
                    downY = moveY;
                    isOldEvent = true;
                    return false;
                }
                downX = moveX;
                downY = moveY;
                break;
            case MotionEvent.ACTION_UP:
                break;
        }
        return super.onInterceptTouchEvent(ev);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        switch (ev.getAction()) {
            case MotionEvent.ACTION_UP:
                isOldEvent = false;
                break;
        }

        return super.dispatchTouchEvent(ev);
    }


}

