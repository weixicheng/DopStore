package com.dopstore.mall.person.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.dopstore.mall.R;
import com.dopstore.mall.base.BaseActivity;
import com.dopstore.mall.util.ACache;
import com.dopstore.mall.util.Constant;
import com.dopstore.mall.util.DataCleanManager;
import com.dopstore.mall.util.LoadImageUtils;
import com.dopstore.mall.util.SkipUtils;
import com.dopstore.mall.util.T;
import com.dopstore.mall.util.UserUtils;
import com.dopstore.mall.view.CommonDialog;


/**
 * 作者：xicheng on 16/9/7 18:24
 * 类别：设置
 */
public class SetActivity extends BaseActivity {
    private RelativeLayout detailLayout, safeLayout, addressLayout, msgLayout, adviceLayout, helpLayout, claenLayout, goodLayout, aboutLayout;
    private TextView textView_set_clear;
    private Button exitBt;
    private CommonDialog dialog;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set);
        initview();
    }

    private void initview() {
        ImageButton id = (ImageButton) findViewById(R.id.title_left_imageButton);
        id.setImageResource(R.mipmap.back_arrow);
        id.setVisibility(View.VISIBLE);
        id.setOnClickListener(listener);
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
                case R.id.title_left_imageButton:
                    Intent intent=new Intent();
                    setResult(Activity.RESULT_OK,intent);
                    SkipUtils.back(SetActivity.this);
                    break;
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
                    dialog=new CommonDialog(SetActivity.this,handler,CLEAR_CACHE_CODE,"提示","是否清理缓存?",Constant.SHOWALLBUTTON);
                    dialog.show();
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
        UserUtils.clear(this);
        ACache.get(this).clear();
        LoadImageUtils.getInstance(this).clear();
        Intent it=new Intent();
        it.setAction(Constant.UP_USER_DATA);
        sendBroadcast(it);
        SkipUtils.back(this);
    }
    private final static int CLEAR_CACHE_CODE=0;
    Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case CLEAR_CACHE_CODE:{
                    DataCleanManager.clearAllCache(SetActivity.this);
                    textView_set_clear.setText("0M");
                }break;
            }
        }
    };


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            Intent intent=new Intent();
            setResult(Activity.RESULT_OK,intent);
            SkipUtils.back(SetActivity.this);
            return true;
        } else {
            return super.onKeyDown(keyCode, event);
        }
    }
}
