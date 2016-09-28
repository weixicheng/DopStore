package com.dopstore.mall.activity.fragment;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationClientOption.AMapLocationProtocol;
import com.amap.api.location.AMapLocationClientOption.AMapLocationMode;
import com.amap.api.location.AMapLocationListener;
import com.dopstore.mall.R;
import com.dopstore.mall.activity.WebActivity;
import com.dopstore.mall.activity.adapter.ActivityAdapter;
import com.dopstore.mall.activity.adapter.HomeAdImageAdapter;
import com.dopstore.mall.activity.bean.ActivityData;
import com.dopstore.mall.activity.bean.CarouselData;
import com.dopstore.mall.shop.activity.ActivityDetailActivity;
import com.dopstore.mall.shop.activity.ActivityListActivity;
import com.dopstore.mall.util.Constant;
import com.dopstore.mall.util.HttpHelper;
import com.dopstore.mall.util.ProUtils;
import com.dopstore.mall.util.SkipUtils;
import com.dopstore.mall.util.T;
import com.dopstore.mall.util.URL;
import com.dopstore.mall.util.UserUtils;
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
public class FirstActivityFragment extends Fragment implements OnHeaderRefreshListener, OnFooterRefreshListener {
    private PullToRefreshView pullToRefreshView;
    private RelativeLayout firstLy, secondLy;
    private TextView firstTv, secondTv;
    private View firstV, secondV;
    private ScrollView mainView;
    private RollPagerView rollPagerView;
    private TextView emptyView;
    private MyListView myListView, otherListView;
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
    private TextView leftTv;
    //声明AMapLocationClient类对象
    public AMapLocationClient mLocationClient = null;
    //声明AMapLocationClientOption对象
    public AMapLocationClientOption mLocationOption = null;
    private View v;

