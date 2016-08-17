package com.fenlisproject.elf.core.net;

import com.fenlisproject.elf.core.util.URLUtils;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.List;

public class UrlEncodedFormData implements RequestBody {

    private String mContentType;
    private List<KeyValuePair> mContent;

    public UrlEncodedFormData() {
        mContent = new ArrayList<>();
        mContentType = "application/x-www-form-urlencoded";
    }

    public UrlEncodedFormData add(String key, String value) {
        mContent.add(new KeyValuePair(key, value));
        return this;
    }

    @Override
    public void write(HttpRequest request) throws IOException {
        request.getConnection().setRequestProperty("Content-Type", mContentType);
        request.getConnection().setDoInput(true);
        request.getConnection().setDoOutput(true);
        OutputStream os = request.getConnection().getOutputStream();
        BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(os, "UTF-8"));
        writer.write(URLUtils.generateUrlEncodedString(mContent));
        writer.flush();
        writer.close();
        os.close();
    }
}
