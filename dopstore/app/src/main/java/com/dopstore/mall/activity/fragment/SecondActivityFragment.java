package com.dopstore.mall.activity.fragment;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.dopstore.mall.R;
import com.dopstore.mall.activity.adapter.ActivityAdapter;
import com.dopstore.mall.activity.bean.ActivityData;
import com.dopstore.mall.shop.activity.ActivityDetailActivity;
import com.dopstore.mall.util.Constant;
import com.dopstore.mall.util.HttpHelper;
import com.dopstore.mall.util.ProUtils;
import com.dopstore.mall.util.SkipUtils;
import com.dopstore.mall.util.T;
import com.dopstore.mall.util.URL;
import com.dopstore.mall.view.PullToRefreshView;
import com.dopstore.mall.view.PullToRefreshView.OnFooterRefreshListener;
import com.dopstore.mall.view.PullToRefreshView.OnHeaderRefreshListener;
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
 * Created by 喜成 on 16/9/5
 * name 活动
 */
public class SecondActivityFragment extends Fragment implements OnFooterRefreshListener,OnHeaderRefreshListener {
    private PullToRefreshView pullToRefreshView;
    private ListView myListView;
    private List<ActivityData> aList = new ArrayList<ActivityData>();
    private ActivityAdapter adapter;
    private HttpHelper httpHelper;
    private ProUtils proUtils;
    private int page=1;
    private String id="";
    private boolean isRefresh= false;
    private boolean isUpRefresh = false;

    public SecondActivityFragment(String id) {
        this.id=id;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.layout_activity_second_fragment, null);
        initView(v);
        initData();
        return v;
    }

    private void initView(View v) {
        httpHelper = HttpHelper.getOkHttpClientUtils(getActivity());
        proUtils = new ProUtils(getActivity());
        pullToRefreshView = (PullToRefreshView) v.findViewById(R.id.fragment_second_activity_pulltorefreshview);
        myListView = (ListView) v.findViewById(R.id.fragment_second_activity_listview);
        pullToRefreshView.setOnFooterRefreshListener(this);
        pullToRefreshView.setOnHeaderRefreshListener(this);
    }

    private void initData() {
        getOtherData(id);
    }

    private void getOtherData(String id) {
        proUtils.show();
        Map<String, String> map = new HashMap<String, String>();
        map.put(Constant.PAGESIZE, "10");
        map.put(Constant.PAGE, "1");
        map.put(Constant.CATEGORY_ID, id);
        httpHelper.postKeyValuePairAsync(getActivity(), URL.RECOMMENDED_ACT, map, new Callback() {
            @Override
            public void onFailure(Request request, IOException e) {
                T.checkNet(getActivity());
                dismissRefresh();
                proUtils.dismiss();
            }

            @Override
            public void onResponse(Response response) throws IOException {
                String body = response.body().string();
                analysisData(body);
                handler.sendEmptyMessage(UPDATA_OTHER_CODE);
                dismissRefresh();
                proUtils.dismiss();
            }
        }, null);
    }



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
                T.show(getActivity(), msg);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void refreshOtherAdapter() {
        if (adapter == null) {
            adapter = new ActivityAdapter(getActivity(), aList,0);
            myListView.setAdapter(adapter);
        } else {
            adapter.upData(aList,0);
        }
        myListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Map<String,Object> map=new HashMap<String, Object>();
                map.put(Constant.LIST,aList.get(i));
                SkipUtils.jumpForMap(getActivity(), ActivityDetailActivity.class,map, false);
            }
        });
    }


    @Override
    public void onFooterRefresh(PullToRefreshView view) {
        isUpRefresh=true;
        if (isUpRefresh) {
            page = page + 1;
            getOtherData(id);
        }
    }

    @Override
    public void onHeaderRefresh(PullToRefreshView view) {
        isRefresh=true;
        if (isRefresh) {
            page = 1;
            getOtherData(id);
        }
    }

    private void dismissRefresh(){
        if (isRefresh){
            pullToRefreshView.onHeaderRefreshComplete();
            isRefresh=false;
        }else if (isUpRefresh){
            pullToRefreshView.onFooterRefreshComplete();
            isUpRefresh=false;
        }

    }
}
