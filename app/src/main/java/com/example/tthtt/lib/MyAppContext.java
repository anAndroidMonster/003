package com.example.tthtt.lib;

import android.app.Application;
import android.content.Context;
import android.support.multidex.MultiDexApplication;

import com.example.tthtt.lib.db.AppDbHelper;

/**
 * Created by book4 on 2018/3/16.
 */

public class MyAppContext extends MultiDexApplication {
    private static Context mInstance;

    @Override
    public void onCreate() {
        super.onCreate();
        mInstance = this;
        AppDbHelper.getInstance().init(this);
    }

    public static Context getInstance(){
        return mInstance;
    }
}
