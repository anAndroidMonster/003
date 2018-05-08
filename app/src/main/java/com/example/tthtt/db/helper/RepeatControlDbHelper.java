package com.example.tthtt.db.helper;

import com.example.tthtt.db.AppDbHelper;
import com.example.tthtt.model.RepeatRateModel;
import com.example.tthtt.logic.syncPhoneDb.db.RepeatRateModelDao;

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
