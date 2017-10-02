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
import com.example.chris.bcconsole.AdminMainActivity;
import com.example.chris.bcconsole.Objects.Reports;
import com.example.chris.bcconsole.R;
import com.example.chris.bcconsole.adapters.ReportSalesAdapter;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static com.example.chris.bcconsole.AdminMainActivity.url;

public class Report_Sale extends AppCompatActivity {
    private Activity context = this;
    private ArrayList<Reports> reports_list;
    private ListView lv_main;
    private ReportSalesAdapter adapter;
    private SwipeRefreshLayout mSwipeRefreshLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report_sale);


        final Intent intent = getIntent();
        final String type = intent.getStringExtra("Report_Type");
        final String start = intent.getStringExtra("Date_Start");
        final String end = intent.getStringExtra("Date_End");

        Toast.makeText(context, "SALESip" +" | "+ start +" | " + end, Toast.LENGTH_SHORT).show();

        GetReport(start,end);

        reports_list = new ArrayList<Reports>();
        lv_main = (ListView)findViewById(R.id.lv_main);
        adapter = new ReportSalesAdapter(context, R.layout.list_report_inventory_list, reports_list);
        lv_main.setAdapter(adapter);


        mSwipeRefreshLayout = (SwipeRefreshLayout)findViewById(R.id.swipeToRefresh);
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                GetReport(start,end);
                mSwipeRefreshLayout.setRefreshing(false);
            }
        });

    }

    private void GetReport(final String start, final String end) {
        Toast.makeText(context, AdminMainActivity.url, Toast.LENGTH_SHORT).show();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("SALES_REPORT",response);
                        try {
                            JSONObject reader = new JSONObject(response);
                            int counter = Integer.valueOf(reader.getString("COUNTER"));

                            for (int x = 1; x <= counter;x++){
                                JSONObject order = new JSONObject(reader.getString(String.valueOf(x)));

//                                "SID" =>$sales_id,
//                                        "DATESTAMP" =>$sales_date,
//                                        "USER" =>$customer_id,
//                                        "TYPE" =>$sales_type,
//                                        "QTY" =>$sales_qty,
//                                        "TOTAL"=>number_format($sales_total,2)

                                reports_list.add(
                                        new Reports(
                                                order.getString("SID"),
                                                order.getString("DATESTAMP"),
                                                order.getString("USER"),
                                                order.getString("TYPE"),
                                                order.getString("QTY"),
                                                order.getString("TOTAL")
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
                params.put("type", "14");
                params.put("fromdate", start);
                params.put("todate", end);
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }
}
