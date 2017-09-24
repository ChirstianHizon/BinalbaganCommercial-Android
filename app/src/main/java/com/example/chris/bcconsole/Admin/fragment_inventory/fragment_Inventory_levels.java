package com.example.chris.bcconsole.Admin.fragment_inventory;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.chris.bcconsole.Admin.InventoryView;
import com.example.chris.bcconsole.R;
import com.example.chris.bcconsole.classes.Products;

/**
 * Created by chris on 05/09/2017.
 */

public class fragment_Inventory_levels extends android.support.v4.app.Fragment {

    public View view;
    private InventoryView activity;
    private Products product;
    private TextView price;
    private TextView level;
    private TextView optimal;
    private TextView warning;

    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_inventory_levels, container, false);
        activity = (InventoryView) getActivity();
        product = activity.getProduct();

        price = (TextView) view.findViewById(R.id.prod_price);
        level = (TextView) view.findViewById(R.id.prod_level);
        optimal = (TextView) view.findViewById(R.id.prod_optimal);
        warning = (TextView) view.findViewById(R.id.prod_warning);

        price.setText(String.valueOf(product.getPrice()));
        level.setText(String.valueOf(product.getLevel()));
        optimal.setText(String.valueOf(product.getOptimal()));
        warning.setText(String.valueOf(product.getWarning()));


        return view;
    }
}
