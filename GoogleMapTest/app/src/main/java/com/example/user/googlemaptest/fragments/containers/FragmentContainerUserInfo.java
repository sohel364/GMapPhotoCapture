package com.example.user.googlemaptest.fragments.containers;

import com.example.user.googlemaptest.fragments.FragmentUserInfo;

/**
 * Created by ASUS on 6/25/2015.
 */
public class FragmentContainerUserInfo extends FragmentContainerBase{
    @Override
    protected void initialiseView() {
        mIsViewInitiated = true;
        replaceFragment(new FragmentUserInfo(), false);
    }
}
