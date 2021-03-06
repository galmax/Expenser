package com.github.sinapple.expenser.model;

import com.orm.SugarRecord;

/**
 * Class represents category of either expense or income
 */
public class TransactionCategory extends SugarRecord {
    private String mName;
    private String mDescription;
    private boolean mExpenseCategory;

    public TransactionCategory() {
    }

    //Getters and Setters
    public String getDescription() {
        return mDescription;
    }

    public void setDescription(String description) {
        mDescription = description;
    }

    public String getName() {
        return mName;
    }

    public void setName(String name) {
        mName = name;
    }

    public boolean isExpenseCategory() {
        return mExpenseCategory;
    }

    public void setExpenseCategory(boolean expenseCategory) {
        mExpenseCategory = expenseCategory;
    }

    public TransactionCategory(String name, String description, boolean expenseCategory) {
        mName = name;
        mDescription = description;
        mExpenseCategory = expenseCategory;
    }

    @Override
    public String toString() {
        return mName;
    }

    @Override
    public boolean equals(Object obj) {
        if(this == obj) return true;
        if(obj == null) return false;
        if(!(obj.getClass() == this.getClass())) return false;
        else {
            TransactionCategory category = (TransactionCategory) obj;
            if(category.getId() == this.getId()) return true;
        }
        return false;
    }
}
