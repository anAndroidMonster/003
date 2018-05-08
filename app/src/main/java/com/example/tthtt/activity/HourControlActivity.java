package com.example.tthtt.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.tthtt.R;
import com.example.tthtt.adapter.HourControlAdapter;
import com.example.tthtt.db.helper.HourControlDbHelper;
import com.example.tthtt.model.HourControlModel;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by book4 on 2018/4/8.
 */

public class HourControlActivity extends Activity {

    private ListView mLvData;
    private HourControlAdapter mAdapter;
    private TextView mTvSave;
    private List<HourControlModel> mDataList = new ArrayList<>();

    public static void enterActivity(Context context){
        Intent intent = new Intent(context, HourControlActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hour_control);
        initView();
        initEvent();
        initData();
    }

    private void initView(){
        mLvData = (ListView) findViewById(R.id.lv_data);
        mAdapter = new HourControlAdapter(HourControlActivity.this, mDataList);
        mLvData.setAdapter(mAdapter);
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
                    doSave();
                    break;
            }
        }
    };

    private void initData(){
        List<HourControlModel> tempList = HourControlDbHelper.getInstance().getList();
        if(tempList == null) tempList = new ArrayList<>();
        if(tempList.size() != 24){
            tempList.clear();
            for(int i=0; i<24; i++){
                HourControlModel data = new HourControlModel();
                data.setHour(i);
                tempList.add(data);
            }
        }
        mDataList.clear();
        mDataList.addAll(tempList);
        mAdapter.notifyDataSetChanged();
    }

    private void doSave(){
        HourControlDbHelper.getInstance().putList(mDataList);
        Toast.makeText(HourControlActivity.this, "保存成功", Toast.LENGTH_SHORT).show();
    }
}
