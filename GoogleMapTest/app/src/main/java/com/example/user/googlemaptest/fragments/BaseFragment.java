package com.example.user.googlemaptest.fragments;

import android.support.v4.app.Fragment;

import com.example.user.googlemaptest.model.Address;

import java.util.List;

/**
 * Created by user on 6/1/2015.
 */
public abstract class BaseFragment extends Fragment{

    public abstract void executeAsyncTaskCallBack(List<Address> addressList);
}
