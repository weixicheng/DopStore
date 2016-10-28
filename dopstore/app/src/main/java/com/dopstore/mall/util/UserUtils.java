package com.dopstore.mall.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;

import com.dopstore.mall.login.bean.UserData;

import static com.dopstore.mall.util.Constant.BIRTHDAY;

/**
 * Created by 喜成 on 16/9/9.
 * name
 */
public class UserUtils {
    private static final String FILE_NAME = "user_data";
    private static final String TOKEN_NAME = "token";

    public static void setData(Context context, UserData userData) {
        SharedPreferences sp = context.getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putString(Constant.ID, userData.getId());
        editor.putString(Constant.USERNAME, userData.getUsername());
        editor.putString(Constant.NICKNAME, userData.getNickname());
        editor.putString(Constant.GENDER, userData.getGender());
        editor.putLong(BIRTHDAY, userData.getBirthday());
        editor.putString(Constant.AVATAR, userData.getAvatar());
        editor.putString(Constant.BABY_NAME, userData.getBaby_name());
        editor.putString(Constant.BABY_GENDER, userData.getBaby_gender());
        editor.putLong(Constant.BABY_BIRTHDAY, userData.getBaby_birthday());
        editor.putString(Constant.CITY, userData.getAddress());
        editor.putString(Constant.MOBILE, userData.getMobile());
        double balance=userData.getBalance();
        float baF=Float.valueOf(balance+"");
        editor.putFloat(Constant.BALANCE, baF);
        editor.commit();
    }

    public static void setToken(Context context,String token){
        SharedPreferences sp = context.getSharedPreferences(TOKEN_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putString("token",token);
        editor.commit();
    }

    public static String getToken(Context context){
        SharedPreferences sp = context.getSharedPreferences(TOKEN_NAME, Context.MODE_PRIVATE);
        return sp.getString("token","");
    }

    public static boolean haveLogin(Context context) {
        SharedPreferences sp = context.getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE);
        String id = sp.getString(Constant.ID, "");
        boolean flag = false;
        if (!TextUtils.isEmpty(id)) {
            flag = true;
        } else {
            flag = false;
        }
        return flag;
    }

    public static String getId(Context context) {
        SharedPreferences sp = context.getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE);
        return sp.getString(Constant.ID, "");
    }

    public static String getNickName(Context context) {
        SharedPreferences sp = context.getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE);
        return sp.getString(Constant.NICKNAME, "");
    }

    public static String getGender(Context context) {
        SharedPreferences sp = context.getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE);
        return sp.getString(Constant.GENDER, "");
    }

    public static Long getBirthday(Context context) {
        SharedPreferences sp = context.getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE);
        return sp.getLong(Constant.BIRTHDAY, 0);
    }

    public static String getAvatar(Context context) {
        SharedPreferences sp = context.getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE);
        return sp.getString(Constant.AVATAR, "");
    }

    public static String getBabyName(Context context) {
        SharedPreferences sp = context.getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE);
        return sp.getString(Constant.BABY_NAME, "");

    }

    public static String getBabyGender(Context context) {
        SharedPreferences sp = context.getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE);
        return sp.getString(Constant.BABY_GENDER, "");
    }

    public static Long getBabyBirthday(Context context) {
        SharedPreferences sp = context.getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE);
        return sp.getLong(Constant.BABY_BIRTHDAY, 0);
    }

    public static String getMobile(Context context) {
        SharedPreferences sp = context.getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE);
        return sp.getString(Constant.MOBILE, "");
    }

    public static String getBalance(Context context) {
        SharedPreferences sp = context.getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE);
        float balance=sp.getFloat(Constant.BALANCE, 0);
        return balance+"";
    }

    public static void setBalance(Context context,String price) {
        SharedPreferences sp = context.getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putFloat(Constant.BALANCE, Float.valueOf(price));
        editor.commit();
    }

    public static String getCity(Context context) {
        SharedPreferences sp = context.getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE);
        return sp.getString(Constant.CITY, "");
    }

    public static void clear(Context context) {
        SharedPreferences sp = context.getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.clear();
        editor.commit();
        SharedPreferences tokenSp = context.getSharedPreferences(TOKEN_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor tokenEd = tokenSp.edit();
        tokenEd.clear();
        tokenEd.commit();
    }
}
