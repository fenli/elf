package com.fenlisproject.elf.core.net.entity;

public class StringFormEntity implements MultipartFormEntity {

    private final String key;
    private final String value;

    public StringFormEntity(String key, String value) {
        this.key = key;
        this.value = value;
    }

    @Override
    public String getKey() {
        return key;
    }

    @Override
    public String getValue() {
        return value;
    }
}
