package com.dopstore.mall.person.activity;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.KeyEvent;
import android.widget.CompoundButton;

import com.dopstore.mall.R;
import com.dopstore.mall.base.BaseActivity;
import com.dopstore.mall.util.SkipUtils;
import com.dopstore.mall.view.UISwitchButton;

import cn.jpush.android.api.JPushInterface;

/**
 * Created by 喜成 on 16/9/12.
 * name
 */
public class NoticActivity extends BaseActivity {
    private UISwitchButton switchBt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notice);
        initView();
        initData();
    }

    private void initView() {
        setCustomTitle("消息通知", getResources().getColor(R.color.white_color));
        leftImageBack(R.mipmap.back_arrow);
        switchBt = (UISwitchButton) findViewById(R.id.setting_switch_bt);

    }


    private void initData() {
        SharedPreferences sp = getSharedPreferences("JPush", Context.MODE_PRIVATE);
        String status = sp.getString("status", "1");
        if ("0".equals(status)) {
            switchBt.setChecked(false);
            JPushInterface.stopPush(getApplicationContext());
        } else {
            switchBt.setChecked(true);
            JPushInterface.resumePush(getApplicationContext());
        }
        switchBt.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    setStaues("1");
                    JPushInterface.resumePush(getApplicationContext());
                } else {
                    setStaues("0");
                    JPushInterface.stopPush(getApplicationContext());
                }
            }

        });
    }

    private void setStaues(String status) {
        SharedPreferences sp = getSharedPreferences("JPush", Context.MODE_PRIVATE);
        SharedPreferences.Editor ed = sp.edit();
        ed.putString("status", status);
        ed.commit();
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
