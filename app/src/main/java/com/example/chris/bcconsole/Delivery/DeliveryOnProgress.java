package com.example.chris.bcconsole.Delivery;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.chris.bcconsole.DeliveryMainActivity;
import com.example.chris.bcconsole.LoginActiviy;
import com.example.chris.bcconsole.R;
import com.example.chris.bcconsole.SQLite.DBController;
import com.example.chris.bcconsole.Service.Service_Location_Tracker;
import com.example.chris.bcconsole.SettingsActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import static com.example.chris.bcconsole.AdminMainActivity.url;

public class DeliveryOnProgress extends AppCompatActivity {

    private Button btnend;
    private Activity context =this;
    private DBController myDb;
    private static Boolean isDeliveryFinised = false;
    private SharedPreferences preferences;
    private TextView tvlat;
    private TextView tvlng;
    private TextView tvcounter;
    private TextView tvorderno;
    private JSONArray coord = new JSONArray();
    private String orderid,customer,address,contact;
    private TextView tvcustname;
    private TextView tvaddress;
    private TextView tvcontact;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delivery_on_progress);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        btnend = (Button)findViewById(R.id.btn_end_delivery);
        tvlat = (TextView)findViewById(R.id.tv_lat);
        tvlng = (TextView)findViewById(R.id.tv_lng);
        tvcounter = (TextView)findViewById(R.id.tv_counter);
        tvorderno = (TextView)findViewById(R.id.tv_orderno);
        tvcustname = (TextView)findViewById(R.id.tv_custname);
        tvaddress = (TextView)findViewById(R.id.tv_address);
        tvcontact = (TextView)findViewById(R.id.tv_contact);


        SharedPreferences prefs = getSharedPreferences("DELIVERY", MODE_PRIVATE);
        orderid = prefs.getString("id", null);
        customer = prefs.getString("customer", null);
        address = prefs.getString("address", null);
        contact = prefs.getString("contact", null);

//        Intent intent = getIntent();
//        String order_id = intent.getStringExtra("ID");
        runLocator(orderid);

        tvorderno.setText(orderid);
        tvcustname.setText(customer);
        tvaddress.setText(address);
        tvcontact.setText(contact);

        myDb = new DBController(this);

        int counter = 0;
        final Cursor route = myDb.getAllRoute();
        while(route.moveToNext()) {
            counter++;
            String lat = route.getString(1);
            String lng = route.getString(2);
            String datetime = route.getString(3);
            String accuracy = route.getString(4);

            Log.d("LOCATION_DB",lat + ","+lng + ","+datetime +", "+accuracy);

        }
//        tvorderno.setText(String.valueOf(counter));





        btnend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int routecount = myDb.countRoute();
                if(routecount > 0 ){

                    btnend.setText("Uploading Data");
                    btnend.setEnabled(false);

                    SharedPreferences prefs = getSharedPreferences("DELIVERY", MODE_PRIVATE);
                    orderid = prefs.getString("id", null);


                    preferences = getSharedPreferences("DELIVERY",MODE_PRIVATE);
                    SharedPreferences.Editor editor = preferences.edit();
                    editor.clear();
                    editor.apply();

//                    myDb.deleteAllRouteData();

                    Intent intx = new Intent(context, Service_Location_Tracker.class);
                    startService(intx);



                }else {
                    Toast.makeText(context, "No Route Found", Toast.LENGTH_SHORT).show();
                }
            }
        });

        String ACTION_LOCATION_BROADCAST = Service_Location_Tracker.class.getName() + "LocationBroadcast";
        LocalBroadcastManager.getInstance(this).registerReceiver(
                new BroadcastReceiver() {
                    @Override
                    public void onReceive(Context context, Intent intent) {
                        String routecount = intent.getStringExtra("counter");
                        Boolean status = intent.getBooleanExtra("status",true);
                        if(routecount != null){
                            tvcounter.setText(routecount);
                        }
                        if(!status){

                            Toast.makeText(context, "UPLOADING TO SERVER", Toast.LENGTH_LONG).show();
                            updateWeb();

                        }
                    }
                }, new IntentFilter(ACTION_LOCATION_BROADCAST)
        );
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

    private void runLocator(String id){
        Intent intent = new Intent(context, Service_Location_Tracker.class);
        startService(intent);

    }

    private void updateWeb(){
        Cursor route = myDb.getAllRoute();

        while(route.moveToNext()) {
            String id = route.getString(0);
            String lat = route.getString(1);
            String lng = route.getString(2);
            String acc = route.getString(3);
            String datetime = route.getString(4);
            String delivid = route.getString(5);
            JSONObject xcord = new JSONObject();
            try {

                xcord.put("id", id);
                xcord.put("lat", lat);
                xcord.put("lng", lng);
                xcord.put("datetime", datetime);
                xcord.put("delivid", delivid);

            } catch (JSONException e) {
                e.printStackTrace();
            }
            Log.d("DB-ROUTE", id + ","+lat + ","+lng + ","+datetime+ ","+delivid+ ","+acc);
            coord.put(xcord);
        }

        Log.d("COORD", String.valueOf(coord));
        submit(coord);
    }

    private void submit(final JSONArray coordobj) {
        Log.d("COORD-ORDERID", orderid);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("COORD-RESULT",response);

                        try {
                            JSONObject reader = new JSONObject(response);
                            if(reader.getBoolean("RESULT")){

                                myDb.deleteAllRouteData();

                                Intent intent = new Intent(context, DeliveryMainActivity.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(intent);
                                finish();

                                Toast.makeText(context, "DELIVERY COMPLETE", Toast.LENGTH_SHORT).show();

                            }else{
                                Toast.makeText(context, "SERVER ERROR", Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                Toast.makeText(context, "Unable to Connect to Server", Toast.LENGTH_SHORT).show();

            }
        }) {
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("access", "Binalbagan_Commercial_MOBILE_Access");
                params.put("type", "9");
                params.put("orderid", orderid);
                params.put("coord", String.valueOf(coordobj));
                Log.d("COORD-POST", String.valueOf(params));
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

}
