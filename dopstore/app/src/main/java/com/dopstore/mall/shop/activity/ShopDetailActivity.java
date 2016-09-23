package com.dopstore.mall.shop.activity;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.webkit.JavascriptInterface;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.dopstore.mall.R;
import com.dopstore.mall.activity.MainActivity;
import com.dopstore.mall.activity.bean.ShopData;
import com.dopstore.mall.base.BaseActivity;
import com.dopstore.mall.shop.adapter.ShopCarAdapter;
import com.dopstore.mall.shop.bean.CommonData;
import com.dopstore.mall.util.Constant;
import com.dopstore.mall.util.HttpHelper;
import com.dopstore.mall.util.LoadImageUtils;
import com.dopstore.mall.util.PopupUtils;
import com.dopstore.mall.util.ProUtils;
import com.dopstore.mall.util.SkipUtils;
import com.dopstore.mall.util.T;
import com.dopstore.mall.util.URL;
import com.dopstore.mall.util.UserUtils;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 作者：xicheng on 16/9/12
 */
public class ShopDetailActivity extends BaseActivity {
    private View serviceV;
    private WebView webView;
    protected WebSettings webSetting;
    private ShopData shopData;
    private HttpHelper httpHelper;
    private ProUtils proUtils;
    private LoadImageUtils loadImageUtils;
    private String isCollect="1";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shop_detail);
        initView();
        initData();
    }

    private void initView() {
        httpHelper=HttpHelper.getOkHttpClientUtils(this);
        proUtils=new ProUtils(this);
        loadImageUtils=LoadImageUtils.getInstance(this);
        Map<String,Object> map=SkipUtils.getMap(this);
        if (map==null)return;
        shopData=(ShopData) map.get(Constant.LIST);
        webView = (WebView) findViewById(R.id.shop_detail_web);
        serviceV = findViewById(R.id.shop_detail_service_bt);
        setCustomTitle("商品详情", getResources().getColor(R.color.white_color));
        leftImageBack(R.mipmap.back_arrow);
        rightFirstImageBack(R.mipmap.share_logo,listener);
        rightSecondImageBack(R.mipmap.collect_small_logo,listener);
        serviceV.setOnClickListener(listener);
        String url=URL.SHOP_GOOD_DETAIL_URL+shopData.getId();
        initWebViewSetting();
        if (!TextUtils.isEmpty(url)) {
            webView.loadUrl(url);
        }
    }

    private void initData() {
        //js调用Android中的方法
//        webView.addJavascriptInterface(this, "XXX");//XXX未html中的方法

    }

    /**
     * 客户端提供send_comment方法被js调用
     * by:chenhe at:2015/09/28
     *
     * @param uuid   uuid
     * @param fun_name 调用的方法名
     * @param json   js给客户端的json
     */
    @JavascriptInterface
    public void send_comment(final String uuid, final String fun_name, final String json) {

        new Thread(new Runnable() {
            @Override
            public void run() {
                Message msg = new Message();
                msg.what = 0;
                msg.obj = 1;
                handler.sendMessage(msg);
            }
        }).start();
    }

    /**
     * 回掉JS方法将处理信息返回给JS
     * @param isSuccess
     * @param json
     */
    public void returnToJs(final boolean isSuccess, final JSONObject json){
        String data = "javascript:app_result('" + isSuccess + "','" + json.toString() + "')";
        webView.loadUrl(data);
    }

    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what==0){
                //TODO:执行相关操作
                returnToJs(true, (JSONObject) msg.obj);
            }
        }
    };

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
            //webView.addJavascriptInterface(new JsObject(), "qifu");
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
    }

    View.OnClickListener listener=new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            switch (view.getId()){
                case R.id.title_right_imageButton:{//分享
                    SkipUtils.directJump(ShopDetailActivity.this, MainActivity.class,true);
                    }break;
                case R.id.title_right_before_imageButton:{//收藏
//                        if ("1".equals(isCollect)){
//                            getCollectStatus(isCollect);
//                        }else {
                            getCollectStatus(isCollect);
//                        }
                }break;
                case R.id.shop_detail_service_bt:{}break;
            }

        }
    };

    private void getCollectStatus(final String isCollect) {
        proUtils.show();
        Map<String, String> map = new HashMap<String, String>();
        map.put("user_id", UserUtils.getId(this));
        map.put("item_id", shopData.getId());
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
                            T.show(ShopDetailActivity.this, "添加成功");
                        }else {
                            T.show(ShopDetailActivity.this, "取消成功");
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
    @JavascriptInterface
    public void addToService() {
        proUtils.show();
        final Map<String,String> map=new HashMap<String,String>();
        map.put("user_id", UserUtils.getId(this));
        map.put("item_id", shopData.getId());
        map.put("count", "1");
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
                    if ("0".equals(code)){
                        T.show(ShopDetailActivity.this, "添加购物车成功");
                    }else {
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
