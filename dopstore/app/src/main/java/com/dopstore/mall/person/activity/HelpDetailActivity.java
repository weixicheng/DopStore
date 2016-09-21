package com.dopstore.mall.person.activity;

import android.os.Bundle;
import android.view.KeyEvent;
import android.widget.TextView;

import com.dopstore.mall.R;
import com.dopstore.mall.base.BaseActivity;
import com.dopstore.mall.person.bean.HelpData;
import com.dopstore.mall.util.Constant;
import com.dopstore.mall.util.SkipUtils;

import java.util.Map;

/**
 * Created by 喜成 on 16/9/12.
 * name
 */
public class HelpDetailActivity extends BaseActivity {
    private TextView tv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_help_detail);
        initView();
    }

    private void initView() {
        leftImageBack(R.mipmap.back_arrow);
        tv = (TextView) findViewById(R.id.help_detail_content_text);
        Map<String,Object> map=SkipUtils.getMap(this);
        if (map!=null){
            HelpData data=(HelpData) map.get(Constant.LIST);
            setCustomTitle(data.getTitle(), getResources().getColor(R.color.white_color));
            tv.setText(data.getContent());
        }else {
            SkipUtils.back(this);
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
