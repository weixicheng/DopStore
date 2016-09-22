package com.dopstore.mall.person.activity;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.SparseArray;
import android.view.KeyEvent;
import android.view.View;
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
import com.dopstore.mall.shop.activity.ConfirmOrderActivity;
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
public class MyCollectActivity extends BaseActivity {
    private ListView mListView;// 列表

    private MyCollectAdapter mListAdapter;// adapter

    private List<MyCollectData> mListData = new ArrayList<MyCollectData>();// 数据

    private boolean isBatchModel;// 是否可删除模式

    private RelativeLayout mBottonLayout;
    private CheckBox mCheckAll; // 全选 全不选

    private TextView mEdit; // 切换到删除模式

    private TextView mDelete; // 删除

    private HttpHelper httpHelper;
    private ProUtils proUtils;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_collect);
        initView();
        initData();
    }


    private void initView() {
        httpHelper=HttpHelper.getOkHttpClientUtils(this);
        proUtils=new ProUtils(this);
        setCustomTitle("我的收藏", getResources().getColor(R.color.white_color));
        leftImageBack(R.mipmap.back_arrow);
        mListView = (ListView) findViewById(R.id.my_collect_list);
        mEdit = (TextView) findViewById(R.id.title_right_textButton);
        mEdit.setText("编辑");
        mEdit.setVisibility(View.VISIBLE);
        mEdit.setTextColor(getResources().getColor(R.color.white_color));
        mEdit.setOnClickListener(listener);
        mBottonLayout = (RelativeLayout) findViewById(R.id.my_collect_bottom);
        mCheckAll = (CheckBox) findViewById(R.id.my_collect_check_box);
        mDelete = (Button) findViewById(R.id.my_collect_delete);
        mDelete.setOnClickListener(listener);
        mCheckAll.setOnClickListener(listener);
    }

    private void initData() {
        getCollectList();
    }

    private void getCollectList() {
        proUtils.show();
        Map<String, String> map = new HashMap<String, String>();
        map.put("user_id", UserUtils.getId(this));
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

    private void analyData(String body){
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

                case R.id.title_right_textButton:
                    isBatchModel = !isBatchModel;
                    if (isBatchModel) {
                        mEdit.setText(getResources().getString(R.string.menu_enter));
                        mBottonLayout.setVisibility(View.VISIBLE);
                    } else {
                        mEdit.setText(getResources().getString(R.string.menu_edit));
                        mBottonLayout.setVisibility(View.GONE);
                    }
                    break;

                case R.id.my_collect_check_box:
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
                        if (mListAdapter != null) {
                            for (int i = 0; i < mListData.size(); i++) {
                                mListData.get(i).setChoose(false);
                            }
                            refreshListView();
                        }
                    }
                    break;

                case R.id.my_collect_delete:
                    if (isBatchModel) {
                        deleteToService(mListData);
                        refreshListView();
                        mCheckAll.setChecked(false);
                    } else {

                    }
                    break;
                default:
                    break;
            }
        }
    };

    private void deleteToService(List<MyCollectData> mListData) {
        String idList="";
        for (int i=0;i<mListData.size();i++){
            boolean flag=mListData.get(i).isChoose();
            int id=mListData.get(i).getId();
            if (flag){
                idList=id+",";
            }
        }
        String ids="["+idList.substring(0,idList.length()-1)+"]";
        proUtils.show();
        Map<String, String> map = new HashMap<String, String>();
        map.put("user_id", UserUtils.getId(this));
        map.put("item_list", ids);
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
                        T.show(MyCollectActivity.this, "删除成功");
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
    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case UPDATA_COLLECT_CODE: {
                    refreshListView();
                }
                break;
            }
        }
    };

    private void refreshListView() {
        if (mListAdapter == null) {
            mListAdapter = new MyCollectAdapter(this, mListData, mCheckAll);
            mListView.setAdapter(mListAdapter);
        } else {
            mListAdapter.upData(mListData, mCheckAll);
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
