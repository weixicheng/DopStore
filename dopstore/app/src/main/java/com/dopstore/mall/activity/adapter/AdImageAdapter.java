package com.dopstore.mall.activity.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.dopstore.mall.util.Constant;
import com.dopstore.mall.util.LoadImageUtils;
import com.dopstore.mall.view.rollviewpager.adapter.StaticPagerAdapter;

import java.util.List;
import java.util.Map;

/**
 * 轮播广告的Adapter
 */
public class AdImageAdapter extends StaticPagerAdapter {

    private List<Map<String, Object>> adapterList;
    private String[] adapterKey;
    private LoadImageUtils loadImageUtils;

    @SuppressWarnings("deprecation")
    public AdImageAdapter(Context context, List<Map<String, Object>> adapterList, String[] adapterKey) {
        super();
        this.adapterList = adapterList;
        this.adapterKey = adapterKey;
        loadImageUtils = LoadImageUtils.getInstance(context);
    }


    @Override
    public View getView(ViewGroup container, int position) {
        ImageView view = new ImageView(container.getContext());
        loadImageUtils.displayImage(adapterList.get(position).get(adapterKey[1]).toString(), view, Constant.OPTIONS_SPECIAL_CODE);
        view.setScaleType(ImageView.ScaleType.CENTER_CROP);
        view.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        return view;
    }

    @Override
    public int getCount() {
        return adapterList.size();
    }
}
