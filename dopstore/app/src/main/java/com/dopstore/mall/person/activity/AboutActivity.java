package com.dopstore.mall.person.activity;

import android.os.Bundle;
import android.view.KeyEvent;

import com.dopstore.mall.R;
import com.dopstore.mall.base.BaseActivity;
import com.dopstore.mall.util.SkipUtils;

/**
 * 作者：xicheng on 2016/10/21 16:58
 * 类别：
 */

public class AboutActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
        initView();
        initData();
    }

    private void initView() {
        setCustomTitle("关于小海囤",getResources().getColor(R.color.white));
        leftImageBack(R.mipmap.back_arrow);
    }

    private void initData() {
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            SkipUtils.back(this);
            return true;
        } else {
            return super.onKeyDown(keyCode, event);
        }
    }
}
