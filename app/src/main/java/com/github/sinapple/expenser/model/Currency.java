package com.github.sinapple.expenser.model;

import com.orm.SugarRecord;

/**
 * Represents currency that's used in transactions, default currency is Euro
 */
public class Currency extends SugarRecord{
    private String mFullName;
    private String mShortName;
    private Character mSign;

    public Currency() {
    }

    //public final static Currency EUR = new Currency("Euro", "EUR", '\u20AC');

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

    public Character getSign() {
        return mSign;
    }

    public void setSign(Character sign) {
        mSign = sign;
    }

    //Constructors
    public Currency(String fullName, String shortName) {
        mFullName = fullName;
        mShortName = shortName;
        mSign = null;
    }

    public Currency(String fullName, String shortName, Character sign) {
        mFullName = fullName;
        mShortName = shortName;
        mSign = sign;
    }
    @Override
    public String toString() {
        return "mFullName: "+mFullName+" "+"mShortName: " +  mShortName+" " +"mSign: "+ mSign+"\n";
    }
}
