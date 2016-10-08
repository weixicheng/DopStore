package com.dopstore.mall.util;

import android.app.Dialog;
import android.content.Context;
import android.graphics.PixelFormat;
import android.view.Gravity;
import android.view.Window;
import android.view.WindowManager;

import com.dopstore.mall.R;

public class ProUtils extends Dialog {


    public ProUtils(Context context) {
        super(context, R.style.actionSheetdialog);
        init();
    }

    public ProUtils(Context context, int themeResId) {
        super(context, R.style.actionSheetdialog);
        init();
    }

    protected ProUtils(Context context, boolean cancelable, OnCancelListener cancelListener) {
        super(context, R.style.actionSheetdialog);
        init();
    }

    private void init() {
        setContentView(R.layout.comm_net_layout);
        setCancelable(false);
        setCanceledOnTouchOutside(false);
        Window window = getWindow();
        window.setType(WindowManager.LayoutParams.TYPE_SYSTEM_ALERT);
        WindowManager.LayoutParams params = window.getAttributes();
        params.windowAnimations = 0; // 设置动画，暂无动画
        params.gravity = Gravity.CENTER; // 设置显示位置
        // 让该window后所有东西都模糊
        params.flags = WindowManager.LayoutParams.FLAG_DIM_BEHIND;
        params.dimAmount = 0.6f; // 设置dialog整个背景层的透明色，这个属性必须设置fllags为FLAG_DIM_BEHIND才有效
        params.format = PixelFormat.TRANSPARENT; // 设置窗口背景为透明色，设置后窗口背景就不会出现黑色背景

    }

    @Override
    public void show() {
        super.show();
    }

    @Override
    public void dismiss() {
        super.dismiss();
    }
}
