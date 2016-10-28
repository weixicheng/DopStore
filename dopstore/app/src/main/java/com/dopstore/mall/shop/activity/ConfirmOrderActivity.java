package com.dopstore.mall.shop.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.dopstore.mall.R;
import com.dopstore.mall.activity.bean.GoodBean;
import com.dopstore.mall.base.BaseActivity;
import com.dopstore.mall.order.activity.ShopCashierActivity;
import com.dopstore.mall.order.adapter.ConfirmOrderAdapter;
import com.dopstore.mall.person.activity.MyAddressActivity;
import com.dopstore.mall.person.bean.MyAddressData;
import com.dopstore.mall.util.CommHttp;
import com.dopstore.mall.util.Constant;
import com.dopstore.mall.util.SkipUtils;
import com.dopstore.mall.util.T;
import com.dopstore.mall.util.URL;
import com.dopstore.mall.util.UserUtils;
import com.dopstore.mall.util.Utils;
import com.dopstore.mall.view.MyListView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
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
    private EditText hintText;
    private TextView userTv, userAddressTv;
    private RelativeLayout addressLayout;
    private MyListView myListView;
    private List<GoodBean> newListData;
    private List<MyAddressData> addressList = new ArrayList<MyAddressData>();
    private Double totalPrice = 0.00; // 商品总价
    private String address_id = "";
    private CommHttp httpHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirm_order);
        initView();
        initData();
    }

    private void initView() {
        httpHelper = CommHttp.getInstance();
        setCustomTitle("确认订单", getResources().getColor(R.color.white_color));
        leftImageBack(R.mipmap.back_arrow);
        payBt = (Button) findViewById(R.id.confirm_order_pay_bt);
        payBt.setOnClickListener(listener);
        totalPriceTv = (TextView) findViewById(R.id.confirm_order_total_price);
        hintText = (EditText) findViewById(R.id.comm_order_hint_et);
        priceTv = (TextView) findViewById(R.id.confirm_order_price);
        passTv = (TextView) findViewById(R.id.confirm_order_pass_price);
        userTv = (TextView) findViewById(R.id.confirm_order_user_detail);
        userAddressTv = (TextView) findViewById(R.id.confirm_order_user_address);
        addressLayout = (RelativeLayout) findViewById(R.id.confirm_order_address_layout);
        addressLayout.setOnClickListener(listener);
        myListView = (MyListView) findViewById(R.id.confirm_order_listview);
        Map<String, Object> map = SkipUtils.getMap(this);
        if (map == null) return;
        newListData = (List<GoodBean>) map.get(Constant.LIST);
    }

    private void initData() {
        if (newListData.size() > 0) {
            for (GoodBean goodBean : newListData) {
                totalPrice += goodBean.getCarNum() * goodBean.getPrice();
            }
            String totalStr = "";
            if (Utils.isDouble(totalPrice.toString())) {
                totalStr = Utils.format(totalPrice);
            } else {
                totalStr = totalPrice + "";
            }
            totalPriceTv.setText("¥" + totalStr);
            priceTv.setText("¥" + totalStr);
            passTv.setText("免运费");
        } else {
            totalPriceTv.setText("¥ 0.00");
            priceTv.setText("¥ 0.00");
            passTv.setText("免运费");
        }
        getAddress();
        myListView.setAdapter(new ConfirmOrderAdapter(this, newListData));
    }


    private void getAddress() {
        String id = UserUtils.getId(this);
        httpHelper.get(this, URL.SHIPPINGADDRESS + id + "/shippingaddress", new CommHttp.HttpCallBack() {
            @Override
            public void success(String body) {
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

            @Override
            public void failed(String msg) {
                T.checkNet(ConfirmOrderActivity.this);
            }
        });
    }

    View.OnClickListener listener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.confirm_order_pay_bt: {
                    checkData();
                }
                break;
                case R.id.confirm_order_address_layout: {
                    Map<String, Object> map = new HashMap<String, Object>();
                    map.put(Constant.ID, "1");
                    SkipUtils.jumpForMapResult(ConfirmOrderActivity.this, MyAddressActivity.class, map, GET_ADDRESS_CODE);
                }
                break;
            }
        }
    };

    private void checkData() {
        JSONArray ja = new JSONArray();
        if (newListData.size() > 0) {
            for (GoodBean goodBean : newListData) {
                try {
                    JSONObject jo = new JSONObject();
                    jo.put("goods_id", goodBean.getId());
                    String num = goodBean.getGoods_sku_id();
                    if (TextUtils.isEmpty(num)) {
                        num = "";
                    }
                    jo.put("goods_sku_id", num);
                    jo.put("num", goodBean.getCarNum());
                    ja.put(jo);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        } else {
            ja = null;
        }
        String noteStr = hintText.getText().toString().trim();
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("user_id", UserUtils.getId(this));
        params.put("goods_relateds", ja.toString());
        params.put("address_id", address_id);
        if (!TextUtils.isEmpty(noteStr)) {
            params.put("note", noteStr);
        }
        getOrderID(params);
    }

    private void getOrderID(Map<String, Object> params) {
        httpHelper.post(this, URL.CART_CREATE_ORDER, params, new CommHttp.HttpCallBack() {
            @Override
            public void success(String body) {
                try {
                    JSONObject jo = new JSONObject(body);
                    String code = jo.optString(Constant.ERROR_CODE);
                    if ("0".equals(code)) {
                        String order_num = jo.optString("order_num");
                        Message msg = new Message();
                        msg.what = INTENT_TO_PAY_CODE;
                        msg.obj = order_num;
                        handler.sendMessage(msg);
                    } else {
                        String msg = jo.optString(Constant.ERROR_MSG);
                        T.show(ConfirmOrderActivity.this, msg);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void failed(String msg) {
                T.show(ConfirmOrderActivity.this, msg);
            }
        });
    }

    private final static int UPDATA_ADDRESS_CODE = 1;
    private final static int INTENT_TO_PAY_CODE = 2;

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case UPDATA_ADDRESS_CODE: {
                    if (addressList.size() > 0) {
                        for (int i = 0; i < addressList.size(); i++) {
                            String isDefault = addressList.get(i).getIs_default();
                            if ("1".equals(isDefault)) {
                                MyAddressData myAddressData = addressList.get(i);
                                address_id = myAddressData.getId();
                                userTv.setText(myAddressData.getShipping_user() + "   " + myAddressData.getMobile());
                                userAddressTv.setText(myAddressData.getProvince() + myAddressData.getCity() + myAddressData.getArea() + myAddressData.getAddress());
                            } else {
                                MyAddressData myAddressData = addressList.get(0);
                                address_id = myAddressData.getId();
                                userTv.setText(myAddressData.getShipping_user() + "   " + myAddressData.getMobile());
                                userAddressTv.setText(myAddressData.getProvince() + myAddressData.getCity() + myAddressData.getArea() + myAddressData.getAddress());
                            }
                        }
                    } else {
                        address_id = "";
                        userTv.setText("");
                        userAddressTv.setText("");
                    }
                }
                break;
                case INTENT_TO_PAY_CODE: {
                    Map<String, Object> map = new HashMap<String, Object>();
                    map.put(Constant.ID, msg.obj);
                    map.put(Constant.PRICE, totalPrice);
                    SkipUtils.jumpForMap(ConfirmOrderActivity.this, ShopCashierActivity.class, map, false);
                }
                break;
            }
        }
    };

    private final static int GET_ADDRESS_CODE = 0;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data == null) return;
        Map<String, Object> map = (Map<String, Object>) data.getSerializableExtra("map");
        if (map == null) return;
        MyAddressData myAddressData = (MyAddressData) map.get(Constant.LIST);
        address_id = myAddressData.getId();
        userTv.setText(myAddressData.getShipping_user() + "   " + myAddressData.getMobile());
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
