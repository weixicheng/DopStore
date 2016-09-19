package com.dopstore.mall.util;

import android.app.ProgressDialog;
import android.content.Context;

/**
 * 作者：xicheng on 16/9/19 14:00
 * 类别：
 */
public class ProUtils {
    private static ProgressDialog pd;

    public ProUtils(Context context) {
        pd = new ProgressDialog(context);
        pd.setCancelable(false);
        pd.setMessage("加载中...");
        pd.setProgressStyle(ProgressDialog.STYLE_SPINNER);
    }

    public void show(){
        pd.show();
    }
    public void diamiss(){
        if (pd!=null&&pd.isShowing()){
            pd.dismiss();
        }
    }


}
