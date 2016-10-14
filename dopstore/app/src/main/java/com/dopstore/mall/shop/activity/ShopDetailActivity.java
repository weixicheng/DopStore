package com.dopstore.mall.shop.activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageButton;
import android.widget.TextView;

import com.dopstore.mall.R;
import com.dopstore.mall.activity.MainActivity;
import com.dopstore.mall.base.BaseActivity;
import com.dopstore.mall.login.activity.LoginActivity;
import com.dopstore.mall.order.activity.MyOrderActivity;
import com.dopstore.mall.order.activity.ShopPaySuccessActivity;
import com.dopstore.mall.util.CommHttp;
import com.dopstore.mall.util.Constant;
import com.dopstore.mall.util.OtherLoginUtils;
import com.dopstore.mall.util.ShareData.ShareData;
import com.dopstore.mall.util.SkipUtils;
import com.dopstore.mall.util.T;
import com.dopstore.mall.util.URL;
import com.dopstore.mall.util.UserUtils;
import com.pingplusplus.android.Pingpp;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;


/**
 * 作者：xicheng on 16/9/12
 */
public class ShopDetailActivity extends BaseActivity {
    private WebView webView;
    private TextView titleTv;
    private ImageButton collectBt,shareBt;
    private String isCollect = "0";
    private String shop_id;
    private JsInterface jsInterface;
    private OtherLoginUtils otherLoginUtils;
    private  String shop_url="";
    private  String shop_title="";
    private  String shop_image="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shop_detail);
        initView();
    }

    private void initView() {
        otherLoginUtils=new OtherLoginUtils(this);
        Map<String, Object> map = SkipUtils.getMap(this);
        if (map == null) return;
        shop_id = map.get(Constant.ID).toString();
        shop_title = map.get(Constant.NAME).toString();
        shop_image = map.get(Constant.PICTURE).toString();
        webView = (WebView) findViewById(R.id.shop_detail_web);
        titleTv= (TextView) findViewById(R.id.title_main_txt);
        titleTv.setText("商品详情");
        titleTv.setTextColor(getResources().getColor(R.color.white_color));
        leftImageBack(R.mipmap.back_arrow);
        collectBt= (ImageButton) findViewById(R.id.title_right_before_imageButton);
        collectBt.setBackgroundResource(R.mipmap.collect_logo);
        collectBt.setVisibility(View.VISIBLE);
        collectBt.setOnClickListener(listener);
        shareBt= (ImageButton) findViewById(R.id.title_right_imageButton);
        shareBt.setBackgroundResource(R.mipmap.share_logo);
        shareBt.setVisibility(View.VISIBLE);
        shareBt.setOnClickListener(listener);
        getCollectStatus();
        jsInterface = new JsInterface();
        initWebViewSetting();
    }

    @SuppressLint({"JavascriptInterface", "SetJavaScriptEnabled"})
    private void initWebViewSetting() {
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
        shop_url= URL.SHOP_GOOD_DETAIL_URL + shop_id;
        webView.loadUrl(shop_url);
        webView.addJavascriptInterface(jsInterface, "androidObj");
    }

    class JsInterface {

        @android.webkit.JavascriptInterface
        public void backToMain() {
            new Handler(Looper.getMainLooper()).post(new Runnable() {

                @Override
                public void run() {
                    SkipUtils.directJump(ShopDetailActivity.this, MainActivity.class, true);
                }
            });
        }

        @android.webkit.JavascriptInterface
        public void backToCart() {
            new Handler(Looper.getMainLooper()).post(new Runnable() {

                @Override
                public void run() {
                    if (!UserUtils.haveLogin(ShopDetailActivity.this)) {
                        SkipUtils.directJump(ShopDetailActivity.this, LoginActivity.class, false);
                        return;
                    }
                    Intent it = new Intent();
                    it.setAction(Constant.BACK_CART_DATA);
                    sendBroadcast(it);
                    SkipUtils.directJump(ShopDetailActivity.this, MainActivity.class, true);
                }
            });

        }

        @android.webkit.JavascriptInterface
        public void joinCart(final String goods_sku_id, final String goods_id, final String num) {
            new Handler(Looper.getMainLooper()).post(new Runnable() {

                @Override
                public void run() {
                    if (UserUtils.haveLogin(ShopDetailActivity.this)) {
                        joinCartInApp(goods_sku_id, goods_id, num);
                    } else {
                        SkipUtils.directJump(ShopDetailActivity.this, LoginActivity.class, false);
                        return;
                    }
                }
            });
        }

        @android.webkit.JavascriptInterface
        public void placeOrder(final String charge) {
            new Handler(Looper.getMainLooper()).post(new Runnable() {

                @Override
                public void run() {
                    if (UserUtils.haveLogin(ShopDetailActivity.this)) {
                        Message msg = new Message();
                        msg.what = PAY_CHARGE_CODE;
                        msg.obj = charge;
                        handler.sendMessage(msg);
                    } else {
                        SkipUtils.directJump(ShopDetailActivity.this, LoginActivity.class, false);
                        return;
                    }
                }
            });
        }

        @android.webkit.JavascriptInterface
        public void changeTitle(final String name) {
            new Handler(Looper.getMainLooper()).post(new Runnable() {

                @Override
                public void run() {
                    if ("小海囤商城".equals(name)){
                        collectBt.setVisibility(View.VISIBLE);
                        shareBt.setVisibility(View.VISIBLE);
                        titleTv.setText("商品详情");
                    }else {
                        collectBt.setVisibility(View.GONE);
                        shareBt.setVisibility(View.GONE);
                        titleTv.setText(name);
                    }

                }
            });
        }

        @android.webkit.JavascriptInterface
        public String getUserID() {
            String userID = "";
            if (UserUtils.haveLogin(ShopDetailActivity.this)) {
                userID = UserUtils.getId(ShopDetailActivity.this);
                return userID;
            } else {
                userID="";
                SkipUtils.directJump(ShopDetailActivity.this, LoginActivity.class, false);
                return userID;
            }
        }
    }

    /**
     * 加入购物车
     *
     * @param goods_sku_id
     * @param goods_id
     * @param num
     */
    private void joinCartInApp(String goods_sku_id, String goods_id, String num) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("user_id", UserUtils.getId(this));
        map.put("goods_id", goods_id);
        map.put("num", num);
        map.put("goods_sku_id", goods_sku_id);
        httpHelper.post(this, URL.CART_GOODS_ADD, map, new CommHttp.HttpCallBack() {
            @Override
            public void success(String body) {
                try {
                    JSONObject jo = new JSONObject(body);
                    String code = jo.optString(Constant.ERROR_CODE);
                    if ("0".equals(code)) {
                        Intent it=new Intent();
                        it.setAction(Constant.BACK_CART_REFRESH_DATA);
                        sendBroadcast(it);
                        T.show(ShopDetailActivity.this, "添加购物车成功");
                    } else {
                        String msg = jo.optString(Constant.ERROR_MSG);
                        T.show(ShopDetailActivity.this, msg);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                proUtils.dismiss();
            }

            @Override
            public void failed(String msg) {
                T.checkNet(ShopDetailActivity.this);
                proUtils.dismiss();
            }
        });
    }

    View.OnClickListener listener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.title_right_imageButton: {//分享
                    ShareData shareData=new ShareData();
                    shareData.setContent(shop_title);
                    shareData.setImage(shop_image);
                    shareData.setUrl("http://orange.dev.attackt.com/h5/goods/"+shop_id);
                    otherLoginUtils.showShare(ShopDetailActivity.this,shareData);
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
            }

        }
    };

    private void getCollectStatus() {
        if (!UserUtils.haveLogin(this)) {
            isCollect = "0";
            collectBt.setBackgroundResource(R.mipmap.collect_logo);
            return;
        }
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("user_id", UserUtils.getId(this));
        map.put("goods_id", shop_id);
        httpHelper.post(this, URL.COLLECTION_GOODS_STATUS, map, new CommHttp.HttpCallBack() {
            @Override
            public void success(String body) {
                try {
                    JSONObject jo = new JSONObject(body);
                    String code = jo.optString(Constant.ERROR_CODE);
                    if ("0".equals(code)) {
                        isCollect = jo.optString("is_collect");
                        handler.sendEmptyMessage(GET_COLLECT_STATUS_CODE);
                    } else {
                        String msg = jo.optString(Constant.ERROR_MSG);
                        T.show(ShopDetailActivity.this, msg);
                        isCollect = "0";
                        handler.sendEmptyMessage(GET_COLLECT_STATUS_CODE);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                proUtils.dismiss();
            }

            @Override
            public void failed(String msg) {
                T.checkNet(ShopDetailActivity.this);
                isCollect = "0";
                handler.sendEmptyMessage(GET_COLLECT_STATUS_CODE);
                proUtils.dismiss();
            }
        });
    }

    private void setCollectStatus(final String isCollect) {
        if (!UserUtils.haveLogin(this)) {
            SkipUtils.directJump(this, LoginActivity.class, false);
            return;
        }
        collectBt.setBackgroundResource(R.mipmap.collect_logo);
        proUtils.show();
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("user_id", UserUtils.getId(this));
        map.put("item_id", shop_id);
        map.put("action_id", isCollect);
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
                        T.show(ShopDetailActivity.this, msg);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                proUtils.dismiss();
            }

            @Override
            public void failed(String msg) {
                T.checkNet(ShopDetailActivity.this);
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

    private final static int COLLECT_SCUESS_CODE = 0;
    private final static int COLLECT_CANCEL_CODE = 1;
    private final static int GET_COLLECT_STATUS_CODE = 3;
    private final static int PAY_CHARGE_CODE = 4;

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case COLLECT_SCUESS_CODE: {
                    T.show(ShopDetailActivity.this, "添加成功");
                    isCollect = "1";
                    collectBt.setBackgroundResource(R.mipmap.collect_check_logo);
                }
                break;
                case COLLECT_CANCEL_CODE: {
                    T.show(ShopDetailActivity.this, "取消成功");
                    isCollect = "0";
                    collectBt.setBackgroundResource(R.mipmap.collect_logo);
                }
                break;
                case GET_COLLECT_STATUS_CODE: {
                    if ("0".equals(isCollect)) {
                        collectBt.setBackgroundResource(R.mipmap.collect_logo);
                    } else {
                        collectBt.setBackgroundResource(R.mipmap.collect_check_logo);
                    }

                }
                break;
                case PAY_CHARGE_CODE: {
                    String charge = msg.obj.toString();
                    if (!TextUtils.isEmpty(charge)) {
                        Pingpp.createPayment(ShopDetailActivity.this, charge);
                    }
                }
                break;
            }
        }
    };


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //支付页面返回处理
        if (requestCode == Pingpp.REQUEST_CODE_PAYMENT) {
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
                        SkipUtils.directJump(ShopDetailActivity.this, ShopPaySuccessActivity.class, true);
                    } else if (result.equals("fail")) {
                        T.show(ShopDetailActivity.this, "支付失败");
                    } else if (result.equals("cancel")) {
                        T.show(ShopDetailActivity.this, "取消支付");
                    } else if (result.equals("invalid")) {
                        T.show(ShopDetailActivity.this, errorMsg);
                    }
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
