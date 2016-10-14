package com.dopstore.mall.activity.fragment;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.dopstore.mall.R;
import com.dopstore.mall.activity.MipcaActivityCapture;
import com.dopstore.mall.activity.adapter.MainOtherShopAdapter;
import com.dopstore.mall.activity.adapter.MainShopAdapter;
import com.dopstore.mall.activity.adapter.TabAdapter;
import com.dopstore.mall.activity.bean.CarouselData;
import com.dopstore.mall.activity.bean.MainMiddleData;
import com.dopstore.mall.activity.bean.MainTabData;
import com.dopstore.mall.activity.bean.MiddleData;
import com.dopstore.mall.activity.bean.ShopData;
import com.dopstore.mall.base.BaseFragment;
import com.dopstore.mall.shop.activity.SearchActivity;
import com.dopstore.mall.util.CommHttp;
import com.dopstore.mall.util.Constant;
import com.dopstore.mall.util.SkipUtils;
import com.dopstore.mall.util.T;
import com.dopstore.mall.util.URL;
import com.dopstore.mall.view.EScrollView;
import com.dopstore.mall.view.PullToRefreshView;
import com.dopstore.mall.view.PullToRefreshView.OnFooterRefreshListener;
import com.dopstore.mall.view.PullToRefreshView.OnHeaderRefreshListener;
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
public class MainShopFragment extends BaseFragment implements OnFooterRefreshListener, OnHeaderRefreshListener {
    private PullToRefreshView pullToRefreshView;
    private ListView listView;
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
    private int viewType=0;
    private String typeId="";
    private Context mContext;

    @SuppressLint("ValidFragment")
    public MainShopFragment(Context context) {
        this.mContext = context;
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
        pullToRefreshView = (PullToRefreshView) v.findViewById(R.id.main_shop_fragment_pulltorefreshview);
        listView = (ListView) v.findViewById(R.id.main_shop_fragment_listview);
        titleTv = (TextView) v.findViewById(R.id.title_main_txt);
        leftBtn = (ImageButton) v.findViewById(R.id.title_left_imageButton);
        rightBtn = (ImageButton) v.findViewById(R.id.title_right_imageButton);
        errorLayout = (LinearLayout) v.findViewById(R.id.main_shop_fragment_error_layout);
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
        pullToRefreshView.setOnHeaderRefreshListener(this);
        pullToRefreshView.setOnFooterRefreshListener(this);
    }

    private void initData() {
        titleAdvertList.clear();
        midddleList.clear();
        bottomList.clear();
        tabList.clear();
        getTabData();
        getTopData();
    }

    private void getTabData() {
        if (isRefresh==false){
            proUtils.show();
        }
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
                        }
                    } else {
                        String msg = jo.optString(Constant.ERROR_MSG);
                        T.show(mContext, msg);
                    }
                    handler.sendEmptyMessage(UPDATA_TAB_CODE);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                proUtils.dismiss();
            }

            @Override
            public void failed(String msg) {
                proUtils.dismiss();
            }
        });
    }

    /**
     * 获取数据
     */
    private void getTopData() {
        if (isRefresh==false){
            proUtils.show();
        }
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
                        }
                    } else {
                        String msg = jo.optString(Constant.ERROR_MSG);
                        T.show(mContext, msg);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                dismissRefresh();
                proUtils.dismiss();
            }

            @Override
            public void failed(String msg) {
                errorLayout.setVisibility(View.VISIBLE);
                dismissRefresh();
                proUtils.dismiss();
            }
        });
        getMiddleData("");
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
                    } else {
                        String msg = new JSONObject(body).optString(Constant.ERROR_MSG);
                        T.show(mContext, msg);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                dismissRefresh();
                proUtils.dismiss();
            }

            @Override
            public void failed(String msg) {
                errorLayout.setVisibility(View.VISIBLE);
                dismissRefresh();
                proUtils.dismiss();
            }
        });
        getHotData(id);
    }

    private void getHotData(final String type) {
        if (isRefresh==false){
            proUtils.show();
        }
        Map<String, Object> map = new HashMap<String, Object>();
        if (!TextUtils.isEmpty(type)) {
            map.put("category_id", type);
        } else {
            map.put(Constant.PAGESIZE, "10");
            map.put(Constant.PAGE, page + "");
            map.put("is_recommended", "1");
        }
        httpHelper.post(mContext, URL.GOODS_LIST, map, new CommHttp.HttpCallBack() {
            @Override
            public void success(String body) {
                errorLayout.setVisibility(View.GONE);
                analyData(body);
                if (!TextUtils.isEmpty(type)) {
                    handler.sendEmptyMessage(UPDATA_OTHER_CODE);
                } else {
                    handler.sendEmptyMessage(UPDATA_DATA_CODE);
                }
                dismissRefresh();
                proUtils.dismiss();
            }

            @Override
            public void failed(String msg) {
                errorLayout.setVisibility(View.VISIBLE);
                dismissRefresh();
                proUtils.dismiss();
            }
        });
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
                T.show(mContext, msg);
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
                        if (viewType==0){
                            initData();
                        }else {
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

    private final static int UPDATA_DATA_CODE = 0;
    private final static int UPDATA_OTHER_CODE = 1;
    private final static int UPDATA_TAB_CODE = 2;
    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case UPDATA_DATA_CODE: {
                    refreshAdapter();
                }
                break;
                case UPDATA_OTHER_CODE: {
                    refreshOtherAdapter();
                }
                break;
                case UPDATA_TAB_CODE: {
                    refreshTabAdapter();
                }
                break;
            }
        }
    };

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
                        viewType=0;
                        isRefresh = true;
                        if (isRefresh) {
                            page = 1;
                            initData();
                        }
                    } else {
                        viewType=1;
                        typeId = tabList.get(position).getId();
                        bottomList.clear();
                        midddleList.clear();
                        getMiddleData(typeId);
                        getHotData(typeId);
                    }
                }
            }
        });
    }

    private void refreshAdapter() {
        listView.setAdapter(new MainShopAdapter(mContext, titleAdvertList, midddleList, bottomList));

    }

    private void refreshOtherAdapter() {
        listView.setAdapter(new MainOtherShopAdapter(mContext, midddleList, bottomList));
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
        isRefresh = true;
        if (isRefresh) {
            page = 1;
            if (viewType==0){
                initData();
            }else {
                bottomList.clear();
                midddleList.clear();
                getMiddleData(typeId);
                getHotData(typeId);
            }

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
}
