package com.dopstore.mall.person.activity;

import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.dopstore.mall.R;
import com.dopstore.mall.base.BaseActivity;
import com.dopstore.mall.person.adapter.HelpAdapter;
import com.dopstore.mall.person.bean.HelpData;
import com.dopstore.mall.util.SkipUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 喜成 on 16/9/12.
 * name
 */
public class HelpActivity extends BaseActivity {
    private ListView  listView;
    private List<HelpData> lists=new ArrayList<HelpData>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_help);
        initView();
        initData();
    }

    private void initView() {
        setCustomTitle("帮助中心", getResources().getColor(R.color.white_color));
        leftImageBack(R.mipmap.back_arrow);
        listView = (ListView) findViewById(R.id.help_list);
    }


    private void initData() {
        for (int i=0;i<3;i++){
            HelpData data=new HelpData();
            data.setId(i+"");
            data.setTitle("购物流程");
            lists.add(data);
        }

        listView.setAdapter(new HelpAdapter(this,lists));
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
