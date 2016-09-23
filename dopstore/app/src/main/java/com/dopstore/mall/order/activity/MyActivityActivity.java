package com.dopstore.mall.order.activity;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.dopstore.mall.R;
import com.dopstore.mall.base.BaseActivity;
import com.dopstore.mall.order.adapter.MyActivityAdapter;
import com.dopstore.mall.order.bean.MyActivityData;
import com.dopstore.mall.util.Constant;
import com.dopstore.mall.util.HttpHelper;
import com.dopstore.mall.util.ProUtils;
import com.dopstore.mall.util.SkipUtils;
import com.dopstore.mall.util.T;
import com.dopstore.mall.util.URL;
import com.dopstore.mall.util.UserUtils;
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
 * Created by 喜成 on 16/9/8.
 * name
 */
public class MyActivityActivity extends BaseActivity {
    private ListView lv;
    private List<MyActivityData> items = new ArrayList<MyActivityData>();
    private MyActivityAdapter adapter;
    private HttpHelper httpHelper;
    private ProUtils proUtils;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_activity);
        initView();
        initData();
    }

    private void initView() {
        setCustomTitle("我的活动", getResources().getColor(R.color.white_color));
        leftImageBack(R.mipmap.back_arrow);
        httpHelper=HttpHelper.getOkHttpClientUtils(this);
        proUtils=new ProUtils(this);
        lv = (ListView) findViewById(R.id.my_activity_lv);
    }

    private void initData() {
        getMyOrder();
    }

    private void getMyOrder() {
        proUtils.show();
        String id= UserUtils.getId(this);
        httpHelper.getDataAsync(this, URL.ORDER_ACTIVITY_LIST+id, new Callback() {
            @Override
            public void onFailure(Request request, IOException e) {
                T.checkNet(MyActivityActivity.this);
                proUtils.dismiss();
            }

            @Override
            public void onResponse(Response response) throws IOException {
                String body = response.body().string();
                try {
                    JSONObject jo = new JSONObject(body);
                    String code = jo.optString(Constant.ERROR_CODE);
                    if ("0".equals(code)) {
                        JSONArray ja=jo.optJSONArray("value");
                        if (ja.length()>0){
                            for (int i=0;i<ja.length();i++){
                                JSONObject json=ja.getJSONObject(i);
                                MyActivityData data = new MyActivityData();
                                data.setId(json.optString("order_num"));
                                data.setState(json.optInt("status")+"");
                                data.setTitle(json.optString("name"));
                                data.setIntro(json.optString("address"));
                                data.setImage(json.optString("pic"));
                                items.add(data);
                            }
                        }

                    } else {
                        String msg = jo.optString(Constant.ERROR_MSG);
                        T.show(MyActivityActivity.this, msg);
                    }
                    handler.sendEmptyMessage(UPDATA_ORDER_CODE);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                proUtils.dismiss();
            }
        }, null);
    }

    private final static int UPDATA_ORDER_CODE=0;
    Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case UPDATA_ORDER_CODE:{
                    refreshListView();
                }break;
            }
        }
    };

    private void refreshListView() {
        lv.setAdapter(new MyActivityAdapter(this, items));

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String id=items.get(i).getId();
                String stu=items.get(i).getState();
                Map<String,Object> map=new HashMap<String, Object>();
                map.put(Constant.ID,id);
                map.put("type",stu);
                SkipUtils.jumpForMap(MyActivityActivity.this,ActivityOrderDetailActivity.class,map,false);
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
