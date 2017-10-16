package com.example.chris.bcconsole.Admin.fragment_inventory;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.chris.bcconsole.Admin.CRUD.Delete_Product;
import com.example.chris.bcconsole.Admin.CRUD.Update_Product;
import com.example.chris.bcconsole.Admin.InventoryView;
import com.example.chris.bcconsole.Objects.Products;
import com.example.chris.bcconsole.R;
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
    private Button btnupdate;
    private Button btndelete;

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

        btnupdate = (Button)view.findViewById(R.id.btn_update);
        btndelete = (Button)view.findViewById(R.id.btn_delete);

        btnupdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), Update_Product.class);
                intent.putExtra("ID",product.getId());
                startActivity(intent);
            }
        });

        btndelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), Delete_Product.class);
                intent.putExtra("ID",product.getId());
                startActivity(intent);

            }
        });

        cat.setText(product.getCategory());
        date.setText(product.getDatestamp());
        if (product.getStatus().equals("1")) {
            status.setVisibility(View.GONE);
        }
//        Toast.makeText(activity, product.getImage(), Toast.LENGTH_SHORT).show();
        Log.d("IMAGE_URL",product.getImage());

        Picasso.with(getContext())
                .load(product.getImage())
                .placeholder(R.drawable.logo)
                .error(R.drawable.logo)
                .resize(200, 200)
                .centerCrop()
                .into(image);

        name.setText(product.getName());
        desc.setText(product.getDesc());

        return view;
    }
}
