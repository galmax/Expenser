package com.github.sinapple.expenser.statistic;

import com.github.sinapple.expenser.model.TransactionCategory;

/**
 * Created by Jarvis on 04.05.2016.
 */
public class StatisticItem {
    private TransactionCategory category;
    private float amount;
    private Integer color;

    // Constructor
    public StatisticItem(){}
    //Suitable constructor
    public StatisticItem(TransactionCategory category, float amount, Integer color) {
        this.category = category;
        this.amount = amount;
        this.color = color;
    }
    // Getters and Setters
    public TransactionCategory getCategory() {
        return category;
    }

    public void setCategory(TransactionCategory category) {
        this.category = category;
    }

    public float getAmount() {
        return amount;
    }

    public void setAmount(float amount) {
        this.amount = amount;
    }

    public Integer getColor() {
        return color;
    }

    public void setColor(Integer color) {
        this.color = color;
    }
}
