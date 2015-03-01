package com.horizon.demo.horizondemo;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

public class AccountSummaryActivity extends Activity {

    //global user object
    private User user;
    private ExchangeRateFragment exchangeRate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account_summary);

        //set account summary layout to invisible to hide it while the account is being loaded
        findViewById(R.id.accountSummaryLayout).setVisibility(View.INVISIBLE);

        //get username data from the previous activity
        String username = getIntent().getStringExtra("username");

        //use asyncTask to load the user object linked to the username passed in from the previous activity
        new accessAccount().execute(username);

        //instantiate new exchange rate fragment for use throughout this activity
        exchangeRate = new ExchangeRateFragment();
    }

    @Override
    protected void onResume() {
        super.onResume();

        //TODO: figure out how to refresh the summary page to show any new pending transfers
        //refreshPendingTransfers();
    }

    @Override
    public void onBackPressed() {
        //super.onBackPressed();

        //TODO:sync pending transactions list with network and logout of the user
        //fake logout of the user account
        new AlertDialog.Builder(this)
                .setMessage("Are you sure you want to logout?")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        AccountSummaryActivity.this.finish();
                        Toast.makeText(getApplicationContext(), user.getName() + " logged out", Toast.LENGTH_LONG).show();
                    }
                })
                .setNegativeButton("No", null)
                .show();
    }

    private class accessAccount extends AsyncTask<String, Integer, Boolean>{
        //private ProgressDialog loadingUserProgress = new ProgressDialog(AccountSummaryActivity.this);
        ProgressBar mAccessingAccountProgress = (ProgressBar) findViewById(R.id.accessingAccountProgress);
        TextView mAccessingAccountMessage = (TextView) findViewById(R.id.accessingAccountMessage);

        protected Boolean doInBackground(String... username) {
            //TODO: connect with actual online user database for real app. and implement error catching
            //read in fake credentials from fake credentials resource file
            int Rid = getResources().getIdentifier(username[0], "string", getPackageName());
            String[] pieces = getResources().getString(Rid).split(":");
            //instantiate user object based on read in credentials
            user = new User(pieces[0], Integer.parseInt(pieces[1]), Float.parseFloat(pieces[2]));

            //read in fake recipients list
            Rid = getResources().getIdentifier(username[0] + "_recipients", "array", getPackageName());
            String[] recipients = getResources().getStringArray(Rid);
            for (String recipient : recipients) {
                String[] recipientInfo = recipient.split(":");
                user.addRecipient(new Recipient(recipientInfo[0], recipientInfo[1]));
            }

            //delay thread to simulate fake loading
            int mProgressStatus=0;
            while(mProgressStatus<100){
                try {
                    Thread.sleep(20);
                    mProgressStatus += Math.random()*5;
                    if(mProgressStatus<100)
                        mAccessingAccountProgress.setProgress(mProgressStatus);
                    else if(mProgressStatus>=100) {
                        mAccessingAccountProgress.setProgress(100);
                        Thread.sleep(200);
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

            //no error catching right now, just assumes user loaded correctly and return true.
            return true;
        }
        protected void onPreExecute(){
            //set progress bar in account summary activity to zero
            mAccessingAccountProgress.setProgress(0);
        }
        protected void onPostExecute(Boolean result){
            //dismiss progress bar on account summary activity
            ((LinearLayout)mAccessingAccountProgress.getParent()).removeView(mAccessingAccountProgress);
            ((LinearLayout)mAccessingAccountMessage.getParent()).removeView(mAccessingAccountMessage);

            //populate and show account summary layout
            TextView tempTextView = (TextView)findViewById(R.id.usernameTextView);
            tempTextView.setText(user.getName());
            tempTextView = (TextView)findViewById(R.id.lastSaw2TextView);
            tempTextView.setText("" + user.getLastLogin());  //TODO: the "days ago" is a string resource that does not change depending the number of days. E.g. doesn't change to "day ago" if user last logged in 1 day ago.
            findViewById(R.id.accountSummaryLayout).setVisibility(View.VISIBLE);

            //load today's rate fragment
            refreshExchangeRate();


//            //fake adding pending transfers
//            Transfer newTransfer = new Transfer(user, user.getRecipient(0), 100, 50, getApplicationContext());
//            user.addPendingTransaction(newTransfer);
//            newTransfer = new Transfer(user, user.getRecipient(1), 400, 50, getApplicationContext());
//            user.addPendingTransaction(newTransfer);
//            newTransfer = new Transfer(user, user.getRecipient(2), 200, 50, getApplicationContext());
//            user.addPendingTransaction(newTransfer);



            //populate list of pending transfers from user object
            refreshPendingTransfers();
        }
    }

    private void refreshExchangeRate() {
        getFragmentManager()
                .beginTransaction()
                //.add(R.id.exchangeRateContainer, new ExchangeRateFragment())
                .add(R.id.exchangeRateContainer, exchangeRate)
                .commit();
    }

    private void refreshPendingTransfers(){
        if(user.getNumPendingTransfers()>0) {
            findViewById(R.id.linearLayout_noPendingTransfersMsg).setVisibility(View.INVISIBLE);

            //try using array adapter to make this work
            TransferArrayAdapter adapter = new TransferArrayAdapter(this, R.layout.fragment_pending_transfer, user.getPendingTransferList());
            ListView listView = (ListView) findViewById(R.id.listView_pendingTransfers);
            listView.setAdapter(adapter);
        }
        else if(user.getNumPendingTransfers()==0){
            //if no pending transfers, ensure that the noPendingTransferMsg layout is shown
            findViewById(R.id.linearLayout_noPendingTransfersMsg).setVisibility(View.VISIBLE);
        }

    }


    public void startNewTransfer(View v){
        //create temporary objects to hold all information user will enter
        final Recipient[] newRecipient = new Recipient[1];
        final Double newRate = ExchangeRateFragment.exchangeRate;
        final Double[] newSendingAmount = new Double[1];
        //create temporary transfer object
        final Transfer[] newTransfer = new Transfer[1];

        //build list of recipient names that are registered with the current user
        String[] recipients = new String[user.getNumRecipients()];
        for(int r=0; r<user.getNumRecipients(); r++)
            recipients[r] = user.getRecipient(r).getRecipientName();

        //get an inflater to help inflate layouts used for the dialog boxes
        LayoutInflater inflater = getLayoutInflater();

        //build the dialog box to take in the user's payment information
        final View paymentOptionsView = inflater.inflate(R.layout.dialog_payment_options, null);
        final AlertDialog paymentOptionDialog = new AlertDialog.Builder(this)
                .setTitle(getResources().getString(R.string.paymentOptionDialogTitle))
                .setMessage(getResources().getString(R.string.paymentOptionDialogMsg))
                .setView(paymentOptionsView)
                .setCancelable(false)
                .setPositiveButton("CONTINUE", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //TODO: connect with actual payment method. e.g online interac or credit card
                        //create new Transfer object and add to the user's pending transfers list
                        newTransfer[0] = new Transfer(user, newRecipient[0], newSendingAmount[0], newRate, getApplicationContext());
                        user.addPendingTransaction(newTransfer[0]);

                        //refresh the pending transfers layout
                        refreshPendingTransfers();

                        //start transfer summary activity and pass in the new transfer information for display
                        Intent intent = new Intent(getApplicationContext(), TransferDetailActivity.class);
                        intent.putExtra(Transfer.BUNDLE_NAME, newTransfer[0].toBundle());
                        startActivity(intent);
                    }
                })
                .create();


        //build the dialog box that shows the transfer terms and asks the user to agree
        final AlertDialog transferTermsDialog = new AlertDialog.Builder(this)
                .setTitle(getResources().getString(R.string.transferTermsDialogTitle))
                .setMessage(getResources().getString(R.string.transferTerms))
                .setCancelable(false)
                .setPositiveButton("AGREE", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //proceed and show the payment method dialog
                        paymentOptionDialog.show();
                    }
                })
                .setNegativeButton("DECLINE", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //TODO: clean up temporary objects and cancel the transfer if user declines the terms. Currently, if the transfer is canceled and tries to start a new transfer, the app crashes
                    }
                })
                .create();

        //build the dialog box that asks user how much money to send
        final View inputSendingAmountView = inflater.inflate(R.layout.dialog_sending_amount, null);
        //update and add the exchange rate info to the dialog box asking user to enter sending amount
