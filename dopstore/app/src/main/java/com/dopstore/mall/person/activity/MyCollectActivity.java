package com.dopstore.mall.person.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.SparseArray;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.dopstore.mall.R;
import com.dopstore.mall.base.BaseActivity;
import com.dopstore.mall.person.adapter.MyCollectAdapter;
import com.dopstore.mall.person.bean.MyCollectData;
import com.dopstore.mall.shop.activity.ActivityDetailActivity;
import com.dopstore.mall.shop.activity.ConfirmOrderActivity;
import com.dopstore.mall.shop.activity.ShopDetailActivity;
import com.dopstore.mall.util.Constant;
import com.dopstore.mall.util.HttpHelper;
import com.dopstore.mall.util.ProUtils;
import com.dopstore.mall.util.SkipUtils;
import com.dopstore.mall.util.T;
import com.dopstore.mall.util.URL;
import com.dopstore.mall.util.UserUtils;
import com.dopstore.mall.view.CommonDialog;
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
public class MyCollectActivity extends BaseActivity {
    private RelativeLayout firstLy, secondLy;
    private TextView firstTv, secondTv;
    private View firstV, secondV;
    private ListView mListView;// 列表
    private MyCollectAdapter mListAdapter;// adapter
    private List<MyCollectData> mListData = new ArrayList<MyCollectData>();// 数据

