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

import com.fenlisproject.elf.core.net.entity.FileFormData;
import com.fenlisproject.elf.core.net.entity.MultipartFormData;
import com.fenlisproject.elf.core.net.entity.StringFormData;
import com.fenlisproject.elf.core.util.FileUtils;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
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
        public static final String CRLF = "\r\n";
        public static final String TWO_HYPHENS = "--";
        private static final String BOUNDARY = "ElfHttpRequestBoundary";
        private int mConnectionTimeout;
        private boolean isUseCache;
        private String mRequestUrl;
        private RequestMethod mRequestMethod;
        private List<NameValuePair> mUrlParams;
        private List<MultipartFormData> mFormData;
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
            this.mFormData.add(new StringFormData(key, value));
            return this;
        }

        public Builder addFormData(String key, File value) {
            this.mFormData.add(new FileFormData(key, value));
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
                    boolean canContainsBody = mRequestMethod == RequestMethod.POST
                            || mRequestMethod == RequestMethod.PUT
                            || mRequestMethod == RequestMethod.DELETE;
                    if (canContainsBody && mFormData.size() > 0) {
                        request.getHttpURLConnection().setRequestProperty(
                                "Content-Type",
                                "multipart/form-data; boundary=" + BOUNDARY);
                        request.getHttpURLConnection().setDoInput(true);
                        request.getHttpURLConnection().setDoOutput(true);
                        OutputStream os = request.getHttpURLConnection().getOutputStream();
                        DataOutputStream dos = new DataOutputStream(os);
                        for (MultipartFormData data : mFormData) {
                            if (data instanceof StringFormData) {
                                StringFormData stringData = ((StringFormData) data);
                                dos.writeBytes(TWO_HYPHENS + BOUNDARY + CRLF);
                                dos.writeBytes(String.format(
                                        "Content-Disposition: form-data; name=\"%s\"",
                                        stringData.getKey()
                                ));
                                dos.writeBytes(CRLF + CRLF);
                                dos.writeBytes(stringData.getValue());
                                dos.writeBytes(CRLF);
                            } else if (data instanceof FileFormData) {
                                FileFormData fileData = ((FileFormData) data);
                                if (fileData.getValue().exists()) {
                                    String mime = FileUtils.getMimeType(fileData.getValue());
                                    dos.writeBytes(TWO_HYPHENS + BOUNDARY + CRLF);
                                    dos.writeBytes(String.format(
                                            "Content-Disposition: form-data; name=\"%s\"; " +
                                                    "filename=\"%s\"",
                                            fileData.getKey(),
                                            fileData.getValue().getName()
                                    ));
                                    if (mime != null) {
                                        dos.writeBytes(CRLF);
                                        dos.writeBytes(String.format("Content-Type: %s", mime));
                                    }
                                    dos.writeBytes(CRLF + CRLF);
                                    FileInputStream fis = new FileInputStream(fileData.getValue());
                                    byte[] buf = new byte[1024];
                                    try {
                                        int read;
                                        while ((read = fis.read(buf)) != -1) {
                                            dos.write(buf, 0, read);
                                        }
                                    } catch (IOException ioe) {
                                        ioe.printStackTrace();
                                    }
                                    dos.writeBytes(CRLF);
                                }
                            }
                        }
                        dos.writeBytes(TWO_HYPHENS + BOUNDARY + TWO_HYPHENS);
                        dos.flush();
                        dos.close();
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
