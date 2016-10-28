package com.dopstore.mall.order.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.dopstore.mall.R;
import com.dopstore.mall.order.bean.DeliveryData.DeliveryListData;
import com.dopstore.mall.util.Utils;

import java.util.List;


public class DeliveryInfoAdapter extends BaseAdapter {

    private List<DeliveryListData> items;
    private LayoutInflater mInflater;

    public DeliveryInfoAdapter(Context context, List<DeliveryListData> items) {
        super();
        this.items = items;
        mInflater = LayoutInflater.from(context);
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
            convertView = mInflater.inflate(R.layout.item_delivery_info_list, null);
            holder.name = (TextView) convertView.findViewById(R.id.item_delivery_info_name);
            holder.time = (TextView) convertView.findViewById(R.id.item_delivery_info_time);
            holder.line = convertView.findViewById(R.id.item_delivery_info_line);
            holder.empty = convertView.findViewById(R.id.item_delivery_info_line_z);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        if (items != null && items.size() > 0) {
            DeliveryListData data = items.get(position);
            if (data != null) {
                holder.name.setText("【" + data.getLogistics_zone() + "】" + data.getLogistics_remark());
                long time = data.getLogistics_time();
                String logistTime = Utils.formatSecond(time, "yyyy-MM-dd HH:mm:ss");
                holder.time.setText(logistTime);
                if (position == 0) {
                    holder.empty.setVisibility(View.VISIBLE);
                    holder.line.setBackgroundResource(R.drawable.dot_focus);
                } else {
                    holder.empty.setVisibility(View.GONE);
                    holder.line.setBackgroundResource(R.drawable.dot_normal);
                }
            }
        }
        return convertView;
    }

    static class ViewHolder {
        private TextView name, time;
        private View line, empty;
    }

}
