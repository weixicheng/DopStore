package com.dopstore.mall.person.activity;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.dopstore.mall.R;
import com.dopstore.mall.base.BaseActivity;
import com.dopstore.mall.person.bean.MyAddressData;
import com.dopstore.mall.util.CommHttp;
import com.dopstore.mall.util.Constant;
import com.dopstore.mall.util.PopupUtils;
import com.dopstore.mall.util.SkipUtils;
import com.dopstore.mall.util.T;
import com.dopstore.mall.util.URL;
import com.dopstore.mall.util.UserUtils;
import com.dopstore.mall.util.Utils;
import com.dopstore.mall.view.addresspicker.wheel.widget.OnWheelChangedListener;
import com.dopstore.mall.view.addresspicker.wheel.widget.WheelView;
import com.dopstore.mall.view.addresspicker.wheel.widget.adapters.ArrayWheelAdapter;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;



/**
 * 新建地址
 */
public class NewAddressActivity extends BaseActivity {
    private LinearLayout mProvince_onclick, mKeep_addrees;
    private TextView mProvince_city_area, hintV;
    private ImageView mLv_click;
    private boolean clcik_address = false;
    private EditText mAddress, Address_name, Address_phone, card_num;
    private Button saveBt, deleBt;
    private PopupWindow popupWindow;
    private LinearLayout bgLayout;
    private WheelView mViewProvince;
    private WheelView mViewCity;
    private WheelView mViewDistrict;
    private TextView mBtnConfirm;
    private TextView mBtnCancle;
    private String is_default = "0";
    private String address_id = "";
    private CommHttp httpHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_address);
        initView();
    }

    private void initView() {
        httpHelper=CommHttp.getInstance();
        setCustomTitle("新增收货地址", getResources().getColor(R.color.white_color));
        leftImageBack(R.mipmap.back_arrow);

        mProvince_onclick = (LinearLayout) findViewById(R.id.new_address_city);
        mKeep_addrees = (LinearLayout) findViewById(R.id.new_address_default);
        mProvince_city_area = (TextView) findViewById(R.id.new_address_city_tv);
        bgLayout = (LinearLayout) findViewById(R.id.new_address_poup_bg);
        hintV = (TextView) findViewById(R.id.new_address_hint);
        mAddress = (EditText) findViewById(R.id.new_address_detail);
        card_num = (EditText) findViewById(R.id.new_address_card);
        Address_name = (EditText) findViewById(R.id.new_address_name);
        Address_phone = (EditText) findViewById(R.id.new_address_phone);
        mLv_click = (ImageView) findViewById(R.id.new_address_default_click);
        saveBt = (Button) findViewById(R.id.new_address_save);
        deleBt = (Button) findViewById(R.id.new_address_delete);
        mProvince_onclick.setOnClickListener(listener);
        mKeep_addrees.setOnClickListener(listener);
        saveBt.setOnClickListener(listener);
        deleBt.setOnClickListener(listener);
        getIntentData();
    }

    private void getIntentData() {
        Map<String, Object> map = SkipUtils.getMap(this);
        if (map != null) {
            MyAddressData data = (MyAddressData) map.get(Constant.LIST);
            address_id = data.getId();
            Address_phone.setText(data.getMobile());
            Address_name.setText(data.getShipping_user());
            mAddress.setText(data.getAddress());
            mCurrentProviceName = data.getProvince();
            mCurrentCityName = data.getCity();
            mCurrentDistrictName = data.getArea();
            mProvince_city_area.setTextColor(getResources().getColor(R.color.gray_color_33));
            mProvince_city_area.setText(mCurrentProviceName + mCurrentCityName + mCurrentDistrictName);
            card_num.setText(data.getId_card());
            is_default = data.is_default;
            if ("0".equals(is_default)) {
                clcik_address = false;
                mLv_click.setImageResource(R.mipmap.checkbox_normal);
            } else {
                clcik_address = true;
                mLv_click.setImageResource(R.mipmap.checkbox_checked);
            }
            deleBt.setVisibility(View.VISIBLE);
            hintV.setVisibility(View.GONE);
        } else {
            address_id = "";
            deleBt.setVisibility(View.GONE);
            hintV.setVisibility(View.VISIBLE);
        }
    }

    OnClickListener listener = new OnClickListener() {
        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.new_address_city: {
                    Utils.hideKeyboard(NewAddressActivity.this);
                    showPop();
                }
                break;
                case R.id.new_address_default: {
                    if (clcik_address) {
                        mLv_click.setImageResource(R.mipmap.checkbox_normal);
                        clcik_address = false;
                        is_default = "0";
                    } else {
                        mLv_click.setImageResource(R.mipmap.checkbox_checked);
                        clcik_address = true;
                        is_default = "1";
                    }
                }
                break;
                case R.id.popup_cancel: {
                    dismissPoup();
                }
                break;
                case R.id.popup_confirm: {
                    mProvince_city_area.setTextColor(getResources().getColor(R.color.gray_color_33));
                    mProvince_city_area.setText(mCurrentProviceName + mCurrentCityName + mCurrentDistrictName);
                    dismissPoup();
                }
                break;
                case R.id.new_address_save: {
                    saveMsg();
                }
                break;
                case R.id.new_address_delete: {
                    deleteMsg();
                }
                break;
            }

        }
    };

    private void deleteMsg() {
        httpHelper.get(this, URL.SHIPPINGADDRESS + "shippingaddress/" + address_id + "/delete", new CommHttp.HttpCallBack() {
            @Override
            public void success(String body) {
                try {
                    JSONObject jo = new JSONObject(body);
                    String code = jo.optString(Constant.ERROR_CODE);
                    if ("0".equals(code)) {
                        T.show(NewAddressActivity.this, "成功");
                        handler.sendEmptyMessageDelayed(0, 300);
                    } else {
                        String msg = jo.optString(Constant.ERROR_MSG);
                        T.show(NewAddressActivity.this, msg);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void failed(String msg) {
                T.checkNet(NewAddressActivity.this);
            }
        });
    }

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            SkipUtils.back(NewAddressActivity.this);
        }
    };

    private void saveMsg() {
        String name = Address_name.getText().toString();
        if (TextUtils.isEmpty(name)) {
            T.show(this, "请输入姓名");
            return;
        }
        String phone = Address_phone.getText().toString();
        if (TextUtils.isEmpty(name)) {
            T.show(this, "请输入手机号");
            return;
        }
        if (!Utils.isPhoneNumberValid(phone)) {
            T.show(this, "请检查手机号");
            return;
        }
        String cardNum = card_num.getText().toString();
        if (TextUtils.isEmpty(name)) {
            T.show(this, "请输入身份证号");
            return;
        }
        String addressStr = mAddress.getText().toString();
        if (TextUtils.isEmpty(name)) {
            T.show(this, "请输入详细地址");
            return;
        }
        String id = UserUtils.getId(this);
        Map<String, Object> map = new HashMap<String, Object>();
        map.put(Constant.ID, address_id);
        map.put(Constant.SHIPPING_USER, name);
        map.put(Constant.MOBILE, phone);
        map.put(Constant.PROVINCE, mCurrentProviceName);
        map.put(Constant.CITY, mCurrentCityName);
        map.put(Constant.AREA, mCurrentDistrictName);
        map.put(Constant.ID_CARD, cardNum);
        map.put(Constant.ADDRESS, addressStr);
        map.put(Constant.IS_DEFAULT, is_default);
        httpHelper.post(this, URL.SHIPPINGADDRESS + id + "/update_shippingaddress", map, new CommHttp.HttpCallBack() {
            @Override
            public void success(String body) {
                try {
                    JSONObject jo = new JSONObject(body);
                    String code = jo.optString(Constant.ERROR_CODE);
                    if ("0".equals(code)) {
                        T.show(NewAddressActivity.this, "成功");
                        handler.sendEmptyMessageDelayed(0, 300);
                    } else {
                        String msg = jo.optString(Constant.ERROR_MSG);
                        T.show(NewAddressActivity.this, msg);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void failed(String msg) {
                T.checkNet(NewAddressActivity.this);
            }
        });
    }

    private void dismissPoup() {
        if (popupWindow.isShowing()) {
            popupWindow.dismiss();
        }
    }

    private void showPop() {
        bgLayout.setVisibility(View.VISIBLE);
        int screenWidth = getWindowManager().getDefaultDisplay().getWidth();
        View v = LayoutInflater.from(this).inflate(R.layout.poup_city_list, null);
        setUpViews(v);
        setUpData();
        popupWindow = PopupUtils.ShowBottomPopupWindow(this, popupWindow, v, screenWidth, 200, findViewById(R.id.new_address_main_relalayout));
        popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                bgLayout.setVisibility(View.GONE);
                popupWindow = null;
            }
        });
    }

    private void setUpViews(View v) {
        mViewProvince = (WheelView) v.findViewById(R.id.id_province);
        mViewCity = (WheelView) v.findViewById(R.id.id_city);
        mViewDistrict = (WheelView) v.findViewById(R.id.id_district);
        mBtnCancle = (TextView) v.findViewById(R.id.popup_cancel);
        mBtnConfirm = (TextView) v.findViewById(R.id.popup_confirm);
        // 添加change事件
        mViewProvince.addChangingListener(onWheelChangedListener);
        // 添加change事件
        mViewCity.addChangingListener(onWheelChangedListener);
        // 添加change事件
        mViewDistrict.addChangingListener(onWheelChangedListener);
        // 添加onclick事件
        mBtnCancle.setOnClickListener(listener);
        mBtnConfirm.setOnClickListener(listener);
    }

    OnWheelChangedListener onWheelChangedListener = new OnWheelChangedListener() {
        @Override
        public void onChanged(WheelView wheel, int oldValue, int newValue) {
            if (wheel == mViewProvince) {
                updateCities();
                mCurrentDistrictName = mDistrictDatasMap.get(mCurrentCityName)[0];
            } else if (wheel == mViewCity) {
                updateAreas();
                mCurrentDistrictName = mDistrictDatasMap.get(mCurrentCityName)[0];
            } else if (wheel == mViewDistrict) {
                mCurrentDistrictName = mDistrictDatasMap.get(mCurrentCityName)[newValue];
                mCurrentZipCode = mZipcodeDatasMap.get(mCurrentDistrictName);
            }
        }
    };

    private void setUpData() {
        initProvinceDatas();
        mViewProvince.setViewAdapter(new ArrayWheelAdapter<String>(this, mProvinceDatas));
        // 设置可见条目数量
        mViewProvince.setVisibleItems(7);
        mViewCity.setVisibleItems(7);
        mViewDistrict.setVisibleItems(7);
        updateCities();
        updateAreas();
    }

    /**
     * 根据当前的市，更新区WheelView的信息
     */
    private void updateAreas() {
        int pCurrent = mViewCity.getCurrentItem();
        mCurrentCityName = mCitisDatasMap.get(mCurrentProviceName)[pCurrent];
        String[] areas = mDistrictDatasMap.get(mCurrentCityName);

        if (areas == null) {
            areas = new String[]{""};
        }
        mViewDistrict.setViewAdapter(new ArrayWheelAdapter<String>(this, areas));
        mViewDistrict.setCurrentItem(0);
    }

    /**
     * 根据当前的省，更新市WheelView的信息
     */
    private void updateCities() {
        int pCurrent = mViewProvince.getCurrentItem();
        mCurrentProviceName = mProvinceDatas[pCurrent];
        String[] cities = mCitisDatasMap.get(mCurrentProviceName);
        if (cities == null) {
            cities = new String[]{""};
        }
        mViewCity.setViewAdapter(new ArrayWheelAdapter<String>(this, cities));
        mViewCity.setCurrentItem(0);
        updateAreas();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            SkipUtils.back(this);
            return true;
        } else {
            return super.onKeyDown(keyCode, event);
        }
    }

}
