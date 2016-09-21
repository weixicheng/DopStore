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

import com.dopstore.mall.R;
import com.dopstore.mall.activity.bean.MainMiddleData;
import com.dopstore.mall.activity.bean.MainMiddleListData;
import com.dopstore.mall.util.Constant;
import com.dopstore.mall.util.LoadImageUtils;
import com.dopstore.mall.util.Utils;
import com.dopstore.mall.view.EScrollView;

import java.util.List;

/**
 * Created by 喜成 on 16/9/5.
 * name
 */
public class MiddleAdapter extends BaseAdapter {
    private Context context;
    private List<MainMiddleData> list;
    private LoadImageUtils loadImageUtils;

    public MiddleAdapter(Context context, List<MainMiddleData> list) {
        this.context = context;
        this.list = list;
        loadImageUtils = LoadImageUtils.getInstance(context);
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
        ViewHolder viewHolder = null;
        if (view == null) {
            view = LayoutInflater.from(context).inflate(R.layout.item_main_middle_layout, null);
            viewHolder = new ViewHolder();
            viewHolder.bigImageView = (ImageView) view.findViewById(R.id.main_middle_bigimage);
            viewHolder.eScrollView = (EScrollView) view.findViewById(R.id.main_middle_escrollview);
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
        int picSize = (screenWidth - marginPx * 2) / 2;
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                screenWidth, picSize);
        viewHolder.bigImageView.setLayoutParams(layoutParams);
        viewHolder.bigImageView.setPadding(marginPx, 0, marginPx, 0);

        loadImageUtils.displayImage(list.get(i).getPicture(), viewHolder.bigImageView, Constant.OPTIONS_SPECIAL_CODE);

        List<MainMiddleListData> datas=list.get(i).getRelated_goods();
            viewHolder.eScrollView.setAdapter(new MiddleDataAdapter(context, datas));

        return view;
    }

    class ViewHolder {
        private ImageView bigImageView;
        private EScrollView eScrollView;
    }
}
