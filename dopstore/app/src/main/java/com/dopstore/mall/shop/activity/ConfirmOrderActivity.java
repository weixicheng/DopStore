package com.dopstore.mall.shop.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.dopstore.mall.R;
import com.dopstore.mall.activity.bean.GoodBean;
import com.dopstore.mall.base.BaseActivity;
import com.dopstore.mall.order.activity.CashierActivity;
import com.dopstore.mall.order.adapter.ConfirmOrderAdapter;
import com.dopstore.mall.order.bean.ConfirmOrderData;
import com.dopstore.mall.person.activity.MyAddressActivity;
import com.dopstore.mall.person.bean.MyAddressData;
import com.dopstore.mall.util.Constant;
import com.dopstore.mall.util.HttpHelper;
import com.dopstore.mall.util.SkipUtils;
import com.dopstore.mall.util.T;
import com.dopstore.mall.util.URL;
import com.dopstore.mall.util.UserUtils;
import com.dopstore.mall.view.MyListView;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;
import com.squareup.okhttp.internal.framed.FrameReader;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

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
    private List<GoodBean> newListData;
    private List<MyAddressData> addressList = new ArrayList<MyAddressData>();
    private HttpHelper httpHelper;
    private int totalPrice = 0; // 商品总价

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirm_order);
        initView();
        initData();
    }

    private void initView() {
        httpHelper=HttpHelper.getOkHttpClientUtils(this);
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
        Map<String,Object> map=SkipUtils.getMap(this);
        if (map==null)return;
        newListData=(List<GoodBean>) map.get(Constant.LIST);
    }

    private void initData() {
        if (newListData.size()>0){
            for (GoodBean goodBean:newListData){
                totalPrice += goodBean.getCarNum() * goodBean.getPrice();
            }
            totalPriceTv.setText("¥"+totalPrice);
            priceTv.setText("¥"+totalPrice);
            passTv.setText("免运费");
        }else {
            totalPriceTv.setText("¥ 0.00");
            priceTv.setText("¥ 0.00");
            passTv.setText("免运费");
        }
        getAddress();
        myListView.setAdapter(new ConfirmOrderAdapter(this, newListData));
    }


    private void getAddress() {
        String id = UserUtils.getId(this);
        httpHelper.getDataAsync(this, URL.SHIPPINGADDRESS + id + "/shippingaddress", new Callback() {
            @Override
            public void onFailure(Request request, IOException e) {
                T.checkNet(ConfirmOrderActivity.this);
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
                                addressList.add(data);
                            }
                        }
                    } else {
                        String msg = jo.optString(Constant.ERROR_MSG);
                        T.show(ConfirmOrderActivity.this, msg);
                    }
                    handler.sendEmptyMessage(UPDATA_ADDRESS_CODE);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, null);
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
                    SkipUtils.directJumpForResult(ConfirmOrderActivity.this, MyAddressActivity.class, GET_ADDRESS_CODE);
                }
                break;
            }
        }
    };

    private final static int UPDATA_ADDRESS_CODE = 1;

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case UPDATA_ADDRESS_CODE: {
                    if (addressList.size()>0){
                        for (int i=0;i<addressList.size();i++){
                            String isDefault=addressList.get(i).getIs_default();
                            if ("1".equals(isDefault)){
                                MyAddressData myAddressData=addressList.get(i);
                                userTv.setText(myAddressData.getShipping_user()+"   "+myAddressData.getMobile());
                                userAddressTv.setText(myAddressData.getProvince() + myAddressData.getCity() + myAddressData.getArea() + myAddressData.getAddress());
                            }else {
                                MyAddressData myAddressData=addressList.get(0);
                                userTv.setText(myAddressData.getShipping_user()+"   "+myAddressData.getMobile());
                                userAddressTv.setText(myAddressData.getProvince() + myAddressData.getCity() + myAddressData.getArea() + myAddressData.getAddress());
                            }
                        }
                    }else {
                        userTv.setText("");
                        userAddressTv.setText("");
                    }
                }
                break;
            }
        }
    };

    private final static int GET_ADDRESS_CODE=0;
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data==null)return;
        Map<String,Object> map=(Map<String, Object>) data.getSerializableExtra("map");
        if (map==null)return;
        MyAddressData myAddressData=(MyAddressData) map.get(Constant.LIST);
        userTv.setText(myAddressData.getShipping_user()+"   "+myAddressData.getMobile());
        userAddressTv.setText(myAddressData.getProvince() + myAddressData.getCity() + myAddressData.getArea() + myAddressData.getAddress());
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
