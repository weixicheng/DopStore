package com.dopstore.mall.shop.activity;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.dopstore.mall.R;
import com.dopstore.mall.activity.bean.ShopData;
import com.dopstore.mall.base.BaseActivity;
import com.dopstore.mall.login.activity.LoginActivity;
import com.dopstore.mall.shop.adapter.ActivityDetailAdapter;
import com.dopstore.mall.shop.bean.ActivityDetailBean;
import com.dopstore.mall.util.CommHttp;
import com.dopstore.mall.util.Constant;
import com.dopstore.mall.util.LoadImageUtils;
import com.dopstore.mall.util.OtherLoginUtils;
import com.dopstore.mall.util.ShareData.ShareData;
import com.dopstore.mall.util.SkipUtils;
import com.dopstore.mall.util.T;
import com.dopstore.mall.util.URL;
import com.dopstore.mall.util.UserUtils;
import com.dopstore.mall.util.Utils;
import com.dopstore.mall.view.EScrollView;
import com.dopstore.mall.view.scrollview.DetailMenu;
import com.dopstore.mall.view.scrollview.YsnowScrollViewPageOne;
import com.dopstore.mall.view.scrollview.YsnowWebView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * 作者：xicheng on 16/9/13
 */
public class ActivityDetailActivity extends BaseActivity {
    private RelativeLayout topLayout, bottomLy;
    private String isCollect = "0";
    private ImageButton collectBt,shareBt;
    private DetailMenu detailMenu;
    private YsnowWebView webView;
    private YsnowScrollViewPageOne pageOne;
    private ImageView imageView;
    private TextView titleTv, priceTv, numTv, timeTv, ageTv, addressTv, shopTv, phoneTv;
    private EScrollView eScrollView;
    private LinearLayout shopLayout;
    private LinearLayout errorLayout;
    private TextView loadTv;

