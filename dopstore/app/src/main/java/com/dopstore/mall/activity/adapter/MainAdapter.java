package com.dopstore.mall.activity.adapter;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;

import com.dopstore.mall.R;
import com.dopstore.mall.activity.WebActivity;
import com.dopstore.mall.activity.bean.MainBottomData;
import com.dopstore.mall.activity.bean.MainMiddleData;
import com.dopstore.mall.activity.bean.MainMiddleListData;
import com.dopstore.mall.util.LoadImageUtils;
import com.dopstore.mall.util.SkipUtils;
import com.dopstore.mall.view.MyGridView;
import com.dopstore.mall.view.MyListView;
import com.dopstore.mall.view.rollviewpager.OnItemClickListener;
import com.dopstore.mall.view.rollviewpager.RollPagerView;
import com.dopstore.mall.view.rollviewpager.hintview.ColorPointHintView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 作者：xicheng on 16/9/5
 */
public class MainAdapter extends BaseAdapter {
    private Context context;
    private LayoutInflater inflater;
    private static final int HEAD_CODE = 0;
    private static final int MIDDLE_CODE = 1;
    private static final int BOTTOM_CODE = 2;
    private List<Map<String, Object>> titleAdvertList = new ArrayList<Map<String, Object>>();
    private String[] titleAdvertKey = {"id", "image", "url"};
    private LoadImageUtils loadImageUtils;

    public MainAdapter(Context context) {
        this.context = context;
        loadImageUtils = LoadImageUtils.getInstance(context);
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        for (int i = 0; i < 4; i++) {
            Map<String, Object> map = new HashMap<String, Object>();
            map.put(titleAdvertKey[0], i);
            map.put(titleAdvertKey[1], "http://pic51.nipic.com/file/20141022/19779658_171157758000_2.jpg");
            map.put(titleAdvertKey[2], "");
            titleAdvertList.add(map);
        }
    }

    @Override
    public int getItemViewType(int position) {
        int p = position;
        if (p == 0) {
            return HEAD_CODE;
        } else if (p == 1) {
            return MIDDLE_CODE;
        } else if (p == 2) {
            return BOTTOM_CODE;
        } else {
            return p;
        }
    }

    @Override
    public int getViewTypeCount() {
        return 3;
    }

