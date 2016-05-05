package com.github.sinapple.expenser.model;

import com.orm.SugarRecord;

/**
 * Represents currency that's used in transactions, default currency is Euro
 */
public class Currency extends SugarRecord {
    private String mFullName;
    private String mShortName;
    private String mSign;

    public Currency() {
    }

    private static long defaultCurrencyId;

    //Getters and setters
    public String getFullName() {
        return mFullName;
    }

    public void setFullName(String mFullName) {
        this.mFullName = mFullName;
    }

    public String getShortName() {
        return mShortName;
    }

    public void setShortName(String shortName) {
        mShortName = shortName;
    }

    public String getSign() {
        return mSign;
    }

    public void setSign(String sign) {
        mSign = sign;
    }

    //Constructors
    public Currency(String fullName, String shortName) {
        mFullName = fullName;
        mShortName = shortName;
        mSign = null;
    }

    public Currency(String fullName, String shortName, String sign) {
        mFullName = fullName;
        mShortName = shortName;
        mSign = sign;
    }

    @Override
    public String toString() {
        return mSign == null ? mShortName : mSign;
    }

    public static Currency getDefaultCurrency(){
        Currency firstRecord = Currency.first(Currency.class);
        if (firstRecord == null){
            firstRecord = new Currency("Euro", "EUR", "\u20AC");
            firstRecord.save();
        }
        return firstRecord;
    }
}
