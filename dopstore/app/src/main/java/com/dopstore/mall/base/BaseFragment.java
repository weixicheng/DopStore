package com.dopstore.mall.base;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;

import com.dopstore.mall.util.HttpHelper;
import com.dopstore.mall.util.ProUtils;

import cn.sharesdk.framework.ShareSDK;

/**
 * 作者：xicheng on 16/10/10 16:18
 * 类别：
 */

public class BaseFragment extends Fragment{
    public HttpHelper httpHelper;
    public ProUtils proUtils;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        httpHelper = HttpHelper.getOkHttpClientUtils(context);
        proUtils = new ProUtils(context);
    }

    @Override
    public void onPause() {
        super.onPause();
        if (proUtils.isShowing()){proUtils.dismiss();}
    }

}
