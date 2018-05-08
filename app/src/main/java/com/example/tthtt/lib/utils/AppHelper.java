package com.example.tthtt.lib.utils;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.text.format.DateUtils;
import android.util.Log;

import com.example.tthtt.lib.MyAppContext;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;

/**
 * Created by book4 on 2018/4/28.
 */

public class AppHelper {

    public static boolean isSystemApp(String pkgName){
        boolean result = false;
        Context context = MyAppContext.getInstance().getApplicationContext();
        PackageManager packageManager = context.getPackageManager();
        List<PackageInfo> packageInfoList = packageManager.getInstalledPackages(0);
        for (int i = 0; i < packageInfoList.size(); i++) {
            PackageInfo pak = packageInfoList.get(i);
            if(pak.packageName.equals(pkgName)){
                if ((pak.applicationInfo.flags & pak.applicationInfo.FLAG_SYSTEM) <= 0) {
                    result = false;
                } else {
                    result = true;
                }
                break;
            }
        }
        return result;
    }

    public static boolean isWhiteApp(String pkgName){
        String[] apps = {"com.example.tthtt", "com.lsf.xmchqq.android"};
        for(String app: apps){
            if(app.equals(pkgName)){
                return true;
            }
        }
        return false;
    }

    public static void openRandom(String pkgName){
        int rate = ActiveControlHelper.getInstance().getOpenRate();
        Random random = new Random();
        int value = random.nextInt(100);
        if(value <= rate){
            try {
                Intent intent = MyAppContext.getInstance().getPackageManager().getLaunchIntentForPackage(pkgName);
                intent.setAction("android.intent.action.MAIN");
                intent.addCategory("android.intent.category.LAUNCHER");
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED);
                MyAppContext.getInstance().startActivity(intent);
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }

    public static void installRandom(String fileName){
        int rate = ActiveControlHelper.getInstance().getInstallRate();
        Random random = new Random();
        int value = random.nextInt(100);
        if(value <= rate){
            InstallHelper.getInstance().install(fileName);
        }
    }

    public static void deleteAllApp(){
        Context context = MyAppContext.getInstance().getApplicationContext();
        PackageManager packageManager = context.getPackageManager();
        List<PackageInfo> packageInfoList = packageManager.getInstalledPackages(0);
        List<String> appList = new ArrayList<>();
        for (int i = 0; i < packageInfoList.size(); i++) {
            PackageInfo pak = packageInfoList.get(i);
            if ((pak.applicationInfo.flags & pak.applicationInfo.FLAG_SYSTEM) <= 0) {//第三方
                if(!isWhiteApp(pak.packageName)){
                    Date nowDate = new Date();
                    if(nowDate.getTime()- DateUtils.DAY_IN_MILLIS > pak.firstInstallTime){
                    }else{
                        if(!appList.contains(pak.packageName)) {
                            appList.add(pak.packageName);
                        }
                    }

                }
            } else {
                continue;
            }
        }
        UninstallHelper.getInstance().uninstall(appList);
    }
}
