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
        Intent intent = new Intent(MainActivity.this, DownloadService.class);
        startService(intent);
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

//    private void getCity(){
//        Retrofit retrofit = new Retrofit.Builder()
//                .baseUrl("http://int.dpool.sina.com.cn/")
//                .addConverterFactory(GsonConverterFactory.create())
//                .build();
//        final GetCityService request = retrofit.create(GetCityService.class);
//        Call<GetCityModel> call = request.getCity();
//        showStatus("\n获取城市");
//        call.enqueue(new Callback<GetCityModel>() {
//            @Override
//            public void onResponse(Call<GetCityModel> call, Response<GetCityModel> response) {
//                GetCityModel temp = response.body();
//                if(temp != null && temp.getCity() != null ) {
//                    mCity = temp.getCity();
//                    showStatus("，获取成功:" + mCity);
//                    mHandler.post(mGetMachineRun);
//                }
//                String result = new Gson().toJson(response.body());
//                LogHelper.d("getCity:" + result);
//            }
//
//            @Override
//            public void onFailure(Call<GetCityModel> call, Throwable t) {
//                showStatus("，获取失败");
//                LogHelper.d("getCity:" + t.getMessage());
//                mHandler.post(new Runnable() {
//                    @Override
//                    public void run() {
//                        reConnectVpn();
//                    }
//                });
//            }
//        });
//    }

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

//    private void getLatLng(PhoneModel data){
//        LocationModel address = LocationDbHelper.getInstance().getByCity(mCity);
//        if(address != null){
//            dealWithData(data, address);
//        }else{
//            LogHelper.d("getLatLng本地:null");
//            getLatLngFromNet(data, mCity);
//        }
//    }

//    private void getLatLngFromNet(final PhoneModel phone, final String city){
//        PoiSearch.Query query = new PoiSearch.Query(city, "190104", "");
//        query.setPageSize(10);// 设置每页最多返回多少条poiitem
//        query.setPageNum(1);
//        PoiSearch poiSearch = new PoiSearch(this, query);
//        LogHelper.d("getLatLng:入参" + city);
//        showStatus("\n获取经纬度：" + city);
//        poiSearch.setOnPoiSearchListener(new PoiSearch.OnPoiSearchListener() {
//            @Override
//            public void onPoiSearched(PoiResult poiResult, int i) {
//                if(poiResult != null && poiResult.getPois() != null && poiResult.getPois().size() > 0){
//                    PoiItem item = poiResult.getPois().get(0);
//                    LatLonPoint latLng = item.getLatLonPoint();
//                    LogHelper.d("getLatLng:经纬度" + latLng.getLongitude() + "," + latLng.getLatitude());
//                    showStatus("，获取成功");
//                    LocationModel data = new LocationModel();
//                    data.setCity(city);
//                    data.setLat(latLng.getLatitude());
//                    data.setLng(latLng.getLongitude());
//                    LocationDbHelper.getInstance().put(data);
//                    dealWithData(phone, data);
//                }else{
//                    showStatus("，获取失败");
//                    LogHelper.d("getLatLng:请求失败");
//                    dealWithData(phone, null);
//                }
//
//            }
//
//            @Override
//            public void onPoiItemSearched(PoiItem poiItem, int i) {
//
//            }
//        });
//        poiSearch.searchPOIAsyn();
//    }

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
        Intent intent = new Intent(MainActivity.this, DownloadService.class);
        stopService(intent);
        super.onDestroy();
    }
}
