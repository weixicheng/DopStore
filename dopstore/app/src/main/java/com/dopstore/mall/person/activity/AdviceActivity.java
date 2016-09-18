package com.dopstore.mall.person.activity;

import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.dopstore.mall.R;
import com.dopstore.mall.base.BaseActivity;
import com.dopstore.mall.util.SkipUtils;

/**
 * Created by 喜成 on 16/9/12.
 * name
 */
public class AdviceActivity extends BaseActivity {
    private EditText adviceEt,postEt;
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
