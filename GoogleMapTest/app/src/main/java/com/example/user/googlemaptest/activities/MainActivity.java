package com.example.user.googlemaptest.activities;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTabHost;
import android.view.Menu;
import android.view.MenuItem;
import android.view.WindowManager;
import android.widget.Toast;

import com.example.user.googlemaptest.R;
import com.example.user.googlemaptest.Utilities.Utils;
import com.example.user.googlemaptest.fragments.FragmentGraph;
import com.example.user.googlemaptest.fragments.FragmentMap;
import com.example.user.googlemaptest.fragments.FragmentUserInfo;
import com.example.user.googlemaptest.fragments.containers.FragmentContainerBase;
import com.example.user.googlemaptest.fragments.containers.FragmentContainerGraph;
import com.example.user.googlemaptest.fragments.containers.FragmentContainerMap;
import com.example.user.googlemaptest.fragments.containers.FragmentContainerUserInfo;


public class MainActivity extends FragmentActivity {

    private FragmentTabHost mTabHost;

    private static final String TAB_1_TAG = "graph";
    private static final String TAB_2_TAG = "map";
    private static final String TAB_3_TAG = "user_info";

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

        mTabHost.addTab(mTabHost.newTabSpec(TAB_1_TAG).setIndicator("Graph"),
                FragmentContainerGraph.class, null);
        mTabHost.addTab(mTabHost.newTabSpec(TAB_2_TAG).setIndicator("Map"),
                FragmentContainerMap.class, null);
        mTabHost.addTab(mTabHost.newTabSpec(TAB_3_TAG).setIndicator("User Info"),
                FragmentContainerUserInfo.class, null);

        getWindow().addFlags(
                WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN);

        SharedPreferences sharedPreferences = this.getSharedPreferences(Utils.SHARED_PREFERENCE_NAME, Context.MODE_PRIVATE);
        String sharedCode = sharedPreferences.getString(Utils.SHARED_KEY_CODE, "");
        if(sharedCode == null || sharedCode.length()<=0) {
            sharedPreferences.edit().putString(Utils.SHARED_KEY_CODE, "master").commit();
        }

        //requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
    }


    @Override
    public void onBackPressed() {
        boolean isPopFragment = false;
        String currentTabTag = mTabHost.getCurrentTabTag();
        if (currentTabTag.equals(TAB_1_TAG)) {
            isPopFragment = ((FragmentContainerBase)getSupportFragmentManager().findFragmentByTag(TAB_1_TAG)).popFragment();
        } else if (currentTabTag.equals(TAB_2_TAG)) {
            isPopFragment = ((FragmentContainerBase)getSupportFragmentManager().findFragmentByTag(TAB_2_TAG)).popFragment();
        } else if (currentTabTag.equals(TAB_3_TAG)) {
            isPopFragment = ((FragmentContainerBase)getSupportFragmentManager().findFragmentByTag(TAB_3_TAG)).popFragment();
        }
        if (!isPopFragment) {
            finish();
        }
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
