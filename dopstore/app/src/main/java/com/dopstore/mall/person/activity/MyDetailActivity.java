package com.dopstore.mall.person.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.dopstore.mall.R;
import com.dopstore.mall.activity.bean.CityBean;
import com.dopstore.mall.activity.bean.UserData;
import com.dopstore.mall.base.BaseActivity;
import com.dopstore.mall.time.TimePopupWindow;
import com.dopstore.mall.util.ACache;
import com.dopstore.mall.util.Constant;
import com.dopstore.mall.util.HttpHelper;
import com.dopstore.mall.util.LoadImageUtils;
import com.dopstore.mall.util.PopupUtils;
import com.dopstore.mall.util.ProUtils;
import com.dopstore.mall.util.SkipUtils;
import com.dopstore.mall.util.T;
import com.dopstore.mall.util.URL;
import com.dopstore.mall.util.UserUtils;
import com.dopstore.mall.util.Utils;
import com.dopstore.mall.view.CircleImageView;
import com.dopstore.mall.view.CommonDialog;
import com.dopstore.mall.view.citypicker.CityPicker;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;
import com.zfdang.multiple_images_selector.ImagesSelectorActivity;
import com.zfdang.multiple_images_selector.SelectorSettings;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class MyDetailActivity extends BaseActivity {
    private RelativeLayout nickLayout, phoneLayout, sexLayout, dateLayout, cityLayout, babyLayout, babysexLy, babydateLy;
    private TextView nickTv, phoneTv, sexTv, dateTv, cityTv, babyTv, babySexTv, babyDateTv;
    private CircleImageView headImage;
    private LinearLayout bgLayout;
    private Button saveBt;
    private ArrayList<String> mListResult = new ArrayList<>();
    private int type = 0;
    private int sexType = 0;
    private final static int MODIFY_NICK_CODE = 0;
    private final static int MODIFY_BABY_CODE = 1;
    private final static int MODIFY_HEAD_CODE = 2;
    private PopupWindow popupWindow;
    private int cityPositon = 0;
    private String imageUrl = "";
    private LoadImageUtils loadImage;
    private HttpHelper httpHelper;
    private ProUtils proUtils;


    private CommonDialog dialog;
    private ACache aCache;
    private List<CityBean> cityList = new ArrayList<CityBean>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_detail);
        initview();
        initData();
    }

    private void initview() {
        loadImage = LoadImageUtils.getInstance(this);
        aCache = ACache.get(this);
        proUtils = new ProUtils(this);
        httpHelper = HttpHelper.getOkHttpClientUtils(this);
        cityList = (List<CityBean>) aCache.getAsObject(Constant.CITYS);
        leftImageBack(R.mipmap.back_arrow);
        setCustomTitle("修改资料", getResources().getColor(R.color.white_color));

        headImage = (CircleImageView) findViewById(R.id.my_detail_head_image);
        headImage.setOnClickListener(listener);
        nickLayout = (RelativeLayout) findViewById(R.id.my_detail_nick_layout);
        nickTv = (TextView) findViewById(R.id.my_detail_nick_name);
        nickLayout.setOnClickListener(listener);
        phoneTv = (TextView) findViewById(R.id.my_detail_phone);
        sexLayout = (RelativeLayout) findViewById(R.id.my_detail_sex_layout);
        sexTv = (TextView) findViewById(R.id.my_detail_sex);
        sexLayout.setOnClickListener(listener);
        dateLayout = (RelativeLayout) findViewById(R.id.my_detail_date_layout);
        dateTv = (TextView) findViewById(R.id.my_detail_date);
        dateLayout.setOnClickListener(listener);
        cityLayout = (RelativeLayout) findViewById(R.id.my_detail_city_layout);
        cityTv = (TextView) findViewById(R.id.my_detail_city);
        cityLayout.setOnClickListener(listener);
        babyLayout = (RelativeLayout) findViewById(R.id.my_detail_baby_layout);
        babyTv = (TextView) findViewById(R.id.my_detail_baby);
        babyLayout.setOnClickListener(listener);
        babysexLy = (RelativeLayout) findViewById(R.id.my_detail_babysex_layout);
        babySexTv = (TextView) findViewById(R.id.my_detail_babysex);
        babysexLy.setOnClickListener(listener);
        babydateLy = (RelativeLayout) findViewById(R.id.my_detail_babydate_layout);
        bgLayout = (LinearLayout) findViewById(R.id.my_detail_poup_bg);
        babyDateTv = (TextView) findViewById(R.id.my_detail_babydate);
        saveBt = (Button) findViewById(R.id.my_detail_save_bt);
        babydateLy.setOnClickListener(listener);
        saveBt.setOnClickListener(listener);

    }

    private void initData() {
        nickTv.setText(UserUtils.getNickName(this));
        phoneTv.setText(UserUtils.getMobile(this));
        String gender = UserUtils.getGender(this);
        imageUrl = UserUtils.getAvatar(this);
        if ("1".equals(gender)) {
            sexTv.setText("男");
        } else {
            sexTv.setText("女");
        }

        dateTv.setText(Utils.formatSecond(UserUtils.getBirthday(this), "yyyy-MM-dd"));
        String cityId = UserUtils.getCity(this);
        String cityName = "北京";
        if (cityList != null) {
            for (CityBean bean : cityList) {
                if (bean.getId().equals(cityId)) {
                    cityName = bean.getName();
                }
            }
            if ("0".equals(cityId)) {
                cityName = "";
            }
        }
        String avatar = UserUtils.getAvatar(this);
        if (TextUtils.isEmpty(avatar)) {
            headImage.setImageResource(R.mipmap.ic);
        } else {
            loadImage.displayImage(avatar, headImage);
        }

        cityTv.setText(cityName);
        babyTv.setText(UserUtils.getBabyName(this));
        String babygender = UserUtils.getBabyGender(this);
        if ("1".equals(babygender)) {
            babySexTv.setText("男");
        } else {
            babySexTv.setText("女");
        }
        babyDateTv.setText(Utils.formatSecond(UserUtils.getBabyBirthday(this), "yyyy-MM-dd"));
    }


    private void dismissPop() {
        if (popupWindow.isShowing()) {
            popupWindow.dismiss();
        }
    }

    private void showCityPoup() {
        bgLayout.setVisibility(View.VISIBLE);
        int screenWidth = getWindowManager().getDefaultDisplay().getWidth();
        View v = LayoutInflater.from(this).inflate(R.layout.common_poup, null);
        LinearLayout top = (LinearLayout) v.findViewById(R.id.popup_top_layout);
        TextView cancleTv = (TextView) v.findViewById(R.id.popup_cancel);
        TextView confirmBt = (TextView) v.findViewById(R.id.popup_confirm);
        final CityPicker cityPicker = new CityPicker(this);
        cityPicker.init(cityList);
        top.addView(cityPicker);
        popupWindow = PopupUtils.ShowBottomPopupWindow(this, popupWindow, v, screenWidth, 192, findViewById(R.id.my_detail_main_layout));
        popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                popupWindow = null;
                bgLayout.setVisibility(View.GONE);
            }
        });
        cityPicker.setOnChangeListener(new CityPicker.OnChangeListener() {
            @Override
            public void onChange(int position) {
                cityPositon = position;
            }
        });
        cancleTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismissPop();
            }
        });
        confirmBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String cityName = cityList.get(cityPositon).getName();
                cityTv.setText(cityName);
                dismissPop();
            }
        });

    }

    private void showSex() {
        View v = LayoutInflater.from(this).inflate(R.layout.dialog_select_sex, null);
        TextView manTv = (TextView) v.findViewById(R.id.dialog_select_man);
        TextView womanTvv = (TextView) v.findViewById(R.id.dialog_select_woman);
        manTv.setOnClickListener(listener);
        womanTvv.setOnClickListener(listener);
        int screenWidth = getWindowManager().getDefaultDisplay().getWidth();
        dialog = new CommonDialog(this, v, screenWidth - 100);
        dialog.show();
    }

    private void setSexText(String sex) {
        if (dialog.isShowing()) {
            dialog.dismiss();
        }
        switch (sexType) {
            case 0:
                sexTv.setText(sex);
                break;
            case 1:
                babySexTv.setText(sex);
                break;
        }
    }

    /**
     * 显示时间选择器
     */
    private void showTimePop() {
        TimePopupWindow timePopupWindow = new TimePopupWindow(this, TimePopupWindow.Type.YEAR_MONTH_DAY);
        Date curDate = new Date(System.currentTimeMillis());//获取当前时间
        timePopupWindow.showAtLocation(this.findViewById(R.id.my_detail_main_layout), Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0, curDate);
        timePopupWindow.setOnTimeSelectListener(new TimePopupWindow.OnTimeSelectListener() {
            @Override
            public void onTimeSelect(Date date) {
                SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
                String timeStr = formatter.format(date);
                switch (type) {
                    case 0:
                        dateTv.setText(timeStr);
                        break;
                    case 1:
                        babyDateTv.setText(timeStr);
                        break;
                }
            }
        });
    }


    View.OnClickListener listener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.my_detail_head_image:
                    mListResult = new ArrayList<String>();
                    Intent intent = new Intent(MyDetailActivity.this, ImagesSelectorActivity.class);
                    // max number of images to be selected
                    intent.putExtra(SelectorSettings.SELECTOR_MAX_IMAGE_NUMBER, 1);
                    // min size of image which will be shown; to filter tiny images (mainly icons)
                    intent.putExtra(SelectorSettings.SELECTOR_MIN_IMAGE_SIZE, 100000);
                    // show camera or not
                    intent.putExtra(SelectorSettings.SELECTOR_SHOW_CAMERA, true);
                    // pass current selected images as the initial value
                    intent.putStringArrayListExtra(SelectorSettings.SELECTOR_INITIAL_SELECTED_LIST,
                            mListResult);
                    // start the selector
                    startActivityForResult(intent, MODIFY_HEAD_CODE);
                    break;
                case R.id.my_detail_nick_layout:
                    String name = nickTv.getText().toString();
                    Map<String, Object> map = new HashMap<>();
                    map.put("name", name);
                    SkipUtils.jumpForMapResult(MyDetailActivity.this, ModifyNickActivity.class, map, MODIFY_NICK_CODE);
                    break;
                case R.id.my_detail_sex_layout:
                    showSex();
                    sexType = 0;
                    break;
                case R.id.my_detail_date_layout:
                    showTimePop();
                    type = 0;
                    break;
                case R.id.my_detail_city_layout:
                    showCityPoup();
                    break;
                case R.id.my_detail_baby_layout:
                    String babyN = babyTv.getText().toString();
                    Map<String, Object> babymap = new HashMap<>();
                    babymap.put("name", babyN);
                    SkipUtils.jumpForMapResult(MyDetailActivity.this, ModifyNickActivity.class, babymap, MODIFY_BABY_CODE);
                    break;
                case R.id.my_detail_babysex_layout:
                    showSex();
                    sexType = 1;
                    break;
                case R.id.my_detail_babydate_layout:
                    type = 1;
                    showTimePop();
                    break;
                case R.id.dialog_select_man:
                    setSexText("男");
                    break;
                case R.id.dialog_select_woman:
                    setSexText("女");
                    break;
                case R.id.my_detail_save_bt:
                    savePicture();
                    break;
            }
        }
    };

    private void savePicture() {
        if (mListResult != null && mListResult.size() > 0) {
            proUtils.show();
            String imageStr = mListResult.get(0);
            String imageBase = Utils.encodeBase64File(imageStr);
            Map<String, String> map = new HashMap<String, String>();
            map.put(Constant.AVATAR_BINARY, imageBase);
            httpHelper.postKeyValuePairAsync(this, URL.UPLOAD_AVATAR, map, new Callback() {
                @Override
                public void onFailure(Request request, IOException e) {
                    T.checkNet(MyDetailActivity.this);
                }

                @Override
                public void onResponse(Response response) throws IOException {
                    String body = response.body().string();
                    try {
                        JSONObject jo = new JSONObject(body);
                        String code = jo.optString(Constant.ERROR_CODE);
                        if ("0".equals(code)) {
                            imageUrl = jo.optString(Constant.AVATAR_URL);
                            saveToService();
                        } else {
                            String msg = jo.optString(Constant.ERROR_MSG);
                            T.show(MyDetailActivity.this, msg);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    proUtils.dismiss();
                }
            }, null);
        } else {
            saveToService();
        }

    }

    private void saveToService() {
        proUtils.show();
        String sexStr = sexTv.getText().toString().trim();
        String babySexStr = babySexTv.getText().toString().trim();
        String cityStr = cityTv.getText().toString();
        String cityId = "0";
        if (cityList != null) {
            for (CityBean city : cityList) {
                String cityName = city.getName();
                if (cityName.equals(cityStr)) {
                    cityId = city.getId();
                }
            }
        }
        Map<String, String> map = new HashMap<String, String>();
        map.put(Constant.USER_ID, UserUtils.getId(this));
        map.put(Constant.AVATAR, imageUrl);
        map.put(Constant.MOBILE, phoneTv.getText().toString().trim());
        map.put(Constant.NICKNAME, nickTv.getText().toString().trim());
        if ("男".equals(sexStr)) {
            map.put(Constant.GENDER, "1");
        } else {
            map.put(Constant.GENDER, "0");
        }
        map.put(Constant.BIRTHDAY, dateTv.getText().toString().trim());
        map.put(Constant.CITY, cityId);
        map.put(Constant.BABY_NAME, babyTv.getText().toString());
        if ("男".equals(babySexStr)) {
            map.put(Constant.BABY_GENDER, "1");
        } else {
            map.put(Constant.BABY_GENDER, "0");
        }
        map.put(Constant.BABY_BIRTHDAY, babyDateTv.getText().toString().trim());
        httpHelper.postKeyValuePairAsync(this, URL.USER_UPDATE, map, new Callback() {
            @Override
            public void onFailure(Request request, IOException e) {
                T.checkNet(MyDetailActivity.this);
                proUtils.dismiss();
            }

            @Override
            public void onResponse(Response response) throws IOException {
                String body = response.body().string();
                try {
                    JSONObject jo = new JSONObject(body);
                    String code = jo.optString(Constant.ERROR_CODE);
                    if ("0".equals(code)) {
                        aCache.put(Constant.TOKEN, jo.optString(Constant.TOKEN));
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
                        UserUtils.setData(MyDetailActivity.this, data);
                        Intent it = new Intent();
                        it.setAction(Constant.UP_USER_DATA);
                        sendBroadcast(it);
                        T.show(MyDetailActivity.this, "修改成功");
                    } else {
                        String msg = jo.optString(Constant.ERROR_MSG);
                        T.show(MyDetailActivity.this, msg);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                proUtils.dismiss();
            }
        }, null);
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data == null) return;
        switch (requestCode) {
            case MODIFY_NICK_CODE: {
                Map<String, Object> map = (Map<String, Object>) data.getSerializableExtra("map");
                nickTv.setText(map.get("name").toString());
            }
            break;
            case MODIFY_BABY_CODE: {
                Map<String, Object> map = (Map<String, Object>) data.getSerializableExtra("map");
                babyTv.setText(map.get("name").toString());
            }
            break;
            case MODIFY_HEAD_CODE:
                if (resultCode == RESULT_OK) {
                    mListResult = data.getStringArrayListExtra(SelectorSettings.SELECTOR_RESULTS);
                    String image = mListResult.get(0);
                    loadImage.displayImage("file://" + image, headImage);
                }
                break;
        }
    }
}
