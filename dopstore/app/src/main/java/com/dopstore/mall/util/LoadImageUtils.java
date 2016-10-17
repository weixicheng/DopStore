package com.dopstore.mall.util;

import android.content.Context;
import android.graphics.Bitmap;
import android.widget.ImageView;

import com.dopstore.mall.R;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

/**
 * 作者：xicheng on 16/8/4 12:55
 * 类别：图片加载工具
 */
public class LoadImageUtils {
    private static LoadImageUtils imageLoadUtils = null;
    public static ImageLoader imageLoader;
    public static DisplayImageOptions options;
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
                .showImageOnLoading(null)
                .showImageForEmptyUri(null)
                .showImageOnFail(null).cacheInMemory(true)
                .cacheOnDisc(true)
                .considerExifParams(true)
                .bitmapConfig(Bitmap.Config.RGB_565).build();
    }

    public static void displayImage(String url, ImageView v) {
        imageLoader.displayImage(url, v, options);
    }

    public void clear() {
        imageLoader.clearDiscCache();
        imageLoader.clearMemoryCache();
    }
}
