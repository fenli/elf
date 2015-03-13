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

import android.support.v4.util.LruCache;

public class MemoryStorage<T> implements DataStorage<T> {

    private final LruCache<String, T> cache;

    public MemoryStorage(int maxSize) {
        cache = new LruCache<>(maxSize);
    }

    @Override
    public T get(String key) {
        return cache.get(key);
    }

    @Override
    public boolean put(String key, T value) {
        return cache.put(key, value) != null;
    }

    @Override
    public boolean remove(String key) {
        return cache.remove(key) != null;
    }
}
