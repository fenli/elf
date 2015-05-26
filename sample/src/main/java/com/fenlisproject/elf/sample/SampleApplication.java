package com.fenlisproject.elf.sample;

import com.fenlisproject.elf.core.base.BaseApplication;
import com.fenlisproject.elf.core.data.SecureSessionStorage;

import java.security.NoSuchAlgorithmException;

import javax.crypto.Cipher;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;

public class SampleApplication extends BaseApplication {

    private SecureSessionStorage secureSessionStorage;

    @Override
    public void onCreate() {
        super.onCreate();
        try {
            Cipher cipher = Cipher.getInstance("AES");
            SecretKeySpec secretKeySpec = new SecretKeySpec("ElfSampleKey1234".getBytes(), "AES");
            secureSessionStorage = new SecureSessionStorage(this,
                    getAppEnvironment().getSessionDirectory(), cipher, secretKeySpec);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (NoSuchPaddingException e) {
            e.printStackTrace();
        }
    }

    public SecureSessionStorage getSecureSessionStorage() {
        return secureSessionStorage;
    }
}
