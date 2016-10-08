package com.dopstore.mall.person.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.dopstore.mall.R;
import com.dopstore.mall.base.BaseActivity;
import com.dopstore.mall.login.activity.LosePwdActivity;
import com.dopstore.mall.util.SkipUtils;
import com.dopstore.mall.util.UserUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by 喜成 on 16/9/12.
 * name
 */
public class SafeActivity extends BaseActivity {
    private RelativeLayout pwdLy, phoneLy;
    private TextView phoneTv;
    private final static int PHONE_CODE = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_safe);
        initView();
        initData();
    }

    private void initView() {
        setCustomTitle("账号与安全", getResources().getColor(R.color.white_color));
        leftImageBack(R.mipmap.back_arrow);
        pwdLy = (RelativeLayout) findViewById(R.id.safe_pwd_layout);
        phoneLy = (RelativeLayout) findViewById(R.id.safe_phone_layout);
        phoneTv = (TextView) findViewById(R.id.safe_phone);
        pwdLy.setOnClickListener(listener);
        phoneLy.setOnClickListener(listener);
    }


    private void initData() {
        phoneTv.setText(UserUtils.getMobile(this));
    }

    View.OnClickListener listener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.safe_pwd_layout: {
                    Map<String, Object> map = new HashMap<String, Object>();
                    map.put("title", "safe");
                    SkipUtils.jumpForMap(SafeActivity.this, LosePwdActivity.class, map, false);
                }
                break;
                case R.id.safe_phone_layout: {

                }
                break;
            }
        }
    };

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data == null) return;
        if (requestCode == PHONE_CODE) {
            initData();
        }
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
