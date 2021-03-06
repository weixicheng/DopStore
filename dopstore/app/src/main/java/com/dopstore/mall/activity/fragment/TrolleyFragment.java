package com.dopstore.mall.activity.fragment;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.dopstore.mall.R;
import com.dopstore.mall.activity.adapter.TrolleyAdapter;
import com.dopstore.mall.activity.bean.GoodBean;
import com.dopstore.mall.shop.activity.ConfirmOrderActivity;
import com.dopstore.mall.shop.activity.ShopDetailActivity;
import com.dopstore.mall.util.CommHttp;
import com.dopstore.mall.util.Constant;
import com.dopstore.mall.util.SkipUtils;
import com.dopstore.mall.util.T;
import com.dopstore.mall.util.URL;
import com.dopstore.mall.util.UserUtils;
import com.dopstore.mall.util.Utils;
import com.dopstore.mall.view.pulltorefresh.PullToRefreshBase;
import com.dopstore.mall.view.pulltorefresh.PullToRefreshBase.Mode;
import com.dopstore.mall.view.pulltorefresh.PullToRefreshBase.OnRefreshListener;
import com.dopstore.mall.view.pulltorefresh.PullToRefreshListView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;


/**
 * Created by 喜成 on 16/9/5.
 * name 购物车
 */
@SuppressLint("ValidFragment")
public class TrolleyFragment extends Fragment implements OnRefreshListener<ListView> {
    private PullToRefreshListView pullToRefreshView;
    private LinearLayout errorLayout, emptyLayout;
    private TextView loadTv;

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

    private Double totalPrice = 0.00; // 商品总价

    private boolean isRefresh = false;

    private View v;

    private Context context;

    private TimerTask doing;
    private Timer timer;
    private CommHttp httpHelper;

    public TrolleyFragment() {
    }

