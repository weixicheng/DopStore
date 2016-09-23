package com.dopstore.mall.activity.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.dopstore.mall.activity.bean.CarouselData;
import com.dopstore.mall.util.Constant;
import com.dopstore.mall.util.LoadImageUtils;
import com.dopstore.mall.view.rollviewpager.adapter.StaticPagerAdapter;

import java.util.List;

/**
 * 轮播广告的Adapter
 */
public class HomeAdImageAdapter extends StaticPagerAdapter {

    private List<CarouselData> adapterList;
    private LoadImageUtils loadImageUtils;

    @SuppressWarnings("deprecation")
    public HomeAdImageAdapter(Context context, List<CarouselData> adapterList) {
        super();
        this.adapterList = adapterList;
        loadImageUtils = LoadImageUtils.getInstance(context);
    }


    @Override
    public View getView(ViewGroup container, int position) {
        ImageView view = new ImageView(container.getContext());
        loadImageUtils.displayImage(adapterList.get(position).getPicture(), view);
        view.setScaleType(ImageView.ScaleType.CENTER_CROP);
        view.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        return view;
    }

    @Override
    public int getCount() {
        return adapterList.size();
    }
}
