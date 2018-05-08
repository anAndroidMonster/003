package com.example.tthtt.logic.syncPhoneDb.act;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.tthtt.R;

/**
 * Created by book4 on 2018/4/8.
 */

public class SettingActivity extends Activity {


    public static void enterActivity(Context context){
        Intent intent = new Intent(context, SettingActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        initView();
        initEvent();
        initData();
    }

    private void initView(){

    }

    private void initEvent(){
        findViewById(R.id.tv_hour_control).setOnClickListener(mClickListener);
        findViewById(R.id.tv_repeat_control).setOnClickListener(mClickListener);
        findViewById(R.id.tv_set_address).setOnClickListener(mClickListener);
        findViewById(R.id.tv_download_machine).setOnClickListener(mClickListener);
        findViewById(R.id.tv_download_phone_num).setOnClickListener(mClickListener);
        findViewById(R.id.tv_active_control).setOnClickListener(mClickListener);
    }

    private View.OnClickListener mClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.tv_hour_control:
                    HourControlActivity.enterActivity(SettingActivity.this);
                    break;
                case R.id.tv_repeat_control:
                    RepeatControlActivity.enterActivity(SettingActivity.this);
                    break;
                case R.id.tv_set_address:
                    VpnListActivity.enterActivity(SettingActivity.this);
                    break;
                case R.id.tv_download_machine:
                    SyncActivity.enterActivity(SettingActivity.this);
                    break;
                case R.id.tv_download_phone_num:
                    PhoneNumberSyncActivity.enterActivity(SettingActivity.this);
                    break;
                case R.id.tv_active_control:
                    ActiveSetActivity.enterActivity(SettingActivity.this);
                    break;
            }
        }
    };

    private void initData(){

    }
}
