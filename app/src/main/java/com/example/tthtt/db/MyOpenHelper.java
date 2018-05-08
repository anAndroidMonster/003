package com.example.tthtt.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.example.tthtt.logic.syncPhoneDb.db.DaoMaster;
import com.example.tthtt.logic.syncPhoneDb.db.RepeatRateModelDao;
import com.example.tthtt.logic.syncPhoneDb.model.ActiveModelDao;
import com.example.tthtt.logic.syncPhoneDb.model.DeviceModelDao;
import com.example.tthtt.logic.syncPhoneDb.model.HourControlModelDao;
import com.example.tthtt.logic.syncPhoneDb.model.LocationModelDao;
import com.example.tthtt.logic.syncPhoneDb.model.PhoneModelDao;
import com.example.tthtt.logic.syncPhoneDb.model.PhoneNumberModelDao;
import com.example.tthtt.logic.syncPhoneDb.model.VpnModelDao;

import org.greenrobot.greendao.database.Database;

/**
 * Created by book4 on 2018/4/9.
 */

public class MyOpenHelper extends DaoMaster.OpenHelper {
    public MyOpenHelper(Context context, String name, SQLiteDatabase.CursorFactory factory) {
        super(context, name, factory);
    }

    @Override
    public void onUpgrade(Database db, int oldVersion, int newVersion) {

        Class[] array = new Class[]{DeviceModelDao.class, HourControlModelDao.class, LocationModelDao.class, PhoneModelDao.class, PhoneNumberModelDao.class,
                RepeatRateModelDao.class, RepeatRateModelDao.class, VpnModelDao.class, ActiveModelDao.class};
        //把需要管理的数据库表DAO作为最后一个参数传入到方法中
        MigrationHelper.migrate(db, new MigrationHelper.ReCreateAllTableListener() {

            @Override
            public void onCreateAllTables(Database db, boolean ifNotExists) {
                DaoMaster.createAllTables(db, ifNotExists);
            }

            @Override
            public void onDropAllTables(Database db, boolean ifExists) {
                DaoMaster.dropAllTables(db, ifExists);
            }
        },  array);
    }
}