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
