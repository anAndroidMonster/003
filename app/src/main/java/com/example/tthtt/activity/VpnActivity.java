package com.example.tthtt.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.amap.api.services.core.LatLonPoint;
import com.amap.api.services.core.PoiItem;
import com.amap.api.services.poisearch.PoiResult;
import com.amap.api.services.poisearch.PoiSearch;
import com.example.tthtt.R;
import com.example.tthtt.common.MyAppContext;
import com.example.tthtt.utils.ChangePhoneHelper;
import com.example.tthtt.utils.ConfigFileHelper;
import com.example.tthtt.utils.LogHelper;
import com.example.tthtt.utils.StopHelper;
import com.example.tthtt.utils.StringUtil;
import com.example.tthtt.utils.VpnHelper;
import com.example.tthtt.db.helper.VpnDbHelper;
import com.example.tthtt.model.GetCityModel;
import com.example.tthtt.model.VpnModel;
import com.example.tthtt.net.GetCityService;
import com.google.gson.Gson;

import java.util.HashMap;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by book4 on 2018/4/28.
 */

public class VpnActivity extends Activity {
    //view
    private EditText mEtName;
    private EditText mEtAddress;
    private TextView mTvMppe;
    private EditText mEtUser;
    private EditText mEtPsw;
    private TextView mTvSaveVpn;
    private TextView mTvDeleteVpn;
    private TextView mTvConnect;
    private TextView mTvTest;
    private TextView mEtCity;
    private TextView mEtLng;
    private TextView mEtLat;
    private TextView mTvGetLocation;
    private TextView mTvIsGood;
    //data
    private VpnModel mData;
    private final String codeStr = "51670030007378737A6C742E69707A756964756F2E6E65740069627A7930303100627A793030310000000066616C7365000000000000";

    public static void enterActivity(Context context){
        Intent intent = new Intent(context, VpnActivity.class);
        context.startActivity(intent);
    }

