package com.example.chris.bcconsole.Admin;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.chris.bcconsole.AdminMainActivity;
import com.example.chris.bcconsole.LoginActiviy;
import com.example.chris.bcconsole.R;
import com.example.chris.bcconsole.SettingsActivity;
import com.example.chris.bcconsole.adapters.DeliveryListAdapter_Admin;
import com.example.chris.bcconsole.Objects.Delivery;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static com.example.chris.bcconsole.AdminMainActivity.url;

public class DeliveryList extends AppCompatActivity {
    private Activity context = this;
    private ArrayList<Delivery> delivery_list;
    private ListView lv_main;
    private DeliveryListAdapter_Admin adapter;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private ProgressBar progbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delivery_list);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // add back arrow to toolbar
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        delivery_list = new ArrayList<Delivery>();
        lv_main = (ListView)findViewById(R.id.lv_main);
        adapter = new DeliveryListAdapter_Admin(context, R.layout.list_delivery_delivery, delivery_list);
        lv_main.setAdapter(adapter);

        checkDelivery();

        progbar = (ProgressBar)findViewById(R.id.progbar);


        mSwipeRefreshLayout = (SwipeRefreshLayout)findViewById(R.id.swipeToRefresh);
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                progbar.setVisibility(View.VISIBLE);
                adapter.clear();
                checkDelivery();
                mSwipeRefreshLayout.setRefreshing(false);
            }
        });
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

        } else if (item.getItemId() == android.R.id.home) {
            finish(); // close this activity and return to preview activity (if there is any)
        }

        return super.onOptionsItemSelected(item);
    }


    private void checkDelivery() {
        Toast.makeText(context, AdminMainActivity.url, Toast.LENGTH_SHORT).show();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("DELIVERY: ",response);
                        try {
                            JSONObject reader = new JSONObject(response);
                            int counter = Integer.valueOf(reader.getString("COUNTER"));

                            for (int x = 1; x <= counter;x++){
                                JSONObject order = new JSONObject(reader.getString(String.valueOf(x)));
                                Log.d("DELIVERY-ID: ", order.getString("STATUS"));
                                String name = order.getString("LNAME") +", "+order.getString("FNAME");

                            delivery_list.add(
                                    new Delivery(
                                            order.getString("ID"),
                                            name,
                                            order.getString("DATE"),
                                            order.getString("STATUS")
                                    ));
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        progbar.setVisibility(View.GONE);
                        adapter.notifyDataSetChanged();
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progbar.setVisibility(View.GONE);
                Toast.makeText(context, "Unable to Connect to Server", Toast.LENGTH_SHORT).show();
            }
        }) {
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("access", "Binalbagan_Commercial_MOBILE_Access");
                params.put("type", "10");
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }
}
