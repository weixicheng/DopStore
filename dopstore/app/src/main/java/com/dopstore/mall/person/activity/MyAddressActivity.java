package com.dopstore.mall.person.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;

import com.dopstore.mall.R;
import com.dopstore.mall.base.BaseActivity;
import com.dopstore.mall.person.adapter.MyAddressAdapter;
import com.dopstore.mall.person.bean.MyAddressData;
import com.dopstore.mall.util.SkipUtils;
import com.dopstore.mall.view.MyListView;

import java.util.ArrayList;
import java.util.List;


/**
 * 我的地址薄
 */
public class MyAddressActivity extends BaseActivity {
    private ListView my_address;
    private RelativeLayout addLayout;
    private List<MyAddressData> listData = new ArrayList<MyAddressData>();
    private MyAddressAdapter mAdapter;
    final int REQUEST_CODE = 1;
    final int RESULT_CODE = 100;
    final int RESULT_CODE_TWO = 200;
    private int status;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_address);
        initView();
        doRequest();
    }

    private void initView() {
        setCustomTitle("选择收货地址", getResources().getColor(R.color.white_color));
        leftImageBack(R.mipmap.back_arrow);
        my_address = (ListView) findViewById(R.id.lv_my_address);
        addLayout = (RelativeLayout) findViewById(R.id.my_address_add_layout);
        addLayout.setOnClickListener(listener);
    }

    private void doRequest() {
        for (int i = 0; i < 5; i++) {
            MyAddressData data = new MyAddressData();
            data.setName("王杰");
            data.setPhone("12345678999");
            data.setAddress("几点技术交底活动速发货多少分的借口就发个快递发几个");
            if (i == 0) {
                data.setIsDefault("1");
            } else {
                data.setIsDefault("0");
            }
            data.setIsCheck("0");
            listData.add(data);
        }
        if (mAdapter == null) {
            mAdapter = new MyAddressAdapter(this, listData);
            my_address.setAdapter(mAdapter);
        }
    }

    OnClickListener listener = new OnClickListener() {
        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.my_address_add_layout: {
                    SkipUtils.directJump(MyAddressActivity.this, NewAddressActivity.class, false);
                }
                break;
            }

        }
    };


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // TODO Auto-generated method stub

        super.onActivityResult(requestCode, resultCode, data);
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
