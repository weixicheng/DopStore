package com.dopstore.mall.shop.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ScrollView;

import com.dopstore.mall.R;
import com.dopstore.mall.activity.adapter.MiddleDataAdapter;
import com.dopstore.mall.activity.bean.MainMiddleData;
import com.dopstore.mall.activity.bean.ShopData;
import com.dopstore.mall.base.BaseActivity;
import com.dopstore.mall.util.Constant;
import com.dopstore.mall.util.SkipUtils;
import com.dopstore.mall.view.MyGridView;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 作者：xicheng on 2016/10/18 15:53
 * 类别：
 */

public class ShopThemeActivity extends BaseActivity {
    private ScrollView scrollView;
    private ImageView topImage;
    private MyGridView myGridView;
    private ImageLoader imageLoader;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shop_theme);
        initView();
        initData();
    }

    private void initView() {
        imageLoader=ImageLoader.getInstance();
        leftImageBack(R.mipmap.back_arrow);
        topImage=(ImageView) findViewById(R.id.shop_theme_image);
        scrollView=(ScrollView) findViewById(R.id.shop_theme_scrollview);
        myGridView=(MyGridView) findViewById(R.id.shop_theme_gridview);
    }
    private void initData() {
        Map<String,Object> map= SkipUtils.getMap(this);
        if (map==null)return;
        MainMiddleData mainMiddleData=(MainMiddleData)map.get(Constant.LIST);
        setCustomTitle(mainMiddleData.getTitle(),getResources().getColor(R.color.white));
        imageLoader.displayImage(mainMiddleData.getPicture(),topImage);
        myGridView.setAdapter(new MiddleDataAdapter(this, mainMiddleData.getRelated_goods()));
        scrollView.smoothScrollBy(0,0);
        final List<ShopData> newDatas=mainMiddleData.getRelated_goods();
        myGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ShopData data=newDatas.get(position);
                Map<String, Object> map = new HashMap<String, Object>();
                map.put(Constant.ID, data.getId());
                map.put(Constant.NAME, data.getName());
                map.put(Constant.PICTURE, data.getCover());
                SkipUtils.jumpForMap(ShopThemeActivity.this, ShopDetailActivity.class, map, false);
            }
        });
    }
}
