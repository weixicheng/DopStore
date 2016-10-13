package com.dopstore.mall.activity.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.dopstore.mall.R;
import com.dopstore.mall.activity.bean.MainMiddleData;
import com.dopstore.mall.activity.bean.ShopData;
import com.dopstore.mall.shop.activity.ShopDetailActivity;
import com.dopstore.mall.util.Constant;
import com.dopstore.mall.util.LoadImageUtils;
import com.dopstore.mall.util.SkipUtils;
import com.dopstore.mall.view.EScrollView;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

        loadImageUtils.displayImage(list.get(i).getPicture(), viewHolder.bigImageView);
//        final int position=i;
//        viewHolder.bigImageView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                String id=list.get(position).getId();
//                String isCollect=list.get(position).getIs_collect();
//                Map<String,Object> map=new HashMap<String, Object>();
//                map.put(Constant.ID,id);
//                map.put(Constant.IS_COLLECT,isCollect);
//                SkipUtils.jumpForMap(context, ShopDetailActivity.class,map,false);
//            }
//        });

        List<ShopData> datas = list.get(i).getRelated_goods();
        viewHolder.eScrollView.setAdapter(new MiddleDataAdapter(context, datas));
        final  List<ShopData> newDatas=datas;
        viewHolder.eScrollView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                ShopData data = newDatas.get(i);
                String id = data.getId();
                Map<String, Object> map = new HashMap<String, Object>();
                map.put(Constant.ID, id);
                map.put(Constant.NAME, data.getName());
                map.put(Constant.PICTURE, data.getCover());
                SkipUtils.jumpForMapResult(context, ShopDetailActivity.class, map, 0);
            }
        });

        return view;
    }

    public void upData(List<MainMiddleData> midddleList) {
        this.list = midddleList;
        notifyDataSetChanged();
    }

    class ViewHolder {
        private ImageView bigImageView;
        private EScrollView eScrollView;
    }
}
