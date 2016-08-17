package com.fenlisproject.elf.core.util;

import com.fenlisproject.elf.core.net.KeyValuePair;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.List;

public class URLUtils {

    public static String generateUrlEncodedString(List<KeyValuePair> params) {
        StringBuilder result = new StringBuilder();
        boolean first = true;
        for (KeyValuePair param : params) {
            String name = param.getKey();
            String value = param.getValue();
            if (name != null && value != null) {
                if (first) first = false;
                else result.append("&");
                try {
                    result.append(URLEncoder.encode(name, "UTF-8"));
                    result.append("=");
                    result.append(URLEncoder.encode(value, "UTF-8"));
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
            }
        }
        return result.toString();
    }
}
