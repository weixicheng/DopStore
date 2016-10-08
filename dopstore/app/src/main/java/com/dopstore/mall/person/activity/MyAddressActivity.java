package com.dopstore.mall.person.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.dopstore.mall.R;
import com.dopstore.mall.base.BaseActivity;
import com.dopstore.mall.person.adapter.MyAddressAdapter;
import com.dopstore.mall.person.bean.MyAddressData;
import com.dopstore.mall.util.Constant;
import com.dopstore.mall.util.HttpHelper;
import com.dopstore.mall.util.ProUtils;
import com.dopstore.mall.util.SkipUtils;
import com.dopstore.mall.util.T;
import com.dopstore.mall.util.URL;
import com.dopstore.mall.util.UserUtils;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
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
    private HttpHelper httpHelper;
    private ProUtils proUtils;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_address);
        initView();
        doRequest();
    }

    private void initView() {
        httpHelper = HttpHelper.getOkHttpClientUtils(this);
        proUtils = new ProUtils(this);
        setCustomTitle("选择收货地址", getResources().getColor(R.color.white_color));
        leftImageBack(R.mipmap.back_arrow);
        my_address = (ListView) findViewById(R.id.lv_my_address);
        addLayout = (RelativeLayout) findViewById(R.id.my_address_add_layout);
        addLayout.setOnClickListener(listener);
    }

    private void doRequest() {
        getAddress();
    }

    private void getAddress() {
        proUtils.show();
        String id = UserUtils.getId(this);
        httpHelper.getDataAsync(this, URL.SHIPPINGADDRESS + id + "/shippingaddress", new Callback() {
            @Override
            public void onFailure(Request request, IOException e) {
                T.checkNet(MyAddressActivity.this);
                proUtils.dismiss();
            }

            @Override
            public void onResponse(Response response) throws IOException {
                String body = response.body().string();
                try {
                    JSONObject jo = new JSONObject(body);
                    String code = jo.optString(Constant.ERROR_CODE);
                    if ("0".equals(code)) {
                        JSONArray ja = jo.getJSONArray(Constant.SHIPPINGADDRESS);
                        if (ja.length() > 0) {
                            for (int i = 0; i < ja.length(); i++) {
                                JSONObject addjo = ja.getJSONObject(i);
                                MyAddressData data = new MyAddressData();
                                data.setId(addjo.optString(Constant.ID));
                                data.setShipping_user(addjo.optString(Constant.SHIPPING_USER));
                                data.setMobile(addjo.optString(Constant.MOBILE));
                                data.setProvince(addjo.optString(Constant.PROVINCE));
                                data.setCity(addjo.optString(Constant.CITY));
                                data.setArea(addjo.optString(Constant.AREA));
                                data.setId_card(addjo.optString(Constant.ID_CARD));
                                data.setAddress(addjo.optString(Constant.ADDRESS));
                                data.setIs_default(addjo.optString(Constant.IS_DEFAULT));
                                data.setIs_check("0");
                                listData.add(data);
                            }
                        }
                        handler.sendEmptyMessage(UPDATE_ADDRESS_CODE);
                    } else {
                        String msg = jo.optString(Constant.ERROR_MSG);
                        T.show(MyAddressActivity.this, msg);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                proUtils.dismiss();
            }
        }, null);
    }

    private void refreshAdapter() {
        if (mAdapter == null) {
            mAdapter = new MyAddressAdapter(this, listData);
            my_address.setAdapter(mAdapter);
        } else {
            mAdapter.upData(listData);
        }

    }

    OnClickListener listener = new OnClickListener() {
        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.my_address_add_layout: {
                    SkipUtils.directJumpForResult(MyAddressActivity.this, NewAddressActivity.class, NEW_ADDRESS_CODE);
                }
                break;
                case R.id.item_my_address_check_layout: {

                    refreshAdapter();
                }
                break;
                case R.id.item_my_address_edit: {

                }
                break;
            }

        }
    };


    private final static int NEW_ADDRESS_CODE = 0;
    private final static int UPDATE_ADDRESS_CODE = 1;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        listData.clear();
        getAddress();
    }

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case UPDATE_ADDRESS_CODE: {
                    refreshAdapter();
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
