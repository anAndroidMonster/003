package com.example.tthtt.lib.utils;

import android.util.Log;

import com.example.tthtt.logic.syncPhoneDb.db.HourControlDbHelper;
import com.example.tthtt.logic.syncPhoneDb.db.RepeatControlDbHelper;
import com.example.tthtt.logic.syncPhoneDb.db.RepeatRateModel;
import com.example.tthtt.logic.syncPhoneDb.model.HourControlModel;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;

/**
 * Created by book4 on 2018/4/8.
 */

public class RepeatControlHelper {
    private static RepeatControlHelper mInstance;
    private SimpleDateFormat mSdf = new SimpleDateFormat("yyyy-MM-dd");
    private List<RepeatRateModel> mDataList = new ArrayList<>();
    private final String Tag = "RepeatControl";

    public static RepeatControlHelper getInstance(){
        if(mInstance == null){
            synchronized (RepeatControlHelper.class){
                if(mInstance == null){
                    mInstance = new RepeatControlHelper();
                }
            }
        }
        return mInstance;
    }

    private void initRate(){
        List<RepeatRateModel> tempList = RepeatControlDbHelper.getInstance().getList();
        if(tempList == null || tempList.size() < 5){
            LogHelper.d("isRepeat: no data");
            return;
        }
        mDataList.clear();
        mDataList.addAll(tempList);
        String date = mSdf.format(new Date());
        if(tempList.get(0).getDate() != null && tempList.get(0).getDate().equals(date)){
            LogHelper.d("isRepeat: today setted");
            return;
        }

        Random random = new Random();
        int total = 100;
        for(RepeatRateModel data: mDataList){
            if(data.getType() == -1) continue;
            int standRate = data.getRate();
            int minRate = standRate - 5;
            int maxRate = standRate + 5;
            if (minRate < 0) minRate = 0;
            if (maxRate > 100) maxRate = 100;
            int todayRate = random.nextInt(maxRate - minRate);
            todayRate += minRate;
            data.setDate(date);
            data.setTodayRate(todayRate);
            total -= todayRate;
            LogHelper.d("isRepeat: set today rate: type" + data.getType() + ", standRate " + data.getRate() + ", todayRate" + data.getTodayRate());
        }
        for(RepeatRateModel data: mDataList){
            if(data.getType() == -1){
                data.setDate(date);
                data.setTodayRate(total);
                LogHelper.d("isRepeat: set today rate: type" + data.getType() + ", standRate " + data.getRate() + ", todayRate" + data.getTodayRate());
            }
        }
        RepeatControlDbHelper.getInstance().putList(mDataList);
    }

    public int getRepeatType(){
        int result = -1;
        initRate();
        if(mDataList == null || mDataList.size() < 5){
            LogHelper.d("isRepeat: list empty return -1");
            return result;
        }
        int newRate=0, todayRate=0, yesRate=0, weekRate=0, monthRate = 0;
        for(RepeatRateModel data: mDataList){
            switch (data.getType()){
                case -1: newRate = data.getTodayRate(); break;
                case 0: todayRate = data.getTodayRate(); break;
                case 1: yesRate = data.getTodayRate(); break;
                case 7: weekRate = data.getTodayRate(); break;
                case 30: monthRate = data.getTodayRate(); break;
            }
        }
        Random random = new Random();
        int tempRate = random.nextInt(100);
        if(tempRate <= newRate){
            result = -1;
        }else if(tempRate <= newRate + todayRate){
            result = 0;
        }
        else if(tempRate <= newRate + todayRate + yesRate){
            result = 1;
        }
        else if(tempRate <= newRate + todayRate + yesRate + weekRate){
            result = 7;
        }else{
            result = 30;
        }
        LogHelper.d("isRepeat:从-1到30：" + newRate + "," + todayRate + "," + yesRate + "," + weekRate + "," + monthRate);
        LogHelper.d("isRepeat:实际概率" + tempRate + ",结果" + result);
        return result;
    }
}
