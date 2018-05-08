package com.example.tthtt.model;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Unique;
import org.greenrobot.greendao.annotation.Generated;

import java.io.Serializable;

/**
 * Created by book4 on 2018/3/28.
 */

@Entity
public class LocationModel implements Serializable {

    private static final long serialVersionUID = -439514228105239820L;
    @Id
    private String city;
    private double lat;//纬度
    private double lng;//经度
    private boolean isSelected;


    @Generated(hash = 536868411)
    public LocationModel() {
    }

    @Generated(hash = 232564503)
    public LocationModel(String city, double lat, double lng, boolean isSelected) {
        this.city = city;
        this.lat = lat;
        this.lng = lng;
        this.isSelected = isSelected;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public double getLng() {
        return lng;
    }

    public void setLng(double lng) {
        this.lng = lng;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }

    public boolean getIsSelected() {
        return this.isSelected;
    }

    public void setIsSelected(boolean isSelected) {
        this.isSelected = isSelected;
    }
}
