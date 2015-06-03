package com.example.user.googlemaptest.fragments;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.MediaStore;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.user.googlemaptest.R;
import com.example.user.googlemaptest.Utilities.AsyncTaskHelper;
import com.example.user.googlemaptest.Utilities.Utils;
import com.example.user.googlemaptest.model.Address;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.Projection;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.VisibleRegion;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * Created by user on 5/20/2015.
 */
public class FragmentMap extends BaseFragment{

    private static  View mView;
    private GoogleMap mGoogleMap;

    private LocationManager mLocationManager;
    private Marker mMarker;

    private HashMap<Marker, Long> mMarkerHash;

    private static String sTagName;

    private double latMax;
    private double latMin;
    private double longMax;
    private double longMin;

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

        initView();
        initListeners();

        return  mView;
    }



    private void initView() {
        sTagName = getActivity().getString(R.string.app_name);
        SupportMapFragment mapFragment = ((SupportMapFragment) getActivity().getSupportFragmentManager().findFragmentById(R.id.map) );
        if(mapFragment != null) {
            mGoogleMap = ((SupportMapFragment) getActivity().getSupportFragmentManager().findFragmentById(R.id.map)).getMap();
        } else {
            mGoogleMap = ((SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map)).getMap();
            mGoogleMap.clear();
            mMarkerHash = new HashMap<Marker, Long>();
            loadCurrentLocationMap();
            //Toast.makeText(getActivity().getApplicationContext(), "Map fragment cannot be found", Toast.LENGTH_LONG).show();
        }
    }

    private void initListeners() {
        mGoogleMap.setOnCameraChangeListener(new GoogleMap.OnCameraChangeListener() {
            @Override
            public void onCameraChange(CameraPosition cameraPosition) {
                //mGoogleMap.clear();
                Float maxZoomLevel = mGoogleMap.getMaxZoomLevel();
                Float zoomLevel = mGoogleMap.getCameraPosition().zoom;
                Log.e(sTagName, "Max Zoom Level: " + maxZoomLevel);
                Log.e(sTagName, "Current Zoom Level: " + zoomLevel);
                if (zoomLevel > maxZoomLevel - 7.1) {
                    getLatitudeLongitudeFourCorners();

                    AsyncTaskHelper asyncTaskHelper = new AsyncTaskHelper(latMin,latMax,longMin,longMax);
                    asyncTaskHelper.execute(FragmentMap.this, null, null);

                }
            }
        });
        mGoogleMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                Toast.makeText(getActivity().getApplicationContext(), "Marker Clicked", Toast.LENGTH_LONG).show();
                return false;
            }
        });

        mGoogleMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
            @Override
            public void onInfoWindowClick(Marker marker) {
                Long id = mMarkerHash.get(marker);
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(intent, Utils.CAMERA_REQUEST);
            }
        });
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == Utils.CAMERA_REQUEST && resultCode == Activity.RESULT_OK) {
            Bitmap photo = (Bitmap) data.getExtras().get("data");
            Toast.makeText(getActivity().getApplicationContext(),"Photo Found", Toast.LENGTH_LONG).show();
            //((ImageView)inflatedView.findViewById(R.id.image)).setImageBitmap(photo);
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void clearUnnecessaryMarkers() {
        Iterator it = mMarkerHash.entrySet().iterator();
        while(it.hasNext()) {
            Map.Entry pair = (Map.Entry)it.next();
            Marker marker = (Marker) pair.getKey();
            double lat = marker.getPosition().latitude;
            double lon = marker.getPosition().longitude;
            if(lat<latMin || lat>latMax || lon<longMin || lon>longMax) {
                marker.remove();
                it.remove();
            }
        }
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

        if(location == null) {
            return;
        }
        LatLng currentLatlong = new LatLng(location.getLatitude(), location.getLongitude());

        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.position(currentLatlong);
        markerOptions.snippet("Current Locations");
        markerOptions.title("Your Location");
        if(mGoogleMap != null) {
            mMarker = mGoogleMap.addMarker(markerOptions);
            //mGoogleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(currentLatlong, 15.0F));

            mGoogleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(currentLatlong, 15.0F), new GoogleMap.CancelableCallback() {
                @Override
                public void onFinish() {
                    getLatitudeLongitudeFourCorners();
                }

                @Override
                public void onCancel() {

                }
            });
            //getLatitudeLongitudeFourCorners();
        }
    }

    private void getLatitudeLongitudeFourCorners (){
        VisibleRegion vr = mGoogleMap.getProjection().getVisibleRegion();
         //= vr.nearLeft.latitude - vr.farRight.latitude > 0 ? vr.nearLeft.latitude : vr.farRight.latitude;
        if(vr.nearLeft.latitude - vr.farRight.latitude > 0) {
            latMax = vr.nearLeft.latitude;
            latMin = vr.farRight.latitude;
        } else {
            latMax = vr.farRight.latitude;
            latMin = vr.nearLeft.latitude;
        }
        if(vr.nearLeft.longitude - vr.farRight.longitude > 0) {
            longMax = vr.nearLeft.longitude;
            longMin = vr.farRight.longitude;
        } else {
            longMax = vr.farRight.longitude;
            longMin = vr.nearLeft.longitude;
        }
        Log.e(sTagName, "Max Latitude: "+latMax);
        Log.e(sTagName, "Min Latitude: "+latMin);
        Log.e(sTagName, "Max Longitude: "+longMax);
        Log.e(sTagName, "Min Longitude: "+longMin);
    }

    private MarkerOptions createMarkerOptionsByLatLng(LatLng latLng, String title, String snippet) {

        Log.e(sTagName, title+": "+snippet+", "+latLng.latitude+", "+latLng.longitude);

        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.position(latLng);
        markerOptions.title(title);
        markerOptions.snippet(snippet);
        return markerOptions;
    }

    public void loadAddressMarkers(List<Address> addressList) {
        if(addressList == null || addressList.size() <= 0) {
            return;
        }
        clearUnnecessaryMarkers();
        int limit = addressList.size();
        for(int i = 0; i<limit; i++) {
            Address address = addressList.get(i);
            MarkerOptions markerOptions = createMarkerOptionsByLatLng(new LatLng(address.getLatitude(),address.getLongitude()),
                    address.getName(), "Tap Here to Take Picture");
            Marker marker = mGoogleMap.addMarker(markerOptions);

            mMarkerHash.put(marker, address.getId());
        }
    }

    @Override
    public void executeAsyncTaskCallBack(List<Address> addressList) {
        loadAddressMarkers(addressList);
    }
}
