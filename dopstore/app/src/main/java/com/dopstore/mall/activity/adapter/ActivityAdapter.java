package com.dopstore.mall.activity.adapter;

import android.app.Activity;
import android.content.Context;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.dopstore.mall.R;
import com.dopstore.mall.activity.bean.ActivityData;
import com.dopstore.mall.util.LoadImageUtils;
import com.dopstore.mall.util.Utils;

import java.util.List;

/**
 * Created by 喜成 on 16/9/5.
 * name 活动Adapter
 */
public class ActivityAdapter extends BaseAdapter {
    private Context context;
    private List<ActivityData> list;
    private LoadImageUtils loadImageUtils;
    private int isShow;

    public ActivityAdapter(Context context, List<ActivityData> list,int isShow) {
        this.context = context;
        this.list = list;
        this.isShow = isShow;
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
        ViewHolder viewHolder;
        if (view == null) {
            view = LayoutInflater.from(context).inflate(R.layout.item_fragment_activity, null);
            viewHolder = new ViewHolder();
            viewHolder.bigImageView = (ImageView) view.findViewById(R.id.item_fragment_activity_image);
            viewHolder.titleTv = (TextView) view.findViewById(R.id.item_fragment_activity_title);
            viewHolder.distanceTv = (TextView) view.findViewById(R.id.item_fragment_activity_distance);
            viewHolder.priceTv = (TextView) view.findViewById(R.id.item_fragment_activity_price);
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
        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.MATCH_PARENT, picSize);
        viewHolder.bigImageView.setLayoutParams(layoutParams);

        loadImageUtils.displayImage(list.get(i).getPicture(), viewHolder.bigImageView);
        viewHolder.titleTv.setText(list.get(i).getName());
        viewHolder.priceTv.setText("￥"+list.get(i).getPrice());
        if (isShow==0){
            viewHolder.distanceTv.setVisibility(View.GONE);
        }else {
            viewHolder.distanceTv.setVisibility(View.VISIBLE);
            viewHolder.distanceTv.setText(list.get(i).getId());//
        }

        return view;
    }

    public void upData(List<ActivityData> aList, int i) {
        this.list=aList;
        this.isShow=i;
        notifyDataSetChanged();
    }

    class ViewHolder {
        private ImageView bigImageView;
        private TextView titleTv, priceTv, distanceTv;
    }
}
