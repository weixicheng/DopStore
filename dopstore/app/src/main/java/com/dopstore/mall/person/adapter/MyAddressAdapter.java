package com.dopstore.mall.person.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.dopstore.mall.R;
import com.dopstore.mall.person.activity.NewAddressActivity;
import com.dopstore.mall.person.bean.MyAddressData;
import com.dopstore.mall.util.Constant;
import com.dopstore.mall.util.SkipUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 地址薄adapter
 */
public class MyAddressAdapter extends BaseAdapter {

    private List<MyAddressData> items;
    private LayoutInflater mInflater;
    public Context  context;

    public MyAddressAdapter(Context context, List<MyAddressData> items) {
        super();
        this.items = items;
        this.context = context;
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
            convertView = mInflater.inflate(R.layout.item_my_address, null);
            holder.name = (TextView) convertView.findViewById(R.id.item_my_address_name);
            holder.phone = (TextView) convertView.findViewById(R.id.item_my_address_phone);
            holder.address = (TextView) convertView.findViewById(R.id.item_my_address_address);
            holder.checkBox = convertView.findViewById(R.id.item_my_address_check);
            holder.checkBoxLayout = (LinearLayout) convertView.findViewById(R.id.item_my_address_check_layout);
            holder.editLy = (LinearLayout) convertView.findViewById(R.id.item_my_address_edit);
            holder.defaultV = convertView.findViewById(R.id.item_my_address_default);
            holder.line = convertView.findViewById(R.id.item_my_address_line);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        if (items != null && items.size() > 0) {
            MyAddressData data = items.get(position);
            if (data != null) {
                holder.name.setText(data.getShipping_user());
                holder.phone.setText(data.getMobile());
                holder.address.setText(data.getProvince()+data.getCity()+data.getArea()+data.getAddress());
                String isDefault = data.getIs_default();
                String isCheck = data.getIs_check();
                if ("1".equals(isDefault)) {
                    holder.defaultV.setVisibility(View.VISIBLE);
                } else {
                    holder.defaultV.setVisibility(View.GONE);
                }
                if ("1".equals(isCheck)) {
                    holder.checkBox.setBackgroundResource(R.mipmap.checkbox_checked);
                } else {
                    holder.checkBox.setBackgroundResource(R.mipmap.checkbox_normal);
                }
                if (position == items.size() - 1) {
                    holder.line.setVisibility(View.GONE);
                } else {
                    holder.line.setVisibility(View.VISIBLE);
                }
                final int mPosition=position;
                holder.editLy.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Map<String,Object> map=new HashMap<String, Object>();
                        map.put(Constant.LIST,items.get(mPosition));
                        SkipUtils.jumpForMapResult(context, NewAddressActivity.class,map, 1);
                    }
                });
                holder.checkBoxLayout.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        String isCheck=items.get(mPosition).getIs_check();
                        if ("1".equals(isCheck)){
                            items.get(mPosition).setIs_check("0");
                        }else {
                            items.get(mPosition).setIs_check("1");
                        }
                        notifyDataSetChanged();
                    }
                });
            }
        }
        return convertView;
    }

    public void upData(List<MyAddressData> listData) {
        this.items=listData;
        notifyDataSetChanged();
    }

    static class ViewHolder {
        private TextView name, phone, address;
        private LinearLayout checkBoxLayout;
        private View checkBox;
        private LinearLayout editLy;
        private View defaultV, line;
    }

}
