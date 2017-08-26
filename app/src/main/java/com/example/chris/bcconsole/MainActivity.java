package com.example.chris.bcconsole;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

public class MainActivity extends AppCompatActivity {

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

        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
    }

}
