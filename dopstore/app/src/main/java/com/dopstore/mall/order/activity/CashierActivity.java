package com.dopstore.mall.order.activity;

import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.dopstore.mall.R;
import com.dopstore.mall.base.BaseActivity;
import com.dopstore.mall.util.SkipUtils;

/**
 * Created by 喜成 on 16/9/13.
 * name
 */
public class CashierActivity extends BaseActivity {
    private RelativeLayout balanceLy,alipayLy,wechatLy;
    private View bv,av,wv;
    private TextView priceTv;
    private Button sureBt;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cashier);
        initView();
        initData();
    }
    private void initView() {
        setCustomTitle("收银台", getResources().getColor(R.color.white_color));
        leftImageBack(R.mipmap.back_arrow);
        balanceLy=(RelativeLayout)findViewById(R.id.cashier_balance_layout);
        bv=findViewById(R.id.cashier_balance_check);
        alipayLy=(RelativeLayout)findViewById(R.id.cashier_alipay_layout);
        av=findViewById(R.id.cashier_alipay_check);
        wechatLy=(RelativeLayout)findViewById(R.id.cashier_wechat_layout);
        wv=findViewById(R.id.cashier_wechat_check);
        priceTv=(TextView)findViewById(R.id.cashier_price);
        sureBt=(Button) findViewById(R.id.cashier_sure_pay_bt);
        balanceLy.setOnClickListener(listener);
        alipayLy.setOnClickListener(listener);
        wechatLy.setOnClickListener(listener);
        sureBt.setOnClickListener(listener);
    }
    private void initData() {
        priceTv.setText("¥ 1122.00");
    }

    View.OnClickListener listener=new View.OnClickListener() {
        @Override
        public void onClick(View view) {
                switch (view.getId()){
                    case R.id.cashier_balance_layout:{
                        bv.setBackgroundResource(R.mipmap.checkbox_checked);
                        av.setBackgroundResource(R.mipmap.checkbox_normal);
                        wv.setBackgroundResource(R.mipmap.checkbox_normal);
                    }break;
                    case R.id.cashier_alipay_layout:{
                        bv.setBackgroundResource(R.mipmap.checkbox_normal);
                        av.setBackgroundResource(R.mipmap.checkbox_checked);
                        wv.setBackgroundResource(R.mipmap.checkbox_normal);
                    }break;
                    case R.id.cashier_wechat_layout:{
                        bv.setBackgroundResource(R.mipmap.checkbox_normal);
                        av.setBackgroundResource(R.mipmap.checkbox_normal);
                        wv.setBackgroundResource(R.mipmap.checkbox_checked);
                    }break;
                    case R.id.cashier_sure_pay_bt:{
                        SkipUtils.directJump(CashierActivity.this,PaySuccessActivity.class,true);
                    }break;
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
