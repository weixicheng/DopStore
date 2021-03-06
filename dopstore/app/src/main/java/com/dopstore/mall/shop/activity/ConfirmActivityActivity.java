package com.dopstore.mall.shop.activity;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.dopstore.mall.R;
import com.dopstore.mall.base.BaseActivity;
import com.dopstore.mall.order.activity.ActivityCashierActivity;
import com.dopstore.mall.order.activity.NoPaySuccessActivity;
import com.dopstore.mall.shop.bean.ActivityDetailBean;
import com.dopstore.mall.util.CommHttp;
import com.dopstore.mall.util.Constant;
import com.dopstore.mall.util.SkipUtils;
import com.dopstore.mall.util.T;
import com.dopstore.mall.util.URL;
import com.dopstore.mall.util.UserUtils;
import com.dopstore.mall.util.Utils;
import com.nostra13.universalimageloader.core.ImageLoader;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;


/**
 * Created by 喜成 on 16/9/13.
 * name
 */
public class ConfirmActivityActivity extends BaseActivity {
    private Button payBt;
    private TextView totalPriceTv;
    private ImageView imageView;
    private TextView titleTv, addressTv, timeTv, typeTv, priceTv, numTv;
    private LinearLayout totalLayout;
    private EditText phoneEt, hintEt;
    private ActivityDetailBean detailBean;
    private String price;
    private ImageLoader imageLoader;
    private CommHttp httpHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirm_activity);
        initView();
        initData();
    }

    private void initView() {
        httpHelper = CommHttp.getInstance();
        imageLoader = ImageLoader.getInstance();
        Map<String, Object> map = SkipUtils.getMap(this);
        if (map == null) return;
        detailBean = (ActivityDetailBean) map.get(Constant.LIST);
        setCustomTitle("活动详情", getResources().getColor(R.color.white_color));
        leftImageBack(R.mipmap.back_arrow);
        payBt = (Button) findViewById(R.id.confirm_activity_pay_bt);
        payBt.setOnClickListener(listener);
        totalPriceTv = (TextView) findViewById(R.id.confirm_activity_total_price);
        titleTv = (TextView) findViewById(R.id.confirm_activity_title);
        imageView = (ImageView) findViewById(R.id.confirm_activity_image);
        addressTv = (TextView) findViewById(R.id.confirm_activity_address);
        timeTv = (TextView) findViewById(R.id.confirm_activity_time);
        typeTv = (TextView) findViewById(R.id.confirm_activity_type);
        priceTv = (TextView) findViewById(R.id.confirm_activity_price);
        numTv = (TextView) findViewById(R.id.confirm_activity_num);
        totalLayout = (LinearLayout) findViewById(R.id.confirm_activity_total_num_layout);
        phoneEt = (EditText) findViewById(R.id.confirm_activity_phone);
        hintEt = (EditText) findViewById(R.id.confirm_activity_hint);
    }

    private void initData() {
        imageLoader.displayImage(detailBean.getCover() + "?imageView2/1/w/180/h/180", imageView);
        totalPriceTv.setText("¥" + Float.parseFloat(detailBean.getPrice()));
        titleTv.setText(detailBean.getName());
        addressTv.setText(detailBean.getAddress());
        String startTime = detailBean.getStart_time();
        String start = Utils.formatTDSecond(startTime);
        timeTv.setText(start);
        String cate = detailBean.getCategory();
        if (TextUtils.isEmpty(cate)) {
            cate = "";
        }
        typeTv.setText(cate);
        price = detailBean.getPrice();
        if (TextUtils.isEmpty(price)) {
            totalLayout.setVisibility(View.GONE);
            priceTv.setText("免费");
        } else {
            totalLayout.setVisibility(View.VISIBLE);
            priceTv.setText("¥" + price);
            totalPriceTv.setText("¥" + price);
        }
    }

    View.OnClickListener listener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.confirm_activity_pay_bt: {
                    checkEdit();
                }
                break;
            }
        }
    };

    private void checkEdit() {
        String phone = phoneEt.getText().toString().trim();
        if (TextUtils.isEmpty(phone)) {
            T.show(this, "请输入联系方式");
            return;
        } else {
            if (!Utils.isPhoneNumberValid(phone)) {
                T.show(this, "请输入正确手机号");
                return;
            } else {
                confirmOrder(phone);
            }
        }
    }

    private void confirmOrder(String phone) {
        String hintStr = hintEt.getText().toString().trim();
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("activity_id", detailBean.getId());
        map.put("user_id", UserUtils.getId(this));
        map.put("phone", phone);
        map.put("note", hintStr);
        httpHelper.post(this, URL.ORDER_ACTIVITY, map, new CommHttp.HttpCallBack() {
            @Override
            public void success(String body) {
                try {
                    JSONObject jo = new JSONObject(body);
                    String code = jo.optString(Constant.ERROR_CODE);
                    if ("0".equals(code)) {
                        String value = jo.optJSONObject("value").toString();
                        Message msg = new Message();
                        msg.what = SURE_ORDER_CODE;
                        msg.obj = value;
                        handler.sendMessage(msg);
                    } else {
                        String msg = jo.optString(Constant.ERROR_MSG);
                        T.show(ConfirmActivityActivity.this, msg);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void failed(String msg) {
                T.checkNet(ConfirmActivityActivity.this);
            }
        });
    }

    private final static int SURE_ORDER_CODE = 0;
    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case SURE_ORDER_CODE: {
                    if (!TextUtils.isEmpty(price)) {
                        String value = msg.obj.toString();
                        try {
                            JSONObject jo = new JSONObject(value);
                            String orderId = jo.optString("order_num");
                            String price = jo.optString("total_fee");
                            if ("0.0".equals(price) || "0".equals(price) || "0.00".equals(price) || TextUtils.isEmpty(price)) {
                                Map<String, Object> map = new HashMap<String, Object>();
                                map.put(Constant.ID, orderId);
                                SkipUtils.jumpForMap(ConfirmActivityActivity.this, NoPaySuccessActivity.class, map, true);
                            } else {
                                Map<String, Object> map = new HashMap<String, Object>();
                                map.put(Constant.ID, orderId);
                                map.put(Constant.PRICE, price);
                                SkipUtils.jumpForMap(ConfirmActivityActivity.this, ActivityCashierActivity.class, map, false);
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    } else {
                        SkipUtils.directJump(ConfirmActivityActivity.this, NoPaySuccessActivity.class, false);
                    }
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
