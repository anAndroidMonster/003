package com.example.tthtt.utils;

import com.example.tthtt.db.helper.HourControlDbHelper;
import com.example.tthtt.model.HourControlModel;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;

/**
 * Created by book4 on 2018/4/8.
 */

public class HourControlHelper {
    private static HourControlHelper mInstance;
    private SimpleDateFormat mSdf = new SimpleDateFormat("yyyy-MM-dd");
    private List<HourControlModel> mDataList = new ArrayList<>();
    private final String Tag = "HourControl";

    public static HourControlHelper getInstance(){
        if(mInstance == null){
            synchronized (HourControlHelper.class){
                if(mInstance == null){
                    mInstance = new HourControlHelper();
                }
            }
        }
        return mInstance;
    }

    private void initRate(){
        List<HourControlModel> tempList = HourControlDbHelper.getInstance().getList();
        if(tempList == null || tempList.size() != 24){
            LogHelper.d("isDelay: no data");
            return;
        }
        mDataList.clear();
        mDataList.addAll(tempList);
        String date = mSdf.format(new Date());
        if(tempList.get(0).getDate() != null && tempList.get(0).getDate().equals(date)){
            LogHelper.d("isDelay: today setted");
            return;
        }
        Random random = new Random();
        for(HourControlModel data: mDataList){
            int standRate = data.getRate();
            if(standRate <= 0){
                data.setDate(date);
                data.setTodayRate(0);
                LogHelper.d("isDelay: set hour " + data.getHour() + ", rate " + data.getRate() + ", to " + data.getTodayRate());
            }else {
                int minRate = standRate - 5;
                int maxRate = standRate + 5;
                if (minRate < 0) minRate = 0;
                if (maxRate > 100) maxRate = 100;
                int todayRate = random.nextInt(maxRate - minRate);
                todayRate += minRate;
                data.setDate(date);
                data.setTodayRate(todayRate);
                LogHelper.d("isDelay: set hour " + data.getHour() + ", rate " + data.getRate() + ", to " + data.getTodayRate());
            }
        }
        HourControlDbHelper.getInstance().putList(mDataList);
    }

    public boolean getIsDelay(){
        boolean result = false;
        initRate();
        if(mDataList == null || mDataList.size() != 24){
            LogHelper.d("isDelay: data empty return false");
            return false;
        }
        int hour = new Date().getHours();
        for(HourControlModel data: mDataList){
            if(hour == data.getHour()){
                int realRate = data.getTodayRate();
                Random random = new Random();
                int tempRate = random.nextInt(100);
                if(realRate >= tempRate){
                    result = false;
                }else{
                    result = true;
                }
                LogHelper.d("isDelay: todayRate " + realRate + ", tempRate " + tempRate + ", return " + result);
                break;
            }
        }
        return result;
    }
}
