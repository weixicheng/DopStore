package com.dopstore.mall.shop.adapter;

import android.app.Activity;
import android.content.Context;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.dopstore.mall.R;
import com.dopstore.mall.activity.bean.MainTabData;
import com.dopstore.mall.view.CircleImageView;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.List;

/**
 * Created by 喜成 on 16/9/5.
 */
public class SearchAdapter extends BaseAdapter {
    private Context context;
    private List<MainTabData> list;
    private ImageLoader imageLoader;

    public SearchAdapter(Context context, List<MainTabData> list) {
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
            view = LayoutInflater.from(context).inflate(R.layout.item_search_layout, null);
            viewHolder = new ViewHolder();
            viewHolder.main = (LinearLayout) view.findViewById(R.id.item_search_main);
            viewHolder.titleTv = (TextView) view.findViewById(R.id.item_search_name);
            viewHolder.head = (CircleImageView) view.findViewById(R.id.item_search_image);
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
        int picSize = screenWidth / 3;
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(picSize, picSize);
        viewHolder.main.setLayoutParams(layoutParams);
        String title = list.get(i).getName();
        String image = list.get(i).getPicture();

        viewHolder.titleTv.setText(title);
        imageLoader.displayImage(image, viewHolder.head);

        return view;
    }

    class ViewHolder {
        private LinearLayout main;
        private CircleImageView head;
        private TextView titleTv;
    }
}
