package com.dopstore.mall.person.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.dopstore.mall.R;
import com.dopstore.mall.person.bean.MyAddressData;

import java.util.List;

/**
 * 地址薄adapter
 */
public class MyAddressAdapter extends BaseAdapter {

    private List<MyAddressData> items;
    private LayoutInflater mInflater;

    public MyAddressAdapter(Context context, List<MyAddressData> items) {
        super();
        this.items = items;
        mInflater = LayoutInflater.from(context);
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
            convertView = mInflater.inflate(R.layout.item_my_address, null);
            holder.name = (TextView) convertView.findViewById(R.id.item_my_address_name);
            holder.phone = (TextView) convertView.findViewById(R.id.item_my_address_phone);
            holder.address = (TextView) convertView.findViewById(R.id.item_my_address_address);
            holder.checkBox = (CheckBox) convertView.findViewById(R.id.item_my_address_check);
            holder.editLy = (LinearLayout) convertView.findViewById(R.id.item_my_address_edit);
            holder.defaultV = convertView.findViewById(R.id.item_my_address_default);
            holder.line = convertView.findViewById(R.id.item_my_address_line);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        if (items != null && items.size() > 0) {
            final MyAddressData data = items.get(position);
            if (data != null) {
                holder.name.setText(data.getName());
                holder.phone.setText(data.getPhone());
                holder.address.setText(data.getAddress());
                String isDefault = data.getIsDefault();
                String isCheck = data.getIsCheck();
                if ("1".equals(isDefault)) {
                    holder.defaultV.setVisibility(View.VISIBLE);
                } else {
                    holder.defaultV.setVisibility(View.GONE);
                }
                if ("1".equals(isCheck)) {
                    holder.checkBox.setChecked(true);
                } else {
                    holder.checkBox.setChecked(false);
                }
                if (position == items.size() - 1) {
                    holder.line.setVisibility(View.GONE);
                } else {
                    holder.line.setVisibility(View.VISIBLE);
                }
                holder.editLy.setOnClickListener(new OnClickListener() {

                    @Override
                    public void onClick(View v) {

                    }
                });
            }
        }
        return convertView;
    }

    static class ViewHolder {
        private TextView name, phone, address;
        private CheckBox checkBox;
        private LinearLayout editLy;
        private View defaultV, line;
    }

}
