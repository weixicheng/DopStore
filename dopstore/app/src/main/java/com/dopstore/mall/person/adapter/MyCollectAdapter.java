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
import com.dopstore.mall.activity.bean.DataBean;
import com.dopstore.mall.person.bean.MyCollectData;
import com.dopstore.mall.util.Constant;
import com.dopstore.mall.util.LoadImageUtils;

import java.util.List;


public class MyCollectAdapter extends BaseAdapter implements AdapterView.OnItemClickListener{

    private Context context;
    private List<MyCollectData> mListData;// 数据
    private SparseArray<Boolean> mSelectState;
    private CheckBox mCheckAll; // 全选 全不选

    public MyCollectAdapter(Context context, List<MyCollectData> mListData, SparseArray<Boolean> mSelectState, CheckBox mCheckAll) {
        this.context = context;
        this.mListData = mListData;
        this.mSelectState = mSelectState;
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

    public void upData(List<MyCollectData> listData, List<MyCollectData> mListData, SparseArray<Boolean> mSelectState, CheckBox mCheckAll){
        this.mListData=mListData;
        this.mListData = mListData;
        this.mSelectState = mSelectState;
        this.mCheckAll = mCheckAll;
        notifyDataSetChanged();
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.item_my_collect, null);
            holder = new ViewHolder();
            holder.num= (TextView) convertView.findViewById(R.id.item_my_collect_price);
            holder.checkBox = (CheckBox) convertView.findViewById(R.id.item_my_collect_check_box);
            holder.title = (TextView) convertView.findViewById(R.id.item_my_collect_title);
            holder.imageView = (ImageView) convertView.findViewById(R.id.item_my_collect_image);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        MyCollectData data = mListData.get(position);
        bindListItem(holder, data);
        return convertView;
    }

    private void bindListItem(ViewHolder holder, MyCollectData data) {
        holder.title.setText(data.getTitle());
        holder.num.setText("￥" + data.getPrice());
        int _id = data.getId();
        boolean selected = mSelectState.get(_id, false);
        holder.checkBox.setChecked(selected);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        MyCollectData bean = mListData.get(position);

        ViewHolder holder = (ViewHolder) view.getTag();
        int _id = (int) bean.getId();

        boolean selected = !mSelectState.get(_id, false);
        holder.checkBox.toggle();
        if (selected) {
            mSelectState.put(_id, true);
        } else {
            mSelectState.delete(_id);
        }
        if (mSelectState.size() == mListData.size()) {
            mCheckAll.setChecked(true);
        } else {
            mCheckAll.setChecked(false);
        }
    }
}

 class ViewHolder {
    public TextView num, title;
    public ImageView imageView;
    public CheckBox checkBox;
}
