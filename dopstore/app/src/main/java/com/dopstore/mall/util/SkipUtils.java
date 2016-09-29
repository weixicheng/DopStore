package com.dopstore.mall.util;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;

import com.dopstore.mall.R;
import com.dopstore.mall.base.MyApplication;

import java.io.Serializable;
import java.util.Map;


/**
 * 作者：weixicheng on 16/7/18 09:50
 */
public class SkipUtils {
    private static SkipUtils windowUtils = null;

    private SkipUtils() {

    }

    public static SkipUtils getInstance() {
        if (windowUtils == null) {
            windowUtils = new SkipUtils();
        }
        return windowUtils;
    }

    /**
     * 直接跳转
     *
     * @param context 上下文对象
     * @param name    目标页面
     * @param flag    是否保留
     */
    public static void directJump(Context context, Class<?> name, boolean flag) {
        Intent intent = new Intent(context, name);
        context.startActivity(intent);
        if (flag == true) MyApplication.distoryActivity((Activity) context);
        ((Activity) context).overridePendingTransition(R.anim.return_from_click, R.anim.return_out_click);
    }

    /**
     * 带参跳转
     *
     * @param context 上下文对象
     * @param name    目标页
     * @param map     参数
     * @param flag    是否保留
     */
    public static void jumpForMap(Context context, Class<?> name, Map<String, Object> map, boolean flag) {
        Intent intent = new Intent(context, name);
        intent.putExtra("map", (Serializable) map);
        context.startActivity(intent);
        if (flag == true) ((Activity) context).finish();
        ((Activity) context).overridePendingTransition(R.anim.return_from_click, R.anim.return_out_click);
    }

    /**
     * 传值交互
     *
     * @param context     上下文
     * @param name        目标页
     * @param requestCode
     */
    public static void directJumpForResult(Context context, Class<?> name, int requestCode) {
        Intent intent = new Intent(context, name);
        ((Activity) context).startActivityForResult(intent, requestCode);
        ((Activity) context).overridePendingTransition(R.anim.return_from_click, R.anim.return_out_click);
    }

    /**
     * 传值交互
     *
     * @param context     上下文
     * @param name        目标页
     * @param map         参数
     * @param requestCode
     */
    public static void jumpForMapResult(Context context, Class<?> name, Map<String, Object> map, int requestCode) {
        Intent intent = new Intent(context, name);
        intent.putExtra("map", (Serializable) map);
        ((Activity) context).startActivityForResult(intent, requestCode);
        ((Activity) context).overridePendingTransition(R.anim.return_from_click, R.anim.return_out_click);
    }

    /**
     * 返回传值交互
     *
     * @param context 上下文
     */
    public static void backForResult(Context context) {
        Intent intent = new Intent();
        ((Activity) context).setResult(Activity.RESULT_OK, intent);
        MyApplication.distoryActivity((Activity) context);
        ((Activity) context).overridePendingTransition(R.anim.enter_from_click, R.anim.enter_out_click);
    }

    /**
     * 返回传值交互
     *
     * @param context 上下文
     * @param map     参数
     */
    public static void backForMapResult(Context context, Map<String, Object> map) {
        Intent intent = new Intent();
        intent.putExtra("map", (Serializable) map);
        ((Activity) context).setResult(Activity.RESULT_OK, intent);
        MyApplication.distoryActivity((Activity) context);
        ((Activity) context).overridePendingTransition(R.anim.enter_from_click, R.anim.enter_out_click);
    }


    /**
     * 获取传递参数
     *
     * @param context 上下文对象
     * @return 传递参数
     */
    public static Map<String, Object> getMap(Context context) {
        Intent intent = ((Activity) context).getIntent();
        return (Map<String, Object>) (intent.getSerializableExtra("map"));
    }

    /**
     * 无参返回
     *
     * @param context
     */
    public static void back(Context context) {
        MyApplication.distoryActivity((Activity) context);
        ((Activity) context).overridePendingTransition(R.anim.enter_from_click, R.anim.enter_out_click);
    }


}
