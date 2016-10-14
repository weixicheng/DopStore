package com.dopstore.mall.util;

import android.app.Activity;
import android.content.Context;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.util.Base64;
import android.view.inputmethod.InputMethodManager;

import java.io.File;
import java.io.FileInputStream;
import java.math.RoundingMode;
import java.text.NumberFormat;
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

    public static String formatSecond(Long time, String formatStr) {
        SimpleDateFormat format = new SimpleDateFormat(formatStr);
        return format.format(time * 1000);
    }

    public static String formatTSecond(String times) {
        long time = Long.parseLong(times);
        SimpleDateFormat format = new SimpleDateFormat("yyyy年MM月dd日");
        return format.format(time * 1000);
    }

    public static String formatTDSecond(String times) {
        long time = Long.parseLong(times);
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        return format.format(time * 1000);
    }

    public static String formatMilli(Long time, String formatStr) {
        SimpleDateFormat format = new SimpleDateFormat(formatStr);
        return format.format(time);
    }


    /**
     * File转换成Base64
     *
     * @param path
     * @return
     * @throws Exception
     */
    public static String encodeBase64File(String path) {
        try {
            File file = new File(path);
            FileInputStream inputFile = new FileInputStream(file);
            byte[] buffer = new byte[(int) file.length()];
            inputFile.read(buffer);
            inputFile.close();
            return Base64.encodeToString(buffer, Base64.DEFAULT);
        } catch (Exception e) {
            return null;
        }
    }


    /**
     * 隐藏键盘
     */
    public static void hideKeyboard(Context context) {
        InputMethodManager inputMethodManager = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        boolean isOpen = inputMethodManager.isActive();
        if (isOpen) {
            try {
                inputMethodManager.hideSoftInputFromWindow(((Activity) context).getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }


    public static String GetHostIp(Context context) {
        //获取wifi服务
        WifiManager wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
        //判断wifi是否开启
        if (!wifiManager.isWifiEnabled()) {
            wifiManager.setWifiEnabled(true);
        }
        WifiInfo wifiInfo = wifiManager.getConnectionInfo();
        int ipAddress = wifiInfo.getIpAddress();
        String ip = intToIp(ipAddress);
        return ip;
    }

    private static String intToIp(int i) {

        return (i & 0xFF) + "." +
                ((i >> 8) & 0xFF) + "." +
                ((i >> 16) & 0xFF) + "." +
                (i >> 24 & 0xFF);
    }

    public static boolean isNum(String str){
        return str.matches("^[-+]?(([0-9]+)([.]([0-9]+))?|([.]([0-9]+))?)$");
    }
    /*
      * 判断是否为浮点数，包括double和float
      * @param str 传入的字符串
      * @return 是浮点数返回true,否则返回false
    */
    public static boolean isDouble(String str) {
        Pattern pattern = Pattern.compile("^[-\\+]?[.\\d]*$");
        return pattern.matcher(str).matches();
    }

    /**
     * 使用NumberFormat,保留小数点后两位
     */
    public static String format(double value) {

        NumberFormat nf = NumberFormat.getNumberInstance();
        nf.setMaximumFractionDigits(2);
        /*
         * setMinimumFractionDigits设置成2
         *
         * 如果不这么做，那么当value的值是100.00的时候返回100
         *
         * 而不是100.00
         */
        nf.setMinimumFractionDigits(2);
        nf.setRoundingMode(RoundingMode.HALF_UP);
        /*
         * 如果想输出的格式用逗号隔开，可以设置成true
         */
        nf.setGroupingUsed(false);
        return nf.format(value);
    }



}
