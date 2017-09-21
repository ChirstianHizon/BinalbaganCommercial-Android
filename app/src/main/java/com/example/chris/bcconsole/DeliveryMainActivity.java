package com.example.chris.bcconsole;

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
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.chris.bcconsole.Delivery.DeliveryOnProgress;
import com.example.chris.bcconsole.Service.LocatorService;
import com.example.chris.bcconsole.adapters.DeliveryListAdapter;
import com.example.chris.bcconsole.classes.Delivery;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.example.chris.bcconsole.AdminMainActivity.url;

public class DeliveryMainActivity extends AppCompatActivity {

    private Activity context = this;
    private ListView lv_main;
    private DeliveryListAdapter adapter;
    private List<Delivery> delivery_list;
    private SwipeRefreshLayout mSwipeRefreshLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delivery_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        SharedPreferences prefs = getSharedPreferences("USER", MODE_PRIVATE);
        String uid = prefs.getString("uid", "true");
        Log.i("USER_ID: ", "UID:" + uid);

        if (Boolean.valueOf(uid)) {
            Intent intent = new Intent(context, LoginActiviy.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            finish();
        }

        SharedPreferences devprefs = getSharedPreferences("DELIVERY", MODE_PRIVATE);
        String devid = devprefs.getString("id", "false");

        Log.d("DELIVERY-PREF",devid);
        if(!Boolean.valueOf(devid).equals("false")){
            resumeDelivery(devid);
        }else{

            String prefurl = prefs.getString("url", AdminMainActivity.defaulturl);
            AdminMainActivity.setNewUrl(prefurl);
            Log.d("URL:",url);
            Log.i("USER_ID: ", "UID:" + uid);




            delivery_list = new ArrayList<Delivery>();
            lv_main = (ListView)findViewById(R.id.lv_main);
            adapter = new DeliveryListAdapter(context, R.layout.list_delivery, delivery_list);
            lv_main.setAdapter(adapter);

            checkDelivery();


            mSwipeRefreshLayout = (SwipeRefreshLayout)findViewById(R.id.swipeToRefresh);
            mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
                @Override
                public void onRefresh() {
                    adapter.clear();
                    checkDelivery();
                    mSwipeRefreshLayout.setRefreshing(false);
                }
            });

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
        }else if(id == R.id.action_settings){

            Intent intent = new Intent(context, SettingsActivity.class);
            startActivity(intent);

        }

        return super.onOptionsItemSelected(item);
    }


    private void checkDelivery() {
//        Toast.makeText(context, AdminMainActivity.url, Toast.LENGTH_SHORT).show();
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

                                if(order.getString("STATUS").equals("200")){
                                    resumeDelivery(order.getString("ID"));
                                }else{
                                delivery_list.add(
                                        new Delivery(
                                                order.getString("ID"),
                                                name
                                        ));
                                }
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        adapter.notifyDataSetChanged();
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }) {
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("access", "Binalbagan_Commercial_MOBILE_Access");
                params.put("type", "6");
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    private void resumeDelivery(String id){

        runService();

        SharedPreferences.Editor editor = getSharedPreferences("DELIVERY", MODE_PRIVATE).edit();
        editor.putString("id", id);
        editor.apply();
        Toast.makeText(context, "Resuming Delivery", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(context, DeliveryOnProgress.class);
        intent.putExtra("ID", id);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finish();

    }

    private void runService(){
        Intent intent = new Intent(context, LocatorService.class);
//        intent.putExtra("STATUS", true);
//        put EXTRA STATUS true to end Locator
        startService(intent);

    }

}
