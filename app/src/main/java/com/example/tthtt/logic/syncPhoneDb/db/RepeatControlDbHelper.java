package com.example.tthtt.logic.syncPhoneDb.db;

import com.example.tthtt.lib.db.AppDbHelper;
import com.example.tthtt.logic.syncPhoneDb.model.HourControlModel;
import com.example.tthtt.logic.syncPhoneDb.model.HourControlModelDao;

import java.util.List;

/**
 * Created by book4 on 2018/3/16.
 */

public class RepeatControlDbHelper {

    private static RepeatControlDbHelper mInstance;
    private RepeatRateModelDao mDao;

    public static RepeatControlDbHelper getInstance(){
        if(mInstance == null){
            synchronized (RepeatControlDbHelper.class){
                if(mInstance == null){
                    mInstance = new RepeatControlDbHelper();
                }
            }
        }
        return mInstance;
    }

    private RepeatControlDbHelper(){
        mDao = AppDbHelper.getInstance().getDaoSession().getRepeatRateModelDao();
    }

    public void putList(List<RepeatRateModel> dataList){
        if(dataList == null || dataList.size() < 5){
            return;
        }
        mDao.deleteAll();
        mDao.insertInTx(dataList);
    }

    public List<RepeatRateModel> getList(){
        return mDao.loadAll();
    }

}
