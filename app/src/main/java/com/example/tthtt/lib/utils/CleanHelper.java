package com.example.tthtt.lib.utils;

import android.os.Environment;

import java.io.File;

/**
 * Created by book4 on 2018/3/25.
 */

public class CleanHelper {
    private static CleanHelper mInstance;
    private final String[] WHITE_LIST = {"config.ini", "device.ini", "reserved", "package_list", "MobileAnJian"};

    public static CleanHelper getInstance(){
        if(mInstance == null){
            synchronized (CleanHelper.class){
                if(mInstance == null){
                    mInstance = new CleanHelper();
                }
            }
        }
        return mInstance;
    }

    public boolean clearSdcard()
    {
        if ("mounted".equals(Environment.getExternalStorageState()))
        {
            File localFile = Environment.getExternalStorageDirectory();
            String[] arrayOfString = localFile.list();
            for(String str: arrayOfString) {
                if (hasEquals(str)) {
                }else{
                    deleteDir(new File(localFile, str));
                }
            }
        }
        return true;
    }

    private boolean hasEquals(String paramString)
    {
        for(String white: WHITE_LIST){
            if(paramString.equals(white)){
                return true;
            }
        }
        return false;
    }

    public void deleteDir(File paramFile)
    {
        if (paramFile.isDirectory())
        {
            String[] arrayOfString = paramFile.list();
            if(arrayOfString != null){
                for(String one: arrayOfString){
                    deleteDir(new File(paramFile, one));
                }
            }
        }
        paramFile.delete();
    }
}
