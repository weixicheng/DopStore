package com.dopstore.mall.util;

import android.util.Base64;

import java.io.File;
import java.io.FileInputStream;
import java.text.SimpleDateFormat;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by 喜成 on 16/9/5.
 * name
 */
public class Utils {

    // 判断是否是手机号
    public static boolean isPhoneNumberValid(String mobiles) {
        Pattern p = Pattern.compile("^((13[0-9])|(14[0-9])|(15[^4,\\D])|(17[0-9])|(18[0-9]))\\d{8}$");
        Matcher m = p.matcher(mobiles);
        return m.matches();
    }

    /**
     * 将dip或dp值转换为px值，保证尺寸大小不变
     *
     * @param dipValue
     * @param scale    （DisplayMetrics类中属�?�density�?
     * @return
     */
    public static int dip2px(float dipValue, float scale) {
        return (int) (dipValue * scale + 0.5f);
    }

    public static String formatSecond(Long time,String formatStr) {
        SimpleDateFormat format = new SimpleDateFormat(formatStr);
        return format.format(time*1000);
    }
    public static String formatMilli(Long time,String formatStr) {
        SimpleDateFormat format = new SimpleDateFormat(formatStr);
        return format.format(time);
    }




    /**
     * File转换成Base64
     * @param path
     * @return
     * @throws Exception
     */
    public static String encodeBase64File(String path){
        try {
            File file = new File(path);
            FileInputStream inputFile = new FileInputStream(file);
            byte[] buffer = new byte[(int)file.length()];
            inputFile.read(buffer);
            inputFile.close();
            return Base64.encodeToString(buffer, Base64.DEFAULT);
        }catch (Exception e){
            return null;
        }
    }

}
