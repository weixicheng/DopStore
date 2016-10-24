package com.dopstore.mall.activity.fragment;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.dopstore.mall.R;
import com.dopstore.mall.activity.MipcaActivityCapture;
import com.dopstore.mall.activity.WebActivity;
import com.dopstore.mall.activity.adapter.BottomAdapter;
import com.dopstore.mall.activity.adapter.MiddleAdapter;
import com.dopstore.mall.activity.adapter.TabAdapter;
import com.dopstore.mall.activity.bean.CarouselData;
import com.dopstore.mall.activity.bean.MainMiddleData;
import com.dopstore.mall.activity.bean.MainTabData;
import com.dopstore.mall.activity.bean.MiddleData;
import com.dopstore.mall.activity.bean.ShopData;
import com.dopstore.mall.activity.bean.ShopListData;
import com.dopstore.mall.shop.activity.SearchActivity;
import com.dopstore.mall.shop.activity.ShopDetailActivity;
import com.dopstore.mall.util.CommHttp;
import com.dopstore.mall.util.Constant;
import com.dopstore.mall.util.SkipUtils;
import com.dopstore.mall.util.T;
import com.dopstore.mall.util.URL;
import com.dopstore.mall.view.EScrollView;
import com.dopstore.mall.view.MyGridView;
import com.dopstore.mall.view.MyListView;
import com.dopstore.mall.view.RollHeaderView;
import com.dopstore.mall.view.pulltorefresh.PullToRefreshBase;
import com.dopstore.mall.view.pulltorefresh.PullToRefreshBase.Mode;
import com.dopstore.mall.view.pulltorefresh.PullToRefreshBase.OnRefreshListener;
import com.dopstore.mall.view.pulltorefresh.PullToRefreshScrollView;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * 作者：xicheng on 16/9/21 17:57
 * 类别：
 */
@SuppressLint("ValidFragment")
public class MainShopFragment extends Fragment implements OnRefreshListener<ScrollView> {
    private PullToRefreshScrollView pullToRefreshView;
    private TextView titleTv;
    private ImageButton leftBtn, rightBtn;
    private EScrollView eScrollView;
    private TabAdapter adapter;
    private LinearLayout errorLayout;
    private TextView loadTv;
    private List<MainTabData> tabList = new ArrayList<MainTabData>();
    private List<CarouselData> titleAdvertList = new ArrayList<CarouselData>();
    private List<MainMiddleData> midddleList = new ArrayList<MainMiddleData>();
    private List<ShopData> bottomList = new ArrayList<ShopData>();
    private int page = 1;
    private boolean isRefresh = false;
    private boolean isUpRefresh = false;
    private View v;
    private int viewType = 0;
    private String typeId = "";
    private Context mContext;
    private RollHeaderView rollHeaderView;
    private MyListView myListView;
    private TextView hotText;
    private MyGridView myGridView;
    private MiddleAdapter middleAdapter;
    private CommHttp httpHelper;

