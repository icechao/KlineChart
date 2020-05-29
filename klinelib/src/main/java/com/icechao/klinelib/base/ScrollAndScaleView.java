package com.icechao.klinelib.base;

import android.content.Context;
import android.support.v4.view.GestureDetectorCompat;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.widget.OverScroller;
import android.widget.RelativeLayout;

import com.icechao.klinelib.utils.Status;

/*************************************************************************
 * Description   :
 *
 * @PackageName  : com.icechao.klinelib.utils
 * @FileName     : ScrollAndScaleView.java
 * @Author       : chao
 * @Date         : 2019/4/8
 * @Email        : icechliu@gmail.com
 * @version      : V1
 *************************************************************************/
public abstract class ScrollAndScaleView extends RelativeLayout implements
        GestureDetector.OnGestureListener,
        ScaleGestureDetector.OnScaleGestureListener {
    protected int scrollX = 0;
    protected GestureDetectorCompat gestureDetector;
    protected ScaleGestureDetector scaleDetector;

    protected boolean showSelected = false;

    protected int selectedIndex = -1;

    protected OverScroller overScroller;

    protected boolean touch = false;

    protected float scaleX = 1;

    protected float scaleXMax = 2f;

    protected float scaleXMin = 0.5f;

    private boolean isMultipleTouch = false;

    private boolean isScrollEnable = true;

    private boolean isScaleEnable = true;

    public ScrollAndScaleView(Context context) {
        super(context);
        init();
    }

    public ScrollAndScaleView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public ScrollAndScaleView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        setWillNotDraw(false);
        gestureDetector = new GestureDetectorCompat(getContext(), this);
        scaleDetector = new ScaleGestureDetector(getContext(), this);
        overScroller = new OverScroller(getContext());

    }

    @Override
    public boolean onDown(MotionEvent e) {
        return false;
    }

    @Override
    public void onShowPress(MotionEvent e) {

    }

    private boolean isTapShow;

    protected Status.ShowCrossModel modle = Status.ShowCrossModel.SELECT_BOTH;

    @Override
    public boolean onSingleTapUp(MotionEvent e) {

        switch (modle) {
            default:
            case SELECT_PRESS:
                showSelected = false;
                return true;
            case SELECT_BOTH:
                if (!isTapShow && showSelected) {
                    showSelected = false;
                    isTapShow = false;
                } else {
                    isTapShow = true;
                    showSelected = true;
                    onSelectedChange(e);
                }
                return true;
            case SELECT_TOUCHE:
                showSelected = true;
                onSelectedChange(e);
                return true;
        }
    }

    @Override
    public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
        if (isTapShow) {
            showSelected = false;
            isTapShow = false;
        }
        if (!showSelected && !isMultipleTouch() && isScrollEnable()) {
            scrollBy(Math.round(distanceX), 0);
            return true;
        }
        return false;
    }

    @Override
    public void onLongPress(MotionEvent e) {
        if (modle == Status.ShowCrossModel.SELECT_PRESS || modle == Status.ShowCrossModel.SELECT_BOTH) {
            showSelected = true;
            onSelectedChange(e);
        }
    }


    public abstract void onSelectedChange(MotionEvent e);

    @Override
    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {

        if (!showSelected && !isTouch() && isScrollEnable()) {
            overScroller.fling(scrollX, 0
                    , Math.round(velocityX / scaleX / 2), 0,
                    Integer.MIN_VALUE, Integer.MAX_VALUE,
                    0, 0);
        }
        return true;
    }

    @Override
    public void computeScroll() {
        if (overScroller.computeScrollOffset()) {
            if (!isTouch() && isScrollEnable()) {
                scrollTo(overScroller.getCurrX(), overScroller.getCurrY());
            } else {
                overScroller.forceFinished(true);
            }
            invalidate();
        }
    }

    @Override
    public void scrollBy(int x, int y) {
        if (isScrollEnable()) {
            scrollTo(scrollX - Math.round(x / scaleX), 0);
        } else {
            overScroller.forceFinished(true);
        }

    }

    @Override
    public void scrollTo(int x, int y) {
        if (isScrollEnable()) {
            int oldX = scrollX;
            scrollX = x;
            if (scrollX != oldX) {
                onScrollChanged(scrollX, 0, oldX, 0);
            }
        } else {
            overScroller.forceFinished(true);
        }

    }

    @Override
    public boolean onScale(ScaleGestureDetector detector) {
        if (!isScaleEnable()) {
            return false;
        }
        float oldScale = scaleX;
        scaleX *= detector.getScaleFactor();
        if (scaleX < scaleXMin) {
            scaleX = scaleXMin;
        } else if (scaleX > scaleXMax) {
            scaleX = scaleXMax;
        }
        onScaleChanged(scaleX, oldScale);

        return true;
    }

    public float getScaleX() {
        return this.scaleX;
    }

    protected void onScaleChanged(float scale, float oldScale) {
//        invalidate();
    }

    @Override
    public boolean onScaleBegin(ScaleGestureDetector detector) {
        return true;
    }

    @Override
    public void onScaleEnd(ScaleGestureDetector detector) {

    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        if (event.getPointerCount() > 1) {
            showSelected = false;
            selectedIndex = -1;
        }
        if (null != eventLisenter) {
            eventLisenter.onEvent();
        }
        switch (event.getAction() & MotionEvent.ACTION_MASK) {
            case MotionEvent.ACTION_DOWN:
                touch = true;
                break;
            case MotionEvent.ACTION_MOVE:
                //长按之后移动
                if (showSelected) {
                    onSelectedChange(event);
                }
                break;
            case MotionEvent.ACTION_POINTER_UP:
                invalidate();
                break;
            case MotionEvent.ACTION_UP:

                touch = false;
                invalidate();
                break;
            case MotionEvent.ACTION_CANCEL:
                showSelected = false;
                selectedIndex = -1;
                touch = false;
                invalidate();
                break;
            default:
                break;
        }
        isMultipleTouch = event.getPointerCount() > 1;
        this.gestureDetector.onTouchEvent(event);
        this.scaleDetector.onTouchEvent(event);
        return true;
    }

    /**
     * 是否在触摸中
     *
     * @return
     */
    public boolean isTouch() {
        return touch;
    }

    /**
     * 设置ScrollX
     *
     * @param scrollX
     */
    public void setScrollX(int scrollX) {
        this.scrollX = scrollX;
        scrollTo(scrollX, 0);
    }

    /**
     * 是否是多指触控
     *
     * @return
     */
    public boolean isMultipleTouch() {
        return isMultipleTouch;
    }


    public float getScaleXMax() {
        return scaleXMax;
    }

    public float getScaleXMin() {
        return scaleXMin;
    }

    public boolean isScrollEnable() {
        return isScrollEnable;
    }

    public boolean isScaleEnable() {
        return isScaleEnable;
    }

    /**
     * 设置缩放的最大值
     */
    public void setScaleXMax(float scaleXMax) {
        this.scaleXMax = scaleXMax;
    }

    /**
     * 设置缩放的最小值
     */
    public void setScaleXMin(float scaleXMin) {
        this.scaleXMin = scaleXMin;
    }

    /**
     * 设置是否可以滑动
     */
    public void setScrollEnable(boolean scrollEnable) {
        isScrollEnable = scrollEnable;
    }

    /**
     * 设置是否可以缩放
     */
    public void setScaleEnable(boolean scaleEnable) {
        isScaleEnable = scaleEnable;
    }


    public void setEventLisenter(EventLisenter lisenter) {
        this.eventLisenter = lisenter;
    }

    public interface EventLisenter {
        void onEvent();
    }

    private EventLisenter eventLisenter;

}
