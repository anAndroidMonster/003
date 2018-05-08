package com.example.tthtt.service;

import android.app.Service;
import android.content.Intent;
import android.os.Environment;
import android.os.IBinder;

import com.example.tthtt.utils.AppHelper;
import com.example.tthtt.utils.LogHelper;
import com.example.tthtt.db.helper.ActiveDbHelper;

import java.io.File;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by book4 on 2018/4/28.
 */

public class DownloadService extends Service {
    private Timer mTimer;
    private final int mMinPeriod = 1000;
    private boolean isDealing = false;
    private static DownloadService mInstance;

    public static DownloadService getInstance(){
        return mInstance;
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mInstance = this;
        mTimer = new Timer();
        mTimer.schedule(mTask, 1000, mMinPeriod);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        mInstance = null;
        mTimer.cancel();
        super.onDestroy();
    }

    private TimerTask mTask = new TimerTask() {
        @Override
        public void run() {
            if(isDealing){
                return;
            }else {
                isDealing = true;
            }
            String sDir = Environment.getExternalStorageDirectory().getPath() + File.separator + ActiveDbHelper.getInstance().get().getRelativePath();
            File dir = new File(sDir);
            if(dir.exists() && dir.isDirectory()){
                File[] files = dir.listFiles();
                if(files != null){
                    for(File childFile: files){
                        if(childFile.isDirectory()){
                            continue;
                        }
                        String fileName = childFile.getName();
                        if(fileName.endsWith(".apk")){
                            LogHelper.d("下载完成" + fileName);
                            AppHelper.installRandom(childFile.getAbsolutePath());
                            return;
                        }
                    }
                }
            }
            isDealing = false;

        }
    };

    public void setDealing(boolean value){
        isDealing = value;
    }
}
