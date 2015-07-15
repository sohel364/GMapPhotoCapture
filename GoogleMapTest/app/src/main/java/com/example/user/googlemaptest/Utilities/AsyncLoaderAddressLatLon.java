package com.example.user.googlemaptest.Utilities;

import android.util.Log;

import com.example.user.googlemaptest.model.AddressBase;
import com.example.user.googlemaptest.model.AddressLatLon;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;

/**
 * Created by ASUS on 7/13/2015.
 */
public class AsyncLoaderAddressLatLon extends AsyncLoaderAddress{

    public AsyncLoaderAddressLatLon(String userName) {
        urlString = Utils.SERVICE_URL_ADDRESS_LATLON + "assigned_user="+userName;
    }

    public AsyncLoaderAddressLatLon(String userName, String pinCode) {
        urlString = Utils.SERVICE_URL_ADDRESS_LATLON + "assigned_user="+userName+"&pin=delhi";//+pinCode;
    }

    public AsyncLoaderAddressLatLon(int status) {
        urlString = Utils.SERVICE_URL_ADDRESS_LATLON + "status="+status;
    }

    @Override
    protected void convertJsonToObject(String jsonString) {
        try {
            mAddressList = new ArrayList<AddressBase>();
            Log.e(Utils.TAG_LOG, jsonString);
            JSONObject jsonObject = new JSONObject(jsonString);
            Iterator<?> keys = jsonObject.keys();
            while( keys.hasNext() ) {
                String key = (String)keys.next();
                if ( jsonObject.get(key) instanceof JSONObject ) {
                    JSONObject jObject = (JSONObject)jsonObject.get(key);
                    long id;
                    String name;
                    Double latitude, longitude;
                    id = jObject.getLong("id");
                    name = jObject.getString("name");
                    String lon = jObject.getString("latitude");
                    String  lat= jObject.getString("longitude");
                    //Log.e(Utils.TAG_LOG, lat);
                    try {
                        latitude = new Double(lat);//jObject.getDouble("latitude");
                        longitude = new Double(lon);
                    } catch (Exception e) {
                        latitude = 0.0;
                        longitude = 0.0;
                        Log.e(Utils.TAG_LOG, lat);
                        Log.e(Utils.TAG_LOG, lon);
                        Log.e(Utils.TAG_LOG, e.toString());
                        e.printStackTrace();
                    }

                    //longitude = jObject.getDouble("longitude");
                    Log.e(Utils.TAG_LOG,id+name+latitude+longitude);
                    mAddressList.add(new AddressLatLon(id, name, latitude, longitude));
                }
            }
        } catch (Exception e){
            Log.e(Utils.TAG_LOG, e.toString());
            e.printStackTrace();
        }
    }
}
