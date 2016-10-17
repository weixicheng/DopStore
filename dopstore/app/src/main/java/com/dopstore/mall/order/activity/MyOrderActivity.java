package com.dopstore.mall.order.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.dopstore.mall.R;
import com.dopstore.mall.activity.MainActivity;
import com.dopstore.mall.base.BaseActivity;
import com.dopstore.mall.order.adapter.MyOrderAdapter;
import com.dopstore.mall.order.bean.OrderData;
import com.dopstore.mall.order.bean.OrderOtherData;
import com.dopstore.mall.person.activity.MyCollectActivity;
import com.dopstore.mall.util.CommHttp;
import com.dopstore.mall.util.Constant;
import com.dopstore.mall.util.SkipUtils;
import com.dopstore.mall.util.T;
import com.dopstore.mall.util.URL;
import com.dopstore.mall.util.UserUtils;
import com.dopstore.mall.view.PullToRefreshView;
import com.dopstore.mall.view.PullToRefreshView.OnFooterRefreshListener;
import com.dopstore.mall.view.PullToRefreshView.OnHeaderRefreshListener;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by 喜成 on 16/9/8.
 * name
 */
public class MyOrderActivity extends BaseActivity implements OnHeaderRefreshListener, OnFooterRefreshListener {
    private PullToRefreshView pullToRefreshView;
    private ListView lv;
    private LinearLayout errorLayout;
    private TextView loadTv;
    private LinearLayout emptyLayout;
    private View emptyV;
    private TextView emptyTv,emptyLoadTV;
    private List<OrderData> items = new ArrayList<OrderData>();
    private String titleStr = "我的订单";
    private int typeId = 0;
    private String idStr = "";
    private boolean isRefresh = false;


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
                idStr = "9";
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
        errorLayout = (LinearLayout) findViewById(R.id.comm_error_layout);
        loadTv = (TextView) findViewById(R.id.error_data_load_tv);
        emptyLayout = (LinearLayout) findViewById(R.id.comm_empty_layout);
        emptyTv = (TextView) findViewById(R.id.comm_empty_text);
        emptyV = findViewById(R.id.comm_empty_v);
        emptyLoadTV = (TextView) findViewById(R.id.empty_data_load_tv);
        emptyV.setBackgroundResource(R.mipmap.order_empty_logo);
        emptyTv.setText("您还没有相关订单");
        pullToRefreshView = (PullToRefreshView) findViewById(R.id.my_order_pulltorefresh);
        pullToRefreshView.setOnFooterRefreshListener(this);
        pullToRefreshView.onFooterRefreshComplete();
        pullToRefreshView.setOnHeaderRefreshListener(this);
        loadTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (items != null) {
                    items.clear();
                }
                getMyOrder();
            }
        });
        emptyLoadTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SkipUtils.directJump(MyOrderActivity.this, MainActivity.class,true);
            }
        });
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
        if (isRefresh == false) {
            proUtils.show();
        }
        String id = UserUtils.getId(this);
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("pageSize", "10");
        map.put("page", "1");
        map.put("user_id", id);
        if (!TextUtils.isEmpty(idStr)){
            map.put("status", idStr);
        }
        httpHelper.post(this, URL.GOODS_ORDERS, map, new CommHttp.HttpCallBack() {
            @Override
            public void success(String body) {
                errorLayout.setVisibility(View.GONE);
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
                dismissRefresh();
                proUtils.dismiss();
            }

            @Override
            public void failed(String msg) {
                errorLayout.setVisibility(View.VISIBLE);
                proUtils.dismiss();
                dismissRefresh();
            }
        });
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
        if (items.size()>0){
            emptyLayout.setVisibility(View.GONE);
        }else {
            emptyLayout.setVisibility(View.VISIBLE);
        }
        lv.setAdapter(new MyOrderAdapter(this, items));
    }

    private final static int DETAIL_BACK_CODE = 1;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data == null) return;
        if (requestCode == DETAIL_BACK_CODE) {
            Map<String, Object> map = (Map<String, Object>) data.getSerializableExtra("map");
            if (map == null) return;
            String name = map.get(Constant.NAME).toString();
            if ("true".equals(name)) {
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

    @Override
    public void onFooterRefresh(PullToRefreshView view) {
        pullToRefreshView.onFooterRefreshComplete();
    }

    @Override
    public void onHeaderRefresh(PullToRefreshView view) {
        isRefresh = true;
        if (isRefresh) {
            items.clear();
            getMyOrder();
        }
    }

    private void dismissRefresh() {
        if (isRefresh) {
            pullToRefreshView.onHeaderRefreshComplete();
            isRefresh = false;
        }
    }
}
