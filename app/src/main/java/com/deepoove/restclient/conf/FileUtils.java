package com.deepoove.restclient.conf;

import java.io.InputStream;

/**
 * @author Sayi
 */

public class FileUtils {

    public static String read(InputStream is) {
        String res = "";
        try {
            int length = is.available();
            byte[] buffer = new byte[length];
            is.read(buffer);
            res = new String(buffer, "UTF-8");

        } catch (Exception e) {
            e.printStackTrace();
        }
        return res;
    }
}
