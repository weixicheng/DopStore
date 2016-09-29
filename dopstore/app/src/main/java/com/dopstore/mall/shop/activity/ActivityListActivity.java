package com.dopstore.mall.shop.activity;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.dopstore.mall.R;
import com.dopstore.mall.activity.adapter.ActivityAdapter;
import com.dopstore.mall.activity.bean.ActivityData;
import com.dopstore.mall.base.BaseActivity;
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
 * Created by 喜成 on 16/9/13
 * name
 */
public class ActivityListActivity extends BaseActivity {
    private TextView firstTv,secondTv,thirdTv,fourTv;
    private View firstv,secondv,thirdv,fourv;
    private ListView listView;
    private ActivityAdapter adapter;
    private List<ActivityData> aList = new ArrayList<ActivityData>();
    private HttpHelper httpHelper;
    private ProUtils proUtils;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_activity_list);
        initView();
        initData();
    }

    private void initView(){
        httpHelper=HttpHelper.getOkHttpClientUtils(this);
        proUtils=new ProUtils(this);
        setCustomTitle("列表", getResources().getColor(R.color.white_color));
        leftImageBack(R.mipmap.back_arrow);
        firstTv=(TextView) findViewById(R.id.shop_list_first);
        secondTv=(TextView) findViewById(R.id.shop_list_second);
        thirdTv=(TextView) findViewById(R.id.shop_list_third);
        fourTv=(TextView) findViewById(R.id.shop_list_four);
        firstv=findViewById(R.id.shop_list_firstv);
        secondv=findViewById(R.id.shop_list_secondv);
        thirdv=findViewById(R.id.shop_list_thirdv);
        fourv=findViewById(R.id.shop_list_fourv);
        listView=(ListView) findViewById(R.id.shop_list_listview);
        firstTv.setOnClickListener(listener);
        secondTv.setOnClickListener(listener);
        thirdTv.setOnClickListener(listener);
        fourTv.setOnClickListener(listener);
    }

    private void initData(){
        getOtherData("");
    }

    private void getOtherData(String id) {
        proUtils.show();
        Map<String, String> map = new HashMap<String, String>();
        map.put(Constant.PAGESIZE, "10");
        map.put(Constant.PAGE, "1");
//        map.put("order_id", id);
        httpHelper.postKeyValuePairAsync(this, URL.RECOMMENDED_ACT, map, new Callback() {
            @Override
            public void onFailure(Request request, IOException e) {
                T.checkNet(ActivityListActivity.this);
                proUtils.dismiss();
            }

            @Override
            public void onResponse(Response response) throws IOException {
                String body = response.body().string();
                analysisData(body);
                handler.sendEmptyMessage(UPDATA_OTHER_CODE);
                proUtils.dismiss();
            }
        }, null);
    }


    View.OnClickListener listener=new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            switch (view.getId()){
                case R.id.shop_list_first:{
                    firstTv.setTextColor(getResources().getColor(R.color.red_color_f93448));
                    firstv.setVisibility(View.VISIBLE);
                    secondTv.setTextColor(getResources().getColor(R.color.gray_color_33));
                    secondv.setVisibility(View.INVISIBLE);
                    thirdTv.setTextColor(getResources().getColor(R.color.gray_color_33));
                    thirdv.setVisibility(View.INVISIBLE);
                    fourTv.setTextColor(getResources().getColor(R.color.gray_color_33));
                    fourv.setVisibility(View.INVISIBLE);
                    aList.clear();
                    getOtherData("1");
                }break;
                case R.id.shop_list_second:{
                    firstTv.setTextColor(getResources().getColor(R.color.gray_color_33));
                    firstv.setVisibility(View.INVISIBLE);
                    secondTv.setTextColor(getResources().getColor(R.color.red_color_f93448));
                    secondv.setVisibility(View.VISIBLE);
                    thirdTv.setTextColor(getResources().getColor(R.color.gray_color_33));
                    thirdv.setVisibility(View.INVISIBLE);
                    fourTv.setTextColor(getResources().getColor(R.color.gray_color_33));
                    fourv.setVisibility(View.INVISIBLE);
                    aList.clear();
                    getOtherData("2");
                }break;
                case R.id.shop_list_third:{
                    firstTv.setTextColor(getResources().getColor(R.color.gray_color_33));
                    firstv.setVisibility(View.INVISIBLE);
                    secondTv.setTextColor(getResources().getColor(R.color.gray_color_33));
                    secondv.setVisibility(View.INVISIBLE);
                    thirdTv.setTextColor(getResources().getColor(R.color.red_color_f93448));
                    thirdv.setVisibility(View.VISIBLE);
                    fourTv.setTextColor(getResources().getColor(R.color.gray_color_33));
                    fourv.setVisibility(View.INVISIBLE);
                    aList.clear();
                    getOtherData("3");

                }break;
                case R.id.shop_list_four:{
                    firstTv.setTextColor(getResources().getColor(R.color.gray_color_33));
                    firstv.setVisibility(View.INVISIBLE);
                    secondTv.setTextColor(getResources().getColor(R.color.gray_color_33));
                    secondv.setVisibility(View.INVISIBLE);
                    thirdTv.setTextColor(getResources().getColor(R.color.gray_color_33));
                    thirdv.setVisibility(View.INVISIBLE);
                    fourTv.setTextColor(getResources().getColor(R.color.red_color_f93448));
                    fourv.setVisibility(View.VISIBLE);
                    aList.clear();
                    getOtherData("4");

                }break;
            }
        }
    };



    private final static int UPDATA_OTHER_CODE = 0;
    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case UPDATA_OTHER_CODE: {
                    refreshOtherAdapter();
                }
                break;

            }
        }
    };

    private void analysisData(String body){
        try {
            JSONObject jo = new JSONObject(body);
            String code = jo.optString(Constant.ERROR_CODE);
            if ("0".equals(code)) {
                JSONArray ja = jo.getJSONArray(Constant.ACTIVITYS);
                if (ja.length() > 0) {
                    for (int i = 0; i < ja.length(); i++) {
                        JSONObject middle = ja.getJSONObject(i);
                        ActivityData middleData = new ActivityData();
                        middleData.setId(middle.optString(Constant.ID));
                        middleData.setName(middle.optString(Constant.NAME));
                        middleData.setPicture(middle.optString(Constant.PICTURE));
                        middleData.setAge(middle.optString(Constant.AGE));
                        middleData.setMerchant(middle.optString(Constant.MERCHANT));
                        middleData.setCity(middle.optString(Constant.CITY));
                        middleData.setLat(middle.optString(Constant.LAT));
                        middleData.setLng(middle.optString(Constant.LNG));
                        middleData.setStart_time(middle.optString(Constant.START_TIME));
                        middleData.setEnd_time(middle.optString(Constant.END_TIME));
                        middleData.setLimit(middle.optString(Constant.LIMIT));
                        middleData.setPrice(middle.optString(Constant.PRICE));
                        middleData.setAddress(middle.optString(Constant.ADDRESS));
                        middleData.setContent(middle.optString(Constant.CONTENT));
                        aList.add(middleData);
                    }
                }
            } else {
                String msg = jo.optString(Constant.ERROR_MSG);
                T.show(ActivityListActivity.this, msg);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void refreshOtherAdapter() {
        if (adapter == null) {
            adapter = new ActivityAdapter(this, aList,0);
            listView.setAdapter(adapter);
        } else {
            adapter.upData(aList,0);
        }
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Map<String,Object> map=new HashMap<String, Object>();
                map.put(Constant.ID,aList.get(i).getId());
                SkipUtils.jumpForMap(ActivityListActivity.this, ActivityDetailActivity.class,map, false);
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
