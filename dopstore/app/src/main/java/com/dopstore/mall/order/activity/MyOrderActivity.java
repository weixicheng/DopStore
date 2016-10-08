package com.dopstore.mall.order.activity;

import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.dopstore.mall.R;
import com.dopstore.mall.base.BaseActivity;
import com.dopstore.mall.order.adapter.MyOrderAdapter;
import com.dopstore.mall.order.bean.OrderData;
import com.dopstore.mall.util.SkipUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by 喜成 on 16/9/8.
 * name
 */
public class MyOrderActivity extends BaseActivity {
    private ListView lv;
    private List<OrderData> items = new ArrayList<OrderData>();
    private String titleStr = "我的订单";
    private int typeId = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_order);
        initView();
        initData();
    }

    private void initView() {
        Map<String, Object> map = SkipUtils.getMap(this);
        if (map == null) return;
        String type = map.get("title").toString();
        typeId = Integer.parseInt(type);
        switch (typeId) {
            case 0: {
                titleStr = "我的订单";
            }
            break;
            case 1: {
                titleStr = "待付款";
            }
            break;
            case 2: {
                titleStr = "待发货";
            }
            break;
            case 3: {
                titleStr = "待收货";
            }
            break;
        }
        setCustomTitle(titleStr, getResources().getColor(R.color.white_color));
        leftImageBack(R.mipmap.back_arrow);
        lv = (ListView) findViewById(R.id.my_order_lv);
    }

    private void initData() {
        for (int i = 0; i < 2; i++) {
            OrderData data = new OrderData();
            data.setId("订单号:1616080517112" + i);
            data.setState("付款成功");
            data.setImage("");
            data.setTitle("nacnac 宝贝可爱黑白条,清凉舒爽纹萌宝必备T恤");
            data.setType("黑色、34");
            data.setNum("✖️ 1");
            data.setPrice("¥ 379");
            items.add(data);
        }
        lv.setAdapter(new MyOrderAdapter(this, items, typeId));

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                if (i == 0) {
                    Map<String, Object> map = new HashMap<String, Object>();
                    map.put("title", typeId);
                    SkipUtils.jumpForMap(MyOrderActivity.this, OrderDetailActivity.class, map, false);
                } else {
                    SkipUtils.directJump(MyOrderActivity.this, RefundOrderActivity.class, false);
                }
            }
        });
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
