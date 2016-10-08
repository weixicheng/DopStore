package com.dopstore.mall.person.activity;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.dopstore.mall.R;
import com.dopstore.mall.base.BaseActivity;
import com.dopstore.mall.person.adapter.HelpAdapter;
import com.dopstore.mall.person.bean.HelpData;
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
 * Created by 喜成 on 16/9/12.
 * name
 */
public class HelpActivity extends BaseActivity {
    private ListView listView;
    private List<HelpData> lists = new ArrayList<HelpData>();
    private HttpHelper httpHelper;
    private ProUtils proUtils;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_help);
        initView();
        initData();
    }

    private void initView() {
        proUtils = new ProUtils(this);
        setCustomTitle("帮助中心", getResources().getColor(R.color.white_color));
        leftImageBack(R.mipmap.back_arrow);
        listView = (ListView) findViewById(R.id.help_list);
        httpHelper = HttpHelper.getOkHttpClientUtils(this);
    }


    private void initData() {
        getData();
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Map<String, Object> map = new HashMap<String, Object>();
                map.put(Constant.LIST, lists.get(i));
                SkipUtils.jumpForMap(HelpActivity.this, HelpDetailActivity.class, map, false);
            }
        });
    }

    private void getData() {
        proUtils.show();
        httpHelper.getDataAsync(this, URL.USER_HELPS, new Callback() {
            @Override
            public void onFailure(Request request, IOException e) {
                T.checkNet(HelpActivity.this);
                proUtils.dismiss();
            }

            @Override
            public void onResponse(Response response) throws IOException {
                String body = response.body().string();
                try {
                    JSONObject jo = new JSONObject(body);
                    String code = jo.optString(Constant.ERROR_CODE);
                    if ("0".equals(code)) {
                        JSONArray helps = jo.optJSONArray(Constant.HELPS);
                        if (helps.length() > 0) {
                            for (int i = 0; i < helps.length(); i++) {
                                JSONObject help = helps.getJSONObject(i);
                                HelpData data = new HelpData();
                                data.setId(help.optString(Constant.ID));
                                data.setTitle(help.optString(Constant.TITLE));
                                data.setContent(help.optString(Constant.CONTENT));
                                lists.add(data);
                            }
                        }
                        handler.sendEmptyMessage(UPDATA_CODE);
                    } else {
                        String msg = jo.optString(Constant.ERROR_MSG);
                        T.show(HelpActivity.this, msg);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                proUtils.dismiss();
            }
        }, null);
    }

    private final static int UPDATA_CODE = 0;
    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            listView.setAdapter(new HelpAdapter(HelpActivity.this, lists));
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
