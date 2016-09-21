package com.dopstore.mall.activity.fragment;

import android.content.Intent;
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
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.dopstore.mall.R;
import com.dopstore.mall.activity.MipcaActivityCapture;
import com.dopstore.mall.activity.bean.MiddleData;
import com.dopstore.mall.shop.activity.SearchActivity;
import com.dopstore.mall.activity.WebActivity;
import com.dopstore.mall.activity.adapter.BottomAdapter;
import com.dopstore.mall.activity.adapter.HomeAdImageAdapter;
import com.dopstore.mall.activity.adapter.MiddleAdapter;
import com.dopstore.mall.activity.adapter.TabAdapter;
import com.dopstore.mall.activity.bean.CarouselData;
import com.dopstore.mall.activity.bean.MainBottomData;
import com.dopstore.mall.activity.bean.MainMiddleData;
import com.dopstore.mall.activity.bean.MainTabData;
import com.dopstore.mall.util.Constant;
import com.dopstore.mall.util.HttpHelper;
import com.dopstore.mall.util.ProUtils;
import com.dopstore.mall.util.SkipUtils;
import com.dopstore.mall.util.T;
import com.dopstore.mall.util.URL;
import com.dopstore.mall.view.EScrollView;
import com.dopstore.mall.view.MyGridView;
import com.dopstore.mall.view.MyListView;
import com.dopstore.mall.view.rollviewpager.OnItemClickListener;
import com.dopstore.mall.view.rollviewpager.RollPagerView;
import com.dopstore.mall.view.rollviewpager.hintview.IconHintView;
import com.dopstore.mall.view.view.PullToRefreshView;
import com.dopstore.mall.view.view.PullToRefreshView.OnRefreshListener;
import com.dopstore.mall.view.view.PullToRefreshView.OnLoadMoreListener;
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
 * Created by 喜成 on 16/9/5.
 * name  首页
 */
