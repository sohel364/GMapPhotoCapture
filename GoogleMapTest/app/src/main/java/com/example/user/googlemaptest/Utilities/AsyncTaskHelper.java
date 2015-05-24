package com.example.user.googlemaptest.Utilities;
import android.annotation.TargetApi;
import android.os.AsyncTask;
import android.os.Build;
import android.util.JsonReader;
import android.util.JsonToken;
import android.util.Log;

import com.example.user.googlemaptest.fragments.FragmentMap;
import com.example.user.googlemaptest.model.Address;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;


@TargetApi(Build.VERSION_CODES.HONEYCOMB)
/**
 * Created by user on 5/24/2015.
 */
public class AsyncTaskHelper extends AsyncTask<FragmentMap, String,String>{

    private FragmentMap mFragmentMap;
    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected String doInBackground(FragmentMap... params) {
        mFragmentMap = (FragmentMap) params[0];
        try {
            HttpResponseData();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
    }



    public void HttpResponseData() throws IOException {

        try {
            /*
                @Tamal We have to pass the params in the url it's static right now.
             */
            URL url = new URL("http://inzaana.com/WebBuilder/lon_lat_service.php?long_min=28.40&long_max=28.60&lat_min=77.10&lat_max=77.30");
            HttpURLConnection con = (HttpURLConnection) url
                    .openConnection();
            //readStream(con.getInputStream());

            readJsonStream(con.getInputStream());
        } catch (Exception e) {
            Log.e(Utils.TAG_LOG, e.toString());
            e.printStackTrace();
        }

        //  return lstString;
    }

    private  List<String> readStream(InputStream in) {
        Log.d(Utils.TAG_LOG, "Starting The data loading operation");
        BufferedReader reader = null;
        List<String> lstString = new ArrayList<String>();
        try {
            reader = new BufferedReader(new InputStreamReader(in,  "UTF-8"), 8);
            String line = "";

            readJsonStream(in);

            while ((line = reader.readLine()) != null) {
                //System.out.println(line);
                Log.e(Utils.TAG_LOG, line);
                lstString.add(line);
                convertJsonToObject(line);
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
        return lstString;
    }

    private void convertJsonToObject(String jsonString) {
        try {
            JSONObject jsonObject = new JSONObject(jsonString);
            Log.e(Utils.TAG_LOG, jsonObject.toString());
            JSONArray jsonArray = new JSONArray(jsonString);
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

        Log.e(Utils.TAG_LOG, "Address List Size: "+addresses.size());
        return addresses;
    }

    private List<Address> readMessagesArray(JsonReader reader) throws IOException {
        List<Address> addresses = new ArrayList();

        reader.beginArray();
        while (reader.hasNext()) {
            addresses.add(readMessage(reader));
        }
        reader.endArray();
        return addresses;
    }

    private Address readMessage(JsonReader reader) throws IOException {
        long id = -1;
        String name = null;
        Double latitude = null;
        Double longitude = null;

        reader.beginObject();
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

}
