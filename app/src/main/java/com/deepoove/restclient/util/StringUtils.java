package com.deepoove.restclient.util;

import java.util.Random;

/**
 * @author Sayi
 */

public class StringUtils {

    public static boolean isEmpty(String str) {
        return null == str || str.length() == 0;
    }

    public static boolean isBlank(String str) {
        return isEmpty(str) || str.trim().length() == 0;
    }

    public static boolean isNotEmpty(String str) {
        return null != str && str.length() > 0;
    }

    public static boolean isNotBlank(String str) {
        return isNotEmpty(str) && str.trim().length() > 0;
    }

    public static String getRandomString(int length) {
        StringBuffer buffer = new StringBuffer("0123456789abcdefghijklmnopqrstuvwxyz_");
        StringBuffer sb = new StringBuffer();
        Random random = new Random();
        int range = buffer.length();
        for (int i = 0; i < length; i++) {
            sb.append(buffer.charAt(random.nextInt(range)));
        }
        return sb.toString();
    }
}
