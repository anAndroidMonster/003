package com.example.tthtt.logic.syncPhoneDb.act;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.tthtt.R;
import com.example.tthtt.logic.syncPhoneDb.adapter.AddressAdapter;
import com.example.tthtt.logic.syncPhoneDb.db.LocationDbHelper;
import com.example.tthtt.logic.syncPhoneDb.model.LocationModel;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by book4 on 2018/3/28.
 */

public class AddressListActivity extends Activity {
    //view
    private ListView mLvAddress;
    private AddressAdapter mAdapter;
    private TextView mTvAdd;
    //data
    private List<LocationModel> mDataList = new ArrayList<>();

    public static void enterActivity(Context context){
        Intent intent = new Intent(context, AddressListActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_address_list);
        initView();
        initEvent();
    }

    private void initView(){
        mLvAddress = (ListView) findViewById(R.id.lv_address);
        mAdapter = new AddressAdapter(AddressListActivity.this, mDataList);
        mLvAddress.setAdapter(mAdapter);
        mTvAdd = (TextView) findViewById(R.id.tv_add);
    }

    private void initEvent(){
        mLvAddress.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                LocationModel data = mDataList.get(position);
                if(data != null){
                    AddressDetailActivity.enterActivity(AddressListActivity.this, data);
                }
            }
        });
        mTvAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AddressDetailActivity.enterActivity(AddressListActivity.this);
            }
        });
    }

    private void initData(){
        List<LocationModel> tempList = LocationDbHelper.getInstance().getList();
        if(tempList == null) tempList = new ArrayList<>();
        mDataList.clear();
        mDataList.addAll(tempList);
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void onResume(){
        super.onResume();
        initData();
    }
}
