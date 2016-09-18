package com.dopstore.mall.activity.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;

import com.dopstore.mall.R;
import com.dopstore.mall.activity.MipcaActivityCapture;
import com.dopstore.mall.activity.SearchActivity;
import com.dopstore.mall.activity.adapter.BottomAdapter;
import com.dopstore.mall.activity.adapter.MainAdapter;
import com.dopstore.mall.activity.adapter.TabAdapter;
import com.dopstore.mall.activity.bean.MainBottomData;
import com.dopstore.mall.activity.bean.MainTabData;
import com.dopstore.mall.util.SkipUtils;
import com.dopstore.mall.view.EScrollView;
import com.dopstore.mall.view.PullToRefreshView;
import com.dopstore.mall.view.PullToRefreshView.OnFooterRefreshListener;
import com.dopstore.mall.view.PullToRefreshView.OnHeaderRefreshListener;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 喜成 on 16/9/5.
 * name  首页
 */
public class MainFragment extends Fragment implements OnHeaderRefreshListener, OnFooterRefreshListener {
    private ImageView titleTv;
    private ImageButton leftBtn, rightBtn;
    private PullToRefreshView pullToRefreshView, gwPullToRefreshView;
    private ListView contentListView;
    private GridView contentGridView;
    private EScrollView eScrollView;
    private List<MainTabData> tabList = new ArrayList<MainTabData>();
    private List<MainBottomData> list = new ArrayList<MainBottomData>();
    private String[] tabStr = {"推荐", "辅食", "尿不湿", "玩具", "奶粉", "童装"};
    private TabAdapter tabAdapter;
    private final int SCANER_CODE = 0;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.layout_main_fragment, null);
        initView(v);
        initData();
        return v;
    }

    private void initView(View v) {
        titleTv = (ImageView) v.findViewById(R.id.title_main_image);
        leftBtn = (ImageButton) v.findViewById(R.id.title_left_imageButton);
        rightBtn = (ImageButton) v.findViewById(R.id.title_right_imageButton);
        contentListView = (ListView) v.findViewById(R.id.main_content_listView);
        pullToRefreshView = (PullToRefreshView) v.findViewById(R.id.fragment_main_pulltorefreshview);
        gwPullToRefreshView = (PullToRefreshView) v.findViewById(R.id.fragment_main_gw_pulltorefreshview);
        contentGridView = (GridView) v.findViewById(R.id.main_content_gridview);
        eScrollView = (EScrollView) v.findViewById(R.id.fragment_main_tab_escrollview);
        pullToRefreshView.setOnFooterRefreshListener(this);
        pullToRefreshView.setOnHeaderRefreshListener(this);
        gwPullToRefreshView.setOnFooterRefreshListener(this);
        gwPullToRefreshView.setOnHeaderRefreshListener(this);
        titleTv.setImageResource(R.mipmap.title_logo);
        leftBtn.setBackgroundResource(R.mipmap.search_logo);
        leftBtn.setVisibility(View.VISIBLE);
        leftBtn.setOnClickListener(listener);
        titleTv.setVisibility(View.VISIBLE);
        rightBtn.setVisibility(View.VISIBLE);
        rightBtn.setBackgroundResource(R.mipmap.sweep_logo);
        rightBtn.setOnClickListener(listener);
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

        for (int i = 0; i <= 6; i++) {
            MainBottomData listData = new MainBottomData();
            listData.setId(i + "");
            listData.setImage("http://pic51.nipic.com/file/20141022/19779658_171157758000_2.jpg");
            listData.setTitle("幼儿服装幼儿班这个衣服日式风格");
            listData.setPrice("¥ 568");
            list.add(listData);
        }
        contentListView.setAdapter(new MainAdapter(getActivity()));
        if (tabAdapter == null) {
            tabAdapter = new TabAdapter(getActivity(), tabList);
            eScrollView.setAdapter(tabAdapter);
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
                    gwPullToRefreshView.setVisibility(View.GONE);
                    pullToRefreshView.setVisibility(View.VISIBLE);
                } else {
                    gwPullToRefreshView.setVisibility(View.VISIBLE);
                    pullToRefreshView.setVisibility(View.GONE);
                    setData();
                }
            }
        });
    }

    private void setData() {
        contentGridView.setAdapter(new BottomAdapter(getActivity(), list));
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

    @Override
    public void onFooterRefresh(PullToRefreshView view) {
        if (view == pullToRefreshView) {
            pullToRefreshView.onFooterRefreshComplete();
        } else {
            gwPullToRefreshView.onFooterRefreshComplete();
        }
    }

    @Override
    public void onHeaderRefresh(PullToRefreshView view) {
        if (view == pullToRefreshView) {
            pullToRefreshView.onHeaderRefreshComplete();
        } else {
            gwPullToRefreshView.onHeaderRefreshComplete();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Bundle bundle = data.getExtras();
//        //显示扫描到的内容
//        mTextView.setText(bundle.getString("result"));
//        //显示
//        mImageView.setImageBitmap((Bitmap) data.getParcelableExtra("bitmap"));

    }
}
