package com.github.sinapple.expenser.statistic;

import com.github.sinapple.expenser.model.MoneyTransaction;
import com.github.sinapple.expenser.model.TransactionCategory;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by Jarvis on 05.05.2016.
 */
public class SDataCreator {
    private List<TransactionCategory> allCategories;
    private List<MoneyTransaction> allMoneyTransaction;
    private List<StatisticItem> statisticList;
    private ColorsListCreator colors;

    public SDataCreator() {
        allCategories = TransactionCategory.listAll(TransactionCategory.class);
        allMoneyTransaction = MoneyTransaction.listAll(MoneyTransaction.class);
        statisticList = new ArrayList<>();
        colors = new ColorsListCreator();
    }

    public List<StatisticItem> getStatisticList(boolean isExpense){
        statisticList.clear();
        List<TransactionCategory> categories = selectCategories(isExpense);

        for (int i = 0; i < categories.size(); i++) {
            StatisticItem item = new StatisticItem();
            item.setCategory(categories.get(i));
            item.setColor(colors.getColors().get(i));

            float amount = 0;
            for (MoneyTransaction transaction : allMoneyTransaction) {
                if(transaction.getCategory().equals(categories.get(i))){
                    amount += transaction.getAmount();
                }
            }

            item.setAmount(amount);
            statisticList.add(item);
        }

        return statisticList;
    }

    public List<StatisticItem> getStatisticList(boolean isExpense, Date fromDate){
        statisticList.clear();
        List<TransactionCategory> categories = selectCategories(isExpense);
        for (int i = 0; i < categories.size(); i++) {
            StatisticItem item = new StatisticItem();
            item.setCategory(categories.get(i));
            item.setColor(colors.getColors().get(i));

            float amount = 0;
            for (MoneyTransaction transaction : allMoneyTransaction) {
                if(transaction.getCategory().equals(categories.get(i))){
                    amount += transaction.getAmount();
                }
            }
            item.setAmount(amount);

            statisticList.add(item);
        }

        return statisticList;
    }






    private List<TransactionCategory> selectCategories(boolean isExpenseStatistic){

        List<TransactionCategory> selectedCategories = new ArrayList<>();

        for (TransactionCategory category : allCategories) {
            if (category.isExpenseCategory() == isExpenseStatistic) {
                selectedCategories.add(category);
            }
        }
        return selectedCategories;
    }
}
