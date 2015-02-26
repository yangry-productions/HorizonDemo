package com.horizon.demo.horizondemo;

/**
 * Created by Yang on 08/02/2015.
 */
public class Recipient {
    public String recipientName;
    public String recipientBank;

    public Recipient(String mRecipientName, String mRecipientBank){
        recipientName = mRecipientName;
        recipientBank = mRecipientBank;
    }

    public String getRecipientName(){
        return recipientName;
    }
    public String getRecipientBank(){
        return recipientBank;
    }
}
