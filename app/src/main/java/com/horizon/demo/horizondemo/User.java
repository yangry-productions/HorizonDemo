package com.horizon.demo.horizondemo;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Yang on 08/02/2015.
 * Encapsulates the user object
 */
public class User {

    public String displayName;
    public int lastLogin;   //TODO: int for demo app. Change to actual date format for real app
    public float balance;
    public List<Recipient> recipient = new ArrayList<>();
    public List<Transfer> pendingTransfer = new ArrayList<>();

    public User(String mDisplayName, int mLastLogin, float mBalance){
        displayName = mDisplayName;
        lastLogin=mLastLogin;
        balance = mBalance;
    }


    public String getName(){
        return displayName;
    }
    public int getLastLogin(){
        return lastLogin;
    }
    public float getBalance(){
        return balance;
    }

    public int getNumRecipients(){
        return recipient.size();
    }

    public Recipient getRecipient(int index){
        if(index<recipient.size())
            return recipient.get(index);
        else
            return null;
    }

    public int getNumPendingTransfers(){
        return pendingTransfer.size();
    }

    public Transfer getTransfer(int index){
        if (index<pendingTransfer.size())
            return pendingTransfer.get(index);
        else
            return null;
    }

    public List<Transfer> getPendingTransferList(){
        return pendingTransfer;
    }

    public void addRecipient(Recipient newRecipient){
        recipient.add(newRecipient);
    }
    public void addPendingTransaction(Transfer newTransfer){
        pendingTransfer.add(newTransfer);
    }
}
