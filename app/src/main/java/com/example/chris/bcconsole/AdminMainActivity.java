package com.example.chris.bcconsole;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;

import com.example.chris.bcconsole.Admin.DeliveryList;
import com.example.chris.bcconsole.fragments.fragment_Dashboard;
import com.example.chris.bcconsole.fragments.fragment_Inventory;
import com.example.chris.bcconsole.fragments.fragment_Reports;

public class AdminMainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private static final String TAG = "Main Activity";
    public static String defaulturl = "http://192.168.137.1./BinalbaganCommercial-Console/php/mobile.php";
    public static String url = defaulturl;
    public ListView lv_inventory;
    private Activity context = this;
    private TextView header;
    private SearchView search;
    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_dashboard:
                    header.setVisibility(View.VISIBLE);
                    search.setVisibility(View.GONE);
                    search.clearFocus();
                    header.setText("Dashboard");
                    fragment_Dashboard dashboard = new fragment_Dashboard();
                    getSupportFragmentManager().beginTransaction().replace(R.id.fragment, dashboard).commit();
                    return true;
                case R.id.navigation_inventory:
                    //TODO: ADD INVENTORY
                    header.setText("Inventory");
                    search.setVisibility(View.VISIBLE);
                    search.clearFocus();
                    fragment_Inventory inventory = new fragment_Inventory();
                    getSupportFragmentManager().beginTransaction().replace(R.id.fragment, inventory).commit();
                    return true;
                case R.id.navigation_reports:
                    //TODO: ADD REPORTS
                    header.setText("Reports");
                    search.setVisibility(View.GONE);
                    search.clearFocus();
                    fragment_Reports reports = new fragment_Reports();
                    getSupportFragmentManager().beginTransaction().replace(R.id.fragment, reports).commit();
                    return true;
            }
            return false;
        }

    };

    public static void setNewUrl(String newurl) {
        url = newurl;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        SharedPreferences prefs = getSharedPreferences("USER", MODE_PRIVATE);
        String uid = prefs.getString("uid", "true");
        String prefurl = prefs.getString("url", defaulturl);
        setNewUrl(prefurl);
        Log.d("URL:", url);
        Log.i("USER_ID: ", "UID:" + uid);

        if (Boolean.valueOf(uid)) {
            Intent intent = new Intent(context, LoginActiviy.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            finish();
        }

        search = (SearchView) findViewById(R.id.searchbar);
        header = (TextView) findViewById(R.id.header);

        lv_inventory = (ListView) findViewById(R.id.lv_main);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_logout) {

            SharedPreferences preferences = getSharedPreferences("USER", MODE_PRIVATE);
            preferences.edit().remove("uid").apply();

            Intent intent = new Intent(context, LoginActiviy.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            finish();
            return true;
        } else if (id == R.id.action_settings) {

            Intent intent = new Intent(context, SettingsActivity.class);
            startActivity(intent);

        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_inventory) {
            header.setText("Inventory");
            search.setVisibility(View.VISIBLE);
            search.clearFocus();
            fragment_Inventory inventory = new fragment_Inventory();
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment, inventory).commit();
        } else if (id == R.id.nav_delivery) {
            Intent intent = new Intent(context, DeliveryList.class);
            startActivity(intent);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public SearchView getSearch() {
        return search;
    }

    public TextView getHeader() {
        return header;
    }

}
