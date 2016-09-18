package com.dopstore.mall.order.activity;

import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.dopstore.mall.R;
import com.dopstore.mall.base.BaseActivity;
import com.dopstore.mall.order.adapter.MyActivityAdapter;
import com.dopstore.mall.order.bean.MyActivityData;
import com.dopstore.mall.util.SkipUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 喜成 on 16/9/8.
 * name
 */
public class MyActivityActivity extends BaseActivity {
    private ListView lv;
    private List<MyActivityData> items = new ArrayList<MyActivityData>();
    private MyActivityAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_activity);
        initView();
        initData();
    }

    private void initView() {
        setCustomTitle("我的活动", getResources().getColor(R.color.white_color));
        leftImageBack(R.mipmap.back_arrow);
        lv = (ListView) findViewById(R.id.my_activity_lv);
    }

    private void initData() {
        for (int i = 0; i < 2; i++) {
            MyActivityData data = new MyActivityData();
            data.setId("订单号:1616080517112" + i);
            data.setState("付款成功");
            data.setTitle("皇家加勒比游轮海洋量子号韩国5日游");
            data.setIntro("上海-仁川-上海5天4晚");
            items.add(data);
        }
        lv.setAdapter(new MyActivityAdapter(this, items));

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                SkipUtils.directJump(MyActivityActivity.this,ActivityOrderDetailActivity.class,false);
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
