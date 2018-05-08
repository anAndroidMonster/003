package com.example.tthtt.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.tthtt.R;
import com.example.tthtt.model.VpnModel;

import java.util.List;

/**
 * Created by book4 on 2018/3/28.
 */

public class VpnAdapter extends BaseAdapter {
    private Context mContext;
    private LayoutInflater mInflater;
    private List<VpnModel> mDataList;

    public VpnAdapter(Context context, List<VpnModel> dataList){
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
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = mInflater.inflate(R.layout.item_vpn, parent, false);
            holder.tvAddress = (TextView) convertView.findViewById(R.id.tv_server);
            holder.tvCity = (TextView) convertView.findViewById(R.id.tv_city);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        final VpnModel data = mDataList.get(position);
        if(data != null){
            holder.tvCity.setText(data.getCity());
            holder.tvAddress.setText(data.getServer());
        }
        return convertView;
    }

    class ViewHolder{
        TextView tvAddress;
        TextView tvCity;
    }
}
