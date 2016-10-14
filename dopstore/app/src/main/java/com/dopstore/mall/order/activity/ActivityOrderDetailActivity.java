package com.dopstore.mall.order.activity;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.dopstore.mall.R;
import com.dopstore.mall.base.BaseActivity;
import com.dopstore.mall.order.bean.ActivityOrderDetailBean;
import com.dopstore.mall.util.CommHttp;
import com.dopstore.mall.util.Constant;
import com.dopstore.mall.util.LoadImageUtils;
import com.dopstore.mall.util.SkipUtils;
import com.dopstore.mall.util.T;
import com.dopstore.mall.util.URL;
import com.dopstore.mall.util.Utils;
import com.dopstore.mall.view.CommonDialog;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;


/**
 * Created by 喜成 on 16/9/12.
 * name
 */
public class ActivityOrderDetailActivity extends BaseActivity {
    private TextView idTv, stateTv, titleTv, addressTv, codeTv, codeTime, timeTv, typeTv, priceTv, userMsgTv, totalPriceTv, cheapTv, passPriceTv, truePriceTv;
    private Button submitBt, kFBt;
    private LoadImageUtils loadImage;
    private ImageView shopImage, zxingImage;
    private RelativeLayout zxingLayout, submitLayout;
    private LinearLayout bigZxingLayout;
    private ImageView bigZxingImage;
    private String id;
    private ActivityOrderDetailBean detailBean;
    private CommonDialog dialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_activity_order_detail);
        initView();
        initData();
    }

    private void initView() {
        loadImage = LoadImageUtils.getInstance(this);
        setCustomTitle("订单详情", getResources().getColor(R.color.white_color));
        leftImageBack(R.mipmap.back_arrow);
        idTv = (TextView) findViewById(R.id.activity_detail_id);
        zxingLayout = (RelativeLayout) findViewById(R.id.activity_detail_zxing_layout);
        stateTv = (TextView) findViewById(R.id.activity_detail_state);
        shopImage = (ImageView) findViewById(R.id.activity_detail_image);
        zxingImage = (ImageView) findViewById(R.id.activity_detail_zxing);
        bigZxingLayout = (LinearLayout) findViewById(R.id.activity_detail_big_zxing_layout);
        bigZxingImage = (ImageView) findViewById(R.id.activity_detail_big_zxing);
        titleTv = (TextView) findViewById(R.id.activity_detail_title);
        addressTv = (TextView) findViewById(R.id.activity_detail_address);
        codeTime = (TextView) findViewById(R.id.activity_detail_zxing_time);
        codeTv = (TextView) findViewById(R.id.activity_detail_zxing_tv);
        timeTv = (TextView) findViewById(R.id.activity_detail_time);
        typeTv = (TextView) findViewById(R.id.activity_detail_type);
        priceTv = (TextView) findViewById(R.id.activity_detail_price);
        userMsgTv = (TextView) findViewById(R.id.activity_detail_user_msg);
        totalPriceTv = (TextView) findViewById(R.id.activity_detail_total_price);
        cheapTv = (TextView) findViewById(R.id.activity_detail_cheap_price);
        passPriceTv = (TextView) findViewById(R.id.activity_detail_pass_price);
        truePriceTv = (TextView) findViewById(R.id.activity_detail_true_price);
        submitBt = (Button) findViewById(R.id.activity_detail_submit);
        kFBt = (Button) findViewById(R.id.activity_detail_cheap_kefu);
        submitLayout = (RelativeLayout) findViewById(R.id.activity_detail_submit_layout);
        submitBt.setOnClickListener(listener);
        kFBt.setOnClickListener(listener);
        bigZxingImage.setOnClickListener(listener);
        Map<String, Object> map = SkipUtils.getMap(this);
        if (map == null) return;
        id = map.get(Constant.ID).toString();
        String typeStr = map.get("type").toString();
        int type = Integer.parseInt(typeStr);
        switch (type) {
            case 0: {
                submitLayout.setVisibility(View.VISIBLE);
                submitBt.setText("立即付款");
            }
            break;
            case 1: {
                submitLayout.setVisibility(View.VISIBLE);
                submitBt.setText("申请退款");
            }
            break;
            case 2:
            case 3:
            case 4:
            case 5:
            case 6:
            case 7: {
                submitLayout.setVisibility(View.GONE);
            }
            break;

        }
    }

    private void initData() {
        getOrderDetail();
    }

    private void getOrderDetail() {
        proUtils.show();
        httpHelper.get(this, URL.ORDER_ACTIVITY + "/" + id, new CommHttp.HttpCallBack() {
            @Override
            public void success(String body) {
                try {
                    JSONObject jo = new JSONObject(body);
                    String code = jo.optString(Constant.ERROR_CODE);
                    if ("0".equals(code)) {
                        JSONObject value = jo.optJSONObject("value");
                        detailBean = new ActivityOrderDetailBean();
                        detailBean.setOrder_num(value.optString("order_num"));
                        detailBean.setStatus(value.optInt("status"));
                        detailBean.setPic(value.optString("pic"));
                        detailBean.setName(value.optString("name"));
                        detailBean.setAddress(value.optString("address"));
                        detailBean.setStart_time(value.optLong("start_time"));
                        detailBean.setCategory(value.optString("category"));
                        detailBean.setPrice(value.optDouble("price"));
                        detailBean.setTotal_fee(value.optDouble("total_fee"));
                        detailBean.setCode(value.optString("code"));
                        detailBean.setQ_code(value.optString("q_code"));
                        detailBean.setUsed_time(value.optString("used_time"));
                        detailBean.setFreight(value.optString("freight"));
                        detailBean.setBenefit(value.optString("benefit"));
                        detailBean.setNote(value.optString("note"));
                    } else {
                        String msg = jo.optString(Constant.ERROR_MSG);
                        T.show(ActivityOrderDetailActivity.this, msg);
                    }
                    handler.sendEmptyMessage(UPDATA_ORDER_CODE);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                proUtils.dismiss();
            }

            @Override
            public void failed(String msg) {
                T.checkNet(ActivityOrderDetailActivity.this);
                proUtils.dismiss();
            }
        });
    }

    View.OnClickListener listener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.activity_detail_submit: {
                    String submitText=submitBt.getText().toString();
                    if ("立即付款".equals(submitText)){
                        Map<String,Object> map=new HashMap<String,Object>();
                        map.put(Constant.ID,detailBean.getOrder_num());
                        map.put(Constant.PRICE,detailBean.getTotal_fee());
                        SkipUtils.jumpForMap(ActivityOrderDetailActivity.this,ActivityCashierActivity.class,map,false);
                    }else if ("申请退款".equals(submitText)){
                        refundOrder();
                    }
                }
                break;
                case R.id.activity_detail_cheap_kefu: {
                }
                break;
                case R.id.activity_detail_zxing: {
                    bigZxingLayout.setVisibility(View.VISIBLE);
                }
                break;
                case R.id.activity_detail_big_zxing: {
                    bigZxingLayout.setVisibility(View.GONE);
                }
                break;

            }

        }
    };


    private void refundOrder() {
        proUtils.show();
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("order_num", detailBean.getOrder_num());
        httpHelper.post(this, URL.ACTIVITY_REFUND, map, new CommHttp.HttpCallBack() {
            @Override
            public void success(String body) {
                try {
                    JSONObject jo = new JSONObject(body);
                    String code = jo.optString(Constant.ERROR_CODE);
                    if ("0".equals(code)) {
                        handler.sendEmptyMessage(SUB_SUCCESS_CODE);
                    } else {
                        String msg = jo.optString(Constant.ERROR_MSG);
                        T.show(ActivityOrderDetailActivity.this, msg);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                proUtils.dismiss();
            }

            @Override
            public void failed(String msg) {
                T.checkNet(ActivityOrderDetailActivity.this);
                proUtils.dismiss();
            }
        });
    }

    private final static int UPDATA_ORDER_CODE = 0;
    private final static int SUB_SUCCESS_CODE = 1;
    private final static int SUB_DIALOG_CODE = 2;
    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case UPDATA_ORDER_CODE: {
                    setData();
                }
                break;
                case SUB_SUCCESS_CODE: {
                    dialog = new CommonDialog(ActivityOrderDetailActivity.this, handler, SUB_DIALOG_CODE, "", "退款申请已提交,商家正在处理...", Constant.SHOWCONFIRMBUTTON);
                    dialog.show();
                }
                break;
                case SUB_DIALOG_CODE: {
                    if (dialog.isShowing()) {
                        dialog.dismiss();
                    }
                    Map<String,Object> map=new HashMap<String,Object>();
                    map.put(Constant.ID,"true");
                    SkipUtils.backForMapResult(ActivityOrderDetailActivity.this,map);
                }
                break;
            }
        }
    };

    private void setData() {
        idTv.setText("订单号:" + detailBean.getOrder_num());
        String zimage = detailBean.getQ_code();
        if (TextUtils.isEmpty(zimage)) {
            zxingLayout.setVisibility(View.GONE);
        } else {
            zxingLayout.setVisibility(View.VISIBLE);
            loadImage.displayImage(zimage, zxingImage);
            loadImage.displayImage(zimage, bigZxingImage);
            codeTv.setText("验证码:" + detailBean.getCode());
        }
        zxingImage.setOnClickListener(listener);
        int type = detailBean.getStatus();
        String typeName = "";
        switch (type) {
            case 0: {
                typeName = "等待付款";
                zxingLayout.setVisibility(View.GONE);
            }
            break;
            case 1: {
                zxingLayout.setVisibility(View.VISIBLE);
                typeName = "付款成功";
            }
            break;
            case 2: {
                zxingLayout.setVisibility(View.VISIBLE);
                typeName = "订单取消";
            }
            break;
            case 3: {
                zxingLayout.setVisibility(View.VISIBLE);
                typeName = "报名成功";
            }
            break;
            case 4: {
                zxingLayout.setVisibility(View.VISIBLE);
                typeName = "退款申请中";
            }
            break;
            case 5: {
                zxingLayout.setVisibility(View.VISIBLE);
                typeName = "退款中";
            }
            break;
            case 6: {
                zxingLayout.setVisibility(View.VISIBLE);
                typeName = "退款成功";
            }
            break;
            case 7: {
                zxingLayout.setVisibility(View.VISIBLE);
                typeName = "已完成";
            }
            break;
        }
        stateTv.setText(typeName);
        loadImage.displayImage(detailBean.getPic(), shopImage);

        titleTv.setText(detailBean.getName());
        addressTv.setText("地址:" + detailBean.getAddress());
        String time = Utils.formatMilli(detailBean.getStart_time(), "yyyy-MM-dd");
        timeTv.setText(time);
        typeTv.setText(detailBean.getCategory());
        priceTv.setText("¥" + detailBean.getPrice());
        userMsgTv.setText(detailBean.getNote());
        String useTime = detailBean.getUsed_time();
        if (TextUtils.isEmpty(useTime)) {
            codeTime.setVisibility(View.GONE);
        } else {
            codeTime.setText(useTime + " 已使用");
            codeTime.setVisibility(View.VISIBLE);
        }
        totalPriceTv.setText("¥" + detailBean.getTotal_fee());
        cheapTv.setText("¥" + detailBean.getBenefit());
        passPriceTv.setText("¥" + detailBean.getFreight());
        truePriceTv.setText("¥" + detailBean.getTotal_fee());
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
