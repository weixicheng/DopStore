package com.dopstore.mall.shop.activity;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.dopstore.mall.R;
import com.dopstore.mall.activity.adapter.BottomAdapter;
import com.dopstore.mall.activity.bean.ShopData;
import com.dopstore.mall.base.BaseActivity;
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
public class ShopListActivity extends BaseActivity {
    private TextView firstTv, secondTv, thirdTv, fourTv;
    private View firstv, secondv, thirdv, fourv;
    private GridView gridView;
    private LinearLayout errorLayout;
    private TextView loadTv;
    private List<ShopData> bottomList = new ArrayList<ShopData>();
    private String typeId = "";
    private String seartchStr = "";
    private String seartchID = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shop_list);
        initView();
        initData();
    }

    private void initView() {
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
        errorLayout = (LinearLayout) findViewById(R.id.comm_error_layout);
        loadTv = (TextView) findViewById(R.id.error_data_load_tv);
        loadTv.setOnClickListener(listener);
        firstTv.setOnClickListener(listener);
        secondTv.setOnClickListener(listener);
        thirdTv.setOnClickListener(listener);
        fourTv.setOnClickListener(listener);
    }

    private void initData() {
        Map<String, Object> map = SkipUtils.getMap(this);
        if (map != null) {
            typeId = map.get(Constant.CATEGORY).toString();
            bottomList.clear();
            if ("1".equals(typeId)) {
                seartchStr = map.get(Constant.NAME).toString();
            } else {
                seartchID = map.get(Constant.ID).toString();
            }
            getHotData();
        }
    }

    /**
     * 排序不考虑
     */
    private void getHotData() {
        proUtils.show();
        Map<String, Object> map = new HashMap<String, Object>();
        map.put(Constant.PAGESIZE, "10");
        map.put(Constant.PAGE, "1");
        if (TextUtils.isEmpty(seartchStr)) {
            map.put("category_id", seartchID);
        } else {
            map.put("query_str", seartchStr);
        }
        httpHelper.post(this, URL.GOODS_LIST, map, new CommHttp.HttpCallBack() {
            @Override
            public void success(String body) {
                errorLayout.setVisibility(View.GONE);
                try {
                    JSONObject jo = new JSONObject(body);
                    String code = jo.optString(Constant.ERROR_CODE);
                    if ("0".equals(code)) {
                        JSONArray ja = jo.getJSONArray(Constant.ITEMS);
                        if (ja.length() > 0) {
                            for (int i = 0; i < ja.length(); i++) {
                                JSONObject job = ja.getJSONObject(i);
                                ShopData data = new ShopData();
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

            @Override
            public void failed(String msg) {
                errorLayout.setVisibility(View.GONE);
                proUtils.dismiss();
            }
        });
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
                case R.id.error_data_load_tv: {
                    if (bottomList != null) {
                        bottomList.clear();
                    }
                    getHotData();
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
