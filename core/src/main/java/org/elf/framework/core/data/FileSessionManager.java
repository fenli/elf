package org.elf.framework.core.data;

import android.content.Context;

import org.elf.framework.core.base.BaseApplication;
import org.elf.framework.core.util.FileUtils;
import org.elf.framework.core.util.SecurityUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

public class FileSessionManager {

    private String mSessionDirectory;

    public FileSessionManager(Context context, String path) {
        mSessionDirectory = context.getFilesDir().getAbsolutePath() + File.separator + path;
        FileUtils.createDirsIfNotExist(mSessionDirectory);
    }

    public synchronized <T extends Serializable> T get(String key, Class<T> tClass) {
        try {
            BaseApplication appContext = BaseApplication.getInstance();
            if (appContext != null) {
                File sessionFile = new File(
                        mSessionDirectory + File.separator + SecurityUtils.md5(key));
                if (!sessionFile.exists()) return null;
                FileInputStream fis = new FileInputStream(sessionFile);
                ObjectInputStream ois = new ObjectInputStream(fis);
                T object = (T) ois.readObject();
                ois.close();
                fis.close();
                return object;
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    public synchronized <T extends Serializable> boolean set(String key, T object) {
        try {
            BaseApplication appContext = BaseApplication.getInstance();
            if (appContext != null) {
                FileOutputStream fis = new FileOutputStream(
                        mSessionDirectory + File.separator + SecurityUtils.md5(key));
                ObjectOutputStream oos = new ObjectOutputStream(fis);
                oos.writeObject(object);
                oos.close();
                fis.close();
                return true;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    public synchronized boolean remove(String key) {
        BaseApplication appContext = BaseApplication.getInstance();
        if (appContext != null) {
            File sessionFile = new File(
                    mSessionDirectory + File.separator + SecurityUtils.md5(key));
            return sessionFile.delete();
        }
        return false;
    }

}
