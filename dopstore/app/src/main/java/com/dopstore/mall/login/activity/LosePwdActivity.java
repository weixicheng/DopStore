package com.dopstore.mall.login.activity;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.Html;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.dopstore.mall.R;
import com.dopstore.mall.base.BaseActivity;
import com.dopstore.mall.util.Constant;
import com.dopstore.mall.util.HttpHelper;
import com.dopstore.mall.util.ProUtils;
import com.dopstore.mall.util.SkipUtils;
import com.dopstore.mall.util.T;
import com.dopstore.mall.util.URL;
import com.dopstore.mall.util.Utils;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

/**
 * 作者：xicheng on 16/7/30 15:18
 * 类别：忘记密码
 */
public class LosePwdActivity extends BaseActivity {
    private LinearLayout thirdLayout;
    private RelativeLayout topLayout;
    private EditText phoneEt, codeEt, pwdEt;
    private Button getBt, registBt;
    private TextView agressTxt, titleTv;
    private MyCount mc;
    private String titleStr = "忘记密码";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        initView();
    }

    private void initView() {
        Map<String, Object> map = SkipUtils.getMap(this);
        if (map != null) {
            String saftStr = map.get("title").toString();
            if ("safe".equals(saftStr)) {
                titleStr = "重置密码";
            }
        }
        topLayout = (RelativeLayout) findViewById(R.id.brandsquare_title_layout);
        titleTv = (TextView) findViewById(R.id.register_title_tv);
        titleTv.setText(titleStr);
        thirdLayout = (LinearLayout) findViewById(R.id.login_third_lly);
        thirdLayout.setVisibility(View.GONE);
        topLayout.setBackgroundColor(getResources().getColor(R.color.white_color));
        leftTextBack("取消", getResources().getColor(R.color.red_color_f93448), listener);
        phoneEt = (EditText) findViewById(R.id.register_phone_Et);
        codeEt = (EditText) findViewById(R.id.register_code_Et);
        pwdEt = (EditText) findViewById(R.id.register_set_pwd_Et);
        getBt = (Button) findViewById(R.id.register_get_code_bt);
        agressTxt = (TextView) findViewById(R.id.register_getvoice_bt);
        registBt = (Button) findViewById(R.id.register_submit_bt);
        registBt.setText("修改密码");
        getBt.setOnClickListener(listener);
        registBt.setOnClickListener(listener);
        agressTxt.setOnClickListener(listener);
        agressTxt.setText(Html.fromHtml("如果没有收到验证码,请点击这里<font color='#f93448'>获取语音验证码</font>"));
    }

    View.OnClickListener listener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.title_left_textView: {//取消
                    SkipUtils.back(LosePwdActivity.this);
                }
                break;
                case R.id.register_get_code_bt: {//获取验证码
                    getPhoneCode();
                }
                break;
                case R.id.register_submit_bt: {//提交并登录
                    registData();
                }
                break;
                case R.id.register_getvoice_bt: {//获取语音验证码
                    String phone = phoneEt.getText().toString().trim();
                    if (TextUtils.isEmpty(phone)) {
                        T.show(LosePwdActivity.this, "请填写手机号");
                    } else {
                        if (!Utils.isPhoneNumberValid(phone)) {
                            T.show(LosePwdActivity.this, "请检查手机号");
                        } else {
                            getVoiceCode(phone);
                        }
                    }
                }
                break;
            }
        }
    };

    private void getVoiceCode(String phone) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put(Constant.MOBILE, phone);
        httpHelper.postKeyValuePairAsync(this, URL.SEND_VOICE_CODE, map, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                T.checkNet(LosePwdActivity.this);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String body = response.body().string();
                try {
                    JSONObject jo = new JSONObject(body);
                    String code = jo.optString(Constant.ERROR_CODE);
                    if ("0".equals(code)) {
                    } else {
                        String msg = jo.optString(Constant.ERROR_MSG);
                        T.show(LosePwdActivity.this, msg);

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, null);

    }

    /**
     * 获取验证码
     */
    private void getPhoneCode() {
        String phone = phoneEt.getText().toString().trim();
        if (TextUtils.isEmpty(phone)) {
            T.show(this, "请填写手机号");
        } else {
            if (!Utils.isPhoneNumberValid(phone)) {
                T.show(this, "请检查手机号");
            } else {
                mc = new MyCount(60000, 1000);
                mc.start();
                getBt.setEnabled(false);
                getCode(phone);
            }
        }
    }

    /**
     * 注册
     */
    private void registData() {
        String phone = phoneEt.getText().toString().trim();
        String code = codeEt.getText().toString().trim();
        String pwd = pwdEt.getText().toString().trim();

        checkPhone(phone);

        if (TextUtils.isEmpty(code)) {
            T.show(this, "请填写验证码");
            return;
        }

        if (TextUtils.isEmpty(pwd)) {
            T.show(this, "请填写密码");
            return;
        }

        if (pwd.length() < 6) {
            T.show(this, "密码长度小于6位");
            return;
        }

        upToService(phone, code, pwd);
    }

    /**
     * 检查手机号
     *
     * @param phone
     */
    private void checkPhone(String phone) {
        if (TextUtils.isEmpty(phone)) {
            T.show(this, "请填写手机号");
            return;
        }

        if (!Utils.isPhoneNumberValid(phone)) {
            T.show(this, "请检查手机号");
            return;
        }
    }

    /**
     * 验证码计时器
     */
    class MyCount extends CountDownTimer {

        public MyCount(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);
        }

        @Override
        public void onTick(long millisUntilFinished) {
            getBt.setText("(" + millisUntilFinished / 1000 + "s)重新发送");
        }

        @Override
        public void onFinish() {
            getBt.setText("获取验证码");
            getBt.setEnabled(true);
        }
    }

    /**
     * 获取验证码
     *
     * @param phone
     */
    private void getCode(String phone) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put(Constant.MOBILE, phone);
        httpHelper.postKeyValuePairAsync(this, URL.SEND_V_CODE, map, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                T.checkNet(LosePwdActivity.this);
            }

            @Override
            public void onResponse(Call call,Response response) throws IOException {
                String body = response.body().string();
                try {
                    JSONObject jo = new JSONObject(body);
                    String code = jo.optString(Constant.ERROR_CODE);
                    if ("0".equals(code)) {

                    } else {
                        String msg = jo.optString(Constant.ERROR_MSG);
                        T.show(LosePwdActivity.this, msg);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, null);
    }

    /**
     * 上传服务器 找回成功登录首页
     *
     * @param phone
     * @param code
     * @param pwd
     */
    private void upToService(String phone, String code, String pwd) {
        proUtils.show();
        Map<String, Object> map = new HashMap<String, Object>();
        map.put(Constant.MOBILE, phone);
        map.put(Constant.PASSWORD, pwd);
        map.put(Constant.V_CODE, code);
        httpHelper.postKeyValuePairAsync(this, URL.RESET_PASSWORD, map, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                T.checkNet(LosePwdActivity.this);
                proUtils.dismiss();
            }

            @Override
            public void onResponse(Call call,Response response) throws IOException {
                String body = response.body().string();
                try {
                    JSONObject jo = new JSONObject(body);
                    String code = jo.optString(Constant.ERROR_CODE);
                    if ("0".equals(code)) {
                        T.show(LosePwdActivity.this, "密码更改成功");
                        SkipUtils.back(LosePwdActivity.this);
                    } else {
                        String msg = jo.optString(Constant.ERROR_MSG);
                        T.show(LosePwdActivity.this, msg);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                proUtils.dismiss();
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
