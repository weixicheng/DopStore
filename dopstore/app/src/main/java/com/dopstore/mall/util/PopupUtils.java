package com.dopstore.mall.util;


import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.PopupWindow;
import android.widget.PopupWindow.OnDismissListener;
import android.widget.RelativeLayout.LayoutParams;

import com.dopstore.mall.R;


public final class PopupUtils {

    public static PopupWindowListener popupWindowListener;

    /**
     * 弹出底部popupWindow
     *
     * @param context
     * @param popupWindow
     * @param view              显示的view
     * @param popupWindowWidth
     * @param popupWindowHieght
     * @param parent            父view
     * @throws �? �? 如果�?个页面中有两个事件需要弹出PopupWindow时，�?要在调用页面做好判断�?
     *            否则会出现在两个事件点击后弹出的是同�?个PopupWindow窗口
     */
    @SuppressWarnings("deprecation")
    public static PopupWindow ShowBottomPopupWindow(Context context,
                                                    PopupWindow popupWindow, View view, int popupWindowWidth,
                                                    int popupWindowHieght, View parent) {
        if (null == popupWindow && popupWindowHieght!=0) {
            DisplayMetrics dm = context.getResources().getDisplayMetrics();
            int heightPx = (int) (dm.density * popupWindowHieght);
            popupWindow = new PopupWindow(view, popupWindowWidth, heightPx);
        }else {
            popupWindow = new PopupWindow(view, popupWindowWidth, ViewGroup.LayoutParams.WRAP_CONTENT);
        }

        popupWindow.setAnimationStyle(R.style.AnimationBottomFade);
        // popupWindow.setBackgroundDrawable(new BitmapDrawable());
        popupWindow.setFocusable(true);
        popupWindow.setOutsideTouchable(false);
        //实例化一个ColorDrawable颜色为半透明
        ColorDrawable dw = new ColorDrawable(0xb0000000);
        //设置SelectPicPopupWindow弹出窗体的背景
        popupWindow.setBackgroundDrawable(dw);
        popupWindow.showAtLocation(parent, Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
        popupWindow.setOnDismissListener(new OnDismissListener() {

            @Override
            public void onDismiss() {
                popupWindowListener.myDissmiss();
            }

        });
        popupWindow.update();
        return popupWindow;
    }

    /**
     * 顶部底部popupWindow
     *
     * @param context
     * @param popupWindow
     * @param view
     * @param parent
     * @param height
     * @return 如果一个页面中有两个事件需要弹出PopupWindow时
     * ，需要在调用页面做好判断 否则会出现在两个事件点击后弹出的是同一个PopupWindow窗口
     */
    @SuppressWarnings("deprecation")
    public static PopupWindow ShowTopPopupWindow(Context context,
                                                 PopupWindow popupWindow, View view, View parent, String height) {
        DisplayMetrics dm = new DisplayMetrics();
        ((Activity) context).getWindowManager().getDefaultDisplay().getMetrics(dm);
        if (null == popupWindow) {
            if ("1".equals(height)) {
                popupWindow = new PopupWindow(view, dip2px(150f, dm.density), dip2px(170f, dm.density));
            } else if ("2".equals(height)) {
                popupWindow = new PopupWindow(view, dm.widthPixels, LayoutParams.WRAP_CONTENT);
            } else {
                popupWindow = new PopupWindow(view, dip2px(150f, dm.density), dip2px(120f, dm.density));
            }
        }
        popupWindow.setBackgroundDrawable(new BitmapDrawable());
        popupWindow.setFocusable(true);
        popupWindow.setOutsideTouchable(false);
        popupWindow.showAsDropDown(parent, 0, 0);
        popupWindow.setOnDismissListener(new OnDismissListener() {

            @Override
            public void onDismiss() {
                popupWindowListener.myDissmiss();
            }

        });
        popupWindow.update();
        return popupWindow;
    }

    /**
     * 顶部底部popupWindow
     *
     * @param context
     * @param popupWindow
     * @param view
     * @param parent
     * @return 如果一个页面中有两个事件需要弹出PopupWindow时
     * ，需要在调用页面做好判断 否则会出现在两个事件点击后弹出的是同一个PopupWindow窗口
     */
    @SuppressWarnings("deprecation")
    public static PopupWindow ShowRightPopupWindow(Context context, PopupWindow popupWindow, View view, View parent) {
        DisplayMetrics dm = new DisplayMetrics();
        ((Activity) context).getWindowManager().getDefaultDisplay()
                .getMetrics(dm);
        if (null == popupWindow) {
            popupWindow = new PopupWindow(view, dm.widthPixels, dm.heightPixels);
        }
        popupWindow.setAnimationStyle(R.style.AnimationRightFade);
        popupWindow.setBackgroundDrawable(new BitmapDrawable());
        popupWindow.setFocusable(true);
        popupWindow.setOutsideTouchable(true);
        popupWindow.showAtLocation(parent, Gravity.RIGHT | Gravity.TOP, 0, 500);
        popupWindow.setOnDismissListener(new OnDismissListener() {

            @Override
            public void onDismiss() {
                popupWindowListener.myDissmiss();
            }
        });
        popupWindow.update();
        return popupWindow;
    }

    public static void setPopupWindowListener(PopupWindowListener myListener) {
        popupWindowListener = myListener;
    }

    public static interface PopupWindowListener {
        void myDissmiss();
    }

    /**
     * 将dip或dp值转换为px值，保证尺寸大小不变
     *
     * @param dipValue
     * @param scale    （DisplayMetrics类中属�?�density�?
     * @return
     */
    public static int dip2px(float dipValue, float scale) {
        return (int) (dipValue * scale + 0.5f);
    }
}
