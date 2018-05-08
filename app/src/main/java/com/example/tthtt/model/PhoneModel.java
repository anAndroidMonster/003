package com.example.tthtt.model;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Generated;

/**
 * Created by book4 on 2018/3/31.
 */
@Entity
public class PhoneModel {

    @Id
    private String p_key;
    private String device_info;
    private boolean isEdit;

    @Generated(hash = 908177172)
    public PhoneModel(String p_key, String device_info, boolean isEdit) {
        this.p_key = p_key;
        this.device_info = device_info;
        this.isEdit = isEdit;
    }

    @Generated(hash = 2003199864)
    public PhoneModel() {
    }

    public String getP_key() {
        return p_key;
    }

    public void setP_key(String p_key) {
        this.p_key = p_key;
    }

    public String getDevice_info() {
        return device_info;
    }

    public void setDevice_info(String device_info) {
        this.device_info = device_info;
    }

    public boolean isEdit() {
        return isEdit;
    }

    public void setEdit(boolean edit) {
        isEdit = edit;
    }

    public boolean getIsEdit() {
        return this.isEdit;
    }

    public void setIsEdit(boolean isEdit) {
        this.isEdit = isEdit;
    }
}
