package com.dopstore.mall.shop.activity;

import android.app.Activity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
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
import com.dopstore.mall.base.BaseActivity;
import com.dopstore.mall.shop.adapter.ShopCarAdapter;
import com.dopstore.mall.shop.bean.CommonData;
import com.dopstore.mall.util.PopupUtils;
import com.dopstore.mall.util.SkipUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 作者：xicheng on 16/9/12
 */
public class ShopDetailActivity extends BaseActivity {
    private LinearLayout bgLayout;
    private RelativeLayout mainLayout;
    private RelativeLayout shopLayout;
    private TextView joinTv,onceTv;
    private View serviceV;
    private WebView webView;
    protected WebSettings webSetting;
    private PopupWindow popupWindow;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shop_detail);
        initView();
        initData();
    }

    private void initView() {
        webView = (WebView) findViewById(R.id.shop_detail_web);
        mainLayout = (RelativeLayout) findViewById(R.id.shop_detail_main_bt);
        shopLayout = (RelativeLayout) findViewById(R.id.shop_detail_shop_bt);
        bgLayout = (LinearLayout) findViewById(R.id.shop_detail_poup_bg);
        joinTv = (TextView) findViewById(R.id.shop_detail_join_bt);
        onceTv = (TextView) findViewById(R.id.shop_detail_once_bt);
        serviceV = findViewById(R.id.shop_detail_service_bt);
        setCustomTitle("商品详情", getResources().getColor(R.color.white_color));
        leftImageBack(R.mipmap.back_arrow);
        rightFirstImageBack(R.mipmap.share_logo,listener);
        rightSecondImageBack(R.mipmap.collect_small_logo,listener);
        mainLayout.setOnClickListener(listener);
        shopLayout.setOnClickListener(listener);
        joinTv.setOnClickListener(listener);
        onceTv.setOnClickListener(listener);
        serviceV.setOnClickListener(listener);
        String url="https://www.baidu.com/";
        initWebViewSetting();
        if (!TextUtils.isEmpty(url)) {
            webView.loadUrl(url);
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
                    }break;
                case R.id.title_right_before_imageButton:{//收藏

                }break;
                case R.id.shop_detail_main_bt:{}break;
                case R.id.shop_detail_shop_bt:{}break;
                case R.id.shop_detail_join_bt:{
                    showPop();
                }break;
                case R.id.shop_detail_once_bt:{}break;
                case R.id.poup_shop_close:{
                    if (popupWindow.isShowing()){popupWindow.dismiss();}
                }break;
                case R.id.poup_shop_reduce:{}break;
                case R.id.poup_shop_add:{}break;
                case R.id.shop_detail_service_bt:{}break;
                case R.id.poup_shop_join_bt:{
                    if (popupWindow.isShowing()){popupWindow.dismiss();}
                }break;
            }

        }
    };

    private void showPop() {
        List<CommonData> lists=new ArrayList<CommonData>();
        for (int i=0;i<4;i++){
            CommonData data=new CommonData();
            data.setName("24");
            lists.add(data);
        }
        bgLayout.setVisibility(View.VISIBLE);
        int screenWidth = getWindowManager().getDefaultDisplay().getWidth();
        View v= LayoutInflater.from(this).inflate(R.layout.poup_shop_car,null);

        ImageView imageView=(ImageView) v.findViewById(R.id.poup_shop_image);
        TextView info=(TextView) v.findViewById(R.id.poup_shop_info);
        TextView price=(TextView) v.findViewById(R.id.poup_shop_price);
        View close=v.findViewById(R.id.poup_shop_close);
        GridView colorGw=(GridView) v.findViewById(R.id.poup_shop_color_gridview);
        GridView typeGw=(GridView) v.findViewById(R.id.poup_shop_type_gridview);
        TextView reduceTv=(TextView) v.findViewById(R.id.poup_shop_reduce);
        TextView numTv=(TextView) v.findViewById(R.id.poup_shop_num);
        TextView addTv=(TextView) v.findViewById(R.id.poup_shop_add);
        RelativeLayout joinBt=(RelativeLayout) v.findViewById(R.id.poup_shop_join_bt);
        colorGw.setAdapter(new ShopCarAdapter(this,lists));
        typeGw.setAdapter(new ShopCarAdapter(this,lists));
        close.setOnClickListener(listener);
        reduceTv.setOnClickListener(listener);
        addTv.setOnClickListener(listener);
        joinBt.setOnClickListener(listener);
        int height=v.getHeight();
        popupWindow= PopupUtils.ShowBottomPopupWindow(this,popupWindow,v,screenWidth,450,findViewById(R.id.shop_detail_main_layout));

        popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                popupWindow=null;
                bgLayout.setVisibility(View.GONE);
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
