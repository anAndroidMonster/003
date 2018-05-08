package com.example.tthtt.utils;

public class StringUtil {

    private StringUtil() {
        throw new AssertionError();
    }


    public static boolean isBlank(String str) {
        return (str == null || str.trim().length() == 0);
    }

    public static boolean isEmpty(CharSequence str) {
        return (str == null || str.length() == 0);
    }


}
