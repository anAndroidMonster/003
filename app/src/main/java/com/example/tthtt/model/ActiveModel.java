package com.example.tthtt.model;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Generated;

/**
 * Created by book4 on 2018/4/28.
 */

@Entity
public class ActiveModel {

    @Id(autoincrement = true)
    private Long id;
    private int installRate = 30;
    private int openRate = 80;
    private String relativePath = "GDTDOWNLOAD/apk";
    private String lastDate;
    private int tempInstallRate;
    private int tempOpenRate;

    @Generated(hash = 954013421)
    public ActiveModel(Long id, int installRate, int openRate, String relativePath,
            String lastDate, int tempInstallRate, int tempOpenRate) {
        this.id = id;
        this.installRate = installRate;
        this.openRate = openRate;
        this.relativePath = relativePath;
        this.lastDate = lastDate;
        this.tempInstallRate = tempInstallRate;
        this.tempOpenRate = tempOpenRate;
    }

    @Generated(hash = 1020796031)
    public ActiveModel() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public int getInstallRate() {
        return installRate;
    }

    public void setInstallRate(int installRate) {
        this.installRate = installRate;
    }

    public int getOpenRate() {
        return openRate;
    }

    public void setOpenRate(int openRate) {
        this.openRate = openRate;
    }

    public String getRelativePath() {
        return relativePath;
    }

    public void setRelativePath(String relativePath) {
        this.relativePath = relativePath;
    }

    public String getLastDate() {
        return lastDate;
    }

    public void setLastDate(String lastDate) {
        this.lastDate = lastDate;
    }

    public int getTempInstallRate() {
        return tempInstallRate;
    }

    public void setTempInstallRate(int tempInstallRate) {
        this.tempInstallRate = tempInstallRate;
    }

    public int getTempOpenRate() {
        return tempOpenRate;
    }

    public void setTempOpenRate(int tempOpenRate) {
        this.tempOpenRate = tempOpenRate;
    }
}
