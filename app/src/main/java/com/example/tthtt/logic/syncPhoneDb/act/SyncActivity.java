package com.example.tthtt.logic.syncPhoneDb.act;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.TextView;

import com.example.tthtt.R;
import com.example.tthtt.lib.utils.VpnHelper;
import com.example.tthtt.logic.syncPhoneDb.db.DeviceDbHelper;
import com.example.tthtt.logic.syncPhoneDb.db.PhoneDbHelper;
import com.example.tthtt.logic.syncPhoneDb.model.DeviceModel;
import com.example.tthtt.logic.syncPhoneDb.model.NetOutModel;
import com.example.tthtt.logic.syncPhoneDb.model.PhoneModel;
import com.example.tthtt.logic.syncPhoneDb.net.GetPhoneService;
import com.example.tthtt.logic.syncPhoneDb.service.DownloadService;
import com.example.tthtt.logic.syncPhoneDb.service.SyncService;
import com.google.gson.Gson;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class SyncActivity extends Activity {

    //view
    private TextView mTvStart;
    private TextView mTvStop;
    private TextView mTvNum;
    private Handler mHandler = new Handler();

    private TextView mTvVpn;
    //data
    private static final String Tag = "syncAct";
    private int mWriteNum;
    private boolean isGet;

    public static void enterActivity(Context context){
        Intent intent = new Intent(context, SyncActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sync);
        initView();
        initEvent();
        initData();
    }

    private void initView(){
        mTvStart = (TextView) findViewById(R.id.tv_start);
        mTvNum = (TextView) findViewById(R.id.tv_num);
        mTvVpn = (TextView) findViewById(R.id.tv_start_service);
        mTvStop = (TextView) findViewById(R.id.tv_stop_service);
    }

    private void initEvent(){
        mTvStart.setOnClickListener(mClickListener);
        mTvVpn.setOnClickListener(mClickListener);
        mTvStop.setOnClickListener(mClickListener);
    }

    private View.OnClickListener mClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.tv_start:
                    isGet = true;
                    getData();
                    break;
                case R.id.tv_start_service:
                    Intent intent = new Intent(SyncActivity.this, SyncService.class);
                    startService(intent);
                    intent = new Intent(SyncActivity.this, DownloadService.class);
                    stopService(intent);
                    break;
                case R.id.tv_stop_service:
                    intent = new Intent(SyncActivity.this, SyncService.class);
                    stopService(intent);
                    break;
            }
        }
    };

    private void initData(){
        mTvNum.setText(PhoneDbHelper.getInstance().getCount() + "");
    }

    private Runnable mRunnable = new Runnable() {
        @Override
        public void run() {
            if(isGet) {
                getData();
            }
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
                if(temp != null && temp.getData() != null && temp.getData().getCurrent_device_info() != null) {
                    DeviceModel device = temp.getData().getCurrent_device_info();
                    DeviceDbHelper.getInstance().put(device);
                    PhoneModel phone = new PhoneModel();
                    phone.setP_key(device.getP_key());
                    phone.setDevice_info(device.getDevice_info());
                    PhoneDbHelper.getInstance().put(phone);

                    mWriteNum += 1;
                    if(mWriteNum >= 100) {
                        mWriteNum = 0;
                        initData();
                    }
                }
                String result = new Gson().toJson(response.body());
                Log.e(Tag, "接口成功:"  + result);
                mHandler.post(mRunnable);
            }

            @Override
            public void onFailure(Call<NetOutModel> call, Throwable t) {
                Log.e(Tag, "接口错误:"  + t.getMessage());
                mHandler.post(mRunnable);
            }
        });
    }

    @Override
    public void onStop(){
        super.onStop();
        isGet = false;
        mHandler.removeCallbacksAndMessages(null);
    }

    @Override
    public void onResume(){
        super.onResume();
    }
}