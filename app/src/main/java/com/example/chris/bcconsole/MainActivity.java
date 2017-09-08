package com.example.chris.bcconsole;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;

import com.example.chris.bcconsole.fragments.fragment_Dashboard;
import com.example.chris.bcconsole.fragments.fragment_Inventory;
import com.example.chris.bcconsole.fragments.fragment_Reports;

public class MainActivity extends AppCompatActivity {

    public final static String url = "http://10.0.2.2/BinalbaganCommercial-Thesis/php/mobile.php";
    //    public final static String url = "http://192.168.1.129/BinalbaganCommercial-Thesis/php/mobile.php";
//    public final static String url = "http://192.168.1.36/BinalbaganCommercial-Thesis/php/mobile.php";
    private static final String TAG = "Main Activity";
    private Toolbar tb;
    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_dashboard:
                    setTitle("Dashboard");
                    fragment_Dashboard dashboard = new fragment_Dashboard();
                    getSupportFragmentManager().beginTransaction().replace(R.id.fragment, dashboard).commit();
                    return true;
                case R.id.navigation_inventory:
                    //TODO: ADD INVENTORY
                    setTitle("Inventory");
                    fragment_Inventory inventory = new fragment_Inventory();
                    getSupportFragmentManager().beginTransaction().replace(R.id.fragment, inventory).commit();
                    return true;
                case R.id.navigation_reports:
                    //TODO: ADD REPORTS
                    setTitle("Reports");
                    fragment_Reports reports = new fragment_Reports();
                    getSupportFragmentManager().beginTransaction().replace(R.id.fragment, reports).commit();
                    return true;
            }
            return false;
        }

    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        tb = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(tb);
        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);


//        DBController db = new DBController(this);
//        Log.d("Insert: ", "Inserting ..");

        SharedPreferences prefs = getSharedPreferences("USER", MODE_PRIVATE);
        String uid = prefs.getString("uid", "true");
        Log.i(TAG, "UID:" + uid);

        if (Boolean.valueOf(uid)) {
            Intent intent = new Intent(MainActivity.this, LoginActiviy.class);
            startActivity(intent);
        }
    }

}
