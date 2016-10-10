package com.dopstore.mall.base;

import android.content.res.AssetManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.amap.api.location.AMapLocationClient;
import com.dopstore.mall.R;
import com.dopstore.mall.util.HttpHelper;
import com.dopstore.mall.util.ProUtils;
import com.dopstore.mall.util.SkipUtils;
import com.dopstore.mall.view.addresspicker.wheel.widget.model.CityModel;
import com.dopstore.mall.view.addresspicker.wheel.widget.model.DistrictModel;
import com.dopstore.mall.view.addresspicker.wheel.widget.model.ProvinceModel;
import com.dopstore.mall.view.addresspicker.wheel.widget.service.XmlParserHandler;

import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import cn.sharesdk.framework.ShareSDK;


public class BaseActivity extends AppCompatActivity {
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


    public HttpHelper httpHelper;
    public ProUtils proUtils;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        MyApplication.getInstance().addActivity(this);
        ShareSDK.initSDK(this);
        httpHelper = HttpHelper.getOkHttpClientUtils(this);
        proUtils = new ProUtils(this);
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (proUtils.isShowing()){proUtils.dismiss();}
        ShareSDK.stopSDK(this);
        MyApplication.getInstance().removeActivity(this);
    }


    /**
     * 设置标题栏背景
     */
    public void setTopBg(int bgId) {
        RelativeLayout topLayout = (RelativeLayout) findViewById(R.id.brandsquare_title_layout);
        if (topLayout == null)
            return;
        topLayout.setBackgroundResource(bgId);
    }

    /**
     * 设置标题
     *
     * @param text
     */
    public void setCustomTitle(String text, int colorId) {
        TextView title = (TextView) findViewById(R.id.title_main_txt);
        if (title == null)
            return;
        title.setText(text);
        title.setTextColor(colorId);
    }

    /**
     * 设置标题颜色
     *
     * @param color
     */
    public void setCustomTitleColor(int color) {
        TextView title = (TextView) findViewById(R.id.title_main_txt);
        if (title == null)
            return;
        title.setTextColor(color);
    }

    /**
     * 左图片按钮
     */
    protected void leftImageBack(int imageId) {
        ImageButton id = (ImageButton) findViewById(R.id.title_left_imageButton);
        id.setImageResource(imageId);
        id.setVisibility(View.VISIBLE);
        if (id != null) {
            id.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    SkipUtils.back(BaseActivity.this);
                }
            });
        }
    }

    /**
     * 左文字按钮
     */
    protected void leftTextBack(String text, int color, View.OnClickListener listener) {
        TextView id = (TextView) findViewById(R.id.title_left_textView);
        id.setText(text);
        id.setTextColor(color);
        id.setVisibility(View.VISIBLE);
        if (id != null) {
            id.setOnClickListener(listener);
        }
    }

    /**
     * 右一图片按钮
     */
    protected void rightFirstImageBack(int imageId, View.OnClickListener listener) {
        ImageButton id = (ImageButton) findViewById(R.id.title_right_imageButton);
        id.setImageResource(imageId);
        id.setVisibility(View.VISIBLE);
        if (id != null) {
            id.setOnClickListener(listener);
        }
    }

    /**
     * 右二图片按钮
     */
    protected void rightSecondImageBack(int imageId, View.OnClickListener listener) {
        ImageButton id = (ImageButton) findViewById(R.id.title_right_before_imageButton);
        id.setImageResource(imageId);
        id.setVisibility(View.VISIBLE);
        if (id != null) {
            id.setOnClickListener(listener);
        }
    }

    /**
     * 右文字按钮
     */
    protected void rightTextBack(String text, int colorId, View.OnClickListener listener) {
        TextView id = (TextView) findViewById(R.id.title_right_textButton);
        id.setText(text);
        id.setTextColor(colorId);
        id.setVisibility(View.VISIBLE);
        if (id != null) {
            id.setOnClickListener(listener);
        }
    }
    /**********************************************地址解析***********************************************/

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
