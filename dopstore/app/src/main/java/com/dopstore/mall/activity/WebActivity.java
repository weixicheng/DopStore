package com.dopstore.mall.activity;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.dopstore.mall.R;
import com.dopstore.mall.base.BaseActivity;
import com.dopstore.mall.util.SkipUtils;

import java.util.Map;

/**
 * 作者：xicheng on 16/7/30 15:38
 * 类别：公共Web页,用于只显示单一Web界面
 */
public class WebActivity extends BaseActivity {
    private WebView webView;
    protected WebSettings webSetting;
    private String titleStr = "";
    private String urlStr = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web);
        initView();
        initData();
    }

    private void initView() {
        webView = (WebView) findViewById(R.id.web_main_web);
        Map<String, Object> map = SkipUtils.getMap(this);
        if (map != null) {
            titleStr = map.get("title").toString().trim();
            urlStr = map.get("url").toString().trim();
        }
        setCustomTitle(titleStr, getResources().getColor(R.color.white_color));
        leftImageBack(R.mipmap.back_arrow);

        initWebViewSetting();
        if (!TextUtils.isEmpty(urlStr)) {
            webView.loadUrl(urlStr);
        }
    }

    private void initData() {
    }

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
