package com.horizon.demo.horizondemo;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

/**
 * Created by Yang on 21/02/2015.
 */
public class TransferArrayAdapter extends ArrayAdapter<Transfer> {
    private Context context;
    private List<Transfer> objects;

    public TransferArrayAdapter(Context context, int resource, List<Transfer> objects){
        super(context, resource, objects);
        this.context = context;
        this.objects = objects;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Transfer transfer = objects.get(position);
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.fragment_pending_transfer, null);

        //update the textViews in the layout with information from the current Transfer object
        //update estimated time of delivery
        TextView tv = (TextView) view.findViewById(R.id.pendingTransferTimeTextView1);
        tv.setText(transfer.getEstimatedDelivery());
        //update estimated local delivery time
        tv = (TextView) view.findViewById(R.id.pendingTransferTimeTextView2);
        tv.setText(transfer.getEstimatedLocalDeliveryTime());
        //update recipient name
        tv = (TextView) view.findViewById(R.id.pendingTransferRecipientTextView);
        tv.setText(transfer.getRecipient());
        //update sending amount
        tv = (TextView) view.findViewById(R.id.pendingTransferAmountTextView);
        tv.setText(String.format("%.2f",transfer.getSendingAmount()));
        
        return view;
    }
}
