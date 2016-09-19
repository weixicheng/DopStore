package com.dopstore.mall.activity.fragment;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONObject;


import com.dopstore.mall.R;
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
import com.dopstore.mall.view.rollviewpager.RollPagerView;
import com.dopstore.mall.view.rollviewpager.hintview.IconHintView;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import org.json.JSONException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by 喜成 on 16/9/5
 * name 活动
 */
public class ActivityFragment extends Fragment {
    private TextView leftTv, titleTv;
    private ImageButton imageButton;
    private RelativeLayout firstLy, secondLy;
    private TextView firstTv, secondTv;
    private View firstV, secondV;
    private Timer timer;
    private TimerTask doing;
    private EScrollView eScrollView;
    private ScrollView mainView;
    private RollPagerView rollPagerView;
    private MyListView myListView;
    private List<MainTabData> tabList = new ArrayList<MainTabData>();
    private List<CarouselData> titleAdvertList = new ArrayList<CarouselData>();
    private List<ActivityData> aList = new ArrayList<ActivityData>();
    private ActivityAdapter adapter;
    private TabAdapter tabAdapter;
    private HttpHelper httpHelper;
    private ProUtils proUtils;
    private String latitude = "";
    private String longitude = "";

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.layout_activity_fragment, null);
        initView(v);
        return v;
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        if (isVisibleToUser) {
            if (null != timer) {
                return;
            } else {
                timer = new Timer();

                doing = new TimerTask() {
                    // 每个timerTask都要重写这个方法，因为是abstract的
                    public void run() {
                        handler.sendEmptyMessage(LAZY_LOADING_MSG);
                        // 通过sendMessage函数将消息压入线程的消息队列。
                    }
                };
                timer.schedule(doing, 100);
            }
        } else {
            // 不可见时不执行操作
        }
        super.setUserVisibleHint(isVisibleToUser);
    }

    private void initView(View v) {
        httpHelper = HttpHelper.getOkHttpClientUtils(getActivity());
        proUtils = new ProUtils(getActivity());
        mainView = (ScrollView) v.findViewById(R.id.fragment_activity_main_ly);
        firstLy = (RelativeLayout) v.findViewById(R.id.fragment_activity_first_ly);
        secondLy = (RelativeLayout) v.findViewById(R.id.fragment_activity_second_ly);
        firstTv = (TextView) v.findViewById(R.id.fragment_activity_first_tv);
        secondTv = (TextView) v.findViewById(R.id.fragment_activity_second_tv);
        firstV = v.findViewById(R.id.fragment_activity_first_v);
        secondV = v.findViewById(R.id.fragment_activity_second_v);
        leftTv = (TextView) v.findViewById(R.id.title_left_textView);
        imageButton = (ImageButton) v.findViewById(R.id.title_right_imageButton);
        titleTv = (TextView) v.findViewById(R.id.title_main_txt);
        eScrollView = (EScrollView) v.findViewById(R.id.fragment_activity_tab_escrollview);
        rollPagerView = (RollPagerView) v.findViewById(R.id.roll_view_pager);
        myListView = (MyListView) v.findViewById(R.id.fragment_activity_mylistview);
        titleTv.setText("亲子活动");
        leftTv.setText("北京");
        leftTv.setVisibility(View.VISIBLE);
        titleTv.setTextColor(getActivity().getResources().getColor(R.color.white_color));
        leftTv.setTextColor(getActivity().getResources().getColor(R.color.white_color));
        leftTv.setOnClickListener(listener);
        imageButton.setImageResource(R.mipmap.search_logo);
        imageButton.setVisibility(View.VISIBLE);
        imageButton.setOnClickListener(listener);
        firstLy.setOnClickListener(listener);
        secondLy.setOnClickListener(listener);
    }

    private void initData() {
        getGPSData();
        proUtils.show();
        getTabData();
        getCarousel();
        latitude="";
        longitude="";
        getData(latitude, longitude);
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
                    latitude="";
                    longitude="";
                    getData(latitude, longitude);
                }
                break;
                case R.id.fragment_activity_second_ly: {
                    firstTv.setTextColor(getResources().getColor(R.color.gray_color_33));
                    firstV.setBackgroundColor(getResources().getColor(R.color.white_color));
                    secondTv.setTextColor(getResources().getColor(R.color.red_color_f93448));
                    secondV.setBackgroundColor(getResources().getColor(R.color.red_color_f93448));
                    getData(latitude, longitude);
                }
                break;
                case R.id.title_right_imageButton: {
                    SkipUtils.directJump(getActivity(), ActivityListActivity.class, false);
                }
                break;
            }
        }
    };

    private void getTabData() {
        httpHelper.getDataAsync(getActivity(), URL.ACT_CATEGORIES, new Callback() {
            @Override
            public void onFailure(Request request, IOException e) {
                T.checkNet(getActivity());
            }

            @Override
            public void onResponse(Response response) throws IOException {
                String body = response.body().string();
                try {
                    JSONObject jo = new JSONObject(body);
                    String code = jo.optString(Constant.ERROR_CODE);
                    if ("0".equals(code)) {
                        JSONArray ja = jo.getJSONArray(Constant.CATEGORYS);
                        if (ja.length() > 0) {
                            for (int i = 0; i < ja.length(); i++) {
                                JSONObject tab = ja.getJSONObject(i);
                                MainTabData tabData = new MainTabData();
                                tabData.setId(tab.optString(Constant.ID));
                                tabData.setName(tab.optString(Constant.NAME));
                                tabData.setPicture(tab.optString(Constant.PICTURE));
                                if (i == 0) {
                                    tabData.setIsSelect("1");
                                } else {
                                    tabData.setIsSelect("0");
                                }
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
                proUtils.diamiss();
            }
        }, null);
    }


    private void getCarousel() {
        httpHelper.getDataAsync(getActivity(), URL.HOME_CAROUSEL + "2", new Callback() {
            @Override
            public void onFailure(Request request, IOException e) {
                T.checkNet(getActivity());
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
                proUtils.diamiss();
            }
        }, null);
    }

    private void getData(String lat, String lng) {
        Map<String, String> map = new HashMap<String, String>();
        map.put(Constant.PAGESIZE, "10");
        map.put(Constant.PAGE, "2");
        map.put(Constant.LAT, lat);
        map.put(Constant.LNG, lng);
        map.put(Constant.CATEGORY_ID, "2");
        httpHelper.postKeyValuePairAsync(getActivity(), URL.RECOMMENDED_ACT, map, new Callback() {
            @Override
            public void onFailure(Request request, IOException e) {
                T.checkNet(getActivity());
            }

            @Override
            public void onResponse(Response response) throws IOException {
                String body = response.body().string();
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
                    handler.sendEmptyMessage(UPDATA_MIDDLE_CODE);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                proUtils.diamiss();
            }
        }, null);
    }

    private final static int UPDATA_TAB_CODE = 0;
    private final static int UPDATA_TOB_CODE = 1;
    private final static int UPDATA_MIDDLE_CODE = 2;
    private final static int LAZY_LOADING_MSG = 3;
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
                    refreshAdapter();
                }
                break;
                case LAZY_LOADING_MSG: {
                    initData();
                }
                break;

            }
        }
    };

    private void refreshAdapter() {
        if (adapter == null) {
            adapter = new ActivityAdapter(getActivity(), aList);
            myListView.setAdapter(adapter);
        } else {
            adapter.notifyDataSetChanged();
        }
        myListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                SkipUtils.directJump(getActivity(), ActivityDetailActivity.class, false);
            }
        });
    }

    private void refreshTabAdapter() {
        if (tabAdapter == null) {
            tabAdapter = new TabAdapter(getActivity(), tabList);
            eScrollView.setAdapter(tabAdapter);
        } else {
            tabAdapter.notifyDataSetChanged();
        }
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

            }
        });
    }

    private void getGPSData() {
        LocationManager locationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
        if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                return;
            }
            Location location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            if (location != null) {
                latitude = location.getLatitude()+"";
                longitude = location.getLongitude()+"";
            }
        } else {
            LocationListener locationListener = new LocationListener() {

                // Provider的状态在可用、暂时不可用和无服务三个状态直接切换时触发此函数
                @Override
                public void onStatusChanged(String provider, int status, Bundle extras) {

                }

                // Provider被enable时触发此函数，比如GPS被打开
                @Override
                public void onProviderEnabled(String provider) {

                }

                // Provider被disable时触发此函数，比如GPS被关闭
                @Override
                public void onProviderDisabled(String provider) {

                }

                //当坐标改变时触发此函数，如果Provider传进相同的坐标，它就不会被触发
                @Override
                public void onLocationChanged(Location location) {
                    if (location != null) {
                        Log.e("Map", "Location changed : Lat: "
                                + location.getLatitude() + " Lng: "
                                + location.getLongitude());
                    }
                }
            };
            locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 1000, 0, locationListener);
            Location location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
            if (location != null) {
                latitude = location.getLatitude()+""; //经度
                longitude = location.getLongitude()+""; //纬度
            }
        }
    }


}
