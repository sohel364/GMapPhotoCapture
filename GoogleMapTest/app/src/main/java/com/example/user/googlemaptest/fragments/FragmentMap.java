package com.example.user.googlemaptest.fragments;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.MediaStore;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.user.googlemaptest.R;
import com.example.user.googlemaptest.Utilities.AsyncLoaderAddress;
import com.example.user.googlemaptest.Utilities.AsyncLoaderAddressLatLon;
import com.example.user.googlemaptest.Utilities.Utils;
import com.example.user.googlemaptest.activities.cameraTest;
import com.example.user.googlemaptest.model.AddressBase;
import com.example.user.googlemaptest.model.AddressLatLon;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.VisibleRegion;

import java.util.HashMap;
import java.util.List;

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

    /*private Spinner mSpinnerCity;
    private EditText mEditPostCode;
    private Button mBtnSearch;*/

    private String userName;
    private String mPinCode;

    Long id;


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

        Bundle bundle = this.getArguments();
        if(bundle != null) {
            mPinCode = bundle.getString(BUNDLE_KEY_PIN_CODE);
        } else {
            mPinCode = "";
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

        /*mSpinnerCity = (Spinner)mView.findViewById(R.id.spinner_city);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getActivity(), R.array.city_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mSpinnerCity.setAdapter(adapter);

        mEditPostCode = (EditText) mView.findViewById(R.id.text_post_code);

        mBtnSearch = (Button) mView.findViewById(R.id.button_search);*/

        SharedPreferences sharedPreferences = getActivity().getSharedPreferences(Utils.SHARED_PREFERENCE_NAME, Context.MODE_PRIVATE);
        userName = sharedPreferences.getString(Utils.SHARED_KEY_USER_ID, "");

        AsyncLoaderAddress asyncTaskHelper;// = new AsyncLoaderAddressLatLon(userName);
        if(mPinCode != null && mPinCode.length() > 0) {
            asyncTaskHelper = new AsyncLoaderAddressLatLon(userName, mPinCode);
        } else {
            asyncTaskHelper = new AsyncLoaderAddressLatLon(userName);
        }
        asyncTaskHelper.execute(FragmentMap.this, null, null);

    }

    private void initListeners() {
        /*mGoogleMap.setOnCameraChangeListener(new GoogleMap.OnCameraChangeListener() {
            @Override
            public void onCameraChange(CameraPosition cameraPosition) {
                //mGoogleMap.clear();
                Float maxZoomLevel = mGoogleMap.getMaxZoomLevel();
                Float zoomLevel = mGoogleMap.getCameraPosition().zoom;
                Log.e(sTagName, "Max Zoom Level: " + maxZoomLevel);
                Log.e(sTagName, "Current Zoom Level: " + zoomLevel);
                if (zoomLevel > maxZoomLevel - 7.1) {
                    getLatitudeLongitudeFourCorners();

                    AsyncLoaderAddress asyncTaskHelper = new AsyncLoaderAddress(latMin, latMax, longMin, longMax, 2);
                    asyncTaskHelper.execute(FragmentMap.this, null, null);

                }
            }
        });*/
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
                id = mMarkerHash.get(marker);
                // camera start here
                //startActivityForResult(intent, Utils.CAMERA_REQUEST);
                Toast.makeText(getActivity().getApplicationContext(), "Clicked for camera", Toast.LENGTH_LONG).show();
                try {
                    Intent _intent = new Intent(getActivity().getApplicationContext(), cameraTest.class);
                    startActivity(_intent);
                } catch (Exception ex) {
                    Toast.makeText(getActivity().getApplicationContext(), ex.toString(), Toast.LENGTH_LONG).show();
                    ex.printStackTrace();
                }

            }
        });

        /*mBtnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                searchLocations();
            }
        });*/
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

    /*private void clearUnnecessaryMarkers() {
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
    }*/

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
        LatLng currentLatLong = new LatLng(location.getLatitude(), location.getLongitude());

        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.position(currentLatLong);
        markerOptions.snippet("Current Locations");
        markerOptions.title("Your Location");
        if(mGoogleMap != null) {
            mMarker = mGoogleMap.addMarker(markerOptions);
            //mGoogleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(currentLatLong, 15.0F));

            moveCameraToLocation(currentLatLong);
            //getLatitudeLongitudeFourCorners();
        }
    }

    private void moveCameraToLocation(LatLng latLng) {
        if(mGoogleMap != null) {
            mGoogleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 10.0F), new GoogleMap.CancelableCallback() {
                @Override
                public void onFinish() {
                    getLatitudeLongitudeFourCorners();
                }

                @Override
                public void onCancel() {

                }
            });
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
        Log.e(sTagName, "Min Latitude: " + latMin);
        Log.e(sTagName, "Max Longitude: " + longMax);
        Log.e(sTagName, "Min Longitude: " + longMin);
    }

    private MarkerOptions createMarkerOptionsByLatLng(LatLng latLng, String title, String snippet) {

        Log.e(sTagName, title+": "+snippet+", "+latLng.latitude+", "+latLng.longitude);

        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.position(latLng);
        markerOptions.title(title);
        markerOptions.snippet(snippet);
        return markerOptions;
    }

    public void loadAddressMarkers(List<AddressBase> addressLatLonList) {
        if(addressLatLonList == null || addressLatLonList.size() <= 0) {
            return;
        }
        /*clearUnnecessaryMarkers();*/
        int limit = addressLatLonList.size();
        for(int i = 0; i<limit; i++) {
            AddressLatLon addressLatLon = (AddressLatLon) addressLatLonList.get(i);
            MarkerOptions markerOptions = createMarkerOptionsByLatLng(new LatLng(addressLatLon.getLatitude(), addressLatLon.getLongitude()),
                    addressLatLon.getName(), "Tap Here to Take Picture");
            Marker marker = mGoogleMap.addMarker(markerOptions);

            mMarkerHash.put(marker, addressLatLon.getId());
        }

        Toast.makeText(getActivity(), "Map markers are being loaded!!!", Toast.LENGTH_LONG).show();
    }

    /*private void searchLocations() {
        int position = mSpinnerCity.getSelectedItemPosition();
        String city = mSpinnerCity.getSelectedItem().toString();
        String code = mEditPostCode.getText().toString();

        //if(!Utils.isInternetConnected(getActivity().getApplicationContext())) {
        //    Utils.createOKDialog(getActivity(), "Internet Connection!!!", "There is no internet connection available. Please enable Wifi or Data connection for better use of the map.");
        //    return;
        //}

        if(position == 0) {
            Utils.createOKDialog(getActivity(), "Alert!!!", "Please select a city.");
            return;
        }
        if(code.length()<= 0) {
            Utils.createOKDialog(getActivity(), "Alert!!!", "Please enter a city code.");
            return;
        }

        AsyncLoaderAddress asyncTaskHelper = new AsyncLoaderAddress(city, code);
        asyncTaskHelper.execute(FragmentMap.this, null, null);
    }*/

    @Override
    public void executeAsyncTaskCallBack(List<AddressBase> addressLatLonList) {
        if(addressLatLonList == null || addressLatLonList.size() <= 0) {
            Toast.makeText(getActivity().getApplicationContext(), "No place found to load in map!!!",Toast.LENGTH_LONG).show();
            return;
        }
        loadAddressMarkers(addressLatLonList);
        AddressLatLon addressLatLon = (AddressLatLon)addressLatLonList.get(0);
        LatLng latLng = new LatLng(addressLatLon.getLatitude(), addressLatLon.getLongitude());
        moveCameraToLocation(latLng);
    }
}
