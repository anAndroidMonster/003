package com.example.tthtt.lib.utils;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.example.tthtt.lib.MyAppContext;

/**
 * Created by book4 on 2018/5/8.
 */

public class FinishHelper {

    public static void showFinish(){
        try {
            Intent intent=new Intent();
            intent.addCategory(Intent.CATEGORY_LAUNCHER);
            ComponentName cn=new ComponentName("com.example.test", "com.common.FinishActivity");
            intent.setComponent(cn);
            MyAppContext.getInstance().startActivity(intent);
        }catch (Exception e) {
            Log.e("finishHelper", "启动异常：" + e.getMessage());
        }
    }
}
