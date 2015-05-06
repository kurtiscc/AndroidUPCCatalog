package com.example.kurtiscc.upccatalog;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by kurtiscc on 2/5/2015.
 */
public class UPCCatalogAdapter extends ArrayAdapter<UPCData> {
    private Context context;
    private int resource;
    private ArrayList<UPCData> UPCData = new ArrayList<UPCData>();

    public UPCCatalogAdapter(Context context, int resource, ArrayList<UPCData> UPCData) {
        super(context, resource, UPCData);
        this.context = context;
        this.resource = resource;
        this.UPCData = UPCData;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row = convertView;
        UPCDataHolder holder = null;

        if(row == null)
        {
            LayoutInflater inflater = ((Activity)context).getLayoutInflater();
            row = inflater.inflate(resource, parent, false);
            holder = new UPCDataHolder();
            holder.id = (TextView) row.findViewById(R.id.list_id);
            holder.upccode = (TextView) row.findViewById(R.id.list_upccode);
            holder.productname = (TextView) row.findViewById(R.id.list_product);
            holder.image = (TextView) row.findViewById(R.id.list_image);

            row.setTag(holder);
        }
        else
        {
            holder = (UPCDataHolder)row.getTag();
        }

        UPCData upc = UPCData.get(position);
        holder.upccode.setText("UPC Code: " + String.valueOf(upc.getUpccode()));
        holder.productname.setText("Product: " + String.valueOf(upc.getProductname()));
        holder.image.setText("Image: " + String.valueOf(upc.getImage()));


        return row;
    }
    static class UPCDataHolder{
        TextView id, upccode, productname, image;
    }
}




