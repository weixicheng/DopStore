package com.dopstore.mall.view;

import android.app.Dialog;
import android.content.Context;
import android.graphics.PixelFormat;
import android.os.Handler;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.dopstore.mall.R;
import com.dopstore.mall.util.Constant;


/**
 * 常用提示框， 用于中间显示各种提示框，可根据需要相应修改
 */
public class CommonDialog extends Dialog implements OnClickListener {

    private Handler handler_confirm;
    private int what_confirm;
    private Object object;

    public static final int CANCEL_MSG = -1; // 取消按钮被点击消息

    public CommonDialog(Context context, Handler handler_confirm, int what_confirm, String title, String message, int buttonStyle, String confirmText, String cancelText) {
        // TODO Auto-generated constructor stub
        super(context, R.style.actionSheetdialog);
        this.handler_confirm = handler_confirm;
        this.what_confirm = what_confirm;

        init(title, message, buttonStyle, confirmText, cancelText);
    }

    public CommonDialog(Context context, Handler handler_confirm, int what_confirm, String title, String message, int buttonStyle, Object object) {
        this(context, handler_confirm, what_confirm, title, message, buttonStyle, "", "");
        this.object = object;
    }

    public CommonDialog(Context context, Handler handler_confirm, int what_confirm, String title, String message, int buttonStyle) {
        // TODO Auto-generated constructor stub
        this(context, handler_confirm, what_confirm, title, message, buttonStyle, "", "");
    }

    public CommonDialog(Context context, View view) {
        super(context, R.style.actionSheetdialog);
        init(view);
    }

    public CommonDialog(Context context, View view, int dialogWidth) {
        super(context, R.style.actionSheetdialog);
        init(view, dialogWidth);
    }

    /**
     * 用于设置复杂内容的对话框，传入view,view由自己控制
     *
     * @param view
     */
    private void init(View view) {
        // TODO Auto-generated method stub
        // 已由外部设置了view
        setContentView(view);
        // 触摸对话框以外的地方取消对话框
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

    /**
     * 用于设置复杂内容的对话框，传入view,view由自己控制
     *
     * @param view
     */
    private void init(View view, int dialogWidth) {
        // TODO Auto-generated method stub
        // 已由外部设置了view
        setContentView(view);
        // 触摸对话框以外的地方取消对话框
        setCanceledOnTouchOutside(false);
        Window window = getWindow();
        // window.setType(WindowManager.LayoutParams.TYPE_SYSTEM_ALERT);
        WindowManager.LayoutParams params = window.getAttributes();
        params.windowAnimations = 0; // 设置动画，暂无动画
        params.gravity = Gravity.CENTER; // 设置显示位置
        // 让该window后所有东西都模糊
        params.flags = WindowManager.LayoutParams.FLAG_DIM_BEHIND;
        params.dimAmount = 0.6f; // 设置dialog整个背景层的透明色，这个属性必须设置fllags为FLAG_DIM_BEHIND才有效
        params.format = PixelFormat.TRANSPARENT; // 设置窗口背景为透明色，设置后窗口背景就不会出现黑色背景
        params.width = dialogWidth;

    }

    /**
     * 用于设置普通带有确定或取消的对话框
     *
     * @param title
     * @param message
     * @param buttonStyle
     */
    private void init(String title, String message, int buttonStyle, String confirmText, String cancelText) {

        setContentView(R.layout.common_dialog);
        // 触摸对话框以外的地方取消对话框
        setCanceledOnTouchOutside(false);
        Window window = getWindow();
        // window.setType(WindowManager.LayoutParams.TYPE_SYSTEM_ALERT);
        WindowManager.LayoutParams params = window.getAttributes();

        params.windowAnimations = 0; // 设置动画，暂无动画
        params.gravity = Gravity.CENTER; // 设置显示位置
        // 让该window后所有东西都模糊
        params.flags = WindowManager.LayoutParams.FLAG_DIM_BEHIND;
        params.dimAmount = 0.6f; // 设置dialog整个背景层的透明色，这个属性必须设置fllags为FLAG_DIM_BEHIND才有效
        params.format = PixelFormat.TRANSPARENT; // 设置窗口背景为透明色，设置后窗口背景就不会出现黑色背景

        // message
        TextView messageTextView = (TextView) findViewById(R.id.common_dialog_message);
        messageTextView.setText(message);
        // button
        TextView cancelButton = (TextView) findViewById(R.id.cancel); // 取消按钮
        cancelButton.setOnClickListener(this);
        // title
        if (!TextUtils.isEmpty(cancelText)) {
            // 已由外部设置了取消按钮内容
            cancelButton.setText(cancelText);
        }
        TextView dialogTitle = (TextView) findViewById(R.id.common_dialog_title);// 消息标题
        if (!TextUtils.isEmpty(title)) {
            // 标题
            dialogTitle.setText(title);
        }
        TextView confirmButton = (TextView) findViewById(R.id.confirm); // 确定按钮
        confirmButton.setOnClickListener(this);
        if (!TextUtils.isEmpty(confirmText)) {
            // 已由外部设置了确定按钮内容
            confirmButton.setText(confirmText);
        }
        // 纵向分界线
        View boundary = findViewById(R.id.boundary);
        switch (buttonStyle) {
            case Constant.SHOWCANCELBUTTON: // 只显示取消按钮
                cancelButton.setVisibility(View.VISIBLE);
                confirmButton.setVisibility(View.GONE);
                boundary.setVisibility(View.GONE);
                dialogTitle.setVisibility(View.GONE);
                break;
            case Constant.SHOWCONFIRMBUTTON: // 只显示确定按钮
                cancelButton.setVisibility(View.GONE);
                confirmButton.setVisibility(View.VISIBLE);
                boundary.setVisibility(View.GONE);
                dialogTitle.setVisibility(View.GONE);
                break;
            case Constant.SHOWALLBUTTON: // 显示取消和确定按钮
                cancelButton.setVisibility(View.VISIBLE);
                confirmButton.setVisibility(View.VISIBLE);
                boundary.setVisibility(View.VISIBLE);
                dialogTitle.setVisibility(View.GONE);
                break;
            case Constant.SHOWTITLEALLBUTTON:// 显示title 取消和确定按钮
                cancelButton.setVisibility(View.VISIBLE);
                confirmButton.setVisibility(View.VISIBLE);
                boundary.setVisibility(View.VISIBLE);
                dialogTitle.setVisibility(View.VISIBLE);
                break;
            case Constant.SHOWTITLECONFIRMBUTTON:// 显示title 和确定按钮
                cancelButton.setVisibility(View.GONE);
                confirmButton.setVisibility(View.VISIBLE);
                boundary.setVisibility(View.GONE);
                dialogTitle.setVisibility(View.VISIBLE);
                break;
            default:
                break;
        }

    }

    @Override
    public void show() {
        // TODO Auto-generated method stub
        super.show();
    }

    @Override
    public void dismiss() {
        // TODO Auto-generated method stub
        super.dismiss();
    }

    @Override
    public boolean isShowing() {
        // TODO Auto-generated method stub
        return super.isShowing();
    }

    @Override
    public void onClick(View v) {
        // TODO Auto-generated method stub
        switch (v.getId()) {
            case R.id.cancel: // 取消被点击
                handler_confirm.obtainMessage(CANCEL_MSG, object).sendToTarget();
                this.dismiss();
                break;
            case R.id.confirm: // 确定被点击
                handler_confirm.obtainMessage(what_confirm, object).sendToTarget();
                this.dismiss();
                break;
            default:
                break;
        }
    }

}
