package com.example.tthtt.common;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.multidex.MultiDexApplication;
import android.util.Log;

import com.example.tthtt.db.AppDbHelper;
import com.example.tthtt.utils.LogHelper;

import java.util.List;

/**
 * Created by book4 on 2018/3/16.
 */

public class tthttContext extends MultiDexApplication {
    private static Context mInstance;

    @Override
    public void onCreate() {
        super.onCreate();
        mInstance = this;
        AppDbHelper.getInstance().init(this);
        watchBackground();
    }

    public static Context getInstance(){
        return mInstance;
    }

    private int count = 0;
    private void watchBackground(){
        registerActivityLifecycleCallbacks(new ActivityLifecycleCallbacks() {

            @Override
            public void onActivityStopped(Activity activity) {
                count--;
                if (count == 0) {
                    LogHelper.d("tthtt回到前台");
                    //防止卸载弹出浏览器
                    backToFront();
                    Log.v("tthtt", ">>>>>>>>>>>>>>>>>>>切到后台  lifecycle");
                }
            }

            @Override
            public void onActivityStarted(Activity activity) {
                if (count == 0) {
                    Log.v("tthtt", ">>>>>>>>>>>>>>>>>>>切到前台  lifecycle");
                }
                count++;
            }

            @Override
            public void onActivitySaveInstanceState(Activity activity, Bundle outState) {
                Log.v("tthtt", activity + "onActivitySaveInstanceState");
            }

            @Override
            public void onActivityResumed(final Activity activity) {
                Log.v("tthtt", activity + "onActivityResumed");
            }

            @Override
            public void onActivityPaused(Activity activity) {
                Log.v("tthtt", activity + "onActivityPaused");
            }

            @Override
            public void onActivityDestroyed(Activity activity) {
                Log.v("tthtt", activity + "onActivityDestroyed");
            }

            @Override
            public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
                Log.v("tthtt", activity + "onActivityCreated");
            }
        });
    }

    private void backToFront(){
        ActivityManager am = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningTaskInfo> taskList = am.getRunningTasks(100);
        for (ActivityManager.RunningTaskInfo rti : taskList) {
            //找到当前应用的task，并启动task的栈顶activity，达到程序切换到前台
            final ComponentName topActivity = rti.topActivity;
            if(topActivity.getPackageName().equals(getPackageName())) {
//                            am.moveTaskToFront(rti.id, 0);
                try {
                    Intent intent = new Intent();
                    intent.addCategory(Intent.ACTION_MAIN);
                    intent.addCategory(Intent.CATEGORY_LAUNCHER);
                    intent.setComponent(new ComponentName(
                            tthttContext.this, Class
                            .forName(topActivity.getClassName())));
                    intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT
                            | Intent.FLAG_ACTIVITY_NEW_TASK
                            | Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED);
                    tthttContext.this.startActivity(intent);
                }catch (ClassNotFoundException ex){
                    ex.printStackTrace();
                }
                break;
            }
        }
    }
}
