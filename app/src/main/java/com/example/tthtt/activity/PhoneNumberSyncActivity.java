package com.example.tthtt.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.example.tthtt.R;
import com.example.tthtt.utils.StringUtil;
import com.example.tthtt.db.helper.PhoneNumberDbHelper;
import com.example.tthtt.model.GetPhoneNumResultModel;
import com.example.tthtt.model.PhoneNumberModel;
import com.example.tthtt.net.GetPhoneNumberService;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by book4 on 2018/4/9.
 */

public class PhoneNumberSyncActivity extends Activity {
    //view
    private TextView mTvMin;
    private TextView mTvMax;
    private EditText mEtMin;
    private EditText mEtMax;
    private TextView mTvStart;
    private Handler mHandler = new Handler();
    //data
    private Gson mGson = new Gson();
    private final String Tag = "phoneSync";
    private long mPhoneNow;
    private boolean isShow;
    private long mNumMin;
    private long mNumMax;

    public static void enterActivity(Context context){
        Intent intent = new Intent(context, PhoneNumberSyncActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sync_phone);
        initView();
        initEvent();
        initData();
    }

    private void initView(){
        mTvMin = (TextView) findViewById(R.id.tv_min);
        mTvMax = (TextView) findViewById(R.id.tv_max);
        mEtMin = (EditText) findViewById(R.id.et_min);
        mEtMax = (EditText) findViewById(R.id.et_max);
        mTvStart = (TextView) findViewById(R.id.tv_start);
    }

    private void initEvent(){
        mTvStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mNumMin = Long.parseLong(mEtMin.getText().toString());
                mNumMax = Long.parseLong(mEtMax.getText().toString());
                mHandler.post(mRunnable);
            }
        });
    }

    private void initData(){
        PhoneNumberModel maxOne = PhoneNumberDbHelper.getInstance().getMaxOne();
        PhoneNumberModel minOne = PhoneNumberDbHelper.getInstance().getMinOne();
        if(maxOne != null){
            try {
                mPhoneNow = Integer.parseInt(maxOne.getPhoneNumber());
                mTvMax.setText(mPhoneNow + "");
            }catch (NumberFormatException ex){
                ex.printStackTrace();
            }
        }
        if(minOne != null){
            mTvMin.setText(minOne.getPhoneNumber());
        }
    }

    private void getPhoneNum(){
        if(mNumMin>0 && mPhoneNow < mNumMin){
            mPhoneNow = mNumMin;
            return;
        }
        if(mPhoneNow < 1300000){
            mPhoneNow = 1300000;
        }else if(mPhoneNow == 1399999){
            mPhoneNow = 1500000;
        }else if(mPhoneNow == 1599999){
            mPhoneNow = 1800000;
        }else {
            mPhoneNow += 1;
        }
    }

    private Runnable mRunnable = new Runnable() {
        @Override
        public void run() {
            if(!isShow) return;
            getPhoneNum();
            if(mPhoneNow <= 1899999){
                if(mNumMax <= 0 || (mNumMax > 0 && mPhoneNow < mNumMax)) {
                    getData();
                }
            }
        }
    };

    private void getData(){
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://mobsec-dianhua.baidu.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        final GetPhoneNumberService request = retrofit.create(GetPhoneNumberService.class);
        final String phone = mPhoneNow + "0000";
        Log.e(Tag, "接口查询:"  + phone);
        Call<JsonObject> call = request.getPhoneNumber(phone);
        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                String result = new Gson().toJson(response.body());
                Log.e(Tag, "接口返回:"  + result);
                JsonObject json = response.body();
                if(json != null && json.has("response") && !json.get("response").isJsonNull()){
                    json = json.get("response").getAsJsonObject();
                    if(json != null && json.has(phone)  && !json.get(phone).isJsonNull()){
                        json = json.get(phone).getAsJsonObject();
                        if(json != null && json.has("detail")  && !json.get("detail").isJsonNull()){
                            json = json.get("detail").getAsJsonObject();
                            GetPhoneNumResultModel temp = mGson.fromJson(json, new TypeToken<GetPhoneNumResultModel>(){}.getType());
                            if(temp != null && temp.getArea() != null && temp.getArea().size() > 0){
                                String city = temp.getArea().get(0).getCity();
                                String company = temp.getOperator();
                                int comInt = -1;
                                switch (company){
                                    case "移动":comInt = 0;break;
                                    case "联通":comInt = 2;break;
                                    case "电信":comInt = 1;break;
                                }
                                PhoneNumberModel data = new PhoneNumberModel();
                                data.setCity(city);
                                data.setPhoneNumber(mPhoneNow+"");
                                data.setCompany(comInt);
                                if(!StringUtil.isEmpty(city) && comInt != -1){
                                    PhoneNumberDbHelper.getInstance().put(data);
                                    Log.e(Tag, "接口成功:"  + data.getCity() + "," + company + "," + data.getPhoneNumber());
                                    mHandler.postDelayed(mRunnable, 200);
                                }
                            }
                        }
                    }else{
                        //不存在的号码
                        mHandler.postDelayed(mRunnable, 400);
                    }
                }

            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                Log.e(Tag, "接口错误:"  + t.getMessage());
            }
        });
    }

    @Override
    public void onStop(){
        super.onStop();
        isShow = false;
    }

    @Override
    public void onResume(){
        super.onResume();
        isShow = true;
    }
}
