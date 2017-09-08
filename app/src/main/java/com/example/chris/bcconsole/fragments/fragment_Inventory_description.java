package com.example.chris.bcconsole.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.chris.bcconsole.Inventory_View;
import com.example.chris.bcconsole.R;
import com.example.chris.bcconsole.classes.Products;

/**
 * Created by chris on 05/09/2017.
 */

public class fragment_Inventory_description extends android.support.v4.app.Fragment {

    public View view;
    private Products product;
    private Inventory_View activity;

    public fragment_Inventory_description() {

    }

    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_inventory_descriptions, container, false);

        activity = (Inventory_View) getActivity();
        product = activity.getProduct();

        return view;
    }
}
