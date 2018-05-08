package com.example.tthtt.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.amap.api.services.core.LatLonPoint;
import com.amap.api.services.core.PoiItem;
import com.amap.api.services.poisearch.PoiResult;
import com.amap.api.services.poisearch.PoiSearch;
import com.example.tthtt.R;
import com.example.tthtt.utils.StringUtil;
import com.example.tthtt.db.helper.LocationDbHelper;
import com.example.tthtt.db.helper.PhoneNumberDbHelper;
import com.example.tthtt.model.LocationModel;

/**
 * Created by book4 on 2018/3/28.
 */

public class AddressDetailActivity extends Activity {
    //view
    private EditText mEtCity;
    private EditText mTvLat;
    private EditText mTvLng;
    private TextView mTvYd;
    private TextView mTvDx;
    private TextView mTvLt;
    private TextView mTvSave;
    //data
    private LocationModel mData;

    public static void enterActivity(Context context){
        Intent intent = new Intent(context, AddressDetailActivity.class);
        context.startActivity(intent);
    }

    public static void enterActivity(Context context, LocationModel address){
        Intent intent = new Intent(context, AddressDetailActivity.class);
        intent.putExtra("address", address);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_address_detail);
        getInParam();
        initView();
        initEvent();
        initData();
    }

    private void getInParam(){
        if(getIntent().getExtras() != null){
            mData = (LocationModel) getIntent().getExtras().getSerializable("address");
        }
    }

    private void initView(){
        mEtCity = (EditText) findViewById(R.id.et_city);
        mTvLat = (EditText) findViewById(R.id.tv_lat);
        mTvLng = (EditText) findViewById(R.id.tv_lng);
        mTvYd = (TextView) findViewById(R.id.tv_yd);
        mTvDx = (TextView) findViewById(R.id.tv_dx);
        mTvLt = (TextView) findViewById(R.id.tv_lt);
        mTvSave = (TextView) findViewById(R.id.tv_save);
    }

    private void initEvent(){
        mTvSave.setOnClickListener(mClickListener);
    }

    private View.OnClickListener mClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.tv_save:
//                    doSave();
                    String city = mEtCity.getText().toString();
                    String lat = mTvLat.getText().toString();
                    String lng = mTvLng.getText().toString();
                    if(StringUtil.isEmpty(lat) || StringUtil.isEmpty(lng)) {
                        getLatLngFromNet(city);
                    }else{
                        LocationModel data = new LocationModel();
                        data.setCity(city);
                        data.setLat(Double.parseDouble(lat));
                        data.setLng(Double.parseDouble(lng));
                        LocationDbHelper.getInstance().put(data);
                        Toast.makeText(AddressDetailActivity.this, "保存成功", Toast.LENGTH_SHORT).show();
                        finish();
                    }
                    break;
            }
        }
    };

    private void initData(){
        if(mData != null){
            mEtCity.setText(mData.getCity());
            mTvLng.setText(mData.getLng() + "");
            mTvLat.setText(mData.getLat() + "");
        }
    }

    @Override
    public void onResume(){
        super.onResume();
        showPhoneNumber();
    }

    private void showPhoneNumber(){
        String city = mEtCity.getText().toString();
        if(!StringUtil.isEmpty(city)) {
            long yd = PhoneNumberDbHelper.getInstance().getCount(city, 0);
            long dx = PhoneNumberDbHelper.getInstance().getCount(city, 1);
            long lt = PhoneNumberDbHelper.getInstance().getCount(city, 2);
            mTvYd.setText("移动" + yd);
            mTvDx.setText("电信" + dx);
            mTvLt.setText("联通" + lt);
        }
    }

    private void getLatLngFromNet(final String city){
        PoiSearch.Query query = new PoiSearch.Query(city, "190104", "");
        query.setPageSize(10);// 设置每页最多返回多少条poiitem
        query.setPageNum(1);
        PoiSearch poiSearch = new PoiSearch(this, query);
        poiSearch.setOnPoiSearchListener(new PoiSearch.OnPoiSearchListener() {
            @Override
            public void onPoiSearched(PoiResult poiResult, int i) {
                if(poiResult != null && poiResult.getPois() != null && poiResult.getPois().size() > 0){
                    PoiItem item = poiResult.getPois().get(0);
                    LatLonPoint latLng = item.getLatLonPoint();
                    Log.d("address", "经纬度" + latLng.getLongitude() + "," + latLng.getLatitude());
                    LocationModel data = new LocationModel();
                    data.setCity(city);
                    data.setLat(latLng.getLatitude());
                    data.setLng(latLng.getLongitude());
                    LocationDbHelper.getInstance().put(data);
                    Toast.makeText(AddressDetailActivity.this, "保存成功", Toast.LENGTH_SHORT).show();
                    finish();
                }else{
                    Toast.makeText(AddressDetailActivity.this, "保存失败", Toast.LENGTH_SHORT).show();
                    Log.d("address", "请求失败");
                }
            }

            @Override
            public void onPoiItemSearched(PoiItem poiItem, int i) {

            }
        });
        poiSearch.searchPOIAsyn();
    }
}
