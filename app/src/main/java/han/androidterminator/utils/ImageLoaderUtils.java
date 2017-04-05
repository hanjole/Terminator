package com.ailk.mobile.b2bclient.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;
import android.widget.ImageView;

import com.ailk.mobile.b2bclient.R;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.cache.memory.impl.LruMemoryCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.nostra13.universalimageloader.core.download.BaseImageDownloader;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.nostra13.universalimageloader.core.listener.ImageLoadingProgressListener;

import java.io.File;


public class ImageLoaderUtils {

    public static void init(Context context) {
        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(context)
                .threadPoolSize(3)
                .threadPriority(Thread.NORM_PRIORITY - 2)
                .tasksProcessingOrder(QueueProcessingType.FIFO)
                .denyCacheImageMultipleSizesInMemory()
                .diskCacheFileNameGenerator(new Md5FileNameGenerator())
                .diskCacheSize(1000 * 1024 * 1024) // 100 MiB
                .memoryCache(new LruMemoryCache(32 * 1024 * 1024))// 可以通过自己的内存缓存实现
//		.writeDebugLogs()

                .imageDownloader(new BaseImageDownloader(context)).build();
        ImageLoader.getInstance().init(config);

    }

    private static DisplayImageOptions optionsAnimate;
    private static DisplayImageOptions options;

    private static DisplayImageOptions getDisplayImageOptions(boolean animate) {
        if (animate) {
            if (null == optionsAnimate)
                optionsAnimate = new DisplayImageOptions.Builder()
                        .showImageForEmptyUri(R.drawable.defaultpic).showImageOnFail(R.drawable.defaultpic)
                        .cacheInMemory(true).cacheOnDisk(true).considerExifParams(true)
                        .bitmapConfig(Bitmap.Config.RGB_565).resetViewBeforeLoading(true)
                        .displayer(new FadeInBitmapDisplayer(600, true, false, false)).build();
            return optionsAnimate;
        } else {
            if (null == options)
                options = new DisplayImageOptions.Builder()
                        .showImageForEmptyUri(R.drawable.defaultpic).showImageOnFail(R.drawable.defaultpic)
                        .cacheInMemory(true).cacheOnDisk(true).considerExifParams(true)
                        .bitmapConfig(Bitmap.Config.RGB_565).build();
            return options;
        }
    }

    public static void displayImage(String uri, ImageView imageView, boolean animate) {
        displayImage(uri, imageView, animate, null, null);
    }

    public static void displayImage(String uri, ImageView imageView, boolean animate, ImageLoadingListener listener,
                                    ImageLoadingProgressListener progressListener) {
        ImageLoader.getInstance().displayImage(uri, imageView, getDisplayImageOptions(animate), listener,
                progressListener);
    }

    public static void loadImage(String uri, ImageLoadingListener listener) {
        ImageLoader.getInstance().loadImage(uri, getDisplayImageOptions(false), listener);
    }

    public static void clearMemoryCache() {
        ImageLoader.getInstance().clearMemoryCache();
    }

    public static void clearDiskCache() {

        ImageLoader.getInstance().clearDiskCache();
    }

    public static File getDiskCacheFile() {
        File file = ImageLoader.getInstance().getDiskCache().getDirectory().getAbsoluteFile();
//		Log.e("getDiskCacheSize" , ImageLoader.getInstance().getDiskCache().getDirectory().getAbsolutePath());
        return file;
    }

    public static String getDiskCachePath() {
        String file = ImageLoader.getInstance().getDiskCache().getDirectory().getAbsolutePath();
//		Log.e("getDiskCacheSize" , ImageLoader.getInstance().getDiskCache().getDirectory().getAbsolutePath());
        return file;
    }
}
