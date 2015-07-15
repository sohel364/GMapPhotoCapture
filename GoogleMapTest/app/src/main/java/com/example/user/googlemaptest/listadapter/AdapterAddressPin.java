package com.example.user.googlemaptest.listadapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.user.googlemaptest.R;
import com.example.user.googlemaptest.model.AddressBase;
import com.example.user.googlemaptest.model.AddressPin;

import java.util.List;

/**
 * Created by ASUS on 7/13/2015.
 */
public class AdapterAddressPin extends BaseAdapter{


    private List<AddressBase> mAddressList;
    private LayoutInflater mLayoutInflater;
    private Activity mActivity;

    public AdapterAddressPin(List<AddressBase> addressLatLonList, Activity activity) {
        this.mAddressList = addressLatLonList;
        this.mActivity = activity;
        this.mLayoutInflater = activity.getLayoutInflater();
    }
    @Override
    public int getCount() {
        return mAddressList.size();
    }

    @Override
    public Object getItem(int position) {
        return mAddressList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return mAddressList.get(position).getId();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if(convertView == null) {
            viewHolder = new ViewHolder();
            convertView = mLayoutInflater.inflate(R.layout.list_view_address_pin, parent, false);

            viewHolder.tvId = (TextView)convertView.findViewById(R.id.text_view_address_id);
            viewHolder.tvName = (TextView)convertView.findViewById(R.id.text_view_address_name);
            viewHolder.tvPin = (TextView)convertView.findViewById(R.id.text_view_address_pin);
            viewHolder.tvCity = (TextView)convertView.findViewById(R.id.text_view_address_city);
            viewHolder.position = position;
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        AddressPin addressPin = (AddressPin) mAddressList.get(position);
        viewHolder.tvId.setText(""+addressPin.getId());
        viewHolder.tvName.setText(addressPin.getName());
        viewHolder.tvPin.setText(addressPin.getPinCode());
        viewHolder.tvCity.setText(addressPin.getCityName());

        return convertView;
    }

    class ViewHolder {
        TextView tvId;
        TextView tvName;
        TextView tvPin;
        TextView tvCity;
        int position;
    }
}
