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
import android.view.View;
import android.widget.SearchView;
import android.widget.TextView;

import com.example.chris.bcconsole.fragments.fragment_Dashboard;
import com.example.chris.bcconsole.fragments.fragment_Inventory;
import com.example.chris.bcconsole.fragments.fragment_Reports;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "Main Activity";
    //    public final static String url = "http://10.0.2.2/BinalbaganCommercial-Thesis/php/mobile.php";
//    public final static String url = "http://192.168.1.129/BinalbaganCommercial-Thesis/php/mobile.php";
//    public final static String url = "http://192.168.1.36/BinalbaganCommercial-Thesis/php/mobile.php";
    public static String url = "http://192.168.137.1/BinalbaganCommercial-Thesis/php/mobile.php";
    private Toolbar tb;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        search = (SearchView) findViewById(R.id.searchbar);
        header = (TextView) findViewById(R.id.header);

//        EditText tvsearch =(EditText)search.findViewById(android.support.v7.appcompat.R.id.search_src_text);
//        tvsearch.setTextColor(Color.parseColor("#ffffff");


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

    public SearchView getSearch() {
        return search;
    }

    public TextView getHeader() {
        return header;
    }

}
