package com.example.user.googlemaptest.fragments;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.user.googlemaptest.R;
import com.example.user.googlemaptest.Utilities.Utils;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

/**
 * Created by user on 5/20/2015.
 */
public class FragmentMap extends Fragment{

    private static  View mView;
    private GoogleMap mGoogleMap;

    private LocationManager mLocationManager;
    private Marker mMarker;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        if(mView != null) {
            ViewGroup parent =(ViewGroup) mView.getParent();
            if(parent != null) {
                parent.removeView(mView);
            }
        }
        try {
            mView = inflater.inflate(R.layout.fragment_map, container, false);
        } catch (Exception e) {

        }

        SupportMapFragment mapFragment = ((SupportMapFragment) getActivity().getSupportFragmentManager().findFragmentById(R.id.map) );
        if(mapFragment != null) {
            mGoogleMap = ((SupportMapFragment) getActivity().getSupportFragmentManager().findFragmentById(R.id.map)).getMap();
        } else {
            mGoogleMap = ((SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map)).getMap();
            loadCurrentLocationMap();
            //Toast.makeText(getActivity().getApplicationContext(), "Map fragment cannot be found", Toast.LENGTH_LONG).show();
        }

        return  mView;
    }


    private void loadCurrentLocationMap() {
        LocationManager service = (LocationManager) getActivity().getSystemService(Activity.LOCATION_SERVICE);
        boolean enabledGPS = service
                .isProviderEnabled(LocationManager.GPS_PROVIDER);
        boolean enabledWiFi = service
                .isProviderEnabled(LocationManager.NETWORK_PROVIDER);

        if(!Utils.isInternetConnected(getActivity().getApplicationContext())) {
            Utils.createOKDialog(getActivity(), "Internet Connection!!!", "There is no internet connection available. Please enable Wifi or Data connection for better use of the map.");
            return;
        }
        // Check if enabled and if not send user to the GSP settings
        // Better solution would be to display a dialog and suggesting to
        // go to the settings
        if (!enabledGPS) {
            Toast.makeText(getActivity().getApplicationContext(), "GPS signal not found\nEnabling GPS!!!",
                    Toast.LENGTH_LONG).show();

            try {
                Utils.turnGPSOn(getActivity().getApplicationContext());
                addMarkerToCurrentLocation();
            } catch (Exception e) {
                AlertDialog.Builder alert = new AlertDialog.Builder(getActivity());
                alert.setTitle("Alert!!!");
                alert.setMessage("Your GPS is not enabled. Please enable the GPS settings.");
                alert.setPositiveButton("OK", new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                        startActivity(intent);
                        addMarkerToCurrentLocation();
                    }
                });
                alert.show();
            }

        } else {
            addMarkerToCurrentLocation();
        }

    }

    private void addMarkerToCurrentLocation() {
        mLocationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);

        // Define the criteria how to select the locatioin provider -> use
        // default
        Criteria criteria = new Criteria();
        String provider = mLocationManager.getBestProvider(criteria, false);
        Location location = mLocationManager.getLastKnownLocation(provider);

        LatLng currentLatlong = new LatLng(location.getLatitude(), location.getLongitude());

        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.position(currentLatlong);
        markerOptions.snippet("Current Locations");
        markerOptions.title("Your Location");
        if(mGoogleMap != null) {
            mMarker = mGoogleMap.addMarker(markerOptions);
            mGoogleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(currentLatlong, 15.0F));
        }
    }
}
