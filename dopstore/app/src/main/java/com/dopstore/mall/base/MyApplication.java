package com.dopstore.mall.base;

import android.app.Activity;
import android.app.Application;

import com.facebook.drawee.backends.pipeline.Fresco;

import java.util.ArrayList;
import java.util.List;


public class MyApplication extends Application {

    public static MyApplication instance = null;
    //用来管理所有打开的Activity
    public static List<Activity> activities = new ArrayList<Activity>();

    @Override
    public void onCreate() {
        super.onCreate();
        Fresco.initialize(getApplicationContext());
        /**
         * JPushInterface.stopPush(getApplicationContext());
         * JPushInterface.resumePush(getApplicationContext());
         * JPushInterface.getRegistrationID(getApplicationContext());

         */

    }


    public static MyApplication getInstance() {
        if (instance == null) {
            instance = new MyApplication();
        }
        return instance;
    }

    /**
     * 打开了某个Activity
     *
     * @param activity
     */
    public void addActivity(Activity activity) {
        activities.add(activity);
    }

    /**
     * 关闭了某个Activity
     *
     * @param activity
     */
    public void removeActivity(Activity activity) {
//        activity.finish();
        activities.remove(activity);
    }

    /**
     * 主动销毁某个Activity
     *
     * @param activity
     */
    public static void distoryActivity(Activity activity) {
        activity.finish();
        activities.remove(activity);
    }

    /**
     * 结束所有的Activity然后退出
     */
    public void finishAllActivity() {
        for (Activity activity : activities) {
            if (!activity.isFinishing()) {
                activity.finish();
            }
        }
    }

}

