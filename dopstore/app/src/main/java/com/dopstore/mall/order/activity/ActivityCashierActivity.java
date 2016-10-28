package com.dopstore.mall.order.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.dopstore.mall.R;
import com.dopstore.mall.activity.bean.CityBean;
import com.dopstore.mall.base.BaseActivity;
import com.dopstore.mall.login.bean.UserData;
import com.dopstore.mall.util.ACache;
import com.dopstore.mall.util.CommHttp;
import com.dopstore.mall.util.Constant;
import com.dopstore.mall.util.SkipUtils;
import com.dopstore.mall.util.T;
import com.dopstore.mall.util.URL;
import com.dopstore.mall.util.UserUtils;
import com.pingplusplus.android.Pingpp;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.math.BigDecimal;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;


/**
 * Created by 喜成 on 16/9/13.
 * name 活动支付
 */
public class ActivityCashierActivity extends BaseActivity {
    private RelativeLayout balanceLy, alipayLy, wechatLy;
    private View bv, av, wv;
    private TextView priceTv, balanceHintTv;
    private Button sureBt;
    private String order_id = "";
    private String order_price = "";

    //余额支付渠道
    private static final String CHANNEL_BALANCE = "balance";
    //微信支付渠道
    private static final String CHANNEL_WECHAT = "wx";
    //支付支付渠道
    private static final String CHANNEL_ALIPAY = "alipay";
    //支付类型
    private int PAY_CODE = 0;

