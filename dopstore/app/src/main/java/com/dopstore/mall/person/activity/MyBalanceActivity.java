package com.dopstore.mall.person.activity;

import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.TextView;

import com.dopstore.mall.R;
import com.dopstore.mall.base.BaseActivity;
import com.dopstore.mall.order.activity.ReChargeActivity;
import com.dopstore.mall.person.adapter.MyBalanceAdapter;
import com.dopstore.mall.person.bean.MyBalanceData;
import com.dopstore.mall.util.Constant;
import com.dopstore.mall.util.SkipUtils;
import com.dopstore.mall.view.MyListView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by 喜成 on 16/9/8.
 * name
 */
public class MyBalanceActivity extends BaseActivity {
    private TextView numTv;
    private MyListView listView;
    private List<MyBalanceData> items = new ArrayList<MyBalanceData>();
    private String numStr="";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_balance);
        initView();
        initData();
    }

    private void initView() {
        setCustomTitle("我的余额", getResources().getColor(R.color.white_color));
//        rightTextBack("交易明细", getResources().getColor(R.color.white_color), listener);
        ImageButton id = (ImageButton) findViewById(R.id.title_left_imageButton);
        id.setBackgroundResource(R.mipmap.back_arrow);
        id.setVisibility(View.VISIBLE);
        id.setOnClickListener(listener);
        numTv = (TextView) findViewById(R.id.my_balance_num);
        listView = (MyListView) findViewById(R.id.my_balance_lv);
    }

    private void initData() {
        Map<String, Object> map = SkipUtils.getMap(this);
        String balance = map.get(Constant.BALANCE).toString();
        numTv.setText(balance);
        final String[] num = {"500", "1000", "2000", "5000"};
        for (int i = 0; i < num.length; i++) {
            MyBalanceData data = new MyBalanceData();
            data.setTitle(num[i]);
            data.setIntro("充值1000送200,返海豚币1000元");
            items.add(data);
        }
        listView.setAdapter(new MyBalanceAdapter(this, items));
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Map<String,Object> map=new HashMap<String, Object>();
                numStr=items.get(position).getTitle();
                map.put(Constant.PRICE,numStr);
                SkipUtils.jumpForMap(MyBalanceActivity.this, ReChargeActivity.class,map,true);
            }
        });
    }


    View.OnClickListener listener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.title_right_textButton: {

                }
                break;
                case R.id.title_left_imageButton: {
                    back();
                }
                break;
            }
         }
    };

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            back();
            return true;
        } else {
            return super.onKeyDown(keyCode, event);
        }
    }

    private void back() {
        String num = numTv.getText().toString();
        Map<String, Object> map = new HashMap<>();
        map.put(Constant.BALANCE, num);
        SkipUtils.backForMapResult(this, map);
    }


}
