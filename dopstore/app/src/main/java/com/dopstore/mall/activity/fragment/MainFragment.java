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

import com.dopstore.mall.R;
import com.dopstore.mall.activity.MipcaActivityCapture;
import com.dopstore.mall.activity.SearchActivity;
import com.dopstore.mall.activity.WebActivity;
import com.dopstore.mall.activity.adapter.BottomAdapter;
import com.dopstore.mall.activity.adapter.HomeAdImageAdapter;
import com.dopstore.mall.activity.adapter.MiddleAdapter;
import com.dopstore.mall.activity.adapter.TabAdapter;
import com.dopstore.mall.activity.bean.CarouselData;
import com.dopstore.mall.activity.bean.MainBottomData;
import com.dopstore.mall.activity.bean.MainMiddleData;
import com.dopstore.mall.activity.bean.MainMiddleListData;
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
public class MainFragment extends Fragment  implements OnRefreshListener,OnLoadMoreListener{
    private ImageView titleTv;
    private ImageButton leftBtn, rightBtn;
    private PullToRefreshView pullToRefreshView;
    private RollPagerView rollPagerView;
    private MyListView myListView;
    private MyGridView myGridView,otherGridView;
    private EScrollView eScrollView;
    private List<MainTabData> tabList = new ArrayList<MainTabData>();
    private List<CarouselData> titleAdvertList = new ArrayList<CarouselData>();
    private List<MainMiddleData> midddleList = new ArrayList<MainMiddleData>();
    private List<MainBottomData> bottomList = new ArrayList<MainBottomData>();
    private TabAdapter tabAdapter;
    private final int SCANER_CODE = 0;
    private HttpHelper httpHelper;
    private ProUtils proUtils;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.layout_main_fragment, null);
        initView(v);
        initData();
        return v;
    }

    private void initView(View v) {
        httpHelper=HttpHelper.getOkHttpClientUtils(getActivity());
        proUtils=new ProUtils(getActivity());
        titleTv = (ImageView) v.findViewById(R.id.title_main_image);
        leftBtn = (ImageButton) v.findViewById(R.id.title_left_imageButton);
        rightBtn = (ImageButton) v.findViewById(R.id.title_right_imageButton);
        pullToRefreshView = (PullToRefreshView) v.findViewById(R.id.fragment_main_pulltorefreshview);
        rollPagerView = (RollPagerView) v.findViewById(R.id.roll_view_pager);
        myListView = (MyListView) v.findViewById(R.id.main_middle_listview);
        myGridView = (MyGridView) v.findViewById(R.id.main_bottom_gridView);
        otherGridView = (MyGridView) v.findViewById(R.id.main_content_gridview);
        eScrollView = (EScrollView) v.findViewById(R.id.fragment_main_tab_escrollview);
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
        proUtils.show();
        getTabData();
        getTopData();
        getMiddleData();
        getHotData("0");
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

    private void setData(String id) {//除推荐外
        bottomList.clear();
        getHotData(id);
    }

    private void getTabData() {
        httpHelper.getDataAsync(getActivity(), URL.GOODS_CATEGORY, new Callback() {
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
                    JSONArray ja=jo.getJSONArray(Constant.CATEGORYS);
                        if (ja.length()>0) {
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
        },null);
    }


    /**
     * 获取数据
     */
    private void getTopData() {
        httpHelper.getDataAsync(getActivity(), URL.HOME_CAROUSEL+"1", new Callback() {
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
                    JSONArray ja=jo.getJSONArray(Constant.CAROUSEL);
                        if (ja.length()>0) {
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
            }
        },null);
        proUtils.diamiss();
    }

    private void getMiddleData() {
        Map<String,String> map=new HashMap<String,String>();
        map.put(Constant.PAGESIZE,"10");
        map.put(Constant.PAGE,"2");
        httpHelper.postKeyValuePairAsync(getActivity(), URL.HOME_THEME,map, new Callback() {
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
                        JSONArray ja=jo.getJSONArray(Constant.THEME);
                            if (ja.length()>0) {
                                for (int i = 0; i < ja.length(); i++) {
                                    JSONObject middle = ja.getJSONObject(i);
                                    List<MainMiddleListData> datalist = new ArrayList<MainMiddleListData>();
                                    MainMiddleData middleData = new MainMiddleData();
                                    middleData.setId(middle.optString(Constant.ID));
                                    middleData.setPicture(middle.optString(Constant.PICTURE));
                                    middleData.setUrl(middle.optString(Constant.URL));
                                    middleData.setTitle(middle.optString(Constant.TITLE));
                                    JSONArray jalist=middle.optJSONArray(Constant.RELATED_GOODS);
                                    if (jalist.length()>0){
                                        for (int j=0;j<jalist.length();i++){
                                            JSONObject jolist=jalist.optJSONObject(j);
                                            MainMiddleListData data=new MainMiddleListData();
                                            data.setId(jolist.optString(Constant.ID));
                                            data.setName(jolist.optString(Constant.NAME));
                                            data.setPrice(jolist.optString(Constant.PRICE));
                                            data.setCover(jolist.optString(Constant.COVER));
                                            data.setNumber(jolist.optString(Constant.NUMBER));
                                            data.setDetail(jolist.optString(Constant.DETAIL));
                                            data.setStock_surplus(jolist.optString(Constant.STOCK_SURPLUS));
                                            data.setStock_number(jolist.optString(Constant.STOCK_NUMBER));
                                            datalist.add(data);
                                        }
                                        middleData.setDataList(datalist);
                                    }
                                    midddleList.add(middleData);
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
            }
        },null);
        proUtils.diamiss();
    }
    private void getHotData(String type) {
        Map<String,String> map=new HashMap<String,String>();
        map.put(Constant.PAGESIZE,"10");
        map.put(Constant.PAGE,"2");
        map.put(Constant.CATEGORY,type);
        httpHelper.getDataAsync(getActivity(), URL.GOODS_LIST, new Callback() {
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
                        JSONArray ja=jo.getJSONArray(Constant.CAROUSEL);
                        if (ja.length()>0) {
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
            }
        },null);
        proUtils.diamiss();
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
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Bundle bundle = data.getExtras();
//        //显示扫描到的内容
//        mTextView.setText(bundle.getString("result"));
//        //显示
//        mImageView.setImageBitmap((Bitmap) data.getParcelableExtra("bitmap"));

    }
    private final static int UPDATA_TAB_CODE=0;
    private final static int UPDATA_TOB_CODE=1;
    private final static int UPDATA_MIDDLE_CODE=2;
    private final static int UPDATA_BOTTOM_CODE=3;
    Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case UPDATA_TAB_CODE:{
                    refreshTabAdapter();

                }break;
                case UPDATA_TOB_CODE:{
                    setAdvertisementData();
                }break;
                case UPDATA_MIDDLE_CODE:{
                    refreshMiddleAdapter();
                }break;
                case UPDATA_BOTTOM_CODE:{
                    refreshBottomAdapter();
                }break;

            }
        }
    };

    private void refreshBottomAdapter() {
        myGridView.setAdapter(new BottomAdapter(getActivity(), bottomList));
    }

    private void refreshMiddleAdapter() {
        myListView.setAdapter(new MiddleAdapter(getActivity(), midddleList));
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
                CarouselData data=titleAdvertList.get(position);
                String urlStr=data.getUrl();
                if (!TextUtils.isEmpty(urlStr)){
                    Map<String, Object> map = new HashMap<String, Object>();
                    map.put(Constant.TITLE, data.getTitle());
                    map.put(Constant.URL, urlStr);
                    SkipUtils.jumpForMap(getActivity(), WebActivity.class, map, false);}
            }
        });

    }

    private void refreshTabAdapter() {
        if (tabAdapter==null){
            tabAdapter=new TabAdapter(getActivity(), tabList);
            eScrollView.setAdapter(tabAdapter);
        }else {
            tabAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onLoadMore() {


    }

    @Override
    public void onRefresh() {

    }
}
