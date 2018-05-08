package com.example.tthtt.db.helper;

import com.example.tthtt.db.AppDbHelper;
import com.example.tthtt.model.DeviceModelDao;
import com.example.tthtt.utils.LogHelper;
import com.example.tthtt.model.DeviceModel;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * Created by book4 on 2018/3/16.
 */

public class DeviceDbHelper {

    private static DeviceDbHelper mInstance;
    private DeviceModelDao mDao;
    private static final long DateLong = 1000*60*60*24;
    private final String Tag = "deviceDb";

    public static DeviceDbHelper getInstance(){
        if(mInstance == null){
            synchronized (DeviceDbHelper.class){
                if(mInstance == null){
                    mInstance = new DeviceDbHelper();
                }
            }
        }
        return mInstance;
    }

    private DeviceDbHelper(){
        mDao = AppDbHelper.getInstance().getDaoSession().getDeviceModelDao();
    }

    public void put(DeviceModel data){
        if(data == null || data.getDevice_info() == null || data.getDevice_info().length() <= 0) return;
        long record = mDao.queryBuilder().where(DeviceModelDao.Properties.P_key.eq(data.getP_key())).count();
        if(record == 0) {
            mDao.insert(data);
        }
    }

    public long getCount(){
        return mDao.count();
    }

    public DeviceModel getOne(int type, String city){
        DeviceModel result = null;
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
        long todayLong = 0;
        try {
            String todayStr = sdf.format(new Date());
            todayLong = sdf.parse(todayStr).getTime();
        }catch (ParseException ex){
            ex.printStackTrace();
        }
        List<DeviceModel> resultList = null;
        switch (type){
            case -1://新的

                break;
            case 0://今天
                resultList = mDao.queryBuilder().where(DeviceModelDao.Properties.City.eq(city), DeviceModelDao.Properties.Last_date.eq(todayLong)).limit(1).list();
                break;
            case 1://昨天
                resultList = mDao.queryBuilder().where(DeviceModelDao.Properties.City.eq(city), DeviceModelDao.Properties.Last_date.between(todayLong-DateLong, todayLong-1)).limit(1).list();
                break;
            case 7://一周内
                resultList = mDao.queryBuilder().where(DeviceModelDao.Properties.City.eq(city),
                        DeviceModelDao.Properties.Last_date.between(todayLong-DateLong*7, todayLong-DateLong-1)).limit(1).list();
                break;
            case 30://一月内
                resultList = mDao.queryBuilder().where(DeviceModelDao.Properties.City.eq(city),
                        DeviceModelDao.Properties.Last_date.between(todayLong-DateLong*30, todayLong-DateLong*7-1)).limit(1).list();
                break;
        }
        if(resultList != null && resultList.size() > 0){
            result = resultList.get(0);
        }else{
            LogHelper.d("getDevice:取新的");
            result = getNew(todayLong-DateLong*30-1);
        }
        if(result != null) {
            LogHelper.d("getDevice:入参" + type + "," + city + ",结果" + (result.getLast_date() > 10 ? sdf.format(new Date(result.getLast_date())) : "new") + "," + result.getCity());
            result.setLast_date(todayLong);
            result.setCity(city);
            mDao.update(result);
        }
        return result;
    }




    private DeviceModel getNew(long time){

        List<DeviceModel> resultList = null;
        resultList = mDao.queryBuilder().where(DeviceModelDao.Properties.Last_date.eq(0)).limit(1).list();
        if(resultList != null && resultList.size() > 0){
            return  resultList.get(0);
        }
//        resultList = mDao.queryBuilder().where(DeviceModelDao.Properties.Id.between(50000, 100000), DeviceModelDao.Properties.Last_date.eq(0)).limit(1).list();
//        if(resultList != null && resultList.size() > 0){
//            return  resultList.get(0);
//        }
//        resultList = mDao.queryBuilder().where(DeviceModelDao.Properties.Id.between(100000, 150000), DeviceModelDao.Properties.Last_date.eq(0)).limit(1).list();
//        if(resultList != null && resultList.size() > 0){
//            return  resultList.get(0);
//        }
//        resultList = mDao.queryBuilder().where(DeviceModelDao.Properties.Id.between(150000, 200000), DeviceModelDao.Properties.Last_date.eq(0)).limit(1).list();
//        if(resultList != null && resultList.size() > 0){
//            return  resultList.get(0);
//        }
        return null;
    }
}
