package org.elf.framework.core.data;

import android.content.Context;
import android.content.SharedPreferences;

import org.elf.framework.core.util.SecurityUtils;

public class SimpleSessionManager {

    private SharedPreferences mPreferences;

    public SimpleSessionManager(Context context, String sessionName) {
        mPreferences = context.getApplicationContext()
                .getSharedPreferences(sessionName, Context.MODE_PRIVATE);
    }

    public synchronized <T> T get(String key, Class<T> tClass) {
        String encodedKey = SecurityUtils.base64Encode(key);
        String encodedValue = mPreferences.getString(encodedKey, null);
        if (encodedValue != null) {
            String decodedValue = SecurityUtils.base64Decode(encodedValue);
            if (tClass == String.class) {
                return (T) decodedValue;
            } else if (tClass == Boolean.class) {
                return (T) new Boolean(decodedValue);
            } else if (tClass == Float.class) {
                return (T) new Float(decodedValue);
            } else if (tClass == Integer.class) {
                return (T) new Integer(decodedValue);
            } else if (tClass == Long.class) {
                return (T) new Long(decodedValue);
            }
        }
        return null;
    }

    public synchronized <T> boolean set(String key, T value) {
        SharedPreferences.Editor editor = mPreferences.edit();
        String encodedKey = SecurityUtils.base64Encode(key);
        String encodedValue = SecurityUtils.base64Encode(String.valueOf(value));
        editor.putString(encodedKey, encodedValue);
        return editor.commit();
    }

    public synchronized boolean remove(String key) {
        SharedPreferences.Editor editor = mPreferences.edit();
        String encodedKey = SecurityUtils.base64Encode(key);
        editor.remove(encodedKey);
        return editor.commit();
    }
}