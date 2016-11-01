package com.dopstore.mall.activity.adapter;

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

import java.util.List;

/**
 * Created by 喜成 on 16/9/5.
 * name 首页顶部
 */
public class TabAdapter extends BaseAdapter {
    private Context context;
    private List<MainTabData> list;

    public TabAdapter(Context context, List<MainTabData> list) {
        this.context = context;
        this.list = list;
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
            view = LayoutInflater.from(context).inflate(R.layout.item_main_top_data_layout, null);
            viewHolder = new ViewHolder();
            viewHolder.topLayout = (LinearLayout) view.findViewById(R.id.main_top_data_ly);
            viewHolder.titleTv = (TextView) view.findViewById(R.id.main_top_data_title);
            viewHolder.line = view.findViewById(R.id.main_top_data_line);
            view.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) view.getTag();
        }
        String title = list.get(i).getName();
        String isSelect = list.get(i).getIsSelect();
        viewHolder.titleTv.setText(title);
        DisplayMetrics dm = new DisplayMetrics();
        ((Activity) context).getWindowManager().getDefaultDisplay().getMetrics(dm);
        int screenWidth = ((Activity) context).getWindowManager().getDefaultDisplay().getWidth();
        if (list.size() <= 5) {
            int picSize = screenWidth / list.size();
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(picSize, LinearLayout.LayoutParams.WRAP_CONTENT);
            viewHolder.topLayout.setLayoutParams(layoutParams);
        }
        if ("1".equals(isSelect)) {
            viewHolder.titleTv.setTextColor(context.getResources().getColor(R.color.red_color_f93448));
            viewHolder.line.setBackgroundColor(context.getResources().getColor(R.color.red_color_f93448));
        } else {
            viewHolder.titleTv.setTextColor(context.getResources().getColor(R.color.gray_color_33));
            viewHolder.line.setBackgroundColor(context.getResources().getColor(R.color.white_color));
        }
        return view;
    }

    class ViewHolder {
        private LinearLayout topLayout;
        private View line;
        private TextView titleTv;
    }
}
