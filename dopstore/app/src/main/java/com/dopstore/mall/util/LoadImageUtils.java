package com.dopstore.mall.util;

import android.content.Context;
import android.graphics.Bitmap;
import android.widget.ImageView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.imageaware.ImageAware;
import com.dopstore.mall.R;

/**
 * 作者：xicheng on 16/8/4 12:55
 * 类别：图片加载工具
 */
public class LoadImageUtils {
    private static LoadImageUtils imageLoadUtils = null;
    public static ImageLoader imageLoader;
    public static DisplayImageOptions options;
    public static DisplayImageOptions spe_options;
    private static Context context;

    private LoadImageUtils() {

    }

    public static LoadImageUtils getInstance(Context mContext) {
        if (imageLoadUtils == null) {
            imageLoadUtils = new LoadImageUtils();
            context = mContext;
            init();
        }
        return imageLoadUtils;
    }

    private static void init() {
        imageLoader = ImageLoader.getInstance();
        imageLoader.init(ImageLoaderConfiguration.createDefault(context));
        options = new DisplayImageOptions.Builder()
                .showImageOnLoading(R.mipmap.ic_launcher)
                .showImageForEmptyUri(R.mipmap.ic_launcher)
                .showImageOnFail(R.mipmap.ic_launcher).cacheInMemory(true)
                .cacheOnDisc(true)
                .considerExifParams(true)
                .bitmapConfig(Bitmap.Config.RGB_565).build();
        spe_options = new DisplayImageOptions.Builder()
                .showImageOnLoading(R.mipmap.ic_launcher)
                .showImageForEmptyUri(R.mipmap.ic_launcher)
                .showImageOnFail(R.mipmap.ic_launcher).cacheInMemory(true)
                .cacheOnDisc(false)
                .considerExifParams(true)
                .bitmapConfig(Bitmap.Config.RGB_565).build();
    }

    public static void displayImage(String url, ImageView v, int type) {
        switch (type) {
            case Constant.OPTIONS_SPECIAL_CODE: {//缓存到内存与Sd
                imageLoader.displayImage(url, v, options);
            }
            break;
            case Constant.OPTIONS_NORMAL_CODE: {//缓存内存
                imageLoader.displayImage(url, v, spe_options);
            }
            break;

        }
    }

    public static void displayAware(String url, ImageAware v, int type) {
        switch (type) {
            case Constant.OPTIONS_SPECIAL_CODE: {//缓存到内存与Sd
                imageLoader.displayImage(url, v, options);
            }
            break;
            case Constant.OPTIONS_NORMAL_CODE: {//缓存内存
                imageLoader.displayImage(url, v, spe_options);
            }
            break;

        }
    }

    public void clear() {
        imageLoader.clearDiscCache();
        imageLoader.clearMemoryCache();
    }
}
