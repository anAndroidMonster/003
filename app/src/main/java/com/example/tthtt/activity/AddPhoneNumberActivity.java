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
import com.example.tthtt.db.helper.PhoneNumberDbHelper;
import com.example.tthtt.model.PhoneNumberModel;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by book4 on 2018/3/28.
 */

public class AddPhoneNumberActivity extends Activity {
    //view
    private EditText mEtCity;
    private EditText mEtCompany;
    private EditText mEtPhoneNumber;
    private TextView mTvSave;
    //data
    private static String Tag = "AddPhoneNumber";
    private String mCity;

    public static void enterActivity(Context context, String city){
        Intent intent = new Intent(context, AddPhoneNumberActivity.class);
        intent.putExtra("city", city);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_phone_number);
        getInParam();
        initView();
        initEvent();
        initData();
    }

    private void getInParam(){
        mCity = getIntent().getStringExtra("city");
    }

    private void initView(){
        mEtCity = (EditText) findViewById(R.id.et_city);
        mEtCompany = (EditText) findViewById(R.id.et_company);
        mEtPhoneNumber = (EditText) findViewById(R.id.et_phone_number);
        mTvSave = (TextView) findViewById(R.id.tv_save);
    }

    private void initEvent(){
        mTvSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                doSave();
            }
        });
    }

    private void initData(){
        if(mCity != null){
            mEtCity.setText(mCity);
        }
    }

    private void doSave(){
        String city = mEtCity.getText().toString();
        String company = mEtCompany.getText().toString();
        int companyInt = -1;
        try {
            companyInt = Integer.parseInt(company);
        }catch (NumberFormatException ex){
            ex.printStackTrace();
        }
        String phone = mEtPhoneNumber.getText().toString();
        if(StringUtil.isEmpty(city) || StringUtil.isEmpty(company) || StringUtil.isEmpty(phone) || companyInt <0){
            Toast.makeText(AddPhoneNumberActivity.this, "请输入正确", Toast.LENGTH_SHORT).show();
            return;
        }
        List<PhoneNumberModel> dataList = new ArrayList<>();
        while (phone.length() > 0){
            PhoneNumberModel data = new PhoneNumberModel();
            data.setCity(city);
            data.setCompany(companyInt);
            String onePhone = phone.substring(0, 7);
            data.setPhoneNumber(onePhone);
            phone = phone.substring(7);
            dataList.add(data);
        }
        PhoneNumberDbHelper.getInstance().put(dataList);
        Toast.makeText(AddPhoneNumberActivity.this, "保存成功", Toast.LENGTH_SHORT).show();
        finish();
    }
}
