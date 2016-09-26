package com.dopstore.mall.shop.activity;

import android.annotation.SuppressLint;
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
    private View serviceV;
    private WebView webView;
    protected WebSettings webSetting;
    private HttpHelper httpHelper;
    private ProUtils proUtils;
    private String isCollect = "0";
    private String shop_id;

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
//       isCollect = map.get(Constant.IS_COLLECT).toString();
        webView = (WebView) findViewById(R.id.shop_detail_web);
        serviceV = findViewById(R.id.shop_detail_service_bt);
        setCustomTitle("商品详情", getResources().getColor(R.color.white_color));
        leftImageBack(R.mipmap.back_arrow);
        rightFirstImageBack(R.mipmap.share_logo, listener);
        rightSecondImageBack(R.mipmap.collect_small_logo, listener);
        serviceV.setOnClickListener(listener);
        initWebViewSetting();
    }

    @SuppressLint("JavascriptInterface")
    private void initWebViewSetting() {
        if (webView != null) {
            webSetting = webView.getSettings();
            // 支持js
            webSetting.setJavaScriptEnabled(true);
            webSetting.setJavaScriptCanOpenWindowsAutomatically(true);
            // 缩放
            webSetting.setSupportZoom(true);
            webSetting.setBuiltInZoomControls(false);
            // 支持保存数据
            webSetting.setSaveFormData(true);
            webSetting.setDefaultTextEncodingName("UTF-8");

            webSetting.setAppCacheEnabled(false);
            webSetting.setDomStorageEnabled(true);
            // webSetting.setAppCacheMaxSize(1024 * 1024 * 8);
            String appCachePath = getApplicationContext().getCacheDir()
                    .getAbsolutePath();
            webSetting.setAppCachePath(appCachePath);
            webSetting.setAllowFileAccess(true);
            // webSetting.setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);
            webView.clearHistory();

            webView.setWebViewClient(new WebViewClient() {
                @Override
                public void onPageFinished(WebView view, String url) {
                    super.onPageFinished(view, url);
                }
            });

            webView.setWebChromeClient(new chromeClient());
            String url = URL.SHOP_GOOD_DETAIL_URL + shop_id;
            if (!TextUtils.isEmpty(url)) {
                webView.loadUrl(url);
            }
            webView.addJavascriptInterface(new JsObject(), "");
        }
    }


    class JsObject {
        @SuppressLint("JavascriptInterface")
        public JsObject() {
        }

        public void backToMain() {
            new Handler(Looper.getMainLooper()).post(new Runnable() {

                @Override
                public void run() {
                    SkipUtils.directJump(ShopDetailActivity.this, MainActivity.class, true);
                }
            });
        }

        public void backToCart() {
            new Handler(Looper.getMainLooper()).post(new Runnable() {

                @Override
                public void run() {
                    Map<String, Object> map = new HashMap<String, Object>();
                    map.put(Constant.ID, "2");
                    SkipUtils.jumpForMap(ShopDetailActivity.this, MainActivity.class, map, true);
                }
            });
        }

        public void placeOrder(final String count, final String color, final String size) {
            new Handler(Looper.getMainLooper()).post(new Runnable() {

                @Override
                public void run() {
                    placeShopOrder(count, color, size);
                }
            });
        }
    }

    protected class chromeClient extends WebChromeClient {

        @Override
        public void onReceivedTitle(WebView view, String title) {
            super.onReceivedTitle(view, title);

        }

        @Override
        public void onProgressChanged(WebView view, int newProgress) {
            super.onProgressChanged(view, newProgress);
        }

        @Override
        public boolean onJsAlert(WebView view, String url, String message, JsResult result) {
            return super.onJsAlert(view, url, message, result);
        }
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
                    if ("0".equals(isCollect)){
                        getCollectStatus("1");
                    }else {
                        getCollectStatus("2");
                    }
                }
                break;
                case R.id.shop_detail_service_bt: {
                }
                break;
            }

        }
    };

    private void getCollectStatus(final String isCollect) {
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

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case COLLECT_SCUESS_CODE: {
                    T.show(ShopDetailActivity.this, "添加成功");
                    rightSecondImageBack(R.mipmap.collect_check_logo, listener);
                }
                break;
                case COLLECT_CANCEL_CODE: {
                    T.show(ShopDetailActivity.this, "取消成功");
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
