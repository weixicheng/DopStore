package com.dopstore.mall.view;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.dopstore.mall.util.Constant;

/**
 * @author weixicheng
 * @TODO 自定义指示器
 * @date: 2015-1-9 上午10:59:27
 * @version: V2.0
 */
public class CustomIndicatorStart {

    private Context mContext; // 会话上下文
    private MyScrollLayout scrollLayout; // scrollLayout布局
    private MyScrollLayoutForStart scrollLayoutStart;
    private LinearLayout indicator; // 指示器布局

    private int length = 0; // 指示器个数
    private boolean isTab = false; // 是不是滑动tab
    // 适配用到
    private int result_resolution = 0;// 分辨率
    private int fsize = 0;// popwindow距离title高度

    public CustomIndicatorStart(Context context, final int length,
                                MyScrollLayout scrollLayout, LinearLayout indicator) {
        // TODO Auto-generated constructor stub
        mContext = context;
        this.length = length;
        this.scrollLayout = scrollLayout;
        this.indicator = indicator;
        isTab = false;
    }

    public CustomIndicatorStart(Context context, final int length,
                                MyScrollLayoutForStart scrollLayout, LinearLayout indicator) {
        // TODO Auto-generated constructor stub
        mContext = context;
        this.length = length;
        this.scrollLayoutStart = scrollLayout;
        this.indicator = indicator;
        isTab = true;
    }

    /**
     * @param lineWidth            dip 线宽
     * @param imageId              指示器图片资源
     * @param onScrollPageListener 页滚动监听
     * @return void
     * @TODO 初始化指示器
     * @throw
     * @author shangxiaoxue
     * @date: 2015-1-9 上午11:15:00
     */
    public void initIndicator(int lineWidth, final int imageId,
                              final OnScrollPageListener onScrollPageListener) {
        // 设备屏幕分辨率
        getResolution(mContext);
        result_resolution = Constant.WIDTH * Constant.HEIGHT;

        DisplayMetrics dm = mContext.getResources().getDisplayMetrics();
        // 获取线宽 px
        int widthLayout = dip2px(lineWidth, dm.density);
        // 设置指示器数组
        final ImageView[] scrollLayoutPageIndex = new ImageView[length];
        // 指示器配置参数
        LinearLayout.LayoutParams imageParams = new LinearLayout.LayoutParams(
                LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        // 设置指示器布局数组
        final LinearLayout[] mLayoutPageIndex = new LinearLayout[length];
        // 设置imageView
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        Bitmap bm = BitmapFactory.decodeResource(mContext.getResources(),
                imageId, options);
        int imageWidth = options.outWidth;
        // 每个指示应该占的宽度
        int perWidth = widthLayout / length;
        // 需要的宽度
        int marginWidth = imageWidth - perWidth;
        float basicDensityDiff = dm.density - 1.5f;
        int basicWeight = (int) (basicDensityDiff / 0.5f);
        if (marginWidth < 0) {
            // 负数
            marginWidth = -((widthLayout - imageWidth * length) / (length - 1) + fsize);
        } else {
            // 正数
            marginWidth = marginWidth + basicWeight * 10;
        }

        for (int i = 0; i < length; i++) {
            scrollLayoutPageIndex[i] = new ImageView(mContext);
            scrollLayoutPageIndex[i].setLayoutParams(imageParams);
            mLayoutPageIndex[i] = new LinearLayout(mContext);
            if (i == 0) {
                // 第一个靠左
                scrollLayoutPageIndex[i].setImageResource(imageId);
                LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                        LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
                mLayoutPageIndex[i].setLayoutParams(layoutParams);
                mLayoutPageIndex[i].addView(scrollLayoutPageIndex[i]);
            } else if (i == length - 1) {
                // 最后一个靠右
                scrollLayoutPageIndex[i].setImageResource(imageId);
                scrollLayoutPageIndex[i].setVisibility(View.INVISIBLE);
                LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                        LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
                layoutParams.leftMargin = -marginWidth;
                mLayoutPageIndex[i].setLayoutParams(layoutParams);
                mLayoutPageIndex[i].addView(scrollLayoutPageIndex[i]);
            } else {
                scrollLayoutPageIndex[i].setImageResource(imageId);
                scrollLayoutPageIndex[i].setVisibility(View.INVISIBLE);
                LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                        LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
                layoutParams.leftMargin = -marginWidth;
                mLayoutPageIndex[i].setLayoutParams(layoutParams);
                mLayoutPageIndex[i].addView(scrollLayoutPageIndex[i]);
            }
            indicator.addView(mLayoutPageIndex[i]);
        }
        if (isTab) {

            scrollLayoutStart
                    .setPageListener(new MyScrollLayoutForStart.PageListener() {

                        @Override
                        public void page(int page) {
                            onScrollPageListener.currentPage(page);
                            for (int i = 0; i < length; i++) {
                                if (i == page) {
                                    scrollLayoutPageIndex[i]
                                            .setImageResource(imageId);
                                    scrollLayoutPageIndex[i]
                                            .setVisibility(View.VISIBLE);
                                } else {
                                    scrollLayoutPageIndex[i]
                                            .setImageResource(imageId);
                                    scrollLayoutPageIndex[i]
                                            .setVisibility(View.INVISIBLE);
                                }
                            }

                        }

                    });
        } else {
            scrollLayout.setPageListener(new MyScrollLayout.PageListener() {

                @Override
                public void page(int page) {
                    // TODO Auto-generated method stub

                    onScrollPageListener.currentPage(page);
                    for (int i = 0; i < length; i++) {
                        if (i == page) {
                            scrollLayoutPageIndex[i].setImageResource(imageId);
                            scrollLayoutPageIndex[i]
                                    .setVisibility(View.VISIBLE);
                        } else {
                            scrollLayoutPageIndex[i].setImageResource(imageId);
                            scrollLayoutPageIndex[i]
                                    .setVisibility(View.INVISIBLE);
                        }
                    }
                }
            });
        }
    }

    public interface OnScrollPageListener {

        void currentPage(int currPage);
    }

    public static int dip2px(float dipValue, float scale) {
        return (int) (dipValue * scale + 0.5f);
    }

    /**
     * @param context
     * @return void
     * @TODO 获取屏幕分辨率
     * @throw
     * @author XinYu.Yang
     * @date: 2014-9-10 下午12:35:23
     */
    public static void getResolution(Context context) {
        DisplayMetrics metrics = new DisplayMetrics();
        ((Activity) context).getWindowManager().getDefaultDisplay().getMetrics(metrics);
        Constant.WIDTH = metrics.widthPixels;
        Constant.HEIGHT = metrics.heightPixels;

    }
}
