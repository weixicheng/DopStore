package com.dopstore.mall.login.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.widget.ImageView;

import com.dopstore.mall.R;
import com.dopstore.mall.activity.MainActivity;
import com.dopstore.mall.base.BaseActivity;
import com.dopstore.mall.base.MyApplication;
import com.dopstore.mall.util.SkipUtils;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

/**
 * Created by 喜成 on 16/9/30.
 * name
 */
public class SplashActivity extends BaseActivity {
    private ImageView imageView;
    private ImageLoader imageLoader;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        initView();
        initData();
    }

    private void initView() {
        imageLoader=ImageLoader.getInstance();
        imageView=(ImageView) findViewById(R.id.splash_image);
        // 设置异步加载条件
        DisplayImageOptions options = new DisplayImageOptions.Builder()
                .showImageOnLoading(R.mipmap.splash)
                .showImageForEmptyUri(R.mipmap.splash)
                .showImageOnFail(R.mipmap.splash)
                .cacheInMemory(true).cacheOnDisk(false).considerExifParams(true)
                .bitmapConfig(Bitmap.Config.RGB_565).build();
        imageLoader.displayImage("",imageView,options);

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
