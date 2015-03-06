package com.fenlisproject.elf.core.net;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.ProtocolException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

public class HttpRequest {

    private HttpURLConnection mConnection;

    private HttpRequest(String url) throws IOException {
        mConnection = (HttpURLConnection) new URL(url).openConnection();
    }

    public HttpURLConnection getHttpURLConnection() {
        return mConnection;
    }

    public void closeConnection() {
        try {
            mConnection.disconnect();
            mConnection = null;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public InputStream getInputStream() {
        try {
            return getHttpURLConnection().getInputStream();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public InputStreamReader getStreamReader() {
        try {
            return new InputStreamReader(mConnection.getInputStream());
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public Bitmap getBitmapContent() {
        Bitmap bitmap = null;
        try {
            bitmap = BitmapFactory.decodeStream(mConnection.getInputStream());
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        } catch (OutOfMemoryError e) {
            System.gc();
            e.printStackTrace();
        }
        return bitmap;
    }

    public String getTextContent() {
        String text = null;
        try {
            BufferedReader r = new BufferedReader(getStreamReader());
            StringBuilder total = new StringBuilder();
            String line;
            while ((line = r.readLine()) != null) {
                total.append(line);
            }
            text = total.toString();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return text;
    }

    public static class Builder {

        public static final int DEFAULT_CONNECTION_TIMEOUT = 8000;
        private int mConnectionTimeout;
        private boolean isUseCache;
        private String mRequestUrl;
        private String mRequestBody;
        private RequestMethod mRequestMethod;
        private List<NameValuePair> mUrlParams;
        private List<NameValuePair> mFormData;
        private List<NameValuePair> mHeaders;
        private boolean isForceUseCache;
        private int mRetryCount;

        public Builder(String url) {
            this.mRequestUrl = url;
            this.isUseCache = false;
            this.isForceUseCache = false;
            this.mRequestMethod = RequestMethod.GET;
            this.mUrlParams = new ArrayList<>();
            this.mFormData = new ArrayList<>();
            this.mHeaders = new ArrayList<>();
            this.mConnectionTimeout = DEFAULT_CONNECTION_TIMEOUT;
            this.mRetryCount = 1;
        }

        public Builder addRequestHeader(String key, String value) {
            this.mHeaders.add(new BasicNameValuePair(key, value));
            return this;
        }

        public Builder addUrlParams(String key, String value) {
            this.mUrlParams.add(new BasicNameValuePair(key, value));
            return this;
        }

        public Builder addFormData(String key, String value) {
            this.mFormData.add(new BasicNameValuePair(key, value));
            return this;
        }

        public Builder setUseCache(boolean useCache) {
            this.isUseCache = useCache;
            return this;
        }

        public Builder setURL(String url) {
            this.mRequestUrl = url;
            return this;
        }

        public Builder setRequestMethod(RequestMethod method) {
            this.mRequestMethod = method;
            return this;
        }

        public void setRequestBody(String mRequestBody) {
            this.mRequestBody = mRequestBody;
        }

        public Builder setForceCache(boolean forceUseCache) {
            this.isForceUseCache = forceUseCache;
            return this;
        }

        public Builder setConnectionTimeout(int connectionTimeout) {
            this.mConnectionTimeout = connectionTimeout;
            return this;
        }

        public void setRetryCount(int retryCount) {
            this.mRetryCount = retryCount;
        }

        private String generateQueryString() {
            StringBuilder result = new StringBuilder();
            boolean first = true;
            for (NameValuePair params : mUrlParams) {
                if (first) first = false;
                else result.append("&");
                try {
                    result.append(URLEncoder.encode(params.getName(), "UTF-8"));
                    result.append("=");
                    result.append(URLEncoder.encode(params.getValue(), "UTF-8"));
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
            }
            return result.toString();
        }

        private String generateUrlEncodedFormData() {
            StringBuilder result = new StringBuilder();
            boolean first = true;
            for (NameValuePair params : mFormData) {
                if (first) first = false;
                else result.append("&");
                try {
                    result.append(URLEncoder.encode(params.getName(), "UTF-8"));
                    result.append("=");
                    result.append(URLEncoder.encode(params.getValue(), "UTF-8"));
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
            }
            return result.toString();
        }

        private String getFullPathRequestUrl() {
            String queryString = generateQueryString();
            if (queryString.length() > 0) {
                return mRequestUrl + "?" + queryString;
            } else {
                return mRequestUrl;
            }
        }

        public HttpRequest create() {
            HttpRequest request = null;
            try {
                request = new HttpRequest(getFullPathRequestUrl());
            } catch (IOException e) {
                e.printStackTrace();
            }
            while (request != null && request.getHttpURLConnection() != null && mRetryCount-- > 0) {
                try {
                    request.getHttpURLConnection().setUseCaches(isUseCache);
                    request.getHttpURLConnection().setRequestMethod(mRequestMethod.name());
                    request.getHttpURLConnection().setConnectTimeout(mConnectionTimeout);
                    for (NameValuePair header : mHeaders) {
                        request.getHttpURLConnection()
                                .setRequestProperty(header.getName(), header.getValue());
                    }
                    if (mRequestMethod == RequestMethod.POST && mFormData.size() > 0) {
                        request.getHttpURLConnection()
                                .setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
                        mRequestBody = generateUrlEncodedFormData();
                    }
                    if (mRequestBody != null) {
                        request.getHttpURLConnection().setDoInput(true);
                        request.getHttpURLConnection().setDoOutput(true);
                        OutputStream os = request.getHttpURLConnection().getOutputStream();
                        BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(os, "UTF-8"));
                        writer.write(mRequestBody);
                        writer.flush();
                        writer.close();
                        os.close();
                    }
                    if (isForceUseCache) {
                        request.getHttpURLConnection()
                                .addRequestProperty("Cache-Control", "only-if-cached");
                    }
                    mRetryCount = 0;
                } catch (ProtocolException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            return request;
        }
    }
}
