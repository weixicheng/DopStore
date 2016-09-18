package com.dopstore.mall.order.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.dopstore.mall.R;
import com.dopstore.mall.order.bean.MyActivityData;
import com.dopstore.mall.util.Constant;
import com.dopstore.mall.util.LoadImageUtils;

import java.util.List;


public class MyActivityAdapter extends BaseAdapter {

    private List<MyActivityData> items;
    private LayoutInflater mInflater;
    private LoadImageUtils loadImageUtils;

    public MyActivityAdapter(Context context, List<MyActivityData> items) {
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
            convertView = mInflater.inflate(R.layout.item_my_activity, null);
            holder.num = (TextView) convertView.findViewById(R.id.item_my_activity_id);
            holder.intro = (TextView) convertView.findViewById(R.id.item_my_activity_intro);
            holder.state = (TextView) convertView.findViewById(R.id.item_my_activity_state);
            holder.title = (TextView) convertView.findViewById(R.id.item_my_activity_title);
            holder.imageView = (ImageView) convertView.findViewById(R.id.item_my_activity_image);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        if (items != null && items.size() > 0) {
            final MyActivityData data = items.get(position);
            if (data != null) {
                holder.num.setText(data.getId());
                holder.state.setText(data.getState());
                holder.title.setText(data.getTitle());
                holder.intro.setText(data.getIntro());
                loadImageUtils.displayImage(data.getImage(), holder.imageView, Constant.OPTIONS_SPECIAL_CODE);
            }
        }
        return convertView;
    }

    static class ViewHolder {
        private TextView num, state, title, intro;
        private ImageView imageView;
    }

}