package com.dopstore.mall.activity.adapter;

import android.content.Context;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.dopstore.mall.R;
import com.dopstore.mall.activity.bean.DataBean;

import java.util.List;

/**
 * Created by 喜成 on 16/9/7.
 * name
 */
public class TrolleyAdapter extends BaseAdapter implements AdapterView.OnItemClickListener {
    private Context context;
    private List<DataBean> mListData;// 数据
    private SparseArray<Boolean> mSelectState;
    private TextView mPriceAll; // 商品总价
    private int totalPrice = 0; // 商品总价
    private CheckBox mCheckAll; // 全选 全不选

    public TrolleyAdapter(Context context, List<DataBean> mListData, SparseArray<Boolean> mSelectState, TextView mPriceAll, int totalPrice, CheckBox mCheckAll) {
        this.context = context;
        this.mListData = mListData;
        this.mSelectState = mSelectState;
        this.mPriceAll = mPriceAll;
        this.totalPrice = totalPrice;
        this.mCheckAll = mCheckAll;
    }

    @Override
    public int getCount() {
        return mListData.size();
    }

    @Override
    public Object getItem(int position) {
        return mListData.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public void upData(List<DataBean> listData, List<DataBean> mListData, SparseArray<Boolean> mSelectState, TextView mPriceAll, int totalPrice, CheckBox mCheckAll){
        this.mListData=mListData;
        this.mListData = mListData;
        this.mSelectState = mSelectState;
        this.mPriceAll = mPriceAll;
        this.totalPrice = totalPrice;
        this.mCheckAll = mCheckAll;
        notifyDataSetChanged();
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        View view = convertView;
        if (view == null) {
            view = LayoutInflater.from(context).inflate(R.layout.cart_list_item, null);
            holder = new ViewHolder(view);
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }

        DataBean data = mListData.get(position);
        bindListItem(holder, data);
        holder.add.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub

                int _id = (int) mListData.get(position).getId();

                boolean selected = mSelectState.get(_id, false);

                mListData.get(position).setCarNum(mListData.get(position).getCarNum() + 1);

                notifyDataSetChanged();

                if (selected) {
                    totalPrice += mListData.get(position).getPrice();
                    mPriceAll.setText("￥" + totalPrice + "");

                }

            }
        });

        holder.red.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                // TODO Auto-generated method stub
                if (mListData.get(position).getCarNum() == 1)
                    return;

                int _id = (int) mListData.get(position).getId();

                boolean selected = mSelectState.get(_id, false);
                mListData.get(position).setCarNum(mListData.get(position).getCarNum() - 1);
                notifyDataSetChanged();

                if (selected) {
                    totalPrice -= mListData.get(position).getPrice();
                    mPriceAll.setText("￥" + totalPrice + "");

                }

            }
        });
        return view;
    }

    private void bindListItem(ViewHolder holder, DataBean data) {

        holder.content.setText(data.getContent());
        holder.price.setText("￥" + data.getPrice());
        holder.carNum.setText(data.getCarNum() + "");
        int _id = data.getId();

        boolean selected = mSelectState.get(_id, false);
        holder.checkBox.setChecked(selected);

    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        DataBean bean = mListData.get(position);

        ViewHolder holder = (ViewHolder) view.getTag();
        int _id = (int) bean.getId();

        boolean selected = !mSelectState.get(_id, false);
        holder.checkBox.toggle();
        if (selected) {
            mSelectState.put(_id, true);
            totalPrice += bean.getCarNum() * bean.getPrice();
        } else {
            mSelectState.delete(_id);
            totalPrice -= bean.getCarNum() * bean.getPrice();
        }
        mPriceAll.setText("￥" + totalPrice + "");
        if (mSelectState.size() == mListData.size()) {
            mCheckAll.setChecked(true);
        } else {
            mCheckAll.setChecked(false);
        }
    }
}

class ViewHolder {
    CheckBox checkBox;
    ImageView image;
    TextView content;
    TextView carNum;
    TextView price;
    TextView add;
    TextView red;

    public ViewHolder(View view) {
        checkBox = (CheckBox) view.findViewById(R.id.check_box);
        image = (ImageView) view.findViewById(R.id.iv_adapter_list_pic);
        content = (TextView) view.findViewById(R.id.tv_intro);
        carNum = (TextView) view.findViewById(R.id.tv_num);
        price = (TextView) view.findViewById(R.id.tv_price);
        add = (TextView) view.findViewById(R.id.tv_add);
        red = (TextView) view.findViewById(R.id.tv_reduce);

    }
}
