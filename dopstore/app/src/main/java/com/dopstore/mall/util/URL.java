package com.dopstore.mall.util;

/**
 * Created by 喜成 on 16/9/9.
 * name
 */
public class URL {
    public static String BASE_URL = "http://orange.dev.attackt.com/";
    public static String SEND_V_CODE = BASE_URL + "api/v1/send_v_code";//发送验证码
    public static String CHECK_V_CODE = BASE_URL + "api/v1/check_v_code";//校验验证码
    public static String SIGN_UP = BASE_URL + "api/v1/signup";//用户注册
    public static String LOGIN = BASE_URL + "api/v1/login";//用户登录
    public static String RESET_PASSWORD = BASE_URL + "api/v1/reset_password";//重置密码
    public static String USER_DETAIL = BASE_URL + "api/v1/user/{user_id}";//用户资料
    public static String USER_UPDATE = BASE_URL + "api/v1/user/update";//修改用户资料
    public static String UPLOAD_AVATAR = BASE_URL + "api/v1/upload_avatar";//上传头像

}
