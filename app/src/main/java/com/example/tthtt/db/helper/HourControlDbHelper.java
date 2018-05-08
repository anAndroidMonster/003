package com.example.tthtt.db.helper;

import com.example.tthtt.db.AppDbHelper;
import com.example.tthtt.model.HourControlModel;
import com.example.tthtt.logic.syncPhoneDb.model.HourControlModelDao;

import java.util.List;

/**
 * Created by book4 on 2018/3/16.
 */

public class HourControlDbHelper {

    private static HourControlDbHelper mInstance;
    private HourControlModelDao mDao;

    public static HourControlDbHelper getInstance(){
        if(mInstance == null){
            synchronized (HourControlDbHelper.class){
                if(mInstance == null){
                    mInstance = new HourControlDbHelper();
                }
            }
        }
        return mInstance;
    }

    private HourControlDbHelper(){
        mDao = AppDbHelper.getInstance().getDaoSession().getHourControlModelDao();
    }

    public void putList(List<HourControlModel> dataList){
        if(dataList == null || dataList.size() != 24){
            return;
        }
        mDao.deleteAll();
        mDao.insertInTx(dataList);
    }

    public List<HourControlModel> getList(){
        return mDao.loadAll();
    }

}
