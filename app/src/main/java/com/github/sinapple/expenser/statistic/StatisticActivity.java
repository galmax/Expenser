package com.github.sinapple.expenser.statistic;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.github.mikephil.charting.charts.PieChart;
import com.github.sinapple.expenser.MainActivity;
import com.github.sinapple.expenser.R;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * Created by Jarvis on 02.05.2016.
 */
public class StatisticActivity extends AppCompatActivity {

    private List<StatisticItem> statistic_list;
    private PieChartManager pieChartManager;
    private boolean isExpenseStatistic;
    private SDataCreator dataCreator;
    private StatisticAdapter adapter;
    private Date fromDate;
    private Date toDate;

    private LinearLayout categoryListLLayout;
    private PieChart pieChart;
    private ListView categoriesListView;
    private ImageButton cancelButton;
    private ImageButton optionButton;
    private Button fromDateButton;
    private Button toDateButton;
    private TextView noDataTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_statistic);

        categoryListLLayout = (LinearLayout) findViewById(R.id.category_list_linearLayout);
        pieChart = (PieChart) findViewById(R.id.statistic_pieChart);
        categoriesListView = (ListView) findViewById(R.id.statistic_category_listView);
        cancelButton = (ImageButton) findViewById(R.id.statistic_panel_cancel_button);
        optionButton = (ImageButton) findViewById(R.id.statistic_panel_option_button);
        fromDateButton = (Button) findViewById(R.id.statistic_panel_fromDateButton);
        toDateButton = (Button) findViewById(R.id.statistic_panel_toDateButton);
        noDataTextView = (TextView) findViewById(R.id.no_data_textView);

        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(fromDate != null || toDate != null) {
                    fromDate = null;
                    fromDateButton.setText(R.string.default_date_button_text);
                    toDate = null;
                    toDateButton.setText(R.string.default_date_button_text);
                    showStatistic();
                }
                else Snackbar.make(v, R.string.isDefaultDate, Snackbar.LENGTH_SHORT).show();
            }
        });
        optionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isExpenseStatistic) {
                    isExpenseStatistic = false;
                    optionButton.setImageResource(R.drawable.ic_menu_incomes);
                    showStatistic();
                } else {
                    isExpenseStatistic = true;
                    optionButton.setImageResource(R.drawable.ic_menu_expenses);
                    showStatistic();
                }
            }
        });
        fromDateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final View mView = v;
                final Calendar calendar = Calendar.getInstance(Locale.getDefault());
                calendar.set(Calendar.HOUR, 0);
                calendar.set(Calendar.MINUTE, 0);
                calendar.set(Calendar.SECOND, 0);

                DatePickerDialog.OnDateSetListener listener = new DatePickerDialog.OnDateSetListener(){
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        calendar.set(Calendar.YEAR, year);
                        calendar.set(Calendar.MONTH, monthOfYear);
                        calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);

                        if(toDate == null || toDate.getTime() >= calendar.getTimeInMillis()){
                            fromDate = new Date();
                            fromDate.setTime(calendar.getTimeInMillis());
                            fromDateButton.setText(dayOfMonth + "." + monthOfYear + "." + year);

                            showStatistic();
                        }
                        else Snackbar.make(mView, R.string.incorrect_date_selected, Snackbar.LENGTH_SHORT).show();
                    }
                };
                new DatePickerDialog(
                        v.getContext(),
                        listener,
                        calendar.get(Calendar.YEAR),
                        calendar.get(Calendar.MONTH),
                        calendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });
        toDateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final View mView = v;
                final Calendar calendar = Calendar.getInstance(Locale.getDefault());
                calendar.set(Calendar.HOUR, 23);
                calendar.set(Calendar.MINUTE, 59);
                calendar.set(Calendar.SECOND, 59);

                DatePickerDialog.OnDateSetListener listener = new DatePickerDialog.OnDateSetListener(){
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        calendar.set(Calendar.YEAR, year);
                        calendar.set(Calendar.MONTH, monthOfYear);
                        calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);

                        if(fromDate == null || fromDate.getTime() <= calendar.getTimeInMillis()) {

                            toDate = new Date();
                            toDate.setTime(calendar.getTimeInMillis());
                            toDateButton.setText(dayOfMonth + "." + monthOfYear + "." + year);

                            showStatistic();
                        }
                        else Snackbar.make(mView, R.string.incorrect_date_selected, Snackbar.LENGTH_SHORT).show();
                    }
                };
                new DatePickerDialog(
                        v.getContext(),
                        listener,
                        calendar.get(Calendar.YEAR),
                        calendar.get(Calendar.MONTH),
                        calendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        // initialize start date
        initStartActivityData();
        showStatistic();
    }

    private void initStartActivityData() {
        dataCreator = new SDataCreator();
        isExpenseStatistic = true;
        fromDate = null;
        toDate = null;

        statistic_list = dataCreator.getStatisticList(isExpenseStatistic);
        adapter = new StatisticAdapter(this, R.layout.statistic_item, statistic_list);
        categoriesListView.setAdapter(adapter);

    }

    private void showStatistic() {
        if(fromDate == null && toDate == null){
            statistic_list = dataCreator.getStatisticList(isExpenseStatistic);
        }
        else if(fromDate != null && toDate != null){
            statistic_list = dataCreator.getStatisticList(isExpenseStatistic, fromDate, toDate);
        }
        else if(fromDate != null){
            statistic_list = dataCreator.getStatisticListFromSomeDate(isExpenseStatistic, fromDate);
        }
        else if (toDate != null){
            statistic_list = dataCreator.getStatisticListToSomeDate(isExpenseStatistic, toDate);
        }

        pieChartManager =
                new PieChartManager(
                        pieChart,
                        statistic_list,
                        isExpenseStatistic ? "Expense\nStatistic" : "Income\nStatistic");

        pieChartManager.initPieChart();
        adapter.notifyDataSetChanged();

        displayDataOrNot();
    }

    private void displayDataOrNot() {
        if (statistic_list.isEmpty()) {
            categoryListLLayout.setVisibility(View.INVISIBLE);
            pieChart.setVisibility(View.INVISIBLE);

            noDataTextView.setVisibility(View.VISIBLE);
        }
        else {
            noDataTextView.setVisibility(View.INVISIBLE);
            categoryListLLayout.setVisibility(View.VISIBLE);
            pieChart.setVisibility(View.VISIBLE);
        }
    }
}

