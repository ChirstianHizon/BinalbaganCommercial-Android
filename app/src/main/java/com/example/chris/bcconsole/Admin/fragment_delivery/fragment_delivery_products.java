package com.example.chris.bcconsole.Admin.fragment_delivery;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.chris.bcconsole.Admin.DeliveryView;
import com.example.chris.bcconsole.AdminMainActivity;
import com.example.chris.bcconsole.R;
import com.example.chris.bcconsole.adapters.OrderListAdapter;
import com.example.chris.bcconsole.Objects.Products;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static android.content.ContentValues.TAG;

/**
 * Created by chris on 05/09/2017.
 */

public class fragment_delivery_products extends android.support.v4.app.Fragment {

    public View view;
    private Products product;
    private DeliveryView activity;
    private TextView name;
    private TextView desc;
    private ImageView image;
    private TextView status;
    private TextView cat;
    private TextView date;
    private String order_id;
    private ListView lv_main;
    private ArrayList<Products> orderList;
    private OrderListAdapter adapter;
    private SwipeRefreshLayout mSwipeRefreshLayout;

    public fragment_delivery_products() {

    }

    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_delivery_products, container, false);

        activity = (DeliveryView) getActivity();
        order_id = activity.getOrder_id();

        orderList = new ArrayList<Products>();

        lv_main = (ListView) view.findViewById(R.id.lv_main);

        adapter = new OrderListAdapter(getContext(),
                R.layout.list_order_list, orderList);

        lv_main.setAdapter(adapter);
        initializeOrderList(order_id);

        mSwipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipeToRefresh);
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                adapter.clear();
                initializeOrderList(order_id);
                mSwipeRefreshLayout.setRefreshing(false);
            }
        });
        return view;
    }


    public void initializeOrderList(final String id) {

        StringRequest stringRequest = new StringRequest(Request.Method.POST, AdminMainActivity.url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("QUERY: ", response);
                        try {
                            JSONObject reader = new JSONObject(response);
                            int total = reader.getInt("COUNTER");
                            for (int x = 1; total >= x; x++) {
                                JSONObject code = new JSONObject(reader.getString(String.valueOf(x)));
                                orderList.add(
                                        new Products(
                                                code.getString("PRDID"),
                                                code.getString("NAME"),
                                                code.getString("PRICE"),
                                                code.getString("QTY")
                                        ));
                            }
                            adapter.notifyDataSetChanged();
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
                params.put("type", "12");
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        requestQueue.add(stringRequest);

    }
}