    @SuppressLint("ValidFragment")
    public MainShopFragment(Context context) {
        this.mContext = context;
        httpHelper=CommHttp.getInstance(context);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.layout_main_shop_fragment, null);
        initView(v);
        initData();
        return v;
    }

    private void initView(View v) {
        pullToRefreshView = (PullToRefreshScrollView) v.findViewById(R.id.main_shop_fragment_pulltorefreshview);
        titleTv = (TextView) v.findViewById(R.id.title_main_txt);
        leftBtn = (ImageButton) v.findViewById(R.id.title_left_imageButton);
        rightBtn = (ImageButton) v.findViewById(R.id.title_right_imageButton);
        errorLayout = (LinearLayout) v.findViewById(R.id.main_shop_fragment_error_layout);
        rollHeaderView = (RollHeaderView) v.findViewById(R.id.layout_main_shop_head_ad);
        myListView = (MyListView) v.findViewById(R.id.main_shop_fragment_middle_listview);
        hotText = (TextView) v.findViewById(R.id.main_shop_fragment_bottom_title_tv);
        myGridView = (MyGridView) v.findViewById(R.id.main_shop_fragment_bottom_gridView);
        loadTv = (TextView) v.findViewById(R.id.error_data_load_tv);
        eScrollView = (EScrollView) v.findViewById(R.id.main_shop_fragment_tab_escrollview);
        titleTv.setText("小海囤");
        titleTv.setTextColor(mContext.getResources().getColor(R.color.white));
        titleTv.setTextSize(20f);
        leftBtn.setBackgroundResource(R.mipmap.search_logo);
        leftBtn.setVisibility(View.VISIBLE);
        leftBtn.setOnClickListener(listener);
        titleTv.setVisibility(View.VISIBLE);
        rightBtn.setVisibility(View.GONE);
        rightBtn.setBackgroundResource(R.mipmap.sweep_logo);
        rightBtn.setOnClickListener(listener);
        loadTv.setOnClickListener(listener);
        pullToRefreshView.setMode(Mode.BOTH);
        pullToRefreshView.setOnRefreshListener(this);
    }

    private void initData() {
        titleAdvertList.clear();
        midddleList.clear();
        bottomList.clear();
        tabList.clear();
        getTabData();
        getTopData();
        getMiddleData("");
        getHotData("");
    }

    private void getTabData() {
        httpHelper.get(mContext, URL.GOODS_CATEGORY, new CommHttp.HttpCallBack() {
            @Override
            public void success(String content) {
                try {
                    JSONObject jo = new JSONObject(content);
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
                            refreshTabAdapter();
                        }
                    } else {
                        String msg = jo.optString(Constant.ERROR_MSG);
                        T.show(mContext, msg);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void failed(String msg) {

            }
        });
    }

    /**
     * 获取数据
     */
    private void getTopData() {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("project_type", "1");
        httpHelper.post(mContext, URL.HOME_CAROUSEL, map, new CommHttp.HttpCallBack() {
            @Override
            public void success(String body) {
                errorLayout.setVisibility(View.GONE);
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
                            rollHeaderView.setVisibility(View.VISIBLE);
                            setHeadData();
                        }else {
                            rollHeaderView.setVisibility(View.GONE);
                        }
                    } else {
                        String msg = jo.optString(Constant.ERROR_MSG);
                        T.show(mContext, msg);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                dismissRefresh();
            }

            @Override
            public void failed(String msg) {
                errorLayout.setVisibility(View.VISIBLE);
                dismissRefresh();
            }
        });
    }

    private void getMiddleData(String id) {
        Map<String, Object> map = new HashMap<String, Object>();
        if (!TextUtils.isEmpty(id)) {
            map.put("category_id", id);
        } else {
            map = null;
        }
        httpHelper.post(mContext, URL.HOME_THEME, map, new CommHttp.HttpCallBack() {
            @Override
            public void success(String body) {
                errorLayout.setVisibility(View.GONE);
                try {
                    JSONObject jo = new JSONObject(body);
                    String code = jo.optString(Constant.ERROR_CODE);
                    if ("0".equals(code)) {
                        Gson gson = new Gson();
                        MiddleData middleData = gson.fromJson(
                                body, MiddleData.class);
                        midddleList = middleData.getThemes();
                        setMiddleData();
                    } else {
                        String msg = new JSONObject(body).optString(Constant.ERROR_MSG);
                        T.show(mContext, msg);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                dismissRefresh();
            }

            @Override
            public void failed(String msg) {
                errorLayout.setVisibility(View.VISIBLE);
                dismissRefresh();
            }
        });

    }

    private void getHotData(String type) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put(Constant.PAGESIZE, "10");
        map.put(Constant.PAGE, page + "");
        if (!TextUtils.isEmpty(type)) {
            map.put("category_id", type);
        } else {
            map.put("is_recommended", "1");
        }
        httpHelper.post(mContext, URL.GOODS_LIST, map, new CommHttp.HttpCallBack() {
            @Override
            public void success(String body) {
                errorLayout.setVisibility(View.GONE);
                try {
                    JSONObject jo = new JSONObject(body);
                    String code = jo.optString(Constant.ERROR_CODE);
                    if ("0".equals(code)) {
                        Gson gson = new Gson();
                        ShopListData shopListData = gson.fromJson(
                                body, ShopListData.class);
                        List<ShopData> list = shopListData.getItems();
                        if (list.size() > 0) {
                            if (isRefresh) {
                                bottomList.clear();
                            }
                        }
                        bottomList.addAll(list);
                        setBottomData(bottomList);
                    } else {
                        String msg = jo.optString(Constant.ERROR_MSG);
                        T.show(mContext, msg);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                dismissRefresh();
            }

            @Override
            public void failed(String msg) {
                errorLayout.setVisibility(View.VISIBLE);
                dismissRefresh();
            }
        });
    }


    View.OnClickListener listener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.title_left_imageButton: {
                    SkipUtils.directJump(mContext, SearchActivity.class, false);
                }
                break;
                case R.id.title_right_imageButton: { // 打开扫描界面扫描条形码或二维码
                    SkipUtils.directJumpForResult(mContext, MipcaActivityCapture.class, SCANER_CODE);
                }
                break;
                case R.id.error_data_load_tv: {
                    isRefresh = true;
                    if (isRefresh) {
                        page = 1;
                        if (viewType == 0) {
                            initData();
                        } else {
                            bottomList.clear();
                            midddleList.clear();
                            getMiddleData(typeId);
                            getHotData(typeId);
                        }
                    }
                }
                break;
            }
        }
    };

    private final int SCANER_CODE = 1;

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data == null) return;
        Bundle bundle = data.getExtras();
