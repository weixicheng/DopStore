package com.dopstore.mall.order.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.dopstore.mall.R;
import com.dopstore.mall.order.bean.OrderData;
import com.dopstore.mall.util.LoadImageUtils;

import java.util.List;


public class MyOrderAdapter extends BaseAdapter {

    private List<OrderData> items;
    private LayoutInflater mInflater;
    private LoadImageUtils loadImageUtils;
    private int type;

    public MyOrderAdapter(Context context, List<OrderData> items, int type) {
        super();
        this.items = items;
        this.type = type;
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
            convertView = mInflater.inflate(R.layout.item_my_order, null);
            holder.id = (TextView) convertView.findViewById(R.id.item_my_order_id);
            holder.state = (TextView) convertView.findViewById(R.id.item_my_order_state);
            holder.title = (TextView) convertView.findViewById(R.id.item_my_order_title);
            holder.type = (TextView) convertView.findViewById(R.id.item_my_order_type);
            holder.num = (TextView) convertView.findViewById(R.id.item_my_order_num);
            holder.price = (TextView) convertView.findViewById(R.id.item_my_order_price);
            holder.payV = convertView.findViewById(R.id.item_my_order_pay);
            holder.imageView = (ImageView) convertView.findViewById(R.id.item_my_order_image);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        if (items != null && items.size() > 0) {
            final OrderData data = items.get(position);
            if (data != null) {
                holder.id.setText(data.getId());
                holder.num.setText(data.getNum());
                holder.type.setText(data.getType());
                holder.state.setText(data.getState());
                holder.title.setText(data.getTitle());
                holder.price.setText(data.getPrice());
                loadImageUtils.displayImage(data.getImage(), holder.imageView);
                if (type == 1) {
                    holder.payV.setVisibility(View.VISIBLE);
                } else {
                    holder.payV.setVisibility(View.GONE);
                }
            }
        }
        return convertView;
    }

    static class ViewHolder {
        private TextView id, state, title, type, num, price;
        private View payV;
        private ImageView imageView;
    }

}
