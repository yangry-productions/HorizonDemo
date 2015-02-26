package com.horizon.demo.horizondemo;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class ExchangeRateFragment extends Fragment {

    public static Double exchangeRate;

    public ExchangeRateFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //get exchange rate once on create.
        exchangeRate = updateExchangeRate();
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_exchange_rate, container, false);
    }


    @Override
    public void onStart(){
        super.onStart();

        //update exchange rate view
        refreshExchangeRateView();
    }

    public double getCurrentExchangeRate(){
        return exchangeRate;
    }

    private void refreshExchangeRateView(){
        //update date
        SimpleDateFormat date = new SimpleDateFormat("yyyy-MM-dd");
        TextView tempTextView = (TextView)getView().findViewById(R.id.exchangeRateDateTextView);
        tempTextView.setText("(" + date.format(Calendar.getInstance().getTime()) + ")");

        //update rate TODO: connect with real system to get current rate
        tempTextView = (TextView)getView().findViewById(R.id.exchangeRateTextView);
        tempTextView.setText(String.format("%.2f", exchangeRate));
    }

    //TODO: implement actual method to connect to online system to get latest exchange rate
    private Double updateExchangeRate() {
        return Double.parseDouble(getResources().getString(R.string.exchangeRate));
    }
}
