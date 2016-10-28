package com.dopstore.mall.order.activity;

import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;

import com.dopstore.mall.R;
import com.dopstore.mall.activity.MainActivity;
import com.dopstore.mall.base.BaseActivity;
import com.dopstore.mall.util.Constant;
import com.dopstore.mall.util.SkipUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by 喜成 on 16/9/13.
 * name
 */
public class NoPaySuccessActivity extends BaseActivity {
    private Button checkBt;
    private Button continueBt;
    private String order_id = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pay_success);
        initView();
        initData();
    }

    private void initView() {
        setCustomTitle("支付成功", getResources().getColor(R.color.white_color));
        leftImageBack(R.mipmap.back_arrow);
        checkBt = (Button) findViewById(R.id.pay_success_check_bt);
        continueBt = (Button) findViewById(R.id.pay_success_continue_bt);
        checkBt.setOnClickListener(listener);
        continueBt.setOnClickListener(listener);
    }

    private void initData() {
        Map<String, Object> map = SkipUtils.getMap(this);
        if (map == null) {
            return;
        }
        order_id = map.get(Constant.ID).toString();
    }

    View.OnClickListener listener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.pay_success_check_bt: {
                    Map<String, Object> map = new HashMap<String, Object>();
                    map.put(Constant.ID, order_id);
                    map.put("type", "3");
                    SkipUtils.jumpForMap(NoPaySuccessActivity.this, ActivityOrderDetailActivity.class, map, true);
                }
                break;
                case R.id.pay_success_continue_bt: {
                    Map<String, Object> map = new HashMap<String, Object>();
                    map.put(Constant.ID, "1");
                    SkipUtils.jumpForMap(NoPaySuccessActivity.this, MainActivity.class, map, true);
                }
                break;
            }
        }
    };

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
