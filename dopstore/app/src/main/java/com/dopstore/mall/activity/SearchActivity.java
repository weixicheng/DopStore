package com.dopstore.mall.activity;

import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;

import com.dopstore.mall.R;
import com.dopstore.mall.activity.adapter.SearchAdapter;
import com.dopstore.mall.activity.bean.SearchData;
import com.dopstore.mall.base.BaseActivity;
import com.dopstore.mall.shop.activity.ShopListActivity;
import com.dopstore.mall.util.SkipUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 喜成 on 16/9/9.
 * name
 */
public class SearchActivity extends BaseActivity {
    private GridView gridView;
    private List<SearchData> list = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        initView();
        initData();
    }

    private void initView() {
        setCustomTitle("分类", getResources().getColor(R.color.white_color));
        leftImageBack(R.mipmap.back_arrow);
        gridView = (GridView) findViewById(R.id.search_gridview);
    }

    private void initData() {
        for (int i = 0; i < 9; i++) {
            SearchData data = new SearchData();
            data.setImage("");
            data.setName("首页");
            list.add(data);
        }
        gridView.setAdapter(new SearchAdapter(this, list));
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                SkipUtils.directJump(SearchActivity.this, ShopListActivity.class,false);
            }
        });
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
