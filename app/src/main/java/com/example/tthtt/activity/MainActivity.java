package com.example.tthtt.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.tthtt.R;
import com.example.tthtt.utils.AppHelper;
import com.example.tthtt.utils.ChangePhoneHelper;
import com.example.tthtt.utils.CleanHelper;
import com.example.tthtt.utils.ConfigFileHelper;
import com.example.tthtt.utils.HourControlHelper;
import com.example.tthtt.utils.LogHelper;
import com.example.tthtt.utils.PowerHelper;
import com.example.tthtt.utils.RepeatControlHelper;
import com.example.tthtt.utils.StopHelper;
import com.example.tthtt.utils.StringUtil;
import com.example.tthtt.utils.VpnHelper;
import com.example.tthtt.db.helper.DeviceDbHelper;
import com.example.tthtt.db.helper.PhoneDbHelper;
import com.example.tthtt.db.helper.PhoneNumberDbHelper;
import com.example.tthtt.model.DeviceModel;
import com.example.tthtt.model.PhoneModel;
import com.example.tthtt.model.PhoneNumberModel;
import com.example.tthtt.model.VpnModel;
import com.example.tthtt.service.DownloadService;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

import java.util.Random;

public class MainActivity extends Activity {

    //view
    //data
    private Handler mHandler = new Handler();
    private int mConnectTime;
    private VpnModel mVpn;
    private boolean isShowing;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        initEvent();
        initData();
    }

    private void initView(){
    }

    private void initEvent(){
        findViewById(R.id.tv_setting).setOnClickListener(mClickListener);
    }

    private View.OnClickListener mClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.tv_setting:
                    SettingActivity.enterActivity(MainActivity.this);
                    break;
            }
        }
    };

    private void initData(){
        ChangePhoneHelper.getInstance().init(MainActivity.this);
    }

    private Runnable mGetMachineRun = new Runnable() {
        @Override
        public void run() {
            //去掉延迟
            getMachine();
        }
    };

    @Override
    public void onResume(){
        super.onResume();

        Toast.makeText(MainActivity.this, "倒计时30秒", Toast.LENGTH_SHORT).show();
        isShowing = true;
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if(isShowing){
                    //停止应用，清除应用数据
                    StopHelper.getInstance().kill();
                    //删除安装应用
                    AppHelper.deleteAllApp();
                    //清除sd卡
                    CleanHelper.getInstance().clearSdcard();
                    //连接网络
                    reConnectVpn();
                }
            }
        }, 1000*30);

    }

    private void getMachine(){
        int type = RepeatControlHelper.getInstance().getRepeatType();
        LogHelper.d("留存类型：" + type);
        DeviceModel data = DeviceDbHelper.getInstance().getOne(type, mVpn.getCity());
        if(data == null) {
            PowerHelper.shutDown();
        }else {
            PhoneModel phone = PhoneDbHelper.getInstance().getByKey(data.getP_key());
            LogHelper.d("获取机型：" + phone.getP_key());
            dealWithData(phone);


        }
    }

    private void dealWithData(PhoneModel data){
        if(data == null ) return;
        String deviceInfo = data.getDevice_info();
        Gson gson = new Gson();
        JsonObject deviceObj = gson.fromJson(deviceInfo, JsonObject.class);
        String latOld = "";
        String lngOld = "";
        String phoneOld = "";
        String stationOld = "";
        if(deviceObj.has("latitude")){
            latOld = deviceObj.get("latitude").getAsString();
        }
        if(deviceObj.has("longitude")){
            lngOld = deviceObj.get("longitude").getAsString();
        }
        if(deviceObj.has("phone_number")){
            phoneOld = deviceObj.get("phone_number").getAsString();
        }
        if(deviceObj.has("base_station_nearby")){
            stationOld = deviceObj.get("base_station_nearby").getAsString();
        }
        LogHelper.d("dealData: old:" + latOld + "," + lngOld + "," + phoneOld + "," + stationOld);

        Random rand = new Random();
        int latValue = rand.nextInt(20000);
        int lngValue = rand.nextInt(20000);
        int latStation = rand.nextInt(20000);
        int lngStation = rand.nextInt(20000);

        double latFinal = mVpn.getLat() + 0.000001 * (latValue - 10000);
        double lngFinal = mVpn.getLng() + 0.000001 * (lngValue - 10000);
        double latStationFinal = mVpn.getLat() + 0.000001 * (latStation - 10000);
        double lngStationFinal = mVpn.getLng() + 0.000001 * (lngStation - 10000);
        LogHelper.d("新经纬度:" + latFinal + "," + lngFinal);
        deviceObj.addProperty("longitude", lngFinal);
        deviceObj.addProperty("latitude", latFinal);

        if(stationOld.length() > 0){
            int lastIndex = stationOld.lastIndexOf(",");
            if(lastIndex > 0){
                stationOld = stationOld.substring(0, lastIndex);
                stationOld = stationOld + lngStationFinal + "," + latStationFinal;
                LogHelper.d("新基站经纬度：" + stationOld);
                deviceObj.addProperty("base_station_nearby", stationOld);
                deviceObj.addProperty("base_station_self", stationOld);
            }
        }

        if(!data.isEdit() && deviceObj.has("phone_number")) {
            String phone = deviceObj.get("phone_number").getAsString();
            long count = PhoneNumberDbHelper.getInstance().getCount(mVpn.getCity());
            if(count > 0) {
                int record = rand.nextInt((int) count);
                PhoneNumberModel phoneNum = PhoneNumberDbHelper.getInstance().getRecord(mVpn.getCity(), record);
                if (phoneNum != null) {
                    phone = phone.substring(phoneNum.getPhoneNumber().length());
                    phone = phoneNum.getPhoneNumber() + phone;
                    LogHelper.d("dealData: new:" + phone);
                    LogHelper.d("新手机号：" + phone);
                    deviceObj.addProperty("phone_number", phone);
                }
            }
        }
        data.setDevice_info(gson.toJson(deviceObj));
        data.setEdit(true);
        PhoneDbHelper.getInstance().put(data);
        ConfigFileHelper.WriteDeviceConfig(data);
        LogHelper.d("写入完毕");


        if(mHandler != null){
            mHandler.post(new Runnable() {
                @Override
                public void run() {
                    AppHelper.openTargetApp();
                }
            });
        }
    }
    
    private Runnable mIsVpnRun = new Runnable() {
        @Override
        public void run() {
            if(VpnHelper.isVpnConnected()){
                LogHelper.d("连接成功，城市:" + mVpn.getCity() + "，经纬度:" + mVpn.getLng() + "," + mVpn.getLat());
                mHandler.postDelayed(mGetMachineRun, 1000*2);
            }else{
                mConnectTime += 1;
                if(mConnectTime < 20){//20秒重试
                    mHandler.postDelayed(mIsVpnRun, 1000);
                }else{
                    reConnectVpn();
                }

            }
        }
    };

    private void reConnectVpn(){
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                LogHelper.d("重新连接");
                mConnectTime = 0;
                mVpn = ChangePhoneHelper.getInstance().connectVpnRandom();
                if(mVpn == null){
                    PowerHelper.shutDown();
                }
            }
        }, 1000);

        mHandler.postDelayed(mIsVpnRun, 2000);
    }

    @Override
    public void onStop(){
        isShowing = false;
        Toast.makeText(MainActivity.this, "倒计时取消", Toast.LENGTH_SHORT).show();
        super.onStop();
    }
}