    public TrolleyFragment(Context context) {
        this.context = context;
        httpHelper = CommHttp.getInstance();
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.layout_trolley_fragment, null);
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
                    }
                };
                timer.schedule(doing, 300);
            }
        } else {

        }
        super.setUserVisibleHint(isVisibleToUser);
    }


    private void deleteToService(final List<GoodBean> mListData) {
        JSONArray ja = new JSONArray();
        for (GoodBean goodBean : mListData) {
            if (goodBean.isChoose() == true) {
                ja.put(goodBean.getId());
            }
        }
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("user_id", UserUtils.getId(context));
        params.put("item_list", ja.toString());
        httpHelper.post(context, URL.CART_DELETE, params, new CommHttp.HttpCallBack() {
            @Override
            public void success(String body) {
                try {
                    JSONObject jo = new JSONObject(body);
                    String code = jo.optString(Constant.ERROR_CODE);
                    if ("0".equals(code)) {
                        T.show(context, "删除成功");
                        isRefresh = false;
                        handler.sendEmptyMessage(LAZY_LOADING_MSG);
                    } else {
                        String msg = jo.optString(Constant.ERROR_MSG);
                        T.show(context, msg);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void failed(String msg) {
                T.checkNet(context);
            }
        });
    }

    private void initView(View v) {
        mBottonLayout = (RelativeLayout) v.findViewById(R.id.cart_rl_allprie_total);
        pullToRefreshView = (PullToRefreshListView) v.findViewById(R.id.main_trolley_fragment_pulltorefreshview);
        checkLayout = (LinearLayout) v.findViewById(R.id.trolley_check_box_layout);
        mCheckAll = (CheckBox) v.findViewById(R.id.check_box);
        errorLayout = (LinearLayout) v.findViewById(R.id.trolley_fragment_error_layout);
        emptyLayout = (LinearLayout) v.findViewById(R.id.trolley_fragment_empty_layout);
        loadTv = (TextView) v.findViewById(R.id.error_data_load_tv);
        mEdit = (TextView) v.findViewById(R.id.title_right_textButton);
        mEdit.setText("编辑");
        mEdit.setVisibility(View.VISIBLE);
        titleTv = (TextView) v.findViewById(R.id.title_main_txt);
        titleTv.setTextColor(getActivity().getResources().getColor(R.color.white_color));
        mEdit.setTextColor(getActivity().getResources().getColor(R.color.white_color));
        titleTv.setText("购物车");
        mPriceAll = (TextView) v.findViewById(R.id.tv_cart_total);
        mDelete = (TextView) v.findViewById(R.id.tv_cart_buy_or_del);
        pullToRefreshView.setMode(Mode.PULL_FROM_START);
        pullToRefreshView.setOnRefreshListener(this);
        mEdit.setOnClickListener(listener);
        mDelete.setOnClickListener(listener);
        mCheckAll.setOnClickListener(listener);
        loadTv.setOnClickListener(listener);
    }


    public void loadData() {
        mListData.clear();
        getCartList();
    }

    private void refreshListView() {
        if (mListData.size() > 0) {
            checkLayout.setVisibility(View.VISIBLE);
            emptyLayout.setVisibility(View.GONE);
        } else {
            checkLayout.setVisibility(View.GONE);
            emptyLayout.setVisibility(View.VISIBLE);
        }
        if (mListAdapter == null) {
            mListAdapter = new TrolleyAdapter(context, mListData, mPriceAll, totalPrice, mCheckAll);
            pullToRefreshView.setAdapter(mListAdapter);
        } else {
            mListAdapter.upData(mListData, mPriceAll, totalPrice, mCheckAll);
        }

        pullToRefreshView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String shop_id = mListData.get(position - 1).getId() + "";
                String shop_name = mListData.get(position - 1).getContent();
                String shop_pic = mListData.get(position - 1).getCover();
                Map<String, Object> map = new HashMap<String, Object>();
                map.put(Constant.ID, shop_id);
                map.put(Constant.NAME, shop_name);
                map.put(Constant.PICTURE, shop_pic);
                SkipUtils.jumpForMap(context, ShopDetailActivity.class, map, false);
            }
        });
    }

    private void getCartList() {
        totalPrice = 0.00;
        mCheckAll.setChecked(false);
        mPriceAll.setText("￥" + 0.00);
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("user_id", UserUtils.getId(context));
        httpHelper.post(context, URL.CART_QUERY, map, new CommHttp.HttpCallBack() {
            @Override
            public void success(String body) {
                errorLayout.setVisibility(View.GONE);
                analyData(body);
                handler.sendEmptyMessage(UPDATA_CART_MSG);
                dismissRefresh();
            }

            @Override
            public void failed(String msg) {
                errorLayout.setVisibility(View.VISIBLE);
                dismissRefresh();
            }
        });
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
                        data.setGoods_sku_id(job.optString("goods_sku_id"));
                        data.setGoods_sku_str(job.optString("goods_sku_str"));
                        String price = job.optString(Constant.PRICE);
                        if (!TextUtils.isEmpty(price)) {
                            if (Utils.isNum(price)) {
                                data.setPrice(Double.parseDouble(price));
                            } else {
                                data.setPrice(0.00);
                            }
                        } else {
                            data.setPrice(0.00);
                        }
                        data.setCover(job.optString(Constant.COVER));
                        data.setChoose(false);
                        mListData.add(data);
                    }
                    emptyLayout.setVisibility(View.GONE);
                } else {
                    emptyLayout.setVisibility(View.VISIBLE);
                }
            } else {
                emptyLayout.setVisibility(View.GONE);
                String msg = jo.optString(Constant.ERROR_MSG);
                T.show(context, msg);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }


    View.OnClickListener listener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.error_data_load_tv: {
                    isRefresh = true;
                    if (isRefresh) {
                        mListData.clear();
                        getCartList();
                    }
                }
                break;
                case R.id.title_right_textButton:
                    if (mListData.size() > 0) {
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
                    }
                    break;
                case R.id.check_box:
                    if (mCheckAll.isChecked()) {
                        totalPrice = 0.00;
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
                            String totalStr = "";
                            if (Utils.isDouble(totalPrice.toString())) {
                                totalStr = Utils.format(totalPrice);
                            } else {
                                totalStr = totalPrice + "";
                            }
                            mPriceAll.setText("￥" + totalStr);
                        }
                    } else {
                        if (mListAdapter != null) {
                            totalPrice = 0.00;
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
                        totalPrice = 0.00;
                        mPriceAll.setText("￥" + 0.00 + "");
                        mCheckAll.setChecked(false);
                    } else {
                        checkOrder();
                    }
                    break;
                default:
                    break;
            }
        }
    };

    private void checkOrder() {
        List<GoodBean> newListData = new ArrayList<GoodBean>();// 数据
        for (int i = 0; i < mListData.size(); i++) {
            boolean flag = mListData.get(i).isChoose();
            if (flag == true) {
                newListData.add(mListData.get(i));
            }
        }
        if (newListData.size() > 0) {
            Map<String, Object> map = new HashMap<String, Object>();
            map.put(Constant.LIST, newListData);
            SkipUtils.jumpForMap(context, ConfirmOrderActivity.class, map, false);
        } else {
            T.show(context, "未选中商品");
        }
    }

    private final static int UPDATA_CART_MSG = 1;
    private final static int LAZY_LOADING_MSG = 0;

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case UPDATA_CART_MSG: {
                    refreshListView();
                }
                break;
                case LAZY_LOADING_MSG: {
                    loadData();
                }
                break;
            }
        }
    };

    private void dismissRefresh() {
        if (isRefresh) {
            pullToRefreshView.onRefreshComplete();
            isRefresh = false;
        }
    }

    @Override
    public void onRefresh(PullToRefreshBase<ListView> refreshView) {
        pullToRefreshView.getLoadingLayoutProxy().setRefreshingLabel("正在刷新");
        pullToRefreshView.getLoadingLayoutProxy().setPullLabel("下拉刷新");
        pullToRefreshView.getLoadingLayoutProxy().setReleaseLabel("释放开始刷新");
        isRefresh = true;
        if (isRefresh) {
            mListData.clear();
            getCartList();
        }

    }
}
