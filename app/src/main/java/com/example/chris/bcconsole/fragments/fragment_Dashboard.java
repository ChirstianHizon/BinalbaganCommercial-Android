package com.example.chris.bcconsole.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.chris.bcconsole.AdminMainActivity;
import com.example.chris.bcconsole.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import static android.content.ContentValues.TAG;

public class fragment_Dashboard extends android.support.v4.app.Fragment {

    private TextView tv_total_products;
    private TextView tv_total_sales;
    private TextView tv_total_delivery;
    private TextView tv_total_pending;

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
                            tv_total_products.setText(reader.getString("PRODUCT_COUNT"));
                            tv_total_sales.setText("P " + reader.getString("SALES_TOTAL"));
                            tv_total_delivery.setText(reader.getString("DELIVERY_TOTAL"));
                            tv_total_pending.setText(reader.getString("PENDING_COUNT"));
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
}
