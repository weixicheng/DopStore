package com.dopstore.mall.login.Adapter;

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
import com.dopstore.mall.login.bean.DetailData;
import com.dopstore.mall.util.Utils;
import com.dopstore.mall.view.CircleImageView;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.List;

/**
 * Created by 喜成 on 16/9/5.
 * name 首页底部数据
 */
public class BabyAdapter extends BaseAdapter {
    private Context context;
    private List<DetailData> list;
    private ImageLoader imageLoader;

    public BabyAdapter(Context context, List<DetailData> list) {
        this.context = context;
        this.list = list;
        imageLoader=ImageLoader.getInstance();
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

    public void upDataList(List<DetailData> list) {
        this.list = list;
        notifyDataSetChanged();
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ViewHolder viewHolder;
        if (view == null) {
            view = LayoutInflater.from(context).inflate(R.layout.item_register_detail, null);
            viewHolder = new ViewHolder();
            viewHolder.bigImageView = (CircleImageView) view.findViewById(R.id.item_register_detail_image);
            viewHolder.titleTv = (TextView) view.findViewById(R.id.item_register_detail_name);
            view.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) view.getTag();
        }

        DisplayMetrics dm = new DisplayMetrics();
        ((Activity) context).getWindowManager().getDefaultDisplay().getMetrics(dm);
        // 设置图片宽高
        int screenWidth = ((Activity) context).getWindowManager().getDefaultDisplay().getWidth();
        int marginPx = Utils.dip2px(15, dm.density);
        int picSize = (screenWidth - marginPx * 4) / 3;
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(picSize, picSize);
        viewHolder.bigImageView.setLayoutParams(layoutParams);

        imageLoader.displayImage(list.get(i).getImage(), viewHolder.bigImageView);
        viewHolder.titleTv.setText(list.get(i).getName());
        String isSelect = list.get(i).getIsSelect();
        if ("1".equals(isSelect)) {
            viewHolder.titleTv.setTextColor(context.getResources().getColor(R.color.red_color_f93448));
        } else {
            viewHolder.titleTv.setTextColor(context.getResources().getColor(R.color.gray_color_33));
        }
        return view;
    }


    class ViewHolder {
        private CircleImageView bigImageView;
        private TextView titleTv;
    }
}
