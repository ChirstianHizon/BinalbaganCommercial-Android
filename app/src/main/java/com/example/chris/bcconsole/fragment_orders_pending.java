package com.example.chris.bcconsole;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

public class fragment_orders_pending extends android.support.v4.app.Fragment {

    public LinearLayout llorder;
    public View view;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_orders_pending, container, false);

        initOrderTable();

        return view;
    }

    public void initOrderTable(){

        llorder = (LinearLayout)view.findViewById(R.id.ll_order);
        LinearLayout llorderno = (LinearLayout) view.findViewById(R.id.ll_order_no);
        LinearLayout llcustomer = (LinearLayout) view.findViewById(R.id.ll_customer);
        LinearLayout lltype = (LinearLayout) view.findViewById(R.id.ll_type);

        int odd = Color.parseColor("#505556");
        int even = Color.parseColor("#ffffff");


        TextView orderno = new TextView(getContext());
        orderno.setText("000001");
        orderno.setBackgroundColor(odd);
        orderno.setPadding(0,10,0,10);
        orderno.setTextColor(even);

        TextView customer = new TextView(getContext());
        customer.setText("MIKE ADDIDAS");
        customer.setBackgroundColor(odd);
        customer.setPadding(0,10,0,10);
        customer.setTextColor(even);

        TextView type = new TextView(getContext());
        type.setText("WALK-IN");
        type.setBackgroundColor(odd);
        type.setPadding(0,10,0,10);
        type.setTextColor(even);
        type.setGravity(Gravity.RIGHT);


        llorderno.addView(orderno);
        llcustomer.addView(customer);
        lltype.addView(type);
    }

}
