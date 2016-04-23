package com.github.sinapple.expenser.model;

import java.util.Date;

/*
 * Represents either expense or income
 */
public class Transaction {
    private String mName;
    private TransactionCategory mCategory;
    private Wallet mWallet;
    private String mDescription;
    private float mAmount;
    private Date mDate;

    //Getters and setters
    public String getName() {
        return mName;
    }

    public void setName(String mName) {
        this.mName = mName;
    }

    public TransactionCategory getCategory() {
        return mCategory;
    }

    public void setCategory(TransactionCategory mCategory) {
        this.mCategory = mCategory;
    }

    public Wallet getWallet() {
        return mWallet;
    }

    public void setWallet(Wallet wallet) {
        mWallet = wallet;
    }

    public float getAmount() {
        return Math.abs(mAmount);
    }

    public void setAmount(float mAmount) {
        this.mAmount = mAmount;
    }

    public String getDescription() {
        return mDescription;
    }

    public void setDescription(String mDescription) {
        this.mDescription = mDescription;
    }

    public Date getDate() {
        return mDate;
    }

    public void setDate(Date date) {
        mDate = date;
    }

    //Constructors

    //Default constructor is for ORM DB
    public Transaction(){}

    public Transaction(String name, TransactionCategory category, Wallet wallet, String description, float amount, Date date) {
        mName = name;
        mCategory = category;
        mWallet = wallet;
        mDescription = description;
        mAmount = amount;
        mDate = date;
    }

    public Transaction(String name, TransactionCategory category, Wallet wallet, String description, float amount) {
        mName = name;
        mCategory = category;
        mWallet = wallet;
        mDescription = description;
        mAmount = amount;
        mDate = new Date();
    }

    public Transaction(String name, TransactionCategory category, Wallet wallet, float amount) {
        mName = name;
        mCategory = category;
        mWallet = wallet;
        mAmount = amount;
        mDate = new Date();
    }

    /*
     * Returns type of transaction
     * If amount of transaction is negative then it's expense, otherwise -- income
     */
    public boolean isExpense() {return mAmount < 0;}



}
