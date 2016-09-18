package com.dopstore.mall.order.activity;

import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.dopstore.mall.R;
import com.dopstore.mall.base.BaseActivity;
import com.dopstore.mall.util.Constant;
import com.dopstore.mall.util.LoadImageUtils;
import com.dopstore.mall.util.SkipUtils;

/**
 * Created by 喜成 on 16/9/12.
 * name
 */
public class RefundOrderActivity extends BaseActivity {
    private TextView idTv,stateTv,titleTv,priceTv,numTv,userTv,userAddressTv;
    private Button subBt;
    private ImageView shopImage;
    private LoadImageUtils loadImage;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_refund_order);
        initView();
        initData();
    }
    private void initView() {
        loadImage=LoadImageUtils.getInstance(this);
        setCustomTitle("订单详情", getResources().getColor(R.color.white_color));
        leftImageBack(R.mipmap.back_arrow);
        idTv=(TextView) findViewById(R.id.refund_order_id);
        stateTv=(TextView) findViewById(R.id.refund_order_state);
        shopImage=(ImageView) findViewById(R.id.refund_order_image);
        titleTv=(TextView) findViewById(R.id.refund_order_title);
        priceTv=(TextView) findViewById(R.id.refund_order_price);
        numTv=(TextView) findViewById(R.id.refund_order_num);
        userTv=(TextView) findViewById(R.id.refund_order_user_detail);
        userAddressTv=(TextView) findViewById(R.id.refund_order_user_address);
        subBt=(Button) findViewById(R.id.refund_order_submit);
    }
    private void initData() {
        idTv.setText("订单号:16160805071127");
        stateTv.setText("等待付款");
        loadImage.displayImage("",shopImage, Constant.OPTIONS_SPECIAL_CODE);
        titleTv.setText("宝贝可爱欧");
        priceTv.setText("¥ 87.00");
        numTv.setText("✖️ 1");
        userTv.setText("李风华   12345678901");
        userAddressTv.setText("北京朝阳区三环以内建外");
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
