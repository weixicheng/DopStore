package com.dopstore.mall.util;

/**
 * Created by 喜成 on 16/9/9.
 * name
 */
public class URL {
    public static String BASE_URL = "http://orange.dev.attackt.com/api/v1/";
    public static String SHOP_GOOD_DETAIL_URL = "http://orange.dev.attackt.com/h5/goods/";
    public static String SEND_V_CODE = BASE_URL + "send_v_code";//发送验证码
    public static String SEND_VOICE_CODE = BASE_URL + "send_voice_code";//语音验证码
    public static String CHECK_V_CODE = BASE_URL + "check_v_code";//校验验证码
    public static String SIGN_UP = BASE_URL + "signup";//用户注册
    public static String LOGIN = BASE_URL + "login";//用户登录
    public static String RESET_PASSWORD = BASE_URL + "reset_password";//重置密码
    public static String USER_DETAIL = BASE_URL + "user/{user_id}";//用户资料
    public static String USER_UPDATE = BASE_URL + "user/update";//修改用户资料
    public static String UPLOAD_AVATAR = BASE_URL + "upload_avatar";//上传头像
    public static String OTHER_SIGNUPL = BASE_URL + "othersignup";//第三方注册
    public static String SHIPPINGADDRESS = BASE_URL + "user/";//收货地址列表
    public static String HOME_CAROUSEL = BASE_URL + "home/carousel";//轮播图
    public static String USER_HELPS = BASE_URL + "helps";//帮助中心
    public static String GOODS_CATEGORY = BASE_URL + "goods/category";//商品分类
    public static String HOME_THEME = BASE_URL + "home/theme";//商城主题
    public static String ACT_CATEGORIES = BASE_URL + "home/act_categories";//活动分类
    public static String RECOMMENDED_ACT = BASE_URL + "home/recommended_act";//推荐活动
    public static String GOODS_LIST = BASE_URL + "goods";//活动列表
    public static String CART_QUERY = BASE_URL + "home/cart/query";//查询购物车
    public static String CART_GOODS_ADD = BASE_URL + "home/cart/goods_add";//添加购物车
    public static String CART_DELETE = BASE_URL + "home/cart/delete";//购物车物品删除
    public static String CART_EDIT = BASE_URL + "home/cart/edit";//购物车物品编辑
    public static String COLLECTION_EDIT = BASE_URL + "home/collection/edit";//加入或取消收藏
    public static String COLLECTION_QUERY = BASE_URL + "home/collection/query";//收藏列表
    public static String COLLECTION_DEL = BASE_URL + "home/collection/batch_del";//收藏删除
    public static String ACTIVITY_DETAILS = BASE_URL + "home/activity_details";//活动详情
    public static String ORDER_ACTIVITY = BASE_URL + "order/activity";//确认活动单下单
    public static String ORDER_ACTIVITY_LIST = BASE_URL + "order/activity_list/";//活动订单列表
    public static String COLLECTION_GOODS_STATUS = BASE_URL + "home/collection/goods";//判断商品是否收藏
    public static String ACTIVITY_PAYMENT = BASE_URL + "order/activity/payment";//活动订单支付
}
