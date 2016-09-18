package com.dopstore.mall.login.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.dopstore.mall.R;
import com.dopstore.mall.activity.MainActivity;
import com.dopstore.mall.activity.bean.CityBean;
import com.dopstore.mall.activity.bean.UserData;
import com.dopstore.mall.base.BaseActivity;
import com.dopstore.mall.util.ACache;
import com.dopstore.mall.util.Constant;
import com.dopstore.mall.util.HttpHelper;
import com.dopstore.mall.util.SkipUtils;
import com.dopstore.mall.util.T;
import com.dopstore.mall.util.URL;
import com.dopstore.mall.util.UserUtils;
import com.dopstore.mall.util.Utils;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


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
    private HttpHelper httpHelper;
    private ACache aCache;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        initView();
        initData();
    }

    private void initView() {
        httpHelper = HttpHelper.getOkHttpClientUtils(this);
        aCache = ACache.get(this);
        topLayout = (RelativeLayout) findViewById(R.id.brandsquare_title);
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
    }

    private void initData() {

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
                    T.show(LoginActivity.this, "微信");
                }
                break;
                case R.id.login_qq_iv: {//QQ
                    T.show(LoginActivity.this, "QQ");
                }
                break;
                case R.id.login_sina_iv: {//新浪
                    T.show(LoginActivity.this, "新浪");
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
        Map<String, String> map = new HashMap<String, String>();
        map.put(Constant.MOBILE, phone);
        map.put(Constant.PASSWORD, pwd);
        httpHelper.postKeyValuePairAsync(this, URL.LOGIN, map, new Callback() {
            @Override
            public void onFailure(Request request, IOException e) {
            }

            @Override
            public void onResponse(Response response) throws IOException {
                String body = response.body().string();
                try {
                    JSONObject jo = new JSONObject(body);
                    String code = jo.optString(Constant.ERROR_CODE);
                    if ("0".equals(code)) {
                        Constant.TOKEN_VALUE = jo.optString(Constant.TOKEN);
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
                        Intent intent = new Intent();
                        intent.setAction(Constant.UPDATA_USER_FLAG);
                        sendBroadcast(intent);
                        SkipUtils.directJump(LoginActivity.this, MainActivity.class, true);
                    } else {
                        String msg = jo.optString(Constant.ERROR_MSG);

                        T.show(LoginActivity.this, msg);

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, null);
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
