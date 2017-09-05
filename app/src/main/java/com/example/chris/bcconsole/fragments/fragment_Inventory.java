package com.example.chris.bcconsole.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
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

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static com.android.volley.VolleyLog.TAG;

public class fragment_Inventory extends android.support.v4.app.Fragment {
    private String url = MainActivity.url + "product.php";
    private TextView text;
    private String last_product_id = "999999999999999";
    private Button btn_loadmore;
    private int paginate_limit = 5;
    private ScrollView sv_main;
    private ProgressBar pb_bottom;
    private ProgressBar pb_center;
    private ListView lv_main;

    private ArrayList<String> product_list = new ArrayList();
    private ArrayAdapter<String> adapter;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_inventory, container, false);
        btn_loadmore = (Button) view.findViewById(R.id.btn_loadmore);
        pb_bottom = (ProgressBar) view.findViewById(R.id.pb_bottom);
        pb_center = (ProgressBar) view.findViewById(R.id.pb_center);
        lv_main = (ListView) view.findViewById(R.id.lv_main);


        adapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_list_item_1, android.R.id.text1, product_list);
        lv_main.setAdapter(adapter);

        btn_loadmore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                pb_bottom.setVisibility(View.VISIBLE);
                checkItems(last_product_id, paginate_limit);
            }
        });


        checkItems(last_product_id, paginate_limit);
        return view;
    }

    private void checkItems(final String last_id, final int page_limit) {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            String result = "";
                            JSONObject json = new JSONObject(response);
                            String json_counter = json.getString("COUNTER");
                            JSONObject product_counter = new JSONObject(json_counter);


                            int counter = Integer.valueOf(product_counter.getString("COUNTER"));
                            if (counter == 0) {
                                Toast.makeText(getContext(), "Reached Last Data", Toast.LENGTH_SHORT).show();
                            } else {
                                for (int x = 1; x <= counter; x++) {
                                    String json_result = json.getString(String.valueOf(x));
                                    JSONObject product = new JSONObject(json_result);
                                    Boolean status = Boolean.valueOf(product.getString("STATUS"));
                                    if (!status) {
                                        result = product.getString("ID") + "\n" +
                                                product.getString("NAME") + "\n" +
                                                product.getString("CATEGORY") + "\n"
                                        ;
                                        product_list.add(result);
                                        adapter.notifyDataSetChanged();
                                    }

                                    if (x == counter) {
                                        last_product_id = product.getString("ID");
                                    }

                                }
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        Log.e(TAG, "onResponse: " + response);
                        pb_bottom.setVisibility(View.GONE);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, "onErrorResponse: " + error);
                pb_bottom.setVisibility(View.GONE);
            }
        }) {
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("access", "Binalbagan_Commercial_MOBILE_Access");
                params.put("id", last_id);
                params.put("paginate", String.valueOf(page_limit));
                params.put("type", "1");
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        requestQueue.add(stringRequest);

        pb_center.setVisibility(View.GONE);
    }
}
