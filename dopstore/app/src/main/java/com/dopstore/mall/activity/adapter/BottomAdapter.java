package com.dopstore.mall.activity.adapter;

import android.app.Activity;
import android.content.Context;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.dopstore.mall.R;
import com.dopstore.mall.activity.bean.ShopData;
import com.dopstore.mall.util.Utils;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.List;

/**
 * Created by 喜成 on 16/9/5.
 * name 首页底部数据
 */
public class BottomAdapter extends BaseAdapter {
    private Context context;
    private List<ShopData> list;
    private ImageLoader imageLoader;

    public BottomAdapter(Context context, List<ShopData> list) {
        this.context = context;
        this.list = list;
        imageLoader = ImageLoader.getInstance();
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
    public View getView(int i, View view, ViewGroup viewGroup) {
        ViewHolder viewHolder;
        if (view == null) {
            view = LayoutInflater.from(context).inflate(R.layout.item_main_bottom_data_layout, null);
            viewHolder = new ViewHolder();
            viewHolder.bigImageView = (ImageView) view.findViewById(R.id.main_bottom_data_image);
            viewHolder.titleTv = (TextView) view.findViewById(R.id.main_bottom_data_title);
            viewHolder.priceTv = (TextView) view.findViewById(R.id.main_bottom_data_price);
            viewHolder.line = view.findViewById(R.id.main_bottom_data_line);
            view.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) view.getTag();
        }

        DisplayMetrics dm = new DisplayMetrics();
        ((Activity) context).getWindowManager().getDefaultDisplay()
                .getMetrics(dm);
        // 设置图片宽高
        int screenWidth = ((Activity) context).getWindowManager()
                .getDefaultDisplay().getWidth();
        int marginPx = Utils.dip2px(10, dm.density);
        int picSize = (screenWidth - marginPx * 3) / 2;
        RelativeLayout.LayoutParams imagePa = new RelativeLayout.LayoutParams(
                picSize, picSize);
        LinearLayout.LayoutParams linePa = new LinearLayout.LayoutParams(
                picSize, 2);
        viewHolder.bigImageView.setLayoutParams(imagePa);
        viewHolder.line.setLayoutParams(linePa);

        imageLoader.displayImage(list.get(i).getCover() + "?imageView2/1/w/500/h/500", viewHolder.bigImageView);
        viewHolder.titleTv.setText(list.get(i).getName());
        viewHolder.priceTv.setText("￥" + list.get(i).getPrice());
        return view;
    }

    public void upData(List<ShopData> bottomList) {
        this.list = bottomList;
        notifyDataSetChanged();
    }

    class ViewHolder {
        private View line;
        private ImageView bigImageView;
        private TextView titleTv, priceTv;
    }
}
