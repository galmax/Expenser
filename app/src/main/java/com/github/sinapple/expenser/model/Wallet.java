package com.github.sinapple.expenser.model;

import com.orm.SugarRecord;

/**
 * Represents specific wallet with its own money balance, currency.
 * Every transaction has to be related with some wallet.
 */
public class Wallet extends SugarRecord{
    private String mName;
    private String mDescription;
    private float mBalance;
    private Currency mCurrency;

    public Wallet() {
    }

    //Getters and Setters
    public String getName() {
        return mName;
    }

    public void setName(String name) {
        mName = name;
    }

    public String getDescription() {
        return mDescription;
    }

    public void setDescription(String description) {
        mDescription = description;
    }

    public float getBalance() {
        return mBalance;
    }

    public void setBalance(float balance) {
        mBalance = balance;
    }

    public Currency getCurrency() {
        return mCurrency;
    }

    public void setCurrency(Currency currency) {
        mCurrency = currency;
    }

    //Constructors
    public Wallet(String name, String description, float balance, Currency currency) {
        mName = name;
        mDescription = description;
        mBalance = balance;
        mCurrency = currency;
    }

    public Wallet(String name, float balance, Currency currency) {
        mName = name;
        mBalance = balance;
        mCurrency = currency;
    }
    @Override
    public String toString() {
        return "mName: "+mName+" "+"mDescription: " +  mDescription+" " +"mBalance: "+ mBalance+" "+"mCurrency: "+ mCurrency.getShortName()+"\n";
    }
}
