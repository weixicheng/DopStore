package com.dopstore.mall.person.activity;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.SparseArray;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.dopstore.mall.R;
import com.dopstore.mall.activity.adapter.TrolleyAdapter;
import com.dopstore.mall.activity.bean.DataBean;
import com.dopstore.mall.base.BaseActivity;
import com.dopstore.mall.person.adapter.MyCollectAdapter;
import com.dopstore.mall.person.bean.MyCollectData;
import com.dopstore.mall.shop.activity.ConfirmOrderActivity;
import com.dopstore.mall.util.SkipUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by 喜成 on 16/9/8.
 * name
 */
public class MyCollectActivity extends BaseActivity {
    private ListView mListView;// 列表

    private MyCollectAdapter mListAdapter;// adapter

    private List<MyCollectData> mListData = new ArrayList<MyCollectData>();// 数据

    private boolean isBatchModel;// 是否可删除模式

    private RelativeLayout mBottonLayout;
    private CheckBox mCheckAll; // 全选 全不选

    private TextView mEdit; // 切换到删除模式

    private TextView mDelete; // 删除

    /**
     * 批量模式下，用来记录当前选中状态
     */
    private SparseArray<Boolean> mSelectState = new SparseArray<Boolean>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_collect);
        initView();
        initData();
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

    private void initView() {
        setCustomTitle("我的收藏", getResources().getColor(R.color.white_color));
        leftImageBack(R.mipmap.back_arrow);
        mListView = (ListView) findViewById(R.id.my_collect_list);
        mEdit = (TextView) findViewById(R.id.title_right_textButton);
        mEdit.setText("编辑");
        mEdit.setVisibility(View.VISIBLE);
        mEdit.setTextColor(getResources().getColor(R.color.white_color));
        mEdit.setOnClickListener(listener);
        mBottonLayout = (RelativeLayout) findViewById(R.id.my_collect_bottom);
        mCheckAll = (CheckBox) findViewById(R.id.my_collect_check_box);
        mDelete = (Button) findViewById(R.id.my_collect_delete);
        mDelete.setOnClickListener(listener);
        mCheckAll.setOnClickListener(listener);
    }

    private void initData() {
        for (int i = 0; i < 3; i++) {
            MyCollectData data = new MyCollectData();
            data.setId(i);
            data.setImage("");
            data.setPrice("1234");
            data.setTitle("hvuxcvibjkfbdfb");
            mListData.add(data);
        }

        refreshListView();
    }

    private void refreshListView() {
        if (mListAdapter == null) {
            mListAdapter = new MyCollectAdapter(this, mListData, mSelectState, mCheckAll);
            mListView.setAdapter(mListAdapter);
            mListView.setOnItemClickListener(mListAdapter);
        } else {
            mListAdapter.upData(mListData,mListData, mSelectState, mCheckAll);
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
                        mBottonLayout.setVisibility(View.VISIBLE);
                    } else {
                        mEdit.setText(getResources().getString(R.string.menu_edit));
                        mBottonLayout.setVisibility(View.GONE);
                    }
                    break;

                case R.id.my_collect_check_box:
                    if (mCheckAll.isChecked()) {
                        if (mListData != null) {
                            mSelectState.clear();
                            int size = mListData.size();
                            if (size == 0) {
                                return;
                            }
                            for (int i = 0; i < size; i++) {
                                int _id = (int) mListData.get(i).getId();
                                mSelectState.put(_id, true);
                            }
                            refreshListView();
                        }
                    } else {
                        if (mListAdapter != null) {
                            mSelectState.clear();
                            refreshListView();
                        }
                    }
                    break;

                case R.id.my_collect_delete:
                    if (isBatchModel) {
                        List<Integer> ids = getSelectedIds();
                        doDelete(ids);
                    } else {
                    }
                    break;
                default:
                    break;
            }
        }
    };

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            SkipUtils.back(this);
            return true;
        } else {
            return super.onKeyDown(keyCode, event);
        }
    }


}