//        getFragmentManager()
//                .beginTransaction()
//                .add(R.id.linearLayout_dialogExchangeRateContainer, new ExchangeRateFragment())
//                .commit();
//
//        AlertDialog.Builder builder = new AlertDialog.Builder(this)
//                .setTitle(getResources().getString(R.string.howMuchDialogTitle))
//                .setCancelable(false)
//                .setView(inputSendingAmount)
//                .setPositiveButton("NEXT", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//                        //do nothing, onClickListener is overridden to check that user actually entered an amount
//                    }
//                });
//        final AlertDialog askSendingAmountDialog = builder.create();
//
//        FrameLayout fl = (FrameLayout) askSendingAmountDialog.findViewById(android.R.id.custom);
//        fl.addView(inputSendingAmount, new WindowManager.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
//
//

        final AlertDialog askSendingAmountDialog = new AlertDialog.Builder(this)
            .setTitle(getResources().getString(R.string.howMuchDialogTitle))
            .setCancelable(false)
            .setView(inputSendingAmountView)
            .setPositiveButton("NEXT", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    //do nothing, onClickListener is overridden to check that user actually entered an amount
                }
            })
            .create();

        //when the ask sending amount dialog shows, check that the user entered an amount
        askSendingAmountDialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialog) {
                Button b = askSendingAmountDialog.getButton(DialogInterface.BUTTON_POSITIVE);
                b.setOnClickListener(new View.OnClickListener(){
                    @Override
                    public void onClick(View view){
                        EditText temp = (EditText) inputSendingAmountView.findViewById(R.id.editText_sendingAmount);
                        //check if user entered anything
                        if (temp.getText().toString().isEmpty())
                            //Show error message and do nothing
                            Toast.makeText(getApplicationContext(), "No Amount Entered", Toast.LENGTH_LONG).show();
                        else {
                            newSendingAmount[0] = Double.parseDouble(temp.getText().toString());
                            askSendingAmountDialog.dismiss();
                            transferTermsDialog.show();
                        }
                    }
                });
            }
        });

        //build the dialog box that asks user to select recipient from list
        AlertDialog chooseRecipientDialog = new AlertDialog.Builder(this)
            .setCancelable(false)
            .setTitle(getResources().getString(R.string.selectRecipientsDialogTitle))
            .setItems(recipients, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    newRecipient[0] = user.getRecipient(which);
                    //show the dialog box to ask user how much money to send
                    askSendingAmountDialog.show();
                }
            })
            .create();
        chooseRecipientDialog.show();
    }
}
