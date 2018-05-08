package com.example.tthtt.db.helper;

import com.example.tthtt.db.AppDbHelper;
import com.example.tthtt.model.VpnModel;
import com.example.tthtt.model.VpnModelDao;

import java.util.List;

/**
 * Created by book4 on 2018/3/16.
 */

public class VpnDbHelper {

    private static VpnDbHelper mInstance;
    private VpnModelDao mDao;
    private final String Tag = "vpnDb";

    public static VpnDbHelper getInstance(){
        if(mInstance == null){
            synchronized (VpnDbHelper.class){
                if(mInstance == null){
                    mInstance = new VpnDbHelper();
                }
            }
        }
        return mInstance;
    }

    private VpnDbHelper(){
        mDao = AppDbHelper.getInstance().getDaoSession().getVpnModelDao();
    }

    public void put(VpnModel data){
        if(data == null ) return;
        mDao.insertOrReplace(data);
    }

    public void delete(Long key){
        if(key == null || key <= 0) return;
        mDao.deleteByKey(key);
    }

    public List<VpnModel> getList(){
        return mDao.loadAll();
    }

    public List<VpnModel> getListInUse(){
        return mDao.queryBuilder().where(VpnModelDao.Properties.IsGood.eq(true)).list();
    }
}
