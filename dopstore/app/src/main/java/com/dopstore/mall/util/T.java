package com.dopstore.mall.util;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;
import android.widget.Toast;

/**
 * Created by 隗喜成 on 16/9/2.
 */
public class T {
    private T() {
        /* cannot be instantiated */
        throw new UnsupportedOperationException("cannot be instantiated");
    }

    public static boolean isShow = true;

    /**
     * 短时间显示Toast
     *
     * @param context
     * @param message
     */
    public static void show(final Context context, final String message) {
        if (isShow)
            new Handler(Looper.getMainLooper()).post(new Runnable() {

                @Override
                public void run() {
                    String msg = message;
                    if (TextUtils.isEmpty(message)) {
                        msg = "暂无数据";
                    }
                    Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
                }
            });
    }

    public static void checkNet(final Context context) {
        if (isShow)
            new Handler(Looper.getMainLooper()).post(new Runnable() {

                @Override
                public void run() {
                    Toast.makeText(context, "请检查网络", Toast.LENGTH_SHORT).show();
                }
            });
    }

}
