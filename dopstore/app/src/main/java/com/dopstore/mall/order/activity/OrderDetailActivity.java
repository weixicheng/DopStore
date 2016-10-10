package com.dopstore.mall.order.activity;

import android.net.Uri;
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
import com.dopstore.mall.base.BaseActivity;
import com.dopstore.mall.order.adapter.CommOrderAdapter;
import com.dopstore.mall.order.bean.CommOrderData;
import com.dopstore.mall.order.bean.DetailAddressData;
import com.dopstore.mall.order.bean.DetailOrderData;
import com.dopstore.mall.order.bean.DetailOrderListData;
import com.dopstore.mall.order.bean.LogisticsData;
import com.dopstore.mall.order.bean.OrderDetailData;
import com.dopstore.mall.order.bean.OrderOtherData;
import com.dopstore.mall.util.Constant;
import com.dopstore.mall.util.HttpHelper;
import com.dopstore.mall.util.LoadImageUtils;
import com.dopstore.mall.util.ProUtils;
import com.dopstore.mall.util.SkipUtils;
import com.dopstore.mall.util.T;
import com.dopstore.mall.util.URL;
import com.dopstore.mall.util.UserUtils;
import com.dopstore.mall.util.Utils;
import com.dopstore.mall.view.MyListView;
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
 * Created by 喜成 on 16/9/12.
 * name
 */
