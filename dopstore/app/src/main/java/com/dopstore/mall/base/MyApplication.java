package com.dopstore.mall.base;

import android.app.Activity;
import android.app.Application;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.pingplusplus.android.PingppLog;

import java.util.ArrayList;
import java.util.List;

import cn.jpush.android.api.JPushInterface;


public class MyApplication extends Application {
    public static MyApplication instance = null;
    public static List<Activity> activities = new ArrayList<Activity>();

    @Override
    public void onCreate() {
        super.onCreate();
        ImageLoader.getInstance().init(ImageLoaderConfiguration.createDefault(this));
        JPushInterface.setDebugMode(false);
        JPushInterface.init(this);
        PingppLog.DEBUG = false;
    }

    public static MyApplication getInstance() {
        if (instance == null) {
            instance = new MyApplication();
        }
        return instance;
    }

    public void addActivity(Activity activity) {
        activities.add(activity);
    }

    public void removeActivity(Activity activity) {
//        activity.finish();
        activities.remove(activity);
    }

    public static void distoryActivity(Activity activity) {
        activity.finish();
        activities.remove(activity);
    }

    public void finishAllActivity() {
        for (Activity activity : activities) {
            if (!activity.isFinishing()) {
                activity.finish();
            }
        }
    }

}