    private HttpHelper httpHelper;
    private ProUtils proUtils;
    private CommonDialog dialog;
    private int mPosition = 0;
    private int type = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_collect);
        initView();
        initData();
    }


    private void initView() {
        httpHelper = HttpHelper.getOkHttpClientUtils(this);
        proUtils = new ProUtils(this);
        setCustomTitle("我的收藏", getResources().getColor(R.color.white_color));
        leftImageBack(R.mipmap.back_arrow);
        mListView = (ListView) findViewById(R.id.my_collect_shop_list);
        firstLy = (RelativeLayout) findViewById(R.id.my_collect_first_ly);
        secondLy = (RelativeLayout) findViewById(R.id.my_collect_second_ly);
        firstTv = (TextView) findViewById(R.id.my_collect_first_tv);
        secondTv = (TextView) findViewById(R.id.my_collect_second_tv);
        firstV = findViewById(R.id.my_collect_first_v);
        secondV = findViewById(R.id.my_collect_second_v);
        firstLy.setOnClickListener(listener);
        secondLy.setOnClickListener(listener);
    }

    private void initData() {
        getCollectList("");
    }

    private void getCollectList(String typeStr) {
        mListData.clear();
        proUtils.show();
        Map<String, String> map = new HashMap<String, String>();
        map.put("user_id", UserUtils.getId(this));
        map.put("is_activity", typeStr);
        httpHelper.postKeyValuePairAsync(this, URL.COLLECTION_QUERY, map, new Callback() {
            @Override
            public void onFailure(Request request, IOException e) {
                T.checkNet(MyCollectActivity.this);
                proUtils.dismiss();
            }

            @Override
            public void onResponse(Response response) throws IOException {
                String body = response.body().string();
                analyData(body);
                handler.sendEmptyMessage(UPDATA_COLLECT_CODE);
                proUtils.dismiss();
            }
        }, null);
    }

    private void analyData(String body) {
        try {
            JSONObject jo = new JSONObject(body);
            String code = jo.optString(Constant.ERROR_CODE);
            if ("0".equals(code)) {
                JSONArray ja = jo.getJSONArray(Constant.ITEMS);
                if (ja.length() > 0) {
                    for (int i = 0; i < ja.length(); i++) {
                        JSONObject job = ja.getJSONObject(i);
                        MyCollectData data = new MyCollectData();
                        data.setId(Integer.parseInt(job.optString(Constant.ID)));
                        data.setImage(job.optString(Constant.COVER));
                        data.setTitle(job.optString(Constant.NAME));
                        data.setPrice(job.optString(Constant.PRICE));
                        data.setChoose(false);
                        mListData.add(data);
                    }
                }
            } else {
                String msg = jo.optString(Constant.ERROR_MSG);
                T.show(MyCollectActivity.this, msg);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    View.OnClickListener listener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.my_collect_first_ly: {
                    firstTv.setTextColor(getResources().getColor(R.color.red_color_f93448));
                    firstV.setBackgroundColor(getResources().getColor(R.color.red_color_f93448));
                    secondTv.setTextColor(getResources().getColor(R.color.gray_color_33));
                    secondV.setBackgroundColor(getResources().getColor(R.color.white_color));
                    type = 0;
                    getCollectList("");
                }
                break;
                case R.id.my_collect_second_ly: {
                    firstTv.setTextColor(getResources().getColor(R.color.gray_color_33));
                    firstV.setBackgroundColor(getResources().getColor(R.color.white_color));
                    secondTv.setTextColor(getResources().getColor(R.color.red_color_f93448));
                    secondV.setBackgroundColor(getResources().getColor(R.color.red_color_f93448));
                    type = 1;
                    getCollectList("1");
                }
                break;
                default:
                    break;
            }
        }
    };

    private void deleteToService(int id, String typeStr) {
        String ids = "[" + id + "]";
        proUtils.show();
        Map<String, String> map = new HashMap<String, String>();
        map.put("user_id", UserUtils.getId(this));
        map.put("item_list", ids);
        map.put("is_activity", typeStr);
        httpHelper.postKeyValuePairAsync(this, URL.COLLECTION_DEL, map, new Callback() {
            @Override
            public void onFailure(Request request, IOException e) {
                T.checkNet(MyCollectActivity.this);
                proUtils.dismiss();
            }

            @Override
            public void onResponse(Response response) throws IOException {
                String body = response.body().string();
                try {
                    JSONObject jo = new JSONObject(body);
                    String code = jo.optString(Constant.ERROR_CODE);
                    if ("0".equals(code)) {
                        handler.sendEmptyMessage(DELETE_SUCCESS_CODE);
                    } else {
                        String msg = jo.optString(Constant.ERROR_MSG);
                        T.show(MyCollectActivity.this, msg);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                proUtils.dismiss();
            }
        }, null);
    }

    private void getCollectStatus(int id,String type) {
        proUtils.show();
        Map<String, String> map = new HashMap<String, String>();
        map.put("user_id", UserUtils.getId(this));
        map.put("item_id", id+"");
        map.put("action_id", "2");
        map.put("is_activity", type);
        httpHelper.postKeyValuePairAsync(this, URL.COLLECTION_EDIT, map, new Callback() {
            @Override
            public void onFailure(Request request, IOException e) {
                T.checkNet(MyCollectActivity.this);
                proUtils.dismiss();
            }

            @Override
            public void onResponse(Response response) throws IOException {
                String body = response.body().string();
                try {
                    JSONObject jo = new JSONObject(body);
                    String code = jo.optString(Constant.ERROR_CODE);
                    if ("0".equals(code)) {
                        handler.sendEmptyMessage(DELETE_SUCCESS_CODE);
                    } else {
                        String msg = jo.optString(Constant.ERROR_MSG);
                        T.show(MyCollectActivity.this, msg);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                proUtils.dismiss();
            }
        }, null);
    }


    private final static int UPDATA_COLLECT_CODE = 0;
    private final static int DELETE_SUCCESS_CODE = 1;
    private final static int DELETE_DATA_CODE = 2;
    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case UPDATA_COLLECT_CODE: {
                    refreshListView();
                }
                break;
                case DELETE_SUCCESS_CODE: {
                    T.show(MyCollectActivity.this, "删除成功");
                    mListData.clear();
                    if (type == 0) {
                        getCollectList("");
                    } else {
                        getCollectList("1");
                    }
                }
                break;
                case DELETE_DATA_CODE: {
                    if (dialog.isShowing()) {
                        dialog.dismiss();
                    }
                    int id = mListData.get(mPosition).getId();
                    if (type == 0) {
                        getCollectStatus(id, "");
                    } else {
                        getCollectStatus(id, "1");
                    }
                }
                break;
            }
        }
    };

    private void refreshListView() {
        mListView.setAdapter(new MyCollectAdapter(this, mListData));

        mListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                mPosition = i;
                dialog = new CommonDialog(MyCollectActivity.this, handler, DELETE_DATA_CODE, "提示", "是否删除?", Constant.SHOWALLBUTTON);
                dialog.show();
                return true;
            }
        });
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                if (type == 0) {
                    Map<String, Object> map = new HashMap<String, Object>();
                    map.put(Constant.ID, mListData.get(i).getId());
                    map.put(Constant.IS_COLLECT, "1");
                    SkipUtils.jumpForMapResult(MyCollectActivity.this, ShopDetailActivity.class, map, UPDATA_DETAIL_CODE);
                } else {
                    Map<String, Object> intentMap = new HashMap<String, Object>();
                    intentMap.put(Constant.ID, mListData.get(i).getId());
                    SkipUtils.jumpForMapResult(MyCollectActivity.this, ActivityDetailActivity.class, intentMap, UPDATA_DETAIL_CODE);
                }
            }
        });
    }

    private final static int UPDATA_DETAIL_CODE = 3;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode==UPDATA_DETAIL_CODE){
            mListData.clear();
            if (type == 0) {
                getCollectList("");
            } else {
                getCollectList("1");
            }
        }
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
