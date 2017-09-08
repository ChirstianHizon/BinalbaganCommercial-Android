package com.example.chris.bcconsole.fragments;

import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.chris.bcconsole.MainActivity;
import com.example.chris.bcconsole.R;
import com.example.chris.bcconsole.adapters.InventoryListAdapter;
import com.example.chris.bcconsole.classes.Products;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.android.volley.VolleyLog.TAG;

public class fragment_Inventory extends android.support.v4.app.Fragment {

    public static Boolean viewDeleted = false;
    private TextView text, status;
    private String last_product_id = "999999999999999";
    private Button btn_loadmore;
    private int paginate_limit = 8;
    private ScrollView sv_main;
    private ProgressBar pb_bottom;
    private ProgressBar pb_center;
    private ListView lv_main;
    private InventoryListAdapter adapter;
    private List<Products> product_list;
    private boolean isLoading = false;
    private boolean flag_loading;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private ConstraintLayout loading;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_inventory, container, false);

        pb_center = (ProgressBar) view.findViewById(R.id.pb_center);
        lv_main = (ListView) view.findViewById(R.id.lv_main);
        loading = (ConstraintLayout) view.findViewById(R.id.loading);
        status = (TextView) view.findViewById(R.id.status);

        product_list = new ArrayList<Products>();

        adapter = new InventoryListAdapter(getContext(), R.layout.list_inventory, product_list);
        lv_main.setAdapter(adapter);

        checkItems(last_product_id, paginate_limit);

        mSwipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipeToRefresh);
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                adapter.clear();
                checkItems("999999999", paginate_limit);
                mSwipeRefreshLayout.setRefreshing(false);
            }
        });

        lv_main.setOnScrollListener(new AbsListView.OnScrollListener() {

            public void onScrollStateChanged(AbsListView view, int scrollState) {
            }

            public void onScroll(AbsListView view, int firstVisibleItem,
                                 int visibleItemCount, int totalItemCount) {

                if (firstVisibleItem + visibleItemCount == totalItemCount && totalItemCount != 0) {
                    if (flag_loading == false) {
                        status.setText("Loading.....");
                        loading.setVisibility(View.VISIBLE);
                        flag_loading = true;
                        Handler handler = new Handler();
                        handler.postDelayed(new Runnable() {
                            public void run() {
                                checkItems(last_product_id, paginate_limit);
                            }
                        }, 2000);
                    }
                }
            }
        });


        return view;
    }

    private void checkItems(final String last_id, final int page_limit) {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, MainActivity.url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject json = new JSONObject(response);
                            String json_counter = json.getString("COUNTER");
                            JSONObject product_counter = new JSONObject(json_counter);


                            int counter = Integer.valueOf(product_counter.getString("COUNTER"));
                            if (counter == 0) {
                                Toast.makeText(getContext(), "Reached Last Data", Toast.LENGTH_SHORT).show();
                                last_product_id = "0";
                            } else {
                                for (int x = 1; x <= counter; x++) {
                                    String json_result = json.getString(String.valueOf(x));
                                    JSONObject product = new JSONObject(json_result);
                                    Boolean status = Boolean.valueOf(product.getString("STATUS"));
                                    if (!status) {
                                        Log.d(TAG, product.getString("ID") + " | " +
                                                product.getString("NAME") + " | " +
                                                product.getString("CATEGORY") + " | " +
                                                product.getString("STATUS") + " | " +
                                                Boolean.valueOf(Integer.valueOf(product.getString("AVAIL")) == 1)
                                        );

                                        if (Boolean.valueOf(Integer.valueOf(product.getString("AVAIL")) == 1) || viewDeleted) {
                                            product_list.add(
                                                    new Products(
                                                            product.getString("ID"),
                                                            product.getString("NAME"),
                                                            product.getString("CATEGORY"),
                                                            product.getString("STATUS")
                                                    ));
                                            adapter.notifyDataSetChanged();
                                        }

                                    }

                                    if (x == counter) {
                                        last_product_id = product.getString("ID");
                                    }

                                }
                                flag_loading = false;
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        Log.e(TAG, "onResponse: " + response);
                        loading.setVisibility(View.GONE);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, "onErrorResponse: " + error);
                loading.setVisibility(View.VISIBLE);
                status.setText("ERROR: Unable to Connect to Server");
            }
        }) {
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("access", "Binalbagan_Commercial_MOBILE_Access");
                params.put("id", last_id);
                params.put("paginate", String.valueOf(page_limit));
                params.put("type", "2");
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        requestQueue.add(stringRequest);

        pb_center.setVisibility(View.GONE);
    }
}
