package com.dopstore.mall.order.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.dopstore.mall.R;
import com.dopstore.mall.activity.MainActivity;
import com.dopstore.mall.base.BaseActivity;
import com.dopstore.mall.order.adapter.MyActivityAdapter;
import com.dopstore.mall.order.bean.MyActivityData;
import com.dopstore.mall.util.CommHttp;
import com.dopstore.mall.util.Constant;
import com.dopstore.mall.util.SkipUtils;
import com.dopstore.mall.util.T;
import com.dopstore.mall.util.URL;
import com.dopstore.mall.util.UserUtils;
import com.dopstore.mall.view.PullToRefreshView;
import com.dopstore.mall.view.PullToRefreshView.OnFooterRefreshListener;
import com.dopstore.mall.view.PullToRefreshView.OnHeaderRefreshListener;

import org.json.JSONArray;
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
public class MyActivityActivity extends BaseActivity implements OnFooterRefreshListener,OnHeaderRefreshListener{
    private PullToRefreshView pullToRefreshView;
    private ListView lv;
    private LinearLayout errorLayout;
    private TextView loadTv;
    private LinearLayout emptyLayout;
    private View emptyV;
    private TextView emptyTv,emptyLoadTV;
    private List<MyActivityData> items = new ArrayList<MyActivityData>();
    private boolean isRefresh=false;

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
        errorLayout = (LinearLayout) findViewById(R.id.comm_error_layout);
        loadTv = (TextView) findViewById(R.id.error_data_load_tv);
        emptyLayout = (LinearLayout) findViewById(R.id.comm_empty_layout);
        emptyTv = (TextView) findViewById(R.id.comm_empty_text);
        emptyV = findViewById(R.id.comm_empty_v);
        emptyV.setBackgroundResource(R.mipmap.activity_empty_logo);
        emptyLoadTV = (TextView) findViewById(R.id.empty_data_load_tv);
        emptyTv.setText("您还没有相关活动订单");
        pullToRefreshView = (PullToRefreshView) findViewById(R.id.my_activity_pulltorefresh);
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
                SkipUtils.directJump(MyActivityActivity.this, MainActivity.class,true);
            }
        });
    }

    private void initData() {
        getMyOrder();
    }

    private void getMyOrder() {
        if (isRefresh==false){
            proUtils.show();}
        String id = UserUtils.getId(this);
        httpHelper.get(this, URL.ORDER_ACTIVITY_LIST + id, new CommHttp.HttpCallBack() {
            @Override
            public void success(String body) {
                errorLayout.setVisibility(View.GONE);
                try {
                    JSONObject jo = new JSONObject(body);
                    String code = jo.optString(Constant.ERROR_CODE);
                    if ("0".equals(code)) {
                        JSONArray ja = jo.optJSONArray("value");
                        if (ja.length() > 0) {
                            for (int i = 0; i < ja.length(); i++) {
                                JSONObject json = ja.getJSONObject(i);
                                MyActivityData data = new MyActivityData();
                                data.setId(json.optString("order_num"));
                                data.setState(json.optInt("status") + "");
                                data.setTitle(json.optString("name"));
                                data.setIntro(json.optString("address"));
                                data.setImage(json.optString("pic"));
                                items.add(data);
                            }
                        }
                    } else {
                        String msg = jo.optString(Constant.ERROR_MSG);
                        T.show(MyActivityActivity.this, msg);
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
                dismissRefresh();
                proUtils.dismiss();
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
        lv.setAdapter(new MyActivityAdapter(this, items));

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String id = items.get(i).getId();
                String stu = items.get(i).getState();
                Map<String, Object> map = new HashMap<String, Object>();
                map.put(Constant.ID, id);
                map.put("type", stu);
                SkipUtils.jumpForMapResult(MyActivityActivity.this, ActivityOrderDetailActivity.class, map, BACK_DETAIL_CODE);
            }
        });
    }

    private final static int BACK_DETAIL_CODE = 1;
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data==null)return;
        if (requestCode==BACK_DETAIL_CODE){
            Map<String,Object> map=(Map<String, Object>) data.getSerializableExtra("map");
            if (map==null)return;
            String flag=map.get(Constant.ID).toString();
            if ("true".equals(flag)) {
                isRefresh = true;
                if (isRefresh) {
                    items.clear();
                    getMyOrder();
                }
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
        isRefresh=true;
        if (isRefresh) {
            items.clear();
            getMyOrder();
        }
    }

    private void dismissRefresh(){
        if (isRefresh){
            pullToRefreshView.onHeaderRefreshComplete();
            isRefresh=false;
        }
    }
}
