package com.dopstore.mall.person.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.dopstore.mall.R;
import com.dopstore.mall.base.BaseActivity;
import com.dopstore.mall.person.adapter.MyCollectAdapter;
import com.dopstore.mall.person.bean.MyCollectData;
import com.dopstore.mall.shop.activity.ActivityDetailActivity;
import com.dopstore.mall.shop.activity.ShopDetailActivity;
import com.dopstore.mall.util.CommHttp;
import com.dopstore.mall.util.Constant;
import com.dopstore.mall.util.SkipUtils;
import com.dopstore.mall.util.T;
import com.dopstore.mall.util.URL;
import com.dopstore.mall.util.UserUtils;
import com.dopstore.mall.util.Utils;
import com.dopstore.mall.view.CommonDialog;

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
    private RelativeLayout firstLy, secondLy,bottomLayout;
    private TextView firstTv, secondTv;
    private View firstV, secondV;
    private ListView mListView;// 列表
    private LinearLayout errorLayout;
    private TextView loadTv;
    private LinearLayout emptyLayout;
    private View emptyV;
    private TextView emptyTv,rightTv;
    private Button deleteBt;
    private CheckBox mCheckAll;// 全选 全不选
    private List<MyCollectData> mListData = new ArrayList<MyCollectData>();// 数据
    private MyCollectAdapter adapter;

    private int type = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_collect);
        initView();
        initData();
    }


    private void initView() {
        setCustomTitle("我的收藏", getResources().getColor(R.color.white_color));
        leftImageBack(R.mipmap.back_arrow);
        rightTv= (TextView) findViewById(R.id.title_right_textButton);
        rightTv.setText("编辑");
        rightTv.setTextColor(getResources().getColor(R.color.white));
        rightTv.setVisibility(View.VISIBLE);
        rightTv.setOnClickListener(listener);
        mListView = (ListView) findViewById(R.id.my_collect_shop_list);
        bottomLayout = (RelativeLayout) findViewById(R.id.my_collect_bottom_layout);
        deleteBt = (Button) findViewById(R.id.my_collect_delete_bt);
        mCheckAll = (CheckBox) findViewById(R.id.my_collect_check_box);
        firstLy = (RelativeLayout) findViewById(R.id.my_collect_first_ly);
        secondLy = (RelativeLayout) findViewById(R.id.my_collect_second_ly);
        firstTv = (TextView) findViewById(R.id.my_collect_first_tv);
        secondTv = (TextView) findViewById(R.id.my_collect_second_tv);
        firstV = findViewById(R.id.my_collect_first_v);
        secondV = findViewById(R.id.my_collect_second_v);
        errorLayout = (LinearLayout) findViewById(R.id.comm_error_layout);
        loadTv = (TextView) findViewById(R.id.error_data_load_tv);
        emptyLayout = (LinearLayout) findViewById(R.id.comm_empty_layout);
        emptyTv = (TextView) findViewById(R.id.comm_empty_text);
        emptyV = findViewById(R.id.comm_empty_v);
        emptyV.setBackgroundResource(R.mipmap.collect_empty_logo);
        emptyTv.setText("您还没有收藏，快去逛逛吧");
        deleteBt.setOnClickListener(listener);
        mCheckAll.setOnClickListener(listener);
        loadTv.setOnClickListener(listener);
        firstLy.setOnClickListener(listener);
        secondLy.setOnClickListener(listener);
    }

    private void initData() {
        getCollectList("");
    }

    private void getCollectList(String typeStr) {
        mListData.clear();
        proUtils.show();
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("user_id", UserUtils.getId(this));
        map.put("is_activity", typeStr);
        httpHelper.post(this, URL.COLLECTION_QUERY, map, new CommHttp.HttpCallBack() {
            @Override
            public void success(String body) {
                errorLayout.setVisibility(View.GONE);
                analyData(body);
                handler.sendEmptyMessage(UPDATA_COLLECT_CODE);
                proUtils.dismiss();
            }

            @Override
            public void failed(String msg) {
                errorLayout.setVisibility(View.VISIBLE);
                proUtils.dismiss();
            }
        });
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
                        data.setIsShow("0");
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
                    if (mListData!=null&&mListData.size()>0){
                        mListData.clear();
                    }
                    rightTv.setText("编辑");
                    bottomLayout.setVisibility(View.GONE);
                    refreshListView();
                    getCollectList("");
                }
                break;
                case R.id.my_collect_second_ly: {
                    firstTv.setTextColor(getResources().getColor(R.color.gray_color_33));
                    firstV.setBackgroundColor(getResources().getColor(R.color.white_color));
                    secondTv.setTextColor(getResources().getColor(R.color.red_color_f93448));
                    secondV.setBackgroundColor(getResources().getColor(R.color.red_color_f93448));
                    type = 1;
                    rightTv.setText("编辑");
                    bottomLayout.setVisibility(View.GONE);
                    if (mListData!=null&&mListData.size()>0){
                        mListData.clear();
                    }
                    refreshListView();
                    getCollectList("1");
                }
                break;
                case R.id.error_data_load_tv:{
                    if (type==0){
                        getCollectList("");
                    }else {
                        getCollectList("1");
                    }
                }break;
                case R.id.title_right_textButton:{
                    if (View.VISIBLE==bottomLayout.getVisibility()){
                        if (mListData != null) {
                            int size = mListData.size();
                            if (size == 0) {
                                return;
                            }
                            for (int i = 0; i < size; i++) {
                                mListData.get(i).setIsShow("0");
                            }
                            refreshListView();
                        }
                        rightTv.setText("编辑");
                        bottomLayout.setVisibility(View.GONE);
                    }else {
                        if (mListData != null) {
                            int size = mListData.size();
                            if (size == 0) {
                                return;
                            }
                            for (int i = 0; i < size; i++) {
                                mListData.get(i).setIsShow("1");
                            }
                            refreshListView();
                        }
                        rightTv.setText("完成");
                        bottomLayout.setVisibility(View.VISIBLE);
                    }
                }break;
                case R.id.my_collect_check_box:{
                    if (mCheckAll.isChecked()) {
                        if (mListData != null) {
                            int size = mListData.size();
                            if (size == 0) {
                                return;
                            }
                            for (int i = 0; i < size; i++) {
                                mListData.get(i).setChoose(true);
                            }
                            refreshListView();
                        }
                    } else {
                        if (adapter != null) {
                            for (int i = 0; i < mListData.size(); i++) {
                                mListData.get(i).setChoose(false);
                            }
                            refreshListView();
                        }
                    }
                }break;
                case R.id.my_collect_delete_bt:{
                    deleteCollect();
                }break;
                default:
                    break;
            }
        }
    };

    private void deleteCollect() {
        JSONArray jsonArray=new JSONArray();
        if (mListData.size()>0&&mListData!=null){
            for (MyCollectData myCollectData:mListData){
                if (myCollectData.isChoose()==true){
                    jsonArray.put(myCollectData.getId());
                }
            }}else {
            jsonArray=null;
            return;
        }
        proUtils.show();
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("user_id", UserUtils.getId(this));
        if (jsonArray!=null&&jsonArray.length()>0){
            map.put("item_list", jsonArray);
        }
        map.put("is_activity", type);
        httpHelper.post(this, URL.COLLECTION_DEL, map, new CommHttp.HttpCallBack() {
            @Override
            public void success(String body) {
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

            @Override
            public void failed(String msg) {
                T.checkNet(MyCollectActivity.this);
                proUtils.dismiss();
            }
        });
    }


    private final static int UPDATA_COLLECT_CODE = 0;
    private final static int DELETE_SUCCESS_CODE = 1;
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
                    if (mListData!=null&&mListData.size()>0){
                        mListData.clear();
                    }
                    refreshListView();
                    if (type == 0) {
                        getCollectList("");
                    } else {
                        getCollectList("1");
                    }
                }
                break;
            }
        }
    };

    private void refreshListView() {
        rightTv.setText("编辑");
        if (mListData.size()>0){
            emptyLayout.setVisibility(View.GONE);
        }else {
            emptyLayout.setVisibility(View.VISIBLE);
        }
        if (adapter==null) {
            adapter=new MyCollectAdapter(this,mListData,mCheckAll);
            mListView.setAdapter(adapter);
        }else {
            adapter.upDataList(mListData,mCheckAll);
        }

        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                if (type == 0) {
                    Map<String, Object> map = new HashMap<String, Object>();
                    map.put(Constant.ID, mListData.get(i).getId());
                    map.put(Constant.NAME, mListData.get(i).getTitle());
                    map.put(Constant.PICTURE, mListData.get(i).getImage());
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
        if (requestCode == UPDATA_DETAIL_CODE) {
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