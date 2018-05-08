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
import com.example.tthtt.adapter.RepeatControlAdapter;
import com.example.tthtt.db.helper.RepeatControlDbHelper;
import com.example.tthtt.model.RepeatRateModel;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by book4 on 2018/4/8.
 */

public class RepeatControlActivity extends Activity {

    private ListView mLvData;
    private RepeatControlAdapter mAdapter;
    private TextView mTvSave;
    private List<RepeatRateModel> mDataList = new ArrayList<>();

    public static void enterActivity(Context context){
        Intent intent = new Intent(context, RepeatControlActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_repeat_control);
        initView();
        initEvent();
        initData();
    }

    private void initView(){
        mLvData = (ListView) findViewById(R.id.lv_data);
        mAdapter = new RepeatControlAdapter(RepeatControlActivity.this, mDataList);
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
        List<RepeatRateModel> tempList = RepeatControlDbHelper.getInstance().getList();
        if(tempList == null) tempList = new ArrayList<>();
        if(tempList.size() < 5){
            tempList.clear();
            RepeatRateModel data = new RepeatRateModel();
            data.setType(-1);
            tempList.add(data);

            data = new RepeatRateModel();
            data.setType(0);
            tempList.add(data);

            data = new RepeatRateModel();
            data.setType(1);
            tempList.add(data);

            data = new RepeatRateModel();
            data.setType(7);
            tempList.add(data);

            data = new RepeatRateModel();
            data.setType(30);
            tempList.add(data);
        }
        mDataList.clear();
        mDataList.addAll(tempList);
        mAdapter.notifyDataSetChanged();
    }

    private void doSave(){
        int total = 0;
        for(RepeatRateModel data: mDataList){
            total += data.getRate();
        }
        if(total != 100){
            Toast.makeText(RepeatControlActivity.this, "总和不等于100", Toast.LENGTH_SHORT).show();
            return;
        }
        RepeatControlDbHelper.getInstance().putList(mDataList);
        Toast.makeText(RepeatControlActivity.this, "保存成功", Toast.LENGTH_SHORT).show();
    }
}