    public FirstActivityFragment(TextView leftTv) {
        this.leftTv = leftTv;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.layout_activity_first_fragment, null);
        initView(v);
        initData();
        return v;
    }

    private void initView(View v) {
        httpHelper = HttpHelper.getOkHttpClientUtils(getActivity());
        proUtils = new ProUtils(getActivity());
        pullToRefreshView = (PullToRefreshView) v.findViewById(R.id.fragment_activity_pulltorefreshview);
        mainView = (ScrollView) v.findViewById(R.id.fragment_activity_main_ly);
        firstLy = (RelativeLayout) v.findViewById(R.id.fragment_activity_first_ly);
        secondLy = (RelativeLayout) v.findViewById(R.id.fragment_activity_second_ly);
        firstTv = (TextView) v.findViewById(R.id.fragment_activity_first_tv);
        secondTv = (TextView) v.findViewById(R.id.fragment_activity_second_tv);
        firstV = v.findViewById(R.id.fragment_activity_first_v);
        secondV = v.findViewById(R.id.fragment_activity_second_v);
        rollPagerView = (RollPagerView) v.findViewById(R.id.roll_view_pager);
        myListView = (MyListView) v.findViewById(R.id.first_fragment_activity_mylistview);
        otherListView = (MyListView) v.findViewById(R.id.first_fragment_activity_near_mylistview);
        emptyView = (TextView) v.findViewById(R.id.fragment_first_activity_empty);
        firstLy.setOnClickListener(listener);
        secondLy.setOnClickListener(listener);
        pullToRefreshView.setOnHeaderRefreshListener(this);
        pullToRefreshView.setOnFooterRefreshListener(this);
    }


    private void initData() {
        getMainData();
    }

    private void getMainData() {
        titleAdvertList.clear();
        aList.clear();
        getCarousel();
        getTdata();
        mainView.fullScroll(ScrollView.FOCUS_UP);
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
                case R.id.fragment_activity_first_ly: {
                    firstTv.setTextColor(getResources().getColor(R.color.red_color_f93448));
                    firstV.setBackgroundColor(getResources().getColor(R.color.red_color_f93448));
                    secondTv.setTextColor(getResources().getColor(R.color.gray_color_33));
                    secondV.setBackgroundColor(getResources().getColor(R.color.white_color));
                    latitude = "";
                    longitude = "";
                    myListView.setVisibility(View.VISIBLE);
                    otherListView.setVisibility(View.GONE);
                    emptyView.setVisibility(View.GONE);
                    aList.clear();
                    getTdata();
                }
                break;
                case R.id.fragment_activity_second_ly: {
                    firstTv.setTextColor(getResources().getColor(R.color.gray_color_33));
                    firstV.setBackgroundColor(getResources().getColor(R.color.white_color));
                    secondTv.setTextColor(getResources().getColor(R.color.red_color_f93448));
                    secondV.setBackgroundColor(getResources().getColor(R.color.red_color_f93448));
                    myListView.setVisibility(View.GONE);
                    otherListView.setVisibility(View.VISIBLE);
                    emptyView.setVisibility(View.GONE);
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


    private void getCarousel() {
        proUtils.show();
        Map<String, String> map = new HashMap<String, String>();
        map.put("project_type", "2");
        map.put("user_id", UserUtils.getId(getActivity()));
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
                        handler.sendEmptyMessage(UPDATA_TOB_CODE);
                    } else {
                        String msg = jo.optString(Constant.ERROR_MSG);
                        T.show(getActivity(), msg);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                proUtils.dismiss();
            }
        }, null);
    }

    private void getTdata() {
        proUtils.show();
        Map<String, String> map = new HashMap<String, String>();
        map.put(Constant.PAGESIZE, "10");
        map.put(Constant.PAGE, page + "");
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
                handler.sendEmptyMessage(UPDATA_MIDDLE_CODE);
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

    private final static int UPDATA_TOB_CODE = 1;
    private final static int UPDATA_MIDDLE_CODE = 2;
    private final static int UPDATA_NFC_CODE = 3;
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
                    refreshAdapter();
                }
                break;
                case UPDATA_NFC_CODE: {
                    refreshNAdapter();
                }
                break;
            }
        }
    };

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
        emptyView.setVisibility(View.GONE);
        if (aList.size()>0) {
            if (adapter == null) {
                adapter = new ActivityAdapter(getActivity(), aList, 0);
                myListView.setAdapter(adapter);
            } else {
                adapter.upData(aList, 0);
            }
            myListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    Map<String, Object> map = new HashMap<String, Object>();
                    map.put(Constant.ID, aList.get(i).getId());
                    SkipUtils.jumpForMap(getActivity(), ActivityDetailActivity.class, map, false);
                }
            });
        }
        mainView.fullScroll(ScrollView.FOCUS_UP);
    }

    private void refreshNAdapter() {
        if (aList.size() > 0) {
            emptyView.setVisibility(View.GONE);
            otherListView.setVisibility(View.VISIBLE);
        } else {
            emptyView.setVisibility(View.VISIBLE);
            otherListView.setVisibility(View.GONE);
        }
        if (aList.size()>0) {
            if (adapter == null) {
                adapter = new ActivityAdapter(getActivity(), aList, 1);
                otherListView.setAdapter(adapter);
            } else {
                adapter.upData(aList, 1);
            }
            otherListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    Map<String, Object> map = new HashMap<String, Object>();
                    map.put(Constant.ID, aList.get(i).getId());
                    SkipUtils.jumpForMap(getActivity(), ActivityDetailActivity.class, map, false);
                }
            });
        }
        mainView.fullScroll(ScrollView.FOCUS_UP);
    }


    @Override
    public void onFooterRefresh(PullToRefreshView view) {
        isUpRefresh = true;
        if (isUpRefresh) {
            page = page + 1;
            initData();
        }
    }

    @Override
    public void onHeaderRefresh(PullToRefreshView view) {
        isRefresh = true;
        if (isRefresh) {
            aList.clear();
            page = 1;
            initData();
        }
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
        if (alm.isProviderEnabled(android.location.LocationManager.GPS_PROVIDER)) {
                   getLocation();
        }else {
            T.show(getActivity(), "请开启GPS");
        }
    }

    private void getLocation() {
        if (mLocationClient==null) {
            mLocationClient = new AMapLocationClient(getActivity());
        }
        mLocationClient.setLocationListener(mLocationListener);
        if (mLocationOption==null) {
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
    public AMapLocationListener mLocationListener = new AMapLocationListener(){

        @Override
        public void onLocationChanged(AMapLocation aMapLocation) {
            if (aMapLocation != null) {
                if (aMapLocation.getErrorCode() == 0) {
                    latitude=aMapLocation.getLatitude()+"";//获取纬度
                    longitude=aMapLocation.getLongitude()+"";//获取经度
                    String city=aMapLocation.getCity();//城市信息
                    leftTv.setText(city);
                    getNdata();
                }else {
                    T.show(getActivity(),"location Error, ErrCode:"
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
