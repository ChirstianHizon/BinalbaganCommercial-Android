package com.example.chris.bcconsole.Admin;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.example.chris.bcconsole.Admin.fragment_delivery.fragment_delivery_details;
import com.example.chris.bcconsole.Admin.fragment_delivery.fragment_delivery_products;
import com.example.chris.bcconsole.R;
import com.example.chris.bcconsole.adapters.TabsPageAdapter;

public class DeliveryView extends AppCompatActivity {

    private TabsPageAdapter mTabsPageAdapter;
    private ViewPager mViewPager;
    private String delivery_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delivery_view_admin);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        // add back arrow to toolbar
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        mTabsPageAdapter = new TabsPageAdapter(getSupportFragmentManager());
        mViewPager = (ViewPager) findViewById(R.id.container);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mViewPager);
        setupViewPager(mViewPager);

        Intent intent = getIntent();
        delivery_id = intent.getStringExtra("ID");
    }

    private void setupViewPager(ViewPager viewPager) {
        TabsPageAdapter adapter = new TabsPageAdapter(getSupportFragmentManager());
        adapter.addFragment(new fragment_delivery_details(), "Details");
        adapter.addFragment(new fragment_delivery_products(), "Products");
        viewPager.setAdapter(adapter);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // handle arrow click here
        if (item.getItemId() == android.R.id.home) {
            finish(); // close this activity and return to preview activity (if there is any)
        }

        return super.onOptionsItemSelected(item);
    }
    public String getDelivery_id(){
        return delivery_id;
    }
}
