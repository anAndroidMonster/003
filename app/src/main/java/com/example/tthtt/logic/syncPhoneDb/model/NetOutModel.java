package com.example.tthtt.logic.syncPhoneDb.model;

/**
 * Created by book4 on 2018/3/15.
 */

public class NetOutModel {
    private int code;
    private String msg;
    private DataModel data;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public DataModel getData() {
        return data;
    }

    public void setData(DataModel data) {
        this.data = data;
    }
}
