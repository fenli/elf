package com.fenlisproject.elf.core.net;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;

public class RawRequestBody implements RequestBody {

    private String mContentType;
    private String mContent;

    public RawRequestBody(String content, String contentType) {
        this.mContent = content;
        this.mContentType = contentType;
    }

    @Override
    public void write(HttpRequest request) throws IOException {
        request.getHttpURLConnection().setRequestProperty("Content-Type", mContentType);
        request.getHttpURLConnection().setDoInput(true);
        request.getHttpURLConnection().setDoOutput(true);
        OutputStream os = request.getHttpURLConnection().getOutputStream();
        BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(os, "UTF-8"));
        writer.write(mContent);
        writer.flush();
        writer.close();
        os.close();
    }
}
