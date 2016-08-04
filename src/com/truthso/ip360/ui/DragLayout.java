package com.truthso.ip360.ui;

import com.nineoldandroids.view.ViewHelper;
import com.truthso.ip360.utils.EvaluateUtil;

import android.content.Context;
import android.graphics.Color;
import android.graphics.PorterDuff.Mode;
import android.support.v4.view.ViewCompat;
import android.support.v4.widget.ViewDragHelper;
import android.support.v4.widget.ViewDragHelper.Callback;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

public class DragLayout extends FrameLayout {

    protected static final String TAG = DragLayout.class.getSimpleName();
    private ViewDragHelper mDragHelper;
    private Callback mCallback = new Callback() {
        @Override
        public boolean tryCaptureView(View child, int pointerId) {
            return child == mMainContent || child == mLeftContent;
        }

        public int clampViewPositionHorizontal(View child, int left, int dx) {
            if (child == mMainContent) {
                left = fixLeft(left);
            }
            return left;
        }

        private int fixLeft(int left) {
            if (left < 0) {
                left = 0;
            } else if (left > mDragRange) {
                left = mDragRange;
            }
            return left;
        };

        public int getViewHorizontalDragRange(View child) {
            return mDragRange;
        };

        public void onViewPositionChanged(View changedView, int left, int top, int dx, int dy) {
            if (changedView == mLeftContent) {
                mLeftContent.layout(0, 0, mWidth, mHeight);
                int newLeft = mMainContent.getLeft() + dx;
                newLeft = fixLeft(newLeft);
                mMainContent.layout(newLeft, 0, newLeft + mWidth, mHeight);
            }
            dispatchDragState(mMainContent.getLeft());
            invalidate(); 
        };

        public void onViewReleased(View releasedChild, float xvel, float yvel) {
            if (xvel == 0.0f && mMainContent.getLeft() < mDragRange * 0.5f) {
                close();
            } else if (xvel < 0) {
                close();
            } else {
                open();
            }
        };
    };
    private ViewGroup mLeftContent;
    private ViewGroup mMainContent;
    private int mWidth;
    private int mHeight;
    private int mDragRange;

    public enum State {
        CLOSE, OPEN, DRAGGING
    }

    private State mState = State.CLOSE;

    public interface OnDragStateChangeListener {
        void onClose();

        void onOpen();

        void onDragging(float percent);
    }

    private OnDragStateChangeListener mOnDragStateChangeListener;

    public State getState() {
        return mState;
    }

    public void setState(State state) {
        mState = state;
    }

    public OnDragStateChangeListener getOnDragStateChangeListener() {
        return mOnDragStateChangeListener;
    }

    public void setOnDragStateChangeListener(OnDragStateChangeListener onDragStateChangeListener) {
        mOnDragStateChangeListener = onDragStateChangeListener;
    }

    public DragLayout(Context context) {
        this(context, null);
    }

    protected void dispatchDragState(int left) {
        float percent = left * 1.0f / mDragRange;
        State preState = mState;
        mState = updateState(percent);
        if(mOnDragStateChangeListener != null) {
            mOnDragStateChangeListener.onDragging(percent);
            if (preState != mState) {
                if (mState == State.CLOSE) {
                    mOnDragStateChangeListener.onClose();
                } else if (mState == State.OPEN) {
                    mOnDragStateChangeListener.onOpen();
                }
            }
        }
        animViews(percent);
    }

    private State updateState(float percent) {
        if (percent == 0.0f) {
            return State.CLOSE;
        } else if (percent == 1.0f) {
            return State.OPEN;
        } else {
            return State.DRAGGING;
        }
    }

    private void animViews(float percent) {
        ViewHelper.setScaleX(mMainContent, 1.0f + (0.8f - 1.0f) * percent);
        ViewHelper.setScaleY(mMainContent, 1.0f + (0.8f - 1.0f) * percent);
        ViewHelper.setScaleX(mLeftContent, 0.5f + (1.0f - 0.5f) * percent);
        ViewHelper.setScaleY(mLeftContent, 0.5f + (1.0f - 0.5f) * percent);
        ViewHelper.setTranslationX(mLeftContent,
                EvaluateUtil.evaluateFloat(percent, -mWidth * 0.5f, 0));
        ViewHelper.setAlpha(mLeftContent, EvaluateUtil.evaluateFloat(percent, 0.0f, 1.0f));
        getBackground().setColorFilter(
                (Integer) EvaluateUtil.evaluateArgb(percent, Color.BLACK, Color.TRANSPARENT),
                Mode.SRC_OVER);
    }

    protected void open() {
        open(true);
    }

    public void open(boolean isSmooth) {
        int finalLeft = mDragRange;
        if (isSmooth) {
            mDragHelper.smoothSlideViewTo(mMainContent, finalLeft, 0);
            invalidate();
        } else {
            mMainContent.layout(finalLeft, 0, finalLeft + mWidth, mHeight);
        }
    }

    protected void close() {
        close(true);
    }

    public void close(boolean isSmooth) {
        int finalLeft = 0;
        if (isSmooth) {
            mDragHelper.smoothSlideViewTo(mMainContent, finalLeft, 0);
            invalidate();
        } else {
            mMainContent.layout(finalLeft, 0, finalLeft + mWidth, mHeight);
        }
    }

    public DragLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public DragLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        mDragHelper = ViewDragHelper.create(this, mCallback);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        return mDragHelper.shouldInterceptTouchEvent(ev);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        mDragHelper.processTouchEvent(event);
        return true;
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        if (getChildCount() < 2) {
            throw new RuntimeException("You must have at least 2 child views");
        }
        if (!(getChildAt(0) instanceof ViewGroup) || !(getChildAt(1) instanceof ViewGroup)) {
            throw new IllegalArgumentException("Your child views must be ViewGroup");
        }
        mLeftContent = (ViewGroup) getChildAt(0);
        mMainContent = (ViewGroup) getChildAt(1);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mWidth = mMainContent.getMeasuredWidth();
        mHeight = mMainContent.getMeasuredHeight();
        mDragRange = (int) (mWidth * 0.6f);
    }

    @Override
    public void computeScroll() {
        super.computeScroll();
        if (mDragHelper.continueSettling(true)) {
            ViewCompat.postInvalidateOnAnimation(this);
        }
    }

}