    private ActivityDetailBean detailBean;
    private List<ShopData> datas;
    private LoadImageUtils loadImageUtils;
    private String activity_id;
    private OtherLoginUtils otherLoginUtils;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_activity_detail);
        initView();
        initData();
    }

    private void initView() {
        loadImageUtils = LoadImageUtils.getInstance(this);
        otherLoginUtils=new OtherLoginUtils(this);
        Map<String, Object> map = SkipUtils.getMap(this);
        if (map == null) return;
        activity_id = map.get(Constant.ID).toString();
        detailMenu = (DetailMenu) findViewById(R.id.activity_detail_content_ly);
        imageView = (ImageView) findViewById(R.id.activity_detail_title_image);
        topLayout = (RelativeLayout) findViewById(R.id.brandsquare_title_layout);
        pageOne = (YsnowScrollViewPageOne) findViewById(R.id.activity_detail_title_ysnowpage);
        webView = (YsnowWebView) findViewById(R.id.activity_detail_bottom_web);
        titleTv = (TextView) findViewById(R.id.activity_detail_title_name);
        priceTv = (TextView) findViewById(R.id.activity_detail_price);
        numTv = (TextView) findViewById(R.id.activity_detail_num);
        timeTv = (TextView) findViewById(R.id.activity_detail_time);
        ageTv = (TextView) findViewById(R.id.activity_detail_age);
        addressTv = (TextView) findViewById(R.id.activity_detail_address);
        shopTv = (TextView) findViewById(R.id.activity_detail_shop_name);
        shopLayout = (LinearLayout) findViewById(R.id.activity_detail_about_shop_layout);
        phoneTv = (TextView) findViewById(R.id.activity_detail_phone);
        eScrollView = (EScrollView) findViewById(R.id.activity_detail_about_scrollview);
        bottomLy = (RelativeLayout) findViewById(R.id.activity_detail_bottom_layout);
        errorLayout = (LinearLayout) findViewById(R.id.comm_error_layout);
        loadTv = (TextView) findViewById(R.id.error_data_load_tv);
        loadTv.setOnClickListener(listener);
        bottomLy.setOnClickListener(listener);
        setTopBg(getResources().getColor(R.color.transparent));
        setCustomTitle("活动详情", getResources().getColor(R.color.white_color));
        leftImageBack(R.mipmap.back_arrow);
        collectBt= (ImageButton) findViewById(R.id.title_right_before_imageButton);
        collectBt.setBackgroundResource(R.mipmap.collect_logo);
        collectBt.setVisibility(View.VISIBLE);
        collectBt.setOnClickListener(listener);
        shareBt= (ImageButton) findViewById(R.id.title_right_imageButton);
        shareBt.setBackgroundResource(R.mipmap.share_logo);
        shareBt.setVisibility(View.VISIBLE);
        shareBt.setOnClickListener(listener);
        if (isCollect.equals("0")) {
            collectBt.setBackgroundResource(R.mipmap.collect_logo);
        } else {
            collectBt.setBackgroundResource(R.mipmap.collect_check_logo);
        }
        detailMenu.openMenu();
        detailMenu.setView(topLayout);
        pageOne.setView(topLayout);
    }

    private void initData() {
        getDetail();
        eScrollView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                ShopData data = datas.get(i);
                Map<String, Object> map = new HashMap<String, Object>();
                map.put(Constant.ID, data.getId());
                map.put(Constant.NAME, data.getName());
                map.put(Constant.PICTURE, data.getCover());
                SkipUtils.jumpForMap(ActivityDetailActivity.this, ShopDetailActivity.class, map, false);
            }
        });
    }

    private void getDetail() {
        proUtils.show();
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("activity_id", activity_id);
        if (UserUtils.haveLogin(this)) {
            map.put("user_id", UserUtils.getId(this));
        }
        httpHelper.post(this, URL.ACTIVITY_DETAILS, map, new CommHttp.HttpCallBack() {
            @Override
            public void success(String body) {
                errorLayout.setVisibility(View.GONE);
                try {
                    JSONObject jo = new JSONObject(body);
                    String code = jo.optString(Constant.ERROR_CODE);
                    if ("0".equals(code)) {
                        JSONObject middle = jo.getJSONObject("details");
                        detailBean = new ActivityDetailBean();
                        detailBean.setId(middle.optString("id"));
                        detailBean.setName(middle.optString("name"));
                        detailBean.setPicture(middle.optString("picture"));
                        detailBean.setAge(middle.optString("age"));
                        detailBean.setMerchant(middle.optString("merchant"));
                        detailBean.setPhone(middle.optString("phone"));
                        detailBean.setStart_time(middle.optLong("start_time") + "");
                        detailBean.setEnd_time(middle.optLong("end_time") + "");
                        detailBean.setLimit(middle.optString("limit"));
                        detailBean.setPrice(middle.optString("price"));
                        detailBean.setAddress(middle.optString("address"));
                        detailBean.setContent(middle.optString("content"));
                        detailBean.setCategory(middle.optString("category"));
                        isCollect = middle.optString("is_collect");
                        detailBean.setIs_collect(middle.optString("is_collect"));
                        datas = new ArrayList<ShopData>();
                        JSONArray ja = middle.optJSONArray("items");
                        if (ja.length() > 0) {
                            for (int i = 0; i < ja.length(); i++) {
                                ShopData data = new ShopData();
                                JSONObject json = ja.getJSONObject(i);
                                data.setId(json.optString("item_id"));
                                data.setName(json.optString("item_name"));
                                data.setCover(json.optString("item_pic"));
                                data.setPrice(json.optString("item_price"));
                                datas.add(data);
                            }
                        }
                        detailBean.setItems(datas);
                    } else {
                        String msg = jo.optString(Constant.ERROR_MSG);
                        T.show(ActivityDetailActivity.this, msg);
                    }
                    handler.sendEmptyMessage(UPDATA_DETAIL_CODE);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                proUtils.dismiss();
            }

            @Override
            public void failed(String msg) {
                errorLayout.setVisibility(View.VISIBLE);
                proUtils.dismiss();
            }
        });
    }

    private final static int UPDATA_DETAIL_CODE = 0;
    private final static int COLLECT_SCUESS_CODE = 1;
    private final static int COLLECT_CANCEL_CODE = 2;
    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case COLLECT_SCUESS_CODE: {
                    T.show(ActivityDetailActivity.this, "添加成功");
                    isCollect = "1";
                    collectBt.setBackgroundResource(R.mipmap.collect_check_logo);
                }
                break;
                case COLLECT_CANCEL_CODE: {
                    T.show(ActivityDetailActivity.this, "取消成功");
                    isCollect = "0";
                    collectBt.setBackgroundResource(R.mipmap.collect_logo);
                }
                break;
                case UPDATA_DETAIL_CODE: {
                    isCollect = detailBean.getIs_collect();
                    if ("1".equals(isCollect)) {
                        collectBt.setBackgroundResource(R.mipmap.collect_check_logo);
                    } else {
                        collectBt.setBackgroundResource(R.mipmap.collect_logo);
                    }
                    loadImageUtils.displayImage(detailBean.getPicture(), imageView);
                    titleTv.setText(detailBean.getName());
                    String price=detailBean.getPrice();
                    priceTv.setText("￥" + price);
                    if ("0.00".equals(price)||"0.0".equals(price)||"0".equals(price)||TextUtils.isEmpty(price)) {
                        String limit = detailBean.getLimit();
                        if (TextUtils.isEmpty(limit)) {
                            numTv.setVisibility(View.GONE);
                        } else {
                            numTv.setText("限制" + limit + "人");
                            numTv.setVisibility(View.VISIBLE);
                        }
                    }else {
                        numTv.setVisibility(View.GONE);
                    }
                    String startTime = detailBean.getStart_time();
                    String endTime = detailBean.getEnd_time();
                    timeTv.setText(Utils.formatTSecond(startTime) + "——" + Utils.formatTSecond(endTime));
                    ageTv.setText(detailBean.getAge());
                    addressTv.setText(detailBean.getAddress());
                    shopTv.setText(detailBean.getMerchant());
                    phoneTv.setText(detailBean.getPhone());
                    List<ShopData> beens = detailBean.getItems();
                    if (beens.size() > 0) {
                        shopLayout.setVisibility(View.VISIBLE);
                        eScrollView.setAdapter(new ActivityDetailAdapter(ActivityDetailActivity.this, beens));
                    } else {
                        shopLayout.setVisibility(View.GONE);
                    }
                    webView.loadDataWithBaseURL(null, detailBean.getContent(), "text/html", "utf-8", null);
                }
                break;
            }
        }
    };

    View.OnClickListener listener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.title_right_imageButton: {//分享
                    ShareData shareData=new ShareData();
                    shareData.setContent(detailBean.getName());
                    shareData.setImage(detailBean.getPicture());
                    shareData.setUrl("http://orange.dev.attackt.com/h5/activity/"+activity_id);
                    otherLoginUtils.showShare(ActivityDetailActivity.this,shareData);
                }
                break;
                case R.id.title_right_before_imageButton: {//收藏
                    if ("0".equals(isCollect)) {
                        setCollectStatus("1");
                    } else {
                        setCollectStatus("2");
                    }
                }
                break;
                case R.id.activity_detail_bottom_layout: {
                    if (UserUtils.haveLogin(ActivityDetailActivity.this)) {
                        Map<String, Object> map = new HashMap<String, Object>();
                        map.put(Constant.LIST, detailBean);
                        SkipUtils.jumpForMap(ActivityDetailActivity.this, ConfirmActivityActivity.class, map, false);
                    } else {
                        SkipUtils.directJump(ActivityDetailActivity.this, LoginActivity.class, false);
                    }
                }
                break;
                case R.id.error_data_load_tv:{
                    if (detailBean!=null){
                        detailBean=null;
                    }
                    getDetail();
                }break;
            }

        }
    };

    private void setCollectStatus(final String isCollect) {
        if (!UserUtils.haveLogin(this)) {
            SkipUtils.directJump(this, LoginActivity.class, false);
            return;
        }
        proUtils.show();
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("user_id", UserUtils.getId(this));
        map.put("item_id", activity_id);
        map.put("action_id", isCollect);
        map.put("is_activity", "1");
        httpHelper.post(this, URL.COLLECTION_EDIT, map, new CommHttp.HttpCallBack() {
            @Override
            public void success(String body) {
                try {
                    JSONObject jo = new JSONObject(body);
                    String code = jo.optString(Constant.ERROR_CODE);
                    if ("0".equals(code)) {
                        if ("1".equals(isCollect)) {
                            handler.sendEmptyMessage(COLLECT_SCUESS_CODE);
                        } else {
                            handler.sendEmptyMessage(COLLECT_CANCEL_CODE);
                        }
                    } else {
                        String msg = jo.optString(Constant.ERROR_MSG);
                        T.show(ActivityDetailActivity.this, msg);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                proUtils.dismiss();
            }

            @Override
            public void failed(String msg) {
                T.checkNet(ActivityDetailActivity.this);
                proUtils.dismiss();
            }
        });
    }


    public void onResume() {
        super.onResume();

    }

    public void onPause() {
        super.onPause();

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
