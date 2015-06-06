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

package com.fenlisproject.elf.core.net;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.fenlisproject.elf.core.util.URLUtils;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.ProtocolException;
import java.net.URL;
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
            return mConnection.getInputStream();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public InputStreamReader getStreamReader() {
        InputStream is = getInputStream();
        if (is != null) {
            return new InputStreamReader(new BufferedInputStream(is));
        } else {
            return new InputStreamReader(new BufferedInputStream(mConnection.getErrorStream()));
        }
    }

    public Bitmap getBitmapContent() {
        return BitmapFactory.decodeStream(getInputStream());
    }

    public String getTextContent() {
        try {
            BufferedReader r = new BufferedReader(getStreamReader());
            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = r.readLine()) != null) {
                sb.append(line);
            }
            return sb.toString();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static class Builder {

        public static final int DEFAULT_CONNECTION_TIMEOUT = 12000;
        public static final int DEFAULT_READ_TIMEOUT = 30000;
        private int mConnectionTimeout;
        private int mReadTimeout;
        private boolean isUseCache;
        private String mRequestUrl;
        private RequestMethod mRequestMethod;
        private List<NameValuePair> mHeaders;
        private List<NameValuePair> mUrlParams;
        private RequestBody mRequestBody;
        private boolean isForceUseCache;
        private int mRetryCount;

        public Builder(String url) {
            this.mRequestUrl = url;
            this.isUseCache = false;
            this.isForceUseCache = false;
            this.mRequestMethod = RequestMethod.GET;
            this.mUrlParams = new ArrayList<>();
            this.mHeaders = new ArrayList<>();
            this.mConnectionTimeout = DEFAULT_CONNECTION_TIMEOUT;
            this.mReadTimeout = DEFAULT_READ_TIMEOUT;
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

        public void setRequestBody(RequestBody requestBody) {
            this.mRequestBody = requestBody;
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

        private String getFullPathRequestUrl() {
            String queryString = URLUtils.generateUrlEncodedString(mUrlParams);
            if (queryString.length() > 0) {
                return mRequestUrl + "?" + queryString;
            } else {
                return mRequestUrl;
            }
        }

        public HttpRequest create() {
            HttpRequest request = null;
            while (mRetryCount-- > 0) {
                try {
                    request = new HttpRequest(getFullPathRequestUrl());
                    request.getHttpURLConnection().setUseCaches(isUseCache);
                    request.getHttpURLConnection().setRequestMethod(mRequestMethod.name());
                    request.getHttpURLConnection().setConnectTimeout(mConnectionTimeout);
                    request.getHttpURLConnection().setReadTimeout(mReadTimeout);
                    for (NameValuePair header : mHeaders) {
                        request.getHttpURLConnection()
                                .setRequestProperty(header.getName(), header.getValue());
                    }
                    switch (mRequestMethod) {
                        case POST:
                        case PUT:
                        case DELETE:
                            if (mRequestBody != null) {
                                mRequestBody.write(request);
                            }
                            break;
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
                } catch (NullPointerException e) {
                    e.printStackTrace();
                }
            }
            return request;
        }
    }
}
