package com.example.tthtt.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.tthtt.R;
import com.example.tthtt.utils.StringUtil;
import com.example.tthtt.db.helper.ActiveDbHelper;
import com.example.tthtt.model.ActiveModel;

/**
 * Created by book4 on 2018/4/28.
 */

public class ActiveSetActivity extends Activity {
    //view
    private EditText mEtInstall;
    private EditText mEtOpen;
    private EditText mEtPath;
    private TextView mTvSave;
    //data
    private ActiveModel mData;

    public static void enterActivity(Context context){
        Intent intent = new Intent(context, ActiveSetActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_active_set);
        initView();
        initEvent();
        initData();
    }

    private void initView(){
        mEtInstall = (EditText) findViewById(R.id.et_install);
        mEtOpen = (EditText) findViewById(R.id.et_open);
        mTvSave = (TextView) findViewById(R.id.tv_save);
        mEtPath = (EditText) findViewById(R.id.et_path);
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
        mData = ActiveDbHelper.getInstance().get();
        if(mData == null){
            mData = new ActiveModel();
        }
        showData();
    }

    private void showData(){
        mEtInstall.setText(mData.getInstallRate() + "");
        mEtOpen.setText(mData.getOpenRate() + "");
        mEtPath.setText(mData.getRelativePath());
    }

    private void doSave(){
        String install = mEtInstall.getText().toString();
        String open = mEtOpen.getText().toString();
        String path = mEtPath.getText().toString();
        if(StringUtil.isEmpty(install) || StringUtil.isEmpty(open) || StringUtil.isEmpty(path)){
            Toast.makeText(ActiveSetActivity.this, "请先填写数据", Toast.LENGTH_SHORT).show();
            return;
        }
        mData.setInstallRate(Integer.parseInt(install));
        mData.setOpenRate(Integer.parseInt(open));
        mData.setRelativePath(path);
        mData.setLastDate("");
        ActiveDbHelper.getInstance().put(mData);
        Toast.makeText(ActiveSetActivity.this, "保存成功", Toast.LENGTH_SHORT).show();
        finish();
    }
}
