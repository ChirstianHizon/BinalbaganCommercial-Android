package com.example.chris.bcconsole.Delivery;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
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
import com.example.chris.bcconsole.R;
import com.example.chris.bcconsole.SQLite.DBController;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import static com.example.chris.bcconsole.AdminMainActivity.url;

public class Delivery_View extends AppCompatActivity {

    private String TAG ="Reports ";
    private String order_id = "",status;
    private TextView tvstatus,tvorderid,tvcustname,tvorderdate,tvtitems,tvtamount;
    private DBController myDb;
    private Button btndelivery;
    private Activity context = this;
    private String name,address,contact;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delivery_view);

        myDb = new DBController(this);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Intent intent = getIntent();
        order_id = intent.getStringExtra("ID");
        deliveryDetails(order_id);
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
                SharedPreferences.Editor editor = getSharedPreferences("DELIVERY", MODE_PRIVATE).edit();
                editor.putString("name", name);
                editor.putString("address", address);
                editor.putString("contact", contact);
                editor.apply();


                Toast.makeText(context, "Delivery Started", Toast.LENGTH_SHORT).show();
                resumeDelivery(order_id);
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

    private void deliveryDetails(final String id) {

        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
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

                            name = json.getString("LNAME") + ", " + json.getString("FNAME");
                            address = json.getString("ADDRESS");
                            contact= json.getString("CONTACT");


                            switch (json.getString("STATUS")){
                                case "1":
                                    tvstatus.setText("ORDER READY FOR DELIVERY");
                                    break;
                                case "0":
                                    tvstatus.setText("ORDER NOT APPROVED");
                                    btndelivery.setVisibility(View.GONE);
                                    break;
                                case "100":
                                    tvstatus.setText("ORDER COMPLETE");
                                    btndelivery.setVisibility(View.GONE);
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

    private void resumeDelivery(String id){

        createDelivery(id);

    }

    private void createDelivery(final String orderid) {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("DELIVERY-RESULT",response);

                        try {
                            JSONObject reader = new JSONObject(response);

                            SharedPreferences.Editor editor = getSharedPreferences("DELIVERY", MODE_PRIVATE).edit();
                            editor.putString("id", reader.getString("ID"));
                            editor.putString("id", orderid);
                            editor.apply();

                            Toast.makeText(context, "Resuming Reports", Toast.LENGTH_SHORT).show();

                            Intent intent = new Intent(context, DeliveryOnProgress.class);
                            intent.putExtra("ID", reader.getString("ID"));
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(intent);
                            finish();

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }


                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }) {
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("access", "Binalbagan_Commercial_MOBILE_Access");
                params.put("type", "8");
                params.put("orderid", orderid);
                Log.d("COORD-POST", String.valueOf(params));
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }
}
