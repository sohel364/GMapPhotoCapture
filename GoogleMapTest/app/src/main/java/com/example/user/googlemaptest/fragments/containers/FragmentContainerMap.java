package com.example.user.googlemaptest.fragments.containers;

import com.example.user.googlemaptest.fragments.FragmentMap;
import com.example.user.googlemaptest.fragments.FragmentPinCodes;

/**
 * Created by ASUS on 6/25/2015.
 */
public class FragmentContainerMap extends FragmentContainerBase {

    @Override
    protected void initialiseView() {
        mIsViewInitiated = true;

        //replaceFragment(new FragmentMap(),false);
        replaceFragment(new FragmentPinCodes(), false);
    }
}
