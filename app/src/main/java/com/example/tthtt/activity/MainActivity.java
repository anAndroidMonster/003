package com.example.tthtt.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.TextView;

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
    private TextView mTvGetPhone;
    //data
    private final String Tag = "mainAct";
    private Handler mHandler = new Handler();
    private final long mTimeDelay = 1000*60;
    private int mConnectTime;
    private VpnModel mVpn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        initEvent();
        initData();
    }

    private void initView(){
        mTvGetPhone = (TextView) findViewById(R.id.tv_get_phone);
    }

    private void initEvent(){
        mTvGetPhone.setOnClickListener(mClickListener);
        findViewById(R.id.tv_setting).setOnClickListener(mClickListener);
    }

    private View.OnClickListener mClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.tv_get_phone:
                    mTvGetPhone.setBackgroundColor(getResources().getColor(R.color.app_gray));
                    mTvGetPhone.setText("");
                    showStatus("停止应用");
                    StopHelper.getInstance().kill();
                    showStatus("，卸载应用");
                    LogHelper.d("开始卸载");
                    AppHelper.deleteAllApp();
                    LogHelper.d("卸载完成");
                    reConnectVpn();
                    break;
                case R.id.tv_setting:
                    SettingActivity.enterActivity(MainActivity.this);
                    break;
            }
        }
    };

    private void initData(){

    }

    private Runnable mGetMachineRun = new Runnable() {
        @Override
        public void run() {
            boolean isDelay = HourControlHelper.getInstance().getIsDelay();
            showStatus("\n是否延迟：" + isDelay);
            if(isDelay){
                mHandler.postDelayed(mGetMachineRun, mTimeDelay);
            }else{
                getMachine();
            }
        }
    };

    @Override
    public void onResume(){
        super.onResume();
        ChangePhoneHelper.getInstance().init(MainActivity.this);


    }

    private void getMachine(){
        int type = RepeatControlHelper.getInstance().getRepeatType();
        showStatus("\n留存类型：" + type);
        DeviceModel data = DeviceDbHelper.getInstance().getOne(type, mVpn.getCity());
        if(data == null) {
            PowerHelper.shutDown();
        }else {
            PhoneModel phone = PhoneDbHelper.getInstance().getByKey(data.getP_key());
            showStatus("\n获取机型：" + phone.getP_key());
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
        if(deviceObj.has("latitude")){
            latOld = deviceObj.get("latitude").getAsString();
        }
        if(deviceObj.has("longitude")){
            lngOld = deviceObj.get("longitude").getAsString();
        }
        if(deviceObj.has("phone_number")){
            phoneOld = deviceObj.get("phone_number").getAsString();
        }
        LogHelper.d("dealData: old:" + latOld + "," + lngOld + "," + phoneOld);
        showStatus("\n旧数据：" + "," + latOld + "," + lngOld + "," + phoneOld);

        Random rand = new Random();
        int latValue = rand.nextInt(20000);
        int lngValue = rand.nextInt(20000);

        double latFinal = mVpn.getLat() + 0.000001 * (latValue - 10000);
        double lngFinal = mVpn.getLng() + 0.000001 * (lngValue - 10000);
        LogHelper.d("dealData: new:" + latFinal + "," + lngFinal);
        deviceObj.addProperty("longitude", lngFinal);
        deviceObj.addProperty("latitude", latFinal);
        showStatus("\n新经纬度：" + latFinal + "," + lngFinal);

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
                    showStatus("\n新手机号：" + phone);
                    deviceObj.addProperty("phone_number", phone);
                }
            }
        }
        data.setDevice_info(gson.toJson(deviceObj));
        data.setEdit(true);
        PhoneDbHelper.getInstance().put(data);
        showStatus("\n写入机型");
        ConfigFileHelper.WriteDeviceConfig(data);
        showStatus("\n清除sd卡");
        CleanHelper.getInstance().clearSdcard();

        if(mHandler != null){
            mHandler.post(new Runnable() {
                @Override
                public void run() {
                    mTvGetPhone.setBackgroundResource(R.color.app_green);
                }
            });
        }
    }
    
    private Runnable mIsVpnRun = new Runnable() {
        @Override
        public void run() {
            if(VpnHelper.isVpnConnected()){
                showStatus("\n城市:" + mVpn.getCity() + "，经纬度:" + mVpn.getLng() + "," + mVpn.getLat());
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

    private void showStatus(String content){
        if(StringUtil.isEmpty(content)) return;
        String old = mTvGetPhone.getText().toString();
        mTvGetPhone.setText(old + content);
    }

    private void reConnectVpn(){
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                showStatus("\n连接vpn");
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
    public void onDestroy(){
        super.onDestroy();
    }
}
