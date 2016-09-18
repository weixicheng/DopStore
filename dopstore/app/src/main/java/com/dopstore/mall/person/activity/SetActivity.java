package com.dopstore.mall.person.activity;

import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.dopstore.mall.R;
import com.dopstore.mall.base.BaseActivity;
import com.dopstore.mall.base.MyApplication;
import com.dopstore.mall.util.ACache;
import com.dopstore.mall.util.DataCleanManager;
import com.dopstore.mall.util.LoadImageUtils;
import com.dopstore.mall.util.SkipUtils;
import com.dopstore.mall.util.T;
import com.dopstore.mall.util.UserUtils;


/**
 * 作者：xicheng on 16/9/7 18:24
 * 类别：设置
 */
public class SetActivity extends BaseActivity {
    private RelativeLayout detailLayout, safeLayout, addressLayout, msgLayout, adviceLayout, helpLayout, claenLayout, goodLayout, aboutLayout;
    private TextView textView_set_clear;
    private Button exitBt;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set);
        initview();
    }

    private void initview() {
        leftImageBack(R.mipmap.back_arrow);
        setCustomTitle("设置", getResources().getColor(R.color.white_color));
        textView_set_clear = (TextView) findViewById(R.id.setting_my_clean_data);
        try {
            String size = DataCleanManager.getTotalCacheSize(this);
            textView_set_clear.setText(size);
        } catch (Exception e) {
            e.printStackTrace();
        }

        detailLayout = (RelativeLayout) findViewById(R.id.setting_my_detail_layout);
        detailLayout.setOnClickListener(listener);
        safeLayout = (RelativeLayout) findViewById(R.id.setting_my_safe_layout);
        safeLayout.setOnClickListener(listener);
        addressLayout = (RelativeLayout) findViewById(R.id.setting_my_address_layout);
        addressLayout.setOnClickListener(listener);
        msgLayout = (RelativeLayout) findViewById(R.id.setting_my_msg_layout);
        msgLayout.setOnClickListener(listener);
        adviceLayout = (RelativeLayout) findViewById(R.id.setting_my_advice_layout);
        adviceLayout.setOnClickListener(listener);
        helpLayout = (RelativeLayout) findViewById(R.id.setting_my_help_layout);
        helpLayout.setOnClickListener(listener);
        claenLayout = (RelativeLayout) findViewById(R.id.setting_my_clean_layout);
        claenLayout.setOnClickListener(listener);
        goodLayout = (RelativeLayout) findViewById(R.id.setting_my_good_layout);
        goodLayout.setOnClickListener(listener);
        aboutLayout = (RelativeLayout) findViewById(R.id.setting_my_about_layout);
        aboutLayout.setOnClickListener(listener);
        exitBt = (Button) findViewById(R.id.setting_my_exit);
        exitBt.setOnClickListener(listener);
    }

    View.OnClickListener listener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.setting_my_detail_layout:
                    SkipUtils.directJump(SetActivity.this, MyDetailActivity.class, false);
                    break;
                case R.id.setting_my_safe_layout:
                    SkipUtils.directJump(SetActivity.this, SafeActivity.class, false);
                    break;
                case R.id.setting_my_address_layout:
                    SkipUtils.directJump(SetActivity.this, MyAddressActivity.class, false);
                    break;
                case R.id.setting_my_msg_layout:
                    SkipUtils.directJump(SetActivity.this, NoticActivity.class, false);
                    break;
                case R.id.setting_my_advice_layout:
                    SkipUtils.directJump(SetActivity.this, AdviceActivity.class, false);
                    break;
                case R.id.setting_my_help_layout:
                    SkipUtils.directJump(SetActivity.this, HelpActivity.class, false);
                    break;
                case R.id.setting_my_clean_layout:
                    DataCleanManager.clearAllCache(SetActivity.this);
                    textView_set_clear.setText("0M");
                    break;
                case R.id.setting_my_good_layout:
                    T.show(SetActivity.this, "亲,给个好评吧");
                    break;
                case R.id.setting_my_about_layout:
                    T.show(SetActivity.this, "关于小海囤");
                    break;
                case R.id.setting_my_exit:
                    exit();
                    break;
            }
        }
    };

    private void exit() {
        LoadImageUtils.getInstance(this).clear();
        ACache aCache = ACache.get(this);
        aCache.clear();
        UserUtils.clear(this);
        MyApplication.getInstance().finishAllActivity();
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
