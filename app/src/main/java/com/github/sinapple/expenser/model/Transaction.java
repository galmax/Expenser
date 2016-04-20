package com.github.sinapple.expenser.model;

import java.util.Date;

/**
 * Represents either expense or income
 */
public class Transaction {
    private String mName;
    private boolean mExpense;
    private TransactionCategory mCategory;
    private String mDescription;
    private float mAmount;
    private Currency mCurrency;
    private Date mDate;

    //Getters and setters
    public String getName() {
        return mName;
    }

    public void setName(String mName) {
        this.mName = mName;
    }

    public boolean isExpense() {return mExpense;}

    public TransactionCategory getCategory() {
        return mCategory;
    }

    public void setCategory(TransactionCategory mCategory) {
        this.mCategory = mCategory;
    }

    public float getAmount() {
        return mAmount;
    }

    public void setAmount(float mAmount) {
        this.mAmount = mAmount;
    }

    public Currency getCurrency() {
        return mCurrency;
    }

    public void setCurrency(Currency currency) {
        mCurrency = currency;
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

    public Transaction(String name, boolean isExpense, TransactionCategory category, String description, float amount, Currency currency, Date date){
        this.mName = name;
        this.mExpense = isExpense;
        this.mCategory = category;
        this.mDescription = description;
        this.mAmount = amount;
        this.mCurrency = currency;
        this.mDate = date;
    }

    public Transaction(String name, boolean isExpense, float amount, Currency currency, Date date){
        this.mName = name;
        this.mExpense = isExpense;
        this.mAmount = amount;
        this.mCurrency = currency;
        this.mDate = date;
    }
}
