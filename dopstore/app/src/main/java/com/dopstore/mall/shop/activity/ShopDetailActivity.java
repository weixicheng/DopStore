package com.dopstore.mall.shop.activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.JavascriptInterface;
import android.webkit.JsResult;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.dopstore.mall.R;
import com.dopstore.mall.activity.MainActivity;
import com.dopstore.mall.base.BaseActivity;
import com.dopstore.mall.login.activity.LoginActivity;
import com.dopstore.mall.order.activity.CashierActivity;
import com.dopstore.mall.util.Constant;
import com.dopstore.mall.util.HttpHelper;
import com.dopstore.mall.util.LoadImageUtils;
import com.dopstore.mall.util.ProUtils;
import com.dopstore.mall.util.SkipUtils;
import com.dopstore.mall.util.T;
import com.dopstore.mall.util.URL;
import com.dopstore.mall.util.UserUtils;
import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;
import com.pingplusplus.android.Pingpp;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

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
    private HttpHelper httpHelper;
    private ProUtils proUtils;
    private String isCollect = "0";
    private String shop_id;
    private JsInterface jsInterface;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shop_detail);
        initView();
    }

    private void initView() {
        httpHelper = HttpHelper.getOkHttpClientUtils(this);
        proUtils = new ProUtils(this);
        Map<String, Object> map = SkipUtils.getMap(this);
        if (map == null) return;
        shop_id = map.get(Constant.ID).toString();
        webView = (WebView) findViewById(R.id.shop_detail_web);
        setCustomTitle("商品详情", getResources().getColor(R.color.white_color));
        leftImageBack(R.mipmap.back_arrow);
        rightFirstImageBack(R.mipmap.share_logo, listener);

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
        String url = URL.SHOP_GOOD_DETAIL_URL + shop_id;
        webView.loadUrl(url);
        webView.addJavascriptInterface(jsInterface, "androidObj");
    }

    class JsInterface {
        String user_id = "";

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
                    Map<String, Object> map = new HashMap<String, Object>();
                    map.put(Constant.ID, "2");
                    SkipUtils.jumpForMap(ShopDetailActivity.this, MainActivity.class, map, true);
                }
            });

        }
        @android.webkit.JavascriptInterface
        public void joinCart(final String goods_sku_id, final String goods_id, final String num) {
            new Handler(Looper.getMainLooper()).post(new Runnable() {

                @Override
                public void run() {
                    if (UserUtils.haveLogin(ShopDetailActivity.this)) {
                        user_id = UserUtils.getId(ShopDetailActivity.this);
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
                        user_id = UserUtils.getId(ShopDetailActivity.this);
                        Pingpp.createPayment(ShopDetailActivity.this, charge);
                    } else {
                        SkipUtils.directJump(ShopDetailActivity.this, LoginActivity.class, false);
                        return;
                    }
                }
            });
        }

    }

    /**
     * 加入购物车
     * @param goods_sku_id
     * @param goods_id
     * @param num
     */
    private void joinCartInApp(String goods_sku_id, String goods_id, String num) {
    }

    View.OnClickListener listener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.title_right_imageButton: {//分享
                    SkipUtils.directJump(ShopDetailActivity.this, MainActivity.class, true);
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
            rightSecondImageBack(R.mipmap.collect_small_logo, listener);
            return;
        }
        Map<String, String> map = new HashMap<String, String>();
        map.put("user_id", UserUtils.getId(this));
        map.put("goods_id", shop_id);
        httpHelper.postKeyValuePairAsync(this, URL.COLLECTION_GOODS_STATUS, map, new Callback() {
            @Override
            public void onFailure(Request request, IOException e) {
                T.checkNet(ShopDetailActivity.this);
                isCollect = "0";
                handler.sendEmptyMessage(GET_COLLECT_STATUS_CODE);
                proUtils.dismiss();
            }

            @Override
            public void onResponse(Response response) throws IOException {
                String body = response.body().string();
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
        }, null);
    }

    private void setCollectStatus(final String isCollect) {
        if (!UserUtils.haveLogin(this)) {
            SkipUtils.directJump(this, LoginActivity.class, false);
            return;
        }
        rightSecondImageBack(R.mipmap.collect_small_logo, null);
        proUtils.show();
        Map<String, String> map = new HashMap<String, String>();
        map.put("user_id", UserUtils.getId(this));
        map.put("item_id", shop_id);
        map.put("action_id", isCollect);
        httpHelper.postKeyValuePairAsync(this, URL.COLLECTION_EDIT, map, new Callback() {
            @Override
            public void onFailure(Request request, IOException e) {
                T.checkNet(ShopDetailActivity.this);
                proUtils.dismiss();
            }

            @Override
            public void onResponse(Response response) throws IOException {
                String body = response.body().string();
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
        }, null);
    }

    public void placeShopOrder(String count, String color, String size) {
        if (!UserUtils.haveLogin(this)) {
            SkipUtils.directJump(this, LoginActivity.class, false);
            return;
        }
        proUtils.show();
        final Map<String, String> map = new HashMap<String, String>();
        map.put("user_id", UserUtils.getId(this));
        map.put("item_id", shop_id);
        map.put("count", count);
        map.put("edit", "");
        httpHelper.postKeyValuePairAsync(this, URL.CART_EDIT, map, new Callback() {
            @Override
            public void onFailure(Request request, IOException e) {
                T.checkNet(ShopDetailActivity.this);
                proUtils.dismiss();
            }

            @Override
            public void onResponse(Response response) throws IOException {
                String body = response.body().string();
                try {
                    JSONObject jo = new JSONObject(body);
                    String code = jo.optString(Constant.ERROR_CODE);
                    if ("0".equals(code)) {
                        handler.sendEmptyMessage(PLACE_ORDER_CODE);
                    } else {
                        String msg = jo.optString(Constant.ERROR_MSG);
                        T.show(ShopDetailActivity.this, msg);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                proUtils.dismiss();
            }
        }, null);
    }


    public void onResume() {
        super.onResume();

    }

    public void onPause() {
        super.onPause();

    }

    private final static int COLLECT_SCUESS_CODE = 0;
    private final static int COLLECT_CANCEL_CODE = 1;
    private final static int PLACE_ORDER_CODE = 2;
    private final static int GET_COLLECT_STATUS_CODE = 3;

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case COLLECT_SCUESS_CODE: {
                    T.show(ShopDetailActivity.this, "添加成功");
                    isCollect = "1";
                    rightSecondImageBack(R.mipmap.collect_check_logo, listener);
                }
                break;
                case COLLECT_CANCEL_CODE: {
                    T.show(ShopDetailActivity.this, "取消成功");
                    isCollect = "0";
                    rightSecondImageBack(R.mipmap.collect_small_logo, listener);
                }
                break;
                case PLACE_ORDER_CODE: {
                    Map<String, Object> map = new HashMap<String, Object>();
                    map.put(Constant.ID, "");
                    map.put(Constant.PRICE, "");
                    SkipUtils.jumpForMap(ShopDetailActivity.this, CashierActivity.class, map, false);
                }
                break;
                case GET_COLLECT_STATUS_CODE: {
                    if ("0".equals(isCollect)) {
                        rightSecondImageBack(R.mipmap.collect_small_logo, listener);
                    } else {
                        rightSecondImageBack(R.mipmap.collect_check_logo, listener);
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
            if (resultCode == Activity.RESULT_OK) {
                String result = data.getExtras().getString("pay_result");
                /* 处理返回值
                 * "success" - payment succeed
                 * "fail"    - payment failed
                 * "cancel"  - user canceld
                 * "invalid" - payment plugin not installed
                 */
                String errorMsg = data.getExtras().getString("error_msg"); // 错误信息
                String extraMsg = data.getExtras().getString("extra_msg"); // 错误信息
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
