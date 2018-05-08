package com.example.tthtt.logic.syncPhoneDb.service;

import android.app.Notification;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Environment;
import android.os.IBinder;
import android.util.Log;
import android.widget.RemoteViews;

import com.example.tthtt.R;
import com.example.tthtt.lib.utils.AppHelper;
import com.example.tthtt.lib.utils.LogHelper;
import com.example.tthtt.logic.syncPhoneDb.db.ActiveDbHelper;
import com.example.tthtt.logic.syncPhoneDb.db.DeviceDbHelper;
import com.example.tthtt.logic.syncPhoneDb.db.PhoneDbHelper;
import com.example.tthtt.logic.syncPhoneDb.model.DeviceModel;
import com.example.tthtt.logic.syncPhoneDb.model.NetOutModel;
import com.example.tthtt.logic.syncPhoneDb.model.PhoneModel;
import com.example.tthtt.logic.syncPhoneDb.net.GetPhoneService;
import com.google.gson.Gson;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by book4 on 2018/4/28.
 */

public class SyncService extends Service {
    private Timer mTimer;
    private final int mMinPeriod = 300;//服务器极限速度200,客户端线程极限速度300
    private final String Tag = "syncService";
    private static final int NOTIFICATION_ID = 1017;

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        startForegroundCompat();
        mTimer = new Timer();
        mTimer.schedule(mTask, 1000, mMinPeriod);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        mTimer.cancel();
        stopForeground(true);
        super.onDestroy();
    }

    private TimerTask mTask = new TimerTask() {
        @Override
        public void run() {
            getData();

        }
    };

    private void getData(){
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://47.92.79.60/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        final GetPhoneService request = retrofit.create(GetPhoneService.class);
        Call<NetOutModel> call = request.getNewPhone();
        call.enqueue(new Callback<NetOutModel>() {
            @Override
            public void onResponse(Call<NetOutModel> call, Response<NetOutModel> response) {
                NetOutModel temp = response.body();
                String result = new Gson().toJson(temp);
                Log.e(Tag, "接口成功:");
                if(temp != null && temp.getData() != null && temp.getData().getCurrent_device_info() != null) {
                    DeviceModel device = temp.getData().getCurrent_device_info();

                    PhoneModel phone = new PhoneModel();
                    phone.setP_key(device.getP_key());
                    phone.setDevice_info(device.getDevice_info());
                    PhoneDbHelper.getInstance().putForService(phone);
                }
            }

            @Override
            public void onFailure(Call<NetOutModel> call, Throwable t) {
                Log.e(Tag, "接口错误:"  + t.getMessage());
            }
        });
    }

    private void startForegroundCompat() {
        if (Build.VERSION.SDK_INT < 18) {
            // api 18（4.3）以下，随便玩
            startForeground(NOTIFICATION_ID, new Notification());
        } else {
            // api 18的时候，google管严了，得绕着玩
            // 先把自己做成一个前台服务，提供合法的参数
            startForeground(NOTIFICATION_ID, fadeNotification(this));

            // 再起一个服务，也是前台的
            startService(new Intent(this, InnerService.class));
        }
    }

    public static class InnerService extends Service {

        @Override
        public void onCreate() {
            super.onCreate();
            // 先把自己也搞成前台的，提供合法参数
            startForeground(NOTIFICATION_ID, fadeNotification(this));

            // 关键步骤来了：自行推掉，或者把AlipayService退掉。
            // duang！系统sb了，说好的人与人的信任呢？
            stopSelf();
        }

        @Override
        public void onDestroy() {
            stopForeground(true);
            super.onDestroy();
        }

        @Override
        public IBinder onBind(Intent intent) {
            return null;
        }
    }

    private static Notification fadeNotification(Context context) {
        Notification notification = new Notification();
        // 随便给一个icon，反正不会显示，只是假装自己是合法的Notification而已
        notification.icon = R.drawable.ic_launcher_background;
        notification.contentView = new RemoteViews(context.getPackageName(), R.layout.activity_main);
        return notification;
    }
}
