package com.dopstore.mall.util;


import android.content.Context;
import android.text.TextUtils;

import java.io.IOException;
import java.io.InputStream;
import java.net.FileNameMap;
import java.net.URLConnection;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;

/**
 * Created by Steven
 * on 16-1-7.
 */
public class HttpHelper {
    private static OkHttpClient okHttpClient = null;
    private static HttpHelper okHttpUtils = null;
    private ACache aCache;

    private HttpHelper(Context context) {
        okHttpClient = getOkHttpSingletonInstance();
        aCache = ACache.get(context);
    }

    public static HttpHelper getOkHttpClientUtils(Context context) {
        if (okHttpUtils == null) {
            synchronized (HttpHelper.class) {
                if (okHttpUtils == null) {
                    okHttpUtils = new HttpHelper(context);
                }
            }
        }
        return okHttpUtils;
    }

    public static OkHttpClient getOkHttpSingletonInstance() {
        if (okHttpClient == null) {
            synchronized (OkHttpClient.class) {
                okHttpClient = new OkHttpClient();
            }
        }
        return okHttpClient;
    }

    ///////////////////////////////////////////////////////////////////////////
    // GET方式网络访问
    ///////////////////////////////////////////////////////////////////////////

    /**
     * 基方法，返回Request对象
     *
     * @param urlString
     * @param tag
     * @return
     */
    private Request buildGetRequest(String urlString, Object tag) {
        Request.Builder builder = new Request.Builder();
        String token = aCache.getAsString(Constant.TOKEN);
        if (!TextUtils.isEmpty(token)) {
            builder.addHeader("Authorization", "JWT " + token);
        }
        builder.url(urlString);
        if (tag != null) {
            builder.tag(tag);
        }
        return builder.build();
    }


    /**
     * 自定义方法，返回Response对象
     *
     * @param urlString
     * @return
     * @throws IOException
     */
    private Response buildResponse(String urlString, Object tag) throws IOException {
        Request request = buildGetRequest(urlString, tag);
        Response response = okHttpClient.newCall(request).execute();
        return response;
    }

    //基础方法，返回ResponseBody对象
    private ResponseBody buildResponseBody(String urlString, Object tag) throws IOException {
        Response response = buildResponse(urlString, tag);
        if (response.isSuccessful()) {
            return response.body();
        }
        return null;
    }

    /**
     * 作用：实现网络访问文件，将获取到数据储存在文件流中
     *
     * @param urlString ：访问网络的url地址
     * @return InputStream
     */
    public static InputStream getStreamFromURL(Context context, String urlString, Object tag)
            throws IOException {
        ResponseBody body = getOkHttpClientUtils(context).buildResponseBody(urlString, tag);
        if (body != null) {
            return body.byteStream();
        }
        return null;
    }

    /**
     * 作用：实现网络访问文件，将获取到的数据存在字节数组中
     *
     * @param urlString ：访问网络的url地址
     * @return byte[]
     */
    public static byte[] getBytesFromURL(Context context, String urlString, Object tag) throws
            IOException {
        ResponseBody body = getOkHttpClientUtils(context).buildResponseBody(urlString, tag);
        if (body != null) {
            return body.bytes();
        }
        return null;
    }

    /**
     * 作用：实现网络访问文件，将获取到的数据存在字符串中
     *
     * @param urlString ：访问网络的url地址
     * @return String
     */
    public static String getStringFromURL(Context context, String urlString, Object tag) throws
            IOException {
        ResponseBody body = getOkHttpClientUtils(context).buildResponseBody(urlString, tag);
        if (body != null) {
            return body.string();
        }
        return null;
    }

    ///////////////////////////////////////////////////////////////////////////
    // POST方式访问网络
    ///////////////////////////////////////////////////////////////////////////

    /**
     * 基方法，返回Request对象
     *
     * @param urlString
     * @param tag
     * @return
     */
    private Request buildPostRequest(String urlString, RequestBody requestBody, Object tag) {
        Request.Builder builder = new Request.Builder();
        String token = aCache.getAsString(Constant.TOKEN);
        if (!TextUtils.isEmpty(token)) {
            builder.addHeader("Authorization", "JWT " + token);
        }
        builder.url(urlString).post(requestBody);
        //builder.addHeader("Accept", "application/json; q=0.5");
        if (tag != null) {
            builder.tag(tag);
        }
        return builder.build();
    }

