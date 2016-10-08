package com.dopstore.mall.order.activity;

import android.os.Bundle;
import android.view.KeyEvent;

import com.dopstore.mall.R;
import com.dopstore.mall.base.BaseActivity;
import com.dopstore.mall.util.SkipUtils;

/**
 * Created by 喜成 on 16/9/12.
 * name
 */
public class DeliveryInfoActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delivery_info);
        initView();
        initData();
    }

    private void initView() {
        setCustomTitle("配送信息", getResources().getColor(R.color.white_color));
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
