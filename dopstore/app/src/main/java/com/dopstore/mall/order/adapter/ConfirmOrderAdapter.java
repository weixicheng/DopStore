package com.dopstore.mall.order.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.dopstore.mall.R;
import com.dopstore.mall.activity.bean.GoodBean;
import com.dopstore.mall.util.LoadImageUtils;

import java.util.List;


public class ConfirmOrderAdapter extends BaseAdapter {

    private List<GoodBean> items;
    private LayoutInflater mInflater;
    private LoadImageUtils loadImageUtils;

    public ConfirmOrderAdapter(Context context, List<GoodBean> items) {
        super();
        this.items = items;
        mInflater = LayoutInflater.from(context);
        loadImageUtils = LoadImageUtils.getInstance(context);
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
            holder.type = (TextView) convertView.findViewById(R.id.comm_order_type);
            holder.price = (TextView) convertView.findViewById(R.id.comm_order_price);
            holder.title = (TextView) convertView.findViewById(R.id.comm_order_title);
            holder.imageView = (ImageView) convertView.findViewById(R.id.comm_order_image);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        if (items != null && items.size() > 0) {
            GoodBean data = items.get(position);
            if (data != null) {
                holder.type.setText(data.getGoods_sku_str());
                holder.num.setText("x " + data.getCarNum());
                holder.price.setText("¥" + data.getPrice());
                holder.title.setText(data.getContent());
                loadImageUtils.displayImage(data.getCover()+"?imageView2/1/w/182/h/182", holder.imageView);
            }
        }
        return convertView;
    }

    static class ViewHolder {
        private TextView num, price, title, type;
        private ImageView imageView;
    }

}
