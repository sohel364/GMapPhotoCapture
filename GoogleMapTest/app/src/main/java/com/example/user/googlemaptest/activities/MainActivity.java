package com.example.user.googlemaptest.activities;

import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTabHost;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import com.example.user.googlemaptest.R;
import com.example.user.googlemaptest.fragments.FragmentGraph;
import com.example.user.googlemaptest.fragments.FragmentMap;


public class MainActivity extends FragmentActivity {

    private FragmentTabHost mTabHost;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        mTabHost = (FragmentTabHost)findViewById(android.R.id.tabhost);
        if(mTabHost == null) {
            Toast.makeText(getApplicationContext(), "mTabHost Null", Toast.LENGTH_LONG);
            return;
        }
        if(getSupportFragmentManager() == null) {
            Toast.makeText(getApplicationContext(), "getSupportFragmentManager Null",Toast.LENGTH_LONG);
            return;
        }
        mTabHost.setup(this, getSupportFragmentManager(), android.R.id.tabcontent);

        mTabHost.addTab(mTabHost.newTabSpec("Graph").setIndicator("Graph"),
                FragmentGraph.class, null);
        mTabHost.addTab(mTabHost.newTabSpec("Map").setIndicator("Map"),
                FragmentMap.class, null);

        getWindow().addFlags(
                WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN);
        //requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
