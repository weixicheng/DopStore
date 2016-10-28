package com.dopstore.mall.util;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;

import com.dopstore.mall.login.activity.LoginActivity;

import org.json.JSONObject;

import java.io.IOException;
import java.net.SocketTimeoutException;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;


/**
 * 作者：xicheng on 16/10/12 10:47
 * 类别：
 */

public class CommHttp {
    private static OkHttpClient client;
    private static CommHttp okHttpUtils = null;
    private static int serversLoadTimes = 0;
    private static int maxLoadTimes = 10;


    public static CommHttp getInstance() {
        if (okHttpUtils == null) {
            synchronized (CommHttp.class) {
                if (okHttpUtils == null) {
                    okHttpUtils = new CommHttp();
                }
            }
        }
        return okHttpUtils;
    }

    private CommHttp() {
        client = new OkHttpClient.Builder()
                .connectTimeout(10, TimeUnit.SECONDS)
                .readTimeout(20, TimeUnit.SECONDS)
                .build();
    }

    public static void post(final Context context, String url, Map<String, Object> map, final HttpCallBack callBack) {
        serversLoadTimes = 0;
        FormBody.Builder formBodyBuilder = new FormBody.Builder();
        if (map != null && map.size() > 0) {
            Set<Map.Entry<String, Object>> entrySet = map.entrySet();
            for (Map.Entry<String, Object> entry : entrySet) {
                formBodyBuilder.add(entry.getKey(), entry.getValue().toString());
            }
        }
        String token = UserUtils.getToken(context);
        Request request;
        if (!TextUtils.isEmpty(token)) {
            request = new Request.Builder().url(url).addHeader("Authorization", "JWT " + token).post(formBodyBuilder.build()).build();
        } else {
            request = new Request.Builder().url(url).post(formBodyBuilder.build()).build();
        }
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                if (e.getCause().equals(SocketTimeoutException.class) && serversLoadTimes < maxLoadTimes)//如果超时并未超过指定次数，则重新连接
                {
                    serversLoadTimes++;
                    client.newCall(call.request()).enqueue(this);
                } else {
                    e.printStackTrace();
                }

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String boby = response.body().string();
                int code = response.networkResponse().code();
                final String bobyStr = boby;
                final int statusCode = code;
                if (code == 200) {
                    try {
                        JSONObject jo = new JSONObject(boby);
                        String detail = jo.optString("detail");
                        if (TextUtils.isEmpty(detail)) {
                            new Handler(Looper.getMainLooper()).post(new Runnable() {

                                @Override
                                public void run() {
                                    callBack.success(bobyStr);
                                }
                            });
                        } else if ("Error decoding signature".equals(detail)) {
                            new Handler(Looper.getMainLooper()).post(new Runnable() {

                                @Override
                                public void run() {
                                    T.show(context, "请重新登录");
                                    SkipUtils.directJump(context, LoginActivity.class, false);
                                }
                            });
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else {
                    new Handler(Looper.getMainLooper()).post(new Runnable() {

                        @Override
                        public void run() {
                            switch (statusCode) {
                                case 401: {
                                    T.show(context, "请重新登录");
                                    SkipUtils.directJump(context, LoginActivity.class, false);
                                }
                                break;
                                case 500: {
                                    T.show(context, "无法连接服务器");
                                }
                                break;
                            }
                        }
                    });
                }
            }
        });
    }

    public static void get(final Context context, String url, final HttpCallBack callBack) {
        serversLoadTimes = 0;
        String token = UserUtils.getToken(context);
        Request request;
        if (!TextUtils.isEmpty(token)) {
            request = new Request.Builder().url(url).addHeader("Authorization", "JWT " + token).get().build();
        } else {
            request = new Request.Builder().url(url).get().build();
        }
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                if (e.getCause().equals(SocketTimeoutException.class) && serversLoadTimes < maxLoadTimes)//如果超时并未超过指定次数，则重新连接
                {
                    serversLoadTimes++;
                    client.newCall(call.request()).enqueue(this);
                } else {
                    e.printStackTrace();
                }
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String boby = response.body().string();
                int code = response.networkResponse().code();
                final String bobyStr = boby;
                final int statusCode = code;
                if (code == 200) {
                    try {
                        JSONObject jo = new JSONObject(boby);
                        String detail = jo.optString("detail");
                        if (TextUtils.isEmpty(detail)) {
                            new Handler(Looper.getMainLooper()).post(new Runnable() {

                                @Override
                                public void run() {
                                    callBack.success(bobyStr);
                                }
                            });
                        } else if ("Error decoding signature".equals(detail)) {
                            new Handler(Looper.getMainLooper()).post(new Runnable() {

                                @Override
                                public void run() {
                                    T.show(context, "请重新登录");
                                    SkipUtils.directJump(context, LoginActivity.class, false);
                                }
                            });
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else {
                    new Handler(Looper.getMainLooper()).post(new Runnable() {

                        @Override
                        public void run() {
                            switch (statusCode) {
                                case 401: {
                                    T.show(context, "请重新登录");
                                    SkipUtils.directJump(context, LoginActivity.class, false);
                                }
                                break;
                                case 500: {
                                    T.show(context, "无法连接服务器");
                                }
                                break;
                            }
                        }
                    });
                }
            }
        });
    }


    public static interface HttpCallBack {
        public void success(String body);

        public void failed(String msg);
    }


}
