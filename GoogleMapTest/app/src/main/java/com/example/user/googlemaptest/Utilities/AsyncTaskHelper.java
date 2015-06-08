package com.example.user.googlemaptest.Utilities;

import android.annotation.TargetApi;
import android.os.AsyncTask;
import android.os.Build;
import android.util.Log;

import com.example.user.googlemaptest.fragments.BaseFragment;
import com.example.user.googlemaptest.model.Address;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;


@TargetApi(Build.VERSION_CODES.HONEYCOMB)
/**
 * Created by user on 5/24/2015.
 */
public class AsyncTaskHelper extends AsyncTask<BaseFragment, String, String> {

    private BaseFragment mFragmentMap;
    private List<Address> mAddressList;
    private String urlString;


    public AsyncTaskHelper(Double latMin, Double latMax, Double longMin, Double longMax, Integer status) {
        urlString = "http://inzaana.com/WebBuilder/lon_lat_service.php?long_min=" + longMin + "&long_max="
                + longMax + "&lat_min=" + latMin + "&lat_max=" + latMax + "&status=" + status;
        Log.e(Utils.TAG_LOG,urlString);
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected String doInBackground(BaseFragment... params) {
        mFragmentMap = (BaseFragment) params[0];
        try {
            HttpResponseData();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected void onPostExecute(String s) {
        //super.onPostExecute(s);
        mFragmentMap.executeAsyncTaskCallBack(mAddressList);
    }

    @Override
    protected void onCancelled() {
        super.onCancelled();
    }

    public void HttpResponseData() throws IOException {
        try {
            //"http://inzaana.com/WebBuilder/lon_lat_service.php?long_min=77.10&long_max=77.15&lat_min=28.40&lat_max=28.45"
            URL url = new URL(urlString);
            HttpURLConnection con = (HttpURLConnection) url
                    .openConnection();
            readStream(con.getInputStream());
        } catch (Exception e) {
            Log.e(Utils.TAG_LOG, e.toString());
            e.printStackTrace();
        }

        //  return lstString;
    }

    private void readStream(InputStream in) {
        Log.d(Utils.TAG_LOG, "Starting The data loading operation");
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new InputStreamReader(in, "UTF-8"), 8);
            String line = "";
            while ((line = reader.readLine()) != null) {
                //Log.e(Utils.TAG_LOG, line);
                if (line != null && line.trim().length() > 3) {
                    Log.e(Utils.TAG_LOG, line);
                    convertJsonToObject(line);
                }
            }
        } catch (IOException e) {
            Log.e(Utils.TAG_LOG, e.toString());
            e.printStackTrace();
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    Log.e(Utils.TAG_LOG, e.toString());
                    e.printStackTrace();
                }
            }
        }
    }

    private void convertJsonToObject(String jsonString) {
        try {
            mAddressList = new ArrayList<Address>();
            //Log.e(Utils.TAG_LOG, jsonString);
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
                    String lat = jObject.getString("latitude");
                    String longi = jObject.getString("longitude");
                    //Log.e(Utils.TAG_LOG, lat);
                    try {
                        latitude = new Double(lat);//jObject.getDouble("latitude");
                        longitude = new Double(longi);
                    } catch (Exception e) {
                        latitude = 0.0;
                        longitude = 0.0;
                        Log.e(Utils.TAG_LOG, lat);
                        Log.e(Utils.TAG_LOG, longi);
                        Log.e(Utils.TAG_LOG, e.toString());
                        e.printStackTrace();
                    }

                    //longitude = jObject.getDouble("longitude");
                    //Log.e(Utils.TAG_LOG,id+name+latitude+longitude);
                    mAddressList.add(new Address(id, name, latitude, longitude));
                }
            }
        } catch (Exception e){
            Log.e(Utils.TAG_LOG, e.toString());
            e.printStackTrace();
        }
    }

    /*
    private void convertJsonToObject(String jsonString) {
        try {
            Log.e(Utils.TAG_LOG, jsonString);   
            JSONObject jsonssObject = new JSONObject(jsonString);
            //Log.e(Utils.TAG_LOG, "json Object"+jsonssObject.toString());

            List<Address> addresses = new ArrayList<Address>();
            JSONArray jsonArray = new JSONArray(jsonString);
            for(int i = 0; i<jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                long id;
                String name;
                Double latitude, longitude;
                id = jsonObject.getLong("id");
                name = jsonObject.getString("name");
                latitude = jsonObject.getDouble("latitude");
                longitude = jsonObject.getDouble("longitude");
                Log.e(Utils.TAG_LOG,id+name+latitude+longitude);
                addresses.add(new Address(id, name, latitude,longitude));
            }
            Log.e(Utils.TAG_LOG, jsonArray.toString());

        } catch (Exception e){
            Log.e(Utils.TAG_LOG, e.toString());
            e.printStackTrace();
        }
    }


    private List<Address> readJsonStream(InputStream in) throws IOException {
        JsonReader reader = new JsonReader(new InputStreamReader(in, "UTF-8"));
        List<Address> addresses = null;

        try {
            Log.e(Utils.TAG_LOG, "Reader Object: \n"+reader.toString());
            addresses = readMessagesArray(reader);
        } catch (Exception e) {
            Log.e(Utils.TAG_LOG, "readJsonStream: "+e.toString());
            e.printStackTrace();
        }
        finally {
            reader.close();
        }

        //Log.e(Utils.TAG_LOG, "Address List Size: "+addresses.size());
        return addresses;
    }

    private List<Address> readMessagesArray(JsonReader reader) throws IOException {
        List<Address> addresses = new ArrayList();

        reader.beginArray();
        while (reader.hasNext()) {
            if(reader != null) {
                Address address = readMessage(reader);
                if(address != null) {
                    addresses.add(address);
                }
            }
        }
        reader.endArray();
        return addresses;
    }

    private Address readMessage(JsonReader reader) throws IOException {
        long id = -1;
        String name = null;
        Double latitude = null;
        Double longitude = null;
        if(reader != null) {
            try {
                reader.beginObject();
            }catch (Exception e) {
                Log.e(Utils.TAG_LOG, "readerBegin is null");
                return null;
            }

            while (reader.hasNext()) {
                String jsonName = reader.nextName();
                if (jsonName.equals("id")) {
                    id = reader.nextLong();
                } else if (jsonName.equals("name")) {
                    name = reader.nextString();
                } else if (jsonName.equals("latitude") && reader.peek() != JsonToken.NULL) {
                    latitude = reader.nextDouble();
                } else if (jsonName.equals("longitude")) {
                    longitude = reader.nextDouble();
                } else {
                    reader.skipValue();
                }
            }
            reader.endObject();
            return new Address(id, name, latitude, longitude);
        }
        return null;
    }
    */

}
