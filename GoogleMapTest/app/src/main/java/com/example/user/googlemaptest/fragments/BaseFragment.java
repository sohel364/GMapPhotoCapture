package com.example.user.googlemaptest.fragments;

import android.support.v4.app.Fragment;

import com.example.user.googlemaptest.model.AddressBase;

import java.util.List;

/**
 * Created by user on 6/1/2015.
 */

public abstract class BaseFragment extends Fragment{
    protected static final String BUNDLE_KEY_PIN_CODE = "BUNDLE_KEY_PIN_CODE";
    public abstract void executeAsyncTaskCallBack(List<AddressBase> addressLatLonList);
}
