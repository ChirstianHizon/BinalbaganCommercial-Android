package com.example.chris.bcconsole.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.chris.bcconsole.R;

/**
 * Created by chris on 05/09/2017.
 */

public class fragment_Inventory_levels extends android.support.v4.app.Fragment {

    public View view;

    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_inventory_levels, container, false);

        return view;
    }
}
