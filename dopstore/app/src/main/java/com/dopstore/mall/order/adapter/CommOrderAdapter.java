package com.dopstore.mall.order.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.dopstore.mall.R;
import com.dopstore.mall.order.bean.DetailOrderListData;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.List;


public class CommOrderAdapter extends BaseAdapter {

    private List<DetailOrderListData> items;
    private LayoutInflater mInflater;
    private ImageLoader imageLoader;

    public CommOrderAdapter(Context context, List<DetailOrderListData> items) {
        super();
        this.items = items;
        mInflater = LayoutInflater.from(context);
        imageLoader = ImageLoader.getInstance();
    }

    @Override
    public int getCount() {
        return items.size();
    }

    @Override
    public Object getItem(int position) {
        return items.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = mInflater.inflate(R.layout.item_comm_order, null);
            holder.num = (TextView) convertView.findViewById(R.id.comm_order_num);
            holder.price = (TextView) convertView.findViewById(R.id.comm_order_price);
            holder.title = (TextView) convertView.findViewById(R.id.comm_order_title);
            holder.imageView = (ImageView) convertView.findViewById(R.id.comm_order_image);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        if (items != null && items.size() > 0) {
            DetailOrderListData data = items.get(position);
            if (data != null) {
                holder.num.setText("x️ " + data.getGoods_num());
                holder.price.setText("¥" + data.getGoods_price());
                holder.title.setText(data.getGoods_name());
                imageLoader.displayImage(data.getGoods_cover() + "?imageView2/1/w/182/h/182", holder.imageView);
            }
        }
        return convertView;
    }

    static class ViewHolder {
        private TextView num, price, title;
        private ImageView imageView;
    }

}
