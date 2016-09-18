package com.dopstore.mall.login.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.dopstore.mall.R;
import com.dopstore.mall.activity.MainActivity;
import com.dopstore.mall.activity.bean.CityBean;
import com.dopstore.mall.activity.bean.UserData;
import com.dopstore.mall.base.BaseActivity;
import com.dopstore.mall.login.Adapter.BabyAdapter;
import com.dopstore.mall.login.bean.DetailData;
import com.dopstore.mall.time.TimePopupWindow;
import com.dopstore.mall.util.ACache;
import com.dopstore.mall.util.Constant;
import com.dopstore.mall.util.HttpHelper;
import com.dopstore.mall.util.SkipUtils;
import com.dopstore.mall.util.T;
import com.dopstore.mall.util.URL;
import com.dopstore.mall.util.UserUtils;
import com.dopstore.mall.util.Utils;
import com.dopstore.mall.view.CircleImageView;
import com.dopstore.mall.view.MyGridView;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by 喜成 on 16/9/6.
 * name 注册资料页
 */
public class RegisterDetailActivity extends BaseActivity {
    private RelativeLayout topLayout;
    private MyGridView hobbyGridView;
    private TextView timeTv;
    private CircleImageView manImage, womanImage;
    private LinearLayout bottomBt;
    private int sexType = 1;
    private List<DetailData> list = new ArrayList<DetailData>();
    private HttpHelper httpHelper;
    private String v_code;
    private String pwd;
    private String mobile;
    private BabyAdapter adapter;
    private ACache aCache;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_detail);
        initView();
        initData();
    }

    private void initView() {
        aCache = ACache.get(this);
        httpHelper = HttpHelper.getOkHttpClientUtils(this);
        Map<String, Object> intentMap = SkipUtils.getMap(this);
        list = (List<DetailData>) intentMap.get(Constant.LIST);
        v_code = intentMap.get(Constant.V_CODE).toString();
        pwd = intentMap.get(Constant.PASSWORD).toString();
        mobile = intentMap.get(Constant.MOBILE).toString();
        topLayout = (RelativeLayout) findViewById(R.id.brandsquare_title);
        topLayout.setBackgroundColor(getResources().getColor(R.color.white_color));
        leftTextBack("取消", getResources().getColor(R.color.red_color_f93448), listener);
        timeTv = (TextView) findViewById(R.id.register_mybaby_time);
        hobbyGridView = (MyGridView) findViewById(R.id.register_mybaby_hobby);
        bottomBt = (LinearLayout) findViewById(R.id.register_mybaby_complete_lly);
        manImage = (CircleImageView) findViewById(R.id.register_detail_man);
        womanImage = (CircleImageView) findViewById(R.id.register_detail_woman);
        manImage.setOnClickListener(listener);
        womanImage.setOnClickListener(listener);
        timeTv.setOnClickListener(listener);
        bottomBt.setOnClickListener(listener);
    }

    private void initData() {
        Calendar calendar = Calendar.getInstance();
        timeTv.setText(calendar.get(Calendar.YEAR) + "-" +
                (calendar.get(Calendar.MONTH) + 1) + "-" +
                calendar.get(Calendar.DAY_OF_MONTH) + "");
        refreshAdapter();
        hobbyGridView.setAdapter(new BabyAdapter(this, list));

        hobbyGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                list.get(i).setIsSelect("1");
                adapter.notifyDataSetChanged();
            }
        });
    }

    private void refreshAdapter() {
        if (adapter == null) {
            adapter = new BabyAdapter(this, list);
            hobbyGridView.setAdapter(adapter);
        } else {
            adapter.upDataList(list);
        }
    }

    View.OnClickListener listener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.title_left_textView: {//取消
                    SkipUtils.back(RegisterDetailActivity.this);
                }
                break;
                case R.id.register_detail_man: {//男
                    sexType = 1;
                    manImage.setImageResource(R.mipmap.select_head_press);
                    womanImage.setImageResource(0);
                }
                break;
                case R.id.register_detail_woman: {//女
                    sexType = 0;
                    manImage.setImageResource(0);
                    womanImage.setImageResource(R.mipmap.select_head_press);
                }
                break;
                case R.id.register_mybaby_time: {//时间
                    showTimePop();
                }
                break;
                case R.id.register_mybaby_complete_lly: {//完成
                    registData();
                }
                break;

            }
        }
    };


    private void registData() {
        String num = "";
        for (int i = 0; i < list.size(); i++) {
            num = list.get(i).getId() + ",";
            num += num;
        }
        String str = "[" + num.substring(0, num.length() - 1) + "]";

        String time = timeTv.getText().toString();
        Map<String, String> map = new HashMap<String, String>();
        map.put(Constant.MOBILE, mobile);
        map.put(Constant.V_CODE, v_code);
        map.put(Constant.PASSWORD, pwd);
        map.put(Constant.BABY_NAME, "");
        map.put(Constant.BABY_GENDER, sexType + "");
        map.put(Constant.BABY_HOBBY, str);
        map.put(Constant.BABY_BIRTHDAY, time);
        httpHelper.postKeyValuePairAsync(this, URL.SIGN_UP, map, new Callback() {
            @Override
            public void onFailure(Request request, IOException e) {
            }

            @Override
            public void onResponse(Response response) throws IOException {
                String body = response.body().string();
                try {
                    JSONObject jo = new JSONObject(body);
                    String code = jo.optString(Constant.ERROR_CODE);
                    if ("0".equals(code)) {
                        Constant.TOKEN_VALUE = jo.optString(Constant.TOKEN);
                        JSONObject user = jo.optJSONObject(Constant.USER);
                        JSONArray citys = jo.optJSONArray(Constant.CITYS);
                        List<CityBean> cityList = new ArrayList<CityBean>();
                        if (citys.length() > 0) {
                            for (int i = 0; i < citys.length(); i++) {
                                JSONObject city = citys.getJSONObject(i);
                                CityBean cityBean = new CityBean();
                                cityBean.setId(city.optString(Constant.ID));
                                cityBean.setName(city.optString(Constant.NAME));
                                cityList.add(cityBean);
                            }
                            aCache.put(Constant.CITYS, (Serializable) cityList);
                        }
                        UserData data = new UserData();
                        data.setId(user.optString(Constant.ID));
                        data.setUsername(user.optString(Constant.USERNAME));
                        data.setNickname(user.optString(Constant.NICKNAME));
                        data.setBalance(user.optString(Constant.BALANCE));
                        data.setAvatar(user.optString(Constant.AVATAR));
                        data.setBirthday(user.optLong(Constant.BIRTHDAY));
                        data.setBaby_birthday(user.optLong(Constant.BABY_BIRTHDAY));
                        data.setBaby_gender(user.optString(Constant.BABY_GENDER));
                        data.setUsername(user.optString(Constant.USERNAME));
                        data.setBaby_name(user.optString(Constant.BABY_NAME));
                        data.setMobile(user.optString(Constant.MOBILE));
                        data.setAddress(user.optString(Constant.CITY));
                        UserUtils.setData(RegisterDetailActivity.this, data);
                        Intent intent = new Intent();
                        intent.setAction(Constant.UPDATA_USER_FLAG);
                        sendBroadcast(intent);
                        Intent it = new Intent(RegisterDetailActivity.this, MainActivity.class);
                        it.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(it);
                        finish();
                        overridePendingTransition(R.anim.return_from_click, R.anim.return_out_click);
                    } else {
                        String msg = jo.optString(Constant.ERROR_MSG);

                        T.show(RegisterDetailActivity.this, msg);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, null);

    }


    /**
     * 显示时间选择器
     */
    private void showTimePop() {
        TimePopupWindow timePopupWindow = new TimePopupWindow(this, TimePopupWindow.Type.YEAR_MONTH_DAY);
        Date curDate = new Date(System.currentTimeMillis());//获取当前时间
        timePopupWindow.showAtLocation(this.findViewById(R.id.register_mybaby_layout), Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0, curDate);
        timePopupWindow.setOnTimeSelectListener(new TimePopupWindow.OnTimeSelectListener() {
            @Override
            public void onTimeSelect(Date date) {
                SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
                String timeStr = formatter.format(date);
                timeTv.setText(timeStr);
            }
        });
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
