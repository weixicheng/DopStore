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
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
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
    private Timer timer;
    private TimerTask doing;
    private EScrollView eScrollView;
    private List<MainTabData> tabList = new ArrayList<MainTabData>();
    private TabAdapter tabAdapter;
    private HttpHelper httpHelper;
    private ProUtils proUtils;

    private FragmentTransaction fragmentTransaction;
    private FragmentManager fragmentManager;
    private FirstActivityFragment firstActivityFragment;
    private SecondActivityFragment secondActivityFragment;
    private Fragment currentFragment;

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
        fragmentManager = getActivity().getSupportFragmentManager();
        leftTv = (TextView) v.findViewById(R.id.title_left_textView);
        imageButton = (ImageButton) v.findViewById(R.id.title_right_imageButton);
        titleTv = (TextView) v.findViewById(R.id.title_main_txt);
        eScrollView = (EScrollView) v.findViewById(R.id.fragment_activity_tab_escrollview);
        titleTv.setText("亲子活动");
        leftTv.setText("北京");
        leftTv.setVisibility(View.VISIBLE);
        titleTv.setTextColor(getActivity().getResources().getColor(R.color.white_color));
        leftTv.setTextColor(getActivity().getResources().getColor(R.color.white_color));
        leftTv.setOnClickListener(listener);
        imageButton.setImageResource(R.mipmap.search_logo);
        imageButton.setVisibility(View.VISIBLE);
        imageButton.setOnClickListener(listener);
    }

    private void initData() {
        tabList.clear();
        getTabData();
        setTabSelection(0, "");
    }


    View.OnClickListener listener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.title_left_textView: {
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





    private final static int UPDATA_TAB_CODE = 0;
    private final static int LAZY_LOADING_MSG = 1;
    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case UPDATA_TAB_CODE: {
                    refreshTabAdapter();

                }
                break;
                case LAZY_LOADING_MSG: {
                    initData();
                }
                break;

            }
        }
    };

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
                if (position == 0) {
                    setTabSelection(0, "");
                } else {
                    String id = tabList.get(position).getId();
                    setTabSelection(1, id);
                }

            }
        });
    }


    private void setTabSelection(int index, String id) {
        fragmentTransaction = fragmentManager.beginTransaction();
        hideFragment(fragmentTransaction);

        if (null != getCurrentFragment()) {
            getCurrentFragment().onPause();
        }
        switch (index) {
            case 0:
                firstActivityFragment = new FirstActivityFragment();
                fragmentTransaction.replace(R.id.fragment_activity_tab_layout, firstActivityFragment);
                setCurrentFragment(firstActivityFragment);
                break;
            case 1:
                secondActivityFragment = new SecondActivityFragment(id);
                fragmentTransaction.replace(R.id.fragment_activity_tab_layout, secondActivityFragment);
                setCurrentFragment(secondActivityFragment);
                break;
            default:
                break;
        }

        fragmentTransaction.commit();
    }


    private void hideFragment(FragmentTransaction fragmentTransaction) {

        if (firstActivityFragment != null) {
            fragmentTransaction.remove(firstActivityFragment);
        }

        if (secondActivityFragment != null) {
            fragmentTransaction.remove(secondActivityFragment);
        }
    }

    public Fragment getCurrentFragment() {
        return currentFragment;
    }

    public void setCurrentFragment(Fragment currentFragment) {
        this.currentFragment = currentFragment;
    }



}
