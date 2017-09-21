package com.example.chris.bcconsole.adapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.chris.bcconsole.Admin.InventoryView;
import com.example.chris.bcconsole.R;
import com.example.chris.bcconsole.classes.Products;

import java.util.List;

/**
 * Created by chris on 05/09/2017.
 */

public class InventoryListAdapter extends ArrayAdapter<Products> {

    Context context;

    public InventoryListAdapter(Context context, int resourceId,
                                List<Products> items) {
        super(context, resourceId, items);
        this.context = context;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        final Products products = getItem(position);

        LayoutInflater mInflater = (LayoutInflater) context
                .getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.list_inventory, null);
            holder = new ViewHolder();

            holder.relRow = (RelativeLayout) convertView.findViewById(R.id.row);
            holder.txtId = (TextView) convertView.findViewById(R.id.id);
            holder.txtDesc = (TextView) convertView.findViewById(R.id.desc);
            holder.txtTitle = (TextView) convertView.findViewById(R.id.title);
            holder.imageView = (ImageView) convertView.findViewById(R.id.icon);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.relRow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(getContext(), InventoryView.class);
                intent.putExtra("ID", String.valueOf(products.getId()));
                context.startActivity(intent);
            }
        });

        holder.txtId.setText(String.valueOf(products.getId()));
        holder.txtTitle.setText(products.getName());
        holder.txtDesc.setText(products.getCategory());
        return convertView;
    }

    /*private view holder class*/
    private class ViewHolder {
        ImageView imageView;
        TextView txtTitle;
        TextView txtId;
        TextView txtDesc;
        RelativeLayout relRow;
    }

}
