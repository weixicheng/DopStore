package com.dopstore.mall.activity.fragment;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.dopstore.mall.R;
import com.dopstore.mall.activity.WebActivity;
import com.dopstore.mall.activity.adapter.BottomAdapter;
import com.dopstore.mall.activity.adapter.HomeAdImageAdapter;
import com.dopstore.mall.activity.adapter.MiddleAdapter;
import com.dopstore.mall.activity.bean.CarouselData;
import com.dopstore.mall.activity.bean.MainMiddleData;
import com.dopstore.mall.activity.bean.MiddleData;
import com.dopstore.mall.activity.bean.ShopData;
import com.dopstore.mall.shop.activity.ShopDetailActivity;
import com.dopstore.mall.util.Constant;
import com.dopstore.mall.util.HttpHelper;
import com.dopstore.mall.util.ProUtils;
import com.dopstore.mall.util.SkipUtils;
import com.dopstore.mall.util.T;
import com.dopstore.mall.util.URL;
import com.dopstore.mall.util.UserUtils;
import com.dopstore.mall.view.MyGridView;
import com.dopstore.mall.view.MyListView;
import com.dopstore.mall.view.PullToRefreshView;
import com.dopstore.mall.view.PullToRefreshView.OnFooterRefreshListener;
import com.dopstore.mall.view.PullToRefreshView.OnHeaderRefreshListener;
import com.dopstore.mall.view.rollviewpager.OnItemClickListener;
import com.dopstore.mall.view.rollviewpager.RollPagerView;
import com.dopstore.mall.view.rollviewpager.hintview.IconHintView;
import com.google.gson.Gson;
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
public class FirstMainFragment extends Fragment implements OnFooterRefreshListener, OnHeaderRefreshListener {
    private PullToRefreshView pullToRefreshView;
    private RollPagerView rollPagerView;
    private MyListView myListView;
    private MyGridView myGridView;
    private LinearLayout hotLayout;
    private List<CarouselData> titleAdvertList = new ArrayList<CarouselData>();
    private List<MainMiddleData> midddleList = new ArrayList<MainMiddleData>();
    private List<ShopData> bottomList = new ArrayList<ShopData>();
    private HttpHelper httpHelper;
    private ProUtils proUtils;
    private ScrollView mainView;
    private int page = 1;
    private boolean isRefresh= false;
    private boolean isUpRefresh = false;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.layout_main_first_fragment, null);
        initView(v);
        initData();
        return v;
    }

    private void initView(View v) {
        httpHelper = HttpHelper.getOkHttpClientUtils(getActivity());
        proUtils = new ProUtils(getActivity());
        mainView = (ScrollView) v.findViewById(R.id.fragment_main_scrollview);
        pullToRefreshView = (PullToRefreshView) v.findViewById(R.id.fragment_main_pulltorefreshview);
        rollPagerView = (RollPagerView) v.findViewById(R.id.roll_view_pager);
        myListView = (MyListView) v.findViewById(R.id.main_middle_listview);
        myGridView = (MyGridView) v.findViewById(R.id.main_bottom_gridView);
        hotLayout = (LinearLayout) v.findViewById(R.id.first_main_fragment_hot_list_layout);
        pullToRefreshView.setOnHeaderRefreshListener(this);
        pullToRefreshView.setOnFooterRefreshListener(this);
    }

    private void initData() {
        titleAdvertList.clear();
        midddleList.clear();
        bottomList.clear();
        getTopData();
        getMiddleData();
        getHotData("");
    }

    /**
     * 获取数据
     */
    private void getTopData() {
        proUtils.show();
        Map<String,String> map=new HashMap<String,String>();
        map.put("project_type","1");
        httpHelper.postKeyValuePairAsync(getActivity(), URL.HOME_CAROUSEL, map,new Callback() {
            @Override
            public void onFailure(Request request, IOException e) {
                T.checkNet(getActivity());
                dismissRefresh();
                proUtils.dismiss();
            }

            @Override
            public void onResponse(Response response) throws IOException {
                String body = response.body().string();
                try {
                    JSONObject jo = new JSONObject(body);
                    String code = jo.optString(Constant.ERROR_CODE);
                    if ("0".equals(code)) {
                        JSONArray ja = jo.getJSONArray(Constant.CAROUSEL);
                        if (ja.length() > 0) {
                            for (int i = 0; i < ja.length(); i++) {
                                JSONObject job = ja.getJSONObject(i);
                                CarouselData data = new CarouselData();
                                data.setId(job.optString(Constant.ID));
                                data.setUrl(job.optString(Constant.URL));
                                data.setTitle(job.optString(Constant.TITLE));
                                data.setPicture(job.optString(Constant.PICTURE));
                                titleAdvertList.add(data);
                            }
                        }
                    } else {
                        String msg = jo.optString(Constant.ERROR_MSG);
                        T.show(getActivity(), msg);
                    }
                    handler.sendEmptyMessage(UPDATA_TOB_CODE);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                dismissRefresh();
                proUtils.dismiss();
            }
        }, null);
    }

    private void getMiddleData() {
        proUtils.show();
        Map<String,String> map=new HashMap<String,String>();
        map.put("category_id","");
        httpHelper.postKeyValuePairAsync(getActivity(), URL.HOME_THEME,null, new Callback() {
            @Override
            public void onFailure(Request request, IOException e) {
                T.checkNet(getActivity());
                dismissRefresh();
                proUtils.dismiss();
            }

            @Override
            public void onResponse(Response response) throws IOException {
                String body = response.body().string();
                try {
                    JSONObject jo = new JSONObject(body);
                    String code = jo.optString(Constant.ERROR_CODE);
                    if ("0".equals(code)) {
                        Gson gson = new Gson();
                        MiddleData middleData = gson.fromJson(
                                body, MiddleData.class);
                        midddleList = middleData.getThemes();
                    } else {
                        String msg = new JSONObject(body).optString(Constant.ERROR_MSG);
                        T.show(getActivity(), msg);
                    }
                    handler.sendEmptyMessage(UPDATA_MIDDLE_CODE);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                dismissRefresh();
                proUtils.dismiss();
            }
        }, null);
    }

    private void getHotData(String type) {
        proUtils.show();
        Map<String, String> map = new HashMap<String, String>();
        map.put(Constant.PAGESIZE, "10");
        map.put(Constant.PAGE, page + "");
        map.put(Constant.CATEGORY, "");
        map.put("is_recommended", "1");
        httpHelper.postKeyValuePairAsync(getActivity(), URL.GOODS_LIST, map, new Callback() {
            @Override
            public void onFailure(Request request, IOException e) {
                T.checkNet(getActivity());
                dismissRefresh();
                proUtils.dismiss();
            }

            @Override
            public void onResponse(Response response) throws IOException {
                String body = response.body().string();
                analyData(body);
                handler.sendEmptyMessage(UPDATA_BOTTOM_CODE);
                dismissRefresh();
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

    private final static int UPDATA_TOB_CODE = 0;
    private final static int UPDATA_MIDDLE_CODE = 1;
    private final static int UPDATA_BOTTOM_CODE = 2;
    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case UPDATA_TOB_CODE: {
                    setAdvertisementData();
                }
                break;
                case UPDATA_MIDDLE_CODE: {
                    refreshMiddleAdapter();
                }
                break;
                case UPDATA_BOTTOM_CODE: {
                    refreshBottomAdapter();
                }
                break;
            }
        }
    };

    private void refreshBottomAdapter() {
        if (bottomList.size() > 0) {
            hotLayout.setVisibility(View.VISIBLE);
        } else {
            hotLayout.setVisibility(View.GONE);
        }
        myGridView.setAdapter(new BottomAdapter(getActivity(), bottomList));
        mainView.smoothScrollTo(0, 0);
        myGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                ShopData data = bottomList.get(i);
                String id=data.getId();
                Map<String,Object> map=new HashMap<String, Object>();
                map.put(Constant.ID,id);
                SkipUtils.jumpForMap(getActivity(), ShopDetailActivity.class, map, false);
            }
        });
        mainView.smoothScrollTo(0, 0);
    }


    private void refreshMiddleAdapter() {
        myListView.setAdapter(new MiddleAdapter(getActivity(), midddleList));
        mainView.smoothScrollTo(0, 0);
    }

    /**
     * 设置轮播
     */
    private void setAdvertisementData() {
        DisplayMetrics dm = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay()
                .getMetrics(dm);
        // 设置图片宽高
        int screenWidth = getActivity().getWindowManager()
                .getDefaultDisplay().getWidth();
        final int picSize = screenWidth / 2;
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                screenWidth, picSize);
        rollPagerView.setLayoutParams(layoutParams);

        if (titleAdvertList != null) {
            //设置播放时间间隔
            rollPagerView.setPlayDelay(1000);
            //设置透明度
            rollPagerView.setAnimationDurtion(500);
            //设置适配器
            rollPagerView.setAdapter(new HomeAdImageAdapter(getActivity(), titleAdvertList));
            rollPagerView.setHintView(new IconHintView(getActivity(), R.mipmap.dop_press, R.mipmap.dop_normal));
            if (titleAdvertList.size() == 1) {
                rollPagerView.pause();
                rollPagerView.setHintView(null);
            }
        }

        rollPagerView.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                CarouselData data = titleAdvertList.get(position);
                String urlStr = data.getUrl();
                if (!TextUtils.isEmpty(urlStr)) {
                    Map<String,Object> map=new HashMap<String, Object>();
                    map.put("title",titleAdvertList.get(position).getTitle());
                    map.put("url",titleAdvertList.get(position).getUrl());
                    SkipUtils.jumpForMap(getActivity(), WebActivity.class,map, false);
                }
            }
        });

        mainView.scrollTo(0, 0);
    }

    @Override
    public void onFooterRefresh(PullToRefreshView view) {
        isUpRefresh=true;
        if (isUpRefresh) {
            page = page + 1;
            initData();
        }
    }

    @Override
    public void onHeaderRefresh(PullToRefreshView view) {
        isRefresh=true;
        if (isRefresh) {
            page = 1;
            initData();
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
