package com.fenlisproject.elf.core.data;


public interface DataStorage<T> {

    public T get(String key);

    public boolean put(String key, T value);

    public boolean remove(String key);
}
