package com.dopstore.mall.view;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.widget.Scroller;

/**
 * 仿Launcher中的WorkSapce，可以左右滑动切换屏幕的类
 */
public class MyScrollLayoutForStart extends ViewGroup {

    private static final String TAG = "ScrollLayout";
    private Scroller mScroller;
    private VelocityTracker mVelocityTracker;

    private int mCurScreen;
    private int page = 0;
    private int mDefaultScreen = 0;

    private static final int TOUCH_STATE_REST = 0;
    private static final int TOUCH_STATE_SCROLLING = 1;

    private static final int SNAP_VELOCITY = 600;

    private int mTouchState = TOUCH_STATE_REST;
    private int mTouchSlop;
    private float mLastMotionX;
    private float mLastMotionY;

    private int childWidth = 0; // 页宽

    private boolean prePage = false; // 前页
    private boolean nextPage = false; // 后页

    private PageListener pageListener;

    private boolean isLoop = false; // 默认不循环
    // 标记是否是从第一页滑到最后一页或从最后一页滑到第一页
    private boolean loop = false;
    private boolean isScroll = false; // 是否继续滚动

    public MyScrollLayoutForStart(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MyScrollLayoutForStart(Context context, AttributeSet attrs,
                                  int defStyle) {
        super(context, attrs, defStyle);
        mScroller = new Scroller(context);

        mCurScreen = mDefaultScreen;
        mTouchSlop = ViewConfiguration.get(getContext()).getScaledTouchSlop();
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        int childLeft = 0;
        final int childCount = getChildCount();

        for (int i = 0; i < childCount; i++) {
            final View childView = getChildAt(i);
            if (childView.getVisibility() != View.GONE) {
                final int childWidth = childView.getMeasuredWidth();
                childView.layout(childLeft, 0, childLeft + childWidth,
                        childView.getMeasuredHeight());
                childLeft += childWidth;
            }
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        final int width = MeasureSpec.getSize(widthMeasureSpec);
        final int count = getChildCount();
        for (int i = 0; i < count; i++) {
            getChildAt(i).measure(widthMeasureSpec, heightMeasureSpec);
        }
        scrollTo(mCurScreen * width, 0);
    }

    /**
     * According to the position of current layout scroll to the destination
     * page.
     */
    public void snapToDestination() {
        final int screenWidth = getWidth();
        int destScreen = (getScrollX() + screenWidth / 2) / screenWidth;
        if (isLoop) {
            if (getScrollX() < 0 && Math.abs(getScrollX()) > (screenWidth / 2)) {
                loop = true;
                destScreen = getChildCount() - 1;
            } else if (getScrollX() > (screenWidth * (getChildCount() - 1))
                    && (getScrollX() - (screenWidth * (getChildCount() - 1))) > (screenWidth / 2)) {
                loop = true;
                destScreen = 0;
            }
        }
        snapToScreen(destScreen);
    }

    /**
     * 循环滑动时
     */
    @Override
    protected void dispatchDraw(Canvas canvas) {
        super.dispatchDraw(canvas);
        if (isLoop) {
            int scrollX = getScrollX();
            final int N = getChildCount();
            final int width = getWidth();
            if (scrollX < 0) {
                View lastChild = getChildAt(N - 1);
                if (lastChild == null) {
                    return;
                }
                canvas.save();
                canvas.translate(-width, 0);
                canvas.clipRect(0, 0, width, getBottom());
                lastChild.draw(canvas);
                canvas.restore();
            } else if (scrollX > (N - 1) * width) {
                View firstChild = getChildAt(0);
                if (firstChild == null) {
                    return;
                }
                canvas.save();
                canvas.translate(N * width, 0);
                canvas.clipRect(0, 0, width, getBottom());
                firstChild.draw(canvas);
                canvas.restore();
            }
        }
    }

    public void snapToScreen(int whichScreen) {
        // get the valid layout page
        whichScreen = Math.max(0, Math.min(whichScreen, getChildCount() - 1));
        if (getScrollX() != (whichScreen * getWidth())) {

            if (loop && getScrollX() < 0 && mCurScreen == 0
                    && getScrollX() != (whichScreen * getWidth())) {
                // 从第一页到最后一页
                int delta = -getWidth() - getScrollX();
                mScroller.startScroll(whichScreen * getWidth() - delta, 0,
                        delta, 0, Math.abs(delta) * 2);
                loop = false;
            } else if (loop && getScrollX() > 0
                    && mCurScreen == (getChildCount() - 1)
                    && getScrollX() != (whichScreen * getWidth())) {
                // 从最后一页到第一页
                int delta = getWidth()
                        - (getScrollX() - (getChildCount() - 1) * getWidth());
                mScroller.startScroll(-delta, 0, delta, 0, Math.abs(delta) * 2);
                loop = false;
            } else {
                int delta = whichScreen * getWidth() - getScrollX();
                mScroller.startScroll(getScrollX(), 0, delta, 0,
                        Math.abs(delta) * 2);
            }

            if (whichScreen != mCurScreen) {
                pageListener.page(whichScreen);
            }
            mCurScreen = whichScreen;
            invalidate(); // Redraw the layout
        }
    }

    public void setToScreen(int whichScreen) {
        whichScreen = Math.max(0, Math.min(whichScreen, getChildCount() - 1));
        mCurScreen = whichScreen;
        scrollTo(whichScreen * getWidth(), 0);
        if (whichScreen != page) {
            page = whichScreen;
        }
    }

    /**
     * 获得当前页码
     */
    public int getCurScreen() {
        return mCurScreen;
    }

    /**
     * 当滑动后的当前页码
     */
    public int getPage() {
        return page;
    }

    @Override
    public void computeScroll() {
        if (mScroller.computeScrollOffset()) {
            scrollTo(mScroller.getCurrX(), mScroller.getCurrY());
            postInvalidate();
        }
    }

    private float point1 = 0;
    private float point2 = 0;
    private boolean isParentUnableScroll;

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        if (mVelocityTracker == null) {
            mVelocityTracker = VelocityTracker.obtain();
        }
        mVelocityTracker.addMovement(event);

        final int action = event.getAction();
        final float x = event.getX();
        final float y = event.getY();

        switch (action) {
            case MotionEvent.ACTION_DOWN:
                if (!mScroller.isFinished()) {
                    mScroller.abortAnimation();
                }
                mLastMotionX = x;
                point1 = x;
                break;
            case MotionEvent.ACTION_MOVE:
                final VelocityTracker velocityTracker_move = mVelocityTracker;
                velocityTracker_move.computeCurrentVelocity(1000);
                int velocityX_move = (int) velocityTracker_move.getXVelocity();
                int deltaX = (int) (mLastMotionX - x);
                mLastMotionX = x;
                point2 = x;
                if (isScroll) {
                    scrollBy(deltaX, 0);
                } else {
                    if (point2 - point1 > 0) {
                        if (mCurScreen == 0) {

                        } else {
                            scrollBy(deltaX, 0);
                        }
                    } else if (point2 - point1 < 0) {
                        if (mCurScreen == (getChildCount() - 1)) {

                        } else {
                            scrollBy(deltaX, 0);
                        }
                    } else {
                        scrollBy(deltaX, 0);
                    }

                    // 判断是否滑到了第一页和最后一页
                    if (velocityX_move < -SNAP_VELOCITY
                            && mCurScreen == getChildCount() - 1) {

                        parentSliding(velocityX_move);

                    } else if (velocityX_move > SNAP_VELOCITY && mCurScreen == 0) {

                        parentSliding(velocityX_move);

                    }
                }
                break;
            case MotionEvent.ACTION_UP:
                final VelocityTracker velocityTracker = mVelocityTracker;
                velocityTracker.computeCurrentVelocity(1000);
                int velocityX = (int) velocityTracker.getXVelocity();

                if (velocityX > SNAP_VELOCITY) {
                    prePage = true;
                    nextPage = false;
                } else if (velocityX < -SNAP_VELOCITY) {
                    prePage = false;
                    nextPage = true;
                } else {
                    // 预留
                }

                if (velocityX > SNAP_VELOCITY && mCurScreen > 0) {
                    // Fling enough to move left
                    snapToScreen(mCurScreen - 1);
                } else if (velocityX < -SNAP_VELOCITY
                        && mCurScreen < getChildCount() - 1) {
                    // Fling enough to move right
                    snapToScreen(mCurScreen + 1);
                } else if (velocityX < -SNAP_VELOCITY
                        && mCurScreen == getChildCount() - 1) {
                    if (isLoop) {
                        loop = true;
                        snapToScreen(0);
                    } else {
                        snapToDestination();
                    }
                } else if (velocityX > SNAP_VELOCITY && mCurScreen == 0) {
                    if (isLoop) {
                        loop = true;
                        snapToScreen(getChildCount() - 1);
                    } else {
                        snapToDestination();
                    }
                } else {
                    snapToDestination();
                }

                if (mVelocityTracker != null) {
                    mVelocityTracker.recycle();
                    mVelocityTracker = null;
                }
                mTouchState = TOUCH_STATE_REST;
                break;
            case MotionEvent.ACTION_CANCEL:
                mTouchState = TOUCH_STATE_REST;
                break;
        }
        return true;
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {

        final int action = ev.getAction();
        if ((action == MotionEvent.ACTION_MOVE)
                && (mTouchState != TOUCH_STATE_REST)) {
            return true;
        }

        final float x = ev.getX();
        final float y = ev.getY();

        switch (action) {
            case MotionEvent.ACTION_MOVE:
                final int xDiff = (int) Math.abs(mLastMotionX - x);
                if (xDiff > mTouchSlop) {
                    mTouchState = TOUCH_STATE_SCROLLING;
                }
                if (isParentUnableScroll) {
                    getParent().requestDisallowInterceptTouchEvent(true);
                }
                break;
            case MotionEvent.ACTION_DOWN:
                mLastMotionX = x;
                mLastMotionY = y;
                mTouchState = mScroller.isFinished() ? TOUCH_STATE_REST
                        : TOUCH_STATE_SCROLLING;
                if (isParentUnableScroll) {
                    getParent().requestDisallowInterceptTouchEvent(true);
                }
                break;

            case MotionEvent.ACTION_CANCEL:
            case MotionEvent.ACTION_UP:
                mTouchState = TOUCH_STATE_REST;
                break;
        }
        return mTouchState != TOUCH_STATE_REST;
    }

    public void setPageListener(PageListener pageListener) {
        this.pageListener = pageListener;
    }

    public interface PageListener {
        void page(int page);
    }

    public boolean isPrePage() {
        return prePage;
    }

    public boolean isNextPage() {
        return nextPage;
    }

    public void setIsLoop(boolean isLoop) {
        this.isLoop = isLoop;
    }

    public void setIsScroll(boolean isScroll) {
        this.isScroll = isScroll;
    }

    public void setParentUnableScroll(boolean isParentUnableScroll) {
        this.isParentUnableScroll = isParentUnableScroll;
    }

    public void parentSliding(int velocityx) {

        if (velocityx > SNAP_VELOCITY) {
            prePage = true;
            nextPage = false;
        } else if (velocityx < -SNAP_VELOCITY) {
            prePage = false;
            nextPage = true;
        } else {
            // 预留
        }

        snapToDestination();

        if (mVelocityTracker != null) {
            mVelocityTracker.recycle();
            mVelocityTracker = null;
        }
        mTouchState = TOUCH_STATE_REST;

        // 失去自己的滑到事件，父类滑到起效

        getParent().requestDisallowInterceptTouchEvent(false);
    }
}