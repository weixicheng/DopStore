package com.dopstore.mall.activity.adapter;

import android.app.Activity;
import android.content.Context;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.dopstore.mall.R;
import com.dopstore.mall.activity.WebActivity;
import com.dopstore.mall.activity.bean.CarouselData;
import com.dopstore.mall.activity.bean.MainMiddleData;
import com.dopstore.mall.activity.bean.ShopData;
import com.dopstore.mall.shop.activity.ShopDetailActivity;
import com.dopstore.mall.util.Constant;
import com.dopstore.mall.util.SkipUtils;
import com.dopstore.mall.view.MyGridView;
import com.dopstore.mall.view.MyListView;
import com.dopstore.mall.view.rollviewpager.OnItemClickListener;
import com.dopstore.mall.view.rollviewpager.RollPagerView;
import com.dopstore.mall.view.rollviewpager.hintview.IconHintView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by 喜成 on 16/9/5.
 * name 活动Adapter
 */
public class MainShopAdapter extends BaseAdapter {
    private Context context;
    private List<String> list;
    private List<CarouselData> titleAdvertList;
    private List<MainMiddleData> midddleList;
    private List<ShopData> bottomList;
    private LayoutInflater inflater;
    final int VIEW_TYPE = 3;
    final int TYPE_1 = 0;
    final int TYPE_2 = 1;
    final int TYPE_3 = 2;

    public MainShopAdapter(Context context, List<CarouselData> titleAdvertList, List<MainMiddleData> midddleList, List<ShopData> bottomList) {
        this.context = context;
        list = new ArrayList<String>();
        for (int i = 0; i < 3; i++) {
            list.add(i + "");
        }
        this.titleAdvertList = titleAdvertList;
        this.midddleList = midddleList;
        this.bottomList = bottomList;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0)
            return TYPE_1;
        else if (position ==1)
            return TYPE_2;
        else if (position ==2)
            return TYPE_3;
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
        HeadViewHolder holder1 = null;
        MiddleViewHolder holder2 = null;
        BottomViewHolder holder3 = null;
        int type = getItemViewType(position);
        //无convertView，需要new出各个控件
        if (convertView == null) {//按当前所需的样式，确定new的布局
            switch (type) {
                case TYPE_1:
                    convertView = inflater.inflate(R.layout.layout_main_shop_head_fragment, parent, false);
                    holder1 = new HeadViewHolder();
                    holder1.rollPagerView = (RollPagerView) convertView.findViewById(R.id.roll_view_pager);
                    convertView.setTag(holder1);
                    break;
                case TYPE_2:
                    convertView = inflater.inflate(R.layout.layout_main_shop_middle_fragment, parent, false);
                    holder2 = new MiddleViewHolder();
                    holder2.myListView = (MyListView) convertView.findViewById(R.id.main_shop_fragment_middle_listview);
                    convertView.setTag(holder2);
                    break;
                case TYPE_3:
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
                    holder1 = (HeadViewHolder) convertView.getTag();
                    break;
                case TYPE_2:
                    holder2 = (MiddleViewHolder) convertView.getTag();
                    break;
                case TYPE_3:
                    holder3 = (BottomViewHolder) convertView.getTag();
                    break;
            }
        }

        //设置资源
        switch (type) {
            case TYPE_1:
                setHeadData(holder1);
                break;
            case TYPE_2:
                setMiddleData(holder2);
                break;
            case TYPE_3:
                setBottomData(holder3);
                break;
        }

        return convertView;
    }

    class HeadViewHolder {
        private RollPagerView rollPagerView;
    }

    class MiddleViewHolder {
        private MyListView myListView;
    }

    class BottomViewHolder {
        private TextView hotText;
        private MyGridView myGridView;
    }

    private void setHeadData(HeadViewHolder holder1) {
        DisplayMetrics dm = new DisplayMetrics();
        ((Activity) context).getWindowManager().getDefaultDisplay()
                .getMetrics(dm);
        // 设置图片宽高
        int screenWidth = ((Activity) context).getWindowManager()
                .getDefaultDisplay().getWidth();
        final int picSize = screenWidth / 2;
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                screenWidth, picSize);
        holder1.rollPagerView.setLayoutParams(layoutParams);

        if (titleAdvertList != null) {
            //设置播放时间间隔
            holder1.rollPagerView.setPlayDelay(1000);
            //设置透明度
            holder1.rollPagerView.setAnimationDurtion(500);
            //设置适配器
            holder1.rollPagerView.setAdapter(new HomeAdImageAdapter(context, titleAdvertList));
            holder1.rollPagerView.setHintView(new IconHintView(context, R.mipmap.dop_press, R.mipmap.dop_normal));
            if (titleAdvertList.size() == 1) {
                holder1.rollPagerView.pause();
                holder1.rollPagerView.setHintView(null);
            }
        }

        holder1.rollPagerView.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                CarouselData data = titleAdvertList.get(position);
                String urlStr = data.getUrl();
//                if (!TextUtils.isEmpty(urlStr)) {
//                    Map<String, Object> map = new HashMap<String, Object>();
//                    map.put("title", titleAdvertList.get(position).getTitle());
//                    map.put("url", titleAdvertList.get(position).getUrl());
//                    SkipUtils.jumpForMap(context, WebActivity.class, map, false);
//                }else {
                    Map<String, Object> map = new HashMap<String, Object>();
                    map.put(Constant.ID, titleAdvertList.get(position).getId());
                    SkipUtils.jumpForMapResult(context, ShopDetailActivity.class, map, 0);
//                }
            }
        });
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
                SkipUtils.jumpForMapResult(context, ShopDetailActivity.class, map, 0);
            }
        });
    }


}
