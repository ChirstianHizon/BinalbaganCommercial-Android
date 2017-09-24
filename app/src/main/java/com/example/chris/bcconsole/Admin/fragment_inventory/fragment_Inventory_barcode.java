package com.example.chris.bcconsole.Admin.fragment_inventory;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.chris.bcconsole.Admin.InventoryView;
import com.example.chris.bcconsole.AdminMainActivity;
import com.example.chris.bcconsole.R;
import com.example.chris.bcconsole.classes.Products;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static android.content.ContentValues.TAG;

/**
 * Created by chris on 05/09/2017.
 */

public class fragment_Inventory_barcode extends android.support.v4.app.Fragment {

    public View view;
    private Products product;
    private InventoryView activity;
    private List<String> barcodeCodes;
    private ArrayAdapter adapter;
    private ListView lv_barcode;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private String product_Id;
    private AdminMainActivity main;

    public fragment_Inventory_barcode() {

    }

    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_inventory_barcode, container, false);

        activity = (InventoryView) getActivity();
//        main = (AdminMainActivity) getActivity();
        product_Id = activity.getID();

        barcodeCodes = new ArrayList<String>();

        lv_barcode = (ListView) view.findViewById(R.id.lv_barcode);

        adapter = new ArrayAdapter<String>(getContext(),
                R.layout.list_barcode, barcodeCodes);

        lv_barcode.setAdapter(adapter);
        initializeBarcodeDetails(product_Id);

        mSwipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipeToRefresh);
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                adapter.clear();
                initializeBarcodeDetails(product_Id);
                mSwipeRefreshLayout.setRefreshing(false);
            }
        });

        return view;
    }

    public void initializeBarcodeDetails(final String id) {

        StringRequest stringRequest = new StringRequest(Request.Method.POST, AdminMainActivity.url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("PRODUCT: ", response);
                        try {
                            JSONObject reader = new JSONObject(response);
                            int total = reader.getInt("COUNTER");
                            for (int x = 1; total >= x; x++) {
                                JSONObject code = new JSONObject(reader.getString(String.valueOf(x)));
                                barcodeCodes.add(code.getString("CODE"));
                            }

                            if (total == 0) {
                                barcodeCodes.add("No Barcode Available");
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
                params.put("type", "5");
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        requestQueue.add(stringRequest);

    }
}
