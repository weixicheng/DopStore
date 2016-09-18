package com.dopstore.mall.activity.fragment;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.dopstore.mall.R;
import com.dopstore.mall.activity.adapter.TrolleyAdapter;
import com.dopstore.mall.activity.bean.DataBean;
import com.dopstore.mall.shop.activity.ConfirmOrderActivity;
import com.dopstore.mall.util.SkipUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by 喜成 on 16/9/5.
 * name 购物车
 */
public class TrolleyFragment extends Fragment {
    private static final int INITIALIZE = 0;

    private ListView mListView;// 列表

    private TrolleyAdapter mListAdapter;// adapter

    private List<DataBean> mListData = new ArrayList<DataBean>();// 数据

    private boolean isBatchModel;// 是否可删除模式

    private RelativeLayout mBottonLayout;
    private CheckBox mCheckAll; // 全选 全不选

    private TextView mEdit; // 切换到删除模式
    private TextView titleTv;

    private TextView mPriceAll; // 商品总价

    private TextView mDelete; // 删除 结算

    private int totalPrice = 0; // 商品总价

    private Timer timer;
    private TimerTask doing;
    private final static int LAZY_LOADING_MSG = 0;
    /**
     * 批量模式下，用来记录当前选中状态
     */
    private SparseArray<Boolean> mSelectState = new SparseArray<Boolean>();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.layout_trolley_fragment, null);
        initView(v);
        initListener();
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

    private void doDelete(List<Integer> ids) {
        for (int i = 0; i < mListData.size(); i++) {
            long dataId = mListData.get(i).getId();
            for (int j = 0; j < ids.size(); j++) {
                int deleteId = ids.get(j);
                if (dataId == deleteId) {
                    mListData.remove(i);
                    i--;
                    ids.remove(j);
                    j--;
                }
            }
        }

        refreshListView();
        mSelectState.clear();
        totalPrice = 0;
        mPriceAll.setText("￥" + 0.00 + "");
        mCheckAll.setChecked(false);

    }

    private final List<Integer> getSelectedIds() {
        ArrayList<Integer> selectedIds = new ArrayList<Integer>();
        for (int index = 0; index < mSelectState.size(); index++) {
            if (mSelectState.valueAt(index)) {
                selectedIds.add(mSelectState.keyAt(index));
            }
        }
        return selectedIds;
    }

    private void initView(View v) {
        mBottonLayout = (RelativeLayout) v.findViewById(R.id.cart_rl_allprie_total);
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
//        mListView.setSelector(R.drawable.list_selector);

    }

    private void initListener() {
        mEdit.setOnClickListener(listener);
        mDelete.setOnClickListener(listener);
        mCheckAll.setOnClickListener(listener);
    }

    private void loadData() {
        mListData = getData();
        refreshListView();
    }

    private void refreshListView() {
        if (mListAdapter == null) {
            mListAdapter = new TrolleyAdapter(getActivity(), mListData, mSelectState, mPriceAll, totalPrice, mCheckAll);
            mListView.setAdapter(mListAdapter);
            mListView.setOnItemClickListener(mListAdapter);
        } else {
            mListAdapter.upData(mListData,mListData, mSelectState, mPriceAll, totalPrice, mCheckAll);
        }
    }

    private List<DataBean> getData() {
        int maxId = 0;
        if (mListData != null && mListData.size() > 0)
            maxId = mListData.get(mListData.size() - 1).getId();
        List<DataBean> result = new ArrayList<DataBean>();
        DataBean data = null;
        for (int i = 0; i < 20; i++) {
            data = new DataBean();
            data.setId(maxId + i + 1);// 从最大Id的下一个开始
            data.setShopName("我的" + (maxId + 1 + i) + "店铺");
            data.setContent("我的购物车里面的第" + (maxId + 1 + i) + "个商品");
            data.setCarNum(1);
            data.setPrice(305f);
            result.add(data);
        }
        return result;
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
                            mSelectState.clear();
                            int size = mListData.size();
                            if (size == 0) {
                                return;
                            }
                            for (int i = 0; i < size; i++) {
                                int _id = (int) mListData.get(i).getId();
                                mSelectState.put(_id, true);
                                totalPrice += mListData.get(i).getCarNum() * mListData.get(i).getPrice();
                            }
                            refreshListView();
                            mPriceAll.setText("￥" + totalPrice + "");

                        }
                    } else {
                        if (mListAdapter != null) {
                            totalPrice = 0;
                            mSelectState.clear();
                            refreshListView();
                            mPriceAll.setText("￥" + 0.00 + "");
                        }
                    }
                    break;

                case R.id.tv_cart_buy_or_del:
                    if (isBatchModel) {
                        List<Integer> ids = getSelectedIds();
                        doDelete(ids);
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

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case LAZY_LOADING_MSG: {
                    loadData();
                }
                break;
            }
        }
    };

}
