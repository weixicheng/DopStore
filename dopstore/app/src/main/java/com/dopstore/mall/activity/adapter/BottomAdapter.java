package com.dopstore.mall.activity.adapter;

import android.app.Activity;
import android.content.Context;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.dopstore.mall.R;
import com.dopstore.mall.activity.bean.MainBottomData;
import com.dopstore.mall.util.Constant;
import com.dopstore.mall.util.LoadImageUtils;
import com.dopstore.mall.util.Utils;

import java.util.List;

/**
 * Created by 喜成 on 16/9/5.
 * name 首页底部数据
 */
public class BottomAdapter extends BaseAdapter {
    private Context context;
    private List<MainBottomData> list;
    private LoadImageUtils loadImageUtils;

    public BottomAdapter(Context context, List<MainBottomData> list) {
        this.context = context;
        this.list = list;
        loadImageUtils = LoadImageUtils.getInstance(context);
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int i) {
        return list.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ViewHolder viewHolder;
        if (view == null) {
            view = LayoutInflater.from(context).inflate(R.layout.item_main_bottom_data_layout, null);
            viewHolder = new ViewHolder();
            viewHolder.bigImageView = (ImageView) view.findViewById(R.id.main_bottom_data_image);
            viewHolder.titleTv = (TextView) view.findViewById(R.id.main_bottom_data_title);
            viewHolder.priceTv = (TextView) view.findViewById(R.id.main_bottom_data_price);
            view.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) view.getTag();
        }

        DisplayMetrics dm = new DisplayMetrics();
        ((Activity) context).getWindowManager().getDefaultDisplay()
                .getMetrics(dm);
        // 设置图片宽高
        int screenWidth = ((Activity) context).getWindowManager()
                .getDefaultDisplay().getWidth();
        int marginPx = Utils.dip2px(10, dm.density);
        int marginPxq = Utils.dip2px(15, dm.density);
        int picSize = (screenWidth - marginPxq * 2 - marginPx) / 2;
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                picSize, picSize);
        viewHolder.bigImageView.setLayoutParams(layoutParams);

        loadImageUtils.displayImage(list.get(i).getImage(), viewHolder.bigImageView, Constant.OPTIONS_SPECIAL_CODE);
        viewHolder.titleTv.setText(list.get(i).getTitle());
        viewHolder.priceTv.setText(list.get(i).getPrice());
        return view;
    }

    class ViewHolder {
        private ImageView bigImageView;
        private TextView titleTv, priceTv;
    }
}
