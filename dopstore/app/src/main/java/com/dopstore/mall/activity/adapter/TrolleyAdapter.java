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
import com.dopstore.mall.util.T;
import com.dopstore.mall.util.URL;
import com.dopstore.mall.util.UserUtils;
import com.dopstore.mall.util.Utils;
import com.nostra13.universalimageloader.core.ImageLoader;

import org.json.JSONException;
import org.json.JSONObject;

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
    private Double totalPrice = 0.00; // 商品总价
    private CheckBox mCheckAll; // 全选 全不选
    private CommHttp httpHelper;
    private ImageLoader imageLoader;

    public TrolleyAdapter(Context context, List<GoodBean> mListData, TextView mPriceAll, Double totalPrice, CheckBox mCheckAll) {
        this.context = context;
        this.mListData = mListData;
        this.mPriceAll = mPriceAll;
        this.totalPrice = totalPrice;
        this.mCheckAll = mCheckAll;
        httpHelper = CommHttp.getInstance();
        imageLoader = ImageLoader.getInstance();
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

    public void upData(List<GoodBean> mListData, TextView mPriceAll, Double totalPrice, CheckBox mCheckAll) {
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
            holder.intro = (TextView) view.findViewById(R.id.tv_intro_sku);
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
        holder.intro.setText(data.getGoods_sku_str());
        holder.price.setText("￥" + data.getPrice());
        holder.carNum.setText(data.getCarNum() + "");
        imageLoader.displayImage(data.getCover() + "?imageView2/1/w/96/h/96", holder.image);
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
                String totalStr = "";
                if (Utils.isDouble(totalPrice.toString())) {
                    totalStr = Utils.format(totalPrice);
                } else {
                    totalStr = totalPrice + "";
                }
                mPriceAll.setText("￥" + totalStr);
                int allCount = 0;
                for (GoodBean bean1 : mListData) {
                    if (bean1.isChoose() == true) {
                        allCount = allCount + 1;
                    }
                }
                if (allCount == mListData.size()) {
                    mCheckAll.setChecked(true);
                } else {
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
                if (selected == true) {
                    addToService(mListData, position);
                } else {
                    T.show(context, "请选中");
                }
            }
        });

        holder.red.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                GoodBean bean = mListData.get(position);
                boolean selected = bean.isChoose();
                if (selected == true) {
                    if (mListData.get(position).getCarNum() == 1)
                        return;
                    redToService(mListData, position);
                } else {
                    T.show(context, "请选中");
                }
            }
        });
        return view;
    }

    private void addToService(List<GoodBean> mListData, final int i) {
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
            }

            @Override
            public void failed(String msg) {
                T.checkNet(context);
            }
        });
    }

    private void redToService(List<GoodBean> mListData, final int i) {
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
            }

            @Override
            public void failed(String msg) {
                T.checkNet(context);
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
                    String totalStr = "";
                    if (Utils.isDouble(totalPrice.toString())) {
                        totalStr = Utils.format(totalPrice);
                    } else {
                        totalStr = totalPrice + "";
                    }
                    mPriceAll.setText("￥" + totalStr);
                    notifyDataSetChanged();
                }
                break;
                case UPDATA_ADD_CART_MSG: {
                    int position = msg.arg1;
                    mListData.get(position).setCarNum(mListData.get(position).getCarNum() + 1);
                    totalPrice += mListData.get(position).getPrice();
                    String totalStr = "";
                    if (Utils.isDouble(totalPrice.toString())) {
                        totalStr = Utils.format(totalPrice);
                    } else {
                        totalStr = totalPrice + "";
                    }
                    mPriceAll.setText("￥" + totalStr);
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
    TextView intro;
    TextView carNum;
    TextView price;
    TextView add;
    TextView red;
}