//        //显示扫描到的内容
//        mTextView.setText(bundle.getString("result"));
//        //显示
//        mImageView.setImageBitmap((Bitmap) data.getParcelableExtra("bitmap"));

    }


    private void refreshTabAdapter() {
        if (tabList.size() > 0) {
            if (adapter == null) {
                adapter = new TabAdapter(mContext, tabList);
                eScrollView.setAdapter(adapter);
            } else {
                adapter.notifyDataSetChanged();
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
                    adapter.notifyDataSetChanged();
                    if (position == 0) {
                        viewType = 0;
                        rollHeaderView.setVisibility(View.VISIBLE);
                        hotText.setVisibility(View.VISIBLE);
                        bottomAdapter.notifyDataSetChanged();
                        middleAdapter.notifyDataSetChanged();
                        initData();
                    } else {
                        viewType = 1;
                        rollHeaderView.setVisibility(View.GONE);
                        hotText.setVisibility(View.GONE);
                        typeId = tabList.get(position).getId();
                        midddleList.clear();
                        bottomList.clear();
                        bottomAdapter.notifyDataSetChanged();
                        middleAdapter.notifyDataSetChanged();
                        getMiddleData(typeId);
                        getHotData(typeId);
                    }
                }
            }
        });
    }


    private void dismissRefresh() {
        if (isRefresh) {
            isRefresh = false;
        } else if (isUpRefresh) {
            isUpRefresh = false;
        }
        pullToRefreshView.onRefreshComplete();
    }

    private void setHeadData() {
        if (titleAdvertList != null&&titleAdvertList.size()>0) {
            rollHeaderView.setVisibility(View.VISIBLE);
            List<String> imgUrlList = new ArrayList<String>();
            for (CarouselData carouselData : titleAdvertList) {
                imgUrlList.add(carouselData.getPicture());
            }
            rollHeaderView.setImgUrlData(imgUrlList);
        }else {
            rollHeaderView.setVisibility(View.GONE);
        }

        rollHeaderView.setOnHeaderViewClickListener(new RollHeaderView.HeaderViewClickListener() {
            @Override
            public void HeaderViewClick(int position) {
                CarouselData data = titleAdvertList.get(position);
                String urlStr = data.getUrl();
                if (!TextUtils.isEmpty(urlStr)) {
                    Map<String, Object> map = new HashMap<String, Object>();
                    map.put("title", titleAdvertList.get(position).getTitle());
                    map.put("url", titleAdvertList.get(position).getUrl());
                    SkipUtils.jumpForMap(mContext, WebActivity.class, map, false);
                } else {
                    Map<String, Object> map = new HashMap<String, Object>();
                    map.put(Constant.ID, titleAdvertList.get(position).getId());
                    map.put(Constant.NAME, titleAdvertList.get(position).getTitle());
                    map.put(Constant.PICTURE, titleAdvertList.get(position).getPicture());
                    SkipUtils.jumpForMapResult(mContext, ShopDetailActivity.class, map, 0);
                }
            }
        });
    }

    private void setMiddleData() {
        if (middleAdapter == null) {
            middleAdapter = new MiddleAdapter(mContext, midddleList);
            myListView.setAdapter(middleAdapter);
        } else {
            middleAdapter.upData(midddleList);
        }
    }

    private BottomAdapter bottomAdapter;

    private void setBottomData(List<ShopData> list) {
        if (viewType == 1) {
            hotText.setVisibility(View.GONE);
        } else {
            if (list.size()>0){
                hotText.setVisibility(View.VISIBLE);
            }else {
                hotText.setVisibility(View.GONE);
            }
        }

        if (bottomAdapter == null) {
            bottomAdapter = new BottomAdapter(mContext, list);
            myGridView.setAdapter(bottomAdapter);
        } else {
            bottomAdapter.upData(list);
        }
        myGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                ShopData data = bottomList.get(i);
                String id = data.getId();
                Map<String, Object> map = new HashMap<String, Object>();
                map.put(Constant.ID, id);
                map.put(Constant.NAME, data.getName());
                map.put(Constant.PICTURE, data.getCover());
                SkipUtils.jumpForMapResult(mContext, ShopDetailActivity.class, map, 0);
            }
        });
    }

    @Override
    public void onRefresh(PullToRefreshBase refreshView) {
        if (refreshView.isShownHeader()) {
            pullToRefreshView.getLoadingLayoutProxy().setRefreshingLabel("正在刷新");
            pullToRefreshView.getLoadingLayoutProxy().setPullLabel("下拉刷新");
            pullToRefreshView.getLoadingLayoutProxy().setReleaseLabel("释放开始刷新");
            isRefresh = true;
            isUpRefresh = false;
            if (isRefresh) {
                page = 1;
                if (viewType == 0) {
                    getHotData("");
                } else {
                    getHotData(typeId);
                }
            }

        }
        // 上拉加载更多 业务代码
        if (refreshView.isShownFooter()) {
            pullToRefreshView.getLoadingLayoutProxy().setRefreshingLabel("正在加载");
            pullToRefreshView.getLoadingLayoutProxy().setPullLabel("上拉加载更多");
            pullToRefreshView.getLoadingLayoutProxy().setReleaseLabel("释放开始加载");
            isUpRefresh = true;
            isRefresh = false;
            if (isUpRefresh) {
                page = page + 1;
                if (viewType == 0) {
                    getHotData("");
                } else {
                    getHotData(typeId);
                }
            }
        }

    }
}
