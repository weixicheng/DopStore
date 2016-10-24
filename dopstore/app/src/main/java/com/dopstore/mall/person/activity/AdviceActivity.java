package com.dopstore.mall.person.activity;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.dopstore.mall.R;
import com.dopstore.mall.base.BaseActivity;
import com.dopstore.mall.util.CommHttp;
import com.dopstore.mall.util.Constant;
import com.dopstore.mall.util.SkipUtils;
import com.dopstore.mall.util.T;
import com.dopstore.mall.util.URL;
import com.dopstore.mall.util.UserUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;


/**
 * Created by 喜成 on 16/9/12.
 * name
 */
public class AdviceActivity extends BaseActivity {
    private EditText adviceEt, postEt;
    private Button submit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_advice);
        initView();
        initData();
    }

    private void initView() {
        setCustomTitle("意见反馈", getResources().getColor(R.color.white_color));
        leftImageBack(R.mipmap.back_arrow);
        adviceEt = (EditText) findViewById(R.id.advice_input_et);
        postEt = (EditText) findViewById(R.id.advice_input_post_et);
        submit = (Button) findViewById(R.id.advice_submit_bt);
        submit.setOnClickListener(listener);
    }


    private void initData() {


    }

    View.OnClickListener listener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            String adviceStr = adviceEt.getText().toString().trim();
            String postStr = postEt.getText().toString().trim();
            if (TextUtils.isEmpty(adviceStr)) {
                T.show(AdviceActivity.this, "请输入您的宝贵意见");
                return;
            }
            if (TextUtils.isEmpty(postStr)) {
                T.show(AdviceActivity.this, "请留下您的联系方式(QQ/邮箱/电话号码)");
                return;
            }
            adviceToUs(adviceStr, postStr);
        }
    };

    private void adviceToUs(String adviceStr, String postStr) {
        String id = UserUtils.getId(this);
        Map<String, Object> map = new HashMap<String, Object>();
        map.put(Constant.CONTENT, adviceStr);
        map.put(Constant.INFORMATION, postStr);
        httpHelper.post(this, URL.SHIPPINGADDRESS + id + "/add_feedback", map, new CommHttp.HttpCallBack() {
            @Override
            public void success(String body) {
                try {
                    JSONObject jo = new JSONObject(body);
                    String code = jo.optString(Constant.ERROR_CODE);
                    if ("0".equals(code)) {
                        T.show(AdviceActivity.this, "提交成功");
                        handler.sendEmptyMessageDelayed(0, 300);

                    } else {
                        String msg = jo.optString(Constant.ERROR_MSG);
                        T.show(AdviceActivity.this, msg);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void failed(String msg) {
                T.checkNet(AdviceActivity.this);
            }
        });
    }

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            SkipUtils.back(AdviceActivity.this);
        }
    };


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
