package com.dopstore.mall.order.activity;

import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
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
public class ActivityOrderDetailActivity extends BaseActivity {
    private TextView idTv,stateTv,titleTv,addressTv,codeTv,timeTv,typeTv,priceTv,numTv,userMsgTv,totalPriceTv,cheapTv,passPriceTv,truePriceTv;
    private Button submitBt,kFBt;
    private LoadImageUtils loadImage;
    private ImageView shopImage,zxingImage;
    private RelativeLayout zxingLayout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_activity_order_detail);
        initView();
        initData();
    }

    private void initView() {
        loadImage=LoadImageUtils.getInstance(this);
//        Map<String, Object> map = SkipUtils.getMap(this);
//        String type = map.get("title").toString();
//       int  typeId = Integer.parseInt(type);
//        switch (typeId) {
//            case 0: {
//                titleStr = "我的订单";
//            }
//            break;
//            case 1: {
//                titleStr = "待付款";
//            }
//            break;
//            case 2: {
//                titleStr = "待发货";
//            }
//            break;
//            case 3: {
//                titleStr = "待收货";
//            }
//            break;
//        }
        setCustomTitle("订单详情", getResources().getColor(R.color.white_color));
        leftImageBack(R.mipmap.back_arrow);
        idTv=(TextView) findViewById(R.id.activity_detail_id);
        zxingLayout=(RelativeLayout) findViewById(R.id.activity_detail_zxing_layout);
        stateTv=(TextView) findViewById(R.id.activity_detail_state);
        shopImage=(ImageView) findViewById(R.id.activity_detail_image);
        zxingImage=(ImageView) findViewById(R.id.activity_detail_zxing);
        titleTv=(TextView) findViewById(R.id.activity_detail_title);
        addressTv=(TextView) findViewById(R.id.activity_detail_address);
        codeTv=(TextView) findViewById(R.id.activity_detail_zxing_tv);
        timeTv=(TextView) findViewById(R.id.activity_detail_time);
        typeTv=(TextView) findViewById(R.id.activity_detail_type);
        priceTv=(TextView) findViewById(R.id.activity_detail_price);
        numTv=(TextView) findViewById(R.id.activity_detail_num);
        userMsgTv=(TextView) findViewById(R.id.activity_detail_user_msg);
        totalPriceTv=(TextView) findViewById(R.id.activity_detail_total_price);
        cheapTv=(TextView) findViewById(R.id.activity_detail_cheap_price);
        passPriceTv=(TextView) findViewById(R.id.activity_detail_pass_price);
        truePriceTv=(TextView) findViewById(R.id.activity_detail_true_price);
        submitBt=(Button) findViewById(R.id.activity_detail_submit);
        kFBt=(Button) findViewById(R.id.activity_detail_cheap_kefu);
        submitBt.setOnClickListener(listener);
        kFBt.setOnClickListener(listener);
    }

    private void initData() {
        idTv.setText("订单号:16160805071127");
        stateTv.setText("等待付款");
        loadImage.displayImage("",shopImage, Constant.OPTIONS_SPECIAL_CODE);
        loadImage.displayImage("",zxingImage, Constant.OPTIONS_SPECIAL_CODE);
        titleTv.setText("皇家游轮");
        addressTv.setText("地址:吧vjadhsihhvb");
        codeTv.setText("验证码:321321321");
        timeTv.setText("2016-8-8");
        typeTv.setText("家庭套餐");
        priceTv.setText("¥ 87.00");
        numTv.setText("1");
        userMsgTv.setText("尽快发货");
        totalPriceTv.setText("¥ 87.00");
        cheapTv.setText("¥ 0");
        passPriceTv.setText("¥ 0");
        truePriceTv.setText("¥ 87.00");
    }

    View.OnClickListener listener=new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            switch (view.getId()){
                case R.id.activity_detail_submit:{}break;
                case R.id.activity_detail_cheap_kefu:{}break;

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
