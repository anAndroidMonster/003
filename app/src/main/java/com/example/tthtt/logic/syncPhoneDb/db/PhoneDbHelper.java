package com.example.tthtt.logic.syncPhoneDb.db;

import android.util.Log;

import com.example.tthtt.lib.db.AppDbHelper;
import com.example.tthtt.lib.utils.StringUtil;
import com.example.tthtt.logic.syncPhoneDb.model.DeviceModel;
import com.example.tthtt.logic.syncPhoneDb.model.DeviceModelDao;
import com.example.tthtt.logic.syncPhoneDb.model.PhoneModel;
import com.example.tthtt.logic.syncPhoneDb.model.PhoneModelDao;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;

/**
 * Created by book4 on 2018/3/16.
 */

public class PhoneDbHelper {

    private static PhoneDbHelper mInstance;
    private PhoneModelDao mDao;
    private List<PhoneModel> mDataList = new ArrayList<>();

    public static PhoneDbHelper getInstance(){
        if(mInstance == null){
            synchronized (PhoneDbHelper.class){
                if(mInstance == null){
                    mInstance = new PhoneDbHelper();
                }
            }
        }
        return mInstance;
    }

    private PhoneDbHelper(){
        mDao = AppDbHelper.getInstance().getDaoSession().getPhoneModelDao();
    }

    public void put(PhoneModel data){
        if(data == null || data.getDevice_info() == null || data.getDevice_info().length() <= 0) return;
        mDao.insertOrReplace(data);
    }

    public PhoneModel getByKey(String pKey){
        if(StringUtil.isEmpty(pKey)) return null;
        return mDao.load(pKey);
    }

    public void putForService(PhoneModel data){
        if(data == null) return;
        if(mDataList.size() >= 100){
            synchronized (PhoneDbHelper.class){
                if(mDataList.size() >= 100){
                    //需要至少80毫秒
                    Log.e("666", "开始批量插入");
                    mDao.insertOrReplaceInTx(mDataList);
                    mDataList.clear();
                    Log.e("666", "结束批量插入");
                }
            }
        }
        mDataList.add(data);
    }

    public long getCount(){
        return mDao.count();
    }
}
