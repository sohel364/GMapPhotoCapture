package com.example.user.googlemaptest.fragments.containers;

import com.example.user.googlemaptest.fragments.FragmentGraph;

/**
 * Created by ASUS on 6/25/2015.
 */
public class FragmentContainerGraph extends FragmentContainerBase{

    @Override
    protected void initialiseView() {
        mIsViewInitiated = true;
        replaceFragment(new FragmentGraph(), false);
    }
}
