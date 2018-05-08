package com.example.tthtt.logic.syncPhoneDb.model;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Unique;
import org.greenrobot.greendao.annotation.Generated;

/**
 * Created by book4 on 2018/4/8.
 */

@Entity
public class HourControlModel {

    @Id
    private Long id;
    private int hour;
    private int rate=-1;
    private String date;
    private int todayRate;

    @Generated(hash = 1249986751)
    public HourControlModel(Long id, int hour, int rate, String date,
            int todayRate) {
        this.id = id;
        this.hour = hour;
        this.rate = rate;
        this.date = date;
        this.todayRate = todayRate;
    }

    @Generated(hash = 1494859308)
    public HourControlModel() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public int getHour() {
        return hour;
    }

    public void setHour(int hour) {
        this.hour = hour;
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
