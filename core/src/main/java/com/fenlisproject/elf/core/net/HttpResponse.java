package com.fenlisproject.elf.core.net;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.fenlisproject.elf.core.io.IOUtils;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.zip.GZIPInputStream;

public class HttpResponse {

    private int mResponseCode;
    private String mResponseMessage;
    private InputStream mResponseStream;
    private HashMap<String, String> mResponseHeaders;

    public HttpResponse(HttpURLConnection connection) throws IOException {
        this.mResponseCode = connection.getResponseCode();
        this.mResponseMessage = connection.getResponseMessage();
        this.mResponseHeaders = new HashMap<>();
        Map<String, List<String>> map = connection.getHeaderFields();
        for (String key : map.keySet()) {
            this.mResponseHeaders.put(key, connection.getHeaderField(key));
        }
        if (isClientError() || isServerError()) {
            this.mResponseStream = connection.getErrorStream();
        } else {
            this.mResponseStream = connection.getInputStream();
        }
    }

    public int getCode() {
        return mResponseCode;
    }

    public String getMessage() {
        return mResponseMessage;
    }

    public boolean isSuccess() {
        return mResponseCode >= 200 && mResponseCode < 300;
    }

    public boolean isRedirection() {
        return mResponseCode >= 300 && mResponseCode < 400;
    }

    public boolean isClientError() {
        return mResponseCode >= 400 && mResponseCode < 500;
    }

    public boolean isServerError() {
        return mResponseCode >= 500 && mResponseCode < 600;
    }

    public InputStream getInputStream() throws IOException {
        String encoding = mResponseHeaders.get("Content-Encoding");
        if (ContentEncoding.GZIP.equals(encoding)) {
            return new GZIPInputStream(mResponseStream);
        } else {
            return mResponseStream;
        }
    }

    public InputStreamReader getStreamReader() throws IOException {
        return new InputStreamReader(new BufferedInputStream(getInputStream()));
    }

    public HashMap<String, String> getHeaders() {
        return mResponseHeaders;
    }

    public <T> T getBody(Class<T> classOfT) {
        try {
            if (classOfT == String.class) {
                return (T) IOUtils.decodeStreamReader(getStreamReader());
            } else if (classOfT == Bitmap.class) {
                return (T) BitmapFactory.decodeStream(getInputStream());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
