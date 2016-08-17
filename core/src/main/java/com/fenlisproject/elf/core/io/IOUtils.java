package com.fenlisproject.elf.core.io;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class IOUtils {
    
    public static String decodeStreamReader(InputStreamReader reader) {
        try {
            BufferedReader r = new BufferedReader(reader);
            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = r.readLine()) != null) {
                sb.append(line);
            }
            return sb.toString();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
