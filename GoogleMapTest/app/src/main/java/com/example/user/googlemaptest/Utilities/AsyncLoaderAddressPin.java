package com.example.user.googlemaptest.Utilities;

import android.util.Log;

import com.example.user.googlemaptest.model.AddressBase;
import com.example.user.googlemaptest.model.AddressPin;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;

/**
 * Created by ASUS on 7/13/2015.
 */
public class AsyncLoaderAddressPin extends AsyncLoaderAddress {

    public AsyncLoaderAddressPin(String userName) {
        urlString = Utils.SERVICE_URL_ADDRESS_PIN + "assigned_user="+userName;
    }

    public AsyncLoaderAddressPin(int status) {
        urlString = Utils.SERVICE_URL_ADDRESS_PIN + "status="+status;
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
                    String name, pin, city;
                    Double latitude, longitude;

                    //Log.e(Utils.TAG_LOG, lat);
                    try {
                        id = jObject.getLong("id");
                        name = jObject.getString("name");
                        pin = jObject.getString("pincode");//pin
                        city= jObject.getString("address");//city
                    } catch (Exception e) {
                        id = -1;
                        name = "NOT FOUND";
                        pin = "NOT FOUND";
                        city= "NOT FOUND";
                        /*Log.e(Utils.TAG_LOG, lat);
                        Log.e(Utils.TAG_LOG, lon);*/
                        Log.e(Utils.TAG_LOG, e.toString());
                        e.printStackTrace();
                    }

                    //longitude = jObject.getDouble("longitude");
                    Log.e(Utils.TAG_LOG,id+name+pin+city);
                    mAddressList.add(new AddressPin(id, name, pin, removeExtraSpace(city.trim())));
                }
            }
        } catch (Exception e){
            Log.e(Utils.TAG_LOG, e.toString());
            e.printStackTrace();
        }
    }

    private String removeExtraSpace(String str) {
        String retStr = "";
        boolean prevSpace = false;
        for(int i = 0; i<str.length(); i++) {
            char ch = str.charAt(i);
            if(ch == ' ' || ch == '\t' || ch == '\n') {
                if(!prevSpace) {
                    if(ch != '\t') {
                        retStr += ch;
                    } else {
                        retStr += " ";
                    }
                    prevSpace = true;
                }
            } else {
                retStr += ch;
                prevSpace = false;
            }
        }
        return retStr;
    }
}
