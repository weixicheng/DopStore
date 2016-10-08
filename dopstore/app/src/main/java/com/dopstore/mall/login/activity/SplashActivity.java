package com.dopstore.mall.login.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;

import com.dopstore.mall.R;
import com.dopstore.mall.activity.MainActivity;
import com.dopstore.mall.base.BaseActivity;
import com.dopstore.mall.base.MyApplication;
import com.dopstore.mall.util.SkipUtils;

/**
 * Created by 喜成 on 16/9/30.
 * name
 */
public class SplashActivity extends BaseActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        initData();
    }

    private void initData() {
        if (getShow()) {
            setLogin(true);
            // 闪屏的核心代码
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    Intent intent = new Intent(SplashActivity.this, MainActivity.class);
                    startActivity(intent);
                    MyApplication.distoryActivity(SplashActivity.this);
                    finish();
                }
            }, 2000); // 启动动画持续2秒钟
        } else {
            setLogin(true);
            // 闪屏的核心代码
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    SkipUtils.directJump(SplashActivity.this, WelcomePageActivity.class, true);
                }
            }, 2000); // 启动动画持续2秒钟
        }
    }

    private void setLogin(boolean flag) {
        SharedPreferences sp = getSharedPreferences("splash", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putBoolean("isFirst", flag);
        editor.commit();
    }

    private boolean getShow() {
        SharedPreferences sp = getSharedPreferences("splash", Context.MODE_PRIVATE);
        return sp.getBoolean("isFirst", false);
    }
}
