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
import com.dopstore.mall.shop.bean.ShopListData;
import com.dopstore.mall.util.Constant;
import com.dopstore.mall.util.LoadImageUtils;
import com.dopstore.mall.util.Utils;

import java.util.List;


public class ShopListAdapter extends BaseAdapter {
    private Context context;

    private List<ShopListData> items;
    private LayoutInflater mInflater;
    private LoadImageUtils loadImageUtils;

    public ShopListAdapter(Context context, List<ShopListData> items) {
        super();
        this.context = context;
        this.items = items;
        mInflater = LayoutInflater.from(context);
        loadImageUtils=LoadImageUtils.getInstance(context);
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
            convertView = mInflater.inflate(R.layout.item_shop_list, null);
            holder.title = (TextView) convertView.findViewById(R.id.item_shop_list_title);
            holder.price = (TextView) convertView.findViewById(R.id.item_shop_list_price);
            holder.imageView = (ImageView) convertView.findViewById(R.id.item_shop_list_image);
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
        int picSize = (screenWidth - marginPx * 3) / 2;
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                picSize, picSize-marginPx);
        holder.imageView.setLayoutParams(layoutParams);
        if (items != null && items.size() > 0) {
            ShopListData data = items.get(position);
            if (data != null) {
                holder.title.setText(data.getTitle());
                holder.price.setText("¥ "+data.getPrice());
                loadImageUtils.displayImage(data.getImage(),holder.imageView, Constant.OPTIONS_SPECIAL_CODE);
            }
        }
        return convertView;
    }

    static class ViewHolder {
        private TextView title,price;
        private ImageView imageView;
    }

}
