package com.example.tthtt.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.tthtt.R;
import com.example.tthtt.db.helper.LocationDbHelper;
import com.example.tthtt.model.LocationModel;

import java.util.List;

/**
 * Created by book4 on 2018/3/28.
 */

public class AddressAdapter extends BaseAdapter {
    private Context mContext;
    private LayoutInflater mInflater;
    private List<LocationModel> mDataList;

    public AddressAdapter(Context context, List<LocationModel> dataList){
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
            convertView = mInflater.inflate(R.layout.item_address, parent, false);
            holder.tvSelect = (TextView) convertView.findViewById(R.id.tv_select);
            holder.tvCity = (TextView) convertView.findViewById(R.id.tv_city);
            holder.tvLat = (TextView) convertView.findViewById(R.id.tv_lat);
            holder.tvLng = (TextView) convertView.findViewById(R.id.tv_lng);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        final LocationModel data = mDataList.get(position);
        if(data != null){
            holder.tvCity.setText(data.getCity());
            holder.tvLat.setText(data.getLat() + "");
            holder.tvLng.setText(data.getLng() + "");
            if(data.getIsSelected()){
                holder.tvSelect.setBackgroundColor(mContext.getResources().getColor(R.color.app_green));
            }else{
                holder.tvSelect.setBackgroundColor(mContext.getResources().getColor(R.color.app_gray));
            }
            holder.tvSelect.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    boolean select = data.getIsSelected();
                    data.setIsSelected(!select);
                    notifyDataSetChanged();
                    LocationDbHelper.getInstance().put(data);
                }
            });
        }
        return convertView;
    }

    class ViewHolder{
        TextView tvSelect;
        TextView tvCity;
        TextView tvLng;
        TextView tvLat;
    }
}
