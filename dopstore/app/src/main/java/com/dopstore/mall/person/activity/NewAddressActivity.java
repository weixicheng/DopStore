package com.dopstore.mall.person.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.dopstore.mall.R;
import com.dopstore.mall.base.BaseActivity;
import com.dopstore.mall.util.SkipUtils;


/**
 * 新建地址
 */
public class NewAddressActivity extends BaseActivity {
    private LinearLayout mProvince_onclick, mKeep_addrees;
    private TextView mProvince_city_area, hintV;
    private ImageView mLv_click;
    private boolean clcik_address = true;
    private EditText mAddress, Address_name, Address_phone, card_num;
    private String address_id;
    private Button saveBt, deleBt;
    private PopupWindow popupWindow;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_address);
        initView();
    }

    private void initView() {
        setCustomTitle("新增收货地址", getResources().getColor(R.color.white_color));
        leftImageBack(R.mipmap.back_arrow);

        mProvince_onclick = (LinearLayout) findViewById(R.id.new_address_city);
        mKeep_addrees = (LinearLayout) findViewById(R.id.new_address_default);
        mProvince_city_area = (TextView) findViewById(R.id.new_address_city_tv);
        hintV = (TextView) findViewById(R.id.new_address_hint);
        mAddress = (EditText) findViewById(R.id.new_address_detail);
        card_num = (EditText) findViewById(R.id.new_address_card);
        Address_name = (EditText) findViewById(R.id.new_address_name);
        Address_phone = (EditText) findViewById(R.id.new_address_phone);
        mLv_click = (ImageView) findViewById(R.id.new_address_default_click);
        saveBt = (Button) findViewById(R.id.new_address_save);
        deleBt = (Button) findViewById(R.id.new_address_delete);
        mProvince_onclick.setOnClickListener(listener);
        mKeep_addrees.setOnClickListener(listener);
    }

    OnClickListener listener = new OnClickListener() {
        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.new_address_city: {
                    showPop();
                }
                break;
                case R.id.new_address_default: {
                    if (clcik_address) {
                        mLv_click.setImageResource(R.mipmap.checkbox_normal);
                        clcik_address = false;
                    } else {
                        mLv_click.setImageResource(R.mipmap.checkbox_checked);
                        clcik_address = true;
                    }
                }
                break;
            }

        }
    };

    private void showPop() {

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);
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
