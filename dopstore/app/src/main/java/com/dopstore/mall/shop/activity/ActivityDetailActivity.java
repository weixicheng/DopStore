package com.dopstore.mall.shop.activity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.ActivityCompat;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
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
import com.dopstore.mall.util.ShareData.OtherLoginUtils;
import com.dopstore.mall.util.ShareData.ShareData;
import com.dopstore.mall.util.SkipUtils;
import com.dopstore.mall.util.T;
import com.dopstore.mall.util.URL;
import com.dopstore.mall.util.UserUtils;
import com.dopstore.mall.util.Utils;
import com.dopstore.mall.view.CommonDialog;
import com.dopstore.mall.view.EScrollView;
import com.dopstore.mall.view.RollHeaderView;
import com.dopstore.mall.view.snapscrollview.McoyProductContentPage;
import com.dopstore.mall.view.snapscrollview.McoyProductDetailInfoPage;
import com.dopstore.mall.view.snapscrollview.McoySnapPageLayout;
import com.nostra13.universalimageloader.core.ImageLoader;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * 作者：xicheng on 16/9/13
 */
public class   ActivityDetailActivity extends BaseActivity {
    private RelativeLayout bottomLy;
    private String isCollect = "0";
    private ImageButton collectBt, shareBt;
    private WebView webView;
    private RollHeaderView rollHeaderView;
    private ImageView headImagView;
    private TextView titleTv, priceTv, numTv, timeTv, ageTv, addressTv, shopTv, phoneTv;
    private EScrollView eScrollView;
    private LinearLayout shopLayout;
    private LinearLayout errorLayout;
    private TextView loadTv;

    private ActivityDetailBean detailBean;
    private List<ShopData> datas;
    private String activity_id;
    private OtherLoginUtils otherLoginUtils;
    private LinearLayout phoneLayout;
    private CommonDialog dialog;
    private McoySnapPageLayout mcoySnapPageLayout = null;

