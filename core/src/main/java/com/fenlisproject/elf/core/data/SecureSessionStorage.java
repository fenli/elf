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

package com.fenlisproject.elf.core.data;

import android.content.Context;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectInputStream;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import java.security.InvalidKeyException;
import java.security.Key;

import javax.crypto.Cipher;
import javax.crypto.CipherInputStream;
import javax.crypto.CipherOutputStream;

public class SecureSessionStorage extends SessionStorage {

    private Cipher cipher;
    private Key secretKey;

    public SecureSessionStorage(Context context, String path, Cipher cipher, Key secretKey) {
        super(context, path);
        this.cipher = cipher;
        this.secretKey = secretKey;
    }

    @Override
    public ObjectInput getObjectInput(String key) throws IOException {
        try {
            cipher.init(Cipher.DECRYPT_MODE, secretKey);
            CipherInputStream cis = new CipherInputStream(
                    new FileInputStream(getSessionFile(key)), cipher);
            return new ObjectInputStream(cis);
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public ObjectOutput getObjectOutput(String key) throws IOException {
        try {
            cipher.init(Cipher.ENCRYPT_MODE, secretKey);
            CipherOutputStream cos = new CipherOutputStream(
                    new FileOutputStream(getSessionFile(key)), cipher);
            return new ObjectOutputStream(cos);
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        }
        return null;
    }
}
