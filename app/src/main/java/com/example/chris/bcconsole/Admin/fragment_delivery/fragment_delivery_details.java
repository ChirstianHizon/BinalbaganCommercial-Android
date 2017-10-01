package com.example.chris.bcconsole.Admin.fragment_delivery;

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
import com.example.chris.bcconsole.Admin.DeliveryView;
import com.example.chris.bcconsole.AdminMainActivity;
import com.example.chris.bcconsole.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by chris on 05/09/2017.
 */

public class fragment_delivery_details extends android.support.v4.app.Fragment {

    public View view;
    private TextView delid;
    private TextView date;
    private TextView products;
    private TextView amount;
    private TextView custname;
    private TextView custaddress;
    private TextView custnotes;
    private DeliveryView activity;
    private String order_id;
    private TextView custcontact;

    public fragment_delivery_details() {

    }

    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_delivery_details, container, false);

        delid = (TextView)view.findViewById(R.id.id);
        date = (TextView)view.findViewById(R.id.date);
        products = (TextView)view.findViewById(R.id.products);
        amount = (TextView)view.findViewById(R.id.amount);
        custname = (TextView)view.findViewById(R.id.custname);
        custaddress = (TextView)view.findViewById(R.id.custaddress);
        custnotes = (TextView)view.findViewById(R.id.custnotes);
        custcontact = (TextView)view.findViewById(R.id.custcontact);

        activity = (DeliveryView) getActivity();
        order_id = activity.getOrder_id();

        initializeDeliveryDetails(order_id);

        return view;
    }

    private void initializeDeliveryDetails(final String id) {

        StringRequest stringRequest = new StringRequest(Request.Method.POST, AdminMainActivity.url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("QUERY", response);
                        try {
                            JSONObject details = new JSONObject(response);

                            delid.setText(details.getString("DELID"));
                            date.setText(details.getString("ORDER_DATE"));
                            products.setText(details.getString("AMOUNT"));
                            amount.setText(details.getString("TOTAL"));
                            custname.setText(details.getString("CUST_NAME"));
                            custaddress.setText(details.getString("CUST_ADDRESS"));
                            custnotes.setText(details.getString("CUST_NOTES"));
                            custcontact.setText(details.getString("CUST_CONTACT"));

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
                params.put("id", id);
                params.put("type", "11");
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        requestQueue.add(stringRequest);
    }
}
