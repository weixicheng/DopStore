package com.dopstore.mall.activity.fragment;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.location.LocationManager;
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
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationClientOption.AMapLocationMode;
import com.amap.api.location.AMapLocationClientOption.AMapLocationProtocol;
import com.amap.api.location.AMapLocationListener;
import com.dopstore.mall.R;
import com.dopstore.mall.activity.WebActivity;
import com.dopstore.mall.activity.adapter.ActivityAdapter;
import com.dopstore.mall.activity.adapter.HomeAdImageAdapter;
import com.dopstore.mall.activity.adapter.TabAdapter;
import com.dopstore.mall.activity.bean.ActivityData;
import com.dopstore.mall.activity.bean.CarouselData;
import com.dopstore.mall.activity.bean.MainTabData;
import com.dopstore.mall.shop.activity.ActivityDetailActivity;
import com.dopstore.mall.shop.activity.ActivityListActivity;
import com.dopstore.mall.util.Constant;
import com.dopstore.mall.util.HttpHelper;
import com.dopstore.mall.util.ProUtils;
import com.dopstore.mall.util.SkipUtils;
import com.dopstore.mall.util.T;
import com.dopstore.mall.util.URL;
import com.dopstore.mall.view.EScrollView;
import com.dopstore.mall.view.MyListView;
import com.dopstore.mall.view.PullToRefreshView;
import com.dopstore.mall.view.PullToRefreshView.OnFooterRefreshListener;
import com.dopstore.mall.view.PullToRefreshView.OnHeaderRefreshListener;
import com.dopstore.mall.view.rollviewpager.OnItemClickListener;
import com.dopstore.mall.view.rollviewpager.RollPagerView;
import com.dopstore.mall.view.rollviewpager.hintview.IconHintView;
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
@SuppressLint("ValidFragment")
public class MainSportFragment extends Fragment implements OnHeaderRefreshListener, OnFooterRefreshListener {
    private PullToRefreshView pullToRefreshView;
    private ScrollView scrollView;
    private MyListView listView;
    private TextView leftTv, titleTv;
    private ImageButton imageButton;
    private EScrollView eScrollView;
    private List<MainTabData> tabList = new ArrayList<MainTabData>();
    private TabAdapter tabAdapter;
    private LinearLayout headLayout;
    private RelativeLayout firstLy, secondLy;
    private TextView firstTv, secondTv;
    private View firstV, secondV;
    private RollPagerView rollPagerView;
    private List<CarouselData> titleAdvertList = new ArrayList<CarouselData>();
    private List<ActivityData> aList = new ArrayList<ActivityData>();
    private ActivityAdapter adapter;
    private HttpHelper httpHelper;
    private ProUtils proUtils;
    private String latitude = "";
    private String longitude = "";
    private int page = 1;
    private boolean isRefresh = false;
    private boolean isUpRefresh = false;
    //声明AMapLocationClient类对象
    public AMapLocationClient mLocationClient = null;
    //声明AMapLocationClientOption对象
    public AMapLocationClientOption mLocationOption = null;
    private View v;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.layout_main_sport_fragment, null);
        initView(v);
        initData();
        return v;
    }

    private void initView(View v) {
        httpHelper = HttpHelper.getOkHttpClientUtils(getActivity());
        proUtils = new ProUtils(getActivity());
        leftTv = (TextView) v.findViewById(R.id.title_left_textView);
        imageButton = (ImageButton) v.findViewById(R.id.title_right_imageButton);
        titleTv = (TextView) v.findViewById(R.id.title_main_txt);
        titleTv.setText("亲子活动");
        leftTv.setText("北京");
        leftTv.setVisibility(View.VISIBLE);
        titleTv.setTextColor(getActivity().getResources().getColor(R.color.white_color));
        leftTv.setTextColor(getActivity().getResources().getColor(R.color.white_color));
        leftTv.setOnClickListener(listener);
        imageButton.setImageResource(R.mipmap.search_logo);
        imageButton.setVisibility(View.VISIBLE);
        imageButton.setOnClickListener(listener);
        eScrollView = (EScrollView) v.findViewById(R.id.main_sport_fragment_tab_escrollview);
        scrollView = (ScrollView) v.findViewById(R.id.main_sport_fragment_main_scrollview);
        pullToRefreshView = (PullToRefreshView) v.findViewById(R.id.main_sport_fragment_pulltorefreshview);
        listView = (MyListView) v.findViewById(R.id.main_sport_fragment_listview);
        initHeadView(v);
        pullToRefreshView.setOnHeaderRefreshListener(this);
        pullToRefreshView.setOnFooterRefreshListener(this);
    }

    private void initHeadView(View headView) {
        headLayout = (LinearLayout) headView.findViewById(R.id.main_sport_head_Layout);
        rollPagerView = (RollPagerView) headView.findViewById(R.id.roll_view_pager);
        firstLy = (RelativeLayout) headView.findViewById(R.id.main_sport_head_first_ly);
        secondLy = (RelativeLayout) headView.findViewById(R.id.main_sport_head_second_ly);
        firstTv = (TextView) headView.findViewById(R.id.main_sport_head_first_tv);
        secondTv = (TextView) headView.findViewById(R.id.main_sport_head_second_tv);
        firstV = headView.findViewById(R.id.main_sport_head_first_v);
        secondV = headView.findViewById(R.id.main_sport_head_second_v);
        firstLy.setOnClickListener(listener);
        secondLy.setOnClickListener(listener);
    }


    private void initData() {
        titleAdvertList.clear();
        aList.clear();
        tabList.clear();
        getTabData();
        getCarousel();
    }

    private void getTabData() {
        proUtils.show();
        httpHelper.getDataAsync(getActivity(), URL.ACT_CATEGORIES, new Callback() {
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
                        data.setId("");
                        data.setName("推荐");
                        data.setIsSelect("1");
                        tabList.add(data);
                        JSONArray ja = jo.getJSONArray(Constant.CATEGORIES);
                        if (ja.length() > 0) {
                            for (int i = 0; i < ja.length(); i++) {
                                JSONObject tab = ja.getJSONObject(i);
                                MainTabData tabData = new MainTabData();
                                tabData.setId(tab.optString(Constant.ID));
                                tabData.setName(tab.optString(Constant.NAME));
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

    private void getCarousel() {
        proUtils.show();
        Map<String, String> map = new HashMap<String, String>();
        map.put("project_type", "2");
        httpHelper.postKeyValuePairAsync(getActivity(), URL.HOME_CAROUSEL, map, new Callback() {
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
                    handler.sendEmptyMessage(UPDATA_HEAD_CODE);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                proUtils.dismiss();
            }
        }, null);
        getTdata("");
    }

    private void getTdata(final String id) {
        Map<String, String> map = new HashMap<String, String>();
        map.put(Constant.PAGESIZE, "10");
        map.put(Constant.PAGE, page + "");
        if (!TextUtils.isEmpty(id)) {
            map.put(Constant.CATEGORY_ID, id);
        }
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
                if (!TextUtils.isEmpty(id)) {
                    handler.sendEmptyMessage(UPDATA_OTHER_CODE);
                } else {
                    handler.sendEmptyMessage(UPDATA_DATA_CODE);
                }
                dismissRefresh();
                proUtils.dismiss();
            }
        }, null);
    }

    private void getNdata() {
        proUtils.show();
        Map<String, String> map = new HashMap<String, String>();
        map.put(Constant.PAGESIZE, "10");
        map.put(Constant.PAGE, "1");
        map.put(Constant.LAT, latitude);
        map.put(Constant.LNG, longitude);
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
                handler.sendEmptyMessage(UPDATA_NFC_CODE);
                dismissRefresh();
                proUtils.dismiss();
            }
        }, null);
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
        int picSize = screenWidth / 2;
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
            //设置指示器（顺序依次）
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
                    map.put("title", titleAdvertList.get(position).getTitle());
                    map.put("url", titleAdvertList.get(position).getUrl());
                    SkipUtils.jumpForMap(getActivity(), WebActivity.class, map, false);
                }else {
                    Map<String, Object> map = new HashMap<String, Object>();
                    map.put(Constant.ID, titleAdvertList.get(position).getId());
                    SkipUtils.jumpForMap(getActivity(), ActivityDetailActivity.class, map, false);
                }
            }
        });
    }

    View.OnClickListener listener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.title_left_textView: {
                }
                break;
                case R.id.main_sport_head_first_ly: {
                    firstTv.setTextColor(getResources().getColor(R.color.red_color_f93448));
                    firstV.setBackgroundColor(getResources().getColor(R.color.red_color_f93448));
                    secondTv.setTextColor(getResources().getColor(R.color.gray_color_33));
                    secondV.setBackgroundColor(getResources().getColor(R.color.white_color));
                    latitude = "";
                    longitude = "";
                    aList.clear();
                    getTdata("");
                }
                break;
                case R.id.main_sport_head_second_ly: {
                    firstTv.setTextColor(getResources().getColor(R.color.gray_color_33));
                    firstV.setBackgroundColor(getResources().getColor(R.color.white_color));
                    secondTv.setTextColor(getResources().getColor(R.color.red_color_f93448));
                    secondV.setBackgroundColor(getResources().getColor(R.color.red_color_f93448));
                    openGPSSettings();

                }
                break;
                case R.id.title_right_imageButton: {
                    SkipUtils.directJump(getActivity(), ActivityListActivity.class, false);
                }
                break;
            }
        }
    };


    private final static int UPDATA_TAB_CODE = 0;
    private final static int UPDATA_HEAD_CODE = 1;
    private final static int UPDATA_DATA_CODE = 2;
    private final static int UPDATA_NFC_CODE = 3;
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
                case UPDATA_HEAD_CODE: {
                    setAdvertisementData();
                }
                break;
                case UPDATA_DATA_CODE: {
                    refreshAdapter();
                }
                break;
                case UPDATA_NFC_CODE: {
                    refreshNAdapter();
                }
                break;
                case UPDATA_OTHER_CODE: {
                    refreshOtherAdapter();
                }
                break;
            }
        }
    };

    private void refreshTabAdapter() {
        if (tabList.size() > 0) {
            if (tabAdapter == null) {
                tabAdapter = new TabAdapter(getActivity(), tabList);
                eScrollView.setAdapter(tabAdapter);
            } else {
                tabAdapter.notifyDataSetChanged();
            }
        }
        eScrollView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                if (tabList.size() > 0) {
                    for (int i = 0; i < tabList.size(); i++) {
                        if (i == position) {
                            tabList.get(i).setIsSelect("1");
                        } else {
                            tabList.get(i).setIsSelect("0");
                        }
                    }
                    tabAdapter.notifyDataSetChanged();
                    if (position == 0) {
                        headLayout.setVisibility(View.VISIBLE);
                        aList.clear();
                        listView.setAdapter(new ActivityAdapter(getActivity(), aList, 0));
                        isRefresh = true;
                        if (isRefresh) {
                            aList.clear();
                            page = 1;
                            initData();
                        }
                    } else {
                        headLayout.setVisibility(View.GONE);
                        String id = tabList.get(position).getId();
                        aList.clear();
                        listView.setAdapter(new ActivityAdapter(getActivity(), aList, 0));
                        getTdata(id);
                    }
                }

            }
        });
    }

    private void analysisData(String body) {
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

    private void refreshAdapter() {
        if (aList.size() > 0) {

            listView.setAdapter(new ActivityAdapter(getActivity(), aList, 0));

            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    Map<String, Object> map = new HashMap<String, Object>();
                    map.put(Constant.ID, aList.get(i).getId());
                    SkipUtils.jumpForMap(getActivity(), ActivityDetailActivity.class, map, false);
                }
            });
            scrollView.smoothScrollTo(0, 0);
        }
    }

    private void refreshNAdapter() {
        if (aList.size() > 0) {
            listView.setAdapter(new ActivityAdapter(getActivity(), aList, 1));

            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    Map<String, Object> map = new HashMap<String, Object>();
                    map.put(Constant.ID, aList.get(i).getId());
                    SkipUtils.jumpForMap(getActivity(), ActivityDetailActivity.class, map, false);
                }
            });
        } else {
            listView.setAdapter(new ActivityAdapter(getActivity(), aList, 1));
        }
        scrollView.smoothScrollTo(0, 0);
    }

    private void refreshOtherAdapter() {

        listView.setAdapter(new ActivityAdapter(getActivity(), aList, 0));

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Map<String, Object> map = new HashMap<String, Object>();
                map.put(Constant.ID, aList.get(i).getId());
                SkipUtils.jumpForMap(getActivity(), ActivityDetailActivity.class, map, false);
            }
        });
    }


    @Override
    public void onFooterRefresh(PullToRefreshView view) {
//        isUpRefresh = true;
//        if (isUpRefresh) {
//            page = page + 1;
//            initData();
//        }
        pullToRefreshView.onFooterRefreshComplete();
    }

    @Override
    public void onHeaderRefresh(PullToRefreshView view) {
//        isRefresh = true;
//        if (isRefresh) {
//            aList.clear();
//            page = 1;
//            initData();
//        }
        pullToRefreshView.onHeaderRefreshComplete();
    }

    private void dismissRefresh() {
        if (isRefresh) {
            pullToRefreshView.onHeaderRefreshComplete();
            isRefresh = false;
        } else if (isUpRefresh) {
            pullToRefreshView.onFooterRefreshComplete();
            isUpRefresh = false;
        }

    }

    private void openGPSSettings() {
        LocationManager alm = (LocationManager) getActivity()
                .getSystemService(Context.LOCATION_SERVICE);
        if (alm.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            getLocation();
        } else {
            aList.clear();
            listView.setAdapter(new ActivityAdapter(getActivity(), aList, 1));
            T.show(getActivity(), "请开启GPS");
        }
    }

    private void getLocation() {
        if (mLocationClient == null) {
            mLocationClient = new AMapLocationClient(getActivity());
        }
        mLocationClient.setLocationListener(mLocationListener);
        if (mLocationOption == null) {
            mLocationOption = new AMapLocationClientOption();
        }
        mLocationOption.setLocationMode(AMapLocationMode.Hight_Accuracy);
        mLocationOption.setNeedAddress(true);
        mLocationOption.setWifiActiveScan(false);
        mLocationOption.setMockEnable(false);
        mLocationOption.setLocationProtocol(AMapLocationProtocol.HTTP);
        mLocationOption.setOnceLocation(true);
        mLocationOption.setOnceLocationLatest(true);
        mLocationClient.setLocationOption(mLocationOption);
        mLocationClient.startLocation();
    }

    //声明定位回调监听器
    public AMapLocationListener mLocationListener = new AMapLocationListener() {

        @Override
        public void onLocationChanged(AMapLocation aMapLocation) {
            if (aMapLocation != null) {
                if (aMapLocation.getErrorCode() == 0) {
                    latitude = aMapLocation.getLatitude() + "";//获取纬度
                    longitude = aMapLocation.getLongitude() + "";//获取经度
                    String city = aMapLocation.getCity();//城市信息
                    leftTv.setText(city);
                    getNdata();
                } else {
                    T.show(getActivity(), "location Error, ErrCode:"
                            + aMapLocation.getErrorCode() + ", errInfo:"
                            + aMapLocation.getErrorInfo());
                }
            }

        }
    };

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }
}
