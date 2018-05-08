package com.example.tthtt.model;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Transient;

/**
 * Created by book4 on 2018/3/15.
 */

@Entity
public class DeviceModel {
    @Id(autoincrement = true)
    private Long id;
    private String p_key;
    private String city;
    private long last_date;
    @Transient
    private String device_info;

    @Generated(hash = 210163102)
    public DeviceModel() {
    }



    @Generated(hash = 1138905691)
    public DeviceModel(Long id, String p_key, String city, long last_date) {
        this.id = id;
        this.p_key = p_key;
        this.city = city;
        this.last_date = last_date;
    }



    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getP_key() {
        return p_key;
    }

    public void setP_key(String p_key) {
        this.p_key = p_key;
    }

    public long getLast_date() {
        return last_date;
    }

    public void setLast_date(long last_date) {
        this.last_date = last_date;
    }

    public String getDevice_info() {
        return device_info;
    }

    public void setDevice_info(String device_info) {
        this.device_info = device_info;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }
}
