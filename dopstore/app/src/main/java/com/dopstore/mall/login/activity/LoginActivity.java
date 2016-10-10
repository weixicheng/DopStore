package com.dopstore.mall.login.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.dopstore.mall.R;
import com.dopstore.mall.activity.bean.CityBean;
import com.dopstore.mall.activity.bean.UserData;
import com.dopstore.mall.base.BaseActivity;
import com.dopstore.mall.util.ACache;
import com.dopstore.mall.util.Constant;
import com.dopstore.mall.util.HttpHelper;
import com.dopstore.mall.util.OtherCallBack;
import com.dopstore.mall.util.OtherLoginUtils;
import com.dopstore.mall.util.ProUtils;
import com.dopstore.mall.util.SkipUtils;
import com.dopstore.mall.util.T;
import com.dopstore.mall.util.URL;
import com.dopstore.mall.util.UserUtils;
import com.dopstore.mall.util.Utils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.ShareSDK;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;


/**
 * 作者：xicheng on 16/7/29 18:25
 * 类别：登陆界面
 */
public class LoginActivity extends BaseActivity {
    private RelativeLayout topLayout;
    private EditText phoneEt, pwdEt;//手机 密码
    private Button loginBt, registBt;
    private TextView loseTxt;
    private ImageView weChatIv, qqIv, sinaIv;
    private ACache aCache;
    private OtherLoginUtils otherLoginUtils;
    private Platform mPlatform;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        initView();
    }

    private void initView() {
        aCache = ACache.get(this);
        otherLoginUtils = new OtherLoginUtils(this);
        topLayout = (RelativeLayout) findViewById(R.id.brandsquare_title_layout);
        topLayout.setBackgroundColor(getResources().getColor(R.color.white_color));
        leftTextBack("取消", getResources().getColor(R.color.red_color_f93448), listener);
        phoneEt = (EditText) findViewById(R.id.login_phone_et);
        pwdEt = (EditText) findViewById(R.id.login_pwd_et);
        loginBt = (Button) findViewById(R.id.login_submit_bt);
        registBt = (Button) findViewById(R.id.login_register_bt);
        loseTxt = (TextView) findViewById(R.id.login_lose_pwd_txt);
        weChatIv = (ImageView) findViewById(R.id.login_wechat_iv);
        qqIv = (ImageView) findViewById(R.id.login_qq_iv);
        sinaIv = (ImageView) findViewById(R.id.login_sina_iv);
        loginBt.setOnClickListener(listener);
        registBt.setOnClickListener(listener);
        loseTxt.setOnClickListener(listener);
        weChatIv.setOnClickListener(listener);
        qqIv.setOnClickListener(listener);
        sinaIv.setOnClickListener(listener);
    }

    View.OnClickListener listener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.title_left_textView: {//取消
                    SkipUtils.back(LoginActivity.this);
                }
                break;
                case R.id.login_register_bt: {//注册
                    SkipUtils.directJump(LoginActivity.this, RegisterActivity.class, false);
                }
                break;
                case R.id.login_submit_bt: {//登录
                    loginData();
                }
                break;
                case R.id.login_lose_pwd_txt: {//忘记密码
                    SkipUtils.directJump(LoginActivity.this, LosePwdActivity.class, false);
                }
                break;
                case R.id.login_wechat_iv: {//微信
                    toOther(1);
                }
                break;
                case R.id.login_qq_iv: {//QQ
                    toOther(0);
                }
                break;
                case R.id.login_sina_iv: {//新浪
                    toOther(2);
                }
                break;
            }

        }
    };

    private void loginData() {
        String phone = phoneEt.getText().toString().trim();
        String pwd = pwdEt.getText().toString().trim();

        if (TextUtils.isEmpty(phone)) {
            T.show(this, "请输入手机号");
            return;
        }

        if (!Utils.isPhoneNumberValid(phone)) {
            T.show(this, "请检查手机号是否正确");
            return;
        }

        if (TextUtils.isEmpty(pwd)) {
            T.show(this, "请输入密码");
            return;
        }

        loginToNext(phone, pwd);
    }

    private void loginToNext(String phone, String pwd) {
        proUtils.show();
        Map<String, Object> map = new HashMap<String, Object>();
        map.put(Constant.MOBILE, phone);
        map.put(Constant.PASSWORD, pwd);
        httpHelper.postKeyValuePairAsync(this, URL.LOGIN, map, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                T.checkNet(LoginActivity.this);
                proUtils.dismiss();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String body = response.body().string();
                AnalyData(body);
                proUtils.dismiss();
            }
        }, null);
    }

    private void toOther(final int numStr) {
        otherLoginUtils.authorize(numStr);
        otherLoginUtils.setCallBack(new OtherCallBack() {
            @Override
            public void success(String name) {
                loginOther(name, numStr);
            }

            @Override
            public void failed(String erroe) {
                T.show(LoginActivity.this, erroe);
            }
        });

    }

    private void loginOther(String platform, int numStr) {
        mPlatform = ShareSDK.getPlatform(platform);
        String gender = "";
        if (platform != null) {
            gender = mPlatform.getDb().getUserGender();
            if (gender.equals("m")) {
                gender = "1";
            } else {
                gender = "0";
            }
            String name = mPlatform.getDb().getUserName();
            String uid = mPlatform.getDb().getUserId();
            String picture = mPlatform.getDb().getUserIcon();
            otherLogin(name, gender, picture, uid, numStr);
        }
    }

    private void otherLogin(String name, String gender, String picture, String uid, int id) {
        proUtils.show();
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("nickname", name);
        map.put("avatar", picture);
        switch (id) {
            case 0: {
                map.put("wx_unionid", "");
                map.put("weibo_uid", "");
                map.put("qq_uid", uid);
            }
            break;
            case 1: {
                map.put("wx_unionid", uid);
                map.put("weibo_uid", "");
                map.put("qq_uid", "");
            }
            break;
            case 2: {
                map.put("wx_unionid", "");
                map.put("weibo_uid", uid);
                map.put("qq_uid", "");
            }
            break;
        }
        map.put("gender", gender);
        httpHelper.postKeyValuePairAsync(this, URL.OTHER_SIGNUPL, map, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                T.checkNet(LoginActivity.this);
                proUtils.dismiss();
            }

            @Override
            public void onResponse(Call call,Response response) throws IOException {
                String body = response.body().string();
                AnalyData(body);
                proUtils.dismiss();
            }
        }, null);
    }

    private void AnalyData(String body) {
        try {
            JSONObject jo = new JSONObject(body);
            String code = jo.optString(Constant.ERROR_CODE);
            if ("0".equals(code)) {
                aCache.put(Constant.TOKEN, jo.optString(Constant.TOKEN));
                JSONObject user = jo.optJSONObject(Constant.USER);
                JSONArray citys = jo.optJSONArray(Constant.CITYS);
                List<CityBean> cityList = new ArrayList<CityBean>();
                if (citys.length() > 0) {
                    for (int i = 0; i < citys.length(); i++) {
                        JSONObject city = citys.getJSONObject(i);
                        CityBean cityBean = new CityBean();
                        cityBean.setId(city.optString(Constant.ID));
                        cityBean.setName(city.optString(Constant.NAME));
                        cityList.add(cityBean);
                    }
                    aCache.put(Constant.CITYS, (Serializable) cityList);
                }
                UserData data = new UserData();
                data.setId(user.optString(Constant.ID));
                data.setUsername(user.optString(Constant.USERNAME));
                data.setNickname(user.optString(Constant.NICKNAME));
                data.setBalance(user.optString(Constant.BALANCE));
                data.setAvatar(user.optString(Constant.AVATAR));
                data.setBirthday(user.optLong(Constant.BIRTHDAY));
                data.setBaby_birthday(user.optLong(Constant.BABY_BIRTHDAY));
                data.setBaby_gender(user.optString(Constant.BABY_GENDER));
                data.setUsername(user.optString(Constant.USERNAME));
                data.setBaby_name(user.optString(Constant.BABY_NAME));
                data.setMobile(user.optString(Constant.MOBILE));
                data.setAddress(user.optString(Constant.CITY));
                UserUtils.setData(LoginActivity.this, data);
                Intent it = new Intent();
                it.setAction(Constant.UP_USER_DATA);
                sendBroadcast(it);
                finish();
            } else {
                String msg = jo.optString(Constant.ERROR_MSG);
                T.show(LoginActivity.this, msg);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

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
