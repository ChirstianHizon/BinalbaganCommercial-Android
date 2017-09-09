package com.example.chris.bcconsole.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.chris.bcconsole.InventoryView;
import com.example.chris.bcconsole.R;
import com.example.chris.bcconsole.classes.Products;

/**
 * Created by chris on 05/09/2017.
 */

public class fragment_Inventory_barcode extends android.support.v4.app.Fragment {

    public View view;
    private Products product;
    private InventoryView activity;

    public fragment_Inventory_barcode() {

    }

    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_inventory_barcode, container, false);

        activity = (InventoryView) getActivity();
        product = activity.getProduct();

        return view;
    }
}
