package com.dopstore.mall.view.scrollview;

import android.content.Context;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;

import com.dopstore.mall.R;

/**
 * Created by ysnow on 2015/3/3.
 */
public class DetailMenu extends ScrollView {
    private int mScreenHeight;
    private RelativeLayout topLayout;


    private YsnowScrollViewPageOne wrapperMenu;
    private YsnowWebView wrapperContent;
    private boolean isSetted = false;
    private boolean ispageOne = true;

    public DetailMenu(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public DetailMenu(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        //获得屏幕的宽度和计算设置的偏移量的像素值,并计算出menu的宽度
        WindowManager windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics metrics = new DisplayMetrics();
        windowManager.getDefaultDisplay().getMetrics(metrics);
        mScreenHeight = metrics.heightPixels;//得到屏幕的宽度(像素)
    }

    public DetailMenu(Context context) {
        this(context, null);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        if (!isSetted) {
            //得到里面的控件
            final LinearLayout wrapper = (LinearLayout) getChildAt(0);
            wrapperMenu = (YsnowScrollViewPageOne) wrapper.getChildAt(0);
            wrapperContent = (YsnowWebView) wrapper.getChildAt(1);
            //设置两个子View的高度为手机的高度
            wrapperMenu.getLayoutParams().height = mScreenHeight;
            wrapperContent.getLayoutParams().height = mScreenHeight;
            isSetted = true;
        }
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
        if (changed) {
            this.scrollTo(0, 0);
        }

    }


    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        int action = ev.getAction();
        switch (action) {
            case MotionEvent.ACTION_UP:
                topLayout.setBackgroundColor(getResources().getColor(R.color.red_color_f93448));
                //隐藏在左边的距离
                int scrollY = getScrollY();
                int creteria = mScreenHeight / 5;//滑动多少距离
                if (ispageOne) {
                    if (scrollY <= creteria) {
                        //显示菜单
                        topLayout.setBackgroundColor(getResources().getColor(R.color.transparent));
                        this.smoothScrollTo(0, 0);
                    } else {
                        //隐藏菜单
                        topLayout.setBackgroundColor(getResources().getColor(R.color.red_color_f93448));
                        this.smoothScrollTo(0, mScreenHeight);
                        this.setFocusable(false);
                        ispageOne = false;
                    }
                } else {
                    int scrollpadding = mScreenHeight - scrollY;
                    if (scrollpadding >= creteria) {
                        topLayout.setBackgroundColor(getResources().getColor(R.color.transparent));
                        this.smoothScrollTo(0, 0);
                        ispageOne = true;
                    } else {
                        topLayout.setBackgroundColor(getResources().getColor(R.color.red_color_f93448));
                        this.smoothScrollTo(0, mScreenHeight);
                    }
                }
                return true;
        }
        return super.onTouchEvent(ev);
    }


    public void closeMenu() {
        if (ispageOne) return;
        this.smoothScrollTo(0, 0);
        ispageOne = true;
    }

    public void openMenu() {
        if (!ispageOne) return;
        this.smoothScrollTo(0, mScreenHeight);
        ispageOne = false;
    }

    public void setView(RelativeLayout topLayout) {
        this.topLayout = topLayout;
    }

    /**
     * 打开和关闭菜单
     */
    public void toggleMenu() {
        if (ispageOne) {
            openMenu();
        } else {
            closeMenu();
        }
    }


}
