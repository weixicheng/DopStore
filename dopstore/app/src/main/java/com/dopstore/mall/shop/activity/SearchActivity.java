package com.dopstore.mall.shop.activity;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.TextView;

import com.dopstore.mall.R;
import com.dopstore.mall.activity.bean.MainTabData;
import com.dopstore.mall.base.BaseActivity;
import com.dopstore.mall.shop.adapter.SearchAdapter;
import com.dopstore.mall.util.Constant;
import com.dopstore.mall.util.HttpHelper;
import com.dopstore.mall.util.ProUtils;
import com.dopstore.mall.util.SkipUtils;
import com.dopstore.mall.util.T;
import com.dopstore.mall.util.URL;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by 喜成 on 16/9/9.
 * name
 */
public class SearchActivity extends BaseActivity {
    private GridView gridView;
    private List<MainTabData> list = new ArrayList<MainTabData>();
    private HttpHelper httpHelper;
    private ProUtils proUtils;
    private EditText editText;
    private TextView searTv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        initView();
        initData();
    }

    private void initView() {
        httpHelper=HttpHelper.getOkHttpClientUtils(this);
        proUtils=new ProUtils(this);
        setCustomTitle("分类", getResources().getColor(R.color.white_color));
        leftImageBack(R.mipmap.back_arrow);
        gridView = (GridView) findViewById(R.id.search_gridview);
        editText = (EditText) findViewById(R.id.search_title_et);
        searTv = (TextView) findViewById(R.id.search_title_tv);
        searTv.setOnClickListener(listener);
    }

    private void initData() {
        getTypeData();
    }

    View.OnClickListener listener=new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            switch (view.getId()){
                case R.id.search_title_tv:{
                    Map<String,Object> map=new HashMap<String, Object>();
                    map.put(Constant.NAME,"");
                    map.put(Constant.CATEGORY,"1");
                    SkipUtils.jumpForMap(SearchActivity.this, ShopListActivity.class,map,false);
                }break;
            }
        }
    };

    private void getTypeData() {
        proUtils.show();
        httpHelper.getDataAsync(this, URL.GOODS_CATEGORY, new Callback() {
            @Override
            public void onFailure(Request request, IOException e) {
                T.checkNet(SearchActivity.this);
                proUtils.dismiss();
            }

            @Override
            public void onResponse(Response response) throws IOException {
                String body = response.body().string();
                try {
                    JSONObject jo = new JSONObject(body);
                    String code = jo.optString(Constant.ERROR_CODE);
                    if ("0".equals(code)) {
                        JSONArray ja = jo.getJSONArray(Constant.CATEGORYS);
                        if (ja.length() > 0) {
                            for (int i = 0; i < ja.length(); i++) {
                                JSONObject tab = ja.getJSONObject(i);
                                MainTabData tabData = new MainTabData();
                                tabData.setId(tab.optString(Constant.ID));
                                tabData.setName(tab.optString(Constant.NAME));
                                tabData.setPicture(tab.optString(Constant.PICTURE));
                                list.add(tabData);
                            }
                        }
                    } else {
                        String msg = jo.optString(Constant.ERROR_MSG);
                        T.show(SearchActivity.this, msg);
                    }
                    handler.sendEmptyMessage(UPDATA_TAB_CODE);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                proUtils.dismiss();
            }
        }, null);
    }

    private final static int UPDATA_TAB_CODE = 0;
    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case UPDATA_TAB_CODE: {
                    refreshTabAdapter();
                }
                break;
            }
        }
    };

    private void refreshTabAdapter() {
        gridView.setAdapter(new SearchAdapter(this, list));
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Map<String,Object> map=new HashMap<String, Object>();
                map.put(Constant.ID,list.get(i).getId());
                map.put(Constant.CATEGORY,"0");
                SkipUtils.jumpForMap(SearchActivity.this, ShopListActivity.class,map,false);
            }
        });
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
