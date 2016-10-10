package com.dopstore.mall.order.activity;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.dopstore.mall.R;
import com.dopstore.mall.base.BaseActivity;
import com.dopstore.mall.order.adapter.CommOrderAdapter;
import com.dopstore.mall.order.bean.DetailAddressData;
import com.dopstore.mall.order.bean.DetailOrderListData;
import com.dopstore.mall.order.bean.OrderDetailData;
import com.dopstore.mall.util.Constant;
import com.dopstore.mall.util.HttpHelper;
import com.dopstore.mall.util.ProUtils;
import com.dopstore.mall.util.SkipUtils;
import com.dopstore.mall.util.T;
import com.dopstore.mall.util.URL;
import com.dopstore.mall.view.CommonDialog;
import com.dopstore.mall.view.MyListView;

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
public class RefundShopOrderActivity extends BaseActivity {
    private TextView idTv, stateTv, userTv, userAddressTv;
    private MyListView myListView;
    private Button subBt;
    private OrderDetailData orderDetailData;
    private CommonDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_refund_order);
        initView();
        initData();
    }

    private void initView() {
        setCustomTitle("订单详情", getResources().getColor(R.color.white_color));
        leftImageBack(R.mipmap.back_arrow);
        idTv = (TextView) findViewById(R.id.refund_order_id);
        stateTv = (TextView) findViewById(R.id.refund_order_state);
        userTv = (TextView) findViewById(R.id.refund_order_user_detail);
        userAddressTv = (TextView) findViewById(R.id.refund_order_user_address);
        myListView = (MyListView) findViewById(R.id.refund_order_listview);
        subBt = (Button) findViewById(R.id.refund_order_submit);
        subBt.setOnClickListener(listener);
        Map<String, Object> map = SkipUtils.getMap(this);
        if (map == null) return;
        orderDetailData = (OrderDetailData) map.get(Constant.LIST);
    }

    private void initData() {
        idTv.setText("订单号:" + orderDetailData.getOrder().getOrder_num());
        String type = orderDetailData.getOrder().getStatus();
        String statusStr = getType(type);
        stateTv.setText(statusStr);
        List<DetailOrderListData> lists = orderDetailData.getGoods_relateds();
        myListView.setAdapter(new CommOrderAdapter(this, lists));
        DetailAddressData addressData = orderDetailData.getOrder().getAddress();
        userTv.setText(addressData.getShipping_user() + "  " + addressData.getMobile());
        userAddressTv.setText(addressData.getDetail());
    }

    private String getType(String status) {
        String nameStr = "";
        if ("0".equals(status)) {
            nameStr = "待下单";
        } else if ("1".equals(status)) {
            nameStr = "付款成功";
        } else if ("2".equals(status)) {
            nameStr = "订单取消";
        } else if ("3".equals(status)) {
            nameStr = "待发货";
        } else if ("4".equals(status)) {
            nameStr = "配送中";
        } else if ("5".equals(status)) {
            nameStr = "已完成";
        } else if ("6".equals(status)) {
            nameStr = "退款申请中";
        } else if ("7".equals(status)) {
            nameStr = "退款中";
        } else if ("8".equals(status)) {
            nameStr = "退款成功";
        } else if ("9".equals(status)) {
            nameStr = "等待付款";
        }
        return nameStr;
    }

    View.OnClickListener listener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.refund_order_submit: {
                    refundOrder();
                }
                break;
            }
        }
    };

    private void refundOrder() {
        proUtils.show();
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("order_num", orderDetailData.getOrder().getOrder_num());
        httpHelper.postKeyValuePairAsync(this, URL.ORDER_REFUND, map, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                T.checkNet(RefundShopOrderActivity.this);
                proUtils.dismiss();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String body = response.body().string();
                try {
                    JSONObject jo = new JSONObject(body);
                    String code = jo.optString(Constant.ERROR_CODE);
                    if ("0".equals(code)) {
                        handler.sendEmptyMessage(SUB_SUCCESS_CODE);
                    } else {
                        String msg = jo.optString(Constant.ERROR_MSG);
                        T.show(RefundShopOrderActivity.this, msg);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                proUtils.dismiss();
            }
        }, null);
    }

    private final static int SUB_SUCCESS_CODE = 0;
    private final static int SUB_DIALOG_CODE = 1;
    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case SUB_SUCCESS_CODE: {
                    dialog = new CommonDialog(RefundShopOrderActivity.this, handler, SUB_DIALOG_CODE, "", "退款申请已提交,商家正在处理...", Constant.SHOWCONFIRMBUTTON);
                    dialog.show();
                }
                break;
                case SUB_DIALOG_CODE: {
                    if (dialog.isShowing()) {
                        dialog.dismiss();
                    }
                    SkipUtils.back(RefundShopOrderActivity.this);
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
