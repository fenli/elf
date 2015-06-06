package com.fenlisproject.elf.core.util;

import org.apache.http.NameValuePair;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.List;

public class URLUtils {

    public static String generateUrlEncodedString(List<NameValuePair> params) {
        StringBuilder result = new StringBuilder();
        boolean first = true;
        for (NameValuePair param : params) {
            if (first) first = false;
            else result.append("&");
            try {
                result.append(URLEncoder.encode(param.getName(), "UTF-8"));
                result.append("=");
                result.append(URLEncoder.encode(param.getValue(), "UTF-8"));
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }
        return result.toString();
    }
}
