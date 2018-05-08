package com.example.tthtt.utils;

import android.util.Log;

/**
 * Created by book4 on 2018/4/12.
 */

public class LogHelper {
    private static final boolean mIsEnable = true;
    private static final String Tag = "happyness";

    public static void d(String content){
        if(mIsEnable){
            Log.d(Tag, content);
        }
    }
}
