package com.example.chris.bcconsole.Delivery;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.chris.bcconsole.AdminMainActivity;
import com.example.chris.bcconsole.Service.LocatorService;
import com.example.chris.bcconsole.R;
import com.example.chris.bcconsole.SQLite.DBController;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class Delivery_View extends AppCompatActivity {

    private String TAG ="Delivery ";
    private String order_id = "",status;
    private TextView tvstatus,tvorderid,tvcustname,tvorderdate,tvtitems,tvtamount;
    private DBController myDb;
    private Button btndelivery;
    private Activity context = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delivery_view);

        myDb = new DBController(this);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Intent intent = getIntent();
        order_id = intent.getStringExtra("ID");
        initializeProductDetails(order_id);
        // add back arrow to toolbar
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        tvstatus = (TextView)findViewById(R.id.tv_status);
        tvorderid = (TextView)findViewById(R.id.tv_orderid);
        tvcustname = (TextView)findViewById(R.id.tv_custname);
        tvorderdate = (TextView)findViewById(R.id.tv_orderdate);
        tvtitems = (TextView)findViewById(R.id.tv_titems);
        tvtamount = (TextView)findViewById(R.id.tv_tamount);

        btndelivery = (Button)findViewById(R.id.btn_delivery);

        btndelivery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            runService();
            }
        });
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // handle arrow click here
        if (item.getItemId() == android.R.id.home) {
            finish(); // close this activity and return to preview activity (if there is any)
        }

        return super.onOptionsItemSelected(item);
    }

    private void initializeProductDetails(final String id) {

        StringRequest stringRequest = new StringRequest(Request.Method.POST, AdminMainActivity.url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("QUERY", response);
                        try {
                            JSONObject json = new JSONObject(response);
                            tvorderid.setText(json.getString("ID"));
                            tvcustname.setText(json.getString("LNAME") + ", " + json.getString("FNAME"));
                            tvorderdate.setText(json.getString("DATE"));
                            tvtitems.setText(json.getString("TITEMS"));
                            tvtamount.setText("P "+json.getString("TAMOUNT"));


                            switch (json.getString("STATUS")){
                                case "1":
                                    tvstatus.setText("ORDER READY FOR DELIVERY");
                                    break;
                                case "0":
                                    tvstatus.setText("ORDER NOT APPROVED");
                                    break;
                                case "100":
                                    tvstatus.setText("ORDER COMPLETE");
                                    break;
                                case "200":
                                    tvstatus.setText("DELIVERY ON PROGRESS");
                                    break;
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
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
                params.put("type", "7");
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    private void DBFunctions(){
        Boolean test = myDb.insertRoute("test","test","");
        Log.d("DB-INSERT", String.valueOf(test));

        Cursor route = myDb.getAllRoute();
        while(route.moveToNext()) {
            String id = route.getString(0);
            String lat = route.getString(1);
            String lng = route.getString(2);
            String datetime = route.getString(3);
            Log.d("DB-VIEW", id + ","+lat + ","+lng + ","+datetime);
        }

        Integer del = myDb.deleteAllRouteData();
        Log.d("DB-DELETE", String.valueOf(del));

        route = myDb.getAllRoute();
        while(route.moveToNext()) {
            String id = route.getString(0);
            String lat = route.getString(1);
            String lng = route.getString(2);
            String datetime = route.getString(3);
            Log.d("DB-VIEW", id + ","+lat + ","+lng + ","+datetime);
        }
    }

    private void runService(){
        Intent intent = new Intent(context, LocatorService.class);
        startService(intent);

    }
}
