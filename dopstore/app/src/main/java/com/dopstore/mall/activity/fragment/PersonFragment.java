package com.dopstore.mall.activity.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.dopstore.mall.R;
import com.dopstore.mall.login.activity.LoginActivity;
import com.dopstore.mall.order.activity.MyActivityActivity;
import com.dopstore.mall.order.activity.MyOrderActivity;
import com.dopstore.mall.person.activity.MyAddressActivity;
import com.dopstore.mall.person.activity.MyBalanceActivity;
import com.dopstore.mall.person.activity.MyCollectActivity;
import com.dopstore.mall.person.activity.SetActivity;
import com.dopstore.mall.util.Constant;
import com.dopstore.mall.util.LoadImageUtils;
import com.dopstore.mall.util.SkipUtils;
import com.dopstore.mall.util.UserUtils;
import com.dopstore.mall.util.Utils;
import com.dopstore.mall.view.CircleImageView;

import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by 喜成 on 16/9/5.
 * name 个人中心
 */
public class PersonFragment extends Fragment {
    private RelativeLayout userLayout, orderLayout, payLayout, sendLayout, receiveLayout, walletLayout, activityLayout, collectLayout, addressLayout;
    private TextView nameTv, introTv, priceTv;
    private ImageButton rightBt;
    private CircleImageView headImage;

    private Timer timer;
    private TimerTask doing;
    private final static int LAZY_LOADING_MSG = 0;
    private LoadImageUtils loadImageUtils;

