package com.dopstore.mall.activity;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.dopstore.mall.R;
import com.dopstore.mall.base.BaseActivity;
import com.dopstore.mall.util.SkipUtils;
import com.dopstore.mall.util.SpUtils;
import com.dopstore.mall.view.CustomIndicatorStart;
import com.dopstore.mall.view.MyScrollLayoutForStart;


/**
 * 欢迎页
 */
public class WelcomePageActivity extends BaseActivity {
    private Button jumpBt;
    private String activityStr;
    private LayoutInflater mInflater;
    private int[] images = {R.mipmap.ic_launcher,
            R.mipmap.ic_launcher, R.mipmap.ic_launcher};
    private LinearLayout scrollIdentifying;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome_page);
        initView();
        initData();

    }


    public void initView() {
        jumpBt = (Button) findViewById(R.id.before_start_jump_bt);
        mInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        boolean flag = (Boolean) SpUtils.getParam(this, "boolean", false);
        if (flag) {
            SkipUtils.directJump(WelcomePageActivity.this, MainActivity.class, true);
        }
        scrollIdentifying = (LinearLayout) findViewById(R.id.before_start_viewIndentifier);
        jumpBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SpUtils.setParam(WelcomePageActivity.this, "boolean", true);
                SkipUtils.directJump(WelcomePageActivity.this, MainActivity.class, true);
            }
        });

    }


    public void initData() {
        initScrollLayout();
    }


    protected void initScrollLayout() {
        // TODO Auto-generated method stub
        MyScrollLayoutForStart scrollLayout = (MyScrollLayoutForStart) findViewById(R.id.before_start_img_scrollLayout);
        scrollLayout.setIsScroll(false);
        // 现在是一张图片，多张后再做处理
        int scrollLayoutAcount = 0;
        if (images.length > 0) {
            scrollLayoutAcount = images.length;
        } else {
            scrollLayoutAcount = 1;
        }

        for (int index = 0; index < scrollLayoutAcount; index++) {
            View view = mInflater.inflate(R.layout.layout_single_image, null);
            ImageView imageView = (ImageView) view.findViewById(R.id.single_image_iv);


//			if (index == images.length - 1) {
//				before_start_btn.setVisibility(View.VISIBLE);
//			} else {
//				before_start_btn.setVisibility(View.GONE);
//			}

            imageView.setBackgroundResource(images[index]);

            scrollLayout.addView(view);
        }

        // 初始化指示器
        CustomIndicatorStart customIndicator = new CustomIndicatorStart(WelcomePageActivity.this, images.length, scrollLayout, scrollIdentifying);
        customIndicator.initIndicator(90, R.mipmap.ic_launcher,
                new CustomIndicatorStart.OnScrollPageListener() {

                    @Override
                    public void currentPage(int currPage) {
                        // TODO Auto-generated method stub
                        // currentPos = currPage;
                    }
                });

    }


}
