package com.example.chris.bcconsole.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.chris.bcconsole.AdminMainActivity;
import com.example.chris.bcconsole.Objects.Orders;
import com.example.chris.bcconsole.R;
import com.example.chris.bcconsole.adapters.DashboardPendOrderAdapter;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static android.content.ContentValues.TAG;
import static com.example.chris.bcconsole.AdminMainActivity.url;

public class fragment_Dashboard extends android.support.v4.app.Fragment {

    private TextView tv_total_products;
    private TextView tv_total_sales;
    private TextView tv_total_delivery;
    private TextView tv_total_pending;
    private ArrayList<Orders> order_list;
    private DashboardPendOrderAdapter adapter;
    private ListView lv_main;
    private SwipeRefreshLayout mSwipeRefreshLayout;

    //    TODO : ADD TABLES
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_dashboard, container, false);
        tv_total_products = (TextView) view.findViewById(R.id.tv_total_products);
        tv_total_sales = (TextView) view.findViewById(R.id.tv_total_sales);
        tv_total_delivery = (TextView) view.findViewById(R.id.tv_total_delivery);
        tv_total_pending = (TextView) view.findViewById(R.id.tv_pending_orders);

        initializeDashboard();
        GetOrdersPending();


        order_list = new ArrayList<Orders>();
        lv_main = (ListView)view.findViewById(R.id.lv_main);
        adapter = new DashboardPendOrderAdapter(getContext(), R.layout.list_report_inventory_list, order_list);
        lv_main.setAdapter(adapter);

        mSwipeRefreshLayout = (SwipeRefreshLayout)view.findViewById(R.id.swipeToRefresh);
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                initializeDashboard();
                GetOrdersPending();
                mSwipeRefreshLayout.setRefreshing(false);
            }
        });
        return view;
    }

    private void initializeDashboard() {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, AdminMainActivity.url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d(TAG, response);
                        try {
                            JSONObject reader = new JSONObject(response);
                            tv_total_products.setText(reader.getString("PRODUCT_COUNT") + " item/s");
                            tv_total_sales.setText("P " + reader.getString("SALES_TOTAL"));
                            tv_total_delivery.setText(reader.getString("DELIVERY_TOTAL") + " delivery/s");
                            tv_total_pending.setText(reader.getString("PENDING_COUNT") + " order/s");
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, "VOLLEY ERROR");
            }
        }) {
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("access", "Binalbagan_Commercial_MOBILE_Access");
                params.put("type", "3");
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        requestQueue.add(stringRequest);

    }

    private void GetOrdersPending() {
//        Toast.makeText(getContext(), AdminMainActivity.url, Toast.LENGTH_SHORT).show();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("ORDER_PENDING_REPORT",response);
                        try {
                            JSONObject reader = new JSONObject(response);
                            int counter = Integer.valueOf(reader.getString("COUNTER"));
// FIX ME
                            for (int x = 1; x <= 3;x++){
                                JSONObject order = new JSONObject(reader.getString(String.valueOf(x)));

//                                Orders(String id,String orderid,String datestamp,String customer,String status,String qty)

                                order_list.add(
                                        new Orders(
                                                order.getString("ID"),
                                                order.getString("TOTAL"),
                                                order.getString("DATESTAMP"),
                                                order.getString("CUSTOMER"),
                                                order.getString("STATUS"),
                                                order.getString("QTY")
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
                Toast.makeText(getContext(), "Unable to Connect to Server", Toast.LENGTH_SHORT).show();
            }
        }) {
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("access", "Binalbagan_Commercial_MOBILE_Access");
                params.put("type", "16");
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        requestQueue.add(stringRequest);
    }
}
