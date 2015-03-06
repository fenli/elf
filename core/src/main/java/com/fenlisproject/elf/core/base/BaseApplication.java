package com.fenlisproject.elf.core.base;

import android.app.Application;
import android.graphics.Typeface;

import com.fenlisproject.elf.core.config.AppEnvironment;
import com.fenlisproject.elf.core.config.Configs;
import com.fenlisproject.elf.core.data.MemoryStorage;
import com.fenlisproject.elf.core.data.PersistentStorage;

public class BaseApplication extends Application {

    private static BaseApplication sInstance;
    private AppEnvironment mAppEnvironment;
    private PersistentStorage mDefaultSessionStorage;
    private MemoryStorage<Typeface> fontCache;

    public BaseApplication() {
        super();
        sInstance = this;
    }

    public static BaseApplication getInstance() {
        return sInstance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mAppEnvironment = new AppEnvironment(this);
        mDefaultSessionStorage = new PersistentStorage(this, Configs.DEFAULT_SESSION_BUNDLE_NAME);
        fontCache = new MemoryStorage<>(Configs.DEFAULT_FONT_CACHE_SIZE);
    }

    public AppEnvironment getAppEnvironment() {
        return mAppEnvironment;
    }

    public PersistentStorage getDefaultSessionStorage() {
        return mDefaultSessionStorage;
    }

    public MemoryStorage<Typeface> getFontCache() {
        return fontCache;
    }
}
