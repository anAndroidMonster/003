package com.example.tthtt.logic.syncPhoneDb.adapter;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.TextView;

import com.example.tthtt.R;
import com.example.tthtt.logic.syncPhoneDb.db.RepeatRateModel;
import com.example.tthtt.logic.syncPhoneDb.model.HourControlModel;

import java.util.List;

/**
 * Created by book4 on 2018/3/28.
 */

public class RepeatControlAdapter extends BaseAdapter {
    private Context mContext;
    private LayoutInflater mInflater;
    private List<RepeatRateModel> mDataList;

    public RepeatControlAdapter(Context context, List<RepeatRateModel> dataList){
        mContext = context;
        mInflater = LayoutInflater.from(mContext);
        mDataList = dataList;
    }

    @Override
    public int getCount() {
        return mDataList == null? 0: mDataList.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final ViewHolder holder;
        holder = new ViewHolder();
        convertView = mInflater.inflate(R.layout.item_repeat_control, parent, false);
        holder.tvType = (TextView) convertView.findViewById(R.id.tv_type);
        holder.etRate = (EditText) convertView.findViewById(R.id.et_rate);
        final RepeatRateModel data = mDataList.get(position);
        if(data != null){
            holder.tvType.setText(data.getType() + "");
            if(data.getRate() >= 0 && data.getRate() <= 100) {
                holder.etRate.setText(data.getRate() + "");
            }else{
                holder.etRate.setText("");
            }
        }
        holder.etRate.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if(data == null) return;
                try{
                    int rate = Integer.parseInt(s.toString());
                    data.setRate(rate);
                }catch (NumberFormatException ex){
                    ex.printStackTrace();
                }
            }
        });
        return convertView;
    }

    class ViewHolder{
        TextView tvType;
        EditText etRate;
    }
}
