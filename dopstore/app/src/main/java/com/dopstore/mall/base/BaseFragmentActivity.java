package com.dopstore.mall.base;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;

import com.dopstore.mall.util.HttpHelper;
import com.dopstore.mall.util.ProUtils;

import cn.sharesdk.framework.ShareSDK;

/**
 * 作者：xicheng on 16/10/10 16:18
 * 类别：
 */

public class BaseFragmentActivity extends FragmentActivity{
    public HttpHelper httpHelper;
    public ProUtils proUtils;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        MyApplication.getInstance().addActivity(this);
        ShareSDK.initSDK(this);
        httpHelper = HttpHelper.getOkHttpClientUtils(this);
        proUtils = new ProUtils(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (proUtils.isShowing()){proUtils.dismiss();}
        ShareSDK.stopSDK(this);
        MyApplication.getInstance().removeActivity(this);
    }

}
