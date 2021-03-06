package com.dopstore.mall.order.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.dopstore.mall.R;
import com.dopstore.mall.order.bean.OrderData;
import com.dopstore.mall.order.bean.OrderDataData;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.List;


public class MyOrderAdapter extends BaseAdapter {

    private List<OrderData> items;
    private LayoutInflater mInflater;
    private ImageLoader imageLoader;

    public MyOrderAdapter(Context context, List<OrderData> items) {
        super();
        this.items = items;
        mInflater = LayoutInflater.from(context);
        imageLoader = ImageLoader.getInstance();
    }

    @Override
    public int getCount() {
        return items.size();
    }

    @Override
    public Object getItem(int position) {
        return items.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = mInflater.inflate(R.layout.item_my_order, null);
            holder.id = (TextView) convertView.findViewById(R.id.item_my_order_id);
            holder.state = (TextView) convertView.findViewById(R.id.item_my_order_state);
            holder.title = (TextView) convertView.findViewById(R.id.item_my_order_title);
            holder.type = (TextView) convertView.findViewById(R.id.item_my_order_type);
            holder.num = (TextView) convertView.findViewById(R.id.item_my_order_num);
            holder.price = (TextView) convertView.findViewById(R.id.item_my_order_price);
            holder.imageView = (ImageView) convertView.findViewById(R.id.item_my_order_image);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        if (items != null && items.size() > 0) {
            final OrderData data = items.get(position);
            if (data != null) {
                holder.id.setText("订单号:" + data.getOrder_num());
                List<OrderDataData> goods = data.getGoods_relateds();
                if (goods != null && goods.size() > 0) {
                    holder.num.setText("x " + goods.get(0).getGoods_num());
                    holder.type.setText(goods.get(0).getGoods_sku_str());
                    holder.title.setText(goods.get(0).getGoods_name());
                    holder.price.setText("￥ " + goods.get(0).getGoods_price());
                    imageLoader.displayImage(goods.get(0).getGoods_cover() + "?imageView2/1/w/182/h/182", holder.imageView);
                } else {
                    holder.num.setText("x 0");
                    holder.type.setText("");
                    holder.title.setText("");
                    holder.price.setText("￥ 0.0");
                    imageLoader.displayImage("", holder.imageView);
                }
                String status = data.getStatus();
                String statusStr = getType(status);
                holder.state.setText(statusStr);

            }
        }
        return convertView;
    }

    private String getType(String status) {
        String nameStr = "";
        if ("0".equals(status)) {
            nameStr = "待下单";
        } else if ("1".equals(status)) {
            nameStr = "付款成功";
        } else if ("2".equals(status)) {
            nameStr = "订单取消";
        } else if ("3".equals(status)) {
            nameStr = "待发货";
        } else if ("4".equals(status)) {
            nameStr = "配送中";
        } else if ("5".equals(status)) {
            nameStr = "已完成";
        } else if ("6".equals(status)) {
            nameStr = "退款申请中";
        } else if ("7".equals(status)) {
            nameStr = "退款中";
        } else if ("8".equals(status)) {
            nameStr = "退款成功";
        } else if ("9".equals(status)) {
            nameStr = "等待付款";
        }
        return nameStr;
    }

    public void UpData(List<OrderData> items) {
        this.items = items;
        notifyDataSetChanged();
    }

    static class ViewHolder {
        private TextView id, state, title, type, num, price;
        private ImageView imageView;
    }

}