public class OrderDetailActivity extends BaseActivity {
    private TextView idTv, stateTv, passTv, passTimeTv, userTv, userAddressTv, userMsgTv, totalPriceTv, cheapTv, passPriceTv, truePriceTv;
    private Button submitBt, kFBt;
    private RelativeLayout wLayout, submitLy;
    private MyListView myListView;
    private OrderDetailData orderDetailData;
    private String order_num = "";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_detail);
        initView();
        initData();
    }

    private void initView() {
        Map<String, Object> map = SkipUtils.getMap(this);
        if (map == null) return;
        order_num = map.get(Constant.ID).toString();
        setCustomTitle("订单详情", getResources().getColor(R.color.white_color));
        leftImageBack(R.mipmap.back_arrow);
        idTv = (TextView) findViewById(R.id.order_detail_id);
        stateTv = (TextView) findViewById(R.id.order_detail_state);
        wLayout = (RelativeLayout) findViewById(R.id.order_detail_wuliu_layout);
        submitLy = (RelativeLayout) findViewById(R.id.order_detail_submit_layout);
        passTv = (TextView) findViewById(R.id.order_detail_wuliu);
        passTimeTv = (TextView) findViewById(R.id.order_detail_wuliu_time);
        userTv = (TextView) findViewById(R.id.order_detail_user_detail);
        userAddressTv = (TextView) findViewById(R.id.order_detail_user_address);
        userMsgTv = (TextView) findViewById(R.id.order_detail_user_msg);
        totalPriceTv = (TextView) findViewById(R.id.order_detail_total_price);
        cheapTv = (TextView) findViewById(R.id.order_detail_cheap_price);
        passPriceTv = (TextView) findViewById(R.id.order_detail_pass_price);
        truePriceTv = (TextView) findViewById(R.id.order_detail_true_price);
        submitBt = (Button) findViewById(R.id.order_detail_submit);
        kFBt = (Button) findViewById(R.id.order_detail_cheap_kefu);
        myListView = (MyListView) findViewById(R.id.order_detail_listview);
        submitBt.setOnClickListener(listener);
        wLayout.setOnClickListener(listener);
        kFBt.setOnClickListener(listener);
    }

    private void initData() {
        getOrderDetail();
    }

    private void getOrderDetail() {
        proUtils.show();
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("order_num", order_num);
        httpHelper.postKeyValuePairAsync(this, URL.ORDER_GOODS, map, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                T.checkNet(OrderDetailActivity.this);
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
                        orderDetailData = gson.fromJson(
                                body, OrderDetailData.class);
                    } else {
                        String msg = jo.optString(Constant.ERROR_MSG);
                        T.show(OrderDetailActivity.this, msg);
                    }
                    handler.sendEmptyMessage(UPDATA_DEATIL_CODE);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                proUtils.dismiss();
            }
        }, null);
    }

    private final static int UPDATA_DEATIL_CODE = 0;
    private final static int MAKE_SURE_CODE = 1;
    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case UPDATA_DEATIL_CODE: {
                    setDetailData();
                }
                break;
                case MAKE_SURE_CODE:{
                    Map<String,Object> map=new HashMap<String,Object>();
                    map.put(Constant.NAME,"true");
                    SkipUtils.backForMapResult(OrderDetailActivity.this,map);
                }
                break;
            }
        }
    };

    private void setDetailData() {
        idTv.setText("订单号:" + orderDetailData.getOrder().getOrder_num());
        String type = orderDetailData.getOrder().getStatus();
        String statusStr = getType(type);
        submitBt.setText(getSubmitText(type));
        stateTv.setText(statusStr);
        LogisticsData logistData = orderDetailData.getLogistics();
        String zone=logistData.getLogistics_zone();
        if ("null".equals(zone)||TextUtils.isEmpty(zone)) {
            wLayout.setVisibility(View.GONE);
        } else {
            wLayout.setVisibility(View.VISIBLE);
            String logistStr = logistData.getLogistics_zone() + "," + logistData.getLogistics_remark() + "  " + logistData.getLogistics_company();
            passTv.setText(logistStr);
            String time=logistData.getLogistics_time();
            String logistTime ="";
            if (TextUtils.isEmpty(time)){
                logistTime ="";
            }else {
                logistTime = Utils.formatMilli(Long.parseLong(time), "yyyy-MM-dd HH:mm:ss");
            }
            passTimeTv.setText(logistTime);
        }
        DetailAddressData addressData = orderDetailData.getOrder().getAddress();
        userTv.setText(addressData.getShipping_user() + "  " + addressData.getMobile());
        userAddressTv.setText(addressData.getDetail());
        DetailOrderData detailOrderData = orderDetailData.getOrder();
        userMsgTv.setText(detailOrderData.getNote());
        float totleFee = Float.parseFloat(detailOrderData.getTotal_fee());
        float trueFee = Float.parseFloat(detailOrderData.getReal_fee());
        float cheapFee = totleFee - trueFee;
        if (cheapFee<0){
            cheapFee=0;
        }
        totalPriceTv.setText("¥ " + totleFee);
        cheapTv.setText("¥ " + cheapFee);
        passPriceTv.setText("¥ 0");
        truePriceTv.setText("¥ " + trueFee);
        List<DetailOrderListData> lists = orderDetailData.getGoods_relateds();
        myListView.setAdapter(new CommOrderAdapter(this, lists));

    }

    private String getSubmitText(String status) {
        String nameStr = "";
        if ("0".equals(status)) {
            nameStr = "立即支付";
            submitLy.setVisibility(View.VISIBLE);
        } else if ("1".equals(status)) {
            nameStr = "申请退款";
            submitLy.setVisibility(View.VISIBLE);
        } else if ("2".equals(status)) {
            submitLy.setVisibility(View.GONE);
        } else if ("3".equals(status)) {
            nameStr = "申请退款";
            submitLy.setVisibility(View.VISIBLE);
        } else if ("4".equals(status)) {
            nameStr = "确认收货";
            submitLy.setVisibility(View.VISIBLE);
        } else if ("5".equals(status)) {
            submitLy.setVisibility(View.GONE);
        } else if ("6".equals(status)) {
            submitLy.setVisibility(View.GONE);
        } else if ("7".equals(status)) {
            submitLy.setVisibility(View.GONE);
        } else if ("8".equals(status)) {
            submitLy.setVisibility(View.GONE);
        } else if ("9".equals(status)) {
            nameStr = "立即支付";
            submitLy.setVisibility(View.VISIBLE);
        }
        return nameStr;
    }

    private String getType(String status){
        String nameStr="";
        if ("0".equals(status)){
            nameStr="待下单";
        }else if ("1".equals(status)){
            nameStr="付款成功";
        }else if ("2".equals(status)){
            nameStr="订单取消";
        }else if ("3".equals(status)){
            nameStr="待发货";
        }else if ("4".equals(status)){
            nameStr="配送中";
        }else if ("5".equals(status)){
            nameStr="已完成";
        }else if ("6".equals(status)){
            nameStr="退款申请中";
        }else if ("7".equals(status)){
            nameStr="退款中";
        }else if ("8".equals(status)){
            nameStr="退款成功";
        }else if ("9".equals(status)){
            nameStr="等待付款";
        }
        return  nameStr;
    }

    View.OnClickListener listener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.order_detail_submit: {
                    checkSubmitBt();
                }
                break;
                case R.id.order_detail_cheap_kefu: {
                }
                break;
                case R.id.order_detail_wuliu_layout: {
                    Map<String,Object> map=new HashMap<String,Object>();
                    map.put(Constant.ID,orderDetailData.getLogistics().getLogistics_no());
                    map.put(Constant.NAME,orderDetailData.getLogistics().getLogistics_company());
                    SkipUtils.jumpForMap(OrderDetailActivity.this, DeliveryInfoActivity.class, map,false);
                }
                break;

            }

        }
    };

    private void checkSubmitBt() {
        String submitStr = submitBt.getText().toString();
        if ("立即支付".equals(submitStr)) {
            Map<String, Object> map = new HashMap<String, Object>();
            map.put(Constant.ID, orderDetailData.getOrder().getOrder_num());
            map.put(Constant.PRICE, orderDetailData.getOrder().getReal_fee());
            SkipUtils.jumpForMap(OrderDetailActivity.this, ShopCashierActivity.class, map, false);
        } else if ("确认收货".equals(submitStr)) {
                makeSure();
        } else if ("申请退款".equals(submitStr)) {
            Map<String, Object> map = new HashMap<String, Object>();
            map.put(Constant.LIST, orderDetailData);
            SkipUtils.jumpForMap(OrderDetailActivity.this, RefundShopOrderActivity.class, map, false);
        }
    }

    private void makeSure() {
        proUtils.show();
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("order_num", order_num);
        httpHelper.postKeyValuePairAsync(this, URL.ORDER_CONFIRM, map, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                T.checkNet(OrderDetailActivity.this);
                proUtils.dismiss();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String body = response.body().string();
                try {
                    JSONObject jo = new JSONObject(body);
                    String code = jo.optString(Constant.ERROR_CODE);
                    if ("0".equals(code)) {
                        handler.sendEmptyMessage(MAKE_SURE_CODE);
                    } else {
                        String msg = jo.optString(Constant.ERROR_MSG);
                        T.show(OrderDetailActivity.this, msg);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                proUtils.dismiss();
            }
        }, null);
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
