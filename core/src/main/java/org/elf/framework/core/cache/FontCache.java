package org.elf.framework.core.cache;

import android.graphics.Typeface;
import android.support.v4.util.LruCache;

import org.elf.framework.core.config.Configs;

public class FontCache {

    private static FontCache sInstance = null;
    private final LruCache<String, Typeface> sTypefaceCache;

    private FontCache() {
        sTypefaceCache = new LruCache<String, Typeface>(Configs.DEFAULT_FONT_CACHE_SIZE);
    }

    public static FontCache getInstance() {
        if (sInstance == null) {
            sInstance = new FontCache();
        }
        return sInstance;
    }

    public Typeface get(String key) {
        return sTypefaceCache.get(key);
    }

    public void put(String key, Typeface typeface) {
        sTypefaceCache.put(key, typeface);
    }
}
