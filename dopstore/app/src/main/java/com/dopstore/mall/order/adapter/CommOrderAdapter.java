package com.dopstore.mall.order.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.dopstore.mall.R;
import com.dopstore.mall.order.bean.CommOrderData;
import com.dopstore.mall.util.Constant;
import com.dopstore.mall.util.LoadImageUtils;

import java.util.List;


public class CommOrderAdapter extends BaseAdapter {

    private List<CommOrderData> items;
    private LayoutInflater mInflater;
    private LoadImageUtils loadImageUtils;

    public CommOrderAdapter(Context context, List<CommOrderData> items) {
        super();
        this.items = items;
        mInflater = LayoutInflater.from(context);
        loadImageUtils = LoadImageUtils.getInstance(context);
    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return items.size();
    }

    @Override
    public Object getItem(int position) {
        // TODO Auto-generated method stub
        return items.get(position);
    }

    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub
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
            CommOrderData data = items.get(position);
            if (data != null) {
                holder.num.setText("✖️ "+data.getNum());
                holder.price.setText("¥"+data.getPrice());
                holder.title.setText(data.getInfo());
                loadImageUtils.displayImage(data.getImage(), holder.imageView);
            }
        }
        return convertView;
    }

    static class ViewHolder {
        private TextView num, price, title;
        private ImageView imageView;
    }

}
