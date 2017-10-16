package com.example.chris.bcconsole.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.example.chris.bcconsole.Objects.Orders;
import com.example.chris.bcconsole.R;
import com.example.chris.bcconsole.adapters.DashboardPendOrderAdapter;

import java.util.ArrayList;

public class fragment_orders_pending extends android.support.v4.app.Fragment {

    public LinearLayout llorder;
    public View view;
    private ArrayList<Orders> order_list;
    private ListView lv_main;
    private DashboardPendOrderAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_orders_pending, container, false);

        return view;
    }


}
