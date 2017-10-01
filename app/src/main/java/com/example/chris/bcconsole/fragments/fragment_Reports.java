package com.example.chris.bcconsole.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.chris.bcconsole.R;
import com.example.chris.bcconsole.Admin.Reports.DatePicker_Start;

public class fragment_Reports extends android.support.v4.app.Fragment {

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_reports, container, false);

        Button btninventory = (Button)view.findViewById(R.id.btn_inventory);
        Button btnsales = (Button)view.findViewById(R.id.btn_sales);
        Button btndelivery = (Button)view.findViewById(R.id.btn_delivery);




        btndelivery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(getContext(), DatePicker_Start.class);
                intent.putExtra("Report_Type","delivery");
                startActivity(intent);


            }
        });

        btnsales.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(getContext(), DatePicker_Start.class);
                intent.putExtra("Report_Type","sales");
                startActivity(intent);

            }
        });

        btninventory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(getContext(), DatePicker_Start.class);
                intent.putExtra("Report_Type","inventory");
                startActivity(intent);


            }
        });

        return view;
    }
}
