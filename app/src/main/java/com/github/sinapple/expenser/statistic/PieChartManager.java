package com.github.sinapple.expenser.statistic;

import android.text.SpannableString;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.formatter.PercentFormatter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Jarvis on 05.05.2016.
 */
public class PieChartManager {
    private List<StatisticItem> statisticItemList;
    private ColorsListCreator colorsCreator;
    private PieChart pieChart;
    private List<String> xVals;
    private List<Entry> yVals;

    public PieChartManager(PieChart pieChart, List<StatisticItem> statisticList, String centerText) {
        this.statisticItemList = statisticList;
        this.pieChart = pieChart;

        colorsCreator = new ColorsListCreator();

        initXVals();
        initYVals();
        generateCenterSpannableText(centerText);
    }

    public void initPieChart() {

        PieDataSet pieDataSet = new PieDataSet(yVals, "");

        pieDataSet.setSliceSpace(5f);
        pieDataSet.setSelectionShift(5f);
        pieDataSet.setColors(colorsCreator.getColors());

        PieData pieData = new PieData(xVals, pieDataSet);
        pieData.setValueFormatter(new PercentFormatter());

        pieChart.setDescription("");    // Hide the description
        pieChart.getLegend().setEnabled(false);

        pieChart.animateY(2000);
        pieChart.setData(pieData);
        pieChart.invalidate();
    }

    private void initXVals(){
        xVals = new ArrayList<>();

        for(int i = 0; i < statisticItemList.size(); i++){
            xVals.add(statisticItemList.get(i).getCategory().getName());
        }

    }

    private void initYVals(){
        yVals = new ArrayList<>();

        float sum = 0;
        for (StatisticItem statisticItem : statisticItemList) {
            sum += statisticItem.getAmount();
        }

        for(int i = 0; i < statisticItemList.size(); i++) {
            float percent;
            if (sum == 0) {
                percent = 0;
            } else {
                percent = (float) statisticItemList.get(i).getAmount() * 100 / sum;
            }
            Entry entry = new Entry(percent, i);
            yVals.add(entry);
        }

    }

    private void generateCenterSpannableText(String text) {
        SpannableString s = new SpannableString(text);
        pieChart.setCenterText(s);
    }
}
