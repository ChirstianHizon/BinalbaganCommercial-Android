package com.example.chris.bcconsole;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.chris.bcconsole.adapters.TabsPageAdapter;
import com.example.chris.bcconsole.classes.Products;
import com.example.chris.bcconsole.fragments.fragment_Inventory_barcode;
import com.example.chris.bcconsole.fragments.fragment_Inventory_description;
import com.example.chris.bcconsole.fragments.fragment_Inventory_levels;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class InventoryView extends AppCompatActivity {

    private TabsPageAdapter mTabsPageAdapter;
    private ViewPager mViewPager;
    private String product_ID;
    private String TAG = "InventoryView";
    private Products prod;
    private String test = "test";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inventory_view);

        mTabsPageAdapter = new TabsPageAdapter(getSupportFragmentManager());
        mViewPager = (ViewPager) findViewById(R.id.container);


        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mViewPager);

        Intent intent = getIntent();
        product_ID = intent.getStringExtra("ID");

        initializeProductDetails(product_ID);

    }

    private void setupViewPager(ViewPager viewPager) {
        TabsPageAdapter adapter = new TabsPageAdapter(getSupportFragmentManager());
        adapter.addFragment(new fragment_Inventory_description(), "Details");
        adapter.addFragment(new fragment_Inventory_levels(), "Levels");
        adapter.addFragment(new fragment_Inventory_barcode(), "Barcode");
        viewPager.setAdapter(adapter);
    }

    private void initializeProductDetails(final String id) {

        StringRequest stringRequest = new StringRequest(Request.Method.POST, MainActivity.url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("QUERY", response);
                        try {
                            JSONObject reader = new JSONObject(response);
                            test = "MAIN";
                            prod = new Products(
                                    Integer.valueOf(product_ID),
                                    reader.getString("NAME"),
                                    reader.getString("DESC"),
                                    reader.getString("DATESTAMP"),
                                    reader.getString("TIMESTAMP"),
                                    reader.getString("PRICE"),
                                    Integer.valueOf(reader.getString("LEVEL")),
                                    Integer.valueOf(reader.getString("OPTIMAL")),
                                    Integer.valueOf(reader.getString("WARNING")),
                                    reader.getString("IMAGE"),
                                    reader.getString("CATEGORY"),
                                    reader.getString("STATUS")
                            );
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        setupViewPager(mViewPager);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d(TAG, "SERVER ERROR");
            }
        }) {
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("access", "Binalbagan_Commercial_MOBILE_Access");
                params.put("id", id);
                params.put("type", "4");
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    public Products getProduct() {
        return prod;
    }
}