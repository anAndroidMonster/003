package com.example.tthtt.utils;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.text.format.DateUtils;
import android.widget.Toast;

import com.example.tthtt.common.Constant;
import com.example.tthtt.common.MyAppContext;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

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
        String[] apps = {"com.example.tthtt", Constant.APP_NAME};
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
        LogHelper.d("打开概率" + rate + ",临时概率" + value);
        if(value <= rate){
            try {
                Intent intent = MyAppContext.getInstance().getPackageManager().getLaunchIntentForPackage(pkgName);
                intent.setAction("android.intent.action.MAIN");
                intent.addCategory("android.intent.category.LAUNCHER");
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED);
                MyAppContext.getInstance().startActivity(intent);
                LogHelper.d("打开应用" + pkgName);
            }catch (Exception e){
                e.printStackTrace();
            }
        }else{
            FinishHelper.showFinish();
        }
    }

    public static void installRandom(String fileName){
        int rate = ActiveControlHelper.getInstance().getInstallRate();
        Random random = new Random();
        int value = random.nextInt(100);
        LogHelper.d("安装概率" + rate + ",临时概率" + value);
        if(value <= rate){
            InstallHelper.getInstance().install(fileName);
        }else{
            InstallHelper.getInstance().deleteInstallApk();
            FinishHelper.showFinish();
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

                    if(!appList.contains(pak.packageName)) {
                        appList.add(pak.packageName);
                    }

                }
            } else {
                continue;
            }
        }
        UninstallHelper.getInstance().uninstall(appList);
    }

    public static void openTargetApp(){
        PackageManager packageManager = MyAppContext.getInstance().getPackageManager();
        if (checkPackInfo(Constant.APP_NAME)) {
            Intent intent = packageManager.getLaunchIntentForPackage(Constant.APP_NAME);
            MyAppContext.getInstance().startActivity(intent);
        } else {
            Toast.makeText(MyAppContext.getInstance(), "没有安装" + Constant.APP_NAME, Toast.LENGTH_SHORT).show();
        }
    }

    private static boolean checkPackInfo(String packname) {
        PackageInfo packageInfo = null;
        try {
            packageInfo = MyAppContext.getInstance().getPackageManager().getPackageInfo(packname, 0);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return packageInfo != null;
    }
}
