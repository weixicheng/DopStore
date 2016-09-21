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
import android.widget.GridView;

import com.dopstore.mall.R;
import com.dopstore.mall.activity.adapter.BottomAdapter;
import com.dopstore.mall.activity.bean.ShopData;
import com.dopstore.mall.shop.activity.ShopDetailActivity;
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
 * 作者：xicheng on 16/9/21 17:57
 * 类别：
 */
public class SecondMainFragment extends Fragment implements OnFooterRefreshListener, OnHeaderRefreshListener {
    private PullToRefreshView pullToRefreshView;
    private GridView myGridView;
    private List<ShopData> bottomList = new ArrayList<ShopData>();
    private HttpHelper httpHelper;
    private ProUtils proUtils;
    private int page=1;
    private String id="";

    public SecondMainFragment(String id) {
        this.id=id;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.layout_main_second_fragment, null);
        initView(v);
        initData();
        return v;
    }

    private void initView(View v) {
        httpHelper = HttpHelper.getOkHttpClientUtils(getActivity());
        proUtils = new ProUtils(getActivity());
        pullToRefreshView = (PullToRefreshView) v.findViewById(R.id.main_second_fragmen_pulltorefreshview);
        myGridView = (GridView) v.findViewById(R.id.main_second_fragment_gridView);
        pullToRefreshView.setOnFooterRefreshListener(this);
        pullToRefreshView.setOnHeaderRefreshListener(this);
    }

    private void initData() {
        bottomList.clear();
        getHotData(id);
    }

    private void getHotData(String type) {
        proUtils.show();
        Map<String, String> map = new HashMap<String, String>();
        map.put(Constant.PAGESIZE, "10");
        map.put(Constant.PAGE, page+"");
        map.put(Constant.CATEGORY, type);
        httpHelper.postKeyValuePairAsync(getActivity(), URL.GOODS_LIST, map, new Callback() {
            @Override
            public void onFailure(Request request, IOException e) {
                T.checkNet(getActivity());
                proUtils.dismiss();
            }

            @Override
            public void onResponse(Response response) throws IOException {
                String body = response.body().string();
                analyData(body);
                handler.sendEmptyMessage(UPDATA_OTHER_CODE);
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
                T.show(getActivity(), msg);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
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

    private void refreshOtherAdapter() {
        myGridView.setAdapter(new BottomAdapter(getActivity(), bottomList));
        myGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                ShopData data=bottomList.get(i);
                Map<String,Object> map=new HashMap<String, Object>();
                map.put(Constant.LIST,data);
                SkipUtils.jumpForMap(getActivity(), ShopDetailActivity.class,map,false);
            }
        });
    }


    @Override
    public void onFooterRefresh(PullToRefreshView view) {
        page=page+1;
        getHotData(id);
    }

    @Override
    public void onHeaderRefresh(PullToRefreshView view) {
        page=1;
        getHotData(id);

    }
}
