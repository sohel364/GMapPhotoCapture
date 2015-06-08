package com.example.user.googlemaptest.listadapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.user.googlemaptest.R;
import com.example.user.googlemaptest.model.Address;

import java.util.List;

/**
 * Created by user on 5/31/2015.
 */
public class LocationListAdapter extends BaseAdapter{

    private List<Address> mAddressList;
    private LayoutInflater mLayoutInflater;
    private Activity mActivity;

    public LocationListAdapter(List<Address> addressList, Activity activity) {
        this.mAddressList = addressList;
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
        return ((Address)mAddressList.get(position)).getId();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View retView;
        if(convertView == null) {
            retView = mLayoutInflater.inflate(R.layout.list_view_location, null);
        } else {
            retView = convertView;
        }
        TextView tvId, tvName, tvLatitude, tvLongitude;

        tvId = (TextView) retView.findViewById(R.id.text_view_location_id);
        tvName = (TextView) retView.findViewById(R.id.text_view_location_name);
        tvLatitude = (TextView) retView.findViewById(R.id.text_view_location_latitude);
        tvLongitude = (TextView) retView.findViewById(R.id.text_view_location_longitude);

        Address address = mAddressList.get(position);
        tvId.setText(address.getId().toString());
        tvName.setText(address.getName().toString());
        tvLatitude.setText(address.getLatitude().toString());
        tvLongitude.setText(address.getLongitude().toString());

        return retView;
    }
}