    /**
     * 作用：post提交数据，返回服务器端返回的字节数组
     *
     * @param urlString ：访问网络的url地址
     * @return byte[]
     */
    private String postRequestBody(String urlString, RequestBody requestBody, Object tag) {
        Request request = buildPostRequest(urlString, requestBody, tag);
        try {
            Response response = okHttpClient.newCall(request).execute();
            if (response.isSuccessful()) {
                return response.body().string();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 作用：POST提交键值对，再返回相应的数据
     *
     * @param urlString ：访问网络的url地址
     * @param map       ：访问url时，需要传递给服务器的键值对数据。
     * @return String
     */
    public static String postKeyValuePair(Context context, String urlString, Map<String, String>
            map, Object tag) {
        //往FormEncodingBuilder对象中放置键值对
        FormBody.Builder formBuilder = new FormBody.Builder();
        if (map != null && !map.isEmpty()) {
            for (Map.Entry<String, String> entry : map.entrySet()) {
                formBuilder.add(entry.getKey(), entry.getValue());
            }
        }
        //生成请求体对象
        RequestBody requestBody = formBuilder.build();
        //将请求提放置到请求对象中
        return getOkHttpClientUtils(context).postRequestBody(urlString, requestBody, tag);
    }

    /**
     * 作用：POST提交Json字符串，再返回相应的数据
     *
     * @param urlString  ：访问网络的url地址
     * @param jsonString ：访问url时，需要传递给服务器的json字符串
     * @return byte[]
     */
    public static String postJsonString(Context context, String urlString, String jsonString,
                                        Object tag) {
        //定义mimetype对象
        /*String MEDIA_TYPE_STREAM = "application/octet-stream;charset=utf-8";
        String MEDIA_TYPE_STRING = "text/plain;charset=utf-8";*/
        String MEDIA_TYPE_JSON = "application/json;charset=utf-8";
        MediaType JSON = MediaType.parse(MEDIA_TYPE_JSON);
        RequestBody requestBody = RequestBody.create(JSON, jsonString);
        return getOkHttpClientUtils(context).postRequestBody(urlString, requestBody, tag);
    }

    ///////////////////////////////////////////////////////////////////////////
    // 异步网络访问
    ///////////////////////////////////////////////////////////////////////////

    /**
     * 开启异步线程访问网络，通过回调方法实现数据加载
     * 如果第二个参数为null，空callback， 则说明不在意返回结果
     *
     * @param urlString
     * @param callback
     */
    public static void getDataAsync(Context context, String urlString, Callback callback, Object
            tag) {
        Request request = getOkHttpClientUtils(context).buildGetRequest(urlString, tag);
        getOkHttpSingletonInstance().newCall(request).enqueue(callback);
    }

    /**
     * 作用：post提交数据，返回服务器端返回的字节数组
     *
     * @param urlString ：访问网络的url地址
     */
    private void postRequestBodyAsync(String urlString, RequestBody requestBody, Callback
            callback, Object tag) {
        Request request = buildPostRequest(urlString, requestBody, tag);
        if (callback == null) {
            new Callback() {

                @Override
                public void onFailure(Call call, IOException e) {

                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {

                }
            };
        }
        okHttpClient.newCall(request).enqueue(callback);
    }


    /**
     * 作用：POST提交键值对，再返回相应的数据
     *
     * @param urlString ：访问网络的url地址
     * @param map       ：访问url时，需要传递给服务器的键值对数据。
     */
    public static void postKeyValuePairAsync(Context context, String urlString, Map<String,
            Object> map, Callback callback, Object tag) {
        //往FormEncodingBuilder对象中放置键值对
        FormBody.Builder builder = new FormBody.Builder();
        if (map != null && !map.isEmpty()) {
            Set set = map.entrySet();
            for (Iterator iter = set.iterator(); iter.hasNext(); ) {
                Map.Entry entry = (Map.Entry) iter.next();
                String key = (String) entry.getKey();
                Object value = entry.getValue();
                builder.add(key, value.toString());
            }
        }
        //生成请求体对象
        RequestBody body = builder.build();
        //将请求提放置到请求对象中
        getOkHttpClientUtils(context).postRequestBodyAsync(urlString, body, callback, tag);
    }

    /**
     * 获取Mime类型
     *
     * @param filename
     * @return
     */
    private static String getMimeType(String filename) {
        FileNameMap fileNameMap = URLConnection.getFileNameMap();
        String contentTypeFor = fileNameMap.getContentTypeFor(filename);
        if (contentTypeFor == null) {
            contentTypeFor = "application/octet-stream";
        }
        return contentTypeFor;
    }


}