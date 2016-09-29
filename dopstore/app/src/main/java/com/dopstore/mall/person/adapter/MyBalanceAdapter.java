package com.dopstore.mall.person.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.dopstore.mall.R;
import com.dopstore.mall.person.bean.MyBalanceData;

import java.util.List;


public class MyBalanceAdapter extends BaseAdapter {

    private List<MyBalanceData> items;
    private LayoutInflater mInflater;

    public MyBalanceAdapter(Context context, List<MyBalanceData> items) {
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
            convertView = mInflater.inflate(R.layout.item_my_balance, null);
            holder.num = (TextView) convertView.findViewById(R.id.item_my_balance_num);
            holder.intro = (TextView) convertView.findViewById(R.id.item_my_balance_intro);
            holder.line = convertView.findViewById(R.id.item_my_balance_line);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        if (items != null && items.size() > 0) {
            final MyBalanceData data = items.get(position);
            if (data != null) {
                holder.num.setText(data.getTitle());
                holder.intro.setText(data.getIntro());
                if (position == items.size() - 1) {
                    holder.line.setVisibility(View.GONE);
                } else {
                    holder.line.setVisibility(View.VISIBLE);
                }
            }
        }
        return convertView;
    }

    static class ViewHolder {
        private TextView num, intro;
        private View line;
    }

}
