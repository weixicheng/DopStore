package com.dopstore.mall.person.adapter;

import android.content.Context;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.dopstore.mall.R;
import com.dopstore.mall.activity.bean.GoodBean;
import com.dopstore.mall.person.bean.MyCollectData;

import java.util.List;


public class MyCollectAdapter extends BaseAdapter {

    private Context context;
    private List<MyCollectData> mListData;// 数据
    private CheckBox mCheckAll; // 全选 全不选

    public MyCollectAdapter(Context context, List<MyCollectData> mListData, CheckBox mCheckAll) {
        this.context = context;
        this.mListData = mListData;
        this.mCheckAll = mCheckAll;
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

    public void upData(List<MyCollectData> mListData, CheckBox mCheckAll) {
        this.mListData = mListData;
        this.mCheckAll = mCheckAll;
        notifyDataSetChanged();
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.item_my_collect, null);
            holder = new ViewHolder();
            holder.num = (TextView) convertView.findViewById(R.id.item_my_collect_price);
            holder.checkBox = (CheckBox) convertView.findViewById(R.id.item_my_collect_check_box);
            holder.title = (TextView) convertView.findViewById(R.id.item_my_collect_title);
            holder.imageView = (ImageView) convertView.findViewById(R.id.item_my_collect_image);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        MyCollectData data = mListData.get(position);
        holder.title.setText(data.getTitle());
        holder.num.setText("￥" + data.getPrice());
        boolean selected = mListData.get(position).isChoose();
        holder.checkBox.setChecked(selected);

        holder.checkBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MyCollectData bean = mListData.get(position);
                boolean selected = bean.isChoose();
                ;
                if (selected) {
                    mListData.get(position).setChoose(false);
                } else {
                    mListData.get(position).setChoose(true);
                }
                for (MyCollectData bean1 : mListData) {
                    if (bean1.isChoose() == false) {
                        mCheckAll.setChecked(false);
                    } else {
                        mCheckAll.setChecked(true);
                    }
                }
                notifyDataSetChanged();
            }
        });
        return convertView;
    }

}

 class ViewHolder {
    public TextView num, title;
    public ImageView imageView;
    public CheckBox checkBox;
}
