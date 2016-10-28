package com.dopstore.mall.person.activity;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
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
import com.dopstore.mall.login.bean.UserData;
import com.dopstore.mall.base.BaseActivity;
import com.dopstore.mall.time.TimePopupWindow;
import com.dopstore.mall.util.ACache;
import com.dopstore.mall.util.CommHttp;
import com.dopstore.mall.util.Constant;
import com.dopstore.mall.util.PopupUtils;
import com.dopstore.mall.util.SkipUtils;
import com.dopstore.mall.util.T;
import com.dopstore.mall.util.UILImageLoader;
import com.dopstore.mall.util.URL;
import com.dopstore.mall.util.UserUtils;
import com.dopstore.mall.util.Utils;
import com.dopstore.mall.view.CircleImageView;
import com.dopstore.mall.view.CommonDialog;
import com.dopstore.mall.view.citypicker.CityPicker;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.finalteam.galleryfinal.CoreConfig;
import cn.finalteam.galleryfinal.FunctionConfig;
import cn.finalteam.galleryfinal.GalleryFinal;
import cn.finalteam.galleryfinal.ThemeConfig;
import cn.finalteam.galleryfinal.model.PhotoInfo;


public class MyDetailActivity extends BaseActivity {
    private RelativeLayout nickLayout, phoneLayout, sexLayout, dateLayout, cityLayout, babyLayout, babysexLy, babydateLy;
    private TextView nickTv, phoneTv, sexTv, dateTv, cityTv, babyTv, babySexTv, babyDateTv;
    private CircleImageView headImage;
    private LinearLayout bgLayout;
    private Button saveBt;
    private int type = 0;
    private int sexType = 0;
    private final static int MODIFY_NICK_CODE = 0;
    private final static int MODIFY_BABY_CODE = 1;
    private final static int REQUEST_CODE_PICK_IMAGE = 2;
    private final static int REQUEST_CODE_CAMERA = 3;
    private PopupWindow popupWindow;
    private int cityPositon = 0;
    private String imageUrl = "";
    private String imagePath = "";
    private  String cityId="1";
    private DisplayImageOptions options;
    private ImageLoader imageLoader;


