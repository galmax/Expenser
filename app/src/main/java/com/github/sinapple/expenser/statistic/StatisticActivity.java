package com.github.sinapple.expenser.statistic;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.formatter.PercentFormatter;
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
    private StatisticAdapter adapter;
    //
    private PieChart pieChart;
    private ListView categoriesListView;
    private ImageButton cancelButton;
    private ImageButton optionButton;
    private Button fromDateButton;
    private Button toDateButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_statistic);

        // initialize fields
        fromDate = null;
        toDate = null;
        isExpenseStatistic = true;
        allCategories = TransactionCategory.listAll(TransactionCategory.class);
        allMoneyTransaction = MoneyTransaction.listAll(MoneyTransaction.class);
        statisticList = new ArrayList<>();

        pieChart = (PieChart) findViewById(R.id.statistic_pieChart);
        categoriesListView = (ListView) findViewById(R.id.statistic_category_listView);
        cancelButton = (ImageButton) findViewById(R.id.statistic_panel_cancel_button);
        optionButton = (ImageButton) findViewById(R.id.statistic_panel_option_button);
        fromDateButton = (Button) findViewById(R.id.statistic_panel_fromDateButton);
        toDateButton = (Button) findViewById(R.id.statistic_panel_toDateButton);

        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fromDate = null;
                toDate = null;
                refreshViews();
            }
        });
        optionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isExpenseStatistic){
                    isExpenseStatistic = false;
                    optionButton.setImageResource(R.drawable.ic_menu_incomes);
                    refreshViews();

                } else {
                    isExpenseStatistic = true;
                    optionButton.setImageResource(R.drawable.ic_menu_expenses);
                    refreshViews();
                }
            }
        });
        


        initColorsList();
        initStatisticList();
        initPieChart();

        adapter = new StatisticAdapter(this, R.layout.statistic_item, statisticList);
        categoriesListView.setAdapter(adapter);

    }

    private void refreshViews() {

        initStatisticList();
        adapter.notifyDataSetChanged();
        initPieChart();
    }

    private void initPieChart() {
        List<String> xVals = new ArrayList<>();
        List<Entry> yVals = new ArrayList<>();

        float sum = 0;
        for (StatisticItem statisticItem : statisticList) {
            sum += statisticItem.getAmount();
        }

        for(int i = 0; i < statisticList.size(); i++){
            float percent;
            if(sum == 0) {
                percent = 0;
            } else{
                percent = (float)statisticList.get(i).getAmount()*100/sum;
            }
            Entry entry = new Entry(percent, i);
            yVals.add(entry);
        }

        for(int i = 0; i < statisticList.size(); i++){
            xVals.add(statisticList.get(i).getCategory().getName());
        }

        PieDataSet pieDataSet = new PieDataSet(yVals, "");
        pieDataSet.setSliceSpace(5f);
        pieDataSet.setSelectionShift(5f);
        pieDataSet.setColors(colors);
        PieData pieData = new PieData(xVals, pieDataSet);
        pieData.setValueFormatter(new PercentFormatter());

        pieChart.setDescription("");    // Hide the description
        pieChart.getLegend().setEnabled(false);
        pieChart.animateY(2000);
        pieChart.setData(pieData);
        pieChart.invalidate();
    }



    private void initStatisticList() {
        statisticList.clear();
        List<TransactionCategory> categories = new ArrayList<>();
        for (TransactionCategory category : allCategories) {
            if (category.isExpenseCategory() == isExpenseStatistic) {
                categories.add(category);
            }
        }

        if (fromDate != null && toDate != null) {
            for (int i = 0; i < categories.size(); i++) {
                StatisticItem item = new StatisticItem();
                item.setCategory(categories.get(i));
                item.setColor(colors.get(i));
                item.setAmount(getAmountForCategoryAndDate(categories.get(i)));
                statisticList.add(item);
            }
        } else if (fromDate != null) {
            for (int i = 0; i < categories.size(); i++) {
                StatisticItem item = new StatisticItem();
                item.setCategory(categories.get(i));
                item.setColor(colors.get(i));
                item.setAmount(getAmountForCategoryFromDate(categories.get(i)));
                statisticList.add(item);
            }

        } else if (toDate != null) {
            for (int i = 0; i < categories.size(); i++) {
                StatisticItem item = new StatisticItem();
                item.setCategory(categories.get(i));
                item.setColor(colors.get(i));
                item.setAmount(getAmountForCategoryToDate(categories.get(i)));
                statisticList.add(item);
            }
        } else {
            for (int i = 0; i < categories.size(); i++) {
                StatisticItem item = new StatisticItem();
                item.setCategory(categories.get(i));
                item.setColor(colors.get(i));
                item.setAmount(getAmountForCategory(categories.get(i)));
                statisticList.add(item);
            }
        }
    }

    private float getAmountForCategoryToDate(TransactionCategory transactionCategory) {
        float result = 0;
        for (MoneyTransaction transaction : allMoneyTransaction) {
            if(transaction.getCategory().equals(transactionCategory)
                    && transaction.getDate().compareTo(toDate) <= 0){

                result+= transaction.getAmount();
            }
        }
        return result;
    }

    private float getAmountForCategoryFromDate(TransactionCategory transactionCategory) {
        float result = 0;
        for (MoneyTransaction transaction : allMoneyTransaction) {
            if(transaction.getCategory().equals(transactionCategory)
                    && transaction.getDate().compareTo(fromDate) >= 0){

                result+= transaction.getAmount();
            }
        }
        return result;
    }

    private float getAmountForCategoryAndDate(TransactionCategory transactionCategory) {
        float result = 0;
        for (MoneyTransaction transaction : allMoneyTransaction) {
            if(transaction.getCategory().equals(transactionCategory)
                    && transaction.getDate().compareTo(fromDate) >= 0
                    && transaction.getDate().compareTo(toDate) <= 0){

                result+= transaction.getAmount();
            }
        }
        return result;

    }

    private float getAmountForCategory(TransactionCategory transactionCategory) {
        float result = 0;
        for (MoneyTransaction transaction : allMoneyTransaction) {
            if(transaction.getCategory().equals(transactionCategory)){
                result+= transaction.getAmount();
            }
        }
        return result;
    }


    private void initColorsList() {
        colors = new ArrayList<>();
        for (int c : ColorTemplate.COLORFUL_COLORS)
            colors.add(c);
        for (int c : ColorTemplate.LIBERTY_COLORS)
            colors.add(c);
        for (int c : ColorTemplate.PASTEL_COLORS)
            colors.add(c);
        for (int c : ColorTemplate.VORDIPLOM_COLORS)
            colors.add(c);
        for (int c : ColorTemplate.JOYFUL_COLORS)
            colors.add(c);
        colors.add(ColorTemplate.getHoloBlue());
    }
}

