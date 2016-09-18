package com.dopstore.mall.activity.fragment;

import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;


import com.dopstore.mall.R;
import com.dopstore.mall.activity.adapter.ActivityAdapter;
import com.dopstore.mall.activity.adapter.AdImageAdapter;
import com.dopstore.mall.activity.adapter.TabAdapter;
import com.dopstore.mall.activity.bean.ActivityData;
import com.dopstore.mall.activity.bean.MainTabData;
import com.dopstore.mall.shop.activity.ActivityDetailActivity;
import com.dopstore.mall.shop.activity.ActivityListActivity;
import com.dopstore.mall.util.SkipUtils;
import com.dopstore.mall.view.EScrollView;
import com.dopstore.mall.view.MyListView;
import com.dopstore.mall.view.rollviewpager.RollPagerView;
import com.dopstore.mall.view.rollviewpager.hintview.ColorPointHintView;

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
    private final static int LAZY_LOADING_MSG = 0;
    private EScrollView eScrollView;
    private ScrollView mainView;
    private RollPagerView rollPagerView;
    private MyListView myListView;
    private List<MainTabData> tabList = new ArrayList<MainTabData>();
    private String[] tabStr = {"推荐", "早教", "游乐场", "亲子游", "儿童剧"};
    private List<Map<String, Object>> titleAdvertList = new ArrayList<Map<String, Object>>();
    private String[] titleAdvertKey = {"id", "image", "url"};
    private List<ActivityData> aList = new ArrayList<ActivityData>();
    private TabAdapter tabAdapter;

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
        mainView = (ScrollView) v.findViewById(R.id.fragment_activity_main_ly);
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
    }

    private void initData() {
        for (int i = 0; i < tabStr.length; i++) {
            MainTabData tabData = new MainTabData();
            tabData.setId(i + "");
            tabData.setName(tabStr[i]);
            if (i == 0) {
                tabData.setIsSelect("1");
            } else {
                tabData.setIsSelect("0");
            }
            tabList.add(tabData);
        }
        for (int i = 0; i < 4; i++) {
            Map<String, Object> map = new HashMap<String, Object>();
            map.put(titleAdvertKey[0], i);
            map.put(titleAdvertKey[1], "http://pic51.nipic.com/file/20141022/19779658_171157758000_2.jpg");
            map.put(titleAdvertKey[2], "");
            titleAdvertList.add(map);
        }
        for (int i = 0; i < 4; i++) {
            ActivityData data = new ActivityData();
            data.setId(i + "");
            data.setImage("http://pic51.nipic.com/file/20141022/19779658_171157758000_2.jpg");
            data.setPrice("¥ 156");
            data.setDistance("<3km");
            data.setTitle("欧克破门刻录机");
            aList.add(data);
        }
        myListView.setAdapter(new ActivityAdapter(getActivity(), aList));
        if (tabAdapter == null) {
            tabAdapter = new TabAdapter(getActivity(), tabList);
            eScrollView.setAdapter(tabAdapter);
        }

        setAdvertisementData();

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
//                if (position==0){
//                    gwPullToRefreshView.setVisibility(View.GONE);
//                    pullToRefreshView.setVisibility(View.VISIBLE);
//                }else {
//                    gwPullToRefreshView.setVisibility(View.VISIBLE);
//                    pullToRefreshView.setVisibility(View.GONE);
//                    setData();
//                }
            }
        });
        mainView.smoothScrollTo(0, 0);

        myListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                SkipUtils.directJump(getActivity(),ActivityDetailActivity.class,false);
            }
        });
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
            rollPagerView.setAdapter(new AdImageAdapter(getActivity(), titleAdvertList, titleAdvertKey));
            //设置指示器（顺序依次）
            //自定义指示器图片
            //设置圆点指示器颜色
            //设置文字指示器
            //隐藏指示器
//            rollPagerView.setHintView(new IconHintView(getActivity(), R.drawable.dot_focus, R.drawable.dot_normal));
            rollPagerView.setHintView(new ColorPointHintView(getActivity(), Color.YELLOW, Color.WHITE));
            //mRollViewPager.setHintView(new TextHintView(this));
            //mRollViewPager.setHintView(null);
            if (titleAdvertList.size() == 1) {
                rollPagerView.pause();
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
                case R.id.title_right_imageButton: {
                    SkipUtils.directJump(getActivity(), ActivityListActivity.class,false);
                }
                break;
            }
        }
    };

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case LAZY_LOADING_MSG: {
                    initData();
                }
                break;
            }
        }
    };


}
