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
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.graphics.Typeface;
import android.util.Base64;

import com.fenlisproject.elf.core.config.AppEnvironment;
import com.fenlisproject.elf.core.config.Configs;
import com.fenlisproject.elf.core.data.MemoryStorage;
import com.fenlisproject.elf.core.data.PreferencesStorage;
import com.fenlisproject.elf.core.data.SecureSessionStorage;
import com.fenlisproject.elf.core.data.SessionStorage;
import com.fenlisproject.elf.core.util.SecurityUtils;

import java.security.Key;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import javax.crypto.Cipher;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;

public class BaseApplication extends Application {

    private AppEnvironment mAppEnvironment;
    private PreferencesStorage mDefaultPreferencesStorage;
    private SessionStorage mDefaultSessionStorage;
    private SecureSessionStorage mSecureSessionStorage;
    private MemoryStorage<Typeface> fontCache;

    @Override
    public void onCreate() {
        super.onCreate();
        mAppEnvironment = new AppEnvironment(this);
        mDefaultPreferencesStorage = new PreferencesStorage(this, Configs.DEFAULT_PREFERENCES_NAME);
        mDefaultSessionStorage = new SessionStorage(this, mAppEnvironment.getSessionDirectory());
        mSecureSessionStorage = new SecureSessionStorage(this,
                getAppEnvironment().getSessionDirectory(),
                getSecureSessionCipher(),
                getSecureSessionKeySpec());
        fontCache = new MemoryStorage<>(Configs.DEFAULT_FONT_CACHE_SIZE);
    }

    protected Key getSecureSessionKeySpec() {
        String secretKey = SecurityUtils.md5("ElfDefaultSecret");
        try {
            PackageInfo info = getPackageManager().getPackageInfo(getPackageName(),
                    PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md;
                md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                secretKey = new String(Base64.encode(md.digest(), 0));
                break;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new SecretKeySpec(secretKey.substring(0, 16).getBytes(), "AES");
    }

    protected Cipher getSecureSessionCipher() {
        try {
            return Cipher.getInstance("AES");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (NoSuchPaddingException e) {
            e.printStackTrace();
        }
        return null;
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

    public SecureSessionStorage getSecureSessionStorage() {
        return mSecureSessionStorage;
    }

    public MemoryStorage<Typeface> getFontCache() {
        return fontCache;
    }
}
