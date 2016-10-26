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
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.List;


public class MyCollectAdapter extends BaseAdapter {

    private Context context;
    private List<MyCollectData> mListData;// 数据
    private CheckBox mCheckAll;
    private ImageLoader imageLoader;

    public MyCollectAdapter(Context context, List<MyCollectData> mListData, CheckBox mCheckAll) {
        this.context = context;
        this.mListData = mListData;
        this.mCheckAll = mCheckAll;
        imageLoader= ImageLoader.getInstance();
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


    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.item_my_collect, null);
            holder = new ViewHolder();
            holder.num = (TextView) convertView.findViewById(R.id.item_my_collect_price);
            holder.title = (TextView) convertView.findViewById(R.id.item_my_collect_title);
            holder.imageView = (ImageView) convertView.findViewById(R.id.item_my_collect_image);
            holder.checkBox = (CheckBox) convertView.findViewById(R.id.item_my_collect_check_box);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        MyCollectData data = mListData.get(position);
        imageLoader.displayImage(data.getImage()+"?imageView2/1/w/182/h/182", holder.imageView);
        holder.title.setText(data.getTitle());
        holder.num.setText("￥" + Float.parseFloat(data.getPrice()));
        String isShow=data.getIsShow();
        if (isShow.equals("1")){
            holder.checkBox.setVisibility(View.VISIBLE);
        }else {
            holder.checkBox.setVisibility(View.GONE);
        }
        boolean isSelect=data.isChoose();
        if (isSelect){
            holder.checkBox.setChecked(true);
        }else {
            holder.checkBox.setChecked(false);
        }
        holder.checkBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MyCollectData bean = mListData.get(position);
                boolean selected = bean.isChoose();
                if (selected) {
                    mListData.get(position).setChoose(false);
                } else {
                    mListData.get(position).setChoose(true);
                }
                int allCount=0;
                for (MyCollectData bean1 : mListData) {
                    if (bean1.isChoose() == true) {
                        allCount=allCount+1;
                    }
                }
                if (allCount==mListData.size()){
                    mCheckAll.setChecked(true);
                }else {
                    mCheckAll.setChecked(false);
                }
                notifyDataSetChanged();
            }
        });

        return convertView;
    }

    public void upDataList(List<MyCollectData> mListData, CheckBox mCheckAll) {
            this.mCheckAll=mCheckAll;
            this.mListData=mListData;
            notifyDataSetChanged();
    }
}

class ViewHolder {
    public TextView num, title;
    public ImageView imageView;
    public CheckBox checkBox;
}
