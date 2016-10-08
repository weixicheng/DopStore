package com.dopstore.mall.login.activity;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.dopstore.mall.R;
import com.dopstore.mall.activity.MainActivity;
import com.dopstore.mall.base.BaseActivity;
import com.dopstore.mall.util.SkipUtils;
import com.dopstore.mall.util.SpUtils;
import com.dopstore.mall.view.MyScrollLayoutForStart;


/**
 * 欢迎页
 */
public class WelcomePageActivity extends BaseActivity {
    private Button jumpBt;
    private LayoutInflater mInflater;
    private int[] images = {R.mipmap.welcome1,
            R.mipmap.welcome2, R.mipmap.welcome3};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
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


            if (index == images.length - 1) {
                imageView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        SkipUtils.directJump(WelcomePageActivity.this, MainActivity.class, true);
                    }
                });
            } else {
                imageView.setOnClickListener(null);
            }

            imageView.setBackgroundResource(images[index]);

            scrollLayout.addView(view);
        }
        scrollLayout.setPageListener(new MyScrollLayoutForStart.PageListener() {
            @Override
            public void page(int page) {

            }
        });
    }


}
