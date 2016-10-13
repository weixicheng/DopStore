package com.dopstore.mall.order.activity;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.dopstore.mall.R;
import com.dopstore.mall.base.BaseActivity;
import com.dopstore.mall.order.adapter.DeliveryInfoAdapter;
import com.dopstore.mall.order.bean.DeliveryData;
import com.dopstore.mall.order.bean.DeliveryData.DeliveryListData;
import com.dopstore.mall.util.CommHttp;
import com.dopstore.mall.util.Constant;
import com.dopstore.mall.util.SkipUtils;
import com.dopstore.mall.util.T;
import com.dopstore.mall.util.URL;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * Created by 喜成 on 16/9/12.
 * name
 */
public class DeliveryInfoActivity extends BaseActivity {
    private TextView numTv, nameTv;
    private ListView listView;
    private String logistics_no = "";
    private String logistics_company = "";
    private LinearLayout errorLayout;
    private TextView loadTv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delivery_info);
        initView();
        initData();
    }

    private void initView() {
        setCustomTitle("配送信息", getResources().getColor(R.color.white_color));
        leftImageBack(R.mipmap.back_arrow);
        errorLayout = (LinearLayout) findViewById(R.id.comm_error_layout);
        loadTv = (TextView) findViewById(R.id.error_data_load_tv);
        numTv = (TextView) findViewById(R.id.delivery_info_id);
        nameTv = (TextView) findViewById(R.id.delivery_info_name);
        listView = (ListView) findViewById(R.id.delivery_info_listview);
        Map<String, Object> map = SkipUtils.getMap(this);
        if (map == null) return;
        logistics_no = map.get(Constant.ID).toString();
        logistics_company = map.get(Constant.NAME).toString();
        loadTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deliveryData = null;
                if (deliveryData != null) {
                    deliveryListData.clear();
                }
                getLogistData();
            }
        });
    }

    private void initData() {
        getLogistData();
    }

    private DeliveryData deliveryData;
    private List<DeliveryListData> deliveryListData;

    private void getLogistData() {
        proUtils.show();
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("logistics_no", logistics_no);
        map.put("logistics_company", logistics_company);
        httpHelper.post(this, URL.LOGISTICS_URL, map, new CommHttp.HttpCallBack() {
            @Override
            public void success(String body) {
                errorLayout.setVisibility(View.GONE);
                try {
                    JSONObject jo = new JSONObject(body);
                    String code = jo.optString(Constant.ERROR_CODE);
                    if ("0".equals(code)) {
                        Gson gson = new Gson();
                        deliveryData = gson.fromJson(
                                body, DeliveryData.class);
                        deliveryListData = deliveryData.getLogistics_list();
                    } else {
                        String msg = jo.optString(Constant.ERROR_MSG);
                        T.show(DeliveryInfoActivity.this, msg);
                    }
                    handler.sendEmptyMessage(UPDATA_ORDER_CODE);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                proUtils.dismiss();
            }

            @Override
            public void failed(String msg) {
                errorLayout.setVisibility(View.VISIBLE);
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
        numTv.setText(deliveryData.getLogistics_no());
        nameTv.setText(deliveryData.getLogistics_company_name());
        listView.setAdapter(new DeliveryInfoAdapter(this, deliveryListData));
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
