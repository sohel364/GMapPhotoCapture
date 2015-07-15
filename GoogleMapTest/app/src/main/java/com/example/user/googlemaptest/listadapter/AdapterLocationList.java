package com.example.user.googlemaptest.listadapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.user.googlemaptest.R;
import com.example.user.googlemaptest.model.AddressBase;
import com.example.user.googlemaptest.model.AddressLatLon;

import java.util.List;

/**
 * Created by user on 5/31/2015.
 */
public class AdapterLocationList extends BaseAdapter{

    private List<AddressBase> mAddressLatLonList;
    private LayoutInflater mLayoutInflater;
    private Activity mActivity;

    public AdapterLocationList(List<AddressBase> addressLatLonList, Activity activity) {
        this.mAddressLatLonList = addressLatLonList;
        this.mActivity = activity;
        this.mLayoutInflater = activity.getLayoutInflater();
    }

    @Override
    public int getCount() {
        return mAddressLatLonList.size();
    }

    @Override
    public Object getItem(int position) {
        return mAddressLatLonList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return ((AddressBase) mAddressLatLonList.get(position)).getId();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if(convertView == null) {
            convertView = mLayoutInflater.inflate(R.layout.list_view_location, parent, false);
            viewHolder = new ViewHolder();
            viewHolder.tvId = (TextView) convertView.findViewById(R.id.text_view_location_id);
            viewHolder.tvName = (TextView) convertView.findViewById(R.id.text_view_location_name);
            viewHolder.tvLatitude = (TextView) convertView.findViewById(R.id.text_view_location_latitude);
            viewHolder.tvLongitude = (TextView) convertView.findViewById(R.id.text_view_location_longitude);
            viewHolder.position = position;
            convertView.setTag(viewHolder);

        } else {
            viewHolder = (ViewHolder)convertView.getTag();
        }


        AddressLatLon addressLatLon = (AddressLatLon) mAddressLatLonList.get(position);
        viewHolder.tvId.setText(addressLatLon.getId().toString());
        viewHolder.tvName.setText(addressLatLon.getName().toString());
        viewHolder.tvLatitude.setText(addressLatLon.getLatitude().toString());
        viewHolder.tvLongitude.setText(addressLatLon.getLongitude().toString());

        return convertView;
    }

    class ViewHolder {
        TextView tvId;
        TextView tvName;
        TextView tvLatitude;
        TextView tvLongitude;
        int position;
    }
}
