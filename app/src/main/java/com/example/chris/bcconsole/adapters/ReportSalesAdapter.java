package com.example.chris.bcconsole.adapters;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.chris.bcconsole.Objects.Reports;
import com.example.chris.bcconsole.R;

import java.util.List;

/**
 * Created by chris on 05/09/2017.
 */

public class ReportSalesAdapter extends ArrayAdapter<Reports> {

    Context context;

    public ReportSalesAdapter(Context context, int resourceId,
                              List<Reports> items) {
        super(context, resourceId, items);
        this.context = context;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        final Reports reports = getItem(position);

        LayoutInflater mInflater = (LayoutInflater) context
                .getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.list_report_sales_list, null);
            holder = new ViewHolder();

            holder.relRow = (RelativeLayout) convertView.findViewById(R.id.row);
            holder.txtId = (TextView) convertView.findViewById(R.id.id);
            holder.txtTitle = (TextView) convertView.findViewById(R.id.title);
            holder.txtDate = (TextView) convertView.findViewById(R.id.date);
            holder.txtQty = (TextView) convertView.findViewById(R.id.quantity);
            holder.txtEmployee = (TextView) convertView.findViewById(R.id.employee);
            holder.txtSubtotal = (TextView) convertView.findViewById(R.id.subtotal);



            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

//        holder.relRow.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//                Intent intent = new Intent(getContext(), DeliveryView.class);
//                intent.putExtra("ID", String.valueOf(reports.getId()));
//                context.startActivity(intent);
//            }
//        });
//        if(reports.getStatus().equals("100")){
//            holder.txtTitle.setTextColor(Color.GREEN);
//        }

        switch (reports.getType()){
            case "IN":

                break;
            case "OUT":
                holder.txtTitle.setTextColor(Color.RED);
                break;

        }

        holder.txtId.setText(String.valueOf(reports.getId()));
        holder.txtTitle.setText(reports.getType());
        holder.txtDate.setText(reports.getDatestamp());
        holder.txtQty.setText(reports.getQty());
        holder.txtEmployee.setText(reports.getUser());
        holder.txtSubtotal.setText(reports.getTotal());


        return convertView;
    }

    /*private view holder class*/
    private class ViewHolder {
        TextView txtTitle;
        TextView txtId;
        RelativeLayout relRow;
        TextView txtDate;
        TextView txtType;
        TextView txtQty;
        TextView txtEmployee;
        TextView txtSubtotal;
    }

}