public class MainFragment extends Fragment implements OnRefreshListener, OnLoadMoreListener {
    private ImageView titleTv;
    private ImageButton leftBtn, rightBtn;
    private PullToRefreshView pullToRefreshView;
    private RollPagerView rollPagerView;
    private MyListView myListView;
    LinearLayout firstLy,otherLy;
    private MyGridView myGridView, otherGridView;
    private TextView hotTv;
    private EScrollView eScrollView;
    private List<MainTabData> tabList = new ArrayList<MainTabData>();
    private List<CarouselData> titleAdvertList = new ArrayList<CarouselData>();
    private List<MainMiddleData> midddleList = new ArrayList<MainMiddleData>();
    private List<MainBottomData> bottomList = new ArrayList<MainBottomData>();
    private TabAdapter tabAdapter;
    private HttpHelper httpHelper;
    private ProUtils proUtils;
    private ScrollView mainView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.layout_main_fragment, null);
        initView(v);
        initData();
        return v;
    }

    private void initView(View v) {
        httpHelper = HttpHelper.getOkHttpClientUtils(getActivity());
        proUtils = new ProUtils(getActivity());
        titleTv = (ImageView) v.findViewById(R.id.title_main_image);
        leftBtn = (ImageButton) v.findViewById(R.id.title_left_imageButton);
        mainView = (ScrollView) v.findViewById(R.id.fragment_main_scrollview);
        rightBtn = (ImageButton) v.findViewById(R.id.title_right_imageButton);
        pullToRefreshView = (PullToRefreshView) v.findViewById(R.id.fragment_main_pulltorefreshview);
        rollPagerView = (RollPagerView) v.findViewById(R.id.roll_view_pager);
        myListView = (MyListView) v.findViewById(R.id.main_middle_listview);
        myGridView = (MyGridView) v.findViewById(R.id.main_bottom_gridView);
        otherGridView = (MyGridView) v.findViewById(R.id.main_content_gridview);
        eScrollView = (EScrollView) v.findViewById(R.id.fragment_main_tab_escrollview);
        firstLy = (LinearLayout) v.findViewById(R.id.main_first_content_layout);
        otherLy = (LinearLayout) v.findViewById(R.id.main_other_content_layout);
        hotTv = (TextView) v.findViewById(R.id.main_bottom_title_tv);
        titleTv.setImageResource(R.mipmap.title_logo);
        leftBtn.setBackgroundResource(R.mipmap.search_logo);
        leftBtn.setVisibility(View.VISIBLE);
        leftBtn.setOnClickListener(listener);
        titleTv.setVisibility(View.VISIBLE);
        rightBtn.setVisibility(View.VISIBLE);
        rightBtn.setBackgroundResource(R.mipmap.sweep_logo);
        rightBtn.setOnClickListener(listener);
        pullToRefreshView.setOnLoadMoreListener(this);
        pullToRefreshView.setOnRefreshListener(this);
    }

    private void initData() {
        tabList.clear();
        titleAdvertList.clear();
        midddleList.clear();
        bottomList.clear();
        getTabData();
        getTopData();
        getMiddleData();
        getHotData("");
        eScrollView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                for (int i = 0; i < tabList.size(); i++) {
                    if (i == position) {
                        tabList.get(i).setIsSelect("1");
                    } else {
                        tabList.get(i).setIsSelect("0");
                    }
                }
                tabAdapter.notifyDataSetChanged();
                if (position==0){
                    firstLy.setVisibility(View.VISIBLE);
                    otherLy.setVisibility(View.GONE);
                    initData();
                }else {
                    firstLy.setVisibility(View.GONE);
                    otherLy.setVisibility(View.VISIBLE);
                    setData(tabList.get(position).getId());
                }
            }
        });
    }

    private void setData(String id) {//除推荐外
        bottomList.clear();
        getOtherData(id);
    }

    private void getTabData() {
        proUtils.show();
        httpHelper.getDataAsync(getActivity(), URL.GOODS_CATEGORY, new Callback() {
            @Override
            public void onFailure(Request request, IOException e) {
                T.checkNet(getActivity());
                proUtils.dismiss();
            }

            @Override
            public void onResponse(Response response) throws IOException {
                String body = response.body().string();
                try {
                    JSONObject jo = new JSONObject(body);
                    String code = jo.optString(Constant.ERROR_CODE);
                    if ("0".equals(code)) {
                        MainTabData data = new MainTabData();
                        data.setId("0");
                        data.setName("推荐");
                        data.setIsSelect("1");
                        tabList.add(data);
                        JSONArray ja = jo.getJSONArray(Constant.CATEGORYS);
                        if (ja.length() > 0) {
                            for (int i = 0; i < ja.length(); i++) {
                                JSONObject tab = ja.getJSONObject(i);
                                MainTabData tabData = new MainTabData();
                                tabData.setId(tab.optString(Constant.ID));
                                tabData.setName(tab.optString(Constant.NAME));
                                tabData.setPicture(tab.optString(Constant.PICTURE));
                                tabData.setIsSelect("0");
                                tabList.add(tabData);
                            }
                        }
                    } else {
                        String msg = jo.optString(Constant.ERROR_MSG);
                        T.show(getActivity(), msg);
                    }
                    handler.sendEmptyMessage(UPDATA_TAB_CODE);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                proUtils.dismiss();
            }
        }, null);
    }


    /**
     * 获取数据
     */
    private void getTopData() {
        proUtils.show();
        httpHelper.getDataAsync(getActivity(), URL.HOME_CAROUSEL + "1", new Callback() {
            @Override
            public void onFailure(Request request, IOException e) {
                T.checkNet(getActivity());
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
                proUtils.dismiss();
            }
        }, null);
    }

    private void getMiddleData() {
        proUtils.show();
        httpHelper.getDataAsync(getActivity(), URL.HOME_THEME, new Callback() {
            @Override
            public void onFailure(Request request, IOException e) {
                T.checkNet(getActivity());
                proUtils.dismiss();
            }

            @Override
            public void onResponse(Response response) throws IOException {
                String body = response.body().string();
                try {
                    JSONObject jo = new JSONObject(body);
                    String code = jo.optString(Constant.ERROR_CODE);
                    if ("0".equals(code)) {
                        Gson gson=new Gson();
                        MiddleData middleData = gson.fromJson(
                                body, MiddleData.class);
                        midddleList=middleData.getThemes();
                    } else {
                        String msg = new JSONObject(body).optString(Constant.ERROR_MSG);
                        T.show(getActivity(), msg);
                    }
                    handler.sendEmptyMessage(UPDATA_MIDDLE_CODE);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                proUtils.dismiss();
            }
        }, null);
    }

    private void getHotData(String type) {
        proUtils.show();
        Map<String, String> map = new HashMap<String, String>();
        map.put(Constant.PAGESIZE, "10");
        map.put(Constant.PAGE, "1");
        map.put(Constant.CATEGORY, type);
        httpHelper.postKeyValuePairAsync(getActivity(), URL.GOODS_LIST, map, new Callback() {
            @Override
            public void onFailure(Request request, IOException e) {
                T.checkNet(getActivity());
                hotTv.setVisibility(View.GONE);
                proUtils.dismiss();
            }

            @Override
            public void onResponse(Response response) throws IOException {
                String body = response.body().string();
                analyData(body);
                handler.sendEmptyMessage(UPDATA_BOTTOM_CODE);
                proUtils.dismiss();
            }
        }, null);
    }

    private void getOtherData(String type) {
        proUtils.show();
        Map<String, String> map = new HashMap<String, String>();
        map.put(Constant.PAGESIZE, "10");
        map.put(Constant.PAGE, "1");
        map.put(Constant.CATEGORY, type);
        httpHelper.postKeyValuePairAsync(getActivity(), URL.GOODS_LIST, map, new Callback() {
            @Override
            public void onFailure(Request request, IOException e) {
                T.checkNet(getActivity());
                hotTv.setVisibility(View.GONE);
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
                T.show(getActivity(), msg);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    View.OnClickListener listener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.title_left_imageButton: {
                    SkipUtils.directJump(getActivity(), SearchActivity.class, false);
                }
                break;
                case R.id.title_right_imageButton: { // 打开扫描界面扫描条形码或二维码
                    SkipUtils.directJumpForResult(getActivity(), MipcaActivityCapture.class, SCANER_CODE);
                }
                break;
            }
        }
    };

    private final int SCANER_CODE = 0;

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Bundle bundle = data.getExtras();
//        //显示扫描到的内容
//        mTextView.setText(bundle.getString("result"));
//        //显示
//        mImageView.setImageBitmap((Bitmap) data.getParcelableExtra("bitmap"));

    }

    private final static int UPDATA_TAB_CODE = 0;
    private final static int UPDATA_TOB_CODE = 1;
    private final static int UPDATA_MIDDLE_CODE = 2;
    private final static int UPDATA_BOTTOM_CODE = 3;
    private final static int UPDATA_OTHER_CODE = 4;
    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case UPDATA_TAB_CODE: {
                    refreshTabAdapter();
                }
                break;
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
                case UPDATA_OTHER_CODE: {
                    refreshOtherAdapter();
                }
                break;

            }
        }
    };

    private void refreshBottomAdapter() {
        if (bottomList.size()>0){
            hotTv.setVisibility(View.VISIBLE);
        }else {
            hotTv.setVisibility(View.GONE);
        }
        myGridView.setAdapter(new BottomAdapter(getActivity(), bottomList));
        mainView.scrollTo(0,0);
    }


    private void refreshMiddleAdapter() {
        myListView.setAdapter(new MiddleAdapter(getActivity(), midddleList));
        mainView.scrollTo(0,0);
    }
    private void refreshOtherAdapter() {
        otherGridView.setAdapter(new BottomAdapter(getActivity(), bottomList));
        mainView.scrollTo(0,0);
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
                    Map<String, Object> map = new HashMap<String, Object>();
                    map.put(Constant.TITLE, data.getTitle());
                    map.put(Constant.URL, urlStr);
                    SkipUtils.jumpForMap(getActivity(), WebActivity.class, map, false);
                }
            }
        });

        mainView.scrollTo(0,0);

    }

    private void refreshTabAdapter() {
        if (tabAdapter == null) {
            tabAdapter = new TabAdapter(getActivity(), tabList);
            eScrollView.setAdapter(tabAdapter);
        } else {
            tabAdapter.notifyDataSetChanged();
        }
        mainView.scrollTo(0,0);
    }

    @Override
    public void onLoadMore() {

        pullToRefreshView.onLoadMoreComplete();
    }

    @Override
    public void onRefresh() {
        pullToRefreshView.onRefreshComplete();
    }
}
