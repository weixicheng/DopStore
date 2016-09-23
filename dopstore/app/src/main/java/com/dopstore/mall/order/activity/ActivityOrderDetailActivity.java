package com.dopstore.mall.order.activity;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.dopstore.mall.R;
import com.dopstore.mall.base.BaseActivity;
import com.dopstore.mall.order.bean.ActivityOrderDetailBean;
import com.dopstore.mall.order.bean.MyActivityData;
import com.dopstore.mall.util.Constant;
import com.dopstore.mall.util.HttpHelper;
import com.dopstore.mall.util.LoadImageUtils;
import com.dopstore.mall.util.ProUtils;
import com.dopstore.mall.util.SkipUtils;
import com.dopstore.mall.util.T;
import com.dopstore.mall.util.URL;
import com.dopstore.mall.util.UserUtils;
import com.dopstore.mall.util.Utils;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.Map;

/**
 * Created by 喜成 on 16/9/12.
 * name
 */
public class ActivityOrderDetailActivity extends BaseActivity {
    private TextView idTv,stateTv,titleTv,addressTv,codeTv,timeTv,typeTv,priceTv,userMsgTv,totalPriceTv,cheapTv,passPriceTv,truePriceTv;
    private Button submitBt,kFBt;
    private LoadImageUtils loadImage;
    private ImageView shopImage,zxingImage;
    private RelativeLayout zxingLayout,submitLayout;
    private String id;
    private HttpHelper httpHelper;
    private ProUtils proUtils;
    private ActivityOrderDetailBean detailBean;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_activity_order_detail);
        initView();
        initData();
    }

    private void initView() {
        loadImage=LoadImageUtils.getInstance(this);
        httpHelper=HttpHelper.getOkHttpClientUtils(this);
        proUtils=new ProUtils(this);
        setCustomTitle("订单详情", getResources().getColor(R.color.white_color));
        leftImageBack(R.mipmap.back_arrow);
        idTv=(TextView) findViewById(R.id.activity_detail_id);
        zxingLayout=(RelativeLayout) findViewById(R.id.activity_detail_zxing_layout);
        stateTv=(TextView) findViewById(R.id.activity_detail_state);
        shopImage=(ImageView) findViewById(R.id.activity_detail_image);
        zxingImage=(ImageView) findViewById(R.id.activity_detail_zxing);
        titleTv=(TextView) findViewById(R.id.activity_detail_title);
        addressTv=(TextView) findViewById(R.id.activity_detail_address);
        codeTv=(TextView) findViewById(R.id.activity_detail_zxing_tv);
        timeTv=(TextView) findViewById(R.id.activity_detail_time);
        typeTv=(TextView) findViewById(R.id.activity_detail_type);
        priceTv=(TextView) findViewById(R.id.activity_detail_price);
        userMsgTv=(TextView) findViewById(R.id.activity_detail_user_msg);
        totalPriceTv=(TextView) findViewById(R.id.activity_detail_total_price);
        cheapTv=(TextView) findViewById(R.id.activity_detail_cheap_price);
        passPriceTv=(TextView) findViewById(R.id.activity_detail_pass_price);
        truePriceTv=(TextView) findViewById(R.id.activity_detail_true_price);
        submitBt=(Button) findViewById(R.id.activity_detail_submit);
        kFBt=(Button) findViewById(R.id.activity_detail_cheap_kefu);
        submitLayout=(RelativeLayout) findViewById(R.id.activity_detail_submit_layout);
        submitBt .setOnClickListener(listener);
        kFBt.setOnClickListener(listener);
        Map<String,Object> map=SkipUtils.getMap(this);
        if (map==null)return;
        id=map.get(Constant.ID).toString();
        String typeStr=map.get("type").toString();
        int type=Integer.parseInt(typeStr);
        switch (type){
            case 0:
            {
                submitLayout.setVisibility(View.VISIBLE);
                submitBt.setText("立即付款");}break;
            case 1:
            {
                submitLayout.setVisibility(View.VISIBLE);
                submitBt.setText("申请退款");}break;
            case 2:
            case 3:
            case 4:
            case 5:
            case 6:
            {
                submitLayout.setVisibility(View.GONE);
            }break;

        }
    }

    private void initData() {
        getOrderDetail();
    }

    private void getOrderDetail() {
        proUtils.show();
        httpHelper.getDataAsync(this, URL.ORDER_ACTIVITY+"/"+id, new Callback() {
            @Override
            public void onFailure(Request request, IOException e) {
                T.checkNet(ActivityOrderDetailActivity.this);
                proUtils.dismiss();
            }

            @Override
            public void onResponse(Response response) throws IOException {
                String body = response.body().string();
                try {
                    JSONObject jo = new JSONObject(body);
                    String code = jo.optString(Constant.ERROR_CODE);
                    if ("0".equals(code)) {
                        JSONObject value=jo.optJSONObject("value");
                        detailBean=new ActivityOrderDetailBean();
                        detailBean.setOrder_num(value.optString("order_num"));
                        detailBean.setStatus(value.optInt("status"));
                        detailBean.setPic(value.optString("pic"));
                        detailBean.setName(value.optString("name"));
                        detailBean.setAddress(value.optString("address"));
                        detailBean.setStart_time(value.optLong("start_time"));
                        detailBean.setCategory(value.optString("category"));
                        detailBean.setPrice(Long.parseLong(value.optString("price")));
                        detailBean.setTotal_fee(Long.parseLong(value.optString("total_fee")));
                        detailBean.setCode(value.optString("code"));
                        detailBean.setQ_code(value.optString("q_code"));
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
        }, null);
    }

    View.OnClickListener listener=new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            switch (view.getId()){
                case R.id.activity_detail_submit:{}break;
                case R.id.activity_detail_cheap_kefu:{}break;

            }

        }
    };

    private final static int UPDATA_ORDER_CODE=0;
    Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case UPDATA_ORDER_CODE:{
                    setData();
                }break;
            }
        }
    };

    private void setData() {
        idTv.setText("订单号:"+detailBean.getOrder_num());
        int type=detailBean.getStatus();
        String typeName="";
        switch (type){
            case 0:{
                typeName="等待付款";
            }break;
            case 1:{
                typeName="付款成功";
            }break;
            case 2:{
                typeName="订单取消";
            }break;
            case 3:{
                typeName="报名成功";
            }break;
            case 4:{
                typeName="退款申请中";
            }break;
            case 5:{
                typeName="退款中";
            }break;
            case 6:{
                typeName="退款成功";
            }break;
        }
        stateTv.setText(typeName);
        loadImage.displayImage(detailBean.getPic(),shopImage);
        String zimage=detailBean.getQ_code();
        if (TextUtils.isEmpty(zimage)){
            zxingLayout.setVisibility(View.GONE);
        }else {
            zxingLayout.setVisibility(View.VISIBLE);
            loadImage.displayImage(zimage,zxingImage);
            codeTv.setText("验证码:"+detailBean.getCode());
        }
        titleTv.setText(detailBean.getName());
        addressTv.setText("地址:"+detailBean.getAddress());
        String time=Utils.formatMilli(detailBean.getStart_time(),"yyyy-MM-dd");
        timeTv.setText(time);
        typeTv.setText(detailBean.getCategory());
        priceTv.setText("¥"+detailBean.getPrice());
        userMsgTv.setText("尽快发货");
        totalPriceTv.setText("¥"+detailBean.getTotal_fee());
        cheapTv.setText("¥ 0");
        passPriceTv.setText("¥ 0");
        truePriceTv.setText("¥"+detailBean.getTotal_fee());
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
