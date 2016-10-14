package com.dopstore.mall.util;

import android.content.Context;
import android.text.TextUtils;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import org.apache.http.Header;

import java.util.Iterator;
import java.util.Map;
import java.util.Set;



/**
 * 作者：xicheng on 16/10/12 10:47
 * 类别：
 */

public class CommHttp {
    private static AsyncHttpClient client;
    private static CommHttp okHttpUtils = null;
    private static ACache aCache;

    public static CommHttp getInstance(Context context) {
        if (okHttpUtils == null) {
            synchronized (CommHttp.class) {
                if (okHttpUtils == null) {
                    okHttpUtils = new CommHttp(context);
                }
            }
        }
        return okHttpUtils;
    }

    private CommHttp(Context context) {
        client = new AsyncHttpClient();
        aCache = ACache.get(context);
    }

    public static void post(final Context context, String url, Map<String,Object> map, final HttpCallBack callBack) {
        String token = aCache.getAsString(Constant.TOKEN);
        if (!TextUtils.isEmpty(token)) {
            client.addHeader("Authorization", "JWT " + token);
        }
        RequestParams params=new RequestParams();
        if (map != null && !map.isEmpty()) {
            Set set = map.entrySet();
            for (Iterator iter = set.iterator(); iter.hasNext(); ) {
                Map.Entry entry = (Map.Entry) iter.next();
                String key = (String) entry.getKey();
                Object value = entry.getValue();
                params.put(key, value);
            }
        }
        client.post(context, url, params, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] bytes) {
                if (statusCode == 200) {
                    callBack.success(new String(bytes));
                }else {
                    T.show(context,statusCode+"");
                }
            }

            @Override
            public void onFailure(int i, Header[] headers, byte[] bytes, Throwable throwable) {
                callBack.failed(new String(bytes));
            }
        });

    }public static void postObject(final Context context, String url,RequestParams params, final HttpCallBack callBack) {
        String token = aCache.getAsString(Constant.TOKEN);
        if (!TextUtils.isEmpty(token)) {
            client.addHeader("Authorization", "JWT " + token);
        }
        client.post(context, url, params, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] bytes) {
                if (statusCode == 200) {
                    callBack.success(new String(bytes));
                }else {
                    T.show(context,statusCode+"");
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] bytes, Throwable throwable) {
                callBack.failed(String.valueOf(statusCode));
            }
        });

    }

    public static void get(final Context context,String url,final HttpCallBack callBack){
        String token = aCache.getAsString(Constant.TOKEN);
        if (!TextUtils.isEmpty(token)) {
            client.addHeader("Authorization", "JWT " + token);
        }
        client.get(context, url, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] bytes) {
                if (statusCode == 200) {
                    callBack.success(new String(bytes));
                }else {
                    T.show(context,statusCode+"");
                }
            }

            @Override
            public void onFailure(int i, Header[] headers, byte[] bytes, Throwable throwable) {
                callBack.failed(new String(bytes));
            }
        });
    }

    public static interface HttpCallBack {
        public void success(String body);

        public void failed(String msg);
    }
}
