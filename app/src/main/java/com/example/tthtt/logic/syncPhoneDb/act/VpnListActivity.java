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
import com.example.tthtt.logic.syncPhoneDb.adapter.VpnAdapter;
import com.example.tthtt.logic.syncPhoneDb.db.LocationDbHelper;
import com.example.tthtt.logic.syncPhoneDb.db.VpnDbHelper;
import com.example.tthtt.logic.syncPhoneDb.model.LocationModel;
import com.example.tthtt.logic.syncPhoneDb.model.VpnModel;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by book4 on 2018/3/28.
 */

public class VpnListActivity extends Activity {
    //view
    private ListView mLvAddress;
    private VpnAdapter mAdapter;
    private TextView mTvAdd;
    private TextView mTvCount;
    //data
    private List<VpnModel> mDataList = new ArrayList<>();

    public static void enterActivity(Context context){
        Intent intent = new Intent(context, VpnListActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vpn_list);
        initView();
        initEvent();
    }

    private void initView(){
        mLvAddress = (ListView) findViewById(R.id.lv_address);
        mAdapter = new VpnAdapter(VpnListActivity.this, mDataList);
        mLvAddress.setAdapter(mAdapter);
        mTvAdd = (TextView) findViewById(R.id.tv_add);
        mTvCount = (TextView) findViewById(R.id.tv_count);
    }

    private void initEvent(){
        mLvAddress.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                VpnModel data = mDataList.get(position);
                if(data != null){
                    VpnActivity.enterActivity(VpnListActivity.this, data);
                }
            }
        });
        mTvAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                VpnActivity.enterActivity(VpnListActivity.this);
            }
        });
    }

    private void initData(){
        List<VpnModel> tempList = VpnDbHelper.getInstance().getList();
        if(tempList == null) tempList = new ArrayList<>();
        mDataList.clear();
        mDataList.addAll(tempList);
        mAdapter.notifyDataSetChanged();
        mTvCount.setText(mDataList.size() + "");
    }

    @Override
    public void onResume(){
        super.onResume();
        initData();
    }
}
