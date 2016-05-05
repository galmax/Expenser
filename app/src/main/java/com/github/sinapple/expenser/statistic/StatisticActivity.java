package com.github.sinapple.expenser.statistic;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;

import com.github.mikephil.charting.utils.ColorTemplate;
import com.github.sinapple.expenser.R;
import com.github.sinapple.expenser.model.MoneyTransaction;
import com.github.sinapple.expenser.model.TransactionCategory;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by Jarvis on 02.05.2016.
 */
public class StatisticActivity extends AppCompatActivity {
    private List<StatisticItem> statisticList;
    private List<Integer> colors;
    private boolean isExpenseStatistic;
    List<TransactionCategory> allCategories;
    List<MoneyTransaction> allMoneyTransaction;


    private Date fromDate;
    private Date toDate;

    ListView categoriesList;
    ImageButton cancelButton;
    ImageButton optionButton;
    Button fromDateButton;
    Button toDateButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_statistic);

        fromDate = null;
        toDate = null;
        isExpenseStatistic = true;
        statisticList = new ArrayList<>();
        allCategories = TransactionCategory.listAll(TransactionCategory.class);
        allMoneyTransaction = MoneyTransaction.listAll(MoneyTransaction.class);

        categoriesList = (ListView) findViewById(R.id.statistic_category_listView);
        cancelButton = (ImageButton) findViewById(R.id.statistic_panel_cancel_button);
        optionButton = (ImageButton) findViewById(R.id.statistic_panel_option_button);
        fromDateButton = (Button) findViewById(R.id.statistic_panel_fromDateButton);
        toDateButton = (Button) findViewById(R.id.statistic_panel_toDateButton);

        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fromDate = null;
                toDate = null;
            }
        });


        initColorsList();
        initStatisticList();

        categoriesList.setAdapter(
                new StatisticAdapter(this, R.layout.statistic_item, statisticList));



    }



    private void initStatisticList() {
        List<TransactionCategory> categories = new ArrayList<>();
        for (TransactionCategory category : allCategories) {
            if (category.isExpenseCategory() == isExpenseStatistic) {
                categories.add(category);
            }
        }

        if (fromDate != null && toDate != null) {

        } else if (fromDate != null) {

        } else if (toDate != null) {

        } else {
            for (int i = 0; i < categories.size(); i++) {
                StatisticItem item = new StatisticItem();
                item.setCategory(categories.get(i));
                item.setColor(colors.get(i));
                item.setAmount(getAmountForCtegory(categories.get(i)));
                statisticList.add(item);
            }
        }
    }

    private float getAmountForCtegory(TransactionCategory transactionCategory) {
        float result = 0;
        for (MoneyTransaction transaction : allMoneyTransaction) {
            if(transaction.getCategory().equals(transactionCategory)){
                result+= transaction.getAmount();
            }
        }
        return (-1) * result;
    }


    private void initColorsList() {
        colors = new ArrayList<>();
        for (int c : ColorTemplate.VORDIPLOM_COLORS)
            colors.add(c);
        for (int c : ColorTemplate.JOYFUL_COLORS)
            colors.add(c);
        for (int c : ColorTemplate.COLORFUL_COLORS)
            colors.add(c);
        for (int c : ColorTemplate.LIBERTY_COLORS)
            colors.add(c);
        for (int c : ColorTemplate.PASTEL_COLORS)
            colors.add(c);
        colors.add(ColorTemplate.getHoloBlue());
    }
}

