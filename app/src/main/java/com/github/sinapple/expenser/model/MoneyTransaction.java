package com.github.sinapple.expenser.model;

import com.orm.SugarRecord;

import java.util.Calendar;
import java.util.Date;
import java.util.List;


/*
 * Represents either expense or income
 */
public class MoneyTransaction extends SugarRecord {
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

    public float getRawAmount(){ return mAmount; }

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

    /*
    * Constructors
    * If you wanna create Transaction that represents expense, pass negative value into constructor,
    * otherwise to create income pass positive value
    */

    //Default constructor is for ORM DB
    public MoneyTransaction() {
    }

    public MoneyTransaction(String name, TransactionCategory category, Wallet wallet, String description, float amount, Date date) {
        mName = name;
        mCategory = category;
        mWallet = wallet;
        mDescription = description;
        mAmount = amount;
        mDate = date;
    }

    public MoneyTransaction(String name, TransactionCategory category, Wallet wallet, String description, float amount) {
        mName = name;
        mCategory = category;
        mWallet = wallet;
        mDescription = description;
        mAmount = amount;
        mDate = new Date();
    }

    public MoneyTransaction(String name, TransactionCategory category, Wallet wallet, float amount) {
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
    public boolean isExpense() {
        return mAmount < 0;
    }

    @Override
    public String toString() {
        return "mName: " + mName + " " + "mCategory: " + mCategory.getName() + " " + "mWallet: " + mWallet.getBalance() + " " + "mDescription: " + mDescription + " " + "mAmount: " + mAmount + " " + " mDate: " + mDate + "\n";
    }

    public static List<MoneyTransaction> findTransactionByDate(Calendar date, boolean lookForExpenses){
        setTimeToZero(date);
        Calendar intervalEnd = Calendar.getInstance();
        intervalEnd.setTimeInMillis(date.getTimeInMillis());
        return findTransactionByDate(date, intervalEnd, lookForExpenses);
    }

    //Returns all transactions that was created in specified date interval including scope
    public static List<MoneyTransaction> findTransactionByDate(Calendar first, Calendar second, boolean lookForExpenses){
        if (first.getTimeInMillis() > second.getTimeInMillis()) return null;
        second.add(Calendar.DAY_OF_MONTH, 1);
        return findTransactionByTime(first, second, lookForExpenses);
    }

    public static List<MoneyTransaction> findTransactionByTime(Calendar first, Calendar second, boolean lookForExpenses){
        if (first.getTimeInMillis() > second.getTimeInMillis()) return null;
        return MoneyTransaction.find(MoneyTransaction.class, "m_amount" + (lookForExpenses ? "<" : ">") + "? and m_wallet = ? and m_date > ? and m_date < ?", "0", Long.toString(Wallet.getCurrentWallet().getId()), Long.toString(first.getTimeInMillis()), Long.toString(second.getTimeInMillis()));
    }

    //Set time to zero for attaching all day to interval
    private static void setTimeToZero(Calendar date){
        date.set(Calendar.HOUR_OF_DAY, 0);
        date.set(Calendar.MINUTE, 0);
        date.set(Calendar.SECOND, 0);
        date.set(Calendar.MILLISECOND, 0);
    }
}
