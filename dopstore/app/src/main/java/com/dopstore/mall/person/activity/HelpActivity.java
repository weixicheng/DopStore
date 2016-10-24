package com.dopstore.mall.person.activity;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.dopstore.mall.R;
import com.dopstore.mall.base.BaseActivity;
import com.dopstore.mall.person.adapter.HelpAdapter;
import com.dopstore.mall.person.bean.HelpData;
import com.dopstore.mall.util.CommHttp;
import com.dopstore.mall.util.Constant;
import com.dopstore.mall.util.SkipUtils;
import com.dopstore.mall.util.T;
import com.dopstore.mall.util.URL;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

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
    private LinearLayout errorLayout;
    private TextView loadTv;
    private List<HelpData> lists = new ArrayList<HelpData>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_help);
        initView();
        initData();
    }

    private void initView() {
        setCustomTitle("帮助中心", getResources().getColor(R.color.white_color));
        leftImageBack(R.mipmap.back_arrow);
        listView = (ListView) findViewById(R.id.help_list);
        errorLayout = (LinearLayout) findViewById(R.id.comm_error_layout);
        loadTv = (TextView) findViewById(R.id.error_data_load_tv);
        loadTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (lists != null) {
                    lists.clear();
                }
                getData();
            }
        });
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
        httpHelper.get(this, URL.USER_HELPS, new CommHttp.HttpCallBack() {
            @Override
            public void success(String body) {
                errorLayout.setVisibility(View.GONE);
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
            }

            @Override
            public void failed(String msg) {
                errorLayout.setVisibility(View.VISIBLE);
            }
        });
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
