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
import com.dopstore.mall.order.adapter.CommOrderAdapter;
import com.dopstore.mall.order.bean.CommOrderData;
import com.dopstore.mall.order.bean.ConfirmOrderData;
import com.dopstore.mall.util.Constant;
import com.dopstore.mall.util.LoadImageUtils;
import com.dopstore.mall.util.SkipUtils;
import com.dopstore.mall.view.MyListView;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by 喜成 on 16/9/12.
 * name
 */
public class OrderDetailActivity extends BaseActivity {
    private TextView idTv,stateTv,passTv,passTimeTv,userTv,userAddressTv,userMsgTv,totalPriceTv,cheapTv,passPriceTv,truePriceTv;
    private Button submitBt,kFBt;
    private RelativeLayout wLayout;
    private LoadImageUtils loadImage;
    private MyListView myListView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_detail);
        initView();
        initData();
    }

    private void initView() {
        loadImage=LoadImageUtils.getInstance(this);
        Map<String, Object> map = SkipUtils.getMap(this);
        String type = map.get("title").toString();
       int  typeId = Integer.parseInt(type);
        switch (typeId) {
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
        }
        setCustomTitle("订单详情", getResources().getColor(R.color.white_color));
        leftImageBack(R.mipmap.back_arrow);
        idTv=(TextView) findViewById(R.id.order_detail_id);
        stateTv=(TextView) findViewById(R.id.order_detail_state);
        wLayout=(RelativeLayout) findViewById(R.id.order_detail_wuliu_layout);
        passTv=(TextView) findViewById(R.id.order_detail_wuliu);
        passTimeTv=(TextView) findViewById(R.id.order_detail_wuliu_time);
        userTv=(TextView) findViewById(R.id.order_detail_user_detail);
        userAddressTv=(TextView) findViewById(R.id.order_detail_user_address);
        userMsgTv=(TextView) findViewById(R.id.order_detail_user_msg);
        totalPriceTv=(TextView) findViewById(R.id.order_detail_total_price);
        cheapTv=(TextView) findViewById(R.id.order_detail_cheap_price);
        passPriceTv=(TextView) findViewById(R.id.order_detail_pass_price);
        truePriceTv=(TextView) findViewById(R.id.order_detail_true_price);
        submitBt=(Button) findViewById(R.id.order_detail_submit);
        kFBt=(Button) findViewById(R.id.order_detail_cheap_kefu);
        myListView=(MyListView) findViewById(R.id.order_detail_listview);
        submitBt.setOnClickListener(listener);
        wLayout.setOnClickListener(listener);
        kFBt.setOnClickListener(listener);
    }

    private void initData() {
        List<CommOrderData> lists=new ArrayList<CommOrderData>();
        idTv.setText("订单号:16160805071127");
        stateTv.setText("等待付款");
        wLayout.setVisibility(View.VISIBLE);
        passTv.setText("物流转运中心圆通快递");
        passTimeTv.setText("2016-06-23 11:39:23");
        userTv.setText("李风华   12345678901");
        userAddressTv.setText("北京朝阳区三环以内建外");
        userMsgTv.setText("尽快发货");
        totalPriceTv.setText("¥ 87.00");
        cheapTv.setText("¥ 0");
        passPriceTv.setText("¥ 0");
        truePriceTv.setText("¥ 87.00");
        for (int i=0;i<1;i++){
            CommOrderData data=new CommOrderData();
            data.setImage("");
            data.setInfo("商品介绍片");
            data.setPrice("156");
            data.setNum("1");
            lists.add(data);
        }
        myListView.setAdapter(new CommOrderAdapter(this,lists));

    }

    View.OnClickListener listener=new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            switch (view.getId()){
                case R.id.order_detail_submit:{}break;
                case R.id.order_detail_cheap_kefu:{}break;
                case R.id.order_detail_wuliu_layout:{
                    SkipUtils.directJump(OrderDetailActivity.this,DeliveryInfoActivity.class,false);
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
