package com.dopstore.mall.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.view.KeyEvent;
import android.view.View;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.dopstore.mall.R;
import com.dopstore.mall.activity.fragment.ActivityFragment;
import com.dopstore.mall.activity.fragment.MainFragment;
import com.dopstore.mall.activity.fragment.PersonFragment;
import com.dopstore.mall.activity.fragment.TrolleyFragment;
import com.dopstore.mall.base.BaseActivity;
import com.dopstore.mall.base.MyApplication;
import com.dopstore.mall.util.Constant;
import com.dopstore.mall.util.HttpHelper;
import com.dopstore.mall.view.CommonDialog;
import com.dopstore.mall.view.MyViewPager;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class MainActivity extends BaseActivity {
    protected RelativeLayout mainRly;
    private RelativeLayout bilingRly;
    private RelativeLayout headRly;
    private RelativeLayout myRly;
    private TextView mainTxt, bilingTxt, headTxt, myTxt;
    private ImageView mainIv, bilingIv, headIv, myIv;
    private MyViewPager viewPager;

    private final static int MAIN_CODE = 0;
    private final static int BILING_CODE = 1;
    private final static int ROB_CODE = 2;
    private final static int MY_CODE = 3;
    private final static int BACK_CODE = 5;

    private MainFragment mainFragment;
    private ActivityFragment activityFragment;
    private TrolleyFragment trolleyFragment;
    private PersonFragment personFragment;


    private CommonDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        initData();
    }

    /**
     * 初始化
     */
    private void initView() {
        Map<String, String> map = new HashMap<>();
        mainRly = (RelativeLayout) findViewById(R.id.main_tab_button);
        viewPager = (MyViewPager) findViewById(R.id.main_content_viewpager);
        bilingRly = (RelativeLayout) findViewById(R.id.billing_tab_button);
        headRly = (RelativeLayout) findViewById(R.id.head_tab_button);
        myRly = (RelativeLayout) findViewById(R.id.my_tab_button);
        mainRly.setOnClickListener(listener);
        bilingRly.setOnClickListener(listener);
        headRly.setOnClickListener(listener);
        myRly.setOnClickListener(listener);

        mainTxt = (TextView) findViewById(R.id.main_tab_text);
        bilingTxt = (TextView) findViewById(R.id.billing_tab_txt);
        headTxt = (TextView) findViewById(R.id.head_tab_txt);
        myTxt = (TextView) findViewById(R.id.my_tab_txt);

        mainIv = (ImageView) findViewById(R.id.main_tab_image);
        bilingIv = (ImageView) findViewById(R.id.billing_tab_image);
        headIv = (ImageView) findViewById(R.id.head_tab_image);
        myIv = (ImageView) findViewById(R.id.my_tab_image);

        FragmentAdapter adapter = new FragmentAdapter(this.getSupportFragmentManager());
        viewPager.setOffscreenPageLimit(5);
        viewPager.setNoScroll(true);
        viewPager.setAdapter(adapter);


    }

    private void initData() {
        // TODO Auto-generated method stub

    }

    /**
     * 点击事件
     */
    View.OnClickListener listener = new View.OnClickListener() {

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.main_tab_button:// 首页
                    setTab(MAIN_CODE);
                    viewPager.setCurrentItem(MAIN_CODE);
                    break;
                case R.id.billing_tab_button:// 活动
                    setTab(BILING_CODE);
                    viewPager.setCurrentItem(BILING_CODE);
                    break;
                case R.id.head_tab_button:// 购物车
                    setTab(ROB_CODE);
                    viewPager.setCurrentItem(ROB_CODE);
                    break;
                case R.id.my_tab_button:// 我的
                    setTab(MY_CODE);
                    viewPager.setCurrentItem(MY_CODE);
                    break;
                default:
                    break;
            }
        }
    };

    /**
     * 设置颜色
     *
     * @param i
     */
    private void setTab(int i) {
        switch (i) {
            case MAIN_CODE: {// 首页
                mainIv.setBackgroundResource(R.mipmap.main_press);
                bilingIv.setBackgroundResource(R.mipmap.activity_normal);
                headIv.setBackgroundResource(R.mipmap.shop_normal);
                myIv.setBackgroundResource(R.mipmap.person_normal);

                mainTxt.setTextColor(getResources().getColor(R.color.red_color_f93448));
                bilingTxt.setTextColor(getResources().getColor(R.color.gray_color_33));
                headTxt.setTextColor(getResources().getColor(R.color.gray_color_33));
                myTxt.setTextColor(getResources().getColor(R.color.gray_color_33));
            }
            break;
            case BILING_CODE: {// 活动
                mainIv.setBackgroundResource(R.mipmap.main_normal);
                bilingIv.setBackgroundResource(R.mipmap.activity_press);
                headIv.setBackgroundResource(R.mipmap.shop_normal);
                myIv.setBackgroundResource(R.mipmap.person_normal);

                mainTxt.setTextColor(getResources().getColor(R.color.gray_color_33));
                bilingTxt.setTextColor(getResources().getColor(R.color.red_color_f93448));
                headTxt.setTextColor(getResources().getColor(R.color.gray_color_33));
                myTxt.setTextColor(getResources().getColor(R.color.gray_color_33));
            }
            break;
            case ROB_CODE: {// 购物车
                mainIv.setBackgroundResource(R.mipmap.main_normal);
                bilingIv.setBackgroundResource(R.mipmap.activity_normal);
                headIv.setBackgroundResource(R.mipmap.shop_press);
                myIv.setBackgroundResource(R.mipmap.person_normal);

                mainTxt.setTextColor(getResources().getColor(R.color.gray_color_33));
                bilingTxt.setTextColor(getResources().getColor(R.color.gray_color_33));
                headTxt.setTextColor(getResources().getColor(R.color.red_color_f93448));
                myTxt.setTextColor(getResources().getColor(R.color.gray_color_33));
            }
            break;
            case MY_CODE: {// 我的
                mainIv.setBackgroundResource(R.mipmap.main_normal);
                bilingIv.setBackgroundResource(R.mipmap.activity_normal);
                headIv.setBackgroundResource(R.mipmap.shop_normal);
                myIv.setBackgroundResource(R.mipmap.person_press);

                mainTxt.setTextColor(getResources().getColor(R.color.gray_color_33));
                bilingTxt.setTextColor(getResources().getColor(R.color.gray_color_33));
                headTxt.setTextColor(getResources().getColor(R.color.gray_color_33));
                myTxt.setTextColor(getResources().getColor(R.color.red_color_f93448));
            }
            break;
            default:
                break;
        }
    }


    class FragmentAdapter extends FragmentStatePagerAdapter {
        public FragmentAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case MAIN_CODE:// 首页
                    return mainFragment = new MainFragment();
                case BILING_CODE:// 活动
                    return activityFragment = new ActivityFragment();
                case ROB_CODE:// 购物车
                    return trolleyFragment = new TrolleyFragment();
                case MY_CODE:// 我的
                    return personFragment = new PersonFragment();
            }
            return null;
        }

        @Override
        public int getCount() {
            return 4;
        }
    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            exitDialog();
            return true;
        } else {
            return super.onKeyDown(keyCode, event);
        }
    }

    private void exitDialog() {
        dialog = new CommonDialog(this, handler, BACK_CODE, "提示", "确定退出?", Constant.SHOWTITLEALLBUTTON);
        dialog.show();
    }

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case BACK_CODE: {
                    MyApplication.getInstance().finishAllActivity();
                }
                break;
            }
        }
    };

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        mainFragment.onActivityResult(requestCode, resultCode, data);
        personFragment.onActivityResult(requestCode, resultCode, data);
    }

}
