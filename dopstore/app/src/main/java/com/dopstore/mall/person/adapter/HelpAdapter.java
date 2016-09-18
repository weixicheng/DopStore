package com.dopstore.mall.person.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.dopstore.mall.R;
import com.dopstore.mall.person.bean.HelpData;

import java.util.List;


public class HelpAdapter extends BaseAdapter {

    private List<HelpData> items;
    private LayoutInflater mInflater;

    public HelpAdapter(Context context, List<HelpData> items) {
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
            convertView = mInflater.inflate(R.layout.item_help, null);
            holder.title = (TextView) convertView.findViewById(R.id.item_help_title);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        if (items != null && items.size() > 0) {
            final HelpData data = items.get(position);
            if (data != null) {
                holder.title.setText(data.getTitle());
            }
        }
        return convertView;
    }

    static class ViewHolder {
        private TextView title;
    }

}
