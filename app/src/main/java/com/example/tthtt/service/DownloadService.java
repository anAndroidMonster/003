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

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        LogHelper.d("启动安装服务");
        doInstall();
        stopSelf();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        LogHelper.d("安装服务销毁");
        super.onDestroy();
    }

    private void doInstall(){
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
                        AppHelper.installRandom(childFile.getAbsolutePath());
                        return;
                    }
                }
            }
        }
    }
}
