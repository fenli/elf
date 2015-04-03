package com.fenlisproject.elf.core.net.entity;

import java.io.File;

public class FileFormData implements MultipartFormData {

    private final String key;
    private final File value;

    public FileFormData(String key, File value) {
        this.key = key;
        this.value = value;
    }

    @Override
    public String getKey() {
        return key;
    }

    @Override
    public File getValue() {
        return value;
    }
}
