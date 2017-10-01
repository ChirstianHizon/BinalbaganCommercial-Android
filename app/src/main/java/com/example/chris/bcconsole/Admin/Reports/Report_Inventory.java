package com.example.chris.bcconsole.Admin.Reports;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.chris.bcconsole.Objects.Reports;
import com.example.chris.bcconsole.R;
import com.example.chris.bcconsole.adapters.ReportInventoryAdapter;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.example.chris.bcconsole.AdminMainActivity.url;

public class Report_Inventory extends AppCompatActivity {

    private Activity context = this;
    private ListView lv_main;
    private ReportInventoryAdapter adapter;
    private List<Reports> reports_list;
    private SwipeRefreshLayout mSwipeRefreshLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report__inventory);


        final Intent intent = getIntent();
        final String type = intent.getStringExtra("Report_Type");
        final String start = intent.getStringExtra("Date_Start");
        final String end = intent.getStringExtra("Date_End");

        Toast.makeText(context, type +" | "+ start +" | " + end, Toast.LENGTH_SHORT).show();
        checkReport(start,end);


        reports_list = new ArrayList<Reports>();
        lv_main = (ListView)findViewById(R.id.lv_main);
        adapter = new ReportInventoryAdapter(context, R.layout.list_report_list, reports_list);
        lv_main.setAdapter(adapter);


        mSwipeRefreshLayout = (SwipeRefreshLayout)findViewById(R.id.swipeToRefresh);
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
            checkReport(start,end);
            mSwipeRefreshLayout.setRefreshing(false);
            }
        });


    }


    private void checkReport(final String start, final String end) {
//        Toast.makeText(context, AdminMainActivity.url, Toast.LENGTH_SHORT).show();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("REPORT_INVENTORY: ",response);
                        try {
                            JSONObject reader = new JSONObject(response);
                            int counter = Integer.valueOf(reader.getString("COUNTER"));

                            for (int x = 1; x <= counter;x++){
                                JSONObject rep = new JSONObject(reader.getString(String.valueOf(x)));

                                reports_list.add(
                                        new Reports(
                                                rep.getString("ID"),
                                                rep.getString("PRDNAME"),
                                                rep.getString("DATESTAMP"),
                                                rep.getString("TYPE"),
                                                rep.getString("EMPLOYEE"),
                                                rep.getString("LOGQTY"),
                                                rep.getString("TOTAL"),
                                                rep.getString("SUPPLIER")
                                        )
                                );
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        adapter.notifyDataSetChanged();
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
                params.put("type", "13");
                params.put("fromdate", start);
                params.put("todate", end);
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }
}


//                        try {
//                                JSONObject reader = new JSONObject(response);
//                                int counter = Integer.valueOf(reader.getString("COUNTER"));
//
//                                for (int x = 1; x <= counter;x++){
//                                JSONObject order = new JSONObject(reader.getString(String.valueOf(x)));
//
//                                }
//                                } catch (JSONException e) {
//                                e.printStackTrace();
//                                }