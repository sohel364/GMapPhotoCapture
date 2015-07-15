package com.example.user.googlemaptest.fragments;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.androidplot.pie.PieChart;
import com.androidplot.pie.PieRenderer;
import com.androidplot.pie.Segment;
import com.androidplot.pie.SegmentFormatter;
import com.androidplot.xy.XYPlot;
import com.example.user.googlemaptest.R;
import com.example.user.googlemaptest.Utilities.AsyncLoaderAddress;
import com.example.user.googlemaptest.Utilities.AsyncLoaderAddressLatLon;
import com.example.user.googlemaptest.Utilities.Utils;
import com.example.user.googlemaptest.listadapter.AdapterLocationList;
import com.example.user.googlemaptest.model.AddressBase;

import java.util.List;

/**
 * Created by user on 5/20/2015.
 */
public class FragmentGraph extends BaseFragment{

    private XYPlot mySimpleXYPlot;

    private ListView mListViewLocations;
    private PieChart mPieChart;
    private AdapterLocationList mAdapterLocationList;
    private Spinner mSpinnerPlaceTypes;
    private RelativeLayout mLoadingPanel;
    private TextView mTvLoadingMsg;
    private TextView mTvFakeSpace;
    private static AsyncLoaderAddress mAsyncTaskHelper;
    private static View mView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if(mView == null) {
            mView = inflater.inflate(R.layout.fragment_graph, container, false);

            initView(mView);
            initListeners();
        }

        return mView;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        /*if(mAsyncTaskHelper != null) {
            mAsyncTaskHelper.cancel(true);
        }*/
    }

    private void initView(View view) {
        mPieChart = (PieChart) view.findViewById(R.id.mySimplePieChart);
        mListViewLocations = (ListView) view.findViewById(R.id.list_view_locations);
        mListViewLocations.setVisibility(View.GONE);
        mAdapterLocationList = null;

        mSpinnerPlaceTypes = (Spinner) view.findViewById(R.id.spinner_place_types);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getActivity(), R.array.place_type_array,android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mSpinnerPlaceTypes.setAdapter(adapter);

        mLoadingPanel = (RelativeLayout) view.findViewById(R.id.loading_panel);

        mTvLoadingMsg = (TextView) view.findViewById(R.id.text_view_loading_Message);
        mTvLoadingMsg.setTextColor(Color.BLUE);
        mTvLoadingMsg.setText("Loading...");

        mTvFakeSpace = (TextView) view.findViewById(R.id.text_view_fake_space);
        mTvFakeSpace.setVisibility(View.GONE);


        loadGraph();
        //loadLocationList();
    }

    private void initListeners() {
        mSpinnerPlaceTypes.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(position == 0) {
                    //Toast.makeText(getActivity().getApplicationContext(), "All", Toast.LENGTH_LONG).show();
                    if(mAsyncTaskHelper != null) {
                        mAsyncTaskHelper.cancel(true);
                    }
                    loadLocationList(0);
                } else if(position == 1) {
                    //Toast.makeText(getActivity().getApplicationContext(), "Completed", Toast.LENGTH_LONG).show();
                    if(mAsyncTaskHelper != null) {
                        mAsyncTaskHelper.cancel(true);
                    }
                    loadLocationList(1);
                } else {
                    //Toast.makeText(getActivity().getApplicationContext(), "Uncompleted", Toast.LENGTH_LONG).show();
                    if(mAsyncTaskHelper != null) {
                        mAsyncTaskHelper.cancel(true);
                    }
                    loadLocationList(2);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void loadGraph(){

        mPieChart.getBackgroundPaint().setColor(Color.WHITE);

        Segment seg0 = new Segment(" ", 2.0);
        Segment seg1 = new Segment(" ", 5);
        Segment seg2 = new Segment(" ", 10);

        mPieChart.addSeries(seg0, new SegmentFormatter(Color.rgb(106, 168, 79), Color.BLACK, Color.BLACK, Color.BLACK));
        mPieChart.addSeries(seg1, new SegmentFormatter(Color.rgb(255, 0, 0), Color.BLACK, Color.BLACK, Color.BLACK));
        mPieChart.addSeries(seg2, new SegmentFormatter(Color.rgb(255, 153, 0), Color.BLACK, Color.BLACK, Color.BLACK));
        PieRenderer pieRenderer = mPieChart.getRenderer(PieRenderer.class);
        pieRenderer.setDonutSize((float) 0 / 100, PieRenderer.DonutMode.PERCENT);
    }


    public void loadLocationList(Integer status) {
        mListViewLocations.setVisibility(View.GONE);
        mLoadingPanel.setVisibility(View.VISIBLE);
        if(Utils.isInternetConnected(getActivity().getApplicationContext())) {
            mAsyncTaskHelper = new AsyncLoaderAddressLatLon(status);
            mAsyncTaskHelper.execute(FragmentGraph.this);
        } else {
            Toast.makeText(getActivity().getApplicationContext(), "Please enable data connection or WiFi to access internet",Toast.LENGTH_LONG).show();
            mLoadingPanel.setVisibility(View.GONE);
            mTvLoadingMsg.setText("Please enable data connection or WiFi to access internet");
            mTvLoadingMsg.setTextColor(Color.RED);
            mTvFakeSpace.setVisibility(View.VISIBLE);
        }

    }

    @Override
    public void executeAsyncTaskCallBack(List<AddressBase> addressLatLonList) {
        if(addressLatLonList == null || addressLatLonList.size()<=0) {
            Toast.makeText(getActivity(), "No data found", Toast.LENGTH_LONG).show();
            setUIVisibility();
            return;
        }
        mAdapterLocationList = new AdapterLocationList(addressLatLonList, getActivity());
        mListViewLocations.setAdapter(mAdapterLocationList);

        mAsyncTaskHelper = null;

        setUIVisibility();
    }

    private void setUIVisibility() {
        mListViewLocations.setVisibility(View.VISIBLE);
        mTvLoadingMsg.setVisibility(View.GONE);
        mLoadingPanel.setVisibility(View.GONE);
        mTvFakeSpace.setVisibility(View.GONE);
    }
}