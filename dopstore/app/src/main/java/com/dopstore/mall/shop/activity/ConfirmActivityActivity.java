package com.dopstore.mall.shop.activity;

import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.dopstore.mall.R;
import com.dopstore.mall.base.BaseActivity;
import com.dopstore.mall.order.activity.CashierActivity;
import com.dopstore.mall.order.adapter.ConfirmOrderAdapter;
import com.dopstore.mall.order.bean.ConfirmOrderData;
import com.dopstore.mall.person.activity.MyAddressActivity;
import com.dopstore.mall.util.Constant;
import com.dopstore.mall.util.LoadImageUtils;
import com.dopstore.mall.util.SkipUtils;
import com.dopstore.mall.view.MyListView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 喜成 on 16/9/13.
 * name
 */
public class ConfirmActivityActivity extends BaseActivity {
    private Button payBt;
    private TextView totalPriceTv;
    private ImageView imageView;
    private TextView titleTv,addressTv,timeTv,typeTv,priceTv,numTv;
    private EditText phoneEt,hintEt;
    private LoadImageUtils loadImageUtils;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirm_activity);
        initView();
        initData();
    }
    private void initView() {
        loadImageUtils=LoadImageUtils.getInstance(this);
        setCustomTitle("活动详情", getResources().getColor(R.color.white_color));
        leftImageBack(R.mipmap.back_arrow);
        payBt=(Button) findViewById(R.id.confirm_activity_pay_bt);
        payBt.setOnClickListener(listener);
        totalPriceTv=(TextView) findViewById(R.id.confirm_activity_total_price);
        titleTv=(TextView) findViewById(R.id.confirm_activity_title);
        imageView=(ImageView) findViewById(R.id.confirm_activity_image);
        addressTv=(TextView) findViewById(R.id.confirm_activity_address);
        timeTv=(TextView) findViewById(R.id.confirm_activity_time);
        typeTv=(TextView) findViewById(R.id.confirm_activity_type);
        priceTv=(TextView) findViewById(R.id.confirm_activity_price);
        numTv=(TextView) findViewById(R.id.confirm_activity_num);
        phoneEt=(EditText) findViewById(R.id.confirm_activity_phone);
        hintEt=(EditText) findViewById(R.id.confirm_activity_hint);

    }
    private void initData() {
        loadImageUtils.displayImage("",imageView, Constant.OPTIONS_SPECIAL_CODE);
        totalPriceTv.setText("¥158.00");
        titleTv.setText("加勒比海一日游");
        addressTv.setText("地址:选武器");
        timeTv.setText("2016-8-8");
        typeTv.setText("家庭套餐");
        priceTv.setText("¥158.00");
        numTv.setText("1");



   }

    View.OnClickListener listener=new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            switch (view.getId()){
                case R.id.confirm_activity_pay_bt:{
                    SkipUtils.directJump(ConfirmActivityActivity.this,CashierActivity.class,false);
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
