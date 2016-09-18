package com.dopstore.mall.shop.adapter;

import android.app.Activity;
import android.content.Context;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.dopstore.mall.R;
import com.dopstore.mall.shop.bean.CommonData;
import com.dopstore.mall.shop.bean.ShopListData;
import com.dopstore.mall.util.Constant;
import com.dopstore.mall.util.LoadImageUtils;
import com.dopstore.mall.util.Utils;

import java.util.List;


public class ShopCarAdapter extends BaseAdapter {
    private Context context;

    private List<CommonData> items;
    private LayoutInflater mInflater;

    public ShopCarAdapter(Context context, List<CommonData> items) {
        super();
        this.context = context;
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
            convertView = mInflater.inflate(R.layout.item_poup_shop_car, null);
            holder.title = (TextView) convertView.findViewById(R.id.item_poup_shop_car);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        DisplayMetrics dm = new DisplayMetrics();
        ((Activity) context).getWindowManager().getDefaultDisplay()
                .getMetrics(dm);
        // 设置图片宽高
        int screenWidth = ((Activity) context).getWindowManager()
                .getDefaultDisplay().getWidth();
        int marginPx = Utils.dip2px(10, dm.density);
        int picSize = (screenWidth - marginPx * 5) / 4;
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                picSize, Utils.dip2px(40, dm.density));
        holder.title.setLayoutParams(layoutParams);
        if (items != null && items.size() > 0) {
            CommonData data = items.get(position);
            if (data != null) {
                holder.title.setText(data.getName());
            }
        }
        return convertView;
    }

    static class ViewHolder {
        private TextView title;
    }

}
