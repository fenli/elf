package org.elf.framework.core.util;


import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class FileUtils {

    private FileUtils() {
    }

    public static boolean writeStreamToFile(InputStream in, String outFileName) {
        OutputStream out = null;
        try {
            out = new FileOutputStream(outFileName);
            byte[] buf = new byte[1024];
            int len;
            while ((len = in.read(buf)) > 0) {
                out.write(buf, 0, len);
            }
            in.close();
            out.close();
            return true;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                in.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
            try {
                out.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return false;
    }

    public static boolean copy(String source, String destination) {
        InputStream in = null;
        OutputStream out = null;
        try {
            in = new FileInputStream(source);
            out = new FileOutputStream(destination);
            byte[] buf = new byte[1024];
            int len;
            while ((len = in.read(buf)) > 0) {
                out.write(buf, 0, len);
            }
            in.close();
            out.close();
            return true;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                in.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
            try {
                out.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return false;
    }

    public static boolean delete(String fileName) {
        File file = new File(fileName);
        return file.delete();
    }

    public static boolean isExist(String fileName) {
        return new File(fileName).exists();
    }

    public static void createDirsIfNotExist(String path) {
        File file = new File(path);
        if (!file.exists()) {
            file.mkdirs();
        }
    }

}
