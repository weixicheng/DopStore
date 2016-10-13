package com.dopstore.mall.base;

import android.content.Context;
import android.support.v4.app.Fragment;

import com.dopstore.mall.util.CommHttp;
import com.dopstore.mall.util.ProUtils;

/**
 * 作者：xicheng on 16/10/10 16:18
 * 类别：
 */

public class BaseFragment extends Fragment{
    public CommHttp httpHelper;
    public ProUtils proUtils;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        httpHelper = CommHttp.getInstance(context);
        proUtils = new ProUtils(context);
    }

    @Override
    public void onPause() {
        super.onPause();
        if (proUtils.isShowing()){proUtils.dismiss();}
    }

}
