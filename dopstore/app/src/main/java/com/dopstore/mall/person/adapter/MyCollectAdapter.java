package com.dopstore.mall.person.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.dopstore.mall.R;
import com.dopstore.mall.person.bean.MyCollectData;
import com.dopstore.mall.util.Constant;
import com.dopstore.mall.util.LoadImageUtils;

import java.util.List;


public class MyCollectAdapter extends BaseAdapter {

    private List<MyCollectData> items;
    private LayoutInflater mInflater;
    private LoadImageUtils loadImageUtils;

    public MyCollectAdapter(Context context, List<MyCollectData> items) {
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
            convertView = mInflater.inflate(R.layout.item_my_collect, null);
            holder.num = (TextView) convertView.findViewById(R.id.item_my_collect_price);
            holder.checkBox = (CheckBox) convertView.findViewById(R.id.item_my_collect_check_box);
            holder.title = (TextView) convertView.findViewById(R.id.item_my_collect_title);
            holder.imageView = (ImageView) convertView.findViewById(R.id.item_my_collect_image);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }


        if (items != null && items.size() > 0) {
            MyCollectData data = items.get(position);
            if (data != null) {
                holder.title.setText(data.getTitle());
                holder.num.setText("¥ " + data.getPrice());
                loadImageUtils.displayImage(data.getImage(), holder.imageView, Constant.OPTIONS_SPECIAL_CODE);
            }
            String isShow = data.getIsShow();
            if ("1".equals(isShow)) {
                holder.checkBox.setVisibility(View.VISIBLE);
            } else {
                holder.checkBox.setVisibility(View.GONE);
            }
            boolean isCheck = data.isSelect();
            if (isCheck) {
                holder.checkBox.setChecked(true);
            } else {
                holder.checkBox.setChecked(false);
            }

        }
        final int mPosition = position;
        final ViewHolder mHolder = holder;
        holder.checkBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mHolder.checkBox.toggle();
                if (mHolder.checkBox.isChecked()) {
                    items.get(mPosition).setSelect(false);
                } else {
                    items.get(mPosition).setSelect(true);
                }
                notifyDataSetChanged();

            }
        });
        return convertView;
    }

    static class ViewHolder {
        private TextView num, title;
        private ImageView imageView;
        private CheckBox checkBox;
    }

}
