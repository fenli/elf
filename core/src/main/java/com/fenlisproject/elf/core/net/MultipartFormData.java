package com.fenlisproject.elf.core.net;

import com.fenlisproject.elf.core.net.entity.FileFormEntity;
import com.fenlisproject.elf.core.net.entity.MultipartFormEntity;
import com.fenlisproject.elf.core.net.entity.StringFormEntity;
import com.fenlisproject.elf.core.util.FileUtils;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

public class MultipartFormData implements RequestBody {

    private final String CRLF = "\r\n";
    private final String TWO_HYPHENS = "--";
    private final String BOUNDARY = "ElfHttpRequestBoundary";
    private String mContentType;
    private List<MultipartFormEntity> mContent;

    public MultipartFormData() {
        mContent = new ArrayList<>();
        mContentType = "multipart/form-data; boundary=" + BOUNDARY;
    }

    public MultipartFormData add(String key, String value) {
        mContent.add(new StringFormEntity(key, value));
        return this;
    }

    public MultipartFormData add(String key, File value) {
        mContent.add(new FileFormEntity(key, value));
        return this;
    }

    @Override
    public void write(HttpRequest request) throws IOException {
        request.getHttpURLConnection().setRequestProperty("Content-Type", mContentType);
        request.getHttpURLConnection().setDoInput(true);
        request.getHttpURLConnection().setDoOutput(true);
        OutputStream os = request.getHttpURLConnection().getOutputStream();
        DataOutputStream dos = new DataOutputStream(os);
        for (MultipartFormEntity data : mContent) {
            if (data instanceof StringFormEntity) {
                StringFormEntity stringData = ((StringFormEntity) data);
                dos.writeBytes(TWO_HYPHENS + BOUNDARY + CRLF);
                dos.writeBytes(String.format(
                        "Content-Disposition: form-data; name=\"%s\"", stringData.getKey()
                ));
                dos.writeBytes(CRLF + CRLF);
                dos.writeBytes(stringData.getValue());
                dos.writeBytes(CRLF);
            } else if (data instanceof FileFormEntity) {
                FileFormEntity fileData = ((FileFormEntity) data);
                if (fileData.getValue().exists()) {
                    String mime = FileUtils.getMimeType(fileData.getValue());
                    dos.writeBytes(TWO_HYPHENS + BOUNDARY + CRLF);
                    dos.writeBytes(String.format(
                            "Content-Disposition: form-data; name=\"%s\"; filename=\"%s\"",
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
}
