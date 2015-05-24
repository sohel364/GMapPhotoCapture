package com.example.user.googlemaptest.fragments;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.androidplot.pie.PieChart;
import com.androidplot.pie.PieRenderer;
import com.androidplot.pie.Segment;
import com.androidplot.pie.SegmentFormatter;
import com.androidplot.xy.LineAndPointFormatter;
import com.androidplot.xy.PointLabelFormatter;
import com.androidplot.xy.SimpleXYSeries;
import com.androidplot.xy.XYPlot;
import com.androidplot.xy.XYSeries;
import com.example.user.googlemaptest.R;
import com.example.user.googlemaptest.Utilities.Utils;

import java.io.IOException;
import java.util.Arrays;

/**
 * Created by user on 5/20/2015.
 */
public class FragmentGraph extends Fragment{

    private XYPlot mySimpleXYPlot;
    private View view;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view =  inflater.inflate(R.layout.fragment_graph, container, false);
        LoadGraph();
        return view;
    }

    public void LoadGraph(){
        PieChart graficoPartidos = (PieChart) view.findViewById(R.id.mySimplePieChart);
        graficoPartidos.getBackgroundPaint().setColor(Color.WHITE);

        Segment seg0 = new Segment(" ", 2.0);
        Segment seg1 = new Segment(" ", 5);
        Segment seg2 = new Segment(" ", 10);

        graficoPartidos.addSeries(seg0, new SegmentFormatter(Color.rgb(106, 168, 79), Color.BLACK,Color.BLACK, Color.BLACK));
        graficoPartidos.addSeries(seg1, new SegmentFormatter(Color.rgb(255, 0, 0), Color.BLACK,Color.BLACK, Color.BLACK));
        graficoPartidos.addSeries(seg2, new SegmentFormatter(Color.rgb(255, 153, 0), Color.BLACK,Color.BLACK, Color.BLACK));
        PieRenderer pieRenderer = graficoPartidos.getRenderer(PieRenderer.class);
        pieRenderer.setDonutSize((float) 0 / 100, PieRenderer.DonutMode.PERCENT);
    }


}