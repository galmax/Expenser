package com.github.sinapple.expenser;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.View;
import android.widget.TextView;

import com.github.sinapple.expenser.model.TransactionCategory;

public class CategoryActivity extends AppCompatActivity {
TextView incomeListLabel,expenseListLabel;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category);
        setTitle("All categories");
        incomeListLabel=(TextView)findViewById(R.id.incomeListLabel);
        expenseListLabel=(TextView)findViewById(R.id.expenseListLabel);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent in = new Intent(CategoryActivity.this, NewCategoryActivity.class);
                in.putExtra("action", false);
                startActivity(in);
            }
        });

        //set up adapters for income and expense lists
        CustomCatListAdapter expenseListAdapter = new CustomCatListAdapter(TransactionCategory.find(TransactionCategory.class, "m_expense_category = ?", "1"));
        CustomCatListAdapter incomeListAdapter = new CustomCatListAdapter(TransactionCategory.find(TransactionCategory.class,"m_expense_category = ?","0"));
        RecyclerView expenseCategoryList = (RecyclerView)findViewById(R.id.expenseList);
        expenseCategoryList.setLayoutManager(new LinearLayoutManager(this));
        expenseCategoryList.setAdapter(expenseListAdapter);
        RecyclerView incomeCategoryList = (RecyclerView)findViewById(R.id.incomeList);
        incomeCategoryList.setLayoutManager(new LinearLayoutManager(this));
        incomeCategoryList.setAdapter(incomeListAdapter);

        //check the total number of items in lists and change the textViews accordingly
        if (expenseListAdapter.getItemCount()==0 && incomeListAdapter.getItemCount()==0) {
            expenseListLabel.setText("There are no categories to display. Please press the \"+\" button to add a new category.");
            //don't display the label below
            incomeListLabel.setText("");
        } else if (expenseListAdapter.getItemCount()==0){
            expenseListLabel.setText("There are no expense categories to display. Please press the \"+\" button to add a new category.");
        } else if (incomeListAdapter.getItemCount()==0){
            incomeListLabel.setText("There are no income categories to display. Please press the \"+\" button to add a new category.");
        }
    }
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;}

}
