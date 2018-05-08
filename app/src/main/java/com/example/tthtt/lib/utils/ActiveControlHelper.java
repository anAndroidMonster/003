package com.example.tthtt.lib.utils;

import com.example.tthtt.logic.syncPhoneDb.db.ActiveDbHelper;
import com.example.tthtt.logic.syncPhoneDb.db.RepeatControlDbHelper;
import com.example.tthtt.logic.syncPhoneDb.db.RepeatRateModel;
import com.example.tthtt.logic.syncPhoneDb.model.ActiveModel;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;

/**
 * Created by book4 on 2018/4/8.
 */

public class ActiveControlHelper {
    private static ActiveControlHelper mInstance;
    private SimpleDateFormat mSdf = new SimpleDateFormat("yyyy-MM-dd");
    private ActiveModel mData;
    private final String Tag = "activeControl";

    public static ActiveControlHelper getInstance(){
        if(mInstance == null){
            synchronized (ActiveControlHelper.class){
                if(mInstance == null){
                    mInstance = new ActiveControlHelper();
                }
            }
        }
        return mInstance;
    }

    private void initRate(){
        mData = ActiveDbHelper.getInstance().get();
        if(mData == null){
            mData = new ActiveModel();
        }
        String date = mSdf.format(new Date());
        if(mData.getLastDate().equals(date)){
            return;
        }
        Random random = new Random();

        int standRate = mData.getOpenRate();
        int minRate = standRate - 10;
        int maxRate = standRate + 10;
        if (minRate < 0) minRate = 0;
        if (maxRate > 100) maxRate = 100;
        int todayRate = random.nextInt(maxRate - minRate);
        todayRate += minRate;
        mData.setLastDate(date);
        mData.setTempOpenRate(todayRate);

        standRate = mData.getInstallRate();
        minRate = standRate - 10;
        maxRate = standRate + 10;
        if (minRate < 0) minRate = 0;
        if (maxRate > 100) maxRate = 100;
        todayRate = random.nextInt(maxRate - minRate);
        todayRate += minRate;
        mData.setTempInstallRate(todayRate);
        LogHelper.d("active rate: standRate " + mData.getOpenRate() + "," + mData.getInstallRate() + ", todayRate" + mData.getTempOpenRate() + "," + mData.getTempInstallRate());
        ActiveDbHelper.getInstance().put(mData);
    }

    public int getOpenRate(){
        initRate();
        return ActiveDbHelper.getInstance().get().getTempOpenRate();
    }

    public int getInstallRate(){
        initRate();
        return ActiveDbHelper.getInstance().get().getTempInstallRate();
    }
}
