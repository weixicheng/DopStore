package com.dopstore.mall.activity.adapter;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.dopstore.mall.R;
import com.dopstore.mall.activity.bean.GoodBean;
import com.dopstore.mall.util.CommHttp;
import com.dopstore.mall.util.Constant;
import com.dopstore.mall.util.LoadImageUtils;
import com.dopstore.mall.util.ProUtils;
import com.dopstore.mall.util.T;
import com.dopstore.mall.util.URL;
import com.dopstore.mall.util.UserUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * Created by 喜成 on 16/9/7.
 * name
 */
public class TrolleyAdapter extends BaseAdapter {
    private Context context;
    private List<GoodBean> mListData;// 数据
    private TextView mPriceAll; // 商品总价
    private int totalPrice = 0; // 商品总价
    private CheckBox mCheckAll; // 全选 全不选
    private CommHttp httpHelper;
    private ProUtils proUtils;
    private LoadImageUtils loadImageUtils;

    public TrolleyAdapter(Context context, List<GoodBean> mListData, TextView mPriceAll, int totalPrice, CheckBox mCheckAll) {
        this.context = context;
        this.mListData = mListData;
        this.mPriceAll = mPriceAll;
        this.totalPrice = totalPrice;
        this.mCheckAll = mCheckAll;
        httpHelper = CommHttp.getInstance(context);
        proUtils = new ProUtils(context);
        loadImageUtils = LoadImageUtils.getInstance(context);
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

    public void upData(List<GoodBean> mListData, TextView mPriceAll, int totalPrice, CheckBox mCheckAll) {
        this.mListData = mListData;
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
            holder = new ViewHolder();
            holder.checkBox = (CheckBox) view.findViewById(R.id.check_box);
            holder.image = (ImageView) view.findViewById(R.id.iv_adapter_list_pic);
            holder.content = (TextView) view.findViewById(R.id.tv_intro);
            holder.carNum = (TextView) view.findViewById(R.id.tv_num);
            holder.price = (TextView) view.findViewById(R.id.tv_price);
            holder.add = (TextView) view.findViewById(R.id.tv_add);
            holder.red = (TextView) view.findViewById(R.id.tv_reduce);
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }

        GoodBean data = mListData.get(position);
        holder.content.setText(data.getContent());
        holder.price.setText("￥" + data.getPrice());
        holder.carNum.setText(data.getCarNum() + "");
        loadImageUtils.displayImage(data.getCover(), holder.image);
        boolean selected = data.isChoose();
        holder.checkBox.setChecked(selected);
        holder.checkBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                GoodBean bean = mListData.get(position);
                boolean selected = bean.isChoose();
                if (selected) {
                    mListData.get(position).setChoose(false);
                    totalPrice -= bean.getCarNum() * bean.getPrice();
                } else {
                    mListData.get(position).setChoose(true);
                    totalPrice += bean.getCarNum() * bean.getPrice();
                }
                mPriceAll.setText("￥" + totalPrice + "");
                int allCount=0;
                for (GoodBean bean1 : mListData) {
                    if (bean1.isChoose() == true) {
                        allCount=allCount+1;
                    }
                }
                if (allCount==mListData.size()){
                    mCheckAll.setChecked(true);
                }else {
                    mCheckAll.setChecked(false);
                }
                notifyDataSetChanged();
            }
        });

        holder.add.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                GoodBean bean = mListData.get(position);
                boolean selected = bean.isChoose();
                if (selected==true) {
                    addToService(mListData, position);
                }else {
                    T.show(context,"请选中");
                }
            }
        });

        holder.red.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                GoodBean bean = mListData.get(position);
                boolean selected = bean.isChoose();
                if (selected==true) {
                    if (mListData.get(position).getCarNum() == 1)
                        return;
                    redToService(mListData, position);
                }else {
                    T.show(context,"请选中");
                }
            }
        });
        return view;
    }

    private void addToService(List<GoodBean> mListData, final int i) {
        proUtils.show();
        final Map<String, Object> map = new HashMap<String, Object>();
        map.put("user_id", UserUtils.getId(context));
        map.put("item_id", (mListData.get(i).getId()) + "");
        map.put("count", (mListData.get(i).getCarNum() + 1) + "");
        map.put("edit", "1");
        httpHelper.post(context, URL.CART_EDIT, map, new CommHttp.HttpCallBack() {
            @Override
            public void success(String body) {
                try {
                    JSONObject jo = new JSONObject(body);
                    String code = jo.optString(Constant.ERROR_CODE);
                    if ("0".equals(code)) {
                        Message msg = new Message();
                        msg.what = UPDATA_ADD_CART_MSG;
                        msg.arg1 = i;
                        handler.sendMessage(msg);
                    } else {
                        String msg = jo.optString(Constant.ERROR_MSG);
                        T.show(context, msg);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                proUtils.dismiss();
            }

            @Override
            public void failed(String msg) {
                T.checkNet(context);
                proUtils.dismiss();
            }
        });
    }

    private void redToService(List<GoodBean> mListData, final int i) {
        proUtils.show();
        final Map<String, Object> map = new HashMap<String, Object>();
        map.put("user_id", UserUtils.getId(context));
        map.put("item_id", (mListData.get(i).getId()) + "");
        map.put("count", (mListData.get(i).getCarNum() - 1) + "");
        map.put("edit", "1");
        httpHelper.post(context, URL.CART_EDIT, map, new CommHttp.HttpCallBack() {
            @Override
            public void success(String body) {
                try {
                    JSONObject jo = new JSONObject(body);
                    String code = jo.optString(Constant.ERROR_CODE);
                    if ("0".equals(code)) {
                        Message msg = new Message();
                        msg.what = UPDATA_RED_CART_MSG;
                        msg.arg1 = i;
                        handler.sendMessage(msg);
                    } else {
                        String msg = jo.optString(Constant.ERROR_MSG);
                        T.show(context, msg);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                proUtils.dismiss();
            }

            @Override
            public void failed(String msg) {
                T.checkNet(context);
                proUtils.dismiss();
            }
        });
    }


    private final static int UPDATA_RED_CART_MSG = 0;
    private final static int UPDATA_ADD_CART_MSG = 1;

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case UPDATA_RED_CART_MSG: {
                    int position = msg.arg1;
                    mListData.get(position).setCarNum(mListData.get(position).getCarNum() - 1);
                    totalPrice -= mListData.get(position).getPrice();
                    mPriceAll.setText("￥" + totalPrice + "");
                    notifyDataSetChanged();
                }
                break;
                case UPDATA_ADD_CART_MSG: {
                    int position = msg.arg1;
                    mListData.get(position).setCarNum(mListData.get(position).getCarNum() + 1);
                    totalPrice += mListData.get(position).getPrice();
                    mPriceAll.setText("￥" + totalPrice + "");
                    notifyDataSetChanged();
                }
                break;
            }
        }
    };

}

class ViewHolder {
    CheckBox checkBox;
    ImageView image;
    TextView content;
    TextView carNum;
    TextView price;
    TextView add;
    TextView red;
}
