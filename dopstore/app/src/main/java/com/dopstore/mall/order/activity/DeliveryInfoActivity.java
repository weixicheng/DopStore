package com.dopstore.mall.order.activity;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;
import android.widget.ListView;
import android.widget.TextView;

import com.dopstore.mall.R;
import com.dopstore.mall.activity.bean.MiddleData;
import com.dopstore.mall.base.BaseActivity;
import com.dopstore.mall.order.adapter.DeliveryInfoAdapter;
import com.dopstore.mall.order.bean.DeliveryData;
import com.dopstore.mall.order.bean.DeliveryData.DeliveryListData;
import com.dopstore.mall.util.Constant;
import com.dopstore.mall.util.HttpHelper;
import com.dopstore.mall.util.ProUtils;
import com.dopstore.mall.util.SkipUtils;
import com.dopstore.mall.util.T;
import com.dopstore.mall.util.URL;
import com.dopstore.mall.util.UserUtils;
import com.dopstore.mall.view.citypicker.CityPicker;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

/**
 * Created by 喜成 on 16/9/12.
 * name
 */
public class DeliveryInfoActivity extends BaseActivity {
    private TextView numTv,nameTv;
    private ListView listView;
    private String logistics_no="";
    private String logistics_company="";

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
        numTv=(TextView) findViewById(R.id.delivery_info_id);
        nameTv=(TextView) findViewById(R.id.delivery_info_name);
        listView=(ListView) findViewById(R.id.delivery_info_listview);
        Map<String,Object> map=SkipUtils.getMap(this);
        if (map==null)return;
        logistics_no=map.get(Constant.ID).toString();
        logistics_company=map.get(Constant.NAME).toString();
    }

    private void initData() {
        getLogistData();
    }

    private DeliveryData deliveryData;
    private List<DeliveryListData> deliveryListData;
    private void getLogistData() {
        proUtils.show();
        Map<String,Object> map=new HashMap<String,Object>();
        map.put("logistics_no",logistics_no);
        map.put("logistics_company",logistics_company);
        httpHelper.postKeyValuePairAsync(this, URL.LOGISTICS_URL,map, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                T.checkNet(DeliveryInfoActivity.this);
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
                       deliveryData= gson.fromJson(
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

    private void refreshListView(){
        numTv.setText(deliveryData.getLogistics_no());
        nameTv.setText(deliveryData.getLogistics_company_name());
        listView.setAdapter(new DeliveryInfoAdapter(this,deliveryListData));
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
