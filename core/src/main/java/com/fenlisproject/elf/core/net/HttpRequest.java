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

import com.fenlisproject.elf.core.util.URLUtils;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.ProtocolException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class HttpRequest {

    private HttpURLConnection mConnection;
    private HttpResponse mResponse;

    private HttpRequest(String url) throws IOException {
        mConnection = (HttpURLConnection) new URL(url).openConnection();
    }

    private void setResponse(HttpResponse mResponse) {
        this.mResponse = mResponse;
    }

    public HttpURLConnection getConnection() {
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

    public HttpResponse getResponse() {
        return mResponse;
    }

    public static class Builder {

        public static final int DEFAULT_CONNECTION_TIMEOUT = 12000;
        public static final int DEFAULT_READ_TIMEOUT = 30000;
        private int mConnectionTimeout;
        private int mReadTimeout;
        private boolean isUseCache;
        private String mRequestUrl;
        private RequestMethod mRequestMethod;
        private List<KeyValuePair> mHeaders;
        private List<KeyValuePair> mUrlParams;
        private RequestBody mRequestBody;
        private boolean isForceUseCache;

        public Builder(String url) {
            this.mRequestUrl = url;
            this.isUseCache = false;
            this.isForceUseCache = false;
            this.mRequestMethod = RequestMethod.GET;
            this.mUrlParams = new ArrayList<>();
            this.mHeaders = new ArrayList<>();
            this.mConnectionTimeout = DEFAULT_CONNECTION_TIMEOUT;
            this.mReadTimeout = DEFAULT_READ_TIMEOUT;
        }

        public Builder addRequestHeader(String key, String value) {
            this.mHeaders.add(new KeyValuePair(key, value));
            return this;
        }

        public Builder addUrlParams(String key, String value) {
            this.mUrlParams.add(new KeyValuePair(key, value));
            return this;
        }

        public Builder setRequestBody(RequestBody requestBody) {
            this.mRequestBody = requestBody;
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

        public Builder setForceCache(boolean forceUseCache) {
            this.isForceUseCache = forceUseCache;
            return this;
        }

        public Builder setConnectionTimeout(int connectionTimeout) {
            this.mConnectionTimeout = connectionTimeout;
            return this;
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
            try {
                request = new HttpRequest(getFullPathRequestUrl());
                request.getConnection().setUseCaches(isUseCache);
                request.getConnection().setRequestMethod(mRequestMethod.name());
                request.getConnection().setConnectTimeout(mConnectionTimeout);
                request.getConnection().setReadTimeout(mReadTimeout);
                for (KeyValuePair header : mHeaders) {
                    request.getConnection().setRequestProperty(header.getKey(), header.getValue());
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
                    request.getConnection().addRequestProperty("Cache-Control", "only-if-cached");
                }
                request.setResponse(new HttpResponse(request.getConnection()));
            } catch (ProtocolException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (NullPointerException e) {
                e.printStackTrace();
            }
            return request;
        }
    }

    @Override
    protected void finalize() throws Throwable {
        closeConnection();
        super.finalize();
    }
}
