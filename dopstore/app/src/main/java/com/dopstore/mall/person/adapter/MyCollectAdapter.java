package com.dopstore.mall.person.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.dopstore.mall.R;
import com.dopstore.mall.person.bean.MyCollectData;
import com.dopstore.mall.util.LoadImageUtils;

import java.util.List;


public class MyCollectAdapter extends BaseAdapter {

    private Context context;
    private List<MyCollectData> mListData;// 数据
    private LoadImageUtils loadImageUtils;

    public MyCollectAdapter(Context context, List<MyCollectData> mListData) {
        this.context = context;
        this.mListData = mListData;
        loadImageUtils=LoadImageUtils.getInstance(context);
    }

    @Override
    public int getCount() {
        return mListData.size();
    }

    @Override
    public Object getItem(int position) {
        return mListData.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public void upData(List<MyCollectData> mListData) {
        this.mListData = mListData;
        notifyDataSetChanged();
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.item_my_collect, null);
            holder = new ViewHolder();
            holder.num = (TextView) convertView.findViewById(R.id.item_my_collect_price);
            holder.title = (TextView) convertView.findViewById(R.id.item_my_collect_title);
            holder.imageView = (ImageView) convertView.findViewById(R.id.item_my_collect_image);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        MyCollectData data = mListData.get(position);
        loadImageUtils.displayImage(data.getImage(),holder.imageView);
        holder.title.setText(data.getTitle());
        holder.num.setText("￥" + Float.parseFloat(data.getPrice()));
        return convertView;
    }

}

 class ViewHolder {
    public TextView num, title;
    public ImageView imageView;
}
