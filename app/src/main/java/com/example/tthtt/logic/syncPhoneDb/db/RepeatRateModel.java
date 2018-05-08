package com.example.tthtt.logic.syncPhoneDb.db;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Generated;

/**
 * Created by book4 on 2018/4/8.
 */

@Entity
public class RepeatRateModel {
    @Id
    private Long id;
    private int type;//-1新建0今天1昨天7一周30一月
    private int rate;
    private String date;
    private int todayRate;

    @Generated(hash = 1695065248)
    public RepeatRateModel(Long id, int type, int rate, String date,
            int todayRate) {
        this.id = id;
        this.type = type;
        this.rate = rate;
        this.date = date;
        this.todayRate = todayRate;
    }

    @Generated(hash = 1834003270)
    public RepeatRateModel() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getRate() {
        return rate;
    }

    public void setRate(int rate) {
        this.rate = rate;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public int getTodayRate() {
        return todayRate;
    }

    public void setTodayRate(int todayRate) {
        this.todayRate = todayRate;
    }
}
