package com.dopstore.mall.shop.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.dopstore.mall.R;
import com.dopstore.mall.activity.bean.ShopData;
import com.dopstore.mall.util.LoadImageUtils;

import java.util.List;

/**
 * Created by 喜成 on 16/9/5.
 * name
 */
public class ActivityDetailAdapter extends BaseAdapter {
    private Context context;
    private List<ShopData> list;
    private LoadImageUtils loadImageUtils;

    public ActivityDetailAdapter(Context context, List<ShopData> list) {
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
        ViewHolder viewHolder = null;
        if (view == null) {
            view = LayoutInflater.from(context).inflate(R.layout.item_activity_detail_data_layout, null);
            viewHolder = new ViewHolder();
            viewHolder.bigImageView = (ImageView) view.findViewById(R.id.item_activity_detail_data_image);
            viewHolder.titleTv = (TextView) view.findViewById(R.id.item_activity_detail_data_title);
            viewHolder.priceTv = (TextView) view.findViewById(R.id.item_activity_detail_data_price);
            view.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) view.getTag();
        }
        if (list.size() > 0) {
            loadImageUtils.displayImage(list.get(i).getCover(), viewHolder.bigImageView);
            viewHolder.titleTv.setText(list.get(i).getName());
            viewHolder.priceTv.setText("￥" + list.get(i).getPrice());
        }
        return view;
    }

    class ViewHolder {
        private ImageView bigImageView;
        private TextView titleTv, priceTv;
    }
}