    @Override
    public int getCount() {
        return 3;
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        HeadViewHolder headViewHolder = null;
        MiddleViewHolder middleViewHolder = null;
        BottomViewHolder bottomViewHolder = null;
        int type = getItemViewType(position);
        if (convertView == null) {
            switch (type) {
                case HEAD_CODE: {
                    convertView = inflater.inflate(R.layout.layout_fragment_main_head_view, null);
                    headViewHolder = new HeadViewHolder();
                    headViewHolder.rollPagerView = (RollPagerView) convertView.findViewById(R.id.roll_view_pager);
                    convertView.setTag(headViewHolder);
                }
                break;
                case MIDDLE_CODE: {
                    convertView = inflater.inflate(R.layout.layout_fragment_main_middle_view, null);
                    middleViewHolder = new MiddleViewHolder();
                    middleViewHolder.myListView = (MyListView) convertView.findViewById(R.id.main_middle_listview);
                    convertView.setTag(middleViewHolder);
                }
                break;
                case BOTTOM_CODE: {
                    convertView = inflater.inflate(R.layout.layout_fragment_main_bottom_view, null);
                    bottomViewHolder = new BottomViewHolder();
                    bottomViewHolder.myGridView = (MyGridView) convertView.findViewById(R.id.main_bottom_gridView);
                    convertView.setTag(bottomViewHolder);
                }
                break;
            }
        } else {
            switch (type) {
                case HEAD_CODE: {
                    headViewHolder = (HeadViewHolder) convertView.getTag();
                }
                break;
                case MIDDLE_CODE: {
                    middleViewHolder = (MiddleViewHolder) convertView.getTag();
                }
                break;
                case BOTTOM_CODE: {
                    bottomViewHolder = (BottomViewHolder) convertView.getTag();
                }
                break;
            }
        }

        switch (type) {
            case HEAD_CODE: {
                setAdvertisementData(headViewHolder);
            }
            break;
            case MIDDLE_CODE: {
                setMiddleData(middleViewHolder);
            }
            break;
            case BOTTOM_CODE: {
                setBottomData(bottomViewHolder);
            }
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
        private MyGridView myGridView;
    }

    /**
     * 设置轮播
     */
    private void setAdvertisementData(HeadViewHolder headViewHolder) {
        DisplayMetrics dm = new DisplayMetrics();
        ((Activity) context).getWindowManager().getDefaultDisplay()
                .getMetrics(dm);
        // 设置图片宽高
        int screenWidth = ((Activity) context).getWindowManager()
                .getDefaultDisplay().getWidth();
        int picSize = screenWidth / 2;
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                screenWidth, picSize);
        headViewHolder.rollPagerView.setLayoutParams(layoutParams);

        if (titleAdvertList != null) {
            //设置播放时间间隔
            headViewHolder.rollPagerView.setPlayDelay(1000);
            //设置透明度
            headViewHolder.rollPagerView.setAnimationDurtion(500);
            //设置适配器
            headViewHolder.rollPagerView.setAdapter(new AdImageAdapter(context, titleAdvertList, titleAdvertKey));
            //设置指示器（顺序依次）
            //自定义指示器图片
            //设置圆点指示器颜色
            //设置文字指示器
            //隐藏指示器
//            rollPagerView.setHintView(new IconHintView(getActivity(), R.drawable.dot_focus, R.drawable.dot_normal));
            headViewHolder.rollPagerView.setHintView(new ColorPointHintView(context, Color.YELLOW, Color.WHITE));
            //mRollViewPager.setHintView(new TextHintView(this));
            //mRollViewPager.setHintView(null);
            if (titleAdvertList.size() == 1) {
                headViewHolder.rollPagerView.pause();
            }
        }


        headViewHolder.rollPagerView.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                Map<String, Object> map = new HashMap<String, Object>();
                map.put("", "广告");
                map.put("url", "www.baidu.com");
                SkipUtils.jumpForMap(context, WebActivity.class, map, false);
            }
        });

    }

    /**
     * 设置中间数据
     */
    private void setMiddleData(MiddleViewHolder middleViewHolder) {
        List<MainMiddleData> list = new ArrayList<MainMiddleData>();
        List<MainMiddleListData> datalist = new ArrayList<MainMiddleListData>();
        for (int i = 0; i <= 5; i++) {
            MainMiddleListData listData = new MainMiddleListData();
            listData.setId(i + "");
            listData.setImage("http://pic51.nipic.com/file/20141022/19779658_171157758000_2.jpg");
            listData.setTitle("幼儿服装幼儿班这个衣服日式风格");
            listData.setPrice("¥ 56");
            datalist.add(listData);
        }
        for (int i = 0; i < 3; i++) {
            MainMiddleData middleData = new MainMiddleData();
            middleData.setId(i + "");
            middleData.setImage("http://scimg.jb51.net/allimg/160815/103-160Q509544OC.jpg");
            middleData.setDataList(datalist);
            list.add(middleData);
        }

        middleViewHolder.myListView.setAdapter(new MiddleAdapter(context, list));
    }

    /**
     * 设置底部数据
     */
    private void setBottomData(BottomViewHolder bottomViewHolder) {
        List<MainBottomData> list = new ArrayList<MainBottomData>();
        for (int i = 0; i <= 6; i++) {
            MainBottomData listData = new MainBottomData();
            listData.setId(i + "");
            listData.setImage("http://pic51.nipic.com/file/20141022/19779658_171157758000_2.jpg");
            listData.setTitle("幼儿服装幼儿班这个衣服日式风格");
            listData.setPrice("¥ 568");
            list.add(listData);
        }
        bottomViewHolder.myGridView.setAdapter(new BottomAdapter(context, list));
    }


}