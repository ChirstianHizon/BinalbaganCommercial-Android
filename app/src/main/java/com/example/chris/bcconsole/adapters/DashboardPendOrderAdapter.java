package com.example.chris.bcconsole.adapters;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.chris.bcconsole.Objects.Orders;
import com.example.chris.bcconsole.R;

import java.util.List;

/**
 * Created by chris on 05/09/2017.
 */

public class DashboardPendOrderAdapter extends ArrayAdapter<Orders> {

    Context context;

    public DashboardPendOrderAdapter(Context context, int resourceId,
                                List<Orders> items) {
        super(context, resourceId, items);
        this.context = context;
    }

    @NonNull
    public View getView(int position, View convertView, @NonNull ViewGroup parent) {
        ViewHolder holder = null;
        final Orders orders = getItem(position);

        LayoutInflater mInflater = (LayoutInflater) context
                .getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.list_inventory, null);
            holder = new ViewHolder();

            holder.relRow = (RelativeLayout) convertView.findViewById(R.id.row);
            holder.txtId = (TextView) convertView.findViewById(R.id.id);
            holder.txtDesc = (TextView) convertView.findViewById(R.id.desc);
            holder.txtTitle = (TextView) convertView.findViewById(R.id.title);
            holder.txtStatus = (TextView) convertView.findViewById(R.id.status);
            holder.imageView = (ImageView) convertView.findViewById(R.id.icon);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

//      Orders(String id,String orderid,String datestamp,String customer,String status,String qty)
        assert orders != null;
        holder.txtId.setText(String.valueOf(orders.getId()));
        holder.txtTitle.setText(orders.getCustomer());
        holder.txtDesc.setText(orders.getTotal());
        holder.txtStatus.setText(orders.getDatestamp());
        return convertView;
    }

    /*private view holder class*/
    private class ViewHolder {
        ImageView imageView;
        TextView txtTitle;
        TextView txtId;
        TextView txtDesc;
        RelativeLayout relRow;
        TextView txtStatus;
    }

}
