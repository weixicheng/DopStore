package com.dopstore.mall.shop.activity;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.TextView;

import com.dopstore.mall.R;
import com.dopstore.mall.activity.adapter.BottomAdapter;
import com.dopstore.mall.activity.bean.MainBottomData;
import com.dopstore.mall.base.BaseActivity;
import com.dopstore.mall.shop.adapter.ShopListAdapter;
import com.dopstore.mall.shop.bean.ShopListData;
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
public class ShopListActivity extends BaseActivity {
    private TextView firstTv, secondTv, thirdTv, fourTv;
    private View firstv, secondv, thirdv, fourv;
    private GridView gridView;
    private ShopListAdapter shopListAdapter;
    private List<ShopListData> lists = new ArrayList<ShopListData>();
    private List<MainBottomData> bottomList = new ArrayList<MainBottomData>();
    private HttpHelper httpHelper;
    private ProUtils proUtils;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shop_list);
        initView();
        initData();
    }

    private void initView() {
        httpHelper = HttpHelper.getOkHttpClientUtils(this);
        proUtils = new ProUtils(this);
        setCustomTitle("列表", getResources().getColor(R.color.white_color));
        leftImageBack(R.mipmap.back_arrow);
        firstTv = (TextView) findViewById(R.id.shop_list_first);
        secondTv = (TextView) findViewById(R.id.shop_list_second);
        thirdTv = (TextView) findViewById(R.id.shop_list_third);
        fourTv = (TextView) findViewById(R.id.shop_list_four);
        firstv = findViewById(R.id.shop_list_firstv);
        secondv = findViewById(R.id.shop_list_secondv);
        thirdv = findViewById(R.id.shop_list_thirdv);
        fourv = findViewById(R.id.shop_list_fourv);
        gridView = (GridView) findViewById(R.id.shop_list_listview);
        firstTv.setOnClickListener(listener);
        secondTv.setOnClickListener(listener);
        thirdTv.setOnClickListener(listener);
        fourTv.setOnClickListener(listener);
    }

    private void initData() {
        Map<String, Object> map = SkipUtils.getMap(this);
        if (map != null) {
            String type = map.get(Constant.CATEGORY).toString();
            bottomList.clear();
            if ("1".equals(type)) {
                String name = map.get(Constant.NAME).toString();
            } else {
                String id = map.get(Constant.ID).toString();
                getHotData(id);
            }
        }
    }

    private void getHotData(String type) {
        proUtils.show();
        Map<String, String> map = new HashMap<String, String>();
        map.put(Constant.PAGESIZE, "10");
        map.put(Constant.PAGE, "1");
        map.put(Constant.CATEGORY, type);
        httpHelper.postKeyValuePairAsync(this, URL.GOODS_LIST, map, new Callback() {
            @Override
            public void onFailure(Request request, IOException e) {
                T.checkNet(ShopListActivity.this);
                proUtils.dismiss();
            }

            @Override
            public void onResponse(Response response) throws IOException {
                String body = response.body().string();
                try {
                    JSONObject jo = new JSONObject(body);
                    String code = jo.optString(Constant.ERROR_CODE);
                    if ("0".equals(code)) {
                        JSONArray ja = jo.getJSONArray(Constant.ITEMS);
                        if (ja.length() > 0) {
                            for (int i = 0; i < ja.length(); i++) {
                                JSONObject job = ja.getJSONObject(i);
                                MainBottomData data = new MainBottomData();
                                data.setId(job.optString(Constant.ID));
                                data.setCover(job.optString(Constant.COVER));
                                data.setName(job.optString(Constant.NAME));
                                data.setNumber(job.optString(Constant.NUMBER));
                                data.setStock_number(job.optString(Constant.STOCK_NUMBER));
                                data.setPrice(job.optString(Constant.PRICE));
                                bottomList.add(data);
                            }
                        }
                    } else {
                        String msg = jo.optString(Constant.ERROR_MSG);
                        T.show(ShopListActivity.this, msg);
                    }
                    handler.sendEmptyMessage(UPDATA_BOTTOM_CODE);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                proUtils.dismiss();
            }
        }, null);
    }

    View.OnClickListener listener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.shop_list_first: {
                    firstTv.setTextColor(getResources().getColor(R.color.red_color_f93448));
                    firstv.setVisibility(View.VISIBLE);
                    secondTv.setTextColor(getResources().getColor(R.color.gray_color_33));
                    secondv.setVisibility(View.INVISIBLE);
                    thirdTv.setTextColor(getResources().getColor(R.color.gray_color_33));
                    thirdv.setVisibility(View.INVISIBLE);
                    fourTv.setTextColor(getResources().getColor(R.color.gray_color_33));
                    fourv.setVisibility(View.INVISIBLE);

                }
                break;
                case R.id.shop_list_second: {
                    firstTv.setTextColor(getResources().getColor(R.color.gray_color_33));
                    firstv.setVisibility(View.INVISIBLE);
                    secondTv.setTextColor(getResources().getColor(R.color.red_color_f93448));
                    secondv.setVisibility(View.VISIBLE);
                    thirdTv.setTextColor(getResources().getColor(R.color.gray_color_33));
                    thirdv.setVisibility(View.INVISIBLE);
                    fourTv.setTextColor(getResources().getColor(R.color.gray_color_33));
                    fourv.setVisibility(View.INVISIBLE);

                }
                break;
                case R.id.shop_list_third: {
                    firstTv.setTextColor(getResources().getColor(R.color.gray_color_33));
                    firstv.setVisibility(View.INVISIBLE);
                    secondTv.setTextColor(getResources().getColor(R.color.gray_color_33));
                    secondv.setVisibility(View.INVISIBLE);
                    thirdTv.setTextColor(getResources().getColor(R.color.red_color_f93448));
                    thirdv.setVisibility(View.VISIBLE);
                    fourTv.setTextColor(getResources().getColor(R.color.gray_color_33));
                    fourv.setVisibility(View.INVISIBLE);

                }
                break;
                case R.id.shop_list_four: {
                    firstTv.setTextColor(getResources().getColor(R.color.gray_color_33));
                    firstv.setVisibility(View.INVISIBLE);
                    secondTv.setTextColor(getResources().getColor(R.color.gray_color_33));
                    secondv.setVisibility(View.INVISIBLE);
                    thirdTv.setTextColor(getResources().getColor(R.color.gray_color_33));
                    thirdv.setVisibility(View.INVISIBLE);
                    fourTv.setTextColor(getResources().getColor(R.color.red_color_f93448));
                    fourv.setVisibility(View.VISIBLE);

                }
                break;
            }
        }
    };

    private final static int UPDATA_BOTTOM_CODE = 0;
    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case UPDATA_BOTTOM_CODE: {
                    refreshBottomAdapter();
                }
                break;

            }
        }
    };

    private void refreshBottomAdapter() {
        gridView.setAdapter(new BottomAdapter(this, bottomList));
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                SkipUtils.directJump(ShopListActivity.this, ShopDetailActivity.class, false);
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
