package com.dopstore.mall.activity.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.dopstore.mall.R;
import com.dopstore.mall.activity.bean.MainMiddleData;
import com.dopstore.mall.activity.bean.ShopData;
import com.dopstore.mall.shop.activity.ShopDetailActivity;
import com.dopstore.mall.util.Constant;
import com.dopstore.mall.util.SkipUtils;
import com.dopstore.mall.view.MyGridView;
import com.dopstore.mall.view.MyListView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by 喜成 on 16/9/5.
 * name 活动Adapter
 */
public class MainOtherShopAdapter extends BaseAdapter {
    private Context context;
    private List<String> list;
    private List<MainMiddleData> midddleList;
    private List<ShopData> bottomList;
    private LayoutInflater inflater;
    final int VIEW_TYPE = 2;
    final int TYPE_1 = 0;
    final int TYPE_2 = 1;

    public MainOtherShopAdapter(Context context, List<MainMiddleData> midddleList, List<ShopData> bottomList) {
        this.context = context;
        list = new ArrayList<String>();
        for (int i = 0; i < 2; i++) {
            list.add(i + "");
        }
        this.midddleList = midddleList;
        this.bottomList = bottomList;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public int getItemViewType(int position) {
        int p = position % 6;
        if (p == 0)
            return TYPE_1;
        else if (p == 1)
            return TYPE_2;
        else
            return TYPE_1;
    }

    @Override
    public int getViewTypeCount() {
        return VIEW_TYPE;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int i) {
        return list.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        MiddleViewHolder holder2 = null;
        BottomViewHolder holder3 = null;
        int type = getItemViewType(position);
        //无convertView，需要new出各个控件
        if (convertView == null) {//按当前所需的样式，确定new的布局
            switch (type) {
                case TYPE_1:
                    convertView = inflater.inflate(R.layout.layout_main_shop_middle_fragment, parent, false);
                    holder2 = new MiddleViewHolder();
                    holder2.myListView = (MyListView) convertView.findViewById(R.id.main_shop_fragment_middle_listview);
                    convertView.setTag(holder2);
                    break;
                case TYPE_2:
                    convertView = inflater.inflate(R.layout.layout_main_shop_bottom_fragment, parent, false);
                    holder3 = new BottomViewHolder();
                    holder3.hotText = (TextView) convertView.findViewById(R.id.main_shop_fragment_bottom_title_tv);
                    holder3.myGridView = (MyGridView) convertView.findViewById(R.id.main_shop_fragment_bottom_gridView);
                    convertView.setTag(holder3);
                    break;
            }
        } else {
            //有convertView，按样式，取得不用的布局
            switch (type) {
                case TYPE_1:
                    holder2 = (MiddleViewHolder) convertView.getTag();
                    break;
                case TYPE_2:
                    holder3 = (BottomViewHolder) convertView.getTag();
                    break;
            }
        }

        //设置资源
        switch (type) {
            case TYPE_1:
                setMiddleData(holder2);
                break;
            case TYPE_2:
                setBottomData(holder3);
                break;
        }

        return convertView;
    }

    class MiddleViewHolder {
        private MyListView myListView;
    }

    class BottomViewHolder {
        private TextView hotText;
        private MyGridView myGridView;
    }

    private void setMiddleData(MiddleViewHolder holder2) {
        holder2.myListView.setAdapter(new MiddleAdapter(context, midddleList));
    }

    private void setBottomData(BottomViewHolder holder3) {
        if (bottomList.size() > 0) {
            holder3.hotText.setVisibility(View.VISIBLE);
        } else {
            holder3.hotText.setVisibility(View.GONE);
        }
        holder3.myGridView.setAdapter(new BottomAdapter(context, bottomList));
        holder3.myGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                ShopData data = bottomList.get(i);
                String id = data.getId();
                Map<String, Object> map = new HashMap<String, Object>();
                map.put(Constant.ID, id);
                map.put(Constant.NAME, data.getName());
                map.put(Constant.PICTURE, data.getCover());
                SkipUtils.jumpForMapResult(context, ShopDetailActivity.class, map, 0);
            }
        });
    }


}
