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

import com.fenlisproject.elf.core.io.KeyBasedObjectIO;
import com.fenlisproject.elf.core.util.SecurityUtils;

import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectInputStream;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import java.io.Serializable;

public class PersistentObjectStorage implements DataStorage<Serializable>, KeyBasedObjectIO {

    protected Context mContext;

    public PersistentObjectStorage(Context context) {
        this.mContext = context.getApplicationContext();
    }

    @Override
    public Serializable get(String key) {
        return get(key, Serializable.class);
    }

    public <V extends Serializable> V get(String key, Class<V> vClass) {
        synchronized (this) {
            String hashedKey = SecurityUtils.md5(key);
            try {
                ObjectInput oi = getObjectInput(hashedKey);
                V object = (V) oi.readObject();
                oi.close();
                return object;
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }
    }

    @Override
    public boolean put(String key, Serializable value) {
        synchronized (this) {
            String hashedKey = SecurityUtils.md5(key);
            try {
                ObjectOutput oo = getObjectOutput(hashedKey);
                oo.writeObject(value);
                oo.close();
                return true;
            } catch (IOException e) {
                e.printStackTrace();
            }
            return false;
        }
    }

    @Override
    public boolean remove(String key) {
        synchronized (this) {
            String hashedKey = SecurityUtils.md5(key);
            return mContext.deleteFile(hashedKey);
        }
    }

    @Override
    public ObjectInput getObjectInput(String key) throws IOException {
        return new ObjectInputStream(mContext.openFileInput(key));
    }

    @Override
    public ObjectOutput getObjectOutput(String key) throws IOException {
        return new ObjectOutputStream(mContext.openFileOutput(key, Context.MODE_PRIVATE));
    }
}