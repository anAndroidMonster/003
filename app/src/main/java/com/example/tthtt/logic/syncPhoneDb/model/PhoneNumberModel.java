package com.example.tthtt.logic.syncPhoneDb.model;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Unique;
import org.greenrobot.greendao.annotation.Generated;

/**
 * Created by book4 on 2018/3/28.
 */

@Entity
public class PhoneNumberModel {

    @Id(autoincrement = true)
    private Long id;
    private String city;
    private int company;//0移动1电信2联通
    @Unique
    private String phoneNumber;

    @Generated(hash = 1793944448)
    public PhoneNumberModel(Long id, String city, int company, String phoneNumber) {
        this.id = id;
        this.city = city;
        this.company = company;
        this.phoneNumber = phoneNumber;
    }

    @Generated(hash = 265918217)
    public PhoneNumberModel() {
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public int getCompany() {
        return company;
    }

    public void setCompany(int company) {
        this.company = company;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
