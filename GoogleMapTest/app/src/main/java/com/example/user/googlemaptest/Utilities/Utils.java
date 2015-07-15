package com.example.user.googlemaptest.Utilities;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.provider.Settings;
import android.support.v7.app.AlertDialog;

/**
 * Created by user on 5/23/2015.
 */
public class Utils {

    public static final String TAG_LOG = "GoogleMapTest";
    public static final int CAMERA_REQUEST = 111;
    public static final String SERVICE_URL_ADDRESS_LATLON = "http://spinelbd.com/tamal/services/android_photo_capture/lat_long_service_assigned_user.php?";//"http://spacetotest.com/dev_v1.0/lat_long_service_assigned_user.php?";
    public static final String SERVICE_URL_ADDRESS_PIN = "http://spinelbd.com/tamal/services/android_photo_capture/pin_code_service_assigned_user.php?";
    public static final String SHARED_PREFERENCE_NAME = "com.example.user.googlemaptest";
    public static final String SHARED_KEY_CODE = "shared_key_code";
    public static final String SHARED_KEY_USER_ID = "shared_key_user_id";
    public static final String SHARED_KEY_USER_NAME = "shared_key_user_name";
    public static final String SHARED_KEY_USER_ADDRESS = "shared_key_user_address";


    public static void turnGPSOn(Context context)
    {
        Intent intent = new Intent("android.location.GPS_ENABLED_CHANGE");
        intent.putExtra("enabled", true);
        context.sendBroadcast(intent);

        String provider = Settings.Secure.getString(context.getContentResolver(), Settings.Secure.LOCATION_PROVIDERS_ALLOWED);
        if(!provider.contains("gps")){ //if gps is disabled
            final Intent poke = new Intent();
            poke.setClassName("com.android.settings", "com.android.settings.widget.SettingsAppWidgetProvider");
            poke.addCategory(Intent.CATEGORY_ALTERNATIVE);
            poke.setData(Uri.parse("3"));
            context.sendBroadcast(poke);
        }
    }

    // automatic turn off the gps
    public static void turnGPSOff(Context context)
    {
        String provider = Settings.Secure.getString(context.getContentResolver(), Settings.Secure.LOCATION_PROVIDERS_ALLOWED);
        if(provider.contains("gps")){ //if gps is enabled
            final Intent poke = new Intent();
            poke.setClassName("com.android.settings", "com.android.settings.widget.SettingsAppWidgetProvider");
            poke.addCategory(Intent.CATEGORY_ALTERNATIVE);
            poke.setData(Uri.parse("3"));
            context.sendBroadcast(poke);
        }
    }

    /*
     * isOnline - Check if there is a NetworkConnection
     * @return boolean
     */
    public static boolean isInternetConnected(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        if (netInfo != null && netInfo.isConnected()) {
            return true;
        } else {
            return false;
        }
    }

    public static void createOKDialog(Context context, String title, String message) {

        AlertDialog.Builder alert = new AlertDialog.Builder(context);
        alert.setTitle(title);
        alert.setMessage(message);
        alert.setPositiveButton("OK", new DialogInterface.OnClickListener() {

            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        alert.show();
    }

    public static Bitmap getResizedBitmap(Bitmap bm, int newHeight, int newWidth) {
        int width = bm.getWidth();
        int height = bm.getHeight();
        float scaleWidth = ((float) newWidth) / width;
        float scaleHeight = ((float) newHeight) / height;
        Matrix matrix = new Matrix();
        matrix.postScale(scaleWidth, scaleHeight);
        Bitmap resizedBitmap = Bitmap.createBitmap(bm, 0, 0, width, height, matrix, false);
        return resizedBitmap;
    }

}
