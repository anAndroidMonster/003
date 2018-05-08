package com.example.tthtt.logic.syncPhoneDb.db;

import com.example.tthtt.lib.db.AppDbHelper;
import com.example.tthtt.logic.syncPhoneDb.model.LocationModel;
import com.example.tthtt.logic.syncPhoneDb.model.LocationModelDao;
import com.example.tthtt.logic.syncPhoneDb.model.PhoneNumberModel;
import com.example.tthtt.logic.syncPhoneDb.model.PhoneNumberModelDao;

import java.util.List;

/**
 * Created by book4 on 2018/3/16.
 */

public class LocationDbHelper {

    private static LocationDbHelper mInstance;
    private LocationModelDao mDao;

    public static LocationDbHelper getInstance(){
        if(mInstance == null){
            synchronized (LocationDbHelper.class){
                if(mInstance == null){
                    mInstance = new LocationDbHelper();
                }
            }
        }
        return mInstance;
    }

    private LocationDbHelper(){
        mDao = AppDbHelper.getInstance().getDaoSession().getLocationModelDao();
    }

    public void put(LocationModel data){
        if(data == null) return;
        mDao.insertOrReplace(data);
    }

    public List<LocationModel> getList(){
        return mDao.loadAll();
    }

    public LocationModel getByCity(String city){
        return mDao.queryBuilder().where(LocationModelDao.Properties.City.eq(city)).unique();
    }

}