    public static void enterActivity(Context context, VpnModel data){
        Intent intent = new Intent(context, VpnActivity.class);
        intent.putExtra("vpn", data);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vpn);
        getInParam();
        initView();
        initEvent();
        initData();
    }

    private void getInParam(){
        mData = (VpnModel) getIntent().getSerializableExtra("vpn");
        if(mData == null){
            mData = ChangePhoneHelper.getInstance().getVpn(codeStr);
        }
    }

    private void initView(){
        mEtName = (EditText) findViewById(R.id.et_name);
        mEtAddress = (EditText) findViewById(R.id.et_address);
        mTvMppe = (TextView) findViewById(R.id.tv_mppe);
        mEtUser = (EditText) findViewById(R.id.et_user);
        mEtPsw = (EditText) findViewById(R.id.et_psw);
        mTvSaveVpn = (TextView) findViewById(R.id.tv_save);
        mTvDeleteVpn = (TextView) findViewById(R.id.tv_delete);
        mTvConnect = (TextView) findViewById(R.id.tv_connect);
        mTvTest = (TextView) findViewById(R.id.tv_test);
        mEtCity = (EditText) findViewById(R.id.tv_city);
        mEtLng = (EditText) findViewById(R.id.tv_lng);
        mEtLat = (EditText) findViewById(R.id.tv_lat);
        mTvGetLocation = (TextView) findViewById(R.id.tv_get_location);
        mTvIsGood = (TextView) findViewById(R.id.tv_is_good);
    }

    private void initEvent(){
        mTvMppe.setOnClickListener(mClickListener);
        mTvConnect.setOnClickListener(mClickListener);
        mTvTest.setOnClickListener(mClickListener);
        mTvGetLocation.setOnClickListener(mClickListener);
        mTvSaveVpn.setOnClickListener(mClickListener);
        mTvDeleteVpn.setOnClickListener(mClickListener);
        mTvIsGood.setOnClickListener(mClickListener);
    }

    private View.OnClickListener mClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.tv_mppe:
                    boolean temp = mData.getMppe();
                    mData.setMppe(!temp);
                    showData();
                    break;
                case R.id.tv_is_good:
                    temp = mData.getIsGood();
                    mData.setIsGood(!temp);
                    showData();
                    break;
                case R.id.tv_connect:
                    connect();
                    break;
                case R.id.tv_test:
                    //清理并打开
                    StopHelper.getInstance().killAdDemo();
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            MyAppContext.getInstance().setNeedFront(false);
                            openAdActivity();
                        }
                    }, 1000*5);
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            MyAppContext.getInstance().setNeedFront(true);
                        }
                    }, 1000*8);
                    break;
                case R.id.tv_get_location:
                    getCity();
                    break;
                case R.id.tv_save:
                    doSave();
                    break;
                case R.id.tv_delete:
                    VpnDbHelper.getInstance().delete(mData.getId());
                    finish();
                    break;
            }
        }
    };

    private void initData(){
        if(mData == null){
            Toast.makeText(VpnActivity.this, "数据异常", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }
        showData();
    }

    private void showData(){
        if(mData != null){
            mEtName.setText(mData.getName());
            mEtAddress.setText(mData.getServer());
            mTvMppe.setBackgroundResource(mData.getMppe()? R.color.app_green: R.color.app_red);
            mEtUser.setText(mData.getUsername());
            mEtPsw.setText(mData.getPassword());
            mTvIsGood.setBackgroundResource(mData.getIsGood()? R.color.app_green: R.color.app_red);
            mEtCity.setText(mData.getCity());
            mEtLng.setText(mData.getLng() + "");
            mEtLat.setText(mData.getLat() + "");
        }
    }

    private void connect(){
        String name = mEtName.getText().toString();
        String address = mEtAddress.getText().toString();
        String user = mEtUser.getText().toString();
        String psw = mEtPsw.getText().toString();
        if(StringUtil.isEmpty(name) || StringUtil.isEmpty(address) || StringUtil.isEmpty(user) || StringUtil.isEmpty(psw)){
            Toast.makeText(VpnActivity.this, "请先填写数据", Toast.LENGTH_SHORT).show();
            return;
        }
        mData.setName(name);
        mData.setServer(address);
        mData.setUsername(user);
        mData.setPassword(psw);


        HashMap<String, String> map = ConfigFileHelper.ReadConfig();
        if(map == null || map.size() <= 0) return;
        String code = ChangePhoneHelper.getInstance().getVpnCode(mData);
        map.put("VPN_PROFILE", code);

        ChangePhoneHelper.getInstance().connectVpn(code);

    }

    private void getCity(){
        if(!VpnHelper.isVpnConnected()){
            Toast.makeText(VpnActivity.this, "vpn未连接", Toast.LENGTH_SHORT).show();
            return;
        }
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://int.dpool.sina.com.cn/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        final GetCityService request = retrofit.create(GetCityService.class);
        Call<GetCityModel> call = request.getCity();
        call.enqueue(new Callback<GetCityModel>() {
            @Override
            public void onResponse(Call<GetCityModel> call, Response<GetCityModel> response) {
                GetCityModel temp = response.body();
                if(temp != null && temp.getCity() != null ) {
                    String city = temp.getCity();
                    mData.setCity(city);
                    showData();
                    getLatLngFromNet(city);
                }
                String result = new Gson().toJson(response.body());
                LogHelper.d("getCity:" + result);
            }

            @Override
            public void onFailure(Call<GetCityModel> call, Throwable t) {
                LogHelper.d("getCity:" + t.getMessage());
                Toast.makeText(VpnActivity.this, "获取城市失败", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void getLatLngFromNet(final String city){
        PoiSearch.Query query = new PoiSearch.Query(city, "190104", "");
        query.setPageSize(10);// 设置每页最多返回多少条poiitem
        query.setPageNum(1);
        PoiSearch poiSearch = new PoiSearch(this, query);
        LogHelper.d("getLatLng:入参" + city);
        poiSearch.setOnPoiSearchListener(new PoiSearch.OnPoiSearchListener() {
            @Override
            public void onPoiSearched(PoiResult poiResult, int i) {
                if(poiResult != null && poiResult.getPois() != null && poiResult.getPois().size() > 0){
                    PoiItem item = poiResult.getPois().get(0);
                    LatLonPoint latLng = item.getLatLonPoint();
                    LogHelper.d("getLatLng:经纬度" + latLng.getLongitude() + "," + latLng.getLatitude());
                    mData.setLat(latLng.getLatitude());
                    mData.setLng(latLng.getLongitude());
                    showData();

                }else{
                    LogHelper.d("getLatLng:请求失败");
                    Toast.makeText(VpnActivity.this, "获取经纬度失败", Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onPoiItemSearched(PoiItem poiItem, int i) {

            }
        });
        poiSearch.searchPOIAsyn();
    }

    private void doSave(){
        String name = mEtName.getText().toString();
        String address = mEtAddress.getText().toString();
        String user = mEtUser.getText().toString();
        String psw = mEtPsw.getText().toString();
        if(StringUtil.isEmpty(name) || StringUtil.isEmpty(address) || StringUtil.isEmpty(user) || StringUtil.isEmpty(psw)){
            Toast.makeText(VpnActivity.this, "请先填写数据", Toast.LENGTH_SHORT).show();
            return;
        }
        String city = mEtCity.getText().toString();
        String lat = mEtLat.getText().toString();
        String lng = mEtLng.getText().toString();
        if(StringUtil.isEmpty(city) || StringUtil.isEmpty(lat) || StringUtil.isEmpty(lng) ){
            Toast.makeText(VpnActivity.this, "请先获取地址", Toast.LENGTH_SHORT).show();
            return;
        }
        mData.setName(name);
        mData.setServer(address);
        mData.setUsername(user);
        mData.setPassword(psw);
        mData.setCity(city);
        mData.setLat(Double.parseDouble(lat));
        mData.setLng(Double.parseDouble(lng));
        VpnDbHelper.getInstance().put(mData);
        String code = ChangePhoneHelper.getInstance().getVpnCode(mData);
        mData.setCodeStr(code);
        Toast.makeText(VpnActivity.this, "保存成功", Toast.LENGTH_SHORT).show();
        finish();
    }

    private void openAdActivity(){
        try {
            Intent intent = getPackageManager().getLaunchIntentForPackage("com.example.addemo");
            intent.setAction("android.intent.action.MAIN");
            intent.addCategory("android.intent.category.LAUNCHER");
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED);
            startActivity(intent);
        }catch (Exception ex){
            ex.printStackTrace();
        }
    }
}
