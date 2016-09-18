package com.dopstore.mall.shop.activity;

import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.TextView;

import com.dopstore.mall.R;
import com.dopstore.mall.activity.adapter.ActivityAdapter;
import com.dopstore.mall.activity.bean.ActivityData;
import com.dopstore.mall.base.BaseActivity;
import com.dopstore.mall.shop.adapter.ShopListAdapter;
import com.dopstore.mall.shop.bean.ShopListData;
import com.dopstore.mall.util.SkipUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 喜成 on 16/9/13
 * name
 */
public class ActivityListActivity extends BaseActivity {
    private TextView firstTv,secondTv,thirdTv,fourTv;
    private View firstv,secondv,thirdv,fourv;
    private ListView listView;
    private List<ActivityData> aList = new ArrayList<ActivityData>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_activity_list);
        initView();
        initData();
    }

    private void initView(){
        setCustomTitle("列表", getResources().getColor(R.color.white_color));
        leftImageBack(R.mipmap.back_arrow);
        firstTv=(TextView) findViewById(R.id.shop_list_first);
        secondTv=(TextView) findViewById(R.id.shop_list_second);
        thirdTv=(TextView) findViewById(R.id.shop_list_third);
        fourTv=(TextView) findViewById(R.id.shop_list_four);
        firstv=findViewById(R.id.shop_list_firstv);
        secondv=findViewById(R.id.shop_list_secondv);
        thirdv=findViewById(R.id.shop_list_thirdv);
        fourv=findViewById(R.id.shop_list_fourv);
        listView=(ListView) findViewById(R.id.shop_list_listview);
        firstTv.setOnClickListener(listener);
        secondTv.setOnClickListener(listener);
        thirdTv.setOnClickListener(listener);
        fourTv.setOnClickListener(listener);
    }

    private void initData(){
        for (int i = 0; i < 4; i++) {
            ActivityData data = new ActivityData();
            data.setId(i + "");
            data.setImage("http://pic51.nipic.com/file/20141022/19779658_171157758000_2.jpg");
            data.setPrice("¥ 156");
            data.setDistance("<3km");
            data.setTitle("欧克破门刻录机");
            aList.add(data);
        }

        listView.setAdapter(new ActivityAdapter(this,aList));

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                SkipUtils.directJump(ActivityListActivity.this,ActivityDetailActivity.class,false);
            }
        });

    }

    View.OnClickListener listener=new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            switch (view.getId()){
                case R.id.shop_list_first:{
                    firstTv.setTextColor(getResources().getColor(R.color.red_color_f93448));
                    firstv.setVisibility(View.VISIBLE);
                    secondTv.setTextColor(getResources().getColor(R.color.gray_color_33));
                    secondv.setVisibility(View.INVISIBLE);
                    thirdTv.setTextColor(getResources().getColor(R.color.gray_color_33));
                    thirdv.setVisibility(View.INVISIBLE);
                    fourTv.setTextColor(getResources().getColor(R.color.gray_color_33));
                    fourv.setVisibility(View.INVISIBLE);

                }break;
                case R.id.shop_list_second:{
                    firstTv.setTextColor(getResources().getColor(R.color.gray_color_33));
                    firstv.setVisibility(View.INVISIBLE);
                    secondTv.setTextColor(getResources().getColor(R.color.red_color_f93448));
                    secondv.setVisibility(View.VISIBLE);
                    thirdTv.setTextColor(getResources().getColor(R.color.gray_color_33));
                    thirdv.setVisibility(View.INVISIBLE);
                    fourTv.setTextColor(getResources().getColor(R.color.gray_color_33));
                    fourv.setVisibility(View.INVISIBLE);

                }break;
                case R.id.shop_list_third:{
                    firstTv.setTextColor(getResources().getColor(R.color.gray_color_33));
                    firstv.setVisibility(View.INVISIBLE);
                    secondTv.setTextColor(getResources().getColor(R.color.gray_color_33));
                    secondv.setVisibility(View.INVISIBLE);
                    thirdTv.setTextColor(getResources().getColor(R.color.red_color_f93448));
                    thirdv.setVisibility(View.VISIBLE);
                    fourTv.setTextColor(getResources().getColor(R.color.gray_color_33));
                    fourv.setVisibility(View.INVISIBLE);

                }break;
                case R.id.shop_list_four:{
                    firstTv.setTextColor(getResources().getColor(R.color.gray_color_33));
                    firstv.setVisibility(View.INVISIBLE);
                    secondTv.setTextColor(getResources().getColor(R.color.gray_color_33));
                    secondv.setVisibility(View.INVISIBLE);
                    thirdTv.setTextColor(getResources().getColor(R.color.gray_color_33));
                    thirdv.setVisibility(View.INVISIBLE);
                    fourTv.setTextColor(getResources().getColor(R.color.red_color_f93448));
                    fourv.setVisibility(View.VISIBLE);

                }break;
            }
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
