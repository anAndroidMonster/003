package com.example.tthtt.lib.utils;

import java.io.File;

/**
 * Created by book4 on 2018/4/6.
 */

public class ClearDataHelper {
    private static ClearDataHelper mInstance;
    private final String[] mAppArray = {""};

    public static ClearDataHelper getInstance(){
        if(mInstance == null){
            synchronized (ClearDataHelper.class){
                mInstance = new ClearDataHelper();
            }
        }
        return mInstance;
    }

    public void clearData(){
        for(String app: mAppArray) {
            deleteDir(new File("/data/data/" + app + "/"));
        }
    }

    private void deleteDir(File paramFile)
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
