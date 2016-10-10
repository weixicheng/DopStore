package com.dopstore.mall.order.activity;

import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

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
public class ShopPaySuccessActivity extends BaseActivity {
    private TextView titleText,hintText;
    private Button checkBt;
    private Button continueBt;

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
        titleText = (TextView) findViewById(R.id.pay_success_title);
        hintText = (TextView) findViewById(R.id.pay_success_title_hint);
        checkBt = (Button) findViewById(R.id.pay_success_check_bt);
        continueBt = (Button) findViewById(R.id.pay_success_continue_bt);
        checkBt.setOnClickListener(listener);
        continueBt.setOnClickListener(listener);
    }

    private void initData() {
        titleText.setText("恭喜您!支付成功");
        hintText.setText("请保持手机畅通,以便快递哥哥第一时间联系到您");
    }

    View.OnClickListener listener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.pay_success_check_bt: {
                    Map<String,Object> map=new HashMap<String,Object>();
                    map.put("title","0");
                    SkipUtils.directJump(ShopPaySuccessActivity.this, MyOrderActivity.class,true);
                }
                break;
                case R.id.pay_success_continue_bt: {
                    SkipUtils.directJump(ShopPaySuccessActivity.this, MainActivity.class, true);
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
