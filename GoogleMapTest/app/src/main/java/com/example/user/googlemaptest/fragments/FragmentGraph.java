package com.example.user.googlemaptest.fragments;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

import com.androidplot.pie.PieChart;
import com.androidplot.pie.PieRenderer;
import com.androidplot.pie.Segment;
import com.androidplot.pie.SegmentFormatter;
import com.androidplot.xy.XYPlot;
import com.example.user.googlemaptest.R;
import com.example.user.googlemaptest.Utilities.AsyncTaskHelper;
import com.example.user.googlemaptest.Utilities.Utils;
import com.example.user.googlemaptest.listadapter.LocationListAdapter;
import com.example.user.googlemaptest.model.Address;

import java.util.List;

/**
 * Created by user on 5/20/2015.
 */
public class FragmentGraph extends BaseFragment{

    private XYPlot mySimpleXYPlot;

    private ListView mListViewLocations;
    private PieChart mPiechart;
    private LocationListAdapter mLocationListAdapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.fragment_graph, container, false);

        initView(view);
        initListeners();

        return view;
    }

    private void initView(View view) {
        mPiechart = (PieChart) view.findViewById(R.id.mySimplePieChart);
        mListViewLocations = (ListView) view.findViewById(R.id.list_view_locations);
        mLocationListAdapter = null;

        loadGraph();
        loadLocationList();
    }

    private void initListeners() {

    }

    private void loadGraph(){

        mPiechart.getBackgroundPaint().setColor(Color.WHITE);

        Segment seg0 = new Segment(" ", 2.0);
        Segment seg1 = new Segment(" ", 5);
        Segment seg2 = new Segment(" ", 10);

        mPiechart.addSeries(seg0, new SegmentFormatter(Color.rgb(106, 168, 79), Color.BLACK, Color.BLACK, Color.BLACK));
        mPiechart.addSeries(seg1, new SegmentFormatter(Color.rgb(255, 0, 0), Color.BLACK, Color.BLACK, Color.BLACK));
        mPiechart.addSeries(seg2, new SegmentFormatter(Color.rgb(255, 153, 0), Color.BLACK, Color.BLACK, Color.BLACK));
        PieRenderer pieRenderer = mPiechart.getRenderer(PieRenderer.class);
        pieRenderer.setDonutSize((float) 0 / 100, PieRenderer.DonutMode.PERCENT);
    }


    public void loadLocationList() {
        if(Utils.isInternetConnected(getActivity().getApplicationContext())) {
            AsyncTaskHelper asyncTaskHelper = new AsyncTaskHelper(-90.0,90.0,-180.0,180.0);
            asyncTaskHelper.execute(FragmentGraph.this);
        } else {
            Toast.makeText(getActivity().getApplicationContext(), "Please enable data connection or WiFi to access internet",Toast.LENGTH_LONG).show();
        }

    }

    @Override
    public void executeAsyncTaskCallBack(List<Address> addressList) {
        mLocationListAdapter = new LocationListAdapter(addressList, getActivity());
        mListViewLocations.setAdapter(mLocationListAdapter);
    }
}