package com.dopstore.mall.person.activity;

import android.os.Bundle;
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

import com.dopstore.mall.R;
import com.dopstore.mall.base.BaseActivity;
import com.dopstore.mall.person.adapter.MyCollectAdapter;
import com.dopstore.mall.person.bean.MyCollectData;
import com.dopstore.mall.util.SkipUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 喜成 on 16/9/8.
 * name
 */
public class MyCollectActivity extends BaseActivity {
    private TextView editTv;
    private ListView listView;
    private RelativeLayout bottomLayout;
    private CheckBox checkBox;
    private Button deleteBt;
    private LinearLayout checkLy;
    private MyCollectAdapter myCollectAdapter;
    private List<MyCollectData> myCollectDatas = new ArrayList<MyCollectData>();
    private boolean showFlag = false;
    private boolean allFlag = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_collect);
        initView();
        initData();
    }

    private void initView() {
        setCustomTitle("我的收藏", getResources().getColor(R.color.white_color));
        leftImageBack(R.mipmap.back_arrow);
        listView = (ListView) findViewById(R.id.my_collect_list);
        editTv = (TextView) findViewById(R.id.title_right_textButton);
        editTv.setText("编辑");
        editTv.setVisibility(View.VISIBLE);
        editTv.setTextColor(getResources().getColor(R.color.white_color));
        editTv.setOnClickListener(listener);
        bottomLayout = (RelativeLayout) findViewById(R.id.my_collect_bottom);
        checkBox = (CheckBox) findViewById(R.id.my_collect_check);
        deleteBt = (Button) findViewById(R.id.my_collect_delete);
        checkLy = (LinearLayout) findViewById(R.id.my_collect_check_layout);
        checkLy.setOnClickListener(listener);
        deleteBt.setOnClickListener(listener);

    }

    private void initData() {
        for (int i = 0; i < 3; i++) {
            MyCollectData data = new MyCollectData();
            data.setId("" + i);
            data.setImage("");
            data.setIsShow("0");
            data.setSelect(false);
            data.setPrice("1234");
            data.setTitle("hvuxcvibjkfbdfb");
            myCollectDatas.add(data);
        }
        refreshAdapter();

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                if (myCollectDatas.get(i).isSelect()) {
                    myCollectDatas.get(i).setSelect(false);
                } else {
                    myCollectDatas.get(i).setSelect(true);
                }
                setSelect();
                refreshAdapter();
            }
        });
    }

    private void setSelect() {
        int count = 1;
        for (int i = 0; i < myCollectDatas.size(); i++) {
            if (myCollectDatas.get(i).isSelect()) {
                count++;
            }
        }
        if (count != myCollectDatas.size()) {
            checkBox.setChecked(false);
            allFlag = false;
        } else {
            checkBox.setChecked(true);
            allFlag = true;
        }
    }

    View.OnClickListener listener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.title_right_textButton: {
                    if (showFlag) {
                        editTv.setText("完成");
                        for (MyCollectData data : myCollectDatas) {
                            data.setIsShow("1");
                        }
                        bottomLayout.setVisibility(View.VISIBLE);
                        showFlag = false;
                    } else {
                        editTv.setText("编辑");
                        for (MyCollectData data : myCollectDatas) {
                            data.setIsShow("0");
                        }
                        bottomLayout.setVisibility(View.GONE);
                        showFlag = true;
                    }
                    refreshAdapter();
                }
                break;
                case R.id.my_collect_check_layout: {
                    if (allFlag) {
                        checkBox.setChecked(true);
                        for (MyCollectData data : myCollectDatas) {
                            data.setSelect(true);
                        }
                        allFlag = false;
                    } else {
                        checkBox.setChecked(false);
                        for (MyCollectData data : myCollectDatas) {
                            data.setSelect(false);
                        }
                        allFlag = true;
                    }
                    refreshAdapter();
                }
                break;
                case R.id.my_collect_delete: {

                }
                break;
            }
        }
    };

    private void refreshAdapter() {
        if (myCollectAdapter == null) {
            myCollectAdapter = new MyCollectAdapter(this, myCollectDatas);
            listView.setAdapter(myCollectAdapter);
        } else {
            myCollectAdapter.notifyDataSetChanged();
        }
    }

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
