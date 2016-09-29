package com.dopstore.mall.activity.fragment;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.dopstore.mall.R;
import com.dopstore.mall.activity.adapter.TrolleyAdapter;
import com.dopstore.mall.activity.bean.GoodBean;
import com.dopstore.mall.shop.activity.ConfirmOrderActivity;
import com.dopstore.mall.util.Constant;
import com.dopstore.mall.util.HttpHelper;
import com.dopstore.mall.util.ProUtils;
import com.dopstore.mall.util.SkipUtils;
import com.dopstore.mall.util.T;
import com.dopstore.mall.util.URL;
import com.dopstore.mall.util.UserUtils;
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
 * name 购物车
 */
public class TrolleyFragment extends Fragment {

    private ListView mListView;// 列表

    private TrolleyAdapter mListAdapter;// adapter

    private List<GoodBean> mListData = new ArrayList<GoodBean>();// 数据

    private boolean isBatchModel;// 是否可删除模式

    private RelativeLayout mBottonLayout;
    private LinearLayout checkLayout;
    private CheckBox mCheckAll; // 全选 全不选

    private TextView mEdit; // 切换到删除模式
    private TextView titleTv;

    private TextView mPriceAll; // 商品总价

    private TextView mDelete; // 删除 结算

    private int totalPrice = 0; // 商品总价

    private HttpHelper httpHelper;
    private ProUtils proUtils;

    private View v;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.layout_trolley_fragment, null);
        initView(v);
        initListener();
        loadData();
        return v;
    }


    private void deleteToService(List<GoodBean> mListData) {
        proUtils.show();
        Map<String,String> map=new HashMap<String,String>();
        map.put("user_id", UserUtils.getId(getActivity()));
        map.put("item_id", "");
        httpHelper.postKeyValuePairAsync(getActivity(), URL.CART_DELETE, map, new Callback() {
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
                    if ("0".equals(code)){
                        T.show(getActivity(), "删除成功");
                    }else {
                        String msg = jo.optString(Constant.ERROR_MSG);
                        T.show(getActivity(), msg);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                handler.sendEmptyMessage(UPDATA_CART_MSG);
                proUtils.dismiss();
            }
        }, null);
    }

    private void initView(View v) {
        httpHelper=HttpHelper.getOkHttpClientUtils(getActivity());
        proUtils=new ProUtils(getActivity());
        mBottonLayout = (RelativeLayout) v.findViewById(R.id.cart_rl_allprie_total);
        checkLayout = (LinearLayout) v.findViewById(R.id.trolley_check_box_layout);
        mCheckAll = (CheckBox) v.findViewById(R.id.check_box);
        mEdit = (TextView) v.findViewById(R.id.title_right_textButton);
        mEdit.setText("编辑");
        mEdit.setVisibility(View.VISIBLE);
        titleTv = (TextView) v.findViewById(R.id.title_main_txt);
        titleTv.setTextColor(getActivity().getResources().getColor(R.color.white_color));
        mEdit.setTextColor(getActivity().getResources().getColor(R.color.white_color));
        titleTv.setText("购物车");
        mPriceAll = (TextView) v.findViewById(R.id.tv_cart_total);
        mDelete = (TextView) v.findViewById(R.id.tv_cart_buy_or_del);
        mListView = (ListView) v.findViewById(R.id.listview);
    }

    private void initListener() {
        mEdit.setOnClickListener(listener);
        mDelete.setOnClickListener(listener);
        mCheckAll.setOnClickListener(listener);
    }

    private void loadData() {
        getCartList();
    }

    private void refreshListView() {
        if (mListData.size()>0){
            checkLayout.setVisibility(View.VISIBLE);
        }else {
            checkLayout.setVisibility(View.GONE);
        }
        if (mListAdapter == null) {
            mListAdapter = new TrolleyAdapter(getActivity(), mListData, mPriceAll, totalPrice, mCheckAll);
            mListView.setAdapter(mListAdapter);
        } else {
            mListAdapter.upData(mListData, mPriceAll, totalPrice, mCheckAll);
        }
    }

    private void getCartList() {
        proUtils.show();
        Map<String,String> map=new HashMap<String,String>();
        map.put("user_id", UserUtils.getId(getActivity()));
        httpHelper.postKeyValuePairAsync(getActivity(), URL.CART_QUERY, map, new Callback() {
            @Override
            public void onFailure(Request request, IOException e) {
                T.checkNet(getActivity());
                proUtils.dismiss();
            }

            @Override
            public void onResponse(Response response) throws IOException {
                String body = response.body().string();
                analyData(body);
                handler.sendEmptyMessage(UPDATA_CART_MSG);
                proUtils.dismiss();
            }
        }, null);
    }

    private void analyData(String body) {
        try {
            JSONObject jo = new JSONObject(body);
            String code = jo.optString(Constant.ERROR_CODE);
            if ("0".equals(code)) {
                JSONArray ja = jo.getJSONArray("items");
                if (ja.length() > 0) {
                    for (int i = 0; i < ja.length(); i++) {
                        JSONObject job = ja.getJSONObject(i);
                        GoodBean data = new GoodBean();
                        data.setId(Integer.parseInt(job.optString(Constant.ID)));
                        data.setCarNum(Integer.parseInt(job.optString(Constant.NUMBER)));
                        data.setContent(job.optString(Constant.NAME));
                        data.setPrice(Float.parseFloat(job.optString(Constant.PRICE)));
                        data.setCover(job.optString(Constant.COVER));
                        data.setChoose(false);
                        mListData.add(data);
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



    View.OnClickListener listener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            switch (view.getId()) {

                case R.id.title_right_textButton:
                    isBatchModel = !isBatchModel;
                    if (isBatchModel) {
                        mEdit.setText(getResources().getString(R.string.menu_enter));
                        mDelete.setText(getResources().getString(R.string.menu_del));
                        mBottonLayout.setVisibility(View.GONE);
                    } else {
                        mEdit.setText(getResources().getString(R.string.menu_edit));
                        mBottonLayout.setVisibility(View.VISIBLE);
                        mDelete.setText(getResources().getString(R.string.menu_sett));
                    }
                    break;

                case R.id.check_box:
                    if (mCheckAll.isChecked()) {
                        totalPrice = 0;
                        if (mListData != null) {
                            int size = mListData.size();
                            if (size == 0) {
                                return;
                            }
                            for (int i = 0; i < size; i++) {
                                mListData.get(i).setChoose(true);
                                totalPrice += mListData.get(i).getCarNum() * mListData.get(i).getPrice();
                            }
                            refreshListView();
                            mPriceAll.setText("￥" + totalPrice + "");
                        }
                    } else {
                        if (mListAdapter != null) {
                            totalPrice = 0;
                            for (int i = 0; i < mListData.size(); i++) {
                                mListData.get(i).setChoose(false);
                            }
                            refreshListView();
                            mPriceAll.setText("￥" + 0.00 + "");
                        }
                    }
                    break;

                case R.id.tv_cart_buy_or_del:
                    if (isBatchModel) {
                        deleteToService(mListData);
                        refreshListView();
                        totalPrice = 0;
                        mPriceAll.setText("￥" + 0.00 + "");
                        mCheckAll.setChecked(false);
                    } else {
                        SkipUtils.directJump(getActivity(),ConfirmOrderActivity.class,false);
                        Toast.makeText(getActivity(), "结算", Toast.LENGTH_SHORT).show();
                    }
                    break;
                default:
                    break;
            }
        }
    };

    private final static int UPDATA_CART_MSG = 1;

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case UPDATA_CART_MSG: {
                    refreshListView();
                }
                break;
            }
        }
    };

}