    private final static int BALANCE_CODE = 0;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.layout_person_fragment, null);
        initView(v);
        return v;
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        if (isVisibleToUser) {
            if (null != timer) {
                return;
            } else {
                timer = new Timer();

                doing = new TimerTask() {
                    // 每个timerTask都要重写这个方法，因为是abstract的
                    public void run() {
                        handler.sendEmptyMessage(LAZY_LOADING_MSG);
                        // 通过sendMessage函数将消息压入线程的消息队列。
                    }
                };
                timer.schedule(doing, 100);
            }
        } else {
            // 不可见时不执行操作
        }
        super.setUserVisibleHint(isVisibleToUser);
    }

    private void initView(View v) {
        loadImageUtils = LoadImageUtils.getInstance(getActivity());
        orderLayout = (RelativeLayout) v.findViewById(R.id.fragment_person_order_layout);
        orderLayout.setOnClickListener(listener);
        userLayout = (RelativeLayout) v.findViewById(R.id.fragment_person_user_layout);
        userLayout.setOnClickListener(listener);
        payLayout = (RelativeLayout) v.findViewById(R.id.fragment_person_pay_layout);
        payLayout.setOnClickListener(listener);
        sendLayout = (RelativeLayout) v.findViewById(R.id.fragment_person_send_layout);
        sendLayout.setOnClickListener(listener);
        receiveLayout = (RelativeLayout) v.findViewById(R.id.fragment_person_receive_layout);
        receiveLayout.setOnClickListener(listener);
        walletLayout = (RelativeLayout) v.findViewById(R.id.fragment_person_wallet_layout);
        walletLayout.setOnClickListener(listener);
        activityLayout = (RelativeLayout) v.findViewById(R.id.fragment_person_activity_layout);
        activityLayout.setOnClickListener(listener);
        collectLayout = (RelativeLayout) v.findViewById(R.id.fragment_person_collect_layout);
        collectLayout.setOnClickListener(listener);
        addressLayout = (RelativeLayout) v.findViewById(R.id.fragment_person_address_layout);
        addressLayout.setOnClickListener(listener);
        nameTv = (TextView) v.findViewById(R.id.fragment_person_user_name);
        introTv = (TextView) v.findViewById(R.id.fragment_person_user_intro);
        priceTv = (TextView) v.findViewById(R.id.fragment_person_wallet_price);
        rightBt = (ImageButton) v.findViewById(R.id.title_right_imageButton);
        rightBt.setImageResource(R.mipmap.setting_logo);
        rightBt.setVisibility(View.VISIBLE);
        rightBt.setOnClickListener(listener);
        headImage = (CircleImageView) v.findViewById(R.id.fragment_person_user_image);
    }

    public void loadData() {
        if (UserUtils.haveLogin(getActivity())) {
            nameTv.setVisibility(View.VISIBLE);
            nameTv.setText(UserUtils.getNickName(getActivity()));
            long babytime=UserUtils.getBabyBirthday(getActivity())*1000;
            Long currentTime=System.currentTimeMillis();
            long time=currentTime-babytime;
            String babyName=UserUtils.getBabyName(getActivity());
            introTv.setText(babyName+"  "+ Utils.formatMilli(time,"dd日HH时"));
            loadImageUtils.displayImage(UserUtils.getAvatar(getActivity()), headImage);
            priceTv.setText(UserUtils.getBalance(getActivity()));
        } else {
            nameTv.setVisibility(View.INVISIBLE);
            introTv.setText("立即登录");
        }
    }

    View.OnClickListener listener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.title_right_imageButton: {
                    if (!UserUtils.haveLogin(getActivity())) {
                        SkipUtils.directJump(getActivity(), LoginActivity.class, false);
                    } else {
                        SkipUtils.directJump(getActivity(), SetActivity.class, false);
                    }
                }
                break;
                case R.id.fragment_person_user_layout: {
                    if (!UserUtils.haveLogin(getActivity())) {
                        SkipUtils.directJump(getActivity(), LoginActivity.class, false);
                    }
                }
                break;
                case R.id.fragment_person_order_layout: {
                    if (!UserUtils.haveLogin(getActivity())) {
                        SkipUtils.directJump(getActivity(), LoginActivity.class, false);
                    } else {
                        Map<String, Object> map = new HashMap<String, Object>();
                        map.put("title", "0");
                        SkipUtils.jumpForMap(getActivity(), MyOrderActivity.class, map, false);
                    }
                }
                break;
                case R.id.fragment_person_pay_layout: {
                    if (!UserUtils.haveLogin(getActivity())) {
                        SkipUtils.directJump(getActivity(), LoginActivity.class, false);
                    } else {
                        Map<String, Object> map = new HashMap<String, Object>();
                        map.put("title", "1");
                        SkipUtils.jumpForMap(getActivity(), MyOrderActivity.class, map, false);
                    }
                }
                break;
                case R.id.fragment_person_send_layout: {
                    if (!UserUtils.haveLogin(getActivity())) {
                        SkipUtils.directJump(getActivity(), LoginActivity.class, false);
                    } else {
                        Map<String, Object> map = new HashMap<String, Object>();
                        map.put("title", "2");
                        SkipUtils.jumpForMap(getActivity(), MyOrderActivity.class, map, false);
                    }
                }
                break;
                case R.id.fragment_person_receive_layout: {
                    if (!UserUtils.haveLogin(getActivity())) {
                        SkipUtils.directJump(getActivity(), LoginActivity.class, false);
                    } else {
                        Map<String, Object> map = new HashMap<String, Object>();
                        map.put("title", "3");
                        SkipUtils.jumpForMap(getActivity(), MyOrderActivity.class, map, false);
                    }
                }
                break;
                case R.id.fragment_person_wallet_layout: {
                    if (!UserUtils.haveLogin(getActivity())) {
                        SkipUtils.directJump(getActivity(), LoginActivity.class, false);
                    } else {
                        Map<String, Object> map = new HashMap<String, Object>();
                        map.put(Constant.BALANCE, priceTv.getText().toString().trim());
                        SkipUtils.jumpForMapResult(getActivity(), MyBalanceActivity.class, map, BALANCE_CODE);
                    }
                }
                break;
                case R.id.fragment_person_activity_layout: {
                    if (!UserUtils.haveLogin(getActivity())) {
                        SkipUtils.directJump(getActivity(), LoginActivity.class, false);
                    } else {
                        SkipUtils.directJump(getActivity(), MyActivityActivity.class, false);
                    }
                }
                break;
                case R.id.fragment_person_collect_layout: {
                    if (!UserUtils.haveLogin(getActivity())) {
                        SkipUtils.directJump(getActivity(), LoginActivity.class, false);
                    } else {
                        SkipUtils.directJump(getActivity(), MyCollectActivity.class, false);
                    }
                }
                break;
                case R.id.fragment_person_address_layout: {
                    if (!UserUtils.haveLogin(getActivity())) {
                        SkipUtils.directJump(getActivity(), LoginActivity.class, false);
                    } else {
                        SkipUtils.directJump(getActivity(), MyAddressActivity.class, false);
                    }
                }
                break;
            }
        }
    };

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case LAZY_LOADING_MSG: {
                    loadData();
                }
                break;
            }
        }
    };



    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data == null) return;
        switch (requestCode) {
            case BALANCE_CODE: {
                Map<String, Object> map = (Map<String, Object>) data.getSerializableExtra("map");
                priceTv.setText(map.get(Constant.BALANCE).toString());
            }
            break;
        }
    }


}