    private McoyProductContentPage bottomPage = null;
    private McoyProductDetailInfoPage topPage = null;
    private ImageLoader imageLoader;
    private CommHttp httpHelper;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_activity_detail);
        initView();
        initData();
    }

    private void initView() {
        httpHelper=CommHttp.getInstance();
        otherLoginUtils = new OtherLoginUtils(this);
        imageLoader=ImageLoader.getInstance();
        Map<String, Object> map = SkipUtils.getMap(this);
        if (map == null) return;
        activity_id = map.get(Constant.ID).toString();
        mcoySnapPageLayout = (McoySnapPageLayout) findViewById(R.id.activity_detail_flipLayout);
        View topView= LayoutInflater.from(this).inflate(R.layout.activity_detail_top_layout, null);
        View bottomView= LayoutInflater.from(this).inflate(R.layout.activity_detail_bottom_layout, null);
        topPage = new McoyProductDetailInfoPage(this, topView);
        bottomPage = new McoyProductContentPage(this, bottomView);
        mcoySnapPageLayout.setSnapPages(topPage, bottomPage);
        initTopView(topView);
        initBottomView(bottomView);

        bottomLy = (RelativeLayout) findViewById(R.id.activity_detail_bottom_layout);
        errorLayout = (LinearLayout) findViewById(R.id.comm_error_layout);
        loadTv = (TextView) findViewById(R.id.error_data_load_tv);
        loadTv.setOnClickListener(listener);
        bottomLy.setOnClickListener(listener);
        TextView title = (TextView) findViewById(R.id.title_main_txt);
        title.setText("活动详情");
        title.setTextColor(getResources().getColor(R.color.white));
        leftImageBack(R.mipmap.back_arrow);
        collectBt = (ImageButton) findViewById(R.id.title_right_before_imageButton);
        collectBt.setBackgroundResource(R.mipmap.collect_logo);
        collectBt.setVisibility(View.VISIBLE);
        collectBt.setOnClickListener(listener);
        shareBt = (ImageButton) findViewById(R.id.title_right_imageButton);
        shareBt.setBackgroundResource(R.mipmap.share_logo);
        shareBt.setVisibility(View.VISIBLE);
        shareBt.setOnClickListener(listener);
        phoneLayout.setOnClickListener(listener);
        if (isCollect.equals("0")) {
            collectBt.setBackgroundResource(R.mipmap.collect_logo);
        } else {
            collectBt.setBackgroundResource(R.mipmap.collect_check_logo);
        }
    }

    private void initTopView(View topView) {
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        // 设置图片宽高
        int screenWidth =getWindowManager().getDefaultDisplay().getWidth();
        int picSize = screenWidth / 2;
        RelativeLayout.LayoutParams imagePa = new RelativeLayout.LayoutParams(
                screenWidth, picSize);
        rollHeaderView = (RollHeaderView) topView.findViewById(R.id.activity_detail_title_image);
        headImagView = (ImageView) topView.findViewById(R.id.activity_detail_title_single_image);
        headImagView.setLayoutParams(imagePa);
        titleTv = (TextView) topView.findViewById(R.id.activity_detail_title_name);
        priceTv = (TextView) topView.findViewById(R.id.activity_detail_price);
        numTv = (TextView) topView.findViewById(R.id.activity_detail_num);
        timeTv = (TextView) topView.findViewById(R.id.activity_detail_time);
        ageTv = (TextView) topView.findViewById(R.id.activity_detail_age);
        addressTv = (TextView) topView.findViewById(R.id.activity_detail_address);
        shopTv = (TextView) topView.findViewById(R.id.activity_detail_shop_name);
        shopLayout = (LinearLayout) topView.findViewById(R.id.activity_detail_about_shop_layout);
        phoneLayout = (LinearLayout) topView.findViewById(R.id.activity_detail_phone_layout);
        phoneTv = (TextView) topView.findViewById(R.id.activity_detail_phone);
        eScrollView = (EScrollView) topView.findViewById(R.id.activity_detail_about_scrollview);
    }
    private void initBottomView(View bottomView) {
        webView=(WebView) bottomView.findViewById(R.id.activity_detail_bottom_web);
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
                        JSONArray pictureJa=middle.optJSONArray("picture");
                        List<String> picList=new ArrayList<String>();
                        if (pictureJa!=null&&pictureJa.length()>0){
                            for (int i=0;i<pictureJa.length();i++){
                                picList.add(pictureJa.getString(i));
                            }
                        }
                        detailBean.setPicture(picList);
                        detailBean.setAge(middle.optString("age"));
                        detailBean.setCover(middle.optString("cover"));
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
            }

            @Override
            public void failed(String msg) {
                errorLayout.setVisibility(View.VISIBLE);
            }
        });
    }

    private final static int UPDATA_DETAIL_CODE = 0;
    private final static int COLLECT_SCUESS_CODE = 1;
    private final static int COLLECT_CANCEL_CODE = 2;
    private final static int MAKE_CALL_CODE = 3;
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
                    setImageList(detailBean.getPicture());
                    titleTv.setText(detailBean.getName());
                    String price = detailBean.getPrice();
                    priceTv.setText("￥" + price);
                    if ("0.00".equals(price) || "0.0".equals(price) || "0".equals(price) || TextUtils.isEmpty(price)) {
                        String limit = detailBean.getLimit();
                        if (TextUtils.isEmpty(limit)) {
                            numTv.setVisibility(View.GONE);
                        } else {
                            numTv.setText("限制" + limit + "人");
                            numTv.setVisibility(View.VISIBLE);
                        }
                    } else {
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

                    String url=detailBean.getContent();
                    initWebViewSetting(url);
                }
                break;
                case MAKE_CALL_CODE: {
                    if (dialog.isShowing()) {
                        dialog.dismiss();
                    }
                    callPhone();
                }
                break;
            }
        }
    };


    private void initWebViewSetting(String url) {
        WebSettings settings = webView.getSettings();
        settings.setJavaScriptEnabled(true);
        settings.setCacheMode(WebSettings.LOAD_NO_CACHE);
        settings.setSupportZoom(true);
        settings.setBuiltInZoomControls(true);

        webView.setWebChromeClient(new WebChromeClient() {

            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                super.onProgressChanged(view, newProgress);
                if (newProgress == 100) {
                    // 滚动条消失
                }
            }

        });

        webView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                webView.loadUrl(url);
                return true;
            }

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
            }

        });
        url="https://www.baidu.com/";
        webView.loadUrl(url);
    }

    private void setImageList(List<String> picture) {
        if (picture != null&&picture.size()>0) {
            if (picture.size()==1){
                rollHeaderView.setVisibility(View.GONE);
                headImagView.setVisibility(View.VISIBLE);
                imageLoader.displayImage(picture.get(0)+"?imageView2/1/w/1000/h/480", headImagView);
            }else {
                rollHeaderView.setVisibility(View.VISIBLE);
                headImagView.setVisibility(View.GONE);
                List<String> imgUrlList = new ArrayList<String>();
                for (int i=0;i<picture.size();i++) {
                    imgUrlList.add(picture.get(i));
                }
                rollHeaderView.setImgUrlData(imgUrlList);
                rollHeaderView.stopRoll();
            }
        }else {
            rollHeaderView.setVisibility(View.GONE);
            headImagView.setVisibility(View.VISIBLE);
        }
    }

    private void callPhone() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                requestPermissions(new String[]{"android.permission.CALL_PHONE"}, 111);
            }
            return;
        }
        String number = phoneTv.getText().toString();
        Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + number));
        startActivity(intent);
    }


    View.OnClickListener listener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.title_right_imageButton: {//分享
                    ShareData shareData = new ShareData();
                    shareData.setContent(detailBean.getName());
                    shareData.setImage(detailBean.getPicture().get(0).toString());
                    shareData.setUrl(URL.ACTIVITY_GOOD_DETAIL_URL + activity_id);
                    otherLoginUtils.showShare(ActivityDetailActivity.this, shareData);
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
                case R.id.error_data_load_tv: {
                    if (detailBean != null) {
                        detailBean = null;
                    }
                    getDetail();
                }
                break;
                case R.id.activity_detail_phone_layout: {
                    String phone = phoneTv.getText().toString();
                    dialog = new CommonDialog(ActivityDetailActivity.this, handler, MAKE_CALL_CODE, "", "是否给商家(" + phone + ")打电话？", Constant.SHOWALLBUTTON);
                    dialog.show();
                }
                break;
            }

        }
    };

    private void setCollectStatus(final String isCollect) {
        if (!UserUtils.haveLogin(this)) {
            SkipUtils.directJump(this, LoginActivity.class, false);
            return;
        }
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
            }

            @Override
            public void failed(String msg) {
                T.checkNet(ActivityDetailActivity.this);
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
