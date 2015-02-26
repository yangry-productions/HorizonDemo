package com.horizon.demo.horizondemo;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;


public class TransferDetailActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transfer_detail);

        //update layout to show passed in information
        //get bundle from the intent
        Bundle b = getIntent().getExtras().getBundle(Transfer.BUNDLE_NAME);

        TextView tv = (TextView) findViewById(R.id.textView_recipientName);
        tv.setText(b.getString(Transfer.RECIPIENT_NAME));
        tv = (TextView) findViewById(R.id.textView_destinationBank);
        tv.setText(b.getString(Transfer.RECIPIENT_BANK));
        tv = (TextView) findViewById(R.id.textView_account);
        tv.setText(b.getString(Transfer.SENDER_ACCOUNT));
        tv = (TextView) findViewById(R.id.textView_sendingAmount);
        tv.setText(b.getString(Transfer.SEND_AMOUNT));
        tv = (TextView) findViewById(R.id.textView_receivingAmount);
        tv.setText(b.getString(Transfer.RECEIVE_AMOUNT));
        tv = (TextView) findViewById(R.id.textView_confirmationNumber);
        tv.setText(b.getString(Transfer.CONFIRMATION_NUMBER));

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_transfer_detail, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void okButtonSelected(View view){
        finish();
    }
}
