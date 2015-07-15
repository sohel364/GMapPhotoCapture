package com.example.user.googlemaptest.fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.user.googlemaptest.R;
import com.example.user.googlemaptest.Utilities.AsyncLoaderAddress;
import com.example.user.googlemaptest.Utilities.AsyncLoaderAddressPin;
import com.example.user.googlemaptest.Utilities.Utils;
import com.example.user.googlemaptest.fragments.containers.FragmentContainerBase;
import com.example.user.googlemaptest.listadapter.AdapterAddressPin;
import com.example.user.googlemaptest.model.AddressBase;

import java.util.List;

/**
 * Created by ASUS on 7/13/2015.
 */
public class FragmentPinCodes extends BaseFragment {

    private ListView mListViewPins;
    private TextView mTvPinMessage;
    private RelativeLayout mLayoutLoadingPin;
    private LinearLayout mLayoutListHeader;

    private String userName;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_assigned_pins, container, false);
        initialiseView(view);
        initialiseListeners();
        return view;
    }

    private void initialiseView(View view) {
        mListViewPins = (ListView) view.findViewById(R.id.list_view_pincodes);
        mTvPinMessage = (TextView) view.findViewById(R.id.text_view_assigned_message);
        mLayoutLoadingPin = (RelativeLayout) view.findViewById(R.id.loading_panel_pin);
        mLayoutListHeader = (LinearLayout) view.findViewById(R.id.layout_list_header_text);
        mTvPinMessage.setText(getActivity().getResources().getText(R.string.pin_message_loading));
        mListViewPins.setVisibility(View.GONE);
        mLayoutListHeader.setVisibility(View.GONE);
        loadAssignPinCodes();
    }

    private void initialiseListeners() {
        mListViewPins.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                TextView tvPin = (TextView) view.findViewById(R.id.text_view_address_pin);
                String pin = tvPin.getText().toString();

                //Toast.makeText(getActivity(), "Selected pin code is: "+pin, Toast.LENGTH_LONG).show();

                Fragment fragment = new FragmentMap();

                Bundle bundle = new Bundle();
                bundle.putString(BUNDLE_KEY_PIN_CODE, pin);
                fragment.setArguments(bundle);

                FragmentContainerBase parentBaseFragment = (FragmentContainerBase) getParentFragment();
                parentBaseFragment.replaceFragment(fragment, true);
            }
        });


    }

    private void loadAssignPinCodes() {
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences(Utils.SHARED_PREFERENCE_NAME, Context.MODE_PRIVATE);
        userName = sharedPreferences.getString(Utils.SHARED_KEY_USER_ID, "");
        if(userName != null || userName.length() > 0) {
            AsyncLoaderAddress asyncLoaderAddress = new AsyncLoaderAddressPin(userName);
            asyncLoaderAddress.execute(FragmentPinCodes.this, null, null);
        }
    }

    @Override
    public void executeAsyncTaskCallBack(List<AddressBase> addressPin) {
        if(addressPin == null || addressPin.size() <= 0) {
            mTvPinMessage.setText(getActivity().getResources().getText(R.string.pin_message_not_found));
            mTvPinMessage.setTextColor(Color.RED);
        } else {
            mTvPinMessage.setText(getActivity().getResources().getText(R.string.pin_message_assigned_pins));
            mTvPinMessage.setTextColor(Color.BLACK);
            AdapterAddressPin adapterAddressPin = new AdapterAddressPin(addressPin, getActivity());
            mListViewPins.setAdapter(adapterAddressPin);
            mLayoutListHeader.setVisibility(View.VISIBLE);
            mListViewPins.setVisibility(View.VISIBLE);
        }
        mLayoutLoadingPin.setVisibility(View.GONE);
    }
}
