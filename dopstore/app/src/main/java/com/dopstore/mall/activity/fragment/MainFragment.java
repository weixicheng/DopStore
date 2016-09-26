package com.dopstore.mall.activity.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.dopstore.mall.R;
import com.dopstore.mall.activity.MipcaActivityCapture;
import com.dopstore.mall.activity.adapter.TabAdapter;
import com.dopstore.mall.activity.bean.MainTabData;
import com.dopstore.mall.shop.activity.SearchActivity;
import com.dopstore.mall.util.Constant;
import com.dopstore.mall.util.HttpHelper;
import com.dopstore.mall.util.ProUtils;
import com.dopstore.mall.util.SkipUtils;
import com.dopstore.mall.util.T;
import com.dopstore.mall.util.URL;
import com.dopstore.mall.view.EScrollView;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by 喜成 on 16/9/5.
 * name  首页
 */
public class MainFragment extends Fragment {
    private ImageView titleTv;
    private ImageButton leftBtn, rightBtn;
    private EScrollView eScrollView;
    private TabAdapter adapter;
    private List<MainTabData> tabList = new ArrayList<MainTabData>();
    private HttpHelper httpHelper;
    private ProUtils proUtils;

    private FragmentTransaction fragmentTransaction;
    private FragmentManager fragmentManager;
    private FirstMainFragment firstMainFragment;
    private SecondMainFragment secondMainFragment;
    private Fragment currentFragment;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.layout_main_fragment, null);
        initView(v);
        initData();
        return v;
    }

    private void initView(View v) {
        fragmentManager = getActivity().getSupportFragmentManager();
        httpHelper = HttpHelper.getOkHttpClientUtils(getActivity());
        proUtils = new ProUtils(getActivity());
        titleTv = (ImageView) v.findViewById(R.id.title_main_image);
        leftBtn = (ImageButton) v.findViewById(R.id.title_left_imageButton);
        rightBtn = (ImageButton) v.findViewById(R.id.title_right_imageButton);
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
        tabList.clear();
        getTabData();
        setTabSelection(0, "");
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
                adapter.notifyDataSetChanged();
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
                firstMainFragment = new FirstMainFragment();
                fragmentTransaction.replace(R.id.fragment_main_tab_layout, firstMainFragment);
                setCurrentFragment(firstMainFragment);
                break;
            case 1:
                secondMainFragment = new SecondMainFragment(id);
                fragmentTransaction.replace(R.id.fragment_main_tab_layout, secondMainFragment);
                setCurrentFragment(secondMainFragment);
                break;
            default:
                break;
        }

        fragmentTransaction.commit();
    }


    private void hideFragment(FragmentTransaction fragmentTransaction) {

        if (firstMainFragment != null) {
            fragmentTransaction.hide(firstMainFragment);
        }

        if (secondMainFragment != null) {
            fragmentTransaction.hide(secondMainFragment);
        }
    }

    public Fragment getCurrentFragment() {
        return currentFragment;
    }

    public void setCurrentFragment(Fragment currentFragment) {
        this.currentFragment = currentFragment;
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
        if (data==null)return;
        Bundle bundle = data.getExtras();
//        //显示扫描到的内容
//        mTextView.setText(bundle.getString("result"));
//        //显示
//        mImageView.setImageBitmap((Bitmap) data.getParcelableExtra("bitmap"));

    }

    private final static int UPDATA_TAB_CODE = 0;
    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case UPDATA_TAB_CODE: {
                    refreshTabAdapter();
                }
                break;
            }
        }
    };

    private void refreshTabAdapter() {
        if (adapter == null) {
            adapter = new TabAdapter(getActivity(), tabList);
            eScrollView.setAdapter(adapter);
        } else {
            adapter.notifyDataSetChanged();
        }
    }

}
