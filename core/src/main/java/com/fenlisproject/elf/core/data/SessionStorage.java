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

package com.fenlisproject.elf.core.data;

import android.content.Context;

import com.fenlisproject.elf.core.util.SecurityUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectInputStream;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import java.io.Serializable;

public class SessionStorage extends PersistentObjectStorage {

    protected String mStoragePath;

    public SessionStorage(Context context, String path) {
        super(context);
        this.mStoragePath = path;
    }

    public <V extends Serializable> SessionDataWrapper<V> getSessionData(String key, Class<V> vClass) {
        SessionDataWrapper<V> object = (SessionDataWrapper<V>) get(key, SessionDataWrapper.class);
        return object;
    }

    public boolean putSessionData(String key, Serializable value) {
        SessionDataWrapper object = new SessionDataWrapper(value);
        return put(key, object);
    }

    public boolean putSessionData(String key, Serializable value, long lifeSpan) {
        SessionDataWrapper object = new SessionDataWrapper(value, lifeSpan);
        return put(key, object);
    }

    @Override
    public boolean remove(String key) {
        synchronized (this) {
            String hashedKey = SecurityUtils.md5(key);
            return getSessionFile(hashedKey).delete();
        }
    }

    @Override
    public ObjectInput getObjectInput(String key) throws IOException {
        return new ObjectInputStream(new FileInputStream(getSessionFile(key)));
    }

    @Override
    public ObjectOutput getObjectOutput(String key) throws IOException {
        return new ObjectOutputStream(new FileOutputStream(getSessionFile(key)));
    }

    protected File getSessionFile(String key) {
        return new File(mStoragePath, key);
    }
}
