package com.example.tthtt.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import com.example.tthtt.utils.AppHelper;
import com.example.tthtt.utils.InstallHelper;
import com.example.tthtt.utils.LogHelper;
import com.example.tthtt.service.DownloadService;

/**
 * Created by limin on 2017/9/28.
 */

public class AppReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        String packageName = intent.getData().getSchemeSpecificPart();
        if(AppHelper.isSystemApp(packageName) || AppHelper.isWhiteApp(packageName)){
            return;
        }
        if (intent.getAction().equals(Intent.ACTION_PACKAGE_ADDED)) {
            LogHelper.d("appReceiver:安装" + packageName);
            InstallHelper.getInstance().deleteInstallApk();
            DownloadService.getInstance().setDealing(false);
            AppHelper.openRandom(packageName);
        }
        if (intent.getAction().equals(Intent.ACTION_PACKAGE_REMOVED)) {
            LogHelper.d("appReceiver:卸载" + packageName);
        }
        if (intent.getAction().equals(Intent.ACTION_PACKAGE_REPLACED)) {
            LogHelper.d("appReceiver:升级" + packageName);
        }

    }
}