    private ACache aCache;
    private CommHttp httpHelper;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cashier);
        initView();
        initData();
    }

    private void initView() {
        aCache = ACache.get(this);
        httpHelper = CommHttp.getInstance();
        setCustomTitle("收银台", getResources().getColor(R.color.white_color));
        leftImageBack(R.mipmap.back_arrow);
        balanceLy = (RelativeLayout) findViewById(R.id.cashier_balance_layout);
        bv = findViewById(R.id.cashier_balance_check);
        alipayLy = (RelativeLayout) findViewById(R.id.cashier_alipay_layout);
        av = findViewById(R.id.cashier_alipay_check);
        wechatLy = (RelativeLayout) findViewById(R.id.cashier_wechat_layout);
        wv = findViewById(R.id.cashier_wechat_check);
        priceTv = (TextView) findViewById(R.id.cashier_price);
        balanceHintTv = (TextView) findViewById(R.id.cashier_balance_content);
        sureBt = (Button) findViewById(R.id.cashier_sure_pay_bt);
        balanceLy.setOnClickListener(listener);
        alipayLy.setOnClickListener(listener);
        wechatLy.setOnClickListener(listener);
        sureBt.setOnClickListener(listener);
    }

    private void initData() {
        Map<String, Object> map = SkipUtils.getMap(this);
        order_id = map.get(Constant.ID).toString();
        order_price = map.get(Constant.PRICE).toString();
        float price = 0;
        if (!TextUtils.isEmpty(order_price)) {
            price = Float.parseFloat(order_price);
            priceTv.setText("¥" + price);
        } else {
            price = 0;
            order_price = "0";
            priceTv.setText("¥ 0");
        }
        String balanceStr = UserUtils.getBalance(this);
        float balanceF = 0;
        if (TextUtils.isEmpty(balanceStr)) {
            balanceF = 0;
        } else {
            balanceF = Float.parseFloat(balanceStr);
        }
        if (balanceF < price) {
            balanceHintTv.setText("余额不足");
            balanceLy.setOnClickListener(null);
            bv.setBackgroundResource(R.mipmap.checkbox_normal);
        } else {
            balanceHintTv.setText("余额支付");
            balanceLy.setOnClickListener(listener);
            bv.setBackgroundResource(R.mipmap.checkbox_checked);
        }
    }

    View.OnClickListener listener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.cashier_balance_layout: {
                    PAY_CODE = 0;
                    bv.setBackgroundResource(R.mipmap.checkbox_checked);
                    av.setBackgroundResource(R.mipmap.checkbox_normal);
                    wv.setBackgroundResource(R.mipmap.checkbox_normal);
                }
                break;
                case R.id.cashier_alipay_layout: {
                    PAY_CODE = 1;
                    bv.setBackgroundResource(R.mipmap.checkbox_normal);
                    av.setBackgroundResource(R.mipmap.checkbox_checked);
                    wv.setBackgroundResource(R.mipmap.checkbox_normal);
                }
                break;
                case R.id.cashier_wechat_layout: {
                    PAY_CODE = 2;
                    bv.setBackgroundResource(R.mipmap.checkbox_normal);
                    av.setBackgroundResource(R.mipmap.checkbox_normal);
                    wv.setBackgroundResource(R.mipmap.checkbox_checked);
                }
                break;
                case R.id.cashier_sure_pay_bt: {
                    payPrice();
                }
                break;
            }
        }
    };

    private void payPrice() {
        String priceNum = priceTv.getText().toString();
        String price = priceNum.substring(1, priceNum.length());
        if (price.equals("")) return;
        String replaceable = String.format("[%s, \\s.]", NumberFormat.getCurrencyInstance(Locale.CHINA).getCurrency().getSymbol(Locale.CHINA));
        String cleanString = price.toString().replaceAll(replaceable, "");
        float payPrice = Float.parseFloat(new BigDecimal(cleanString).toString());
        getServiceCharge(payPrice);
    }

    private void getServiceCharge(float payPrice) {
        balanceLy.setOnClickListener(null);
        alipayLy.setOnClickListener(null);
        wechatLy.setOnClickListener(null);
        final String type = getPay();
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("order_num", order_id);
        map.put("channel", type);
        final String channelStr = type;
        httpHelper.post(this, URL.ACTIVITY_PAYMENT, map, new CommHttp.HttpCallBack() {
            @Override
            public void success(String body) {
                try {
                    JSONObject jo = new JSONObject(body);
                    String code = jo.optString(Constant.ERROR_CODE);
                    if ("0".equals(code)) {
                        if (!"balance".equals(channelStr)) {
                            JSONObject charge = jo.optJSONObject("charge");
                            Message msg = new Message();
                            msg.what = PAY_CHARGE_CODE;
                            msg.obj = charge.toString();
                            handler.sendMessage(msg);
                        } else {
                            upUserData();
                        }
                    } else {
                        String msg = jo.optString(Constant.ERROR_MSG);
                        T.show(ActivityCashierActivity.this, msg);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void failed(String msg) {
                T.checkNet(ActivityCashierActivity.this);
            }
        });
    }

    private void upUserData() {
        String user_id = UserUtils.getId(this);
        httpHelper.get(this, URL.USER_DETAIL + user_id, new CommHttp.HttpCallBack() {
            @Override
            public void success(String body) {
                AnalyData(body);
            }

            @Override
            public void failed(String msg) {
                T.checkNet(ActivityCashierActivity.this);
            }
        });
    }

    private void AnalyData(String body) {
        try {
            JSONObject jo = new JSONObject(body);
            String code = jo.optString(Constant.ERROR_CODE);
            if ("0".equals(code)) {
                String tokenStr = jo.optString(Constant.TOKEN);
                UserUtils.setToken(ActivityCashierActivity.this, tokenStr);
                JSONObject user = jo.optJSONObject(Constant.USER);
                JSONArray citys = jo.optJSONArray(Constant.CITYS);
                List<CityBean> cityList = new ArrayList<CityBean>();
                if (citys.length() > 0) {
                    for (int i = 0; i < citys.length(); i++) {
                        JSONObject city = citys.getJSONObject(i);
                        CityBean cityBean = new CityBean();
                        cityBean.setId(city.optString(Constant.ID));
                        cityBean.setName(city.optString(Constant.NAME));
                        cityList.add(cityBean);
                    }
                    aCache.put(Constant.CITYS, (Serializable) cityList);
                }
                UserData data = new UserData();
                data.setId(user.optString(Constant.ID));
                data.setUsername(user.optString(Constant.USERNAME));
                data.setNickname(user.optString(Constant.NICKNAME));
                data.setGender(user.optString(Constant.GENDER));
                data.setAvatar(user.optString(Constant.AVATAR));
                data.setBirthday(user.optLong(Constant.BIRTHDAY));
                data.setBaby_birthday(user.optLong(Constant.BABY_BIRTHDAY));
                data.setBaby_gender(user.optString(Constant.BABY_GENDER));
                data.setBaby_name(user.optString(Constant.BABY_NAME));
                data.setMobile(user.optString(Constant.MOBILE));
                data.setAddress(user.optString(Constant.CITY));
                data.setBalance(user.optDouble(Constant.BALANCE));
                UserUtils.setData(ActivityCashierActivity.this, data);
                Intent it = new Intent();
                it.setAction(Constant.UP_USER_DATA);
                sendBroadcast(it);
                Map<String, Object> map = new HashMap<>();
                map.put(Constant.ID, order_id);
                SkipUtils.jumpForMap(ActivityCashierActivity.this, PaySuccessActivity.class, map, true);
            } else {
                String msg = jo.optString(Constant.ERROR_MSG);
                T.show(ActivityCashierActivity.this, msg);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    /**
     * 获取支付类型
     *
     * @return
     */
    private String getPay() {
        String pay_type = CHANNEL_BALANCE;

        switch (PAY_CODE) {
            case 0: {
                pay_type = CHANNEL_BALANCE;
            }
            break;
            case 1: {
                pay_type = CHANNEL_ALIPAY;
            }
            break;
            case 2: {
                pay_type = CHANNEL_WECHAT;
            }
            break;
        }
        return pay_type;
    }

    private final static int PAY_CHARGE_CODE = 0;
    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case PAY_CHARGE_CODE: {
                    String charge = msg.obj.toString();
                    if (!TextUtils.isEmpty(charge)) {
                        Pingpp.createPayment(ActivityCashierActivity.this, charge);
                    }
                }
                break;
            }
        }
    };

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        balanceLy.setOnClickListener(listener);
        alipayLy.setOnClickListener(listener);
        wechatLy.setOnClickListener(listener);
        //支付页面返回处理
        if (requestCode == Pingpp.REQUEST_CODE_PAYMENT) {
            if (resultCode == Activity.RESULT_OK) {
                String result = data.getExtras().getString("pay_result");
                /* 处理返回值
                 * "success" - payment succeed
                 * "fail"    - payment failed
                 * "cancel"  - user canceld
                 * "invalid" - payment plugin not installed
                 */
                String errorMsg = data.getExtras().getString("error_msg"); // 错误信息
                //String extraMsg = data.getExtras().getString("extra_msg"); // 错误信息
                if (result.equals("success")) {
                    Map<String, Object> map = new HashMap<>();
                    map.put(Constant.ID, order_id);
                    SkipUtils.jumpForMap(ActivityCashierActivity.this, PaySuccessActivity.class, map, true);
                } else if (result.equals("fail")) {
                    T.show(ActivityCashierActivity.this, "支付失败");
                } else if (result.equals("cancel")) {
                    T.show(ActivityCashierActivity.this, "取消支付");
                } else if (result.equals("invalid")) {
                    T.show(ActivityCashierActivity.this, errorMsg);
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
}
