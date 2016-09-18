package com.dopstore.mall.person.activity;

import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;

import com.dopstore.mall.R;
import com.dopstore.mall.base.BaseActivity;
import com.dopstore.mall.util.SkipUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by 喜成 on 16/9/8.
 * name
 */
public class ModifyNickActivity extends BaseActivity {
    private EditText nameEdit;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modify_nick);
        initView();
    }

    private void initView() {
        setCustomTitle("修改", getResources().getColor(R.color.white_color));
        leftImageBack(R.mipmap.back_arrow);
        rightTextBack("确定", getResources().getColor(R.color.white_color), listener);
        nameEdit = (EditText) findViewById(R.id.modify_nick_edit);
        String name = SkipUtils.getMap(this).get("name").toString();
        nameEdit.setText(name);
        nameEdit.setSelection(name.length());
    }

    View.OnClickListener listener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            back();
        }
    };

    private void back() {
        String name = nameEdit.getText().toString();
        Map<String, Object> map = new HashMap<>();
        map.put("name", name);
        SkipUtils.backForMapResult(this, map);
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
