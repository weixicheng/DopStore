package com.dopstore.mall.util;

/**
 * Created by 喜成 on 16/9/9.
 * name
 */
public class URL {
    public static String BASE_URL = "http://orange.dev.attackt.com/";
    public static String SEND_V_CODE = BASE_URL + "api/v1/send_v_code";//发送验证码
    public static String SEND_VOICE_CODE = BASE_URL + "api/v1/send_voice_code";//语音验证码
    public static String CHECK_V_CODE = BASE_URL + "api/v1/check_v_code";//校验验证码
    public static String SIGN_UP = BASE_URL + "api/v1/signup";//用户注册
    public static String LOGIN = BASE_URL + "api/v1/login";//用户登录
    public static String RESET_PASSWORD = BASE_URL + "api/v1/reset_password";//重置密码
    public static String USER_DETAIL = BASE_URL + "api/v1/user/{user_id}";//用户资料
    public static String USER_UPDATE = BASE_URL + "api/v1/user/update";//修改用户资料
    public static String UPLOAD_AVATAR = BASE_URL + "api/v1/upload_avatar";//上传头像
    public static String OTHER_SIGNUPL = BASE_URL + "api/v1/othersignup";//第三方注册
    public static String SHIPPINGADDRESS = BASE_URL + "api/v1/user/";//收货地址列表
    public static String HOME_CAROUSEL = BASE_URL + "api/v1/home/carousel/";//轮播图
    public static String USER_HELPS = BASE_URL + "api/v1/helps";//帮助中心
    public static String GOODS_CATEGORY = BASE_URL + "api/v1/goods/category";//商品分类
    public static String HOME_THEME = BASE_URL + "api/v1/home/theme";//商城主题
    public static String ACT_CATEGORIES = BASE_URL + "api/v1/home/act_categories";//活动分类
    public static String RECOMMENDED_ACT = BASE_URL + "api/v1/home/recommended_act";//推荐活动
    public static String GOODS_LIST = BASE_URL + "api/v1/goods";//活动列表

}
