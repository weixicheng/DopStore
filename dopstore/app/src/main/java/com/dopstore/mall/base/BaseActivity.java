package com.dopstore.mall.base;

import android.content.res.AssetManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.dopstore.mall.R;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        MyApplication.getInstance().addActivity(this);
        ShareSDK.initSDK(this);
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
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



}
