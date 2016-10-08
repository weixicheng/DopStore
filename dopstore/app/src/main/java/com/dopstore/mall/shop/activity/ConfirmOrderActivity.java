package com.dopstore.mall.shop.activity;

import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.dopstore.mall.R;
import com.dopstore.mall.base.BaseActivity;
import com.dopstore.mall.order.activity.CashierActivity;
import com.dopstore.mall.order.adapter.ConfirmOrderAdapter;
import com.dopstore.mall.order.bean.ConfirmOrderData;
import com.dopstore.mall.person.activity.MyAddressActivity;
import com.dopstore.mall.util.SkipUtils;
import com.dopstore.mall.view.MyListView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 喜成 on 16/9/13.
 * name
 */
public class ConfirmOrderActivity extends BaseActivity {
    private Button payBt;
    private TextView totalPriceTv;
    private TextView priceTv, passTv;
    private TextView userTv, userAddressTv;
    private RelativeLayout addressLayout;
    private MyListView myListView;
    private ConfirmOrderAdapter adapter;
    private List<ConfirmOrderData> lists = new ArrayList<ConfirmOrderData>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirm_order);
        initView();
        initData();
    }

    private void initView() {
        setCustomTitle("确认订单", getResources().getColor(R.color.white_color));
        leftImageBack(R.mipmap.back_arrow);
        payBt = (Button) findViewById(R.id.confirm_order_pay_bt);
        payBt.setOnClickListener(listener);
        totalPriceTv = (TextView) findViewById(R.id.confirm_order_total_price);
        priceTv = (TextView) findViewById(R.id.confirm_order_price);
        passTv = (TextView) findViewById(R.id.confirm_order_pass_price);
        userTv = (TextView) findViewById(R.id.confirm_order_user_detail);
        userAddressTv = (TextView) findViewById(R.id.confirm_order_user_address);
        addressLayout = (RelativeLayout) findViewById(R.id.confirm_order_address_layout);
        addressLayout.setOnClickListener(listener);
        myListView = (MyListView) findViewById(R.id.confirm_order_listview);

    }

    private void initData() {
        totalPriceTv.setText("¥158.00");
        priceTv.setText("¥158.00");
        passTv.setText("免运费");
        userTv.setText("李阳  123456");
        userAddressTv.setText("北京朝阳区");
        for (int i = 0; i < 1; i++) {
            ConfirmOrderData data = new ConfirmOrderData();
            data.setImage("");
            data.setInfo("商品介绍片");
            data.setTitle("自营");
            data.setPrice("156");
            data.setNum("1");
            lists.add(data);
        }

        myListView.setAdapter(new ConfirmOrderAdapter(this, lists));


    }

    View.OnClickListener listener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.confirm_order_pay_bt: {
                    SkipUtils.directJump(ConfirmOrderActivity.this, CashierActivity.class, false);
                }
                break;
                case R.id.confirm_order_address_layout: {
                    SkipUtils.directJump(ConfirmOrderActivity.this, MyAddressActivity.class, false);
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
