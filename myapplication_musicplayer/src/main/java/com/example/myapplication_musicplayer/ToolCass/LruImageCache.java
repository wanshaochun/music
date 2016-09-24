package com.example.myapplication_musicplayer.ToolCass;

import android.graphics.Bitmap;
import android.util.LruCache;

import com.android.volley.toolbox.ImageLoader;

/**
 * Created by wansh on 2016/9/22.
 */
public class LruImageCache implements ImageLoader.ImageCache {

    private static LruCache<String, Bitmap> mMemoryCache;
    private static LruImageCache lruImageCache;

    private LruImageCache() {
        // Get the Max available memory
        int maxMemory = (int) Runtime.getRuntime().maxMemory();
        int cacheSize = maxMemory / 8;
        mMemoryCache = new LruCache<String, Bitmap>(cacheSize) {
            @Override
            protected int sizeOf(String key, Bitmap bitmap) {
                return bitmap.getRowBytes() * bitmap.getHeight();
            }
        };
    }

    public static LruImageCache instance() {
        if (lruImageCache == null) {
            lruImageCache = new LruImageCache();
        }
        return lruImageCache;
    }

    @Override
    public Bitmap getBitmap(String s) {
        return mMemoryCache.get(s);
    }

    @Override
    public void putBitmap(String s, Bitmap bitmap) {
        if (getBitmap(s) == null) {
            mMemoryCache.put(s, bitmap);
        }
    }
}
