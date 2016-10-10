package com.dopstore.mall.order.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.dopstore.mall.R;
import com.dopstore.mall.base.BaseActivity;
import com.dopstore.mall.order.adapter.MyOrderAdapter;
import com.dopstore.mall.order.bean.OrderData;
import com.dopstore.mall.order.bean.OrderOtherData;
import com.dopstore.mall.util.Constant;
import com.dopstore.mall.util.SkipUtils;
import com.dopstore.mall.util.T;
import com.dopstore.mall.util.URL;
import com.dopstore.mall.util.UserUtils;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

/**
 * Created by 喜成 on 16/9/8.
 * name
 */
public class MyOrderActivity extends BaseActivity {
    private ListView lv;
    private List<OrderData> items = new ArrayList<OrderData>();
    private String titleStr = "我的订单";
    private int typeId = 0;
    private String idStr = "";


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
                idStr = "";
                titleStr = "我的订单";
            }
            break;
            case 1: {
                idStr = "0";
                titleStr = "待付款";
            }
            break;
            case 2: {
                idStr = "3";
                titleStr = "待发货";
            }
            break;
            case 3: {
                idStr = "4";
                titleStr = "待收货";
            }
            break;
        }
        setCustomTitle(titleStr, getResources().getColor(R.color.white_color));
        leftImageBack(R.mipmap.back_arrow);
        lv = (ListView) findViewById(R.id.my_order_lv);
    }

    private void initData() {
        getMyOrder();

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Map<String, Object> map = new HashMap<String, Object>();
                map.put(Constant.ID, items.get(i).getOrder_num());
                SkipUtils.jumpForMapResult(MyOrderActivity.this, OrderDetailActivity.class, map, DETAIL_BACK_CODE);
            }
        });
    }

    private void getMyOrder() {
        proUtils.show();
        String id = UserUtils.getId(this);
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("pageSize", "10");
        map.put("page", "1");
        map.put("user_id", id);
        map.put("status", idStr);
        httpHelper.postKeyValuePairAsync(this, URL.GOODS_ORDERS, map, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                T.checkNet(MyOrderActivity.this);
                proUtils.dismiss();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String body = response.body().string();
                try {
                    JSONObject jo = new JSONObject(body);
                    String code = jo.optString(Constant.ERROR_CODE);
                    if ("0".equals(code)) {
                        Gson gson = new Gson();
                        OrderOtherData middleData = gson.fromJson(
                                body, OrderOtherData.class);
                        items = middleData.getOrders();
                    } else {
                        String msg = jo.optString(Constant.ERROR_MSG);
                        T.show(MyOrderActivity.this, msg);
                    }
                    handler.sendEmptyMessage(UPDATA_ORDER_CODE);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                proUtils.dismiss();
            }
        }, null);
    }

    private final static int UPDATA_ORDER_CODE = 0;
    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case UPDATA_ORDER_CODE: {
                    refreshListView();
                }
                break;
            }
        }
    };

    private void refreshListView() {
        lv.setAdapter(new MyOrderAdapter(this, items));
    }

    private final static int DETAIL_BACK_CODE = 1;
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data==null)return;
        if (requestCode==DETAIL_BACK_CODE){
            Map<String,Object> map=(Map<String,Object>)data.getSerializableExtra("map");
            if (map==null)return;
            String name=map.get(Constant.NAME).toString();
            if ("true".equals(name)){
                items.clear();
                getMyOrder();
            }
        }
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
