package com.dopstore.mall.shop.activity;

import android.graphics.Color;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.dopstore.mall.R;
import com.dopstore.mall.activity.adapter.HomeAdImageAdapter;
import com.dopstore.mall.activity.adapter.MiddleDataAdapter;
import com.dopstore.mall.activity.bean.ActivityData;
import com.dopstore.mall.activity.bean.CarouselData;
import com.dopstore.mall.activity.bean.ShopData;
import com.dopstore.mall.base.BaseActivity;
import com.dopstore.mall.util.Constant;
import com.dopstore.mall.util.HttpHelper;
import com.dopstore.mall.util.ProUtils;
import com.dopstore.mall.util.SkipUtils;
import com.dopstore.mall.util.T;
import com.dopstore.mall.util.URL;
import com.dopstore.mall.util.UserUtils;
import com.dopstore.mall.view.EScrollView;
import com.dopstore.mall.view.scrollview.DetailMenu;
import com.dopstore.mall.view.rollviewpager.RollPagerView;
import com.dopstore.mall.view.rollviewpager.hintview.IconHintView;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 作者：xicheng on 16/9/13
 */
public class ActivityDetailActivity extends BaseActivity {
    private RelativeLayout bottomLy;
    private HttpHelper httpHelper;
    private ProUtils proUtils;
    private ActivityData activityData;
    private String isCollect="1";
    private DetailMenu detailMenu;
    private RollPagerView rollPagerView;
    private TextView titleTv,priceTv,numTv,timeTv,ageTv,addressTv,phoneTv;
    private EScrollView eScrollView;
    private RelativeLayout titleLayout;


    private List<CarouselData> titleAdvertList = new ArrayList<CarouselData>();
    private List<ShopData> datas=new ArrayList<ShopData>();



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_activity_detail);
        initView();
        initData();
    }

    private void initView() {
        httpHelper=HttpHelper.getOkHttpClientUtils(this);
        proUtils=new ProUtils(this);
        Map<String,Object> map=SkipUtils.getMap(this);
        if (map==null)return;
        activityData=(ActivityData) map.get(Constant.LIST);
        detailMenu = (DetailMenu) findViewById(R.id.activity_detail_content_ly);
        rollPagerView = (RollPagerView) findViewById(R.id.roll_view_pager);
        titleTv = (TextView) findViewById(R.id.activity_detail_title_name);
        priceTv = (TextView) findViewById(R.id.activity_detail_price);
        numTv = (TextView) findViewById(R.id.activity_detail_num);
        timeTv = (TextView) findViewById(R.id.activity_detail_time);
        ageTv = (TextView) findViewById(R.id.activity_detail_age);
        addressTv = (TextView) findViewById(R.id.activity_detail_address);
        phoneTv = (TextView) findViewById(R.id.activity_detail_phone);
        eScrollView = (EScrollView) findViewById(R.id.activity_detail_about_scrollview);
        bottomLy = (RelativeLayout) findViewById(R.id.activity_detail_bottom_layout);
        bottomLy.setOnClickListener(listener);
        setTopBg(getResources().getColor(R.color.transparent));
        setCustomTitle("活动详情", getResources().getColor(R.color.white_color));
        leftImageBack(R.mipmap.back_arrow);
        rightFirstImageBack(R.mipmap.share_logo,listener);
        rightSecondImageBack(R.mipmap.collect_small_logo,listener);
        detailMenu.openMenu();
    }

    private void initData() {

        for (int i=0;i<5;i++){
            CarouselData data=new CarouselData();
            data.setPicture("http://www.taopic.com/uploads/allimg/120421/107063-12042114025737.jpg");
            data.setId(i+"");
            titleAdvertList.add(data);
        }

        titleTv.setText("海岛五日游,上海-仁川 5日4晚");
        priceTv.setText("免费");
        numTv.setText("限制20人");
        timeTv.setText("2016年03月05日——2016年04月05日");
        ageTv.setText("3——6岁");
        addressTv.setText("喝吧");
        phoneTv.setText("010-22222222");

        for (int i=0;i<9;i++){
            ShopData shopData=new ShopData();
            shopData.setId(i+"");
            shopData.setPrice("123");
            shopData.setCover("http://www.taopic.com/uploads/allimg/120421/107063-12042114025737.jpg");
            shopData.setName("sdjk");
            datas.add(shopData);
        }
        eScrollView.setAdapter(new MiddleDataAdapter(this, datas));
        eScrollView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                ShopData data=datas.get(i);
                Map<String,Object> map=new HashMap<String, Object>();
                map.put(Constant.LIST,data);
                SkipUtils.jumpForMap(ActivityDetailActivity.this, ShopDetailActivity.class,map,false);
            }
        });

        setAdvertisementData();
    }

    View.OnClickListener listener=new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            switch (view.getId()){
                case R.id.title_right_imageButton:{//分享
                    }break;
                case R.id.title_right_before_imageButton:{//收藏
                    if ("1".equals(isCollect)){
                        isCollect="2";
                        getCollectStatus(isCollect);
                    }else {
                        isCollect="1";
                        getCollectStatus(isCollect);
                    }
                }break;
                case R.id.activity_detail_bottom_layout:{
                    SkipUtils.directJump(ActivityDetailActivity.this,ConfirmActivityActivity.class,false);
                }break;
            }

        }
    };

    private void getCollectStatus(final String isCollect) {
        proUtils.show();
        Map<String, String> map = new HashMap<String, String>();
        map.put("user_id", UserUtils.getId(this));
        map.put("item_id", activityData.getId());
        map.put("action_id", isCollect);
        httpHelper.postKeyValuePairAsync(this, URL.COLLECTION_EDIT, map, new Callback() {
            @Override
            public void onFailure(Request request, IOException e) {
                T.checkNet(ActivityDetailActivity.this);
                proUtils.dismiss();
            }

            @Override
            public void onResponse(Response response) throws IOException {
                String body = response.body().string();
                try {
                    JSONObject jo = new JSONObject(body);
                    String code = jo.optString(Constant.ERROR_CODE);
                    if ("0".equals(code)) {
                        if ("1".equals(isCollect)) {
                            T.show(ActivityDetailActivity.this, "添加成功");
                        }else {
                            T.show(ActivityDetailActivity.this, "取消成功");
                        }
                    } else {
                        String msg = jo.optString(Constant.ERROR_MSG);
                        T.show(ActivityDetailActivity.this, msg);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                proUtils.dismiss();
            }
        }, null);
    }



    public void onResume() {
        super.onResume();

    }

    public void onPause() {
        super.onPause();

    }

    /**
     * 设置轮播
     */
    private void setAdvertisementData() {
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay()
                .getMetrics(dm);
        // 设置图片宽高
        int screenWidth = getWindowManager()
                .getDefaultDisplay().getWidth();
        final int picSize = screenWidth / 2;
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                screenWidth, picSize);
        rollPagerView.setLayoutParams(layoutParams);
        rollPagerView.pause();

        if (titleAdvertList != null) {
            //设置播放时间间隔
            rollPagerView.setPlayDelay(1000);
            //设置透明度
            rollPagerView.setAnimationDurtion(500);
            //设置适配器
            rollPagerView.setAdapter(new HomeAdImageAdapter(this, titleAdvertList));
            rollPagerView.setHintView(new IconHintView(this, R.mipmap.dop_press, R.mipmap.dop_normal));
            if (titleAdvertList.size() == 1) {
                rollPagerView.setHintView(null);
            }
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
