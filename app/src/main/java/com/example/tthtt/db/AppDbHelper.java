package com.example.tthtt.db;

import android.content.Context;

import com.example.tthtt.model.DaoMaster;
import com.example.tthtt.model.DaoSession;

import org.greenrobot.greendao.database.Database;

/**
 * Created by book4 on 2018/3/16.
 */

public class AppDbHelper {
    private static AppDbHelper mInstance;
    private DaoSession mDaoSession;

    public static AppDbHelper getInstance(){
        if(mInstance == null){
            synchronized (AppDbHelper.class){
                if(mInstance == null){
                    mInstance = new AppDbHelper();
                }
            }
        }
        return mInstance;
    }

    public void init(Context appContext){

        MyOpenHelper mSQLiteOpenHelper = new MyOpenHelper(appContext, "tthtt-db", null);//建库
        Database db = mSQLiteOpenHelper.getWritableDb();
        mDaoSession = new DaoMaster(db).newSession();
    }

    public DaoSession getDaoSession(){
        return mDaoSession;
    }
}
