/*
* Copyright (C) 2015 Steven Lewi
*
* Licensed under the Apache License, Version 2.0 (the "License");
* you may not use this file except in compliance with the License.
* You may obtain a copy of the License at
*
*      http://www.apache.org/licenses/LICENSE-2.0
*
* Unless required by applicable law or agreed to in writing, software
* distributed under the License is distributed on an "AS IS" BASIS,
* WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
* See the License for the specific language governing permissions and
* limitations under the License.
*/

package com.fenlisproject.elf.core.base;

import android.app.Application;
import android.graphics.Typeface;

import com.fenlisproject.elf.core.config.AppEnvironment;
import com.fenlisproject.elf.core.config.Configs;
import com.fenlisproject.elf.core.data.MemoryStorage;
import com.fenlisproject.elf.core.data.PreferencesStorage;
import com.fenlisproject.elf.core.data.SessionStorage;

public class BaseApplication extends Application {

    private AppEnvironment mAppEnvironment;
    private PreferencesStorage mDefaultPreferencesStorage;
    private SessionStorage mDefaultSessionStorage;
    private MemoryStorage<Typeface> fontCache;

    @Override
    public void onCreate() {
        super.onCreate();
        mAppEnvironment = new AppEnvironment(this);
        mDefaultPreferencesStorage = new PreferencesStorage(this, Configs.DEFAULT_PREFERENCES_NAME);
        mDefaultSessionStorage = new SessionStorage(this, mAppEnvironment.getSessionDirectory());
        fontCache = new MemoryStorage<>(Configs.DEFAULT_FONT_CACHE_SIZE);
    }

    public AppEnvironment getAppEnvironment() {
        return mAppEnvironment;
    }

    public PreferencesStorage getDefaultPreferencesStorage() {
        return mDefaultPreferencesStorage;
    }

    public SessionStorage getDefaultSessionStorage() {
        return mDefaultSessionStorage;
    }

    public MemoryStorage<Typeface> getFontCache() {
        return fontCache;
    }
}
