package com.fenlisproject.elf.core.net;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class StreamRequestBody implements RequestBody {

    private String mContentType;
    private InputStream mContent;

    public StreamRequestBody(InputStream content, String contentType) {
        this.mContent = content;
        this.mContentType = contentType;
    }

    @Override
    public void write(HttpRequest request) throws IOException {
        request.getHttpURLConnection().setRequestProperty("Content-Type", mContentType);
        request.getHttpURLConnection().setDoInput(true);
        request.getHttpURLConnection().setDoOutput(true);
        OutputStream os = request.getHttpURLConnection().getOutputStream();
        DataOutputStream dos = new DataOutputStream(os);
        byte[] buf = new byte[1024];
        try {
            int read;
            while ((read = mContent.read(buf)) != -1) {
                dos.write(buf, 0, read);
            }
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
        dos.flush();
        dos.close();
        os.close();
    }
}
