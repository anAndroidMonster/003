package com.example.tthtt.utils;

import android.content.ComponentName;
import android.content.Intent;
import android.util.Log;

import com.example.tthtt.common.Constant;
import com.example.tthtt.common.MyAppContext;

/**
 * Created by book4 on 2018/5/8.
 */

public class FinishHelper {

    public static void showFinish(){
        try {
            Intent intent=new Intent();
            intent.addCategory(Intent.CATEGORY_LAUNCHER);
            ComponentName cn=new ComponentName(Constant.APP_NAME, "com.common.FinishActivity");
            intent.setComponent(cn);
            intent.addFlags( Intent.FLAG_ACTIVITY_NEW_TASK);
            MyAppContext.getInstance().startActivity(intent);
        }catch (Exception e) {
            Log.e("finishHelper", "启动异常：" + e.getMessage());
        }
    }
}