    private CommonDialog dialog;
    private ACache aCache;
    private List<CityBean> cityList = new ArrayList<CityBean>();
    private CommHttp httpHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_detail);
        initview();
        initData();
    }

    private void initview() {
        httpHelper=CommHttp.getInstance();
        aCache = ACache.get(this);
        imageLoader=ImageLoader.getInstance();
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

        ThemeConfig themeConfig = new ThemeConfig.Builder()
        .setTitleBarTextColor(getResources().getColor(R.color.white))
        .setTitleBarBgColor(getResources().getColor(R.color.red_color_f93448))
        .build();
        cn.finalteam.galleryfinal.ImageLoader imageLoader = new UILImageLoader();
        FunctionConfig.Builder functionConfigBuilder = new FunctionConfig.Builder();
        FunctionConfig functionConfig = functionConfigBuilder.build();
        CoreConfig coreConfig = new CoreConfig.Builder(MyDetailActivity.this, imageLoader, themeConfig)
                .setFunctionConfig(functionConfig)
                .setNoAnimcation(false)
                .build();
        GalleryFinal.init(coreConfig);

        options = new DisplayImageOptions.Builder()
                .showImageOnFail(R.mipmap.me_icon)
                .showImageForEmptyUri(R.mipmap.me_icon)
                .showImageOnLoading(R.mipmap.me_icon).build();

        initImageLoader(this);
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
        cityId = UserUtils.getCity(this);
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
        }else {
            cityId="1";
            cityName = "北京";
        }
        String avatar = UserUtils.getAvatar(this);
        imageLoader.displayImage(avatar, headImage, options);

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
        if (cityList!=null&&cityList.size()>0) {
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
                    ShowPhotoSelect();
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
                case R.id.photo_pick_image:
                    dismissPop();
                    getPick();
                    break;
                case R.id.photo_pick_select:
                    dismissPop();
                    getSelect();
                    break;
                case R.id.photo_pick_cancle:
                    dismissPop();
                    break;
            }
        }
    };

    private void getSelect() {
        GalleryFinal.openGallerySingle(REQUEST_CODE_PICK_IMAGE, resultCallback);
    }

    private void getPick() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                requestPermissions(new String[]{"android.permission.CAMERA"}, 112);
            }
            return;
        }
        GalleryFinal.openCamera(REQUEST_CODE_CAMERA, new GalleryFinal.OnHanlderResultCallback() {
            @Override
            public void onHanlderSuccess(int reqeustCode, List<PhotoInfo> resultList) {
                imagePath = resultList.get(0).getPhotoPath();
                imageLoader.displayImage("file:/" + imagePath, headImage, options);
            }

            @Override
            public void onHanlderFailure(int requestCode, String errorMsg) {
                T.show(MyDetailActivity.this, requestCode + "=" + errorMsg);
                headImage.setImageResource(R.mipmap.me_icon);
            }
        });
    }


    GalleryFinal.OnHanlderResultCallback resultCallback = new GalleryFinal.OnHanlderResultCallback() {
        @Override
        public void onHanlderSuccess(int reqeustCode, List<PhotoInfo> resultList) {
            imagePath = resultList.get(0).getPhotoPath();
            imageLoader.displayImage("file:/" + imagePath, headImage, options);
        }

        @Override
        public void onHanlderFailure(int requestCode, String errorMsg) {
            T.show(MyDetailActivity.this, requestCode + "=" + errorMsg);
            headImage.setImageResource(R.mipmap.me_icon);
        }
    };

    private void ShowPhotoSelect() {
        bgLayout.setVisibility(View.VISIBLE);
        int screenWidth = getWindowManager().getDefaultDisplay().getWidth();
        View v = LayoutInflater.from(this).inflate(R.layout.pop_photo_pick, null);
        TextView pickTv = (TextView) v.findViewById(R.id.photo_pick_image);
        TextView selectTv = (TextView) v.findViewById(R.id.photo_pick_select);
        TextView cancleTv = (TextView) v.findViewById(R.id.photo_pick_cancle);
        pickTv.setOnClickListener(listener);
        selectTv.setOnClickListener(listener);
        cancleTv.setOnClickListener(listener);
        popupWindow = PopupUtils.ShowBottomPopupWindow(this, popupWindow, v, screenWidth, 152, findViewById(R.id.my_detail_main_layout));
        popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                popupWindow = null;
                bgLayout.setVisibility(View.GONE);
            }
        });
    }


    private void savePicture() {
        if (!TextUtils.isEmpty(imagePath)) {
            String imageBase = Utils.encodeBase64File(imagePath);
            Map<String, Object> map = new HashMap<String, Object>();
            map.put(Constant.AVATAR_BINARY, imageBase);
            httpHelper.post(this, URL.UPLOAD_AVATAR, map, new CommHttp.HttpCallBack() {
                @Override
                public void success(String body) {
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
                }

                @Override
                public void failed(String msg) {

                    T.checkNet(MyDetailActivity.this);
                }
            });
        } else {
            saveToService();
        }

    }

    private void saveToService() {
        String sexStr = sexTv.getText().toString().trim();
        String babySexStr = babySexTv.getText().toString().trim();
        String cityStr = cityTv.getText().toString();
        if (cityList != null) {
            for (CityBean city : cityList) {
                String cityName = city.getName();
                if (cityName.equals(cityStr)) {
                    cityId = city.getId();
                }
            }
        }
        Map<String, Object> map = new HashMap<String, Object>();
        map.put(Constant.USER_ID, UserUtils.getId(this));
        map.put(Constant.AVATAR, imageUrl);
        map.put(Constant.MOBILE, phoneTv.getText().toString().trim());
        map.put(Constant.NICKNAME, nickTv.getText().toString().trim());
        if ("男".equals(sexStr)) {
            map.put(Constant.GENDER, "1");
        } else {
            map.put(Constant.GENDER, "2");
        }
        map.put(Constant.BIRTHDAY, dateTv.getText().toString().trim());
        map.put(Constant.CITY, cityId);
        map.put(Constant.BABY_NAME, babyTv.getText().toString());
        if ("男".equals(babySexStr)) {
            map.put(Constant.BABY_GENDER, "1");
        } else {
            map.put(Constant.BABY_GENDER, "2");
        }
        map.put(Constant.BABY_BIRTHDAY, babyDateTv.getText().toString().trim());
        httpHelper.post(this, URL.USER_UPDATE, map, new CommHttp.HttpCallBack() {
            @Override
            public void success(String body) {
                try {
                    JSONObject jo = new JSONObject(body);
                    String code = jo.optString(Constant.ERROR_CODE);
                    if ("0".equals(code)) {
                        String tokenStr=jo.optString(Constant.TOKEN);
                        UserUtils.setToken(MyDetailActivity.this, tokenStr);
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
                        data.setGender(user.optString(Constant.GENDER));
                        data.setAvatar(user.optString(Constant.AVATAR));
                        data.setBirthday(user.optLong(Constant.BIRTHDAY));
                        data.setBaby_birthday(user.optLong(Constant.BABY_BIRTHDAY));
                        data.setBaby_gender(user.optString(Constant.BABY_GENDER));
                        data.setBaby_name(user.optString(Constant.BABY_NAME));
                        data.setMobile(user.optString(Constant.MOBILE));
                        data.setAddress(user.optString(Constant.CITY));
                        data.setBalance(user.optDouble(Constant.BALANCE));
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
            }

            @Override
            public void failed(String msg) {
                T.checkNet(MyDetailActivity.this);
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
        }
    }

    private void initImageLoader(Context context) {
        ImageLoaderConfiguration.Builder config = new ImageLoaderConfiguration.Builder(context);
        config.threadPriority(Thread.NORM_PRIORITY - 2);
        config.denyCacheImageMultipleSizesInMemory();
        config.diskCacheFileNameGenerator(new Md5FileNameGenerator());
        config.diskCacheSize(50 * 1024 * 1024); // 50 MiB
        config.tasksProcessingOrder(QueueProcessingType.LIFO);
        config.writeDebugLogs();
        imageLoader.init(config.build());
    }

}
