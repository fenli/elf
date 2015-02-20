package org.elf.framework.core.base;

import android.app.Application;

import org.elf.framework.core.config.AppEnvironment;
import org.elf.framework.core.config.Configs;
import org.elf.framework.core.data.FileSessionManager;
import org.elf.framework.core.data.SimpleSessionManager;

public class BaseApplication extends Application {

    private static BaseApplication sInstance;
    private AppEnvironment mAppEnvironment;
    private SimpleSessionManager mDefaultSessionManager;
    private FileSessionManager mFileSessionManager;

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
        mDefaultSessionManager = new SimpleSessionManager(this, Configs.DEFAULT_SESSION_NAME);
        mFileSessionManager = new FileSessionManager(this, Configs.DEFAULT_SESSION_NAME);
    }

    public AppEnvironment getAppEnvironment() {
        return mAppEnvironment;
    }

    public SimpleSessionManager getDefaultSessionManager() {
        return mDefaultSessionManager;
    }

    public FileSessionManager getFileSessionManager() {
        return mFileSessionManager;
    }
}
