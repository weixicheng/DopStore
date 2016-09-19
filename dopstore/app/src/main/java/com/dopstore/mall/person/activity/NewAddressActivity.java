package com.dopstore.mall.person.activity;

import android.content.Intent;
import android.content.res.AssetManager;
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
import android.widget.Toast;

import com.dopstore.mall.R;
import com.dopstore.mall.base.BaseActivity;
import com.dopstore.mall.person.bean.MyAddressData;
import com.dopstore.mall.util.Constant;
import com.dopstore.mall.util.HttpHelper;
import com.dopstore.mall.util.PopupUtils;
import com.dopstore.mall.util.ProUtils;
import com.dopstore.mall.util.SkipUtils;
import com.dopstore.mall.util.T;
import com.dopstore.mall.util.URL;
import com.dopstore.mall.util.UserUtils;
import com.dopstore.mall.util.Utils;
import com.dopstore.mall.view.addresspicker.wheel.widget.OnWheelChangedListener;
import com.dopstore.mall.view.addresspicker.wheel.widget.WheelView;
import com.dopstore.mall.view.addresspicker.wheel.widget.adapters.ArrayWheelAdapter;
import com.dopstore.mall.view.addresspicker.wheel.widget.model.CityModel;
import com.dopstore.mall.view.addresspicker.wheel.widget.model.DistrictModel;
import com.dopstore.mall.view.addresspicker.wheel.widget.model.ProvinceModel;
import com.dopstore.mall.view.addresspicker.wheel.widget.service.XmlParserHandler;
import com.squareup.okhttp.Address;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;


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
    private HttpHelper httpHelper;
    private String is_default="0";
    private String address_id="";
    private ProUtils proUtils;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_address);
        initView();
    }

    private void initView() {

        httpHelper=HttpHelper.getOkHttpClientUtils(this);
        proUtils=new ProUtils(this);
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
        Map<String,Object> map=SkipUtils.getMap(this);
        if (map!=null) {
            MyAddressData data = (MyAddressData) map.get(Constant.LIST);
            address_id=data.getId();
            Address_phone.setText(data.getMobile());
            Address_name.setText(data.getShipping_user());
            mAddress.setText(data.getAddress());
            mCurrentProviceName=data.getProvince();
            mCurrentCityName=data.getCity();
            mCurrentDistrictName=data.getArea();
            mProvince_city_area.setTextColor(getResources().getColor(R.color.gray_color_33));
            mProvince_city_area.setText(mCurrentProviceName+mCurrentCityName +mCurrentDistrictName);
            card_num.setText(data.getId_card());
            is_default=data.is_default;
            if ("0".equals(is_default)){
                clcik_address = false;
                mLv_click.setImageResource(R.mipmap.checkbox_normal);
            }else {
                clcik_address=true;
                mLv_click.setImageResource(R.mipmap.checkbox_checked);
            }
            deleBt.setVisibility(View.VISIBLE);
            hintV.setVisibility(View.GONE);
        }else {
            address_id="";
            deleBt.setVisibility(View.GONE);
            hintV.setVisibility(View.VISIBLE);
        }
    }

    OnClickListener listener = new OnClickListener() {
        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.new_address_city: {
                    showPop();
                }
                break;
                case R.id.new_address_default: {
                    if (clcik_address) {
                        mLv_click.setImageResource(R.mipmap.checkbox_normal);
                        clcik_address = false;
                        is_default="0";
                    } else {
                        mLv_click.setImageResource(R.mipmap.checkbox_checked);
                        clcik_address = true;
                        is_default="1";
                    }
                }
                break;
                case R.id.popup_cancel:{
                   dismissPoup();
                }break;
                case R.id.popup_confirm:{
                    mProvince_city_area.setTextColor(getResources().getColor(R.color.gray_color_33));
                    mProvince_city_area.setText(mCurrentProviceName+mCurrentCityName +mCurrentDistrictName);
                    dismissPoup();
                }break;
                case R.id.new_address_save:{saveMsg();}break;
                case R.id.new_address_delete:{deleteMsg();}break;
            }

        }
    };

    private void deleteMsg() {
        proUtils.show();
        httpHelper.getDataAsync(this, URL.SHIPPINGADDRESS+"shippingaddress/"+address_id+"/delete", new Callback() {
            @Override
            public void onFailure(Request request, IOException e) {
                T.checkNet(NewAddressActivity.this);
            }

            @Override
            public void onResponse(Response response) throws IOException {
                String body = response.body().string();
                try {
                    JSONObject jo = new JSONObject(body);
                    String code = jo.optString(Constant.ERROR_CODE);
                    if ("0".equals(code)) {
                        T.show(NewAddressActivity.this, "成功");
                        handler.sendEmptyMessageDelayed(0,300);
                    } else {
                        String msg = jo.optString(Constant.ERROR_MSG);
                        T.show(NewAddressActivity.this, msg);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                proUtils.diamiss();
            }
        },null);
    }

    Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            SkipUtils.back(NewAddressActivity.this);
        }
    };

    private void saveMsg() {
        String name=Address_name.getText().toString();
        if (TextUtils.isEmpty(name)){
            T.show(this,"请输入姓名");
            return;
        }
        String phone=Address_phone.getText().toString();
        if (TextUtils.isEmpty(name)){
            T.show(this,"请输入手机号");
            return;
        }
        if (!Utils.isPhoneNumberValid(phone)){
            T.show(this,"请检查手机号");
            return;
        }
        String cardNum=card_num.getText().toString();
        if (TextUtils.isEmpty(name)){
            T.show(this,"请输入身份证号");
            return;
        }
        String addressStr=mAddress.getText().toString();
        if (TextUtils.isEmpty(name)){
            T.show(this,"请输入详细地址");
            return;
        }
        proUtils.show();
        String id= UserUtils.getId(this);
        Map<String,String> map=new HashMap<String,String>();
        map.put(Constant.ID,address_id);
        map.put(Constant.SHIPPING_USER,name);
        map.put(Constant.MOBILE,phone);
        map.put(Constant.PROVINCE,mCurrentProviceName);
        map.put(Constant.CITY,mCurrentCityName);
        map.put(Constant.AREA,mCurrentDistrictName);
        map.put(Constant.ID_CARD,cardNum);
        map.put(Constant.ADDRESS,addressStr);
        map.put(Constant.IS_DEFAULT,is_default);
        httpHelper.postKeyValuePairAsync(this, URL.SHIPPINGADDRESS+id+"/update_shippingaddress",map, new Callback() {
            @Override
            public void onFailure(Request request, IOException e) {
                T.checkNet(NewAddressActivity.this);
            }

            @Override
            public void onResponse(Response response) throws IOException {
                String body = response.body().string();
                try {
                    JSONObject jo = new JSONObject(body);
                    String code = jo.optString(Constant.ERROR_CODE);
                    if ("0".equals(code)) {
                        T.show(NewAddressActivity.this, "成功");
                        handler.sendEmptyMessageDelayed(0,300);
                    } else {
                        String msg = jo.optString(Constant.ERROR_MSG);
                        T.show(NewAddressActivity.this, msg);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                proUtils.diamiss();
            }
        },null);
    }

    private void dismissPoup(){
        if (popupWindow.isShowing()){popupWindow.dismiss();}
    }

    private void showPop() {
        bgLayout.setVisibility(View.VISIBLE);
        int screenWidth = getWindowManager().getDefaultDisplay().getWidth();
        View v= LayoutInflater.from(this).inflate(R.layout.poup_city_list,null);
        setUpViews(v);
        setUpData();
        popupWindow= PopupUtils.ShowBottomPopupWindow(this,popupWindow,v,screenWidth,200,findViewById(R.id.new_address_main_relalayout));
        popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                bgLayout.setVisibility(View.GONE);
                popupWindow=null;
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

    OnWheelChangedListener onWheelChangedListener=new OnWheelChangedListener() {
        @Override
        public void onChanged(WheelView wheel, int oldValue, int newValue) {
            if (wheel == mViewProvince) {
                updateCities();
            } else if (wheel == mViewCity) {
                updateAreas();
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
            areas = new String[] { "" };
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
            cities = new String[] { "" };
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


    /**********************************************地址解析***********************************************/
    /**
     * 所有省
     */
    protected String[] mProvinceDatas;
    /**
     * key - 省 value - 市
     */
    protected Map<String, String[]> mCitisDatasMap = new HashMap<String, String[]>();
    /**
     * key - 市 values - 区
     */
    protected Map<String, String[]> mDistrictDatasMap = new HashMap<String, String[]>();

    /**
     * key - 区 values - 邮编
     */
    protected Map<String, String> mZipcodeDatasMap = new HashMap<String, String>();

    /**
     * 当前省的名称
     */
    protected String mCurrentProviceName;
    /**
     * 当前市的名称
     */
    protected String mCurrentCityName;
    /**
     * 当前区的名称
     */
    protected String mCurrentDistrictName = "";

    /**
     * 当前区的邮政编码
     */
    protected String mCurrentZipCode = "";

    /**
     * 解析省市区的XML数据
     */

    protected void initProvinceDatas() {
        List<ProvinceModel> provinceList = null;
        AssetManager asset = getAssets();
        try {
            InputStream input = asset.open("province_data.xml");
            // 创建一个解析xml的工厂对象
            SAXParserFactory spf = SAXParserFactory.newInstance();
            // 解析xml
            SAXParser parser = spf.newSAXParser();
            XmlParserHandler handler = new XmlParserHandler();
            parser.parse(input, handler);
            input.close();
            // 获取解析出来的数据
            provinceList = handler.getDataList();
            //*/ 初始化默认选中的省、市、区
            if (provinceList != null && !provinceList.isEmpty()) {
                mCurrentProviceName = provinceList.get(0).getName();
                List<CityModel> cityList = provinceList.get(0).getCityList();
                if (cityList != null && !cityList.isEmpty()) {
                    mCurrentCityName = cityList.get(0).getName();
                    List<DistrictModel> districtList = cityList.get(0).getDistrictList();
                    mCurrentDistrictName = districtList.get(0).getName();
                    mCurrentZipCode = districtList.get(0).getZipcode();
                }
            }
            //*/
            mProvinceDatas = new String[provinceList.size()];
            for (int i = 0; i < provinceList.size(); i++) {
                // 遍历所有省的数据
                mProvinceDatas[i] = provinceList.get(i).getName();
                List<CityModel> cityList = provinceList.get(i).getCityList();
                String[] cityNames = new String[cityList.size()];
                for (int j = 0; j < cityList.size(); j++) {
                    // 遍历省下面的所有市的数据
                    cityNames[j] = cityList.get(j).getName();
                    List<DistrictModel> districtList = cityList.get(j).getDistrictList();
                    String[] distrinctNameArray = new String[districtList.size()];
                    DistrictModel[] distrinctArray = new DistrictModel[districtList.size()];
                    for (int k = 0; k < districtList.size(); k++) {
                        // 遍历市下面所有区/县的数据
                        DistrictModel districtModel = new DistrictModel(districtList.get(k).getName(), districtList.get(k).getZipcode());
                        // 区/县对于的邮编，保存到mZipcodeDatasMap
                        mZipcodeDatasMap.put(districtList.get(k).getName(), districtList.get(k).getZipcode());
                        distrinctArray[k] = districtModel;
                        distrinctNameArray[k] = districtModel.getName();
                    }
                    // 市-区/县的数据，保存到mDistrictDatasMap
                    mDistrictDatasMap.put(cityNames[j], distrinctNameArray);
                }
                // 省-市的数据，保存到mCitisDatasMap
                mCitisDatasMap.put(provinceList.get(i).getName(), cityNames);
            }
        } catch (Throwable e) {
            e.printStackTrace();
        } finally {

        }
    }


}
