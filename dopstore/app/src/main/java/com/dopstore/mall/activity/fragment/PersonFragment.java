package com.dopstore.mall.activity.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.dopstore.mall.R;
import com.dopstore.mall.base.BaseFragment;
import com.dopstore.mall.login.activity.LoginActivity;
import com.dopstore.mall.order.activity.MyActivityActivity;
import com.dopstore.mall.order.activity.MyOrderActivity;
import com.dopstore.mall.person.activity.MyAddressActivity;
import com.dopstore.mall.person.activity.MyBalanceActivity;
import com.dopstore.mall.person.activity.MyCollectActivity;
import com.dopstore.mall.person.activity.MyDetailActivity;
import com.dopstore.mall.person.activity.SetActivity;
import com.dopstore.mall.util.Constant;
import com.dopstore.mall.util.LoadImageUtils;
import com.dopstore.mall.util.SkipUtils;
import com.dopstore.mall.util.UserUtils;
import com.dopstore.mall.util.Utils;
import com.dopstore.mall.view.CircleImageView;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by 喜成 on 16/9/5.
 * name 个人中心
 */
public class PersonFragment extends BaseFragment {
    private RelativeLayout userLayout, orderLayout, payLayout, sendLayout, receiveLayout, walletLayout, activityLayout, collectLayout, addressLayout;
    private TextView nameTv, introTv, priceTv;
    private ImageButton rightBt;
    private CircleImageView headImage;

    private LoadImageUtils loadImageUtils;

    private final static int BALANCE_CODE = 0;
    private final static int SETTING_CODE = 1;

    private View v;
    
    private Context context;

    public PersonFragment(Context context) {
        this.context = context;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.layout_person_fragment, null);
        initView(v);
        loadData();
        return v;
    }

    private void initView(View v) {
        loadImageUtils = LoadImageUtils.getInstance(context);
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
        if (UserUtils.haveLogin(context)) {
            nameTv.setVisibility(View.VISIBLE);
            nameTv.setText(UserUtils.getNickName(context));
            long babytime = UserUtils.getBabyBirthday(context) * 1000;
            Long currentTime = System.currentTimeMillis();
            long time = currentTime - babytime;
            String babyName = UserUtils.getBabyName(context);
            introTv.setText(babyName + "  " + Utils.formatMilli(babytime, "yyyy年MM月dd日"));
            String avatar = UserUtils.getAvatar(context);
            if (TextUtils.isEmpty(avatar)) {
                headImage.setImageResource(R.mipmap.ic);
            } else {
                loadImageUtils.displayImage(avatar, headImage);
            }
            priceTv.setText(UserUtils.getBalance(context));
        } else {
            headImage.setImageResource(R.mipmap.ic);
            nameTv.setVisibility(View.INVISIBLE);
            introTv.setText("立即登录");
        }
    }

    View.OnClickListener listener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.title_right_imageButton: {
                    if (!UserUtils.haveLogin(context)) {
                        SkipUtils.directJump(context, LoginActivity.class, false);
                    } else {
                        SkipUtils.directJumpForResult(context, SetActivity.class, SETTING_CODE);
                    }
                }
                break;
                case R.id.fragment_person_user_layout: {
                    if (!UserUtils.haveLogin(context)) {
                        SkipUtils.directJump(context, LoginActivity.class, false);
                    }else {
                        SkipUtils.directJump(context, MyDetailActivity.class,false);
                    }
                }
                break;
                case R.id.fragment_person_order_layout: {
                    if (!UserUtils.haveLogin(context)) {
                        SkipUtils.directJump(context, LoginActivity.class, false);
                    } else {
                        Map<String, Object> map = new HashMap<String, Object>();
                        map.put("title", "0");
                        SkipUtils.jumpForMap(context, MyOrderActivity.class, map, false);
                    }
                }
                break;
                case R.id.fragment_person_pay_layout: {
                    if (!UserUtils.haveLogin(context)) {
                        SkipUtils.directJump(context, LoginActivity.class, false);
                    } else {
                        Map<String, Object> map = new HashMap<String, Object>();
                        map.put("title", "1");
                        SkipUtils.jumpForMap(context, MyOrderActivity.class, map, false);
                    }
                }
                break;
                case R.id.fragment_person_send_layout: {
                    if (!UserUtils.haveLogin(context)) {
                        SkipUtils.directJump(context, LoginActivity.class, false);
                    } else {
                        Map<String, Object> map = new HashMap<String, Object>();
                        map.put("title", "2");
                        SkipUtils.jumpForMap(context, MyOrderActivity.class, map, false);
                    }
                }
                break;
                case R.id.fragment_person_receive_layout: {
                    if (!UserUtils.haveLogin(context)) {
                        SkipUtils.directJump(context, LoginActivity.class, false);
                    } else {
                        Map<String, Object> map = new HashMap<String, Object>();
                        map.put("title", "3");
                        SkipUtils.jumpForMap(context, MyOrderActivity.class, map, false);
                    }
                }
                break;
                case R.id.fragment_person_wallet_layout: {
                    if (!UserUtils.haveLogin(context)) {
                        SkipUtils.directJump(context, LoginActivity.class, false);
                    } else {
                        Map<String, Object> map = new HashMap<String, Object>();
                        map.put(Constant.BALANCE, priceTv.getText().toString().trim());
                        SkipUtils.jumpForMapResult(context, MyBalanceActivity.class, map, BALANCE_CODE);
                    }
                }
                break;
                case R.id.fragment_person_activity_layout: {
                    if (!UserUtils.haveLogin(context)) {
                        SkipUtils.directJump(context, LoginActivity.class, false);
                    } else {
                        SkipUtils.directJump(context, MyActivityActivity.class, false);
                    }
                }
                break;
                case R.id.fragment_person_collect_layout: {
                    if (!UserUtils.haveLogin(context)) {
                        SkipUtils.directJump(context, LoginActivity.class, false);
                    } else {
                        SkipUtils.directJump(context, MyCollectActivity.class, false);
                    }
                }
                break;
                case R.id.fragment_person_address_layout: {
                    if (!UserUtils.haveLogin(context)) {
                        SkipUtils.directJump(context, LoginActivity.class, false);
                    } else {
                        SkipUtils.directJump(context, MyAddressActivity.class, false);
                    }
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
            case SETTING_CODE: {
                loadData();
            }
            break;
        }
    }


}
