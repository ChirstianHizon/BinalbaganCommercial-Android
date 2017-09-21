package com.example.chris.bcconsole.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.chris.bcconsole.Admin.InventoryView;
import com.example.chris.bcconsole.R;
import com.example.chris.bcconsole.classes.Products;
import com.squareup.picasso.Picasso;

/**
 * Created by chris on 05/09/2017.
 */

public class fragment_Inventory_description extends android.support.v4.app.Fragment {

    public View view;
    private Products product;
    private InventoryView activity;
    private TextView name;
    private TextView desc;
    private ImageView image;
    private TextView status;
    private TextView cat;
    private TextView date;

    public fragment_Inventory_description() {

    }

    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_inventory_description, container, false);

        activity = (InventoryView) getActivity();
        product = activity.getProduct();

        name = (TextView) view.findViewById(R.id.prod_name);
        desc = (TextView) view.findViewById(R.id.prod_desc);
        cat = (TextView) view.findViewById(R.id.prod_cat);
        date = (TextView) view.findViewById(R.id.prod_date);
        image = (ImageView) view.findViewById(R.id.iv_image);
        status = (TextView) view.findViewById(R.id.status);

        cat.setText(product.getCategory());
        date.setText(product.getDatestamp());
        if (product.getStatus().equals("1")) {
            status.setVisibility(View.GONE);
        }
        Picasso.with(getContext())
                .load(product.getImage())
                .resize(200, 200)
                .centerCrop()
                .into(image);


        name.setText(product.getName());
        desc.setText(product.getDesc());

        return view;
    }
}
