package com.fenlisproject.elf.core.net;

import com.fenlisproject.elf.core.util.FileUtils;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;

public class BinaryRequestBody implements RequestBody {

    private String mContentType;
    private File mContent;

    public BinaryRequestBody(File content) {
        this.mContent = content;
        this.mContentType = FileUtils.getMimeType(content);
    }

    public BinaryRequestBody(File content, String contentType) {
        this.mContent = content;
        this.mContentType = contentType;
    }

    @Override
    public void write(HttpRequest request) throws IOException {
        request.getConnection().setRequestProperty("Content-Type", mContentType);
        request.getConnection().setDoInput(true);
        request.getConnection().setDoOutput(true);
        OutputStream os = request.getConnection().getOutputStream();
        DataOutputStream dos = new DataOutputStream(os);
        FileInputStream fis = new FileInputStream(mContent);
        byte[] buf = new byte[1024];
        try {
            int read;
            while ((read = fis.read(buf)) != -1) {
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
