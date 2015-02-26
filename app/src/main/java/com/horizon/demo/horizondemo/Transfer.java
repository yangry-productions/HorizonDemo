package com.horizon.demo.horizondemo;

import android.content.Context;
import android.os.Bundle;

import java.util.Calendar;

/**
 * Created by Yang on 08/02/2015.
 * Encapsulate each money transaction in a respective Transaction object
 */
public class Transfer {

    //constants for field references
    public static final String BUNDLE_NAME = "transferDetails";
    public static final String RECIPIENT_NAME = "recipientName";
    public static final String RECIPIENT_BANK = "recipientBank";
    public static final String SENDER_ACCOUNT = "senderAccount";
    public static final String SEND_AMOUNT = "sendAmount";
    public static final String RECEIVE_AMOUNT = "receiveAmount";
    public static final String CONFIRMATION_NUMBER = "confirmationNumber";

    //class fields
    public static int id;
    public final User sender;
    public final Recipient recipient;
    public final double rate;
    public final double sendingAmount;
    public final double receivingAmount;
    public final String confirmationNumber;
    public Calendar transactionTime;
    public Calendar estimatedDeliveryTime;
    public Calendar estimatedLocalDeliveryTime;



    //constructor
    public Transfer(User mSender, Recipient mRecipient, double mSendingAmount, double mRate, Context myContext){
        //increment unique transaction id
        id++;

        //capture passed in objects
        sender = mSender;
        recipient = mRecipient;
        sendingAmount = mSendingAmount;
        rate = mRate;
        //calculate receiving amount
        receivingAmount = sendingAmount*rate;

        //capture transfer creation time
        transactionTime = Calendar.getInstance();

        //fake estimate delivery time by rolling creation time by 4 hours
        estimatedDeliveryTime = Calendar.getInstance();
        estimatedDeliveryTime.roll(Calendar.HOUR_OF_DAY, 4);

        //fake local delivery time by rolling creation time by the time difference: 13 hours
        estimatedLocalDeliveryTime = Calendar.getInstance();
        estimatedLocalDeliveryTime.roll(Calendar.HOUR_OF_DAY, 17);

        //generate confirmation number
        confirmationNumber = myContext.getResources().getStringArray(R.array.fakeConfirmationNumber)[5];
    }

    public int getId(){
        return id;
    }

    public String getSender(){
        return sender.getName();
    }

    public String getRecipient(){
        return recipient.getRecipientName();
    }

    public String getDestinationBank(){
        return recipient.getRecipientBank();
    }

    public double getRate(){
        return rate;
    }

    public double getSendingAmount(){
        return sendingAmount;
    }

    public double getReceivingAmount(){
        return receivingAmount;
    }

    public String getConfirmationNumber(){
        return confirmationNumber;
    }

    public String getEstimatedDelivery(){
        String remainingTime;
        //calculate remaining time from estimated delivery time and current time
//        Calendar currentTime = Calendar.getInstance();
//        if(estimatedDeliveryTime.compareTo(currentTime)>0)
//            remainingTime = String.format("%02d", estimatedDeliveryTime.get(Calendar.HOUR_OF_DAY))
//                    + String.format(":%02d", estimatedDeliveryTime.get(Calendar.MINUTE));
//        else
//            remainingTime = "error";

        //TODO: properly calculate delivery time in number of hours and minutes remaining. For now, just show the time of estimated delivery
        remainingTime = String.format("%02d", estimatedDeliveryTime.get(Calendar.HOUR_OF_DAY))
                    + String.format(":%02d", estimatedDeliveryTime.get(Calendar.MINUTE));

        return remainingTime;
    }

    public String getEstimatedLocalDeliveryTime(){
        String localTime;
        localTime = String.format("%02d", estimatedLocalDeliveryTime.get(Calendar.HOUR_OF_DAY))
                + String.format(":%02d", estimatedLocalDeliveryTime.get(Calendar.MINUTE));
        return localTime;
    }


    public Bundle toBundle() {
        Bundle b = new Bundle();
        b.putString(RECIPIENT_NAME, this.recipient.getRecipientName());
        b.putString(RECIPIENT_BANK, this.recipient.getRecipientBank());
        b.putString(SENDER_ACCOUNT, "XXXX-XXX-XXXXXXXXX"); //TODO: actually pull correct account info based on user's chosen payment method
        b.putString(SEND_AMOUNT, String.format("%.2f", this.sendingAmount));
        b.putString(RECEIVE_AMOUNT, String.format("%.2f", this.receivingAmount));
        b.putString(CONFIRMATION_NUMBER, this.getConfirmationNumber());

        return b;
    }
}
