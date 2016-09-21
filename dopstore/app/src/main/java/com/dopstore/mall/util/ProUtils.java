package com.dopstore.mall.util;

import android.app.ProgressDialog;
import android.content.Context;

public class ProUtils extends ProgressDialog {


    public ProUtils(Context context) {
        super(context);
        init();
    }

    public ProUtils(Context context, int theme) {
        super(context, theme);
        init();
    }

    private void init(){
        setProgressStyle(ProgressDialog.STYLE_SPINNER);
        setCancelable(false);
        setMessage("正在努力加载...");
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
