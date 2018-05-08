package com.example.tthtt.db.helper;

import com.example.tthtt.db.AppDbHelper;
import com.example.tthtt.model.ActiveModel;
import com.example.tthtt.model.ActiveModelDao;

import java.util.List;

/**
 * Created by book4 on 2018/3/16.
 */

public class ActiveDbHelper {

    private static ActiveDbHelper mInstance;
    private ActiveModelDao mDao;
    private final String Tag = "activeDb";

    public static ActiveDbHelper getInstance(){
        if(mInstance == null){
            synchronized (ActiveDbHelper.class){
                if(mInstance == null){
                    mInstance = new ActiveDbHelper();
                }
            }
        }
        return mInstance;
    }

    private ActiveDbHelper(){
        mDao = AppDbHelper.getInstance().getDaoSession().getActiveModelDao();
    }

    public void put(ActiveModel data){
        if(data == null ) return;
        mDao.insertOrReplace(data);
    }

    public ActiveModel get(){
        List<ActiveModel> resultList = mDao.loadAll();
        if(resultList == null || resultList.size() <= 0){
            return new ActiveModel();
        }else{
            return resultList.get(0);
        }
    }
}
